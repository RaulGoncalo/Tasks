package com.rgosdeveloper.tasks.di

import com.rgosdeveloper.tasks.data.remote.ApiService
import com.rgosdeveloper.tasks.data.remote.AuthInterceptor
import com.rgosdeveloper.tasks.data.repository.AuthRepository
import com.rgosdeveloper.tasks.data.repository.TaskRepository
import com.rgosdeveloper.tasks.data.repository.UserPreferencesRepository
import com.rgosdeveloper.tasks.domain.usecase.AuthUseCase
import com.rgosdeveloper.tasks.domain.usecase.TaskUseCase
import com.rgosdeveloper.tasks.utils.ApiConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
@InstallIn(ViewModelComponent::class)
object ApiModule {
    @Provides
    fun provideAuthInterceptor(userPreferencesRepository: UserPreferencesRepository): Interceptor {
        return AuthInterceptor(userPreferencesRepository)
    }

    @Provides
    fun provideOkHttpClient(authInterceptor: Interceptor): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    @Provides
    fun provideRetrofitService(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(ApiConstants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    fun provideAuthRepository(apiService: ApiService): AuthRepository {
        return AuthRepository(apiService)
    }

    @Provides
    fun provideAuthUseCase(
        authRepository: AuthRepository,
        userPreferencesRepository: UserPreferencesRepository
    ): AuthUseCase {
        return AuthUseCase(authRepository, userPreferencesRepository)
    }

    @Provides
    fun provideTaskRepository(apiService: ApiService) : TaskRepository{
        return TaskRepository(apiService)
    }

    @Provides
    fun provideTaskUseCase(taskRepository: TaskRepository) : TaskUseCase{
        return TaskUseCase(taskRepository)
    }
}