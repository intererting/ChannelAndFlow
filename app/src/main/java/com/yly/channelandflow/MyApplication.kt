package com.yly.channelandflow

import android.app.Application
import androidx.lifecycle.*

class MyApplication : Application(), LifecycleObserver {

    override fun onCreate() {
        super.onCreate()
        //用来监听全局app状态
//        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun applicationOnCreate() {
        println("application onCreate")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun applicationOnStart() {
        println("application onStart")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun applicationOnResume() {
        println("application onResume")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun applicationOnPause() {
        println("application onPause")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun applicationOnStop() {
        println("application onStop")
    }

}