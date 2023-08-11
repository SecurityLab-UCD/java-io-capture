package com.example;
import com.agent.Agent;

/**
 * Hello world!
 *
 */
public class App 
{
    public static int add(int x, int y) {
        return x + y;
    }
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        int z = add(1, 2);
        System.out.println(z);
    }
}
