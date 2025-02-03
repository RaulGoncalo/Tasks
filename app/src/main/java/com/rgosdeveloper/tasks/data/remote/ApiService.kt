package com.rgosdeveloper.tasks.data.remote

import com.rgosdeveloper.tasks.domain.models.SignInModel
import com.rgosdeveloper.tasks.domain.models.TaskModel
import com.rgosdeveloper.tasks.domain.models.UserModel
import com.rgosdeveloper.tasks.utils.ApiConstants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST(ApiConstants.Endpoints.SIGN_UP)
    suspend fun signUp(
        @Body user: UserModel
    ) : Response<Unit>

    @POST(ApiConstants.Endpoints.SIGN_IN)
    suspend fun signIn(
       @Body signInModel: SignInModel
    ) : Response<UserModel>

    @GET(ApiConstants.Endpoints.TASKS)
    suspend fun getTasks(
        @Query(ApiConstants.QUERY_DATE) date: String? = null
    ) : Response<List<TaskModel>>

    @POST(ApiConstants.Endpoints.TASKS)
    suspend fun salveTask(
        @Body task: TaskModel
    ) : Response<Unit>

    @DELETE(ApiConstants.Endpoints.TASKS_DELETE)
    suspend fun deleteTask(
        @Path(ApiConstants.PATH_ID) id: Int
    ) : Response<Unit>

    @PUT(ApiConstants.Endpoints.TASKS_PUT)
    suspend fun toggleTask(
        @Path(ApiConstants.PATH_ID) id: Int
    ) : Response<Unit>

}