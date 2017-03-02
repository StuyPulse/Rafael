package com.stuypulse.frc2017.robot.cv;

import java.io.File;

import org.opencv.core.Mat;

import stuyvision.ModuleRunner;
import stuyvision.capture.DeviceCaptureSource;
import stuyvision.capture.ImageCaptureSource;
import stuyvision.gui.VisionGui;

public class Main {

    final static String BOILER_IMAGE_PATH = "/images/LED Boiler/";
    final static String LIFT_IMAGE_PATH = "/images/LED Peg/";
    final static String LIFT_TEST_IMAGES_PATH = "/images/FieldTestImages/";

    final static int MAX_IMAGES = 16;

    public static void main(String[] args) {
        ModuleRunner runner = new ModuleRunner(5);
        // processBoilerSamples(runner);
        // processLiftSamples(runner);
        processLiftTestImages(runner);
        //processCamera(runner);
        VisionGui.begin(args, runner);
    }

    public static void processCamera(ModuleRunner runner) {
        DeviceCaptureSource cam = Cameras.initializeCamera(0);
        runner.addMapping(cam, new LiftVision());
    }

    public static File[] getFiles(String path) {
        path = System.getProperty("user.dir") + path;
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            path = path.replace('/', '\\');
        }
        File directory = new File(path);
        File[] directoryListing = directory.listFiles();
        return directoryListing;
    }

    public static void processBoilerSamples(ModuleRunner runner) {
        File[] samples = getFiles(BOILER_IMAGE_PATH);
        for (int i = 0; i < samples.length && i < MAX_IMAGES; i++) {
            String path = System.getProperty("user.dir") + BOILER_IMAGE_PATH + samples[i].getName();
            runner.addMapping(new ImageCaptureSource(path), new BoilerVision());
        }
    }

    public static void processLiftSamples(ModuleRunner runner) {
        File[] samples = getFiles(LIFT_IMAGE_PATH);
        for (int i = 0; i < samples.length && i < MAX_IMAGES; i++) {
            String path = System.getProperty("user.dir") + LIFT_IMAGE_PATH + samples[i].getName();
            runner.addMapping(new ImageCaptureSource(path), new LiftVision());
        }
    }

    public static void processLiftTestImages(ModuleRunner runner) {
        File[] imgs = getFiles(LIFT_TEST_IMAGES_PATH);
        Mat frame = new Mat();
        for (int i = 0; i < imgs.length && i < MAX_IMAGES; i++) {
            String path = System.getProperty("user.dir") + LIFT_TEST_IMAGES_PATH + imgs[i].getName();
            System.out.println(imgs[i].getName() + " - " + path);
            LiftVision v = new LiftVision();
            ImageCaptureSource img = new ImageCaptureSource(path);
            img.readFrame(frame);
            v.run(frame);
            runner.addMapping(img, v);
        }
    }
}
