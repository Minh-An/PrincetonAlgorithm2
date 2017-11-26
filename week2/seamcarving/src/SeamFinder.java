import edu.princeton.cs.algs4.Queue;

/*
Helper class for SeamCarver to find the shortest seam of a picture
using the Topological order shortest path algorithm
 */
public class SeamFinder {

    private double[][] energy;
    private double[] distTo;
    private int[] edgeTo;
    private int pixels;

    public SeamFinder(double[][] energy) {
        this.energy = energy;
        pixels = energy.length * energy[0].length;
        distTo = new double[pixels];
        edgeTo = new int[pixels];

        for (int i = 0; i < pixels; i++) {
            distTo[i] = Double.MAX_VALUE;

            //initialize distTo and edgeTo for top row of pixels
            if(i < energy.length)
            {
                edgeTo[i] = -1;
                distTo[i] = 1000.0;
            }
        }

        //relax each pixel level by level
        for (int v = 0; v < pixels; v++) {
            for (int w : adj(v)) {
                relax(v, w);
            }
        }
    }

    //relax an "edge" from pixel v to w
    private void relax(int v, int w) {
        int y = w / energy.length;
        int x = w % energy.length;

        if (distTo[w] > distTo[v] + energy[x][y]) {
            distTo[w] = distTo[v] + energy[x][y];
            edgeTo[w] = v;
        }
    }

    //finds the seam, the shortest path across the energy array
    public int[] seam() {

        //finds the smallest distance from the bottom row of pixels
        double minimum = Double.MAX_VALUE;
        int index = -1;
        int offset = pixels - energy.length;

        for (int i = 0; i < energy.length; i++) {
            if (distTo[offset + i] < minimum) {
                minimum = distTo[offset + i];
                index = offset + i;
            }
        }

        //find the x values of the pixels that make up the seam
        int[] seam = new int[energy[0].length];
        int count = seam.length - 1;
        for (int v = index; v != -1; v = edgeTo[v]) {
            seam[count--] = v % energy.length;
        }
        return seam;
    }

    //finds adjacent pixels of pixel v
    private Iterable<Integer> adj(int v) {
        Queue<Integer> adj = new Queue<>();

        int x = v % energy.length;
        int y = v / energy.length;

        //if v is not in the not bottom row
        if (y != energy[0].length - 1) {

            //pixel below (x, y+1) is adjacent
            adj.enqueue(v + energy.length);

            //if not leftmost column
            if (x != 0) {
                //pixel (x-1, y+1) is adjacent
                adj.enqueue(v + energy.length - 1);

                //if not rightmost column
                if (x != energy.length - 1) {
                    adj.enqueue(v + energy.length + 1);
                }

            }
        }
        return adj;
    }

}