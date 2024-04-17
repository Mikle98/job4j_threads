package ru.job4j.concurrent;

public class ConsoleProgress implements Runnable {

    public static void main(String[] args) throws InterruptedException {
        Thread progress = new Thread(new ConsoleProgress());
        progress.start();
        Thread.sleep(5000);
        progress.interrupt();
    }

    @Override
    public void run() {
        try {
            var process = new char[] {'-', '\\', '|', '/'};
            var i = 0;
            while (!Thread.currentThread().isInterrupted()) {
                    i = i > process.length - 1 ? 0 : i;
                    System.out.print("\r load: " + process[i]);
                    Thread.sleep(500);
                    i++;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
