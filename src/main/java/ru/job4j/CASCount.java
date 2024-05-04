package ru.job4j;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@ThreadSafe
public class CASCount {
    private final AtomicInteger count = new AtomicInteger();

    public void increment() {
        int newCount;
        do {
            newCount = count.get() + 1;
        } while (!count.compareAndSet(count.get(), newCount));
    }

    public int get() {
        return count.get();
    }
}
