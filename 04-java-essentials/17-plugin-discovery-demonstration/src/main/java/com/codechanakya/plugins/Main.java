package com.codechanakya.plugins;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        PluginLoader loader = new PluginLoader();
        List<LoadedPlugin> plugins;
        try {
            plugins = loader.load("/plugins.txt");
        } catch (IOException | PluginLoadException e) {
            System.out.println("Startup failed: " + e.getMessage());
            return;
        }

        System.out.println("--- Skipped ---");
        for (String reason : loader.getSkipped()) {
            System.out.println("  " + reason);
        }

        System.out.println();
        System.out.println("--- Running order ---");
        for (LoadedPlugin loaded : plugins) {
            PluginInfo info = loaded.getInfo();
            System.out.println(String.format("  %-8s %3d  %s", info.phase(), info.order(), info.name()));
        }

        System.out.println();
        System.out.println("--- Running ---");
        for (LoadedPlugin loaded : plugins) {
            loaded.getPlugin().run();
        }

        System.out.println();
        describe(plugins.get(0).getPlugin().getClass());
    }

    /**
     * Prints what reflection can tell us about a class. getDeclaredMethods does
     * not promise any particular order, so the names are sorted to keep the
     * output the same on every machine.
     */
    private static void describe(Class<?> type) {
        System.out.println("--- Reflection on " + type.getSimpleName() + " ---");
        System.out.println("Full name : " + type.getName());
        System.out.println("Package   : " + type.getPackageName());
        System.out.println("Implements: " + Arrays.toString(Arrays.stream(type.getInterfaces()).map(Class::getSimpleName).toArray()));
        PluginInfo info = type.getAnnotation(PluginInfo.class);
        System.out.println("Annotation: name=" + info.name() + ", order=" + info.order()
                + ", phase=" + info.phase() + ", enabled=" + info.enabled());
        String[] methods = new String[type.getDeclaredMethods().length];
        int i = 0;
        for (Method method : type.getDeclaredMethods()) {
            methods[i++] = method.getName();
        }
        Arrays.sort(methods);
        System.out.println("Methods   : " + Arrays.toString(methods));
    }
}
