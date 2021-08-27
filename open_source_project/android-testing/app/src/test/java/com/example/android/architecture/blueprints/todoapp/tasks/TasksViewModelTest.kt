package com.example.android.architecture.blueprints.todoapp.tasks

import android.util.EventLog
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.architecture.blueprints.todoapp.Event
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.nullValue
import org.hamcrest.core.IsNot.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class TasksViewModelTest{
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var tasksViewModel: TasksViewModel

    @Before
    fun setupViewModel() {
        tasksViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun addNewTask_setsNewTaskEvent() {
        //如果是 Local Test，getApplicationContext() 将返回一个模拟的 Applcation Context
        //如果是 Instrumented Test，getApplicationContext() 将返回模拟器或者真机的 Application Context
        // val tasksViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())

        tasksViewModel.addNewTask()
        val value = tasksViewModel.newTaskEvent.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled(), (not(nullValue())))
    }

    @Test
    fun setFilterAllTasks_tasksAddViewVisible() {
        // val tasksViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())

        //taskViewModel 是一个 LiveData，因此需要添加 Observer。
        tasksViewModel.setFiltering(TasksFilterType.ALL_TASKS)
        assertThat(tasksViewModel.tasksAddViewVisible.getOrAwaitValue(), (`is`(true)))
    }
}