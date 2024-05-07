package ru.job4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmailNotification {
    private final ExecutorService pool = Executors.newCachedThreadPool();

    public void emailTo(User user) {
        pool.submit(
                () -> {
                    var subject = String.format("Notification %s to email %s",
                                                        user.username(),
                                                        user.email());
                    var body = String.format("Add a new event to %s",
                                                    user.username());
                    send(subject, body, user.email());
                }
        );
    }

    public void close() {
        pool.shutdown();
        while (!pool.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void send(String subject, String body, String email) {
        System.out.println("Send msg " + email);
    }

    public static void main(String[] args) {
        EmailNotification notification = new EmailNotification();
        User user = new User("test", "123@123.12");
        notification.emailTo(user);
        notification.close();
    }
}
