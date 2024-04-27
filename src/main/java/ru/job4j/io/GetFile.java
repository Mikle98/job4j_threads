package ru.job4j.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Predicate;

public class GetFile {
    private final File file;

    public GetFile(final File file) {
        this.file = file;
    }

    public String getContent() throws IOException {
        return content(x -> true);
    }

    public String getContentWithoutUnicode() throws IOException {
        return content(x -> x < 0x80);
    }

    public String content(Predicate<Character> filter) throws IOException {
        try (InputStream input = new FileInputStream(file)) {
            StringBuilder output = new StringBuilder();
            int data;
            while ((data = input.read(new byte[1024], 0, new byte[1024].length)) != -1) {
                if (filter.test((char) data)) {
                    output.append((char) data);
                }
            }
            return output.toString();
        }
    }
}
