package com.rgosdeveloper.tasks.presentation.adapters

import android.content.res.ColorStateList
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rgosdeveloper.tasks.R
import com.rgosdeveloper.tasks.databinding.ItemTaskBinding
import com.rgosdeveloper.tasks.domain.models.TaskModel
import com.rgosdeveloper.tasks.utils.AppConstants

class TaskAdapter(
    private val filter: String,
    private val deleteTask: (Int) -> Unit,
    private val toogleStatusTask: (Int) -> Unit,
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private val tasks = mutableListOf<TaskModel>()

    fun setTasks(newTasks: List<TaskModel>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }

    inner class TaskViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: TaskModel) {
            val isDone = task.doneAt != null

            binding.rbTasks.text = task.desc
            if (isDone) {
                binding.rbTasks.paintFlags =
                    binding.rbTasks.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                binding.txtDateTask.text = "De ${task.estimateAt} concluida em ${task.doneAt}"
            } else {
                binding.rbTasks.paintFlags =
                    binding.rbTasks.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                binding.txtDateTask.text = task.estimateAt
            }

            binding.btnDelete.setOnClickListener {
                deleteTask(task.id)
            }

            binding.rbTasks.setOnClickListener {
                toogleStatusTask(task.id)
            }

            binding.rbTasks.isChecked = isDone

            when (filter) {
                AppConstants.FILTER_TODAY -> {
                    binding.rbTasks.buttonTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.todayColor
                        )
                    )
                }

                AppConstants.FILTER_TOMORROW -> {
                    binding.rbTasks.buttonTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.tomorrowColor
                        )
                    )
                }

                AppConstants.FILTER_WEEK -> {
                    binding.rbTasks.buttonTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.weekColor
                        )
                    )
                }

                AppConstants.FILTER_MONTH -> {
                    binding.rbTasks.buttonTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            itemView.context, R.color.monthColor
                        )
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount(): Int = tasks.size
}