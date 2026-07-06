package com.example.todo.presentation.task.incompletedtasklist

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.R
import com.example.todo.data.Task
import com.example.todo.databinding.FragmentTaskListBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class IncompletedTaskListFragment : Fragment() {

    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TaskListViewModel by viewModels()
    private var allTasksList: List<Task> = emptyList()
    private lateinit var adapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TaskAdapter(emptyList()) { task ->
            val bundle = Bundle().apply {
                putInt("taskId", task.id)
                putString("taskTitle", task.title)
                putString("taskDateTime", task.time)
                putString("taskDate", task.date)
                putString("taskDescription", task.description)
                putBoolean("taskIsPinned", task.isPinned)
                putBoolean("taskIsCompleted", task.isCompleted)
            }
            findNavController().navigate(R.id.taskDetailFragment, bundle)
        }

        binding.tasksRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.tasksRecyclerView.adapter = adapter

        viewModel.allTasks.observe(viewLifecycleOwner) { tasks ->
            allTasksList = tasks
            applyFilterAndSort(binding.searchInput.text.toString().trim())
        }

        viewModel.currentSortOption.observe(viewLifecycleOwner) {
            applyFilterAndSort(binding.searchInput.text.toString().trim())
        }

        binding.searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                applyFilterAndSort(s.toString().trim())
            }
        })

        binding.sortButton.setOnClickListener {
            PopupMenu(requireContext(), binding.sortButton).apply {
                menu.add(0, 1, 0, "Title A-Z")
                menu.add(0, 2, 0, "Title Z-A")
                menu.add(0, 3, 0, "Date Ascending")
                menu.add(0, 4, 0, "Date Descending")
                menu.add(0, 7, 0, "Clear Sort")
                setOnMenuItemClickListener { item ->
                    viewModel.setSortOption(when (item.itemId) {
                        1 -> SortOption.TITLE_ASC
                        2 -> SortOption.TITLE_DESC
                        3 -> SortOption.DATE_ASC
                        4 -> SortOption.DATE_DESC
                        else -> SortOption.NONE
                    })
                    true
                }
                show()
            }
        }
    }

    private fun parseDate(dateStr: String): Date {
        return try {
            SimpleDateFormat("d/M/yyyy", Locale.getDefault()).parse(dateStr) ?: Date(0)
        } catch (_: Exception) {
            Date(0)
        }
    }

    private fun applyFilterAndSort(query: String) {
        val currentSortOption = viewModel.currentSortOption.value ?: SortOption.NONE

        var result = if (query.isEmpty()) {
            allTasksList
        } else {
            allTasksList.filter { it.title.lowercase().contains(query.lowercase()) }
        }

        result = when (currentSortOption) {
            SortOption.TITLE_ASC -> result.sortedBy { it.title.lowercase() }
            SortOption.TITLE_DESC -> result.sortedByDescending { it.title.lowercase() }
            SortOption.DATE_ASC -> result.sortedBy { parseDate(it.date) }
            SortOption.DATE_DESC -> result.sortedByDescending { parseDate(it.date) }
            else -> result
        }

        result = result.sortedByDescending { it.isPinned }

        binding.emptyStateText.visibility = if (result.isEmpty()) View.VISIBLE else View.GONE
        binding.tasksRecyclerView.visibility = if (result.isEmpty()) View.GONE else View.VISIBLE

        adapter.updateTasks(result)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}