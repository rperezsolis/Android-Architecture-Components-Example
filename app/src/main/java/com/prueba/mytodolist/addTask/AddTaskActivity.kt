package com.prueba.mytodolist.addTask

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.prueba.mytodolist.AppExecutors
import com.prueba.mytodolist.R
import com.prueba.mytodolist.database.TaskEntry
import com.prueba.mytodolist.repository.TaskRepository
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    companion object {
        val EXTRA_TASK_ID = "extraTaskId"
        val INSTANCE_TASK_ID = "instanceTaskId"
    }

    private val DEFAULT_TASK_ID = -1
    private val PRIORITY_HIGH = 1
    private val PRIORITY_MEDIUM = 2
    private val PRIORITY_LOW = 3
    private var mTaskId = DEFAULT_TASK_ID
    private lateinit var taskRepository: TaskRepository
    private lateinit var mEditText: EditText
    private lateinit var mRadioGroup: RadioGroup
    private lateinit var mButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        taskRepository = TaskRepository(applicationContext)
        initViews()
        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_TASK_ID)) {
            mTaskId = savedInstanceState.getInt(INSTANCE_TASK_ID, DEFAULT_TASK_ID)
        }
        if (intent != null && intent.hasExtra(EXTRA_TASK_ID)) {
            mButton.text = "Update"
            if (mTaskId == DEFAULT_TASK_ID) {
                mTaskId = intent.getIntExtra(EXTRA_TASK_ID, DEFAULT_TASK_ID)
                val factory: AddTaskViewModelFactory = taskRepository.setAddTaskViewModel(mTaskId, application)
                val viewModel: AddTaskViewModel = ViewModelProviders.of(this, factory).get(AddTaskViewModel::class.java)
                val taskObserver = Observer { taskEntry: TaskEntry ->
                    //viewModel.task.removeObserver(taskObserver)
                    populateUI(taskEntry)
                }
                viewModel.task.observe(this, taskObserver)
            }
        }
    }

    private fun initViews() {
        mEditText = findViewById(R.id.editTextTaskDescription)
        mRadioGroup = findViewById(R.id.radioGroup)
        mButton = findViewById(R.id.saveButton)
        mButton.setOnClickListener { onSaveButtonClicked() }
    }

    private fun populateUI(task: TaskEntry?) {
        if (task == null) {
            return
        }
        mEditText.setText(task.description)
        setPriorityInViews(task.priority)
    }

    private fun onSaveButtonClicked() {
        val description = mEditText.text.toString()
        val priority = getPriorityFromViews()
        val date = Date()

        val task = TaskEntry(0, description, priority, date)
        AppExecutors.getInstance()?.mDiskIO?.execute {
            if (mTaskId == DEFAULT_TASK_ID) {
                taskRepository.insertTask(task)
            } else {
                task.id = mTaskId
                taskRepository.updateTask(task)
            }
            finish()
        }
    }

    private fun getPriorityFromViews(): Int {
        var priority = 1
        when ((findViewById<RadioGroup>(R.id.radioGroup)).checkedRadioButtonId) {
            R.id.radButton1 -> priority = PRIORITY_HIGH
            R.id.radButton2 -> priority = PRIORITY_MEDIUM
            R.id.radButton3 -> priority = PRIORITY_LOW
        }
        return priority
    }

    private fun setPriorityInViews(priority: Int) {
        when (priority) {
            PRIORITY_HIGH -> (findViewById<RadioGroup>(R.id.radioGroup)).check(R.id.radButton1)
            PRIORITY_MEDIUM -> (findViewById<RadioGroup>(R.id.radioGroup)).check(R.id.radButton2)
            PRIORITY_LOW -> (findViewById<RadioGroup>(R.id.radioGroup)).check(R.id.radButton3)
        }
    }
}
