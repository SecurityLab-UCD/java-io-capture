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
import java.util.Arrays;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.lang.instrument.Instrumentation;
import java.util.stream.Collectors;

public class Agent {
    public static void premain(String agentArgs, Instrumentation inst) {
        new AgentBuilder.Default()
                .type(ElementMatchers.any())
                .transform((builder, typeDescription, classLoader, module, domain) -> builder
                        .method(ElementMatchers.any())
                        .intercept(MethodDelegation.to(Interceptor.class)))
                .installOn(inst);
    }

    // todo
    // 1. dereference for "pointer" types (like String)
    // 2. handle exceptions
    // 3. fix reporting format
    public static class Interceptor {

        private static ReportTable report_table = new ReportTable(10);

        @RuntimeType
        public static Object intercept(
                @This(optional = true) Object origin,
                @Origin(cache = false) Method method,
                @SuperCall Callable<?> callable,
                @AllArguments Object[] args) throws Exception {

            // dump report table at program exit
            if (method.getName().equals("main")) {
                Runtime.getRuntime().addShutdownHook(new Thread() {
                    public void run() {
                        System.err.println("report table:");
                        System.err.println(report_table.toString());
                    }
                });
                return callable.call();
            }

            ArrayList<String> inputs = new ArrayList<String>();
            ArrayList<String> outputs = new ArrayList<String>();

            // 1. report input arguments
            inputs.addAll(
                    Arrays.stream(args)
                            .map(Object::toString)
                            .collect(Collectors.toCollection(ArrayList::new)));
            // 2. call the original method
            Object rnt = callable.call();

            // 3. report return value
            if (rnt == null) {
                outputs.add("");
            } else {
                outputs.add(rnt.toString());
            }

            // 4. return the original return value
            // fixme: report not working
            // report_table.report(method.getName(), new IOPair(inputs, outputs));
            return rnt;
        }
    }
}
