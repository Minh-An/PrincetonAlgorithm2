import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.Queue;

public class SAP {

    private Digraph digraph;

    private int[] distToV;
    private int[] distToW;
    
    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G)
    {
	digraph = G;
	distToV = new int[digraph.V()];
	distToW = new int[digraph.V()];
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w)
    {
	reset();
	Queue qv = new Queue<>();
	qv.enqueue(v);
	BFS(qv, true);

	Queue qw = new Queue<>();
	qw.enqueue(w);

	int ancestor = BFS(qw, false);
	if(ancestor != -1)
	{
	    return distToV[ancestor] + distToW[ancestor];
	}
	return -1;
    }

    private void BFS(Iterable<Integer> vertices, int[] )
    {
	Queue<Integer> queue = new Queue<>();
	for(int vertex: vertices)
	{
	    queue.enqueue(vertex);
	    distTo[vertex] = 0;
	}
	
	while(!queue.isEmpty())
	{
	    int v = queue.dequeue();
	    if(other[v] != -1)
	    {
		if((other[v] + distTo[v]) < length)
		{
		    ancestor = v;
		    length = other[v] + distTo[v];
		}
	    }
	    for (int w: digraph.adj(v))
	    {
		if(distTo[w] == -1)
		{
		    queue.enqueue(w);
		    distTo[w] = distTo[v] + 1;
		}
	    }
	}
    }

    private int 

    private void reset()
    {
	for(int i = 0; i < digraph.V(); i++)
	{
	    distToV[i] = -1;
	    distToW[i] = -1;
	}
    }
    
    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w)
    {
	reset();
	return -1;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w)
    {
	reset();
	return -1;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w)
    {
	reset();
	return -1;
    }

    // do unit testing of this class
    public static void main(String[] args)
    {
	In in = new In(args[0]);
	Digraph G = new Digraph(in);
	SAP sap = new SAP(G);
	while (!StdIn.isEmpty()) {
	    int v = StdIn.readInt();
	    int w = StdIn.readInt();
	    int length   = sap.length(v, w);
	    int ancestor = sap.ancestor(v, w);
	    StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
	}
	
    }

}
