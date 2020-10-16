package com.yosefmoq.firstcode.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.yosefmoq.firstcode.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {
    val postDao = AppDatabase.invoke(application).postDao()
//    val spHelper = SPHelper.invoke(application)

    private var job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}