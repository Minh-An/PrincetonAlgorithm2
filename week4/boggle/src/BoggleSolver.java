import edu.princeton.cs.algs4.*;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;

public class BoggleSolver {
    private TST<Boolean> dict;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        dict = new TST<>();
        for (int i = 0; i < dictionary.length; i++) {
            dict.put(dictionary[i], true);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        SeparateChainingHashST<Integer, Queue<Integer>> adj = adjacentBlocks(board);
        return dfs(board, adj);
    }

    private Queue<String> dfs(BoggleBoard board, SeparateChainingHashST<Integer, Queue<Integer>> adj) {
        Queue<String> validWords = new Queue<>();
        int tiles = board.rows() * board.cols();
        for (int v = 0; v < tiles; v++) {
            boolean[] marked = new boolean[tiles];
            StringBuilder prefix = new StringBuilder();
            dfs(board, v, prefix, marked, adj, validWords);
        }
        return validWords;
    }

    private void dfs(BoggleBoard board, int v, StringBuilder prefix, boolean[] marked,
                     SeparateChainingHashST<Integer, Queue<Integer>> adj, Queue<String> validWords) {
        marked[v] = true;
        int row = v / board.cols();
        int col = v % board.cols();
        prefix.append(board.getLetter(row, col));
        StdOut.println(prefix.toString());
        if(dict.get(prefix.toString()) != null)
        {

            validWords.enqueue(prefix.toString());
        }

        for(String s : dict.keysWithPrefix(prefix.toString())) {
            for (int w : adj.get(v)) {
                if (!marked[w]) {
                    dfs(board, w, prefix, marked, adj, validWords);
                    marked[v] = false;
                }
            }
            break;
        }
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        int n = word.length();
        if (dict.get(word) && n > 2) {
            if (n >= 8) {
                return 11;
            }
            if (n == 7) {
                return 5;
            }
            if (n == 3) {
                return 1;
            } else {
                return n - 3;
            }
        }
        return 0;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }


    private SeparateChainingHashST<Integer, Queue<Integer>> adjacentBlocks(BoggleBoard board) {
        SeparateChainingHashST<Integer, Queue<Integer>> adj = new SeparateChainingHashST<>();
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                Queue<Integer> adjToIJ = new Queue<>();
                if (i != 0) {
                    adjToIJ.enqueue((i - 1) * board.cols() + j);
                    if (j != 0) {
                        adjToIJ.enqueue((i - 1) * board.cols() + j - 1);
                    }
                    if (j != board.cols()-1) {
                        adjToIJ.enqueue((i - 1) * board.cols() + j + 1);
                    }
                }
                if (i != board.rows() - 1) {
                    adjToIJ.enqueue((i + 1) * board.cols() + j);
                    if (j != 0) {
                        adjToIJ.enqueue((i + 1) * board.cols() + j - 1);
                    }
                    if (j != board.cols()-1) {
                        adjToIJ.enqueue((i + 1) * board.cols() + j + 1);
                    }
                }
                if (j != 0) {
                    adjToIJ.enqueue((i) * board.cols() + j - 1);
                }
                if (j != board.cols()-1) {
                    adjToIJ.enqueue((i) * board.cols() + j + 1);
                }
                adj.put(i * board.cols() + j, adjToIJ);
            }
        }
        return adj;
    }
}