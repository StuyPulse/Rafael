package com.stuypulse.frc2017.util.recorder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileIO {

    public static void writeFile(String contents, String path) {
        // Ahhhhh try with resources
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(contents);
        } catch (IOException e) {
            // I mean, what else are you supposed to do?
            e.printStackTrace();
        }
    }

    public static String readFile(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            StringBuilder result = new StringBuilder();
            String current;
            while ((current = reader.readLine()) != null) {
                result.append(current);
            }
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
