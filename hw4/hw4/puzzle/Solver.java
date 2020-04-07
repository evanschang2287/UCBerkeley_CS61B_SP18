package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Solver {
    private class SearchNode {
        private WorldState state;
        private int numOfMovs;
        private Integer priority;     /* Determine the order in MinPQ. */
        private SearchNode prev;

        public SearchNode(WorldState state, SearchNode prev) {
            this.state = state;
            numOfMovs = prev == null ? 0 : prev.numOfMovs + 1;
            if (edCaches.containsKey(this.state)) {
                int ed = edCaches.get(this.state);
                priority = numOfMovs + ed;
            } else {
                int ed = this.state.estimatedDistanceToGoal();
                edCaches.put(this.state, ed);
                priority = numOfMovs + ed;
            }
            this.prev = prev;
        }
    }

    private class SearchNodeComparator implements Comparator<SearchNode> {
        @Override
        public int compare(SearchNode node1, SearchNode node2) {
            return node1.priority.compareTo(node2.priority);
        }
    }

    private Map<WorldState, Integer> edCaches = new HashMap<>();
    private Stack<WorldState> path = new Stack<>();
    private int searchCount = 0;

    public Solver(WorldState initial) {
        MinPQ<SearchNode> pq = new MinPQ<>(new SearchNodeComparator());
        SearchNode currentNode = new SearchNode(initial, null);
        //pq.insert(currentNode);

        while (!currentNode.state.isGoal()) {
            for (WorldState nextState : currentNode.state.neighbors()) {
                if (currentNode.prev == null || !nextState.equals(currentNode.prev.state)) {
                    SearchNode nextNode = new SearchNode(nextState, currentNode);
                    pq.insert(nextNode);
                    searchCount += 1;
                }
            }
            currentNode = pq.delMin();
        }

        for (SearchNode node = currentNode; node != null; node = node.prev) {
            path.push(node.state);
        }
    }

    public int moves() {
        return path.size() - 1;
    }

    public Iterable<WorldState> solution() {
        return path;
    }

    private int searchCount() {
        return searchCount;
    }
}
