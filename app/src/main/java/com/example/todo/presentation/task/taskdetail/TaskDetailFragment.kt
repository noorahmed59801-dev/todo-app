package com.example.todo.presentation.task.taskdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.todo.R
import com.example.todo.databinding.FragmentTaskDetailBinding
import com.example.todo.presentation.task.taskcreation.CreateTaskFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskDetailFragment : Fragment() {

    private var _binding: FragmentTaskDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TaskDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val taskId = arguments?.getInt("taskId") ?: return

        viewModel.loadTaskById(taskId)

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        // Navigate back only once the write to the database has actually finished
        viewModel.taskMarkedDone.observe(viewLifecycleOwner) { done ->
            if (done) {
                Toast.makeText(requireContext(), "Task marked as done!", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }

        viewModel.taskDeleted.observe(viewLifecycleOwner) { deleted ->
            if (deleted) {
                Toast.makeText(requireContext(), "Task deleted!", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }

        viewModel.selectedTask.observe(viewLifecycleOwner) { task ->
            if (task == null) return@observe

            with(binding) {
                taskTitleText.text = task.title
                taskDateTimeText.text = getString(R.string.set_text_to1, task.date, task.time)
                taskDescriptionText.text = task.description

                doneCard.setOnClickListener {
                    viewModel.markAsDone(task)   // navigation now happens via the observer above
                }

                deleteCard.setOnClickListener {
                    viewModel.deleteTask(task)   // navigation now happens via the observer above
                }

                pinCard.setOnClickListener {
                    viewModel.togglePin(task)
                    Toast.makeText(requireContext(), "Task pin toggled!", Toast.LENGTH_SHORT).show()
                }

                editBtn.setOnClickListener {
                    CreateTaskFragment.newInstance(task)
                        .show(parentFragmentManager, "EditTaskSheet")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}