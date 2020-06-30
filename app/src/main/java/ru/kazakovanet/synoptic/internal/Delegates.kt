package ru.kazakovanet.synoptic.internal

import kotlinx.coroutines.*

/**
 * Created by NKazakova on 30.06.2020.
 */

fun <T> lazyDeferred(block: suspend CoroutineScope.() -> T): Lazy<Deferred<T>> {
    return lazy {
        GlobalScope.async(start = CoroutineStart.LAZY) {
            block.invoke(this)
        }
    }
}