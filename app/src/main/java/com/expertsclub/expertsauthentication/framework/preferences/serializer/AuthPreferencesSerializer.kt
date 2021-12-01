package com.expertsclub.expertsauthentication.framework.preferences.serializer

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.expertsclub.expertsauthentication.AuthPreferences
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

object AuthPreferencesSerializer : Serializer<AuthPreferences> {
    override val defaultValue: AuthPreferences = AuthPreferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): AuthPreferences {
        try {
            return AuthPreferences.parseFrom(input)
        } catch (exception: IOException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: AuthPreferences, output: OutputStream) = t.writeTo(output)

}