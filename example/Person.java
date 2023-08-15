
public class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void introduce() {
        System.out.printf("Hi, my name is %s and I am %d years old.\n", name, age);
    }
}