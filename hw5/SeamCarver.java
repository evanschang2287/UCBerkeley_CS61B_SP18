import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

public class SeamCarver {
    private Picture pic;
    private int width;
    private int height;

    public SeamCarver(Picture picture) {
        pic = new Picture(picture);
        width = pic.width();
        height = pic.height();
    }

    public Picture picture() {
        return new Picture(pic);
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    private int changeX(int x, int d) {
        if (x + d == width) {
            return 0;
        } else if (x + d < 0) {
            return width - 1;
        } else {
            return x + d;
        }
    }

    private int changeY(int y, int d) {
        if (y + d == height) {
            return 0;
        } else if (y + d < 0) {
            return height - 1;
        } else {
            return y + d;
        }
    }

    private int calEnergyX(int x, int y) {
        int prevX = changeX(x, -1);
        int nextX = changeX(x, 1);

        Color prev = pic.get(prevX, y);
        Color next = pic.get(nextX, y);

        int rX = Math.abs(prev.getRed() - next.getRed());
        int gX = Math.abs(prev.getGreen() - next.getGreen());
        int bX = Math.abs(prev.getBlue() - next.getBlue());

        return rX * rX + gX * gX + bX * bX;
    }

    private int calEnergyY(int x, int y) {
        int prevY = changeY(y, -1);
        int nextY = changeY(y, 1);

        Color prev = pic.get(x, prevY);
        Color next = pic.get(x, nextY);

        int rY = Math.abs(prev.getRed() - next.getRed());
        int gY = Math.abs(prev.getGreen() - next.getGreen());
        int bY = Math.abs(prev.getBlue() - next.getBlue());

        return rY * rY + gY * gY + bY * bY;
    }

    public double energy(int x, int y) {
        if (x < 0 || x >= width) {
            throw new IndexOutOfBoundsException();
        }
        if (y < 0 || y >= height) {
            throw new IndexOutOfBoundsException();
        }

        return calEnergyX(x, y) + calEnergyY(x, y);
    }

    private int minEnergyVertical(int x, int y) {
        double minE = Double.MAX_VALUE;
        int diff = -1;
        for (int d = -1; d <= 1; d++) {
            int xPos = changeX(x, d);
            int yPos = changeY(y, 1);
            double e = energy(xPos, yPos);
            if (e < minE) {
                minE = e;
                diff = d;
            }
        }
        return diff;
    }

    private int minEnergyHorizontal(int x, int y) {
        double minE = Double.MAX_VALUE;
        int diff = -1;
        for (int d = -1; d <= 1; d++) {
            int xPos = changeX(x, 1);
            int yPos = changeY(y, d);
            double e = energy(xPos, yPos);
            if (e < minE) {
                minE = e;
                diff = d;
            }
        }
        return diff;
    }

    public int[] findHorizontalSeam() {
        int[] horizontalSeam = new int[width];

        int startY = 0;
        double minE = Double.MAX_VALUE;
        for (int y = 0; y < height; y++)  {
            double e = energy(0, y);
            if (e < minE) {
                minE = e;
                startY = y;
            }
        }
        horizontalSeam[0] = startY;

        int yPos = startY;
        for (int x = 0; x < width - 1; x++) {
            int diff = minEnergyHorizontal(x, yPos);
            yPos += diff;
            horizontalSeam[x + 1] = yPos;
        }

        return horizontalSeam;
    }

    public int[] findVerticalSeam() {
        int[] verticalSeam = new int[height];

        int startX = 0;
        double minE = Double.MAX_VALUE;
        for (int x = 0; x < width; x++) {
            double e = energy(x, 0);
            if (e < minE) {
                minE = e;
                startX = x;
            }
        }
        verticalSeam[0] = startX;

        int xPos = startX;
        for (int y = 0; y < height - 1; y++) {
            int diff = minEnergyVertical(xPos, y);
            xPos += diff;
            verticalSeam[y + 1] = xPos;
        }

        return verticalSeam;
    }

    private boolean validSeam(int[] seam) {
        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                return false;
            }
        }
        return true;
    }

    public void removeHorizontalSeam(int[] seam) {
        if (validSeam(seam)) {
            pic = new Picture(SeamRemover.removeHorizontalSeam(pic, seam));
            height--;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void removeVerticalSeam(int[] seam) {
        if (validSeam(seam)) {
            pic = new Picture(SeamRemover.removeVerticalSeam(pic, seam));
            width--;
        } else {
            throw new IllegalArgumentException();
        }
    }
}
