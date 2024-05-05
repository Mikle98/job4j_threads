package ru.job4j;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

@ThreadSafe
public class SimpleBlockingQueue<T> {
    @GuardedBy("this")
    private final Queue<T> queue = new LinkedList<>();
    private int max;

    public SimpleBlockingQueue(int value) {
        max = value;
    }
    public synchronized void offer(T value) throws InterruptedException {
        while (queue.size() == max) {
            this.wait();
        }
        queue.offer(value);
        this.notifyAll();
    }

    public synchronized T poll() throws InterruptedException {
        while (queue.isEmpty()) {
            this.wait();
        }
        T result = queue.poll();
        this.notifyAll();
        return result;
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
