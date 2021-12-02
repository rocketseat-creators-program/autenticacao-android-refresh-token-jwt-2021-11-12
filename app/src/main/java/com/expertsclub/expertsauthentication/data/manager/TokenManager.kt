package com.expertsclub.expertsauthentication.data.manager

import com.expertsclub.expertsauthentication.domain.model.TokenData
import kotlinx.coroutines.flow.Flow

interface TokenManager {

    suspend fun getTokenData(): Flow<TokenData>
    suspend fun saveTokenData(tokenData: TokenData)
    suspend fun clearTokenData()
}