package com.expertsclub.expertsauthentication.framework.network.response

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("accessToken")
    val accessToken: String?,
    @SerializedName("refreshToken")
    val refreshToken: String?
)
