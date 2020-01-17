package example.com;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

public class ParallelFJImageFilter extends RecursiveTask {

    public static final long threshold = 10_000;

    private int[] src;
	private int[] dst;
	private int width;
	private int height;

	private final int NRSTEPS = 100;

	public ParallelFJImageFilter(int[] src, int[] dst, int w, int h) {
		this.src = src;
		this.dst = dst;
		width = w;
		height = h;
	}

    @Override
    protected Object compute() {
		return null;
    //     int length = height - width;
    //     if (length <= threshold) {
    //         return add();
    //     }

    //     ParallelFJImageFilter firstTask = new ParallelFJImageFilter(width, width + length / 2);
    //     firstTask.fork(); // start asynchronously

    //     ParallelFJImageFilter secondTask = new ParallelFJImageFilter(width + length / 2, height);

    //     Long secondTaskResult = secondTask.compute();
    //     Long firstTaskResult = firstTask.join();

    //     return firstTaskResult + secondTaskResult;

    // }

    // private long add() {
    //     long result = 0;
    //     for (int i = width; i < height; i++) {
    //         result += numbers[i];
    //     }
    //     return result;
    // }

    // public void apply() {
	// 	int index, pixel;
	// 	for (int steps = 0; steps < NRSTEPS; steps++) {
	// 		for (int i = 1; i < height - 1; i++) {
	// 			for (int j = 1; j < width - 1; j++) {
	// 				float rt = 0, gt = 0, bt = 0;
	// 				for (int k = i - 1; k <= i + 1; k++) {
	// 					index = k * width + j - 1;
	// 					pixel = src[index];
	// 					rt += (float) ((pixel & 0x00ff0000) >> 16);
	// 					gt += (float) ((pixel & 0x0000ff00) >> 8);
	// 					bt += (float) ((pixel & 0x000000ff));

	// 					index = k * width + j;
	// 					pixel = src[index];
	// 					rt += (float) ((pixel & 0x00ff0000) >> 16);
	// 					gt += (float) ((pixel & 0x0000ff00) >> 8);
	// 					bt += (float) ((pixel & 0x000000ff));

	// 					index = k * width + j + 1;
	// 					pixel = src[index];
	// 					rt += (float) ((pixel & 0x00ff0000) >> 16);
	// 					gt += (float) ((pixel & 0x0000ff00) >> 8);
	// 					bt += (float) ((pixel & 0x000000ff));
	// 				}
	// 				// Re-assemble destination pixel.
	// 				index = i * width + j;
	// 				int dpixel = (0xff000000) | (((int) rt / 9) << 16) | (((int) gt / 9) << 8) | (((int) bt / 9));
	// 				dst[index] = dpixel;
	// 			}
	// 		}
	// 		// swap references
	// 		int[] help;
	// 		help = src;
	// 		src = dst;
	// 		dst = help;
	// 	}
	}

    // public static long startForkJoinSum(long n) {
    //     long[] numbers = LongStream.rangeClosed(1, n).toArray();
    //     ForkJoinTask<Long> task = new ParallelFJImageFilter(numbers);
    //     return new ForkJoinPool().invoke(task);
    // }

}