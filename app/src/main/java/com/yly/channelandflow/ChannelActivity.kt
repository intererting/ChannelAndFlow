package com.yly.channelandflow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.flow.*
import kotlin.coroutines.suspendCoroutine

class ChannelActivity : AppCompatActivity(R.layout.activity_channel) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        basicChannel()

//        produceChannel()

//        pipeChannel()

//        replacePipeWithIterator()

//        sendChannel()

//        tickerChannel()

//        channelWithFlow()
    }

    private fun channelWithFlow() {
        lifecycleScope.launchWhenCreated {
//            channelFlow {
//                launch(Dispatchers.IO) {
//                    send(1)
//                }
//                launch(Dispatchers.Main) {
//                    send("a")
//                }
//            }.collect { loge(it) }

            flow {
                launch(Dispatchers.IO) {
                    emit(1)
                }
            }.collect { loge(it) }
        }

        //flow 和channelflow的区别：channelflow中可以使用协程发送数据

    }

    private fun tickerChannel() {
        val channel = ticker(delayMillis = 1000)
        lifecycleScope.launchWhenCreated {
            for (data in channel) {
                loge(channel)
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

//        for (data in iterator) {
//            loge(data)
//        }

        repeat(5) {
            loge(iterator.next())
        }

    }

    private fun pipeChannel() {

        lifecycleScope.launchWhenCreated {
//            val channel = square(produceNumbers())
//            repeat(5) {
//                loge(channel.receive())
//            }
//            coroutineContext.cancelChildren()


            var curent = produceNumbers()
            repeat(5) {
                val value = curent.receive()
                loge(value)
                curent = filter(curent, value)
            }
        }

    }

    fun CoroutineScope.produceNumbers() = produce {
//        var x = 1
        var x = 2
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

    private fun produceChannel() {
        lifecycleScope.launch {
            val channel: ReceiveChannel<Int> = produce {
                for (i in 0..5) {
                    send(i)
                }
            }

//            for (data in channel) {
//                loge(data)
//            }

            //consumeEach底层会cancel掉管道，如果多协程取数据的话就会不安全
//            channel.consumeEach {
//                loge(it)
//            }

//            channel.consumeAsFlow().collectLatest {
//                loge(it)
//            }
        }
    }

    private fun basicChannel() {
        val channel = Channel<Int>(3)
        lifecycleScope.launchWhenCreated {
            for (i in 0 until 10) {
                //suspend
                channel.send(i)
            }
            channel.close()
            loge("send finish")
        }
        lifecycleScope.launchWhenCreated {
//            repeat(11) {
            repeat(10) {
                //suspend
//                loge(channel.receive())

//                loge(channel.receiveOrNull())

//                channel.receiveAsFlow().collectLatest {
////                    delay(10)
//                    //collectLatest只会接收到flow里面最新的数据
//                    loge(it)
//                }

//                for (data in channel) {
//                    loge(data)
//                }
            }
            loge("receive finish")
        }
    }
}