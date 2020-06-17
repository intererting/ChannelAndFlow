package com.yly.channelandflow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.collect
import kotlin.coroutines.coroutineContext
import kotlin.system.measureTimeMillis

class FlowActivity : AppCompatActivity(R.layout.activity_flow) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        lifecycleScope.launchWhenCreated {
            kotlin.coroutines.coroutineContext[Job]?.invokeOnCompletion {
                loge("invokeOnCompletion")
            }

            launch {
                repeat(100){
                    delay(1000)
                }
            }
        }

//        testSequence()
//
//        basicFlow()

//        cancelFlow()

//        lifecycleScope.launchWhenCreated {
//            flowCreation()
//        }

        flowOp()

    }

    private fun flowOp() {
        lifecycleScope.launchWhenCreated {
//            (0..2).asFlow().map { performRequest(it) }.collect { loge(it) }

//            (0..2).asFlow().transform {
//                emit("start")
//                emit(performRequest(it))
//            }.collect {
//                loge(it)
//            }

//            (0..5).asFlow().map {
//                loge("in map  $it ${Thread.currentThread()}")
//                it * 2
//            }.flowOn(Dispatchers.IO).collect {
//                loge("collect  $it  ${Thread.currentThread()}")
//            }

//            (0..3).asFlow().map {
//                loge("in map  $it ${Thread.currentThread()}")
//                it * 2
//            }.flowOn(Dispatchers.Main).onEach {
//                loge(it)
//                loge("onEach  $it ${Thread.currentThread()}")
//            }.launchIn(CoroutineScope(Dispatchers.IO))


//            val time = measureTimeMillis {
//                (0..5).asFlow().map {
//                    delay(100)
//                    it * 2
//                }.conflate().collect {
//                    delay(300)
//                    loge(it)
//                }
//            }
//
//            loge(time)


//            val time = measureTimeMillis {
//                (0..5).asFlow().map {
//                    delay(100)
//                    it * 2
//                }.flowOn(Dispatchers.IO).collectLatest {
//                    loge(it)
//                    delay(300)
//                    loge("after delay $it")
//                }
//            }

//            loge(time)

//            conflate和collectLatest不同点：conflate下一个数据来的时候不会取消上一次的监听，所以是并行处理的
//            但是collectLatest会取消上一次监听，所以每次有数据只有最新的一次监听有效，详情见源码如下
//              previousFlow?.apply {
//                    cancel(ChildCancelledException())
//                    join()
//                }
//                // Do not pay for dispatch here, it's never necessary
//                previousFlow = launch(start = CoroutineStart.UNDISPATCHED) {
//                    collector.transform(value)
//                }

//            val flowA = flowOf(1, 2, 3).onEach { delay(100) }
//            val flowB = flowOf("a", "b", "c")
//            flowA.zip(flowB) { a, b ->
//                "$a  zip   $b"
//            }.collect {
////                2020-06-17 13:57:54.315 32610-32610/com.yly.channelandflow E/loge: 1  zip   a
////2020-06-17 13:57:55.315 32610-32610/com.yly.channelandflow E/loge: 2  zip   b
////2020-06-17 13:57:56.319 32610-32610/com.yly.channelandflow E/loge: 3  zip   c
//                loge(it)
//            }

//            val flowA = flowOf(1, 2, 3).onEach { delay(100) }
//            val flowB = flowOf("a", "b", "c")
//            flowA.combine(flowB) { a, b ->
//                "$a  zip   $b"
//            }.collect {
////                2020-06-17 13:59:16.853 634-634/com.yly.channelandflow E/loge: 1  zip   c
////2020-06-17 13:59:16.990 634-634/com.yly.channelandflow E/loge: 2  zip   c
////2020-06-17 13:59:17.093 634-634/com.yly.channelandflow E/loge: 3  zip   c
//                loge(it)
//            }


//          zip和combine的不同点：zip会等待有数据一起发送，combine不会等待


//            (0..2).asFlow().map {
//                check(it < 1) {
//                    "value >= 1"
//                }
//                it
//            }.catch { loge(it) }
//                .collect { loge(it) }

            //onCompletion可以捕获到最后collect中的异常
//            (0..2).asFlow().onCompletion { error ->
//                loge("onCompletion  $error")
//            }.collect {
//                check(it < 1) {
//                    "value >=1"
//                }
//                loge(it)
//            }


//            (0..2).asFlow().debounce(100).collect {
//                loge(it)
//            }

//            flowOf(1, 2, 2, 3, 3, 4, 4, 4).distinctUntilChanged { old, new ->
//                old == new
//            }.collect { loge(it) }

//            flowOf(1, 2).onEach {
//                loge("oneach  $it")
//                check(it < 2) {
//                    "value error"
//                }
//            }.retry(3) {
//                return@retry true
//            }.collect {
//                loge(it)
//            }


            //scan和flod差不多，只不过scan会把中间过程一样发送出去
//            flowOf(1, 2).scan(10, { a, b -> a + b }).collect {
//                loge(it)
//            }

//            flowOf(1, 2, 3).asLiveData().observe(this@FlowActivity, Observer {
//                loge(it)
//            })

        }
    }

    //sampleStart
    suspend fun performRequest(request: Int): String {
        delay(3000) // imitate long-running asynchronous work
        return "response $request"
    }

    private fun CoroutineScope.flowCreation() {
//        launch {
//            (0..9).asFlow().collect { loge(it) }
//        }

//        flowOf(1, 2, 3)
    }

    private fun cancelFlow() {
        val flow = flow {
            repeat(10) {
                emit(it)
                kotlinx.coroutines.delay(100)
            }
        }

        lifecycleScope.launchWhenCreated {
            withTimeout(300) {
                flow.collect {
                    loge(it)
                }
            }
        }
    }

    private fun basicFlow() {
        val flow = flow {
            repeat(10) {
                emit(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            flow.collect {
                loge(it)
            }

        }
    }

    private fun testSequence() {
        val sequence = sequence {
            repeat(5) {
                yield(it)
            }
        }

        for (data in sequence) {
            loge(data)
        }
    }
}