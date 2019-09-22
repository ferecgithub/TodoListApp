package com.ferechamitbeyli.todolistappkotlin.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ferechamitbeyli.todolistappkotlin.R
import com.ferechamitbeyli.todolistappkotlin.model.Task

class TaskAdapter(var context: Context, var taskList: ArrayList<Task>):
    RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    private lateinit var onTaskCompleteListener: OnTaskCompleteListener
    private lateinit var onEditTaskListener: OnTaskEditListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = taskList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.taskName.text = taskList[position].name
        holder.taskDate.text = taskList[position].date
        holder.completeCheckbox.isChecked = false // Yoksa bazen checked olabiliyor
        holder.completeCheckbox.setOnClickListener {// Network işlemlerini vs. adaptor içinde yapmayın. Onun yerine bir listener kullanın.
            onTaskCompleteListener.let {
                it.onTaskComplete(taskList[position].id)
            }
        }
        holder.itemView.setOnCreateContextMenuListener { menu, v, menuInfo -> // itemView (view' ın kendisi) üzerine uzun basınca üzerinde Edit yazan bir context menu açacağız ve tıklandığında düzenleme sayfası gelecek
            menu.add("Edit").setOnMenuItemClickListener {
                onEditTaskListener.let {
                    it.onEditTask(taskList[position])
                }
                return@setOnMenuItemClickListener true
            }
        }
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val taskName = view.findViewById<TextView>(R.id.item_task_name)
        val taskDate = view.findViewById<TextView>(R.id.item_date)
        val completeCheckbox = view.findViewById<CheckBox>(R.id.item_complete_cb)
    }

    fun updateList(newList: ArrayList<Task>) {
        taskList.clear()
        taskList.addAll(newList)
        notifyDataSetChanged()
    }

    fun setOnTaskCompleteListener(onTaskCompleteListener: OnTaskCompleteListener) {
        this.onTaskCompleteListener = onTaskCompleteListener
    }

    fun setOnTaskEditListener(onEditTaskListener: OnTaskEditListener) {
        this.onEditTaskListener = onEditTaskListener
    }

    interface OnTaskCompleteListener {
        fun onTaskComplete(taskId: Int)
    }

    interface OnTaskEditListener {
        fun onEditTask(task: Task)
    }
}