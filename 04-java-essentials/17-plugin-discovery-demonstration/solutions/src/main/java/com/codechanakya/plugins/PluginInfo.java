package com.codechanakya.plugins;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Labels a class as a plugin and describes it.
 *
 * An annotation is metadata: information ABOUT a class that does not change
 * what the class does by itself. Someone has to read it. Here the PluginLoader
 * reads it at run time using reflection.
 *
 * The two lines above the declaration are annotations on the annotation:
 *   Retention RUNTIME  keep this information in the compiled class so the
 *                      running program can read it (the default would drop it)
 *   Target TYPE        it may only be placed on a class, interface or enum
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PluginInfo {

    /** The name shown in reports. Required: it has no default. */
    String name();

    /** Smaller numbers run first within a phase. */
    int order() default 100;

    PluginPhase phase() default PluginPhase.MAIN;

    /** A disabled plugin is found but never started. */
    boolean enabled() default true;

    /** I1: a short sentence for reports. */
    String description() default "";
}
