# Coding challenge
The coding challenge app reads csv files with events for a Soccer match and gives statistics for each team at any given 
instance of time. The application is a maven project with JUNIT as dependency mentioned in the pom.xml.

## Design
#### Overview
StatsAnalyzer class takes filename as input in constructor and sets its eventsFile property.An init call on the analyzer
loads all the events from the csv file. Key logic here is the transformation of event time stamp for each event file 
into java.time.LocalTime from java time APIs. This allows us to calculate duration between events.
#### Duration Calculation Logic
1. create a map to store (team, possessionTime) as key value pair.
2. loop events
3. any time team changes in events stream (except BREAK, END), it indicates the possession is gone
4. calculate the duration by subtracting current event time with the last event associated with the other team
5. look for existing possessionTime in the map and add calculated duration to it and replace in map.


### Steps to run
1. Run Main.java
2. Provide input timestamp in mm:ss format.
3. Alternatively, run the JUnit test cases inside src/test/java/coding/challenge/test. Test cases will also print the 
stats.