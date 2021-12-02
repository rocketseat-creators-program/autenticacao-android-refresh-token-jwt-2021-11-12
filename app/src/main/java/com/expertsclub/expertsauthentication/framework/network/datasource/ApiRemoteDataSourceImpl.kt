package com.expertsclub.expertsauthentication.framework.network.datasource

import com.expertsclub.expertsauthentication.data.repository.ApiRemoteDataSource
import com.expertsclub.expertsauthentication.framework.network.ApiService
import com.expertsclub.expertsauthentication.framework.network.response.AuthResponse
import com.expertsclub.expertsauthentication.framework.network.response.UserResponse

class ApiRemoteDataSourceImpl(
    private val apiService: ApiService
) : ApiRemoteDataSource {

    override suspend fun getUser(id: String): UserResponse {
        return apiService.getUser(id)
    }
}