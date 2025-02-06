package com.rgosdeveloper.tasks.presentation.fragments

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.rgosdeveloper.tasks.R
import com.rgosdeveloper.tasks.domain.models.TaskRequestModel
import com.rgosdeveloper.tasks.databinding.FragmentTasksBinding
import com.rgosdeveloper.tasks.domain.common.ResultState
import com.rgosdeveloper.tasks.presentation.adapters.TaskAdapter
import com.rgosdeveloper.tasks.presentation.viewmodel.TaskViewModel
import com.rgosdeveloper.tasks.utils.AppConstants
import com.rgosdeveloper.tasks.utils.formatDate
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class TasksFragment : Fragment() {
    private lateinit var filter: String
    private lateinit var binding: FragmentTasksBinding
    private lateinit var adapter: TaskAdapter
    private lateinit var taskViewModel: TaskViewModel
    private val currentDate: String = getCurrentDate()


    companion object {
        fun getCurrentDate(): String {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LocalDate.now().toString()
            } else {
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Recupera o filtro do Bundle
        binding = FragmentTasksBinding.inflate(inflater, container, false)


        taskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]

        setArguments()
        setObservers()
        taskViewModel.getTasks(currentDate, filter)

        initViews()
        return binding.root
    }

    private fun setArguments() {
        filter = arguments?.getString(
            AppConstants.KEY_PUT_EXTRA_FRAGMENT, AppConstants.FILTER_TODAY
        ) ?: AppConstants.FILTER_TODAY
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
        binding.txtDay.text = formattedDate

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

        setColorOnViews()
        setupFab()
    }

    private fun setupFab() {
        binding.fabAddTask.setOnClickListener {
            openCustomDialog()
        }
    }

    private fun openCustomDialog() {
        val alertDialog = createCustomDialog()
        alertDialog?.show()
    }

    private fun createCustomDialog(): AlertDialog? {
        val viewDialog = LayoutInflater.from(context).inflate(R.layout.custom_dialog, null)
        val btnSave = viewDialog.findViewById<AppCompatButton>(R.id.btnSave)
        val editText = viewDialog.findViewById<TextInputEditText>(R.id.editTxtTask)
        val calendarView = viewDialog.findViewById<CalendarView>(R.id.cvCompletionDate)

        val alertDialog = context?.let {
            AlertDialog.Builder(it)
                .setView(viewDialog)
                .create()
        }
        alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        var selectedDate = currentDate

        // Atualiza a data quando o usuário seleciona uma nova
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = "$year-${month + 1}-$dayOfMonth"
        }

        btnSave.setOnClickListener {
            val inputText = editText.text.toString()
            if (inputText.isNotEmpty()) {
                alertDialog?.dismiss()
                handleSaveTask(inputText, selectedDate)
            } else {
                Toast.makeText(context, AppConstants.EMPTY_INPUT_MESSAGE, Toast.LENGTH_SHORT).show()
            }
        }
        return alertDialog
    }


    private fun handleSaveTask(inputText: String, selectedDate: String) {
        val task = TaskRequestModel(
            desc = inputText,
            estimateAt = selectedDate
        )
        taskViewModel.addTask(
            task,
            filter,
            currentDate
        )
    }

    private fun handleDeleteTask(idTask: Int) {
        context?.let {
            AlertDialog.Builder(it)
                .setTitle(AppConstants.TITLE_DELETE_TASK)
                .setMessage(AppConstants.MESSAGE_DELETE_TASK)
                .setNegativeButton(AppConstants.TXT_NEGATIVE_BUTTON) { dialog, posicao -> }
                .setPositiveButton(AppConstants.TXT_POSITIVE_BUTTON) { dialog, posicao ->
                    taskViewModel.deleteTask(idTask, currentDate, filter)
                    Toast.makeText(it, AppConstants.SUCCESS_MESSEGE_DELETE_TASK, Toast.LENGTH_SHORT)
                        .show()
                }
                .create()
                .show()
        }
    }

    private fun handleToggleTask(idTask: Int) {
        taskViewModel.toggleTask(idTask, currentDate, filter)
    }

    private fun setColorOnViews() {
        when (filter) {
            AppConstants.FILTER_TODAY -> {
                context.let {
                    binding.fabAddTask.backgroundTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(it!!, R.color.todayColor))
                }
                binding.backgroundImage.setImageResource(R.drawable.today)
                binding.txtFilter.text = AppConstants.TITLE_TODAY
            }

            AppConstants.FILTER_TOMORROW -> {
                context.let {
                    binding.fabAddTask.backgroundTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(it!!, R.color.tomorrowColor))
                }
                binding.backgroundImage.setImageResource(R.drawable.tomorrow)
                binding.txtFilter.text = AppConstants.TITLE_TOMORROW
            }

            AppConstants.FILTER_WEEK -> {
                context.let {
                    binding.fabAddTask.backgroundTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(it!!, R.color.weekColor))
                }
                binding.backgroundImage.setImageResource(R.drawable.week)
                binding.txtFilter.text = AppConstants.TITLE_WEEK
            }

            AppConstants.FILTER_MONTH -> {
                context.let {
                    binding.fabAddTask.backgroundTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(it!!, R.color.monthColor))
                }
                binding.backgroundImage.setImageResource(R.drawable.month)
                binding.txtFilter.text = AppConstants.TITLE_MONTH
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