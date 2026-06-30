package com.example.todo.presentation.task.taskcreation

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.todo.R
import com.example.todo.data.Task
import com.example.todo.databinding.FragmentCreateTaskBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class CreateTaskFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentCreateTaskBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CreateTaskViewModel by viewModels()
    private var editTaskId: Int = -1
    private var editIsPinned: Boolean = false
    private var editIsCompleted: Boolean = false

    companion object {
        fun newInstance(task: Task? = null): CreateTaskFragment {
            val fragment = CreateTaskFragment()
            task?.let {
                fragment.arguments = Bundle().apply {
                    putInt("editTaskId", it.id)
                    putString("editTitle", it.title)
                    putString("editDate", it.date)
                    putString("editTime", it.time)
                    putString("editDescription", it.description)
                    putBoolean("editIsPinned", it.isPinned)
                    putBoolean("editIsCompleted", it.isCompleted)
                }
            }
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { args ->
            editTaskId = args.getInt("editTaskId", -1)
            if (editTaskId != -1) {
                editIsPinned = args.getBoolean("editIsPinned", false)
                editIsCompleted = args.getBoolean("editIsCompleted", false)
                binding.taskTitleInput.setText(args.getString("editTitle", ""))
                binding.taskDateInput.setText(args.getString("editDate", ""))
                binding.taskTimeInput.setText(args.getString("editTime", ""))
                binding.taskDescriptionInput.setText(args.getString("editDescription", ""))
                binding.createBtn.text = getString(R.string.update)
            }
        }

        with(binding) {
            taskDateInput.setOnClickListener {
                Calendar.getInstance().let { cal ->
                    DatePickerDialog(requireContext(), { _, year, month, day ->
                        taskDateInput.setText(getString(R.string.settext, day, month + 1, year))
                    }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
                }
            }

            taskTimeInput.setOnClickListener {
                Calendar.getInstance().let { cal ->
                    TimePickerDialog(requireContext(), { _, hour, minute ->
                        val amPm = if (hour >= 12) "pm" else "am"
                        val h = if (hour > 12) hour - 12 else if (hour == 0) 12 else hour
                        taskTimeInput.setText(
                            getString(
                                R.string.settext2,
                                h,
                                minute.toString().padStart(2, '0'),
                                amPm
                            ))
                    }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show()
                }
            }

            cancelBtn.setOnClickListener { dismiss() }

            createBtn.setOnClickListener {
                val title = taskTitleInput.text.toString().trim()
                val description = taskDescriptionInput.text.toString().trim()
                val date = taskDateInput.text.toString().trim()
                val time = taskTimeInput.text.toString().trim()

                when {
                    title.isEmpty() -> Toast.makeText(requireContext(), "Please enter a task title", Toast.LENGTH_SHORT).show()
                    date.isEmpty() -> Toast.makeText(requireContext(), "Please select a date", Toast.LENGTH_SHORT).show()
                    time.isEmpty() -> Toast.makeText(requireContext(), "Please select a time", Toast.LENGTH_SHORT).show()
                    else -> {
                        if (editTaskId != -1) {
                            viewModel.updateTask(Task(
                                id = editTaskId,
                                title = title,
                                description = description,
                                date = date,
                                time = time,
                                isPinned = editIsPinned,
                                isCompleted = editIsCompleted
                            ))
                            Toast.makeText(requireContext(), "Task updated!", Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.insertTask(Task(
                                title = title,
                                description = description,
                                date = date,
                                time = time
                            ))
                            Toast.makeText(requireContext(), "Task created!", Toast.LENGTH_SHORT).show()
                        }
                        dismiss()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}