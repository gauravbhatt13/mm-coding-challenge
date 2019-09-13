package coding.challenge;

import coding.challenge.domain.Event;
import coding.challenge.domain.EventType;
import coding.challenge.domain.Stats;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class StatsAnalyzer {
    // STRING CONSTANTS USED IN THE CODE
    private static final String EVENT_ROW_SEPARATOR = ",";
    private static final String TIME_SPLITTER = ":";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final String ZERO_HOUR = "00:";
    private static final String ONE_HOUR = "01:";

    // INPUT FILE CONTAINING ALL EVENTS
    private String eventsFile = null;
    // LOCAL TIME COMPUTED FROM INPUT TIME
    private LocalTime inputTimestamp = null;
    // EVENTS LIST FILTERED BASED ON INPUT TIME
    private List<Event> filteredEventList = null;
    // ALL EVENTS FOR THE MATCH
    private List<Event> events = new ArrayList<>();

    public StatsAnalyzer(String fileName) {
        this.eventsFile = fileName;
    }

    /**
     * Init method will add all events in the file to 'events' field
     */
    public void init() {
        Scanner fileScanner = null;
        try {
            fileScanner = new Scanner(new File(this.eventsFile));
            fileScanner.nextLine(); //SKIPPING FILE HEADERS
            while (fileScanner.hasNextLine()) {
                addEvent(fileScanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Please make sure that the events file is in classpath (resources folder).");
        } finally {
            if (fileScanner != null) {
                fileScanner.close();
            }
        }
    }

    /**
     * For every row in the input events file, corresponding 'Event' object will be created
     * @param eventRow
     */
    private void addEvent(String eventRow) {
        String[] row = eventRow.split(EVENT_ROW_SEPARATOR);
        Event event = new Event();
        event.setTimestamp(getTimestamp(row[0]));
        event.setEventType(EventType.valueOf(row[1].trim()));

        // SINCE BREAK & END EVENTS DO NOT HAVE TEAMS, SO CHECKING EVENT TYPE
        if (event.getEventType() != EventType.BREAK && event.getEventType() != EventType.END) {
            event.setTeam(row[2].trim());
        }
        this.events.add(event);
    }

    /**
     * This method is the main method to interact with StatsAnalizer class. It will take an input as a time stamp,
     * convert it into a LocalTime object, filter the events, generate stats and return the stats list.
     * @param inputTime
     * @return Stats for each team
     */
    public List<Stats> getStatsList(String inputTime) {
        this.inputTimestamp = getTimestamp(inputTime); //CONVERTING INPUT STRING TIME TO JAVA LOCALTIME

        // FILTERING LIST BASED ON INPUT TIME TO INCLUDE EVENTS ONLY MEANT UPTO THE INPUT TIME
        this.filteredEventList = this.events.stream().filter(event ->
                event.getEventType() != EventType.BREAK && (event.getTimestamp().equals(this.inputTimestamp) ||
                        event.getTimestamp().isBefore(this.inputTimestamp))).collect(Collectors.toList());

        Map<String, Integer> possessionMap = getStats(EventType.POSSESS); // FETCHING STATS FOR POSSESSION
        Map<String, Integer> scoreMap = getStats(EventType.SCORE);  // FETCHING STATS FOR SCORES
        Map<String, Integer> shotMap = getStats(EventType.SHOT);    // FETCHING STATS FOR SHOTS

        // BELOW SNIPPET IS CREATING 'Stats' OBJECT FOR EACH TEAM
        List<Stats> statsList = new ArrayList<>();
        for (String team : possessionMap.keySet()) {
            Stats stats = new Stats();
            stats.setInputTime(inputTime);
            stats.setTeam(team);

            if (scoreMap.get(team) != null) {
                stats.setScores(scoreMap.get(team));
            }

            if (shotMap.get(team) != null) {
                stats.setShots(shotMap.get(team));
            }

            // CALCULATING POSSESSION TIME PERCENTAGE
            float possessionTime = (possessionMap.get(team) * 100) / Float.valueOf(this.inputTimestamp.toSecondOfDay());
            stats.setPossessionTime(String.valueOf(Math.round(possessionTime)) + "%");  // ROUNDING PERCENTAGE
            statsList.add(stats);
        }
        return statsList;
    }

    /**
     * This method will populate a Hashmap for SCORE, SHOT and POSSESS events. Map will contain team name as the key and
     * value can be a count of score/shots or duration of possession in seconds.
     * @param eventType
     * @return
     */
    private Map<String, Integer> getStats(EventType eventType) {
        Map<String, Integer> statMap = new HashMap<>();
        if (eventType == EventType.POSSESS) {
            getPossessionStats(statMap);
        } else {
            for (Event event : this.filteredEventList) {
                if (event.getEventType() == eventType) {
                    if (statMap.get(event.getTeam()) != null) {
                        statMap.put(event.getTeam(), statMap.get(event.getTeam()) +
                                1);
                    } else {
                        statMap.put(event.getTeam(), 1);
                    }
                }
            }
        }
        return statMap;
    }

    /**
     * Method to specifically calculate the durations of possession for each team.
     * @param statMap
     */
    private void getPossessionStats(Map<String, Integer> statMap) {
        int remainder = 0;
        int totalPossessionTime = 0;
        Event currentEvent = this.filteredEventList.get(0);
        
        for (Event event : this.filteredEventList) {
            if (currentEvent.getEventType() == EventType.END || !currentEvent.getTeam().equals(event.getTeam())) {
                // CALCULATING DURATION BETWEEN TWO EVENTS
                int gap = (int) ChronoUnit.SECONDS.between(currentEvent.getTimestamp(), event.getTimestamp());

                if (statMap.get(currentEvent.getTeam()) != null) {
                    statMap.put(currentEvent.getTeam(), statMap.get(currentEvent.getTeam()) +
                            Integer.valueOf(gap));
                } else {
                    statMap.put(currentEvent.getTeam(), Integer.valueOf(gap));
                }
                currentEvent = event;
            }
        }

        // CALCULATING TOTAL POSSESSION TIME
        for (String team : statMap.keySet()) {
            totalPossessionTime += statMap.get(team);
        }

        remainder = this.inputTimestamp.toSecondOfDay() - totalPossessionTime;

        if (statMap.get(currentEvent.getTeam()) != null) {
            statMap.put(currentEvent.getTeam(), statMap.get(currentEvent.getTeam()) +
                    Integer.valueOf(remainder));
        } else {
            if (currentEvent.getTeam() != null) {
                statMap.put(currentEvent.getTeam(), Integer.valueOf(remainder));
            }
        }
    }

    /**
     * This is the core of the logic that converts 90 minutes into 1 hour 30 minutes format that
     * is understood by the java time APIs so that we can easily calculate the durations.
     * @param time
     * @return
     */
    private LocalTime getTimestamp(String time) {
        String[] splitTime = time.split(TIME_SPLITTER);
        String updatedTime = null;
        if (Integer.valueOf(splitTime[0]) < 60) {
            updatedTime = ZERO_HOUR + time;
        } else {
            updatedTime = ONE_HOUR + String.valueOf(Integer.valueOf(splitTime[0]) - 60) + ":" + splitTime[1];
        }
        return LocalTime.parse(updatedTime, DateTimeFormatter.ofPattern(TIME_FORMAT));
    }
}
