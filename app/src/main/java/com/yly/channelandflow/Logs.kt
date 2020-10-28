package com.yly.channelandflow

import android.util.Log
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.loge(value: Any?) {
    if (BuildConfig.DEBUG) {
        Log.e("loge", value?.toString() ?: "null")
    }
}