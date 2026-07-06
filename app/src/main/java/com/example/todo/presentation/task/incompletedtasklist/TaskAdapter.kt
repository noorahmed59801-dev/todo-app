package com.example.todo.presentation.task.incompletedtasklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.data.Task

class TaskAdapter(
    private var tasks: List<Task>,
    private val onClick: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.taskTitle)
        val dateTime: TextView = view.findViewById(R.id.taskDateTime)
        val statusIcon: ImageView = view.findViewById(R.id.taskStatusIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.title.text = task.title
        holder.dateTime.text = holder.itemView.context.getString(R.string.task_date_time, task.date, task.time)
        holder.statusIcon.visibility = if (task.isCompleted) View.VISIBLE else View.GONE
        holder.itemView.setOnClickListener { onClick(task) }
    }

    override fun getItemCount() = tasks.size

    fun updateTasks(newTasks: List<Task>) {
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize() = tasks.size
            override fun getNewListSize() = newTasks.size
            override fun areItemsTheSame(oldPos: Int, newPos: Int) =
                tasks[oldPos].id == newTasks[newPos].id
            override fun areContentsTheSame(oldPos: Int, newPos: Int) =
                tasks[oldPos] == newTasks[newPos]
        })
        tasks = newTasks
        diffResult.dispatchUpdatesTo(this)
    }
}