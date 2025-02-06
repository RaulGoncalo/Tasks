package com.rgosdeveloper.tasks.domain.usecase

import com.rgosdeveloper.tasks.domain.models.TaskRequestModel
import com.rgosdeveloper.tasks.data.repository.TaskRepository
import com.rgosdeveloper.tasks.domain.common.ResultState
import com.rgosdeveloper.tasks.domain.models.TaskModel
import com.rgosdeveloper.tasks.utils.AppConstants
import com.rgosdeveloper.tasks.utils.addDaysToDate
import com.rgosdeveloper.tasks.utils.formatDate
import javax.inject.Inject

class TaskUseCase @Inject constructor(
    private val respository: TaskRepository
) {
    suspend fun getTasks(todaysDate: String, filter: String): ResultState<List<TaskModel>> {
        var dateToRepository: String? = when {
            filter == AppConstants.FILTER_TODAY -> todaysDate
            filter == AppConstants.FILTER_TOMORROW -> addDaysToDate(
                todaysDate,
                AppConstants.DAY_TO_ADD_TOMORROW
            )

            filter == AppConstants.FILTER_WEEK -> addDaysToDate(
                todaysDate,
                AppConstants.DAY_TO_ADD_WEEK
            )

            filter == AppConstants.FILTER_MONTH -> addDaysToDate(
                todaysDate,
                AppConstants.DAY_TO_ADD_MONTH
            )

            else -> null
        }

        val tasksResult = respository.getTasks(dateToRepository)

        // Formatar datas das tarefas
        if (tasksResult is ResultState.Success) {
            tasksResult.data?.let { tasks ->
                tasks.forEach { task ->
                    task.estimateAt = formatDate(task.estimateAt) // Formata a data de estimateAt
                    task.doneAt?.let { doneAt ->
                        task.doneAt = formatDate(doneAt) // Formata a data de doneAt
                    }
                }
            }
        }

        return tasksResult
    }

    suspend fun toggleTask(id: Int) : ResultState<Unit>{
        return respository.toggleTask(id)
    }

    suspend fun deleteTask(id: Int) : ResultState<Unit>{
        return respository.deleteTask(id)
    }

    suspend fun addTask(task: TaskRequestModel) : ResultState<Unit>{
        return respository.addTask(task)
    }
}