package com.expertsclub.expertsauthentication.data.repository

import com.expertsclub.expertsauthentication.framework.network.response.AuthResponse

interface AuthRemoteDataSource {

    suspend fun login(email: String, password: String): AuthResponse

    suspend fun refreshToken(refreshToken: String): AuthResponse
}