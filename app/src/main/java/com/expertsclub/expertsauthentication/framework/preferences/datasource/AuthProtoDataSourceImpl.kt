package com.expertsclub.expertsauthentication.framework.preferences.datasource

import androidx.datastore.core.DataStore
import com.expertsclub.expertsauthentication.AuthPreferences
import com.expertsclub.expertsauthentication.data.repository.ProtoDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException

class AuthProtoDataSourceImpl(
    private val dataStore: DataStore<AuthPreferences>
) : ProtoDataSource<AuthPreferences> {

    override val preferencesFlow: Flow<AuthPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(AuthPreferences.getDefaultInstance())
            } else throw exception
        }

    override suspend fun updateValue(value: AuthPreferences) {
        dataStore.updateData { authPreferences ->
            authPreferences.toBuilder()
                .setAccessToken(value.accessToken)
                .setRefreshToken(value.refreshToken)
                .build()
        }
    }

    override suspend fun clearAll() {
        dataStore.updateData {
            it.toBuilder()
                .clear()
                .build()
        }
    }
}