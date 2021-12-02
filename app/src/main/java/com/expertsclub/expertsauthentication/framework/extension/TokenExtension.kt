package com.expertsclub.expertsauthentication.framework.extension

import com.auth0.android.jwt.JWT
import com.expertsclub.expertsauthentication.framework.network.response.AuthResponse
import java.lang.Exception

fun AuthResponse.getUserIdFromAccessToken(): String = getAccessTokenJWT()?.getClaim("id")?.asString() ?: ""

fun String.isExpired() = toJwt()?.isExpired(0) ?: true

private fun AuthResponse.getAccessTokenJWT(): JWT? = accessToken?.toJwt()

private fun String.toJwt() = try {
    JWT(this)
} catch (exception: Exception) {
    null
}