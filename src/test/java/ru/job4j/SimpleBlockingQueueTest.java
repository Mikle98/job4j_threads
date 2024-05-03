package ru.job4j;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class SimpleBlockingQueueTest {
    @Test
    public void consumerWaitProducer() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue();
        Thread threadConsumer = new Thread(
                () -> {
                    try {
                        System.out.println(queue.poll());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
        );
        Thread threadProducer = new Thread(
                () -> queue.offer(1)
        );
        threadProducer.start();
        threadProducer.join();
        threadConsumer.start();
        threadConsumer.join();
    }
}