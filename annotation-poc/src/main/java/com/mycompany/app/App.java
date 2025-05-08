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
        simpleMethod("value");
    }

    @SimpleAnnotation(name="simple")
    public static void simpleMethod(@ParameterAnnotation String value){
        System.out.println("simple method " + value);
    }
}
