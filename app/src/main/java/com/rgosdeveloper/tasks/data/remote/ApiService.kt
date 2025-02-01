package com.rgosdeveloper.tasks.data.remote

import com.rgosdeveloper.tasks.domain.UserModel
import com.rgosdeveloper.tasks.utils.ApiConstants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST(ApiConstants.Endpoints.SIGN_UP)
    suspend fun signUp(
        @Body user: UserModel
    ) : Response<Unit>

    @POST(ApiConstants.Endpoints.SIGN_IN)
    suspend fun signIn(
        @Body user: UserModel
    ) : Response<UserModel>
}