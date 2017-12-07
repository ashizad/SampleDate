 SampleDate
===================
Calculate the difference between two dates

I have been using Spring Boot and you can run it from both Maven and Gradle.

To mock some test cases I have been using Mockito, however, I believe Spock is better in large application (as part of Integration testing). 


overview of the selected solution
===================
To calculate the date difference, I have to analyse and do reverse engineering of the current Java Date and LocalDateTime  classes to understand different algorithms of Date.
I found that calculating based on time (milisec) is better approach for this project as I don't consider daylight saving and other options in our task.

I calculate each DateUnit object based on milisec and then I subtract them from each other. The result returns time since 1970 in milliseconds, so the difference is also in milliseconds. 1000 milliseconds make 1 second, 60 seconds make 1 minute, 60 minutes make 1 hour and 24 hours make 1 day, as a result I have have to divide with milisec to get days amount:
long diffDays = diffTime / (1000 * 60 * 60 * 24);

I have added all related methods (To get milisec for our date) into DateUnit.java, however, It's better off to make them in a separate class. For now, I left them in this class.

The main method is getTimeOfOurDateUnit();


## Technology stack

 - Spring Boot
 - Java 8
 - REST API
 - Maven
 - Gradle

## To build this application
From command line Maven::

	mvn clean install

Or Gradle:

	./gradlew build


From eclipse, you can also run it from Sprin Boot (You need to have STS plugin)
	 
## To run this application

Eiether you can run it from Maven in the folder you have the project:

	mvn spring-boot:run

Or you can run it from Gradle:

	./gradlew run
You can then access the sample here: [http://localhost:8080/](http://localhost:8080/)

To load from file, you have to use Ecnode URL. For instance, using %5C instead of /: as an example:

http://localhost:8080/file/path?path=c:%5CAsh%5CProjects%5Csrc%5Cmain%5Cresources%5Csample.txt

To load from standard input, you can type in like this path. Just bear in mind, if you have any special characters, you must encode them:

http://localhost:8080/input?input=20 11 1980, 28 10 1990 

You also can use curl to test REST API, for instance:

curl localhost:8080/file/path?path=K:%5CAsh%5CProjects%5CSampleDate%5Csrc%5Cmain%5Cresources%5Csample.txt

## Unit Test
There are almost 25 test cases for this project.

