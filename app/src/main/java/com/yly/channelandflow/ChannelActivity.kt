package com.yly.channelandflow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.flow.*
import java.lang.RuntimeException
import kotlin.coroutines.suspendCoroutine
import kotlin.math.log

class ChannelActivity : AppCompatActivity(R.layout.activity_channel) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        basicChannel()

//        produceChannel()
//
//        pipeChannel()

//        replacePipeWithIterator()

//        sendChannel()

//        tickerChannel()

//        channelWithFlow()

//        testExceptionHandler()
    }

    private fun testExceptionHandler() {
        val handler = CoroutineExceptionHandler { _, throwable -> loge(throwable.message) }
        lifecycleScope.launch(handler) {
//            throw RuntimeException("haha exception")
            async {
                testExceptionHandlerFun()
            }.await()
        }
    }

    suspend fun testExceptionHandlerFun(): String {
        throw  RuntimeException("async exception")
    }

    @ExperimentalCoroutinesApi
    private fun channelWithFlow() {
        lifecycleScope.launchWhenCreated {
            channelFlow {
                launch(Dispatchers.IO) {
                    send(1)
                }
                launch(Dispatchers.Main) {
                    send("a")
                }
            }.collect { loge(it) }

//            flow {
//                emit(1)
//            }.collect { loge(it) }
        }

        //flow 和channelflow的区别：channelflow中可以使用协程发送数据

    }

    private fun tickerChannel() {
        val channel = ticker(delayMillis = 1000)
        lifecycleScope.launchWhenCreated {
            for (data in channel) {
                loge(data)
            }
        }
    }

    private fun sendChannel() {

        val channel = Channel<Int>()

        lifecycleScope.launchWhenCreated {
            testSendChannel(channel)
            for (data in channel) {
                loge(data)
            }
        }

    }

    fun CoroutineScope.testSendChannel(channel: SendChannel<Int>) {
        launch {
            repeat(5) {
                channel.send(it)
            }
        }
    }

    /**
     *  the benefit of a pipeline that uses channels as shown above is that
     *  it can actually use multiple CPU cores if you run it in Dispatchers.Default context.
     */
    private fun replacePipeWithIterator() {

        val iterator = iterator {
            for (i in 0..5) {
                yield(i)
            }
        }

//        sequence {
//            yield(1)
//        }

//        for (data in iterator) {
//            loge(data)
//        }

        repeat(5) {
            loge(iterator.next())
        }

    }

    private fun pipeChannel() {

        lifecycleScope.launchWhenCreated {
            val channel = square(produceNumbers())
            repeat(5) {
                loge(channel.receive())
            }
//            coroutineContext.cancelChildren()


//            var curent = produceNumbers()
//            repeat(5) {
//                val value = curent.receive()
//                loge(value)
//                curent = filter(curent, value)
//            }
        }

    }

    fun CoroutineScope.produceNumbers() = produce {
        var x = 1
//        var x = 2
        while (true) send(x++) // infinite stream of integers starting from 1
    }

    fun CoroutineScope.square(numbers: ReceiveChannel<Int>): ReceiveChannel<Int> = produce {
        for (x in numbers) send(x * x)
    }

    fun CoroutineScope.filter(numbers: ReceiveChannel<Int>, prime: Int) = produce {
        for (number in numbers) {
            if (number % prime != 0) {
                send(number)
            }
        }
    }

    @ExperimentalCoroutinesApi
    private fun produceChannel() {
        lifecycleScope.launch {
            //produce会自动在协程结束的时候调用close
            val channel: ReceiveChannel<Int> = produce {
                for (i in 0..5) {
                    send(i)
                }
            }

//            for (data in channel) {
//                loge(data)
//            }

//            loge(channel.isClosedForReceive) //true
//            for (data in channel) {
            //没有数据
//                loge(data)
//            }

            //consumeEach底层会cancel掉管道，如果多协程取数据的话就会不安全
//            channel.consumeEach {
//                loge(it)
//            }

            channel.consumeAsFlow().collect {
                loge(it)
            }
            loge("consume over")
        }
    }

    private fun basicChannel() {
        val channel = Channel<Int>(3)
        lifecycleScope.launchWhenCreated {
            for (i in 0 until 4) {
                //suspend
//                println("send  $i")//0 1 2 3因为buffer为3
                channel.send(i)
            }
//            channel.close()
            loge("send finish")
        }

        lifecycleScope.launchWhenCreated {
            channel.receiveAsFlow().collectLatest {
                delay(10)
                //collectLatest只会接收到flow里面最新的数据
                loge(it)
            }

            //                for (data in channel) {
//                    loge(data)
//                }

            repeat(10) {
//            suspend
//                loge(channel.receive())

//                loge(channel.receiveOrNull())

            }
        }
    }
}