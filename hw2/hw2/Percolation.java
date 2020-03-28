package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;
    private WeightedQuickUnionUF uf;

    private int sitesCount;
    private int N;
    private int vTop;     /** Virtual top sites that connects all the top sites. */
    private int vBottom;  /** Virtual bottom sites that connects all the bottom sites. */

    private final static int[][] DIRECTION = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
                                            /** top     down    left     right */
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("N should be larger than zero!");
        }

        sitesCount = 0;
        this.N = N;
        vTop = N * N;
        vBottom = N * N + 1;
        grid = new boolean[N][N];
        uf = new WeightedQuickUnionUF(N * N + 2);
        setup();
    }

    /** Optimization:
     *  Connect vTop to all the top row's elements.
     *
     *  Why don't we connect vBottom to all the bottom row's elements as well?
     *  Ans: To prevent backwash!!!
     */
    private void setup() {
        for (int j = 0; j < N; j++) {
            uf.union(coorTo1D(0, j), vTop);
            //uf.union(coorTo1D(N - 1, j), vBottom);
        }
    }

    private int coorTo1D(int row, int col) {
        return row * N + col;
    }

    /** Prevent corner cases:
     *  top left, top right, bottom left, bottom right and all boundary sites.
     */
    private boolean validIndex(int row, int col) {
        if (row < 0 || row > N - 1) {
            return false;
        }
        if (col < 0 || col > N - 1) {
            return false;
        }
        return true;
    }

    private void checkIndex(int row, int col) {
        if (!validIndex(row, col)) {
            throw new IndexOutOfBoundsException();
        }
    }

    public void open(int row, int col) {
        checkIndex(row, col);
        if (isOpen(row, col)) {
            return;
        }
        grid[row][col] = true;
        sitesCount += 1;

        /** If the adjacent site is opened, connect them. */
        for (int[] dir : DIRECTION) {
            int r = row + dir[0];
            int c = col + dir[1];
            if (validIndex(r, c) && isOpen(r, c)) {
                int index1 = coorTo1D(r, c);
                int index2 = coorTo1D(row, col);
                uf.union(index1, index2);
                if (row == N - 1) {
                    uf.union(index2, vBottom);
                }
            }
        }
    }

    public boolean isOpen(int row, int col) {
        checkIndex(row, col);
        return grid[row][col];
    }

    public boolean isFull(int row, int col) {
        checkIndex(row, col);
        if (N == 1) {
            return isOpen(0, 0);
        } else {
            return uf.connected(coorTo1D(row, col), vTop) && isOpen(row, col);
        }
    }

    public int numberOfOpenSites() {
        return sitesCount;
    }

    public boolean percolates() {
        if (N == 1) {
            return isOpen(0, 0);
        } else {
            return uf.connected(vTop, vBottom);
        }
    }

    public static void main(String[] args) {
        Percolation p = new Percolation(2);

        p.print();

        testOpen(p, 0, 0);
        testOpen(p, 1, 0);
    }

    private void print() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (isFull(i, j)) {
                    System.out.print("Full\t");
                } else {
                    System.out.print(grid[i][j] + "\t");
                }
            }
            System.out.println();
        }
    }

    private static void testOpen(Percolation p, int row, int col) {
        p.open(row, col);
        System.out.println("Opened: " + "(" + row + ", " + col + ")");
        System.out.println("#site opened: " + p.numberOfOpenSites());
        p.print();
        System.out.println("Percolated: " + p.percolates());
        System.out.println();
    }
}
