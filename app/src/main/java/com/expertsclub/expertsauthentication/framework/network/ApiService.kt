package com.expertsclub.expertsauthentication.framework.network

import com.expertsclub.expertsauthentication.framework.BASE_URL
import com.expertsclub.expertsauthentication.framework.PATH_USERS
import com.expertsclub.expertsauthentication.framework.network.interceptor.AuthInterceptor
import com.expertsclub.expertsauthentication.framework.network.response.UserResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("$PATH_USERS/{id}")
    suspend fun getUser(@Path("id") id: String): UserResponse

    companion object {
        fun getService(authInterceptor: AuthInterceptor): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpClient(authInterceptor))
                .build()

            return retrofit.create(ApiService::class.java)
        }

        private fun getOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(
                    HttpLoggingInterceptor().apply {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    }
                )
                .build()
        }
    }
}

