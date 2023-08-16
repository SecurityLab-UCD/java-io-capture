package com.agent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.lang.instrument.Instrumentation;

public class Agent {
    public static void premain(String agentArgs, Instrumentation inst) {
        new AgentBuilder.Default()
                .type(ElementMatchers.any())
                .transform((builder, typeDescription, classLoader, module) -> builder
                        .method(ElementMatchers.any())
                        .intercept(MethodDelegation.to(Interceptor.class)))
                .installOn(inst);
    }

    // todo
    // 1. dereference for "pointer" types (like String)
    // 2. handle exceptions
    // 3. fix reporting format
    public static class Interceptor {
        @RuntimeType
        public static Object intercept(
                @This(optional = true) Object origin,
                @Origin(cache = false) Method method,
                @SuperCall Callable<?> callable,
                @AllArguments Object[] args) throws Exception {

            // 1. report input arguments
            System.err.printf("======= Entering method: %s =======>\n", method);
            System.err.println("Arguments: ");
            for (Object arg : args) {
                // temporary reporting format
                // value: type
                System.err.println(arg + ": " + arg.getClass());
            }
            System.err.println("<======================================");

            // 2. call the original method
            Object rnt = callable.call();

            // 3. report return value
            System.err.printf("------ Exiting method: %s ------->\n", method);
            System.err.println("Returned value: ");
            if (rnt == null) {
                System.err.println("null");
            } else {
                System.err.println(rnt + ": " + rnt.getClass());
            }
            System.err.println("<--------------------------------------");

            // 4. return the original return value
            return rnt;
        }
    }
}
