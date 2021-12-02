package com.expertsclub.expertsauthentication.framework.network.manager

import com.expertsclub.expertsauthentication.AuthPreferences
import com.expertsclub.expertsauthentication.data.manager.TokenManager
import com.expertsclub.expertsauthentication.data.repository.PreferencesDataSource
import com.expertsclub.expertsauthentication.data.repository.ProtoDataSource
import com.expertsclub.expertsauthentication.domain.model.TokenData
import com.expertsclub.expertsauthentication.framework.PREF_KEY_ACCESS_TOKEN
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TokenManagerImpl(
    private val protoDataSource: ProtoDataSource<AuthPreferences>
) : TokenManager {

    override suspend fun getTokenData(): Flow<TokenData> {
        return protoDataSource.preferencesFlow.map {
            TokenData(it.accessToken, it.refreshToken)
        }
    }

    override suspend fun saveTokenData(tokenData: TokenData) {
        AuthPreferences.newBuilder()
            .setAccessToken(tokenData.accessToken)
            .setRefreshToken(tokenData.refreshToken)
            .build().run {
                protoDataSource.updateValue(this)
            }
    }

    override suspend fun clearTokenData() {
        protoDataSource.clearAll()
    }
}