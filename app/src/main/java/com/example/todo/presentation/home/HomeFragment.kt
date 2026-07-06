package com.example.todo.presentation.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.R
import com.example.todo.data.Task
import com.example.todo.databinding.FragmentHomeBinding
import com.example.todo.presentation.task.taskcreation.CreateTaskFragment
import com.example.todo.presentation.task.incompletedtasklist.TaskAdapter
import com.example.todo.util.PrefsConstants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var incompleteAdapter: TaskAdapter
    private lateinit var completeAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name = requireContext()
            .getSharedPreferences(PrefsConstants.PREFS_NAME, Context.MODE_PRIVATE)
            .getString(PrefsConstants.KEY_USER_NAME, "")

        incompleteAdapter = TaskAdapter(emptyList()) { task -> navigateToDetail(task) }
        completeAdapter = TaskAdapter(emptyList()) { task -> navigateToDetail(task) }

        with(binding) {
            title1.text = if (name.isNullOrEmpty()) "WELCOME" else "WELCOME $name".uppercase()

            incompleteTasksRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            incompleteTasksRecyclerView.adapter = incompleteAdapter
            incompleteTasksRecyclerView.isNestedScrollingEnabled = false

            completeTasksRecyclerView1.layoutManager = LinearLayoutManager(requireContext())
            completeTasksRecyclerView1.adapter = completeAdapter
            completeTasksRecyclerView1.isNestedScrollingEnabled = false

            addbutton.setOnClickListener {
                CreateTaskFragment.newInstance().show(parentFragmentManager, "CreateTaskFragment")
            }

            seebutton1.setOnClickListener {
                findNavController().navigate(R.id.tasksFragment)
            }

            viewallbutttontext.setOnClickListener {
                findNavController().navigate(R.id.calendarFragment)
            }
        }

        viewModel.todayCompletedCount.observe(viewLifecycleOwner) { count ->
            binding.taskcounter1.completedCount.text = count.toString()
        }

        viewModel.todayRemainingCount.observe(viewLifecycleOwner) { count ->
            binding.taskcounter1.remainingCount.text = count.toString()
        }

        viewModel.incompleteTasks.observe(viewLifecycleOwner) { tasks ->
            incompleteAdapter.updateTasks(tasks)
            binding.incompleteGroup.visibility = if (tasks.isEmpty()) View.GONE else View.VISIBLE
        }

        viewModel.completedTasks.observe(viewLifecycleOwner) { tasks ->
            completeAdapter.updateTasks(tasks)
            binding.completeGroup.visibility = if (tasks.isEmpty()) View.GONE else View.VISIBLE
        }
    }

    private fun navigateToDetail(task: Task) {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}