package com.prueba.mytodolist.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.prueba.mytodolist.R
import com.prueba.mytodolist.viewModel.MainViewModel
import com.prueba.mytodolist.view.adapter.TaskAdapter
import com.prueba.mytodolist.model.TaskEntry

class MainActivity : AppCompatActivity(), TaskAdapter.ItemClickListener {

    private lateinit var mRecyclerView : RecyclerView
    private lateinit var mAdapter: TaskAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpRecyclerView()
        setItemTouchHelper()
        setFloatingActionButton()
        setupViewModel()
    }

    private fun setUpRecyclerView() {
        mAdapter = TaskAdapter(this, this)
        mRecyclerView = findViewById(R.id.recyclerViewTasks)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = mAdapter
        val decoration = DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        mRecyclerView.addItemDecoration(decoration)
    }

    private fun setItemTouchHelper() {
        ItemTouchHelper(MyItemTouchHelper(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)).attachToRecyclerView(mRecyclerView)
    }

    private fun setFloatingActionButton() {
        val floatingButton: View = findViewById(R.id.fab)
        floatingButton.setOnClickListener { view ->
            startActivity(Intent(this, AddOrUpdateTaskActivity::class.java))
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        val tasksObserver = Observer { tasks: List<TaskEntry> ->
            mAdapter.setTasks(tasks)
        }
        viewModel.getTasks().observe(this, tasksObserver)
    }

    inner class MyItemTouchHelper(dragDirs: Int, swipeDirs: Int): ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
            viewModel.deleteTask(viewHolder.adapterPosition, mAdapter)
        }
    }

    override fun onItemClickListener(itemId: Int) {
        val intent = Intent(this, AddOrUpdateTaskActivity::class.java)
        intent.putExtra(AddOrUpdateTaskActivity.EXTRA_TASK_ID, itemId)
        startActivity(intent)
    }
}
