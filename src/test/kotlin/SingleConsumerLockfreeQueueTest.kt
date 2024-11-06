package io.github.tanshoo.singleConsumerLockfreeQueue

import org.jetbrains.kotlinx.lincheck.annotations.*
import org.jetbrains.kotlinx.lincheck.check
import org.jetbrains.kotlinx.lincheck.strategy.managed.modelchecking.*
import org.jetbrains.kotlinx.lincheck.strategy.stress.*
import org.junit.*

class SingleConsumerLockfreeQueueTest {
    private val queue = SingleConsumerLockfreeQueue<Int>()

    @Operation
    fun enqueue(value: Int)  = queue.enqueue(value)

    @Operation(nonParallelGroup = "consumers")
    fun dequeue() = queue.dequeue()

    @Operation
    fun peek() = queue.peek()

    @Operation
    fun isEmpty() = queue.isEmpty()

    @Test
    fun stressTest() = StressOptions().check(this::class)

    @Test
    fun modelCheckingTest() = ModelCheckingOptions()
        .checkObstructionFreedom()
        .check(this::class)
}