package com.agent;

import net.bytebuddy.asm.Advice;

public class Interceptor {
    @Advice.OnMethodEnter
    static void onEnter(@Advice.Origin String method, @Advice.AllArguments Object[] args) {
        System.out.println("Entering method: " + method);
        // System.out.println("Arguments: ");
        // for (Object arg : args) {
        // System.out.println(arg);
        // }
    }

    @Advice.OnMethodExit
    static void onExit(@Advice.Return(readOnly = false) Object returned) {
        // System.out.println("Returned value: " + returned);
    }
}
