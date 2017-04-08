package com.stuypulse.frc2017.util;

/*
 * Stores data, has logging and writing capabilities
 */
public class DataBuffer {
    private StringBuilder builder;

    private String separator;

    public DataBuffer() {
        this("\n"); // default is newline
    }

    public DataBuffer(String separator) {
        builder = new StringBuilder();
        this.separator = separator;
    }

    public void addData(String data) {
        builder.append(data).append(separator);
    }

    public void write(String file) {
        FileHandler.writeToFile(builder.toString(), file);
    }

}
