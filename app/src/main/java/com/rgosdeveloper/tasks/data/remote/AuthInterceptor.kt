package com.rgosdeveloper.tasks.data.remote

import com.rgosdeveloper.tasks.data.repository.UserPreferencesRepository
import com.rgosdeveloper.tasks.domain.common.ResultState
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val userPreferencesRepository: UserPreferencesRepository
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            when (val result = userPreferencesRepository.getUserPreferences()) {
                is ResultState.Success -> result.data.token
                else -> null
            }
        }

        val requestBuilder = chain.request().newBuilder()

        if (!token.isNullOrEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        val request = requestBuilder.build()

        return chain.proceed(request)
    }
}