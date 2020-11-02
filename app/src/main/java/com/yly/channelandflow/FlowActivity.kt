package com.yly.channelandflow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_flow.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.math.BigInteger

class FlowActivity : AppCompatActivity(R.layout.activity_flow) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        flowTest.setOnClickListener {
            testCancelable()
        }
//            testSharedFlow()

//            testStateFlow()

//        testThreadSwitch()

//        testFibo()

//        testLifeCycleScope()

//        testSequence()
//
//        basicFlow()

//        cancelFlow()

//        lifecycleScope.launchWhenCreated {
//            flowCreation()
//        }

//        flowOp()

    }

    private fun testCancelable() {
        //sampleStart
        fun main() = runBlocking<Unit> {
            (1..5).asFlow().cancellable().collect { value ->
                if (value == 3) cancel()
                println(value)
            }
        }
    }

    private fun testStateFlow() {
        GlobalScope.launch {
            val stateFlow = MutableStateFlow("first")
            stateFlow.tryEmit("second")
            stateFlow.asStateFlow().collect {
                println(it)
            }
            stateFlow.value = "third"
        }
    }

    private fun testThreadSwitch() {
        //2020-10-28 10:55:54.941 5329-5355/com.yly.channelandflow I/System.out: main Thread[DefaultDispatcher-worker-2,5,main]
        //2020-10-28 10:55:54.942 5329-5355/com.yly.channelandflow I/System.out: map  Thread[DefaultDispatcher-worker-2,5,main]
        //2020-10-28 10:55:54.944 5329-5329/com.yly.channelandflow I/System.out: onEach  Thread[main,5,main]
        flow {
            println("main ${Thread.currentThread()}")
            emit("1")
        }.map {
            println("map  ${Thread.currentThread()}")
            it
        }.flowOn(Dispatchers.IO)
            .onEach {
                println("onEach  ${Thread.currentThread()}")
            }.launchIn(CoroutineScope(Dispatchers.Main))
    }

    private fun testFibo() {
        GlobalScope.launch {
            fibonacci().take(10).collect {
            }
        }
    }

    private fun fibonacci(): Flow<BigInteger> = flow {
        var x = BigInteger.ZERO
        var y = BigInteger.ONE
        while (true) {
            emit(x)
            x = y.also {
                y += x
            }
            println("x    $x")
            println("y    $y")
        }
    }


    /**
     * sharedFlow和stateFlow的区别
     *
     * State flow always has an initial value, replays one most recent value to new subscribers, does not buffer any
     * more values, but keeps the last emitted one, and does not support [resetReplayCache][MutableSharedFlow.resetReplayCache].
     *
     * Use [SharedFlow] when you need a [StateFlow] with tweaks in its behavior such as extra buffering, replaying more
     * values, or omitting the initial value.
     */
    private fun testSharedFlow() {
        GlobalScope.launch {
            val shared = MutableSharedFlow<String>(
                replay = 2, //缓存最近3个
            )
            shared.map {
                it
            }.flowOn(Dispatchers.IO)//flowOn只会影响到之前的操作符的执行环境
                .onEach {
                    println(it)
                }
                .launchIn(CoroutineScope(Dispatchers.Main))
            shared.tryEmit("1")
            shared.tryEmit("2")
            shared.tryEmit("3")
            shared.tryEmit("4")
            shared.tryEmit("5")
        }
    }

    private fun testLifeCycleScope() {
        lifecycleScope.launchWhenCreated {
            kotlin.coroutines.coroutineContext[Job]?.invokeOnCompletion {
                loge("invokeOnCompletion")
            }

            launch {
                repeat(100) {
                    delay(1000)
                }
            }
        }
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