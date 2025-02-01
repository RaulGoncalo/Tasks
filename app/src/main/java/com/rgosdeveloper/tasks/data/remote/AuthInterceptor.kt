package com.rgosdeveloper.tasks.data.remote

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwibmFtZSI6IlJhdWwiLCJlbWFpbCI6InJhdWxnb25jYWxvLjk4QGdtYWlsLmNvbSJ9.NHQVQaS-Ya-IUEwHMGRJWsS6CFaGVVCq4yZhXX6r88U"
        val requestBuilder = chain.request().newBuilder()

        val request = requestBuilder.addHeader(
            "Authorization",
            "Bearer token $TOKEN"
        ).build()

        return chain.proceed(request)
    }

}