package com.yly.channelandflow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_select.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.channels.receiveOrNull
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.channels.onReceiveOrNull as onReceiveNull
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.sync.Mutex
import java.lang.Exception

/**
 * @author    yiliyang
 * @date      20-10-30 上午9:05
 * @version   1.0
 * @since     1.0
 */
@kotlinx.coroutines.ExperimentalCoroutinesApi
class SelectActivity : AppCompatActivity(R.layout.activity_select) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectBasic.setOnClickListener {
            testSelect()

//            selectWithTicker()

        }
    }

    private fun selectWithTicker() {
        GlobalScope.launch {
            val ticker = ticker(delayMillis = 900)
            val producer = produce {
                delay(1000)
                send("producer")
            }
            select<Unit> {
                ticker.onReceive {
                    loge("ticker receive")
                }
                producer.onReceive {
                    println("producer  $it")
                }
            }
        }

    }

    private fun testSelect() {
        GlobalScope.launch {
            val fizz = fizz()
            val buzz = buzz()
            repeat(10) {
                selectFizzOrBuzz(fizz, buzz)
            }
//            coroutineContext.cancel()
        }
    }
}

@kotlinx.coroutines.ExperimentalCoroutinesApi
fun CoroutineScope.fizz() = produce {
    repeat(1) {
        delay(300)
        send("fizz")
//        coroutineContext.cancel()
    }
}

@kotlinx.coroutines.ExperimentalCoroutinesApi
fun CoroutineScope.buzz(): ReceiveChannel<String> {
    return produce {
        while (true) {
            delay(500)
            send("buzz")
        }
    }
}

@kotlinx.coroutines.ExperimentalCoroutinesApi
suspend fun selectFizzOrBuzz(
    fizz: ReceiveChannel<String>,
    buzz: ReceiveChannel<String>
) {
    select<Unit> {
        fizz.onReceiveNull().invoke {
            println("fizz $it")
        }

//        buzz.onReceiveNull().invoke {
//            println("buzz  $it")
//        }
        buzz.onReceive {
            println("buzz  $it")
        }
    }

}

















