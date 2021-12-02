package com.expertsclub.expertsauthentication.data.repository

import com.expertsclub.expertsauthentication.framework.network.response.UserResponse

interface ApiRemoteDataSource {

    suspend fun getUser(id: String): UserResponse
}