package com.mycompany.app;

import com.mycompany.app.annotations.ClassAnnotation;
import com.mycompany.app.annotations.FieldAnnotation;
import com.mycompany.app.annotations.ParameterAnnotation;
import com.mycompany.app.annotations.SimpleAnnotation;

/**
 * Hello world!
 */
@ClassAnnotation
public class App {

    @FieldAnnotation
    public String appName;

    public static void main(String[] args) {
        try {
            simpleMethod("value");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @SimpleAnnotation(name="simple")
    public static void simpleMethod(@ParameterAnnotation String value) throws NoSuchMethodException {
        System.out.println("simple method " + value);
        SimpleAnnotation annotation = App.class.getDeclaredMethod("simpleMethod", String.class).getAnnotation(SimpleAnnotation.class);
        System.out.println("annotation name value: " + annotation.name());
    }
}
