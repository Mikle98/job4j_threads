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
        var file = new File(this.url.substring(this.url.lastIndexOf('/') + 1));
        String url = this.url;
        try (var input = new URL(url).openStream();
             var output = new FileOutputStream(file)) {
            int bytesRead;
            long totalBytes = 0;
            var startAt = System.currentTimeMillis();
            while ((bytesRead = input.read(new byte[this.speed], 0, new byte[this.speed].length)) != -1) {
                output.write(new byte[this.speed], 0, bytesRead);
                totalBytes += bytesRead;
                if (totalBytes >= this.speed) {
                    if ((System.currentTimeMillis() - startAt) > 1000) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    totalBytes = 0;
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