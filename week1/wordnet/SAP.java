import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.Queue;

public class SAP {

    private Digraph digraph;

    private int[] distFromV;
    private int[] distFromW;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        digraph = new Digraph(G);
        distFromV = new int[digraph.V()];
        distFromW = new int[digraph.V()];
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        reset();
        Queue<Integer> qv = new Queue<>();
        qv.enqueue(v);
        bfs(qv, distFromV);

        Queue<Integer> qw = new Queue<>();
        qw.enqueue(w);

        bfs(qw, distFromW);

        int ancestor = getAncestor();
        if (ancestor != -1) {
            return distFromV[ancestor] + distFromW[ancestor];
        }
        return -1;
    }


    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        reset();

        Queue<Integer> qv = new Queue<>();
        qv.enqueue(v);
        bfs(qv, distFromV);

        Queue<Integer> qw = new Queue<>();
        qw.enqueue(w);

        bfs(qw, distFromW);

        return getAncestor();
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if(v == null)
        {
            throw new java.lang.IllegalArgumentException("First Iterable argument is null.");
        }
        if(w == null)
        {
            throw new java.lang.IllegalArgumentException("Second Iterable argument is null.");
        }
        reset();
        bfs(v, distFromV);
        bfs(w, distFromW);
        int ancestor = getAncestor();
        if (ancestor != -1) {
            return distFromV[ancestor] + distFromW[ancestor];
        }
        return -1;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if(v == null)
        {
            throw new java.lang.IllegalArgumentException("First Iterable argument is null.");
        }
        if(w == null)
        {
            throw new java.lang.IllegalArgumentException("Second Iterable argument is null.");
        }
        reset();

        bfs(v, distFromV);
        bfs(w, distFromW);
        return getAncestor();
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v1 = StdIn.readInt();
            int v2 = StdIn.readInt();
            int w1 = StdIn.readInt();
            int w2 = StdIn.readInt();

            Queue<Integer> qv = new Queue<>();
            qv.enqueue(v1);
            qv.enqueue(v2);

            Queue<Integer> qw = new Queue<>();
            qw.enqueue(w1);
            qw.enqueue(w2);

            int length = sap.length(qv, qw);
            int ancestor = sap.ancestor(qv, qw);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }

    private void bfs(Iterable<Integer> vertices, int[] distTo) {
        Queue<Integer> queue = new Queue<>();
        for (int vertex : vertices) {
            if (vertex < 0 || vertex >= digraph.V()) {
                throw new java.lang.IllegalArgumentException("Vertex" + vertex + " is not in range.");
            }
            queue.enqueue(vertex);
            distTo[vertex] = 0;
        }

        while (!queue.isEmpty()) {
            int v = queue.dequeue();
            for (int w : digraph.adj(v)) {
                if (distTo[w] == -1) {
                    queue.enqueue(w);
                    distTo[w] = distTo[v] + 1;
                }
            }
        }
    }

    private int getAncestor() {
        int ancestor = -1;
        int length = Integer.MAX_VALUE;
        for (int i = 0; i < distFromV.length; i++) {
            if (distFromV[i] != -1 && distFromW[i] != -1) {
                if ((distFromW[i] + distFromV[i]) < length) {
                    ancestor = i;
                    length = distFromW[i] + distFromV[i];
                }
            }
        }
        return ancestor;
    }

    private void reset() {
        for (int i = 0; i < digraph.V(); i++) {
            distFromV[i] = -1;
            distFromW[i] = -1;
        }
    }

}
