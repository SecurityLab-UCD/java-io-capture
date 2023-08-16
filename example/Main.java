public class Main {
    public static int add(int x, int y) {
        return x + y;
    }

    public static void main(String[] args) {
        System.out.println("Hello world from java-io-capture example project!");
        int z = add(1, 2);

        Person p = new Person("John", 13);
        p.introduce();

        p.growBy(2);
        int newAge = p.getAge();
        p.introduce();
    }
}