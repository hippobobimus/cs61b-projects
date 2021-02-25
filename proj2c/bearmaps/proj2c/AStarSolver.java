package bearmaps.proj2c;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import edu.princeton.cs.algs4.Stopwatch;
import bearmaps.proj2ab.ArrayHeapMinPQ;

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {
    private HashMap<Vertex, Double> distTo;
    private HashMap<Vertex, WeightedEdge<Vertex>> edgeTo;
    private ArrayHeapMinPQ<Vertex> fringe;
    private AStarGraph<Vertex> graph;
    private Vertex origin;
    private Vertex goal;
    private int numStatesExplored;
    private SolverOutcome outcome;
    private List<Vertex> solution;
    private double solutionWeight;
    private double timeSpent;

    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        Stopwatch sw = new Stopwatch();

        // initialise instance variables
        distTo = new HashMap<Vertex, Double>();
        edgeTo= new HashMap<Vertex, WeightedEdge<Vertex>>();
        fringe = new ArrayHeapMinPQ<Vertex>();
        graph = input;
        origin = start;
        goal = end;
        numStatesExplored = 0;
        solution = new ArrayList<Vertex>();
        solutionWeight = 0;

        // add origin
        distTo.put(origin, 0.0);
        fringe.add(origin, graph.estimatedDistanceToGoal(origin, goal));

        while (fringe.size() > 0) {
            if (sw.elapsedTime() > timeout) {
                outcome = SolverOutcome.TIMEOUT;
                timeSpent = sw.elapsedTime();
                return;
            }

            Vertex p = fringe.removeSmallest();

            if (p.equals(goal)) {
                generateSolution();
                outcome = SolverOutcome.SOLVED;
                timeSpent = sw.elapsedTime();
                return;
            }

            List<WeightedEdge<Vertex>> edges = graph.neighbors(p);

            for (WeightedEdge<Vertex> e : edges) {
                relax(e);
            }

            numStatesExplored++;
        }

        timeSpent = sw.elapsedTime();
        outcome = SolverOutcome.UNSOLVABLE;
    }

    public SolverOutcome outcome() {
        return outcome;
    }

    public List<Vertex> solution() {
        return solution;
    }

    public double solutionWeight() {
        return solutionWeight;
    }

    public int numStatesExplored() {
        return numStatesExplored;
    }

    public double explorationTime() {
        return timeSpent;
    }

    /* HELPER METHODS */

    private void generateSolution(Vertex p) {
        if (p.equals(origin)) {
            return;
        }

        WeightedEdge<Vertex> e = edgeTo.get(p);
        Vertex prev = e.from();

        generateSolution(prev);

        solution.add(prev);
        solutionWeight += e.weight();
    }

    private void generateSolution() {
        generateSolution(goal);
        solution.add(goal);
    }

    private void relax(WeightedEdge<Vertex> e) {
        Vertex p = e.from();
        Vertex q = e.to();
        double w = e.weight();

        double newDist = distTo.get(p) + w;
        Double currDist = distTo.get(q);

        if (currDist == null || newDist < currDist) {
            distTo.put(q, newDist);
            edgeTo.put(q, e);

            double newPriority = newDist + graph.estimatedDistanceToGoal(q, goal);

            if (fringe.contains(q)) {
                fringe.changePriority(q, newPriority);
            } else {
                fringe.add(q, newPriority);
            }
        }
    }
}
