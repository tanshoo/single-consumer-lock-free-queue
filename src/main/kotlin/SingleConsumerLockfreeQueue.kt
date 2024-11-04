import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.AtomicRef

/**
 * A single-consumer, lock-free queue based on singly linked lists.
 *
 * @param T the type of elements in the queue
 */
class SingleConsumerLockfreeQueue<T> {
    /**
     * Queue node, represents an element in the queue.
     * Contains the value of the element and a reference to the next node.
     */
    private class Node<T>(
        val value: T?,
        val next: AtomicRef<Node<T>?> = atomic(null)
    )

    private val head = atomic(Node<T>(null))
    private val tail = atomic(head.value)

    /**
     * Adds the specified element to the end of the queue.
     *
     * @param value the element to be added to the queue
     */
    fun enqueue(value: T) {
        val newNode = Node(value)
        while (true) {
            val currTail = tail.value
            // Attempt to insert the new node at the end of the queue
            if (currTail.next.compareAndSet(null, newNode)) {
                // Update the tail to point to the new node if it is still the last node
                tail.compareAndSet(currTail, newNode)
                return
            } else {
                //  If some other thread has already added a new node,
                //  update the tail to point to it
                tail.compareAndSet(currTail, currTail.next.value!!)
            }
        }
    }

    /**
     * Removes and returns the element at the front of the queue.
     * Caution: Works only if there is a single consumer.
     *
     * @return the element at the front of the queue
     * or null if the queue is empty
     */
    fun dequeue(): T? {
        // Return null if the queue is empty
        val nextNode = head.value.next.value ?: return null
        // Update the head to point to the next node
        head.value = nextNode
        return nextNode.value
    }

        /**
         *  Checks if the queue is empty.
         *
         *  @return true if the queue is empty, false otherwise
         */
        fun isEmpty(): Boolean {
            return head.value.next.value == null
        }
}
