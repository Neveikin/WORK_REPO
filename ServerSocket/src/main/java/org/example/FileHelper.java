package org.example;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileHelper {
    public static int writeToFile(String filename, StringBuilder data, boolean append) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filename, append))) {
            writer.write(String.valueOf(data));
            writer.newLine();
        } catch (IOException e) {
            return 0;
        }
        return 1;
    }

    public static String readFromFile(String filename) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line.trim());
            }
        } catch (IOException e) {
            return null;

        } return content.toString();
    }
}

