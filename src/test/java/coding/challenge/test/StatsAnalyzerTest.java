package coding.challenge.test;

import coding.challenge.StatsAnalyzer;
import coding.challenge.domain.Stats;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class StatsAnalyzerTest {
    private static final String TEST_FILE = "src/test/resources/tests.csv";
    private List<Stats> statsList = null;

    @Test
    public void testScoreEach() {
        StatsAnalyzer statsAnalyzer = new StatsAnalyzer(TEST_FILE);
        statsAnalyzer.init();
        statsList = statsAnalyzer.getStatsList("90:00");
        Assert.assertEquals(1, statsList.get(0).getScores());
        Assert.assertEquals(1, statsList.get(1).getScores());
    }

    @Test
    public void testTwoShotsEach() {
        StatsAnalyzer statsAnalyzer = new StatsAnalyzer(TEST_FILE);
        statsAnalyzer.init();
        statsList = statsAnalyzer.getStatsList("90:00");
        Assert.assertEquals(2, statsList.get(0).getShots());
        Assert.assertEquals(2, statsList.get(1).getShots());
    }

    @Test
    public void testNoShots() {
        StatsAnalyzer statsAnalyzer = new StatsAnalyzer(TEST_FILE);
        statsAnalyzer.init();
        statsList = statsAnalyzer.getStatsList("01:19");
        Assert.assertEquals(0, statsList.get(0).getShots());
        Assert.assertEquals(0, statsList.get(1).getShots());
    }

    @Test
    public void testNoScores() {
        StatsAnalyzer statsAnalyzer = new StatsAnalyzer(TEST_FILE);
        statsAnalyzer.init();
        statsList = statsAnalyzer.getStatsList("02:19");
        Assert.assertEquals(0, statsList.get(0).getScores());
        Assert.assertEquals(0, statsList.get(1).getScores());
    }

    @Test
    public void testSingleEvent() {
        StatsAnalyzer statsAnalyzer = new StatsAnalyzer(TEST_FILE);
        statsAnalyzer.init();
        statsList = statsAnalyzer.getStatsList("00:00");
        Assert.assertEquals(0, statsList.get(0).getShots());
    }
    
    @After
    public void printStats(){
        System.out.printf("%s %s %s %s %s\n", "Timestamp", ", Team", ", Possession", ", Shot", ", Score");
        for (Stats stats : statsList) {
            System.out.printf("%6s %6s %9s %9s %6s\n", stats.getInputTime(), ", " + stats.getTeam(), ", " +
                    stats.getPossessionTime(), ", " + stats.getShots(), ", " + stats.getScores());
        }
    }
}
