import edu.princeton.cs.algs4.*;

public class BaseballElimination {

    private int numTeams;
    private SeparateChainingHashST<String, Integer> teams;
    private int[] wins;
    private int[] losses;
    private int[] remaining;
    private int[][] games;

    private Queue[] certificates;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        if (in.hasNextChar()) {
            numTeams = in.readInt();

            wins = new int[numTeams];
            losses = new int[numTeams];
            remaining = new int[numTeams];
            games = new int[numTeams][numTeams];
            teams = new SeparateChainingHashST<>();
            certificates = new Queue[numTeams];

            int max = 0;
            for (int i = 0; i < numTeams; i++) {
                teams.put(in.readString(), i);
                wins[i] = in.readInt();
                losses[i] = in.readInt();
                remaining[i] = in.readInt();

                for (int j = 0; j < numTeams; j++) {
                    games[i][j] = in.readInt();
                }
            }
        }
    }

    // number of teams
    public int numberOfTeams() {
        return numTeams;
    }

    // all teams
    public Iterable<String> teams() {
        return teams.keys();
    }

    // number of wins for given team
    public int wins(String team) {
        if (!teams.contains(team)) {
            throw new IllegalArgumentException("Illegal team name.");
        }
        return wins[teams.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        if (!teams.contains(team)) {
            throw new IllegalArgumentException("Illegal team name.");
        }
        return losses[teams.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (!teams.contains(team)) {
            throw new IllegalArgumentException("Illegal team name.");
        }
        return remaining[teams.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (!teams.contains(team1) || !teams.contains(team2)) {
            throw new IllegalArgumentException("Illegal team name.");
        }
        return games[teams.get(team1)][teams.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (!teams.contains(team)) {
            throw new IllegalArgumentException("Illegal team name.");
        }

        int x = teams.get(team);
        int possibleWins = wins[x] + remaining[x];
        Queue<String> certificate = new Queue<>();
        String trivial = trivialCase(wins[x] + remaining[x]);
        if (trivial != null) {
            certificate.enqueue(trivial);
            certificates[x] = certificate;
            return true;
        }

        //2 vertices for source/sink, one for every team, and each game between the teams
        int vertices = 2 + numTeams + ((numTeams - 1) * (numTeams - 2) / 2);
        FlowNetwork network = new FlowNetwork(vertices);

        //number of vertices before the team vertices
        int offset = ((numTeams - 1) * (numTeams - 2) / 2) + 1;

        //to check for elimination
        int totalCapacity = 0;

        int n = 1;
        for (int i = 0; i < numTeams; i++) {
            if (i != x) {
                for (int j = i + 1; j < numTeams; j++) {
                    if (j != x) {
                        network.addEdge(new FlowEdge(0, n, games[i][j]));
                        totalCapacity += games[i][j];
                        network.addEdge(new FlowEdge(n, offset + i, Double.POSITIVE_INFINITY));
                        network.addEdge(new FlowEdge(n, offset + j, Double.POSITIVE_INFINITY));
                        n++;
                    }
                }
                network.addEdge(new FlowEdge(offset + i, vertices - 1, possibleWins - wins[i]));
            }
        }

        FordFulkerson elimination = new FordFulkerson(network, 0, vertices - 1);

        if (elimination.value() < totalCapacity) {
            for (String t : teams.keys()) {
                if (elimination.inCut(offset + teams.get(t))) {
                    certificate.enqueue(t);
                    certificates[x] = certificate;
                }
            }
            return true;
        }
        return false;
    }

    private String trivialCase(int possibleWins) {
        for (String team : teams.keys()) {
            if (possibleWins < wins[teams.get(team)]) {
                return team;
            }
        }
        return null;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (!teams.contains(team)) {
            throw new IllegalArgumentException("Illegal team name.");
        }

        int x = teams.get(team);
        if (certificates[x] == null) {
            isEliminated(team);
        }
        return certificates[x];
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }

    }
}