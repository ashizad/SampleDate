 SampleDate
===================
Calculate the difference between two dates

I have been using Spring Boot and you can run it from both Maven or Gradle.

To mock some test cases I have been using Mockito, however, I believe Spock is better in large application.


overview of the selected solution
===================
To calculate the date difference, I have to analyse and do reverse engineering of the current Java Date and LocalDateTime  classes to understand different algorithms of Date.
I found that calculating based on time (milisec) is better approach for this project as we don't consider daylight saving and other options in our task.

I have added all related methods (To get milisec for our date) into DateUnit.java, however, It's better to make them separate into another class. For now, I left them in this class.

The main method is getTimeOfOurDateUnit();



## Technology stack

 - Spring Boot
 - Java 8
 - REST API
 - Maven
 - Gradle

## To build this application
From command line:

- Maven:
		mvn clean install

- Gradle:
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

