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
        public static void onEnter(@Advice.Origin String method, @Advice.AllArguments Object[] args) {
            System.err.printf("======= Entering method: %s =======>\n", method);
            System.err.println("Arguments: ");
            for (Object arg : args) {
                // temporary reporting format
                // value: type
                System.err.println(arg + ": " + arg.getClass());
            }
            System.err.println("<======================================");
        }

        @Advice.OnMethodExit
        static void onExit(@Advice.Origin String method) {
            // !
            // fix: if this parameter is added, whole instrumentation will not work
            // public static void onExit(@Advice.Origin String method,
            // @Advice.Return(readOnly = true) boolean returned) {
            System.err.printf("------ Exiting method: %s ------->\n", method);
            System.err.println("Returned value: ");
            // System.err.println(rnt + ": " + rnt.getClass());
            System.err.println("<--------------------------------------");
        }

    }
}
