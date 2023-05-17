# ENSF380_Final_Project

A GUI Application that reads from a database and rearranges tasks to meet specifications. The final schedule is printed in a text file.

# What I learned

- UML diagram design according to specifications defined by a client
- Object-oriented programming principles such as inheritance, abstraction, encapsulation, and polymorphism
- SQL queries to fetch items from the database
- GUI using AWT and Swing in Java
- Use of data structures such as a TreeMap, ArrayLists to organize data
- Unit test creation
- Java documentation

# How to run the program

Clone the repository on your local machine and run the treatments.sql file to create the SQL database.
Creating the database is important as this program functionality depends on the database input.

On the command line, use the `cd` command to navigate to the `Scheduling_Application` folder.

To run the program on Windows:
Compile the application using the commands `javac -cp .;mysql-connector-java-8.0.23.jar edu/ucalgary/oop/GUI.java`
Run the application using the commands `java -cp .;mysql-connector-java-8.0.23.jar edu.ucalgary.oop.GUI`

To run the program on Mac:
Compile the application using the commands `javac -cp .:mysql-connector-java-8.0.23.jar edu/ucalgary/oop/GUI.java`
Run the application using the commands `java -cp .:mysql-connector-java-8.0.23.jar edu.ucalgary.oop.GUI`

### .gitignore

There is a gitignore file which ignores the lib folder used for library dependencies, bin folder used to store .class files. These files and folders do not need to be version controlled since it is not a part of the source code.

---
