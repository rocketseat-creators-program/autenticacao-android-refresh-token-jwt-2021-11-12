package com.expertsclub.expertsauthentication.framework.network.interceptor

import com.expertsclub.expertsauthentication.data.manager.TokenManager
import com.expertsclub.expertsauthentication.data.repository.AuthRemoteDataSource
import com.expertsclub.expertsauthentication.domain.model.TokenData
import com.expertsclub.expertsauthentication.framework.HEADER_AUTHORIZATION
import com.expertsclub.expertsauthentication.framework.HEADER_BEARER
import com.expertsclub.expertsauthentication.framework.PATH_AUTH
import com.expertsclub.expertsauthentication.framework.extension.isExpired
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthInterceptor(
    private val tokenManager: TokenManager,
    private val authRemoteDataSource: AuthRemoteDataSource
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url

        val newRequest = if (!url.pathSegments.contains(PATH_AUTH)) {
            val tokenData = runBlocking { tokenManager.getTokenData().first() }
            val refreshToken = tokenData.refreshToken

            if (refreshToken != null) {
                val accessToken = if (tokenData.accessToken?.isExpired() == true) {
                    val authResponse = runBlocking {
                        authRemoteDataSource.refreshToken(refreshToken)
                    }

                    if (authResponse.accessToken != null && authResponse.refreshToken != null) {
                        runBlocking {
                            tokenManager.saveTokenData(
                                TokenData(authResponse.accessToken, authResponse.refreshToken)
                            )
                        }
                    }

                    authResponse.accessToken
                } else tokenData.accessToken

                accessToken?.getNewRequest(request) ?: request
            } else request
        } else request

        val response = chain.proceed(newRequest)

        if (response.code == 401) {
            val tokenData = runBlocking { tokenManager.getTokenData().first() }
            val refreshToken = tokenData.refreshToken

            val accessToken = if (refreshToken != null) {
                val authResponse = runBlocking {
                    authRemoteDataSource.refreshToken(tokenData.refreshToken)
                }

                if (authResponse.accessToken != null && authResponse.refreshToken != null) {
                    runBlocking {
                        tokenManager.saveTokenData(
                            TokenData(authResponse.accessToken, authResponse.refreshToken)
                        )
                    }
                }

                authResponse.accessToken
            } else null

            accessToken?.let {
                response.close()
                return chain.proceed(it.getNewRequest(request))
            }
        }

        return response
    }

    private fun String.getNewRequest(request: Request): Request = request.newBuilder()
        .header(HEADER_AUTHORIZATION, "$HEADER_BEARER $this")
        .build()
}