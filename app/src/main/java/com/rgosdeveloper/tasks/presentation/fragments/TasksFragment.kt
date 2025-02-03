package com.rgosdeveloper.tasks.presentation.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rgosdeveloper.tasks.R
import com.rgosdeveloper.tasks.databinding.FragmentTasksBinding
import com.rgosdeveloper.tasks.domain.common.ResultState
import com.rgosdeveloper.tasks.presentation.adapters.TaskAdapter
import com.rgosdeveloper.tasks.domain.models.TaskModel
import com.rgosdeveloper.tasks.presentation.activites.SigninActivity
import com.rgosdeveloper.tasks.presentation.viewmodel.TaskViewModel
import com.rgosdeveloper.tasks.utils.AppConstants
import com.rgosdeveloper.tasks.utils.MainConstants
import com.rgosdeveloper.tasks.utils.formatDate
import dagger.hilt.android.AndroidEntryPoint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class TasksFragment : Fragment() {
    private lateinit var filter: String
    private lateinit var binding: FragmentTasksBinding
    private lateinit var adapter: TaskAdapter
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var currentDate: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Recupera o filtro do Bundle
        binding = FragmentTasksBinding.inflate(inflater, container, false)

        filter =
            arguments?.getString(AppConstants.KEY_PUT_EXTRA_FRAGMENT, AppConstants.FILTER_TODAY)
                ?: AppConstants.FILTER_TODAY

        currentDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate.now().toString()
        } else {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            sdf.format(Date())
        }

        taskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        setObservers()
        taskViewModel.getTasks(currentDate, filter)

        initViews()
        return binding.root
    }

    private fun setObservers() {
        taskViewModel.tasks.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Success -> {
                    val tasks = result.data
                    adapter.setTasks(tasks)
                    hideLoading()
                }

                is ResultState.Error -> {
                    Toast.makeText(activity, result.exception.message, Toast.LENGTH_SHORT).show()
                    hideLoading()
                }

                ResultState.Loading -> {
                    showLoading()
                }
            }
        }
    }

    private fun initViews() {
        val formattedDate = formatDate(currentDate)
        binding.tvDay.text = formattedDate

        adapter = TaskAdapter(
            filter,
            { idTask ->
                handleDeleteTask(idTask)
            },
            { idTask ->
                handleToggleTask(idTask)
            }
        )

        binding.rvTasks.adapter = adapter
        binding.rvTasks.layoutManager = LinearLayoutManager(context)

        // Ajusta a imagem de fundo com base no filtro
        setBackgroundImg()
    }

    private fun handleDeleteTask(idTask: Int) {
        context?.let {
            AlertDialog.Builder(it)
                .setTitle(AppConstants.TITLE_DELETE_TASK)
                .setMessage(AppConstants.MESSAGE_DELETE_TASK)
                .setNegativeButton(AppConstants.TXT_NEGATIVE_BUTTON){ dialog, posicao -> }
                .setPositiveButton(AppConstants.TXT_POSITIVE_BUTTON) { dialog, posicao ->
                    taskViewModel.deleteTask(idTask, currentDate, filter)
                    Toast.makeText(it, AppConstants.SUCCESS_MESSEGE_DELETE_TASK, Toast.LENGTH_SHORT).show()
                }
                .create()
                .show()
        }


    }

    private fun handleToggleTask(idTask: Int) {
        taskViewModel.toggleTask(idTask, currentDate, filter)
    }

    private fun setBackgroundImg() {
        when (filter) {
            AppConstants.FILTER_TODAY -> {
                binding.backgroundImage.setImageResource(R.drawable.today)
                binding.tvFilter.text = "Hoje"
            }

            AppConstants.FILTER_TOMORROW -> {
                binding.backgroundImage.setImageResource(R.drawable.tomorrow)
                binding.tvFilter.text = "Amanhã"
            }

            AppConstants.FILTER_WEEK -> {
                binding.backgroundImage.setImageResource(R.drawable.week)
                binding.tvFilter.text = "Semana"

            }

            AppConstants.FILTER_MONTH -> {
                binding.backgroundImage.setImageResource(R.drawable.month)
                binding.tvFilter.text = "Mês"
            }
        }
    }

    private fun showLoading() {
        binding.pbLoading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.pbLoading.visibility = View.GONE
    }

}