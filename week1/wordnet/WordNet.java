import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.SeparateChainingHashST;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;

public class WordNet {

    private final SAP wordnetSAP;
    private final SeparateChainingHashST<String, Queue<Integer>> nounToIds;
    private final SeparateChainingHashST<Integer, String> idsToNouns;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms)
    {
        nounToIds = new SeparateChainingHashST<>();
        idsToNouns = new SeparateChainingHashST<>();
        addSynsets(new In(synsets));

        wordnetSAP = new SAP(createDAG(new In(hypernyms)));
    }

    private Digraph createDAG(In hypernyms)
    {
        Digraph wordnetDAG = new Digraph(idsToNouns.size());
        for(String line = hypernyms.readLine(); line != null; line = hypernyms.readLine())
        {
            String[] ids = line.split(",");
            int hyponym = Integer.parseInt(ids[0]);
            for(int i = 1; i < ids.length; i++)
            {
                wordnetDAG.addEdge(hyponym, Integer.parseInt(ids[i]));
            }
        }
        DirectedCycle finder = new DirectedCycle(wordnetDAG);
        if(finder.hasCycle()) //TODO find if wordnet is rooted
        {
            throw new java.lang.IllegalArgumentException("WordNet is not a rooted DAG.");
        }
        return wordnetDAG;
    }

    private void addSynsets(In synsets)
    {
        for(String line = synsets.readLine(); line != null; line = synsets.readLine())
        {
            String[] synset = line.split(",");
            int id = Integer.parseInt(synset[0]);
            idsToNouns.put(id, synset[1]);

            String[] nouns = synset[1].split(" ");
            for (String noun : nouns) {
                Queue<Integer> nounIds;
                if(nounToIds.contains(noun))
                {
                    nounIds = nounToIds.get(noun);
                }
                else
                {
                    nounIds = new Queue<>();
                    nounToIds.put(noun, nounIds);
                }
                nounIds.enqueue(id);
            }
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns()
    {
        return nounToIds.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word)
    {
        return nounToIds.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB)
    {
        return wordnetSAP.length(nounToIds.get(nounA), nounToIds.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB)
    {
        int id = wordnetSAP.ancestor(nounToIds.get(nounA), nounToIds.get(nounB));
        return idsToNouns.get(id);
    }

    // do unit testing of this class
    public static void main(String[] args)
    {
        String synsets = args[0];
        String hypernyms = args[1];
        WordNet wordNet = new WordNet(synsets, hypernyms);
        while (!StdIn.isEmpty()) {
            String nounA = StdIn.readString();
            String nounB = StdIn.readString();

            int dist = wordNet.distance(nounA, nounB);
            String ancestor = wordNet.sap(nounA, nounB);
            StdOut.printf("distance = %d, ancestor synset = %s\n", dist, ancestor);
        }
    }
}