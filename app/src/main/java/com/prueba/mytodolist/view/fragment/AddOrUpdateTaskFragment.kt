package com.prueba.mytodolist.view.fragment

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.prueba.mytodolist.R
import com.prueba.mytodolist.model.TaskEntry
import com.prueba.mytodolist.viewModel.AddOrUpdateTaskViewModel
import com.prueba.mytodolist.viewModel.AddOrUpdateTaskViewModelFactory
import java.util.*


class AddOrUpdateTaskFragment : Fragment() {

    companion object {
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


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        val itemId = AddOrUpdateTaskFragmentArgs.fromBundle(arguments!!).itemId
        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_TASK_ID)) {
            mTaskId = savedInstanceState.getInt(INSTANCE_TASK_ID, AddOrUpdateTaskViewModel.DEFAULT_TASK_ID)
        }
        if (itemId != AddOrUpdateTaskViewModel.DEFAULT_TASK_ID) {
            mButton.text = getString(R.string.update_button)
            if (mTaskId == AddOrUpdateTaskViewModel.DEFAULT_TASK_ID) {
                mTaskId = itemId
            }
        }
        setupViewModel(mTaskId, view, activity!!.application)
    }

    private fun initViews(view: View) {
        mEditText = view.findViewById(R.id.editTextTaskDescription)
        mRadioGroup = view.findViewById(R.id.radioGroup)
        mButton = view.findViewById(R.id.saveButton)
        mButton.setOnClickListener { onSaveButtonClicked(view) }
    }

    private fun setupViewModel(taskId: Int, view: View, application: Application) {
        val factory: AddOrUpdateTaskViewModelFactory = AddOrUpdateTaskViewModelFactory.setAddTaskViewModel(taskId, application)
        viewModel = ViewModelProviders.of(this, factory).get(AddOrUpdateTaskViewModel::class.java)
        if (mTaskId != AddOrUpdateTaskViewModel.DEFAULT_TASK_ID) {
            val taskObserver = Observer { taskEntry: TaskEntry ->
                //viewModel.getTask().removeObserver(taskObserver)
                populateUI(taskEntry, view)
            }
            viewModel.getTask().observe(this, taskObserver)
        }
    }

    private fun populateUI(task: TaskEntry?, view: View) {
        if (task == null) {
            return
        }
        mEditText.setText(task.description)
        setPriorityInViews(task.priority, view)
    }

    private fun onSaveButtonClicked(view: View) {
        val description = mEditText.text.toString()
        val priority = getPriorityFromViews(view)
        val date = Date()
        viewModel.insertOrUpdateTask(mTaskId, description, priority, date)
        hideKeyboardFrom(view.context, view)
        view.findNavController().popBackStack()
    }

    private fun getPriorityFromViews(view: View): Int {
        var priority = 1
        when ((view.findViewById<RadioGroup>(R.id.radioGroup)).checkedRadioButtonId) {
            R.id.radioButton1 -> priority = PRIORITY_HIGH
            R.id.radioButton2 -> priority = PRIORITY_MEDIUM
            R.id.radioButton3 -> priority = PRIORITY_LOW
        }
        return priority
    }

    private fun setPriorityInViews(priority: Int, view: View) {
        when (priority) {
            PRIORITY_HIGH -> (view.findViewById<RadioGroup>(R.id.radioGroup)).check(R.id.radioButton1)
            PRIORITY_MEDIUM -> (view.findViewById<RadioGroup>(R.id.radioGroup)).check(R.id.radioButton2)
            PRIORITY_LOW -> (view.findViewById<RadioGroup>(R.id.radioGroup)).check(R.id.radioButton3)
        }
    }

    private fun hideKeyboardFrom(context: Context, view: View) {
        val imm: InputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}