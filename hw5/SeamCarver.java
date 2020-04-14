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

    private double minEnergy(double leftE, double topE, double rightE) {
        if (leftE <= topE && leftE <= rightE) {
            return leftE;
        } else if (rightE <= leftE && rightE <= topE) {
            return rightE;
        }
        return topE;
    }

    private void transpose() {
        Picture temp = new Picture(height, width);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                temp.set(row, col, pic.get(col, row));
            }
        }
        pic = temp;
        int t = width;
        width = height;
        height = t;
    }

    public int[] findHorizontalSeam() {
        transpose();
        int[] horizontalSeam = findVerticalSeam();
        transpose();

        return horizontalSeam;
    }

    public int[] findVerticalSeam() {
        int[] verticalSeam = new int[height];
        double[][] M = findM();

        int startX = 0;
        double minM = Double.MAX_VALUE;
        for (int col = 0; col < width; col++) {
            double e = M[col][height - 1];
            if (e < minM) {
                minM = e;
                startX = col;
            }
        }
        verticalSeam[height - 1] = startX;

        int xPos = startX;
        for (int row = height - 1; row > 0; row--) {
            int yPos = row - 1;
            int leftX = xPos - 1;
            int topX = xPos;
            int rightX = xPos + 1;
            double leftM = 0.0, topM = 0.0, rightM = 0.0;

            if (leftX < 0) {
                leftM = Double.MAX_VALUE;
            } else {
                leftM = M[leftX][yPos];
            }
            if (rightX >= width) {
                rightM = Double.MAX_VALUE;
            } else {
                rightM = M[rightX][yPos];
            }
            topM = M[topX][yPos];

            minM = minEnergy(leftM, topM, rightM);
            if (minM == leftM) {
                verticalSeam[row - 1] = leftX;
                xPos = leftX;
            } else if (minM == rightM) {
                verticalSeam[row - 1] = rightX;
                xPos = rightX;
            } else {
                verticalSeam[row - 1] = topX;
                xPos = topX;
            }
        }
        return verticalSeam;
    }

    private double[][] findM() {
        double[][] e = new double[width][height];
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                e[col][row] = energy(col, row);
            }
        }

        double[][] M = new double[width][height];
        for (int col = 0; col < width; col++) {
            M[col][0] = e[col][0];
        }

        if (width == 1) {
            return M;
        } else {
            for (int row = 1; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    int topX = col;
                    int leftX = col - 1;
                    int rightX = col + 1;
                    int yPos = row - 1;
                    double leftM = 0.0, topM = 0.0, rightM = 0.0;

                    if (leftX < 0) {
                        leftM = Double.MAX_VALUE;
                    } else {
                        leftM = M[leftX][yPos];
                    }
                    if (rightX >= width) {
                        rightM = Double.MAX_VALUE;
                    } else {
                        rightM = M[rightX][yPos];
                    }
                    topM = M[topX][yPos];

                    double minM = minEnergy(leftM, topM, rightM);
                    M[col][row] = energy(col, row) + minM;
                }
            }
        }

        return M;
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
