package ru.job4j.concurrent;

public class Wget {
    public static void main(String[] args) {
        Thread first = new Thread(
                () -> {
                    try {
                        int index = 0;
                        while (index < 100) {
                            System.out.print("\rLoading : " + index + "%");
                            Thread.sleep(1000);
                            index++;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );
        first.start();
    }
}
