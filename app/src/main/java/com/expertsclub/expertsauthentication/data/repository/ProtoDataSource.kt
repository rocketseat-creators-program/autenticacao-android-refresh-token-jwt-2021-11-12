package com.expertsclub.expertsauthentication.data.repository

import kotlinx.coroutines.flow.Flow

interface ProtoDataSource<T> {

    val preferencesFlow: Flow<T>

    suspend fun updateValue(value: T)

    suspend fun clearAll()
}