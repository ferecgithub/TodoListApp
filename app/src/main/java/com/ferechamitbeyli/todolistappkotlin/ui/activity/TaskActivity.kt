package com.ferechamitbeyli.todolistappkotlin.ui.activity

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.ferechamitbeyli.todolistappkotlin.R
import com.ferechamitbeyli.todolistappkotlin.db.TaskRepository
import com.ferechamitbeyli.todolistappkotlin.model.Task
import kotlinx.android.synthetic.main.activity_task.*
import java.util.*

class TaskActivity : AppCompatActivity() {

    private lateinit var taskRepository: TaskRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        setSupportActionBar(task_toolbar)
        supportActionBar?.title = "Add Todo"

        taskRepository = TaskRepository(this)

        if(intent.extras != null) {
            val task: Task = intent.extras!!.getSerializable(MainActivity.EXTRA_TASK) as Task
            task_name_edt.setText(task.name)
            end_date_text.text = task.date
        }

        confirm_fab.setOnClickListener {
            if(intent.extras != null) {
                val task: Task = intent.extras!!.getSerializable(MainActivity.EXTRA_TASK) as Task
                val rowId = taskRepository.updateTask(Task(task.id, task_name_edt.text.toString(), end_date_text.text.toString()))
                if (rowId > -1) Toast.makeText(this, "Güncellendi", Toast.LENGTH_SHORT).show()
                else Toast.makeText(this, "Güncelleme başarısız", Toast.LENGTH_SHORT).show()
            } else {
                if(!TextUtils.isEmpty(task_name_edt.text.toString())) { // Eğer task_name_edt içindeki text boş değilse
                    val date: String =
                        if(end_date_text.text == null || end_date_text.text == "End Date") "No End Date"
                        else end_date_text.text.toString()

                    val rowId = taskRepository.insertTask(Task(name = task_name_edt.text.toString(), date = date))
                    if(rowId > -1) Toast.makeText(this, "Eklendi", Toast.LENGTH_SHORT).show()
                    else Toast.makeText(this, "Ekleme başarısız", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(this, "Task adı boş geçilemez.", Toast.LENGTH_SHORT).show()
                }
            }

        }
        end_date_layout.setOnClickListener {getDatePickerDialog()}

    }

    private fun getDatePickerDialog() {
        val calender = Calendar.getInstance()
        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH)
        val day = calender.get(Calendar.DAY_OF_MONTH)

        val dialog = DatePickerDialog(this, {view, year, month, dayOfMonth ->
            val endDate = "$dayOfMonth.$month.$year"
            end_date_text.text = endDate
        },year,month,day)

        dialog.datePicker.minDate = System.currentTimeMillis() // En son bugünün tarihini girebilsin. Öncesini giremesin
        dialog.show()
    }
}
