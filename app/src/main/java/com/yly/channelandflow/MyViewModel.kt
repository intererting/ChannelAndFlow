package com.yly.channelandflow

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.liveData
import kotlinx.coroutines.delay

class MyViewModel(
    application: Application,
    val savedStateHandle: SavedStateHandle
) :
    AndroidViewModel(application) {

    val secondLiveData = liveData {
        delay(3000)
        emit("second")
    }


    val testLiveData = liveData {
        emit("first")
        emitSource(secondLiveData)
    }

    fun saveDataInSaveState() {
        savedStateHandle.set("data", "saved name -> yuliyang")
    }

    fun getSavedStateData(): LiveData<String> {
        return savedStateHandle.getLiveData("data")
    }
}