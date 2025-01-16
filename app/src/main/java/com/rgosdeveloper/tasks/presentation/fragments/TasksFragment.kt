package com.rgosdeveloper.tasks.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.rgosdeveloper.tasks.R
import com.rgosdeveloper.tasks.databinding.FragmentTasksBinding
import com.rgosdeveloper.tasks.presentation.adapters.TaskAdapter

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

        adapter = TaskAdapter()
        val tarefas = listOf("Tarefa 1", "Tarefa 2", "Tarefa 3")
        adapter.setTasks(tarefas)

        binding.rvTasks.adapter = adapter
        binding.rvTasks.layoutManager = LinearLayoutManager(context)


        // Ajusta a imagem de fundo com base no filtro
        when (filter) {
            "today" -> {
                binding.backgroundImage.setImageResource(R.drawable.today)
            }
            "tomorrow" -> {
                binding.backgroundImage.setImageResource(R.drawable.tomorrow)
            }
            "week" -> {
                binding.backgroundImage.setImageResource(R.drawable.week)
            }
            "month" -> {
                binding.backgroundImage.setImageResource(R.drawable.month)
            }
        }

        // Aqui você pode ajustar o RecyclerView conforme o filtro
        loadTasksBasedOnFilter(filter)

        return binding.root
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