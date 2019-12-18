package com.prueba.mytodolist.view.adapter

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.prueba.mytodolist.R
import com.prueba.mytodolist.model.TaskEntry
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TaskAdapter(context: Context, itemClickListener: ItemClickListener) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    var mContext: Context = context
    var mItemClickListener: ItemClickListener = itemClickListener
    private var mTaskEntries: List<TaskEntry> = ArrayList()

    fun getTasks(): List<TaskEntry> {
        return mTaskEntries
    }

    fun setTasks(taskEntries: List<TaskEntry>) {
        mTaskEntries = taskEntries
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.task_layout, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val taskEntry = mTaskEntries[position]
        holder.bind(taskEntry)
    }

    override fun getItemCount(): Int {
        return mTaskEntries.size
    }

    interface ItemClickListener {
        fun onItemClickListener(itemId: Int)
    }

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val DATE_FORMAT: String = "dd/MM/yy"
        private val dateFormat: SimpleDateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())

        private val taskDescriptionView: TextView = view.findViewById(R.id.taskDescription)
        private val updatedAtView: TextView = view.findViewById(R.id.taskUpdatedAt)
        private val priorityView: TextView = view.findViewById(R.id.priorityTextView)

        fun bind(taskEntry: TaskEntry) {
            taskDescriptionView.text = taskEntry.description
            updatedAtView.text = dateFormat.format(taskEntry.updatedAt)
            priorityView.text = taskEntry.priority.toString()
            val priorityCircle: GradientDrawable = priorityView.background as GradientDrawable
            val priorityColor: Int = when(taskEntry.priority) {
                1 -> ContextCompat.getColor(mContext, R.color.materialRed)
                2 -> ContextCompat.getColor(mContext, R.color.materialOrange)
                else -> {ContextCompat.getColor(mContext, R.color.materialYellow)}
            }
            priorityCircle.setColor(priorityColor)

            itemView.setOnClickListener {
                val itemId = taskEntry.id
                mItemClickListener.onItemClickListener(itemId)
            }
        }
    }
}