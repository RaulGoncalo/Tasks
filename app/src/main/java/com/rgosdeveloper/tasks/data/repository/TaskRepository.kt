package com.rgosdeveloper.tasks.data.repository

import android.util.Log
import com.rgosdeveloper.tasks.data.remote.ApiService
import com.rgosdeveloper.tasks.domain.common.ResultState
import com.rgosdeveloper.tasks.domain.models.TaskModel
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getTasks(date: String?) : ResultState<List<TaskModel>>{
        return try {
            val response = apiService.getTasks(date)

            if(response.isSuccessful() && response.code() == 200){
                val tasks = response.body()
                if(tasks != null){
                    ResultState.Success(tasks)
                }else{
                    Log.i("app_tasks", "Nenhuma tarefa encontrada")
                    ResultState.Error(Exception("Nenhuma tarefa encontrada"))
                }
            }else{
                Log.i("app_tasks", "CODIGO RESPONSE: ${response.code()}")
                ResultState.Error(Exception("Erro ao buscar tarefas"))
            }
        }catch (e: Exception){
            Log.i("app_tasks", "${e.message}")
            ResultState.Error(e)
        }
    }
}