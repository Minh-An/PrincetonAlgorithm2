import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SeparateChainingHashST;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {

    private int numTeams;
    private SeparateChainingHashST<String, Integer> teams;
    private int[] wins;
    private int[] losses;
    private int[] remaining;
    private int[][] games;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename)
    {
        In in = new In(filename);
        if(in.hasNextChar())
        {
            numTeams = in.readInt();

            wins = new int[numTeams];
            losses = new int[numTeams];
            remaining = new int[numTeams];
            games = new int[numTeams][numTeams];

            for (int i = 0; i < numTeams; i++) {
                teams.put(in.readString(), i);
                wins[i] = in.readInt();
                losses[i] = in.readInt();
                remaining[i] = in.readInt();

                for(int j = 0; j < numTeams; j++)
                {
                    games[i][j] = in.readInt();
                }
            }
        }
    }

    // number of teams
    public int numberOfTeams()
    {
        return numTeams;
    }

    // all teams
    public Iterable<String> teams()
    {
        return teams.keys();
    }

    // number of wins for given team
    public int wins(String team)
    {
        return wins[teams.get(team)];
    }

    // number of losses for given team
    public int losses(String team)
    {
        return losses[teams.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team)
    {
        return remaining[teams.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1,String team2)
    {
        return games[teams.get(team1)][teams.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team)
    {

    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team)
    {

    }

    public static void main(String[]args){
//        BaseballElimination division=new BaseballElimination(args[0]);
//        for(String team:division.teams())
//        {
//            if(division.isEliminated(team))
//            {
//                StdOut.print(team+" is eliminated by the subset R = { ");
//                for(String t:division.certificateOfElimination(team))
//                {
//                    StdOut.print(t+" ");
//                }
//                StdOut.println("}");
//            }
//            else
//            {
//                StdOut.println(team+" is not eliminated");
//            }
//        }
        BaseballElimination division = new BaseballElimination(args[0]);
        for(String team: division.teams())
        {
            StdOut.print(team + ": Wins: " + division.wins(team) + " Loses: " +
                    division.losses(team) + " Remaining: " + division.remaining(team) );
            StdOut.print("Games Left: ");
            for(String opponent: division.teams())
            {
                StdOut.print("");
            }
            StdOut.println();
        }
    }

}
