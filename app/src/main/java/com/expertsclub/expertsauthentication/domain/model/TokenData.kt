package com.expertsclub.expertsauthentication.domain.model

data class TokenData(
    val accessToken: String?,
    val refreshToken: String?
)
