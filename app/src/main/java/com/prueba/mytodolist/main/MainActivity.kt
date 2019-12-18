package com.prueba.mytodolist.main

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.prueba.mytodolist.AppExecutors
import com.prueba.mytodolist.R
import com.prueba.mytodolist.addTask.AddTaskActivity
import com.prueba.mytodolist.database.TaskEntry
import com.prueba.mytodolist.repository.TaskRepository

class MainActivity : AppCompatActivity(), TaskAdapter.ItemClickListener {

    lateinit var mRecyclerView : RecyclerView
    private lateinit var mAdapter: TaskAdapter
    private lateinit var taskRepository: TaskRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAdapter = TaskAdapter(this, this)
        taskRepository = TaskRepository(applicationContext)
        setContentView(R.layout.activity_main)
        setUpRecyclerView()
        setItemTouchHelper()
        setFloatingActionButton()
        setupViewModel()
    }

    private fun setUpRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerViewTasks)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = mAdapter
        val decoration = DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        mRecyclerView.addItemDecoration(decoration)
    }

    private fun setItemTouchHelper() {
        ItemTouchHelper(MyItemTouhHelper(0, ItemTouchHelper.LEFT)).attachToRecyclerView(mRecyclerView)
    }

    private fun setFloatingActionButton() {
        val floatingButton: View = findViewById(R.id.fab)
        floatingButton.setOnClickListener { view ->
            startActivity(Intent(this, AddTaskActivity::class.java))
        }
    }

    private fun setupViewModel() {
        val viewModel: MainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        val tasksObserver = Observer { tasks: List<TaskEntry> ->
            mAdapter.setTasks(tasks)
        }
        viewModel.getTasks().observe(this, tasksObserver)
    }

    private fun <T : View> Activity.bind(@IdRes res : Int) : T {
        @Suppress("UNCHECKED_CAST")
        return findViewById(res)
    }

    inner class MyItemTouhHelper(dragDirs: Int, swipeDirs: Int): ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
            AppExecutors.getInstance()?.mDiskIO?.execute {
                val position = viewHolder.adapterPosition
                val tasks = mAdapter.getTasks()
                taskRepository.deleteTask(tasks[position])
            }
        }
    }

    override fun onItemClickListener(itemId: Int) {
        val intent = Intent(this, AddTaskActivity::class.java)
        intent.putExtra(AddTaskActivity.EXTRA_TASK_ID, itemId)
        startActivity(intent)
    }
}
