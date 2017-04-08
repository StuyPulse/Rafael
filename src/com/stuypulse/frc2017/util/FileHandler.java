package com.stuypulse.frc2017.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileHandler {

    // Writes string to file
    public static void writeToFile(String content, String directory) {
        try(BufferedWriter out = new BufferedWriter(new FileWriter(directory))) {
            out.write(content);
        } catch (IOException e) {
            System.err.println("FAILED TO WRITE TO FILE " + directory);
            e.printStackTrace();
            
        }
        
    }
}
