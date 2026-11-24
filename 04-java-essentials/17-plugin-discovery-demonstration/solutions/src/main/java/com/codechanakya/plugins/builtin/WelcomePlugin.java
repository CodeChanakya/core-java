package com.codechanakya.plugins.builtin;

import com.codechanakya.plugins.Command;
import com.codechanakya.plugins.Plugin;
import com.codechanakya.plugins.PluginInfo;
import com.codechanakya.plugins.PluginPhase;

@PluginInfo(name = "Welcome", order = 10, phase = PluginPhase.STARTUP, description = "Says hello")
public class WelcomePlugin implements Plugin {

    @Override
    public void run() {
        System.out.println("Welcome to the plugin demo");
    }

    @Command(label = "tip")
    public void showTip() {
        System.out.println("Tip: annotations describe, reflection reads");
    }
}
