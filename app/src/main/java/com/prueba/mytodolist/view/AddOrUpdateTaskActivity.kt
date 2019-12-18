package com.prueba.mytodolist.view

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.prueba.mytodolist.R
import com.prueba.mytodolist.viewModel.AddOrUpdateTaskViewModel
import com.prueba.mytodolist.viewModel.AddOrUpdateTaskViewModelFactory
import com.prueba.mytodolist.model.TaskEntry
import java.util.*

class AddOrUpdateTaskActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TASK_ID = "extraTaskId"
        const val INSTANCE_TASK_ID = "instanceTaskId"
        private const val PRIORITY_HIGH = 1
        private const val PRIORITY_MEDIUM = 2
        private const val PRIORITY_LOW = 3
    }

    private lateinit var viewModel: AddOrUpdateTaskViewModel
    private var mTaskId = AddOrUpdateTaskViewModel.DEFAULT_TASK_ID
    private lateinit var mEditText: EditText
    private lateinit var mRadioGroup: RadioGroup
    private lateinit var mButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        initViews()
        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_TASK_ID)) {
            mTaskId = savedInstanceState.getInt(INSTANCE_TASK_ID, AddOrUpdateTaskViewModel.DEFAULT_TASK_ID)
        }
        if (intent != null && intent.hasExtra(EXTRA_TASK_ID)) {
            mButton.text = getString(R.string.update_button)
            if (mTaskId == AddOrUpdateTaskViewModel.DEFAULT_TASK_ID) {
                mTaskId = intent.getIntExtra(EXTRA_TASK_ID, AddOrUpdateTaskViewModel.DEFAULT_TASK_ID)
            }
        }
        setupViewModel(mTaskId)
    }

    private fun initViews() {
        mEditText = findViewById(R.id.editTextTaskDescription)
        mRadioGroup = findViewById(R.id.radioGroup)
        mButton = findViewById(R.id.saveButton)
        mButton.setOnClickListener { onSaveButtonClicked() }
    }

    private fun setupViewModel(taskId: Int) {
        val factory: AddOrUpdateTaskViewModelFactory = AddOrUpdateTaskViewModelFactory.setAddTaskViewModel(taskId, application)
        viewModel = ViewModelProviders.of(this, factory).get(AddOrUpdateTaskViewModel::class.java)
        if (mTaskId != AddOrUpdateTaskViewModel.DEFAULT_TASK_ID) {
            val taskObserver = Observer { taskEntry: TaskEntry ->
                //viewModel.getTask().removeObserver(taskObserver)
                populateUI(taskEntry)
            }
            viewModel.getTask().observe(this, taskObserver)
        }
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
        viewModel.insertOrUpdateTask(mTaskId, description, priority, date)
        finish()
    }

    private fun getPriorityFromViews(): Int {
        var priority = 1
        when ((findViewById<RadioGroup>(R.id.radioGroup)).checkedRadioButtonId) {
            R.id.radioButton1 -> priority = PRIORITY_HIGH
            R.id.radioButton2 -> priority = PRIORITY_MEDIUM
            R.id.radioButton3 -> priority = PRIORITY_LOW
        }
        return priority
    }

    private fun setPriorityInViews(priority: Int) {
        when (priority) {
            PRIORITY_HIGH -> (findViewById<RadioGroup>(R.id.radioGroup)).check(R.id.radioButton1)
            PRIORITY_MEDIUM -> (findViewById<RadioGroup>(R.id.radioGroup)).check(R.id.radioButton2)
            PRIORITY_LOW -> (findViewById<RadioGroup>(R.id.radioGroup)).check(R.id.radioButton3)
        }
    }
}
