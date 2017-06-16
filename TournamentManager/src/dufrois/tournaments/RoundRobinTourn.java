package dufrois.tournaments;

import java.util.ArrayList;
import java.util.Random;

import dufrois.individuals.Match;
import dufrois.individuals.Team;

/**
 * A tournament in a round robin format
 * 
 * @author Christian Dufrois
 * @version 2017.04.17
 *
 * @param <T> Generic type that is a team
 */
public class RoundRobinTourn<T extends Team> implements TournamentInterface<T> {
    
    private ArrayList<T> teams;
    private String name;
    private int numTeams;
    private int runThroughs;
    private boolean started;
    private ArrayList<Match<T>[]> games;
    
    /**
     * Constructor for the round robin tournament format
     * 
     * @param name Name identifier for the tournament
     * @param rnThrghs Number
     */
    public RoundRobinTourn(String name, int rnThrghs) {
        started = false;
        teams = new ArrayList<T>();
        numTeams = 0;
        runThroughs = rnThrghs;
        this.name = name;
    }
    
    /**
     * Number of teams in the tournament
     * 
     * @return Number of teams
     */
    public int getNumTeams() {
        return numTeams;
    }
    
    /**
     * The team at the given index
     * 
     * @param index The index of the team to return
     * @return The team at the given index
     */
    @Override
    public T getTeam(int index) {
        return teams.get(index);
    }
    
    /**
     * An array of all the teams
     * 
     * @return An array of all the teams
     */
    @SuppressWarnings("unchecked")
    @Override
    public T[] getTeams() {
        return (T[]) teams.toArray();
    }
    
    /**
     * The name identifier of the tournament
     * 
     * @return The name of the tournament
     */
    public String getName() {
        return name;
    }
    
    /**
     * Add a team to the tournament
     * 
     * @param team The team to be added
     * @throws TournamentStartedException If the tournament has already started
     */
    @Override
    public void addTeam(T team) throws TournamentStartedException {
        if (started) {
            throw new TournamentStartedException();
        }
        teams.add(team);
        numTeams++;
    }
    
    /**
     * Remove the team from the tournament
     * 
     * @param team Team to be removed
     * @return If the team was found and removed
     * @throws TournamentStartedException If the tournament has already started
     */
    public boolean removeTeam(T team) throws TournamentStartedException {
        if (started) {
            throw new TournamentStartedException();
        }
        if (teams.remove(team)) {
            numTeams--;
            return true;
        }
        return false;
    }
    
    /**
     * Create the matches for the game
     * 
     * @throws TournamentStartedException
     */
    @SuppressWarnings("unchecked")
    @Override
    public void startTournament() throws TournamentStartedException {
        // Only start game once
        if (started) {
            throw new TournamentStartedException();
        }
        started = true;
        
        // Assure even number of teams
        if (teams.size() % 2 == 1) {
            teams.add((T) new Team("Bye Match"));
            numTeams++;
        }
        
        randomizeTeams();
        
        int wpr = numTeams - 1; // Weeks per round
        int mpw = numTeams / 2; // Matches per week
        games = new ArrayList<Match<T>[]>(runThroughs * wpr);
        
        // Create all the matches
        for (int r = 0; r < runThroughs; r++) { // Each round
            for (int w = 0; w < wpr; w++) { // Each week
                
                Match<T>[] week = new Match[mpw]; // Array of matches
                for (int m = 0; m < mpw; m++) { // Each match
                    
                    int matchNum = (r * wpr * mpw) + (w * mpw) + (m + 1); // Correct number based on which round, 
                    week[m] = new Match<T>(teams.get(m), teams.get(numTeams - m - 1), matchNum);
                }
                
                games.add(r * (numTeams - 1) + w, week);
                rotateTeams();
            }
            
        }
        
    }
    
    /**
     * Randomize the order of the teams
     */
    private void randomizeTeams() {
        Random rand = new Random();
        for (int i = 0; i < getNumTeams() - 1; i++) { // Go through list of teams
            
            int num = rand.nextInt(numTeams - i); // Random number between 0 and last index
            // System.out.println("" + num + " | " + (numTeams - i));
            if (i != num) {
                T temp = teams.get(num);
                teams.set(num, teams.get(i));
                teams.set(i, temp);
            }
        }
    }
    
    /**
     * Rotate all the teams but the first team to the right.
     */
    private void rotateTeams() {
        if (teams.size() == 2) {
            return;
        }
        
        T temp = teams.get(teams.size() - 1);
        for (int i = teams.size() - 1; i > 1; i--) {
            teams.set(i, teams.get(i - 1));
        }
        teams.set(1, temp);
    }
    
    /**
     * Get the match at the given week and position
     * 
     * @param week The week of the tournament
     * @param pos The position in the week for the match
     * @return The match at the given values
     */
    public Match<T> getMatch(int week, int pos) {
        return games.get(week)[pos];
    }
    
    /**
     * Returns a string representation of all the matches If the tournament hasn't started, it returns the name of the
     * tournament
     * 
     * @return String representation of the tournament
     */
    @Override
    public String toString() {
        if (!started) {
            return "Tournament " + name;
        }
        
        StringBuilder builder = new StringBuilder(name);
        
        for (int r = 0; r < runThroughs; r++) { // Each round
            builder.append("\nRun Through ");
            builder.append(r + 1);
            for (int w = 0; w < numTeams - 1; w++) { // Each week
                builder.append("\n  Week ");
                builder.append(r * (numTeams - 1) + w + 1);
                
                Match<T>[] week = games.get(r * (numTeams - 1) + w);
                for (Match<T> match : week) { // Each match
                    builder.append("\n    ");
                    builder.append(match.toString());
                }
            }
        }
        
        return builder.toString();
    }
}