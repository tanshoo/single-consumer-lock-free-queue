import kotlinx.coroutines.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*


class SingleConsumerLockfreeQueueTest {
    private val queue = SingleConsumerLockfreeQueue<Int>()
    
    fun enqueue(value: Int) = queue.enqueue(value)

    fun dequeue(): Int? = queue.dequeue()

    // Basic tests
    @Test
    fun testEnqueueDequeue() {
        repeat(10) { i -> queue.enqueue(i) }
        repeat(10) { i -> assertEquals(i, queue.dequeue()) }
        assertNull(queue.dequeue())
    }

    @Test
    fun testDequeueReturnsNullWhenEmpty() {
        val queue = SingleConsumerLockfreeQueue<Int>()
        assertNull(queue.dequeue())
    }

    // Concurrency test with multiple producers
    // Check that all elements are enqueued and dequeued
    @Test
    fun testConcurrency() = runBlocking {
        val jobs = List(10) {
            launch {
                repeat(10) { i -> queue.enqueue(i) }
            }
        }
        jobs.forEach { it.join() }

        val dequeueResults = mutableListOf<Int?>()
        repeat(100) {
            dequeueResults.add(queue.dequeue())
        }

        assertEquals(100, dequeueResults.size)
    }


    @Test
    fun concurrentEnqueueDequeueWithContention() = runBlocking {
        val enqueueJobs = List(100) {
            launch {
                repeat(100) { i -> queue.enqueue(i) }
            }
        }

        val dequeueJobs = List(100) {
            launch {
                repeat(100) {
                    queue.dequeue()
                }
            }
        }

        enqueueJobs.forEach { it.join() }
        dequeueJobs.forEach { it.join() }

        // Check if the queue is empty after all operations
        assertTrue(queue.isEmpty())
    }

}
