# Coding challenge
The coding challenge app reads csv files with events for a Soccer match and gives statistics for each team at any given 
instance of time. The application is a maven project with JUNIT as dependency mentioned in the pom.xml. Place your 
events file under 'src/main/resources/events.csv'.

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

#### Timestamp logic
1. For any given timestamp split on colon(:)
2. Convert each split into Integer. In case of any exception show 'Invalid input message' and return null.
3. Check if the timestamp conversion was successful based on null check. If not then show invalid message with expected 
format and range and exit.
4. If integer conversion was successful then compare if first part of the split is less than equals to 90 and second
part of the split is not greater than 59. Throw exception if any of the conditions fail.

### Steps to setup
1. Unzip the source zip file.
2. Use any IDE to import/create a project from source.
3. Install maven pom dependencies mentioned in pom.xml
> Note: If maven is not installed then please delete the test folder and run only the Main program.

### Steps to run
1. Place events.csv file in location : 'src/main/resources/events.csv'
2. Run Main.java
3. Provide input timestamp in mm:ss format.
4. Alternatively, run the JUnit test cases inside src/test/java/coding/challenge/test. Test cases will also print the 
stats.