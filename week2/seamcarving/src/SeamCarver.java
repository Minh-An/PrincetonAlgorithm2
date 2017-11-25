import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {

    private Picture picture;
    int width;
    int height;
    private int[][] colors;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new java.lang.IllegalArgumentException("Picture is not given (null).");
        }
        this.picture = new Picture(picture);
        width = picture.width();
        height = picture.height();
        colors = new int[width()][height()];
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                colors[i][j] = picture.get(i,j).getRGB();
            }
        }
    }

    // current picture
    public Picture picture() {
        return picture;
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x > width() - 1) {
            throw new java.lang.IllegalArgumentException("x is out of range.");
        }
        if (y < 0 || y > height() - 1) {
            throw new java.lang.IllegalArgumentException("y is out of range.");
        }
        //check if the pixel is the border
        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1) {
            return 1000.0;
        }
        double xDiff = colorDifferenceSquared(colors[x - 1][ y],colors[x + 1][y]);
        double yDiff = colorDifferenceSquared(colors[x][y - 1],colors[x][y + 1]);
        return Math.sqrt(xDiff + yDiff);
    }

    private double colorDifferenceSquared(int x, int y) {

        int redDiff = Math.abs((x >> 16 & 0xFF) - (y >> 16 & 0xFF));
        int blueDiff = Math.abs((x >> 8 & 0xFF) - (y >> 16 & 0xFF));
        int greenDiff = Math.abs((x & 0xFF) - (y & 0xFF));
        return Math.pow(redDiff, 2) + Math.pow(blueDiff, 2) + Math.pow(greenDiff, 2);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transpose(false);
        int[] seam = findVerticalSeam();
        transpose(true);
        return seam;
    }

    private void transpose(boolean retranspose) {
        height = colors.length;
        width = colors[0].length;
        int[][] temp = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                temp[i][j] = colors[j][i];
            }
        }
        colors = temp;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] energy = new double[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                energy[i][j] = energy(i,j);
            }
        }
        SeamFinder seamFinder = new SeamFinder(energy);
        return seamFinder.seam();
    }


    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) {
            throw new java.lang.IllegalArgumentException("Seam is not given (null).");
        }
//        for (int i = 0; i < energy.length; i++) {
//            System.arraycopy(energy[i], seam[i], energy[i], seam[i]+1, );
//            energy[seam[i]][i] = -1;
//        }

        height--;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new java.lang.IllegalArgumentException("Seam is not given (null).");
        }
        width--;
        transpose(false);
        removeHorizontalSeam(seam);
        transpose(true);
        height++;

    }

}