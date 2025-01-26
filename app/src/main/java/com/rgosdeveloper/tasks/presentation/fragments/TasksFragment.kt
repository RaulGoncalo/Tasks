package com.rgosdeveloper.tasks.presentation.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.rgosdeveloper.tasks.R
import com.rgosdeveloper.tasks.databinding.FragmentTasksBinding
import com.rgosdeveloper.tasks.presentation.adapters.TaskAdapter
import com.rgosdeveloper.tasks.domain.TaskModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TasksFragment : Fragment() {
    private lateinit var filter: String
    private lateinit var binding: FragmentTasksBinding
    private lateinit var adapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Recupera o filtro do Bundle
        binding = FragmentTasksBinding.inflate(inflater, container, false)
        filter = arguments?.getString("filter", "today") ?: "today"


        initViews()

        // Aqui você pode ajustar o RecyclerView conforme o filtro
        loadTasksBasedOnFilter(filter)

        return binding.root
    }
    

    private fun initViews() {
        val currentDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate.now()
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val formattedDate = currentDate.format(formatter)

        binding.tvDay.text = formattedDate.toString()

        val tarefas = mutableListOf(
            TaskModel("Hoje tenho que ir comprar pão na padaria e depois ir ao mercado comprar carne de vaca", 1, false),
            TaskModel("Tarefa 2", 2, true),
            TaskModel("Tarefa 3", 3, false),
        )

        adapter = TaskAdapter(filter) {id ->
            tarefas.find{it.id == id}?.let{
                it.isDone = !it.isDone
            }
        }
        adapter.setTasks(tarefas)

        binding.rvTasks.adapter = adapter
        binding.rvTasks.layoutManager = LinearLayoutManager(context)


        // Ajusta a imagem de fundo com base no filtro
        when (filter) {
            "today" -> {
                binding.backgroundImage.setImageResource(R.drawable.today)
                binding.tvFilter.text = "Hoje"
            }
            "tomorrow" -> {
                binding.backgroundImage.setImageResource(R.drawable.tomorrow)
                binding.tvFilter.text = "Amanhã"
            }
            "week" -> {
                binding.backgroundImage.setImageResource(R.drawable.week)
                binding.tvFilter.text = "Semana"

            }
            "month" -> {
                binding.backgroundImage.setImageResource(R.drawable.month)
                binding.tvFilter.text = "Mês"
            }
        }
    }

    private fun loadTasksBasedOnFilter(filtro: String) {
        // Lógica para buscar as tarefas no banco de dados com base no filtro
        when (filtro) {
            "today" -> { /* Carregar tarefas de hoje */
            }

            "tomorrow" -> { /* Carregar tarefas de amanhã */
            }

            "week" -> { /* Carregar tarefas da semana */
            }

            "month" -> { /* Carregar tarefas do mês */
            }
        }
    }
}