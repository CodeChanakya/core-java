package com.codechanakya.plugins;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Finds plugins by reading class names from a text file and inspecting them.
 *
 * This is reflection: the program looks at classes it did not know about when
 * it was compiled. Nothing here mentions WelcomePlugin or ReportPlugin by
 * name. Add a class and a line in plugins.txt, and it is picked up.
 *
 * Every class goes through the same four questions, and the answer to each is
 * recorded in "skipped" so a reader can see why something did not run.
 */
public class PluginLoader {

    private final List<String> skipped = new ArrayList<>();

    /** Reads the file and returns the plugins that passed, in running order. */
    public List<LoadedPlugin> load(String resourceName) throws IOException, PluginLoadException {
        List<LoadedPlugin> found = new ArrayList<>();

        for (String className : readClassNames(resourceName)) {
            Class<?> type;
            try {
                type = Class.forName(className);                        // 1. does the class exist?
            } catch (ClassNotFoundException e) {
                skipped.add(shortName(className) + ": class not found");
                continue;
            }

            PluginInfo info = type.getAnnotation(PluginInfo.class);     // 2. is it marked @PluginInfo?
            if (info == null) {
                skipped.add(type.getSimpleName() + ": no @PluginInfo annotation");
                continue;
            }
            if (!Plugin.class.isAssignableFrom(type)) {                 // 3. can it run?
                skipped.add(type.getSimpleName() + ": does not implement Plugin");
                continue;
            }
            if (!info.enabled()) {                                      // 4. is it switched on?
                skipped.add(type.getSimpleName() + ": disabled");
                continue;
            }

            try {
                Plugin instance = (Plugin) type.getDeclaredConstructor().newInstance();
                found.add(new LoadedPlugin(instance, info));
            } catch (ReflectiveOperationException e) {
                throw new PluginLoadException("Could not create " + type.getName(), e);
            }
        }

        Collections.sort(found);
        return found;
    }

    public List<String> getSkipped() {
        return skipped;
    }

    private List<String> readClassNames(String resourceName) throws IOException {
        List<String> names = new ArrayList<>();
        try (InputStream in = PluginLoader.class.getResourceAsStream(resourceName)) {
            if (in == null) {
                throw new IOException("Resource not found: " + resourceName);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (!trimmed.isEmpty() && !trimmed.startsWith("#")) {
                    names.add(trimmed);
                }
            }
        }
        return names;
    }

    private String shortName(String fullName) {
        return fullName.substring(fullName.lastIndexOf('.') + 1);
    }
}
