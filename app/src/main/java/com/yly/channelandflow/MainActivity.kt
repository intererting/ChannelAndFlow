package com.yly.channelandflow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.lifecycle.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val myViewModel by viewModels<MyViewModel> {
        object : AbstractSavedStateViewModelFactory(this, null) {
            override fun <T : ViewModel?> create(
                key: String,
                modelClass: Class<T>,
                handle: SavedStateHandle
            ): T {
                @Suppress("UNCHECKED_CAST")
                return MyViewModel(application, handle) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startSecondActivity.setOnClickListener {
            startActivity(Intent(this, SecondActivity::class.java))
        }

        getSaveState.setOnClickListener {
            myViewModel.getSavedStateData().observe(this, Observer {
                println(it)
            })
        }

        // 测试新的LiveData函数,旋转屏幕
        myViewModel.testLiveData.observe(this, Observer {
            println("receive $it")
        })

        //返回键监听
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                println("接收到按下返回键")
            }
        })

        //测试ViewModel SaveState
        myViewModel.saveDataInSaveState()

    }
}