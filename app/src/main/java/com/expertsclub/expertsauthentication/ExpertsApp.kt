package com.expertsclub.expertsauthentication

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.expertsclub.expertsauthentication.framework.preferences.serializer.AuthPreferencesSerializer

class ExpertsApp : Application() {

    val authDataStore: DataStore<AuthPreferences> by dataStore(
        fileName = "auth.pb",
        serializer = AuthPreferencesSerializer
    )
    val localDataStore: DataStore<Preferences> by preferencesDataStore(name = "localStore")
}