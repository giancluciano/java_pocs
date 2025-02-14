package com.mycompany.app;

import org.graalvm.polyglot.Context;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        try (Context context = Context.create()) {
            context.eval("python", "print('Hello from GraalPy!')");
        }
    }
}
