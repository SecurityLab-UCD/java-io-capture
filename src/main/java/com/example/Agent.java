package com.example;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.lang.instrument.Instrumentation;

public class Agent {
    public static void premain(String agentArgs, Instrumentation inst) {
        new AgentBuilder.Default()
                .type(ElementMatchers.any())
                .transform((builder, typeDescription, classLoader, javaModule, protectionDomain) -> builder
                        .method(ElementMatchers.any())
                        .intercept(Advice.to(Interceptor.class)))
                .installOn(inst);
    }

    public static class Interceptor {
        @Advice.OnMethodEnter
        static void onEnter(@Advice.Origin String method, @Advice.AllArguments Object[] args) {
            System.err.println("Entering method: " + method);
            System.err.println("Arguments: ");
            for (Object arg : args) {
                System.err.println(arg);
            }
        }

        @Advice.OnMethodExit
        static void onExit(@Advice.Return(readOnly = false) Object returned) {
            System.err.println("Returned value: " + returned);
        }
    }
}
