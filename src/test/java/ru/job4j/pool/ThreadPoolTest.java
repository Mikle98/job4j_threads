package ru.job4j.pool;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

class ThreadPoolTest {
    @Test
    public void checkCountThread() {
        var countTread = Runtime.getRuntime().availableProcessors();
        var pool = new ThreadPool(10);
        assertThat(pool.getAllThread().size()).isEqualTo(countTread);
    }

    @Test
    public void checkStartThread() {
        var pool = new ThreadPool(10);
        var threads = pool.getAllThread();
        threads.stream()
                .forEach(thread -> assertThat(thread.getState())
                                    .isEqualTo(Thread.State.WAITING));
    }

    @Test
    public void checkShutDownThread() throws InterruptedException {
        var pool = new ThreadPool(100);
        var threads = pool.getAllThread();
        var workThread = new Thread(
                () -> {
                    while (!Thread.currentThread().isInterrupted()) {
                        try {
                            pool.work(System.out::println);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
        );
        var shutDown = new Thread(
                () -> {
                    while (threads.stream()
                            .filter(thread -> Thread.State.TERMINATED.equals(thread.getState()))
                            .count() != Runtime.getRuntime().availableProcessors()) {
                        threads.stream()
                                .filter(thread -> Thread.State.RUNNABLE.equals(thread.getState()))
                                .forEach(Thread::interrupt);
                    }
                }
        );
        workThread.start();
        shutDown.start();
        shutDown.join();
        workThread.interrupt();
        workThread.join();
        threads.forEach(thread -> assertThat(thread.getState()).isEqualTo(Thread.State.TERMINATED));
    }

    @Test
    public void checkWork() throws InterruptedException {
        var pool = new ThreadPool(100);
        pool.work(System.out::println);
        Thread.sleep(1000);
        var rsl = pool.getTask().isEmpty();
        assertThat(rsl).isTrue();
    }
}