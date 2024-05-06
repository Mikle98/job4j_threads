package ru.job4j.pool;

import ru.job4j.SimpleBlockingQueue;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

public class ThreadPool {
    private final List<Thread> threads = new LinkedList<>();
    private final SimpleBlockingQueue<Runnable> tasks;

    public ThreadPool(int value) {
        tasks = new SimpleBlockingQueue<>(value);
        int size = Runtime.getRuntime().availableProcessors();
        IntStream.range(0, size)
                .forEach(iterate ->
                    threads.add(new Thread(
                            () -> {
                                while (!Thread.currentThread().isInterrupted()) {
                                    try {
                                        tasks.poll();
                                    } catch (InterruptedException e) {
                                        Thread.currentThread().interrupt();
                                    }
                                }
                            }
                    ))
                );
        threads.forEach(Thread::start);
    }

    public void work(Runnable job) throws InterruptedException {
        tasks.offer(job);
    }

    public void shutdown() throws InterruptedException {
        threads.forEach(Thread::interrupt);
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadPool threadPool = new ThreadPool(10);
        IntStream.range(0, 100)
                .forEach(iterate -> {
                        try {
                            threadPool.work(System.out::println);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                });
        threadPool.shutdown();
        System.out.println(threadPool.tasks.isEmpty());
        threadPool.threads.forEach(thread -> System.out.println(thread.getState()));
    }
}
