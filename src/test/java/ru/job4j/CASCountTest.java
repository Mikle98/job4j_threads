package ru.job4j;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

class CASCountTest {
    @Test
    public void threeThreadIncrement() throws InterruptedException {
        CASCount casCount = new CASCount();
        Thread tread1 = new Thread(
                () -> IntStream.range(1, 10)
                        .forEach(el -> casCount.increment())
        );
        Thread tread2 = new Thread(
                () -> IntStream.range(1, 10)
                        .forEach(el -> casCount.increment())
        );
        Thread tread3 = new Thread(
                () -> IntStream.range(1, 10)
                        .forEach(el -> casCount.increment())
        );
        tread1.start();
        tread1.join();
        tread2.start();
        tread2.join();
        tread3.start();
        tread3.join();
        assertThat(casCount.get()).isEqualTo(27);
    }
}