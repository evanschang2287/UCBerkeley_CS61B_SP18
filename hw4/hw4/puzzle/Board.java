package hw4.puzzle;

import edu.princeton.cs.algs4.Queue;

public class Board implements WorldState {
    private static final int BLANK = 0;
    private int N;
    private int[][] tiles;

    /**
     * Constructs a board from an N-by-N array of tiles where
     * tiles[i][j] = tile at row i, column j.
     */
    public Board(int[][] tiles) {
        this.N = tiles.length;
        this.tiles = new int[N][N];
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                this.tiles[r][c] = tiles[r][c];
            }
        }
    }

    /**
     * Returns value of tile at row i, column j (or 0 if blank).
     */
    public int tileAt(int i, int j) {
        if (i < 0 || i > N - 1) {
            throw new IndexOutOfBoundsException("i should be between 0 and N-1");
        }
        if (j < 0 || j > N - 1) {
            throw new IndexOutOfBoundsException("j should be between 0 and N-1");
        }

        return tiles[i][j];
    }

    /**
     * Returns the board size N.
     */
    public int size() {
        return N;
    }

    /**
     * Returns the neighbors of the current board.
     * @author Josh Hug (Cited from http://joshh.ug/neighbors.html)
     */
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int hug = size();
        int bug = -1;
        int zug = -1;
        for (int rug = 0; rug < hug; rug++) {
            for (int tug = 0; tug < hug; tug++) {
                if (tileAt(rug, tug) == BLANK) {
                    bug = rug;
                    zug = tug;
                }
            }
        }
        int[][] ili1li1 = new int[hug][hug];
        for (int pug = 0; pug < hug; pug++) {
            for (int yug = 0; yug < hug; yug++) {
                ili1li1[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int l11il = 0; l11il < hug; l11il++) {
            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
                    ili1li1[l11il][lil1il1] = BLANK;
                    Board neighbor = new Board(ili1li1);
                    neighbors.enqueue(neighbor);
                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
                    ili1li1[bug][zug] = BLANK;
                }
            }
        }
        return neighbors;
    }

    /**
     * Returns hamming estimated distance.
     */
    public int hamming() {
        int expectedDistance = 0;
        int expectedValue = 1;

        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                if (expectedValue == N * N) {
                    break;
                }
                if (tileAt(row, col) != expectedValue) {
                    expectedDistance += 1;
                }
                expectedValue += 1;
            }
        }
        return expectedDistance;
    }

    /**
     * Returns manhattan estimated distance.
     */
    public int manhattan() {
        int expectedDistance = 0;

        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                int actualValue = tileAt(row, col);
                if (actualValue == 0) {
                    continue;
                }
                int rowNum = (tileAt(row, col) - 1) / N;
                int colNum = (tileAt(row, col) - 1) % N;
                expectedDistance += Math.abs(row - rowNum);
                expectedDistance += Math.abs(col - colNum);
            }
        }
        return expectedDistance;
    }

    /**
     * Estimated distance to goal. Simply return manhattan().
     */
    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Return true if this board's tile values are the same position as y's.
     */
    @Override
    public boolean equals(Object j) {
        if (this == j) {
            return true;
        }
        if (j == null || this.getClass() != j.getClass()) {
            return false;
        }

        Board board = (Board) j;
        if (N != board.N) {
            return false;
        }
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                if (tileAt(row, col) != board.tileAt(row, col)) {
                    return false;
                }
            }
        }
        return true;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        int n = size();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
