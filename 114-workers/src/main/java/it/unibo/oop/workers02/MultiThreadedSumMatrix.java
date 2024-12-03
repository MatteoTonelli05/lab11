package it.unibo.oop.workers02;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class MultiThreadedSumMatrix implements SumMatrix {

    private final int nthread;

    /**
     * constructor.
     * @param threads number of thread.
     */
    public MultiThreadedSumMatrix(final int threads) {
        this.nthread = threads;
    }

    /**
     * sum all number in the matrix.
     * @param matrix matrix.
     */
    @Override
    public double sum(final double[][] matrix) {
        final int matrixSize = matrix.length * matrix[0].length;
        final int size = matrixSize % nthread + matrixSize / nthread;
        final List<Worker> workers = new ArrayList<>(nthread);
        for (int start = 0; start < matrixSize; start += size) {
            workers.add(new Worker(matrix, start, size));
        }
        for (final Worker w: workers) {
            w.start();
        }
        long sum = 0;
        for (final Worker w: workers) {
            try {
                w.join();
                sum += w.getResult();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
        return sum;
    }

    private static class Worker extends Thread {
        private final double[][] matrix;
        private final int startpos;
        private final int nelem;
        private long res;

        /**
         * Build a new worker.
         * @param matrix the matrix to sum
         * @param startpos the initial position for this worker
         * @param nelem the no. of elems to sum up for this worker
         */
        Worker(final double[][] matrix, final int startpos, final int nelem) {
            super();
            this.matrix = matrix.clone();
            this.startpos = startpos;
            this.nelem = nelem;
            res = 0;
        }

        @Override
        @SuppressWarnings("PMD.SystemPrintln")
        public void run() {
            final int matrixSize = matrix.length * matrix[0].length;
            System.out.println("Working from position " + startpos + " to position " + (startpos + nelem - 1));
            for (int i = startpos; i < matrixSize && i < startpos + nelem; i++) {
                if (getxFromIndex(i) < matrix[0].length && getyFromIndex(i) < matrix.length) {
                    this.res += matrix[getxFromIndex(i)][getyFromIndex(i)];
                }
            }
        }

        public int getxFromIndex(final int index) {
            return index % matrix[0].length; 
        }

        public int getyFromIndex(final int index) {
            return (int) Math.floor((double) index / matrix[0].length); 
        }

        /**
         * Returns the result of summing up the integers within the list.
         * 
         * @return the sum of every element in the array
         */
        public long getResult() {
            return this.res;
        }

    }

}
