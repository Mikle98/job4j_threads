package ru.job4j.pool;

import ru.job4j.SimpleBlockingQueue;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

public class ThreadPool {
    private final List<Thread> threads = new LinkedList<>();
    private final SimpleBlockingQueue<Runnable> tasks;
    private int size = Runtime.getRuntime().availableProcessors();

    public ThreadPool(int value) {
        tasks = new SimpleBlockingQueue<>(value);
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
        startAllThread();
    }

    public void work(Runnable job) throws InterruptedException {
        tasks.offer(job);
    }

    public void shutdown() throws InterruptedException {
        threads.stream()
                .filter(thread -> Thread.State.RUNNABLE.equals(thread.getState()))
                .forEach(Thread::interrupt);
    }

    private void startAllThread() {
        threads.stream()
                .forEach(thread -> thread.start());
    }

    public List<Thread> getAllThread() {
        return threads;
    }

    public SimpleBlockingQueue<Runnable> getTask() {
        return tasks;
    }
}
