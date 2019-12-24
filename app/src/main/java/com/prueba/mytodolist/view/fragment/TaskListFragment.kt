package com.prueba.mytodolist.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.prueba.mytodolist.R
import com.prueba.mytodolist.model.TaskEntry
import com.prueba.mytodolist.view.adapter.TaskListAdapter
import com.prueba.mytodolist.viewModel.MainViewModel

class TaskListFragment : Fragment(), TaskListAdapter.ItemClickListener {

    private lateinit var mRecyclerView : RecyclerView
    private lateinit var mAdapter: TaskListAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.task_list_recyclerview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView(view)
        setItemTouchHelper()
        setFloatingActionButton(view)
        setupViewModel()
    }

    private fun setUpRecyclerView(view: View) {
        mRecyclerView = view.findViewById(R.id.recyclerViewTasksFragment)
        mRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = TaskListAdapter(view.context, this@TaskListFragment)
            mAdapter = adapter as TaskListAdapter
            val decoration = DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL)
            mRecyclerView.addItemDecoration(decoration)
        }
    }

    private fun setItemTouchHelper() {
        ItemTouchHelper(TaskItemTouchHelper(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)).attachToRecyclerView(mRecyclerView)
    }

    private fun setFloatingActionButton(view: View) {
        val floatingButton: View = view.findViewById(R.id.fab)
        floatingButton.setOnClickListener {
            val action = TaskListFragmentDirections.actionTaskListFragmentToAddTaskFragment()
            it.findNavController().navigate(action)
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        val tasksObserver = Observer { tasks: List<TaskEntry> ->
            mAdapter.setTasks(tasks)
        }
        viewModel.getTasks().observe(this, tasksObserver)
    }

    inner class TaskItemTouchHelper(dragDirs: Int, swipeDirs: Int): ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
            viewModel.deleteTask(viewHolder.adapterPosition, mAdapter)
        }
    }

    override fun onItemClickListener(itemId: Int, view: View) {
        val action = TaskListFragmentDirections.actionTaskListFragmentToAddTaskFragment(itemId)
        view.findNavController().navigate(action)
    }

}