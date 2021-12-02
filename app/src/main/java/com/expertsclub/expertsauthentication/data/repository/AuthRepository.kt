package com.expertsclub.expertsauthentication.data.repository

import com.expertsclub.expertsauthentication.data.manager.TokenManager
import com.expertsclub.expertsauthentication.domain.model.TokenData
import com.expertsclub.expertsauthentication.framework.network.response.AuthResponse

class AuthRepository(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val tokenManager: TokenManager
) {

    suspend fun login(email: String, password: String): AuthResponse {
        return authRemoteDataSource.login(email, password)
    }

    suspend fun saveAccessToken(tokenData: TokenData) {
        tokenManager.saveTokenData(tokenData)
    }

    suspend fun clearAccessToken() {
        tokenManager.clearTokenData()
    }
}