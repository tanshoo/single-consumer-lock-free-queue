# SingleConsumerLockfreeQueue

Kotlin implementation of a lock-free queue with a single reader and multiple writers. The queue is based on a singly linked list with a sentinel node at the head.

The queue supports:
* enqueue (thread-safe)
* dequeue (*not* thread-safe)
* peek (thread-safe)
* isEmpty (thread-safe)