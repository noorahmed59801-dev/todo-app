package com.example.todo.presentation.calendar

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.todo.R
import com.example.todo.data.Task
import com.example.todo.databinding.FragmentCalenderBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class CalendarFragment : Fragment() {

    private var _binding: FragmentCalenderBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CalendarViewModel by viewModels()

    private var selectedDate: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalenderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedDate = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
            .format(Calendar.getInstance().time)

        with(binding) {
            selectedDateLabel.text = getString(R.string.set_task_for, selectedDate)

            calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
                selectedDate = "$dayOfMonth/${month + 1}/$year"
                selectedDateLabel.text = getString(R.string.set_task_for1, selectedDate)
            }

            timeInput.setOnClickListener {
                Calendar.getInstance().let { cal ->
                    TimePickerDialog(requireContext(), { _, hour, minute ->
                        val amPm = if (hour >= 12) "pm" else "am"
                        val h = if (hour > 12) hour - 12 else if (hour == 0) 12 else hour
                        timeInput.setText(
                            getString(
                                R.string.set_text_for3,
                                h,
                                minute.toString().padStart(2, '0'),
                                amPm
                            ))
                    }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show()
                }
            }

            submitBtn.setOnClickListener {
                val title = taskInput.text.toString().trim()
                val time = timeInput.text.toString().trim()

                when {
                    title.isEmpty() -> Toast.makeText(requireContext(), "Please enter a task title", Toast.LENGTH_SHORT).show()
                    time.isEmpty() -> Toast.makeText(requireContext(), "Please select a time", Toast.LENGTH_SHORT).show()
                    else -> {
                        viewModel.insertTask(Task(
                            title = title,
                            date = selectedDate,
                            time = time,
                            isCompleted = false,
                            description = ""
                        ))
                        taskInput.setText("")
                        timeInput.setText("")
                        Toast.makeText(requireContext(), "Task added for $selectedDate at $time", Toast.LENGTH_SHORT).show()
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