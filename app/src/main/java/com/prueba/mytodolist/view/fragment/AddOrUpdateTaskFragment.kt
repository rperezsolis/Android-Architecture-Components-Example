package com.prueba.mytodolist.view.fragment

import android.app.Activity
import android.app.Application
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.prueba.mytodolist.R
import com.prueba.mytodolist.database.DateConverter
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
    private var deadline: Date? = null
    private lateinit var mEditTextDescription: EditText
    private lateinit var mRadioGroup: RadioGroup
    private lateinit var mButtonSetDeadline: Button
    private lateinit var mTextDeadline: TextView
    private lateinit var mButtonClearDeadline: ImageView
    private lateinit var mButtonSave: Button

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
            mButtonSave.text = getString(R.string.update_button)
            if (mTaskId == AddOrUpdateTaskViewModel.DEFAULT_TASK_ID) {
                mTaskId = itemId
            }
        }
        setupViewModel(mTaskId, view, activity!!.application)
    }

    private fun initViews(view: View) {
        mEditTextDescription = view.findViewById(R.id.editTextTaskDescription)
        mRadioGroup = view.findViewById(R.id.radioGroup)
        mButtonSetDeadline = view.findViewById(R.id.buttonSetDeadline)
        mTextDeadline = view.findViewById(R.id.textViewDeadline)
        mButtonClearDeadline = view.findViewById(R.id.clearDeadlineImageView)
        mButtonSave = view.findViewById(R.id.saveButton)
        mButtonSetDeadline.setOnClickListener { onAddDeadlineClicked() }
        mButtonClearDeadline.setOnClickListener { onClearDeadlineClicked() }
        mButtonSave.setOnClickListener { onSaveButtonClicked(view) }
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
        mEditTextDescription.setText(task.description)
        setPriorityInViews(task.priority, view)
        when(task.deadline) {
            null -> mTextDeadline.text = ""
            else -> {
                deadline = task.deadline
                mTextDeadline.text = DateConverter.dateFormat.format(task.deadline)
                mButtonClearDeadline.visibility = View.VISIBLE
            }
        }
    }

    private fun onAddDeadlineClicked() {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)

        DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { _, year, month, day ->
            TimePickerDialog(requireContext(), TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                val pickedDateTime = Calendar.getInstance()
                pickedDateTime.set(year, month, day, hour, minute)
                deadline =  pickedDateTime.time
                mTextDeadline.text = DateConverter.dateFormat.format(deadline)
                mButtonClearDeadline.visibility = View.VISIBLE
            }, startHour, startMinute, false).show()
        }, startYear, startMonth, startDay).show()
    }

    private fun onClearDeadlineClicked() {
        deadline = null
        mTextDeadline.text = ""
        mButtonClearDeadline.visibility = View.GONE
    }

    private fun onSaveButtonClicked(view: View) {
        val description = mEditTextDescription.text.toString()
        val priority = getPriorityFromViews(view)
        val date = Date()
        viewModel.insertOrUpdateTask(mTaskId, description, priority, date, deadline)
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