package example.com;

import java.util.concurrent.RecursiveAction;

public class ParallelFJImageFilter extends RecursiveAction {

    public static final long threshold = 5001;

    private final int[] src;
	private final int[] dst;
	private final int width;
	private final int height;
	private final int start;
    private final int end;

	private final int NRSTEPS = 1;

	public ParallelFJImageFilter(int[] src, int[] dst, int w, int h, int start, int end) {
		this.src = src;
		this.dst = dst;
		this.width = w;
		this.height = h;
		this.start = start;
		this.end = end;
	}

    @Override
    protected void compute() {
        if ( end - start <= 5000) {
			apply();
        } else {
			ParallelFJImageFilter firstTask = new ParallelFJImageFilter(src, dst, width, height / 2, start, 4999);
			ParallelFJImageFilter secondTask = new ParallelFJImageFilter(src, dst, width, height / 2, 5000, end);
			ParallelFJImageFilter initialTask = new ParallelFJImageFilter(src, dst, width, 4, 4800, 5300);
	
			firstTask.fork();
			secondTask.fork();
			firstTask.join();
			secondTask.join();
			initialTask.fork();
			initialTask.join();
		}
    }

    private void apply() {
		// final int index, pixel;
		// for (int steps = 0; steps < NRSTEPS; steps++) {
			// for (int i = start; i < height - 1; i++) {
			for (int i = 1; i < height - 1; i++) {
				for (int j = 1; j < width - 1; j++) {
					float rt = 0, gt = 0, bt = 0;
					for (int k = i - 1; k <= i + 1; k++) {
						
						rt += (float) ((src[k * width + j - 1+ start] & 0x00ff0000) >> 16);
						gt += (float) ((src[k * width + j - 1+ start] & 0x0000ff00) >> 8);
						bt += (float) ((src[k * width + j - 1+ start] & 0x000000ff));
						// System.out.println("index (with -1):" + index);

						rt += (float) ((src[k * width + j+ start] & 0x00ff0000) >> 16);
						gt += (float) ((src[k * width + j+ start] & 0x0000ff00) >> 8);
						bt += (float) ((src[k * width + j+ start] & 0x000000ff));
						// System.out.println("index (with 0):" + index);

						rt += (float) ((src[k * width + j + 1+ start] & 0x00ff0000) >> 16);
						gt += (float) ((src[k * width + j + 1+ start] & 0x0000ff00) >> 8);
						bt += (float) ((src[k * width + j + 1+ start] & 0x000000ff));
						// System.out.println("index (with +1):" + index);
					}
					// Re-assemble destination pixel.
					// index = ;
					
					// System.out.println("index (row * NUMCOLS) + column:" + ((i * width) + j + start));
					System.out.println("index (with i):" + ((i * width) + j + start));
					int dpixel = (0xff000000) | (((int) rt / 9) << 16) | (((int) gt / 9) << 8) | (((int) bt / 9));
					dst[i * width + j+ start] = dpixel;
				}
			}
			// swap references
			compAndSwap(src, dst);
			// int[] help;
			// help = src;
			// src = dst;
			// dst = help;
		// }
	}

	private void compAndSwap(int source[], int dest[]) {
		int[] help;
		help = source;
		source = dest;
		dest = help;
    }
}