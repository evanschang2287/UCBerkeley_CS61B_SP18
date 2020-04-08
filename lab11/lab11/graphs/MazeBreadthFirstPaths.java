package lab11.graphs;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.ArrayDeque;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private int t;
    private Maze maze;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m); /* Use the super class's constructor (ie. MazeExplorer(m)). */
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
        // TODO: Your code here. Don't forget to update distTo, edgeTo, and marked, as well as call announce()
        ArrayDeque<Integer> fringe = new ArrayDeque<>();
        fringe.add(s);
        marked[s] = true;

        while (!fringe.isEmpty()) {
            int v = fringe.poll();
            if (v == t) {
                return;
            }
            for (int w : maze.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1; /* Assuming that each edge's weight is 1. */
                    marked[w] = true;
                    announce();
                    fringe.add(w);
                }
            }
        }
    }

    @Override
    public void solve() {
        bfs();
    }
}

