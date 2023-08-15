CC = javac

.PHONY: all example clean

all: clean example

Person.class: Person.java
	$(CC) Person.java

Main.class: Main.java
	$(CC) Main.java

example: Main.class Person.class
	jar cvfe example.jar Main *.class

clean:
	rm -f *.class *.jar