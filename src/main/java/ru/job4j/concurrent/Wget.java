package ru.job4j.concurrent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Wget implements Runnable {
    private final String url;
    private final int speed;

    public Wget(String url, int speed) {
        this.url = url;
        this.speed = speed;
    }

    @Override
    public void run() {
        var startAt = System.currentTimeMillis();
        var file = new File("tmp.xml");
        String url = this.url;
        try (var input = new URL(url).openStream();
             var output = new FileOutputStream(file)) {
            System.out.println("Open connection: " + (System.currentTimeMillis() - startAt) + " ms");
            var dataBuffer = new byte[512];
            int bytesRead;
            while ((bytesRead = input.read(dataBuffer, 0, dataBuffer.length)) != -1) {
                var downloadAt = System.nanoTime();
                output.write(dataBuffer, 0, bytesRead);
                System.out.println("Read 512 bytes : " + (System.nanoTime() - downloadAt) + " nano.");
                if (this.speed < 6000) {
                    try {
                        Thread.sleep(((1000000 / (System.nanoTime() - downloadAt)) * 512) / this.speed);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(Files.size(file.toPath()) + " bytes");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        paramValid(args);
        String url = args[0];
        int speed = Integer.parseInt(args[1]);
        Thread wget = new Thread(new Wget(url, speed));
        wget.start();
        wget.join();
    }

    public static void paramValid(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("Need 2 params");
        }
        Pattern patternUrl = Pattern.compile("^((http|https|ftp):\\/\\/)?(([A-Z0-9][A-Z0-9_-]*)(\\.[A-Z0-9][A-Z0-9_-]*)+)");
        Pattern patternNum = Pattern.compile("\\\\d+");
        Matcher matcherUrl = patternUrl.matcher(args[0]);
        Matcher matcherNum = patternNum.matcher(args[1]);
        if (matcherUrl.find()) {
            throw new IllegalArgumentException("First param must be url");
        }
        if (matcherNum.find()) {
            throw new IllegalArgumentException("Second param must be Num");
        }
    }
}