import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.awt.*;

public class SeamCarver {

    Picture picture;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture)
    {
        if(picture == null)
        {
            throw new java.lang.IllegalArgumentException("Picture is not given (null).");
        }
        this.picture = new Picture(picture);
    }

    // current picture
    public Picture picture()
    {
        return picture;
    }

    // width of current picture
    public int width()
    {
        return picture.width();
    }

    // height of current picture
    public int height()
    {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y)
    {
        if(x < 0 || x > width()-1)
        {
            throw new java.lang.IllegalArgumentException("x is out of range.");
        }
        if(y < 0 || y > height()-1)
        {
            throw new java.lang.IllegalArgumentException("y is out of range.");
        }
        //check if the pixel is the border
        if(x == 0 || x == width()-1 || y == 0 || y == height()-1)
        {
            return 1000.0;
        }
        double xDiff = colorDifferenceSquared(picture.get(x-1, y), picture.get(x+1, y));
        double yDiff = colorDifferenceSquared(picture.get(x, y-1), picture.get(x, y+1));
        return Math.sqrt(xDiff + yDiff);
    }

    private double colorDifferenceSquared(Color x, Color y)
    {
        int redDiff = Math.abs( x.getRed() - y.getRed());
        int blueDiff = Math.abs( x.getBlue() - y.getBlue());
        int greenDiff = Math.abs( x.getGreen() - y.getGreen());
        return Math.pow(redDiff, 2) + Math.pow(blueDiff, 2) + Math.pow(greenDiff, 2);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam()
    {
        transpose();
        int[] seam = findVerticalSeam();
        transpose();
        return seam;
    }

    private void transpose()
    {
        //TODO transpose image???
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam()
    {
        double[][] energy = new double[width()][height()];
        for(int i = 0; i < width(); i++)
        {
            for(int j = 0; j < height(); j++)
            {
                energy[i][j] = energy(i,j);
            }
        }

        return null;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam)
    {
        if(seam == null)
        {
            throw new java.lang.IllegalArgumentException("Seam is not given (null).");
        }
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam)
    {
        if(seam == null)
        {
            throw new java.lang.IllegalArgumentException("Seam is not given (null).");
        }
    }

}