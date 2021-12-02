package com.expertsclub.expertsauthentication.framework.network

import com.expertsclub.expertsauthentication.framework.BASE_URL
import com.expertsclub.expertsauthentication.framework.PATH_AUTH
import com.expertsclub.expertsauthentication.framework.PATH_REFRESH_TOKEN
import com.expertsclub.expertsauthentication.framework.network.response.AuthResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthService {

    @FormUrlEncoded
    @POST(PATH_AUTH)
    suspend fun login(
        @Field("email")
        email: String,
        @Field("password")
        password: String
    ): AuthResponse

    @FormUrlEncoded
    @POST(PATH_REFRESH_TOKEN)
    suspend fun refreshToken(
        @Field("refreshToken")
        refreshToken: String
    ): AuthResponse

    companion object {
        fun getService(): AuthService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpClient())
                .build()

            return retrofit.create(AuthService::class.java)
        }

        private fun getOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor(
                    HttpLoggingInterceptor().apply {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    }
                )
                .build()
        }
    }
}