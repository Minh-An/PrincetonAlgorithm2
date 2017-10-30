import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

// Class to determine the topological order of a DAG (directed acyclic graph)
public class Topological {

  private boolean[] marked;
  private int[] rank;
  private int count;
  private Stack<Integer> reversePost;
  private DirectedCycle cycle;

  // constructor for the Topological class
  public Topological(Digraph G) {
    cycle = new DirectedCycle(G);
    if (hasOrder()) {
      marked = new boolean[G.V()];
      rank = new int[G.V()];
      count = 0;
      reversePost = new Stack<Integer>();

      for (int v = 0; v < G.V(); v++) {
        if (!marked[v]) {
          dfs(G, v);
        }
      }
    }
  }

  // recursive DFS
  private void dfs(Digraph G, int v) {
    marked[v] = true;
    for (int w : G.adj(v)) {
      if (!marked[w]) {
        dfs(G, w);
      }
    }
    reversePost.push(v);
    rank[v] = count++;
  }

  // does the Digraph have a topological order? (is it a DAG?)
  public boolean hasOrder() { return !(cycle.hasCycle()); }

  // returns the topological order
  public Iterable<Integer> order() { return reversePost; }

  // returns the rank of the vertex, rank 0 being most dependent and rank 12
  // being least dependent
  public int rank(int v) {
    if (hasOrder()) {
      return rank[v];
    }
    return -1;
  }

  // main test function
  public static void main(String[] args) {
    In in = new In(args[0]);
    Digraph test = new Digraph(in);
    Topological top = new Topological(test);

    if (top.hasOrder()) {
      StdOut.printf("Topological Order for %s\n", args[0]);
      for (int v : top.order()) {
        StdOut.printf("%d rank: %d\n", v, top.rank(v));
      }
    }
  }
}
