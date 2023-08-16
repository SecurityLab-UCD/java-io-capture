# java-io-capture

Capture JAVA function IO and report to file

## Build

```sh
mvn clean package
```

## Example

```sh
cd example; make; cd ..
java -javaagent:./target/java-io-capture-1.0-SNAPSHOT.jar -jar example/example.jar
```
