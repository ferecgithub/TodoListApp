package com.ferechamitbeyli.todolistappkotlin.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ferechamitbeyli.todolistappkotlin.R
import com.ferechamitbeyli.todolistappkotlin.db.TaskRepository
import com.ferechamitbeyli.todolistappkotlin.model.Task
import com.ferechamitbeyli.todolistappkotlin.ui.adapter.TaskAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), TaskAdapter.OnTaskCompleteListener, TaskAdapter.OnTaskEditListener {

    private lateinit var taskRepository: TaskRepository
    private lateinit var taskList: ArrayList<Task>
    private lateinit var adapter: TaskAdapter

    companion object {
        const val EXTRA_TASK = "extra_task"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(main_toolbar)
        supportActionBar?.title = "TODO-List"

        taskRepository = TaskRepository(this)
        taskList = taskRepository.getAllTask()

        task_recyclerview.layoutManager = LinearLayoutManager(this)
        adapter = TaskAdapter(this, taskList)
        task_recyclerview.adapter = adapter
        task_recyclerview.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL)) // her recyclerview iteminin altına bir çizgi atarak belirginleştirdik.

        adapter.setOnTaskCompleteListener(this)
        adapter.setOnTaskEditListener(this)

        task_fab.setOnClickListener {startActivity(Intent(this, TaskActivity::class.java))}
    }

    override fun onTaskComplete(taskId: Int) {
        taskRepository.deleteTask(taskId)
        taskList = taskRepository.getAllTask()
        adapter.updateList(taskList)
    }

    override fun onEditTask(task: Task) {
        val intent = Intent(this, TaskActivity::class.java)
        intent.putExtra(EXTRA_TASK, task) // nesnenin kendisini gönderebilmek için iki yol var. Model sınıfını Serializable veya Parcelable' dan türetmek. En performanslısı parcelable ancak öğrenmek amacıyla diğerini kullanıyoruz.
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()

        taskList = taskRepository.getAllTask()
        adapter.updateList(taskList)
    }
}
