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
        if (leftE < topE && leftE < rightE) {
            return leftE;
        } else if (topE < leftE && topE < rightE) {
            return topE;
        }
        return rightE;
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

        double minTotalEnergy = Double.MAX_VALUE;
        for (int col = 0; col < width; col++) {
            int xPos = col;
            int[] tempSeam = new int[height];
            double tempTotalEnergy = 0.0;

            for (int y = 0; y < height; y++) {
                if (y == 0) {
                    tempTotalEnergy += energy(xPos, y);
                    tempSeam[y] = xPos;
                } else {
                    double leftE = 0.0, topE = 0.0, rightE = 0.0;

                    /* Calculate leftX's energy & topX's energy & rightX's energy. */
                    if (xPos - 1 < 0) {
                        leftE = Double.MAX_VALUE;
                    } else {
                        leftE = energy(xPos - 1, y);
                    }
                    if (xPos + 1 >= width) {
                        rightE = Double.MAX_VALUE;
                    } else {
                        rightE = energy(xPos + 1, y);
                    }
                    topE = energy(xPos, y);

                    double minE = minEnergy(leftE, topE, rightE);
                    tempTotalEnergy += minE;
                    /* Determine next position of x. */
                    if (minE == leftE) {
                        tempSeam[y] = xPos - 1;
                        xPos -= 1;
                    } else if (minE == rightE) {
                        tempSeam[y] = xPos + 1;
                        xPos += 1;
                    } else {
                        tempSeam[y] = xPos;
                    }
                }
            }

            if (tempTotalEnergy < minTotalEnergy) {
                minTotalEnergy = tempTotalEnergy;
                verticalSeam = tempSeam;
            }
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
