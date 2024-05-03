package ru.job4j;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@ThreadSafe
public class CASCount {
    private final AtomicInteger count = new AtomicInteger();

    public void increment() {
        try {
            count.incrementAndGet();
        } catch (UnsupportedOperationException e) {
            throw new UnsupportedOperationException("Count is not impl.");
        }
    }

    public int get() {
        try {
            return count.get();
        } catch (UnsupportedOperationException e) {
            throw new UnsupportedOperationException("Count is not impl.");
        }
    }
}
