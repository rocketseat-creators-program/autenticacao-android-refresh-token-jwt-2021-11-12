package com.expertsclub.expertsauthentication.framework.network.datasource

import com.expertsclub.expertsauthentication.data.repository.AuthRemoteDataSource
import com.expertsclub.expertsauthentication.framework.network.AuthService
import com.expertsclub.expertsauthentication.framework.network.response.AuthResponse

class AuthRemoteDataSourceImpl(
    private val authService: AuthService
) : AuthRemoteDataSource {

    override suspend fun login(email: String, password: String): AuthResponse {
        return authService.login(email, password)
    }

    override suspend fun refreshToken(refreshToken: String): AuthResponse {
        return authService.refreshToken(refreshToken)
    }
}