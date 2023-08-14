CC = javac

.PHONY: all example clean

all: clean example

example: example/Main.java
	$(CC) -cp ./target/java-io-capture-1.0-SNAPSHOT.jar example/Main.java -d .

clean:
	rm -f *.class