package ru.job4j.concurrent;

public class ThreadState {
    public static void main(String[] args) {
        Thread first = new Thread(
                () -> { }
        );
        Thread second = new Thread(
                () -> { }
        );
        System.out.println(first.getState());
        System.out.println(second.getState());
        first.start();
        second.start();
        System.out.println(first.getName());
        System.out.println(second.getName());
        while (first.getState() != Thread.State.TERMINATED
                || second.getState() != Thread.State.TERMINATED) {
            System.out.println();
        }
        System.out.println(first.getState());
        System.out.println(second.getState());
        System.out.printf("%s ������ ���������%n", Thread.currentThread().getName());
    }
}
