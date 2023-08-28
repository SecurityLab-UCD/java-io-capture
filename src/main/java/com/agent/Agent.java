package com.agent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
// Packages to write JSON dump, might not need in this file
import java.io.FileWriter;
import java.io.IOException;

import static net.bytebuddy.matcher.ElementMatchers.*;

public class Agent {
    public static void premain(String agentArgs, Instrumentation inst) {
        new AgentBuilder.Default()
                // ignore functions in this com.agent package to avoid circular reference
                .type(not(nameStartsWith("com.agent")))
                .transform((builder, typeDescription, classLoader, module, domain) -> builder
                        .method(any())
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
                        try {
                            // ReportTable localCopy = new ReportTable(report_table);
                            // localCopy.toJson();
                            FileWriter file = new FileWriter("./dump/temp.json");
                            report_table.toJson();
                            //file.write(report_table.toJson().toString());
                            file.close();
                        } catch (IOException e) {
                            System.err.println("Error opening file!");
                            e.printStackTrace();
                        }
                    }
                });

                // Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                //     synchronized (report_table) {
                //         System.err.println("report table:");
                //         System.err.println(report_table.toString());
                //         try {
                //             FileWriter file = new FileWriter("./dump/temp.json");
                //             // Call toJson() within synchronized block
                //             file.write(report_table.toJson().toString());
                //             file.close();
                //         } catch (IOException e) {
                //             System.err.println("Error opening file!");
                //             e.printStackTrace();
                //         }
                //     }
                // }));
                return callable.call();
            }

            ArrayList<String> inputs = new ArrayList<String>();
            ArrayList<String> outputs = new ArrayList<String>();

            // 1. collect function inputs
            inputs.addAll(
                    Arrays.stream(args)
                            .map(Object::toString)
                            .collect(Collectors.toCollection(ArrayList::new)));

            // 2. call the original method and collect outputs
            Object rnt = callable.call();
            outputs.add(rnt == null ? "" : rnt.toString());

            // 3. report collected inputs and outputs to report table
            report_table.report(method.getName(), new IOPair(inputs, outputs));

            // 4. return the original return value
            return rnt;
        }
    }
}

