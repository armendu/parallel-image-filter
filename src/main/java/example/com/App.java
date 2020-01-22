package example.com;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ForkJoinPool;
import javax.imageio.IIOException;
import javax.imageio.ImageIO;

public final class App {
    private App() {
    }

    public static void main(final String[] args) throws IOException {
        // sequential_filter(args[0]);
        parallel_filter(args[0]);
        // System.out.println(ForkJoinAdd.startForkJoinSum(1_000_000));
    }

    private static void parallel_filter(final String inputFilePath) throws IOException {
        BufferedImage image = null;
        String sourceFilePath = null;
        String sourceFileName = null;
        try {
            final File srcFile = new File(inputFilePath);
            sourceFilePath = srcFile.getAbsolutePath();
            sourceFileName = srcFile.getName();
            System.out.println(sourceFilePath);
            image = ImageIO.read(srcFile);
        } catch (final ArrayIndexOutOfBoundsException e) {
            System.out.println("Usage: java App.java <image-file>");
            System.exit(1);
        } catch (final IIOException e) {
            System.out.println("Error reading image file " + sourceFileName + " !");
            System.exit(1);
        }

        System.out.println("Source image: " + sourceFileName);

        final int w = image.getWidth();
        final int h = image.getHeight();
        System.out.println("Image size is " + w + "x" + h);
        System.out.println();

        final int[] src = image.getRGB(0, 0, w, h, null, 0, w);
        final int[] dst = new int[src.length];

        System.out.println("Starting parallel image filter.");

        final long startTime = System.currentTimeMillis();
        ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());   
        ParallelFJImageFilter task = new ParallelFJImageFilter(src, dst, w, h, 0, (w * h));
        pool.execute(task);
        task.join();
        // pool.shutdown();
        // new ForkJoinPool().invoke(task);
        
        final long endTime = System.currentTimeMillis();

        final long tSequential = endTime - startTime;
        System.out.println("Sequential image filter took " + tSequential + " milliseconds.");

        final BufferedImage dstImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        dstImage.setRGB(0, 0, w, h, dst, 0, w);

        sourceFilePath.replace(sourceFileName, "");
        final File dstFile = new File(sourceFilePath + "Filtered.jpg");

        ImageIO.write(dstImage, "jpg", dstFile);
        dstImage.flush();

        System.out.println("Output image: " + dstFile.getName());
    }

    private static void sequential_filter(final String inputFilePath) throws IOException {
        BufferedImage image = null;
        String sourceFilePath = null;
        String sourceFileName = null;
        try {
            final File srcFile = new File(inputFilePath);
            sourceFilePath = srcFile.getAbsolutePath();
            sourceFileName = srcFile.getName();
            System.out.println(sourceFilePath);
            image = ImageIO.read(srcFile);
        } catch (final ArrayIndexOutOfBoundsException e) {
            System.out.println("Usage: java App.java <image-file>");
            System.exit(1);
        } catch (final IIOException e) {
            System.out.println("Error reading image file " + sourceFileName + " !");
            System.exit(1);
        }

        System.out.println("Source image: " + sourceFileName);

        final int w = image.getWidth();
        final int h = image.getHeight();
        System.out.println("Image size is " + w + "x" + h);
        System.out.println();

        final int[] src = image.getRGB(0, 0, w, h, null, 0, w);
        final int[] dst = new int[src.length];

        System.out.println("Starting sequential image filter.");

        final long startTime = System.currentTimeMillis();
        final ImageFilter filter0 = new ImageFilter(src, dst, w, h);
        filter0.apply();
        final long endTime = System.currentTimeMillis();

        final long tSequential = endTime - startTime;
        System.out.println("Sequential image filter took " + tSequential + " milliseconds.");

        final BufferedImage dstImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        dstImage.setRGB(0, 0, w, h, dst, 0, w);

        sourceFilePath.replace(sourceFileName, "");
        final File dstFile = new File(sourceFilePath + "Filtered.jpg");

        ImageIO.write(dstImage, "jpg", dstFile);

        System.out.println("Output image: " + dstFile.getName());
    }
}