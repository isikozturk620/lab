/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package footballtournamentsystem;




import java.util.Scanner;

public class FootballTournamentSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        
        String[] teamNames = {"Team A", "Team B", "Team C", "Team D"};
        
        
        int[] matchesPlayed = new int[4];
        int[] wins = new int[4];
        int[] draws = new int[4];
        int[] losses = new int[4];
        int[] goalsFor = new int[4];
        int[] goalsAgainst = new int[4];
        int[] points = new int[4];

       
        int[][] fixture = {
            {0, 1}, // Match 1: Team A vs Team B[cite: 1]
            {0, 2}, // Match 2: Team A vs Team C[cite: 1]
            {0, 3}, // Match 3: Team A vs Team D[cite: 1]
            {1, 2}, // Match 4: Team B vs Team C[cite: 1]
            {1, 3}, // Match 5: Team B vs Team D[cite: 1]
            {2, 3}  // Match 6: Team C vs Team D[cite: 1]
        };

        int[] score1 = new int[6];
        int[] score2 = new int[6];

        
        System.out.println("==========================================");
        System.out.println("   Football Tournament Standings System   ");
        System.out.println("==========================================");
        System.out.println("\nFixed Fixture:");
        System.out.println("Match 1: Team A vs Team B");
        System.out.println("Match 2: Team A vs Team C");
        System.out.println("Match 3: Team A vs Team D");
        System.out.println("Match 4: Team B vs Team C");
        System.out.println("Match 5: Team B vs Team D");
        System.out.println("Match 6: Team C vs Team D\n");
        System.out.println("------------------------------------------");
        System.out.println("Please enter the scores for each match:\n");

        // Kullanıcıdan skorların alınması[cite: 3]
        for (int i = 0; i < 6; i++) {
            int t1Idx = fixture[i][0];
            int t2Idx = fixture[i][1];

            System.out.println("Match " + (i + 1) + ": " + teamNames[t1Idx] + " vs " + teamNames[t2Idx]);
            System.out.print("  -> " + teamNames[t1Idx] + " goals: ");
            score1[i] = scanner.nextInt();
            System.out.print("  -> " + teamNames[t2Idx] + " goals: ");
            score2[i] = scanner.nextInt();
            System.out.println();
        }

        System.out.println("==========================================");
        System.out.println("        ALL ENTERED MATCH SCORES          ");
        System.out.println("==========================================");
        for (int i = 0; i < 6; i++) {
            int t1Idx = fixture[i][0];
            int t2Idx = fixture[i][1];

            System.out.println("Match " + (i + 1) + ": " + teamNames[t1Idx] + " " + score1[i] + " - " + score2[i] + " " + teamNames[t2Idx]);

          
            matchesPlayed[t1Idx]++;
            matchesPlayed[t2Idx]++;

            
            goalsFor[t1Idx] += score1[i];
            goalsAgainst[t1Idx] += score2[i];
            goalsFor[t2Idx] += score2[i];
            goalsAgainst[t2Idx] += score1[i];

            if (score1[i] > score2[i]) {
                wins[t1Idx]++;
                points[t1Idx] += 3;
                losses[t2Idx]++;
            } else if (score2[i] > score1[i]) {
                wins[t2Idx]++;
                points[t2Idx] += 3;
                losses[t1Idx]++;
            } else {
                draws[t1Idx]++;
                points[t1Idx] += 1;
                draws[t2Idx]++;
                points[t2Idx] += 1;
            }
        }

        System.out.println("\n==================================================================");
        System.out.println("                         STANDINGS TABLE                          ");
        System.out.println("==================================================================");
        System.out.printf("%-10s | %-8s | %-5s | %-5s | %-5s | %-8s | %-15s%n", 
                "Team", "Played", "Wins", "Draws", "Losses", "Points", "Goal Diff");
        System.out.println("------------------------------------------------------------------");

        for (int i = 0; i < 4; i++) {
            int goalDiff = goalsFor[i] - goalsAgainst[i];
            System.out.printf("%-10s | %-8d | %-5d | %-5d | %-5d | %-8d | %-15d%n",
                    teamNames[i], matchesPlayed[i], wins[i], draws[i], losses[i], points[i], goalDiff);
        }
        System.out.println("==================================================================");

        
        int championIdx = 0;
        for (int i = 1; i < 4; i++) {
            if (points[i] > points[championIdx]) {
                championIdx = i;
            } else if (points[i] == points[championIdx]) {
                // Puanlar eşitse averaja bakılır[cite: 2]
                int diffI = goalsFor[i] - goalsAgainst[i];
                int diffChampion = goalsFor[championIdx] - goalsAgainst[championIdx];
                if (diffI > diffChampion) {
                    championIdx = i;
                }
            }
        }

        System.out.println("\nTournament Champion: " + teamNames[championIdx]);
        System.out.println("==================================================================");

       
    }}

