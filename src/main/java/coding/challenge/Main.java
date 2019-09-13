package coding.challenge;

import coding.challenge.domain.Stats;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String... args) {
        StatsAnalyzer statsAnalyzer = new StatsAnalyzer("src/main/resources/events.csv");
        statsAnalyzer.init(); //LOAD ALL EVENTS FROM CSV FILE.

        Scanner input = new Scanner(System.in);
        System.out.print("input time (mm:ss) : ");
        String inputTime = input.nextLine(); //READ INPUT TIME

        List<Stats> statsList = statsAnalyzer.getStatsList(inputTime);

        //PRINT THE REPORT ON THE CONSOLE
        System.out.printf("%s %s %s %s %s\n", "Timestamp", ", Team", ", Possession", ", Shot", ", Score");
        for (Stats stats : statsList) {
            System.out.printf("%6s %6s %9s %9s %6s\n", stats.getInputTime(), ", " + stats.getTeam(), ", " +
                    stats.getPossessionTime(), ", " + stats.getShots(), ", " + stats.getScores());
        }
    }
}
