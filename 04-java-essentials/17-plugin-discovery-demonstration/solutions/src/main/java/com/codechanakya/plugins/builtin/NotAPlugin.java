package com.codechanakya.plugins.builtin;

/** Listed in plugins.txt by mistake: it has no @PluginInfo, so the loader ignores it. */
public class NotAPlugin {

    public void run() {
        System.out.println("This should never print either");
    }
}
