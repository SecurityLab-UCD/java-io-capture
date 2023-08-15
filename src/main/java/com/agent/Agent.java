package com.agent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.lang.instrument.Instrumentation;

public class Agent {
    public static void premain(String agentArgs, Instrumentation inst) {
        System.err.println("Agent premain");
        new AgentBuilder.Default()
                .type(ElementMatchers.any())
                .transform((builder, typeDescription, classLoader, module) -> builder
                        .method(ElementMatchers.any())
                        .intercept(Advice.to(Interceptor.class)))
                .installOn(inst);
    }

    public static class Interceptor {
        @Advice.OnMethodEnter
        static void onEnter(@Advice.Origin String method, @Advice.AllArguments Object[] args) {
            System.out.println("Entering method: " + method);
            System.out.println("Arguments: ");
            for (Object arg : args) {
                System.out.println(arg);
            }
        }

        @Advice.OnMethodExit
        static void onExit(@Advice.Origin String method, @Advice.Return(readOnly = false) Object returned) {
            System.out.println("Exiting method: " + method);
            System.out.println("Returned value: " + returned);
        }
    }

}
