package example.com;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.IIOException;

import javax.imageio.ImageIO;

public final class App {
    private App() {
    }

    public static void main(String[] args) throws IOException {
        sequential_filter(args[0]);
    }

    private static void sequential_filter(String inputFilePath) throws IOException {
        BufferedImage image = null;
        String sourceFilePath = null;
        String sourceFileName = null;
        try {
            File srcFile = new File(inputFilePath);
            sourceFilePath = srcFile.getAbsolutePath();
            sourceFileName = srcFile.getName();
            System.out.println(sourceFilePath);
            image = ImageIO.read(srcFile);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Usage: java App.java <image-file>");
            System.exit(1);
        } catch (IIOException e) {
            System.out.println("Error reading image file " + sourceFileName + " !");
            System.exit(1);
        }

        System.out.println("Source image: " + sourceFileName);

        int w = image.getWidth();
        int h = image.getHeight();
        System.out.println("Image size is " + w + "x" + h);
        System.out.println();

        int[] src = image.getRGB(0, 0, w, h, null, 0, w);
        int[] dst = new int[src.length];

        System.out.println("Starting sequential image filter.");

        long startTime = System.currentTimeMillis();
        ImageFilter filter0 = new ImageFilter(src, dst, w, h);
        filter0.apply();
        long endTime = System.currentTimeMillis();

        long tSequential = endTime - startTime;
        System.out.println("Sequential image filter took " + tSequential + " milliseconds.");

        BufferedImage dstImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        dstImage.setRGB(0, 0, w, h, dst, 0, w);

        File dstFile = new File(sourceFilePath + "Filtered");

        ImageIO.write(dstImage, "jpg", dstFile);

        System.out.println("Output image: " + dstFile.getName());
    }
}
