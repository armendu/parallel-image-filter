package com.example;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private final static Logger LOGGER = Logger.getLogger(Main.class.getName());
    private static int numberOfThreads = 8;
    private static String sourceFilePath = null;
    private static String sourceFileName = null;
    private static String srcFileName = null;

    public static void main(String[] args) throws Exception {
        BufferedImage image = null;
        try {
            srcFileName = args[0];
            File srcFile = new File(srcFileName);
            sourceFilePath = srcFile.getAbsolutePath();
            sourceFileName = srcFile.getName();
            image = ImageIO.read(srcFile);
        } catch (ArrayIndexOutOfBoundsException e) {
            LOGGER.severe("Usage: java TestAll <image-file>");
            System.exit(1);
        } catch (IIOException e) {
            LOGGER.severe("Error reading image file " + srcFileName + " !");
            System.exit(1);
        }

        LOGGER.info("Source image: " + srcFileName);

        int width = image.getWidth();
        int height = image.getHeight();
        LOGGER.info("Image size is " + width + "x" + height);
        System.out.println();

        int[] src = image.getRGB(0, 0, width, height, null, 0, width);
        int[] dst = new int[src.length];

//        int[] parallelResult = executeParallelFilter(src, dst, width, height);
        int[] sequentialResult = executeSequentialFilter(src, dst, width, height);

//        LOGGER.info("The result is: " + compareArrays(sequentialResult, parallelResult));
        LOGGER.info("Done.");
    }

    private static int[] executeParallelFilter(int[] src, int[] dst, final int width, final int height) throws IOException {
        LOGGER.info("Available processors: " + Runtime.getRuntime().availableProcessors());
        LOGGER.warning("Starting parallel image filter using " + numberOfThreads + " threads.");

        ParallelFJImageFilter filter1 = new ParallelFJImageFilter(src, dst, width, 1, height - 1);
        ForkJoinPool pool = new ForkJoinPool(numberOfThreads);

        final long startTime = System.currentTimeMillis();
        pool.invoke(filter1);
        final long endTime = System.currentTimeMillis();

        final long totalTime = endTime - startTime;

        final BufferedImage dstPImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        dstPImage.setRGB(0, 0, width, height, dst, 0, width);

        LOGGER.info("Parallel image filter took " + totalTime + " milliseconds using " + numberOfThreads + " threads.");

        final String filteredFileName = sourceFilePath.replace(sourceFileName, "Filtered" + sourceFileName);
        final File dstFile = new File(filteredFileName);

        ImageIO.write(dstPImage, "jpg", dstFile);

        LOGGER.info("\nOutput image for parallel filter: " + dstFile.getName());

        return dst;
    }

    private static int[] executeSequentialFilter(int[] src, int[] dst, final int width, final int height) throws IOException {
        LOGGER.warning("Starting sequential image filter.");

        final long startTime = System.currentTimeMillis();
        final ImageFilter filter0 = new ImageFilter(src, dst, width, height);
        filter0.apply();
        final long endTime = System.currentTimeMillis();

        final long totalTime = endTime - startTime;
        LOGGER.info("Sequential image filter took " + totalTime + " milliseconds.");

        BufferedImage dstImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        dstImage.setRGB(0, 0, width, height, dst, 0, width);

        String filteredFileName = sourceFilePath.replace(sourceFileName, "Filtered" + sourceFileName);
        final File dstFile = new File(filteredFileName);

        ImageIO.write(dstImage, "jpg", dstFile);

        LOGGER.info("\nOutput image  for sequential filter: " + dstFile.getName());

        return dst;
    }

    public static boolean compareArrays(int[] firstArray, int[] secondArray) {
        for (int i = 0; i < firstArray.length; i++) {
            if (firstArray[i] != secondArray[i]) return false;
        }
        return true;
    }
}