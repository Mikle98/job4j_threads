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
                        queue.poll();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
        );
        Thread threadProducer = new Thread(
                () -> {
                    queue.offer(1);
                }
        );
        threadConsumer.start();
        Thread.sleep(2500);
        assertThat(threadConsumer.getState()).isEqualTo(Thread.State.WAITING);
        threadProducer.start();
        threadProducer.join();
        assertThat(threadConsumer.getState()).isEqualTo(Thread.State.RUNNABLE);
        threadConsumer.join();
    }
}