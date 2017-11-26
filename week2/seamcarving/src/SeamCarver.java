import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

/*
Data type that resizes a picture using the seam carving method
The seam carving method calculates the energy of each pixel in a picture,
finds the seam of pixels across the image with the smallest total energy, and removes it
 */
public class SeamCarver {

    private Picture picture;
    private int width;
    private int height;
    private int[][] colors; //2D array of RGB integers representing the picture

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("Picture is not given (null).");
        }

        this.picture = new Picture(picture);
        width = picture.width();
        height = picture.height();

        colors = new int[width()][height()];
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                colors[i][j] = picture.get(i, j).getRGB();
            }
        }
    }

    // current picture
    public Picture picture() {
        picture = new Picture(width, height);
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                picture.set(i, j, new Color(colors[i][j]));
            }
        }
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

        double xDiff = colorDifferenceSquared(colors[x - 1][y], colors[x + 1][y]);
        double yDiff = colorDifferenceSquared(colors[x][y - 1], colors[x][y + 1]);
        return Math.sqrt(xDiff + yDiff);
    }

    private double colorDifferenceSquared(int x, int y) {
        int xRed = (x >> 16) & 0xFF;
        int yRed = (y >> 16) & 0xFF;
        int redDiff = Math.abs(xRed - yRed);

        int xBlue = (x >> 8) & 0xFF;
        int yBlue = (y >> 8) & 0xFF;
        int blueDiff = Math.abs(xBlue - yBlue);

        int xGreen = x & 0xFF;
        int yGreen = y & 0xFF;
        int greenDiff = Math.abs(xGreen - yGreen);

        return Math.pow(redDiff, 2) + Math.pow(blueDiff, 2) + Math.pow(greenDiff, 2);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transpose();
        int[] seam = findVerticalSeam();
        transpose();
        return seam;
    }


    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {

        //construct energy array
        double[][] energy = new double[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                energy[i][j] = energy(i, j);
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

        height--;
        for (int i = 0; i < width; i++) {
            int x = seam[i];
            System.arraycopy(colors[i], x + 1, colors[i], x, height - x);
        }
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new java.lang.IllegalArgumentException("Seam is not given (null).");
        }
        transpose();
        removeHorizontalSeam(seam);
        transpose();

    }

    //transpose the colors array representing the image
    private void transpose() {
        int newHeight = width;
        int newWidth = height;
        int[][] temp = new int[newWidth][newHeight];
        for (int i = 0; i < newWidth; i++) {
            for (int j = 0; j < newHeight; j++) {
                temp[i][j] = colors[j][i];
            }
        }
        colors = temp;
        width = newWidth;
        height = newHeight;
    }
}