package com.yly.channelandflow

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_coroutines_util_class.*
import kotlinx.coroutines.*
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.coroutines.resume

/**
 * @author    yiliyang
 * @date      20-10-28 下午1:33
 * @version   1.0
 * @since     1.0
 */
class CoroutineUtilClassActivity : AppCompatActivity(R.layout.activity_coroutines_util_class) {

    val mainScope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        test.setOnClickListener {
//            testCompletableDeffer()

//            testLazyJob()

//            testCancel()

//            testCompleteable()

            testScheduler()
        }
    }

    private fun testScheduler() {
        GlobalScope.launch {


            suspendCancellableCoroutine<String> {
                it.resume("haha")
            }



            launch(CoroutineName("first")) {
                println("first coroutines")
                delay(100000)
            }
            launch(CoroutineName("second")) {
                println("second coroutines")
            }
        }
    }

    private fun testCompleteable() {
        val completableJob = SupervisorJob()
        with(CoroutineScope(completableJob)) {
            launch {
                while (this.isActive) {
                    println("ticker")
                    delay(1000)
                }
            }
        }
        GlobalScope.launch {
            delay(3000)
            completableJob.complete()
        }
    }

    private fun testCancel() {
        GlobalScope.launch {
            val job = launch {
                try {
                    repeat(100) {
                        delay(1000)
                        println("ticker")
                    }
                } catch (e: CancellationException) {
                    println("catch ${e.message}")
                }
            }

            job.invokeOnCompletion {
                println("xxx")
            }
            launch {
                delay(3000)
                job.cancel()
//                repeat(100) {
//                    println("canceled")
//                    delay(1000)
//                }
            }
        }
    }

    private fun testLazyJob() {
        val job = GlobalScope.launch(start = CoroutineStart.LAZY) {
            println("lazy")
        }

        GlobalScope.launch {
            job.start()
//                job.join()
        }
    }

    private fun testCompletableDeffer() {
        val completableDeferred = CompletableDeferred<String>()
        val handler = CoroutineExceptionHandler { _, throwable ->
            println(throwable.message)
        }
        GlobalScope.launch(handler) {
            launch {
                val result = completableDeferred.await()
                println("await $result")
            }
            launch {
                delay(2000)
//                    completableDeferred.complete("complete")
                completableDeferred.completeExceptionally(RuntimeException("error"))
            }
        }
    }
}