package com.codechanakya.plugins.builtin;

import com.codechanakya.plugins.Plugin;
import com.codechanakya.plugins.PluginInfo;

/** B1: a new plugin. Adding it needed one new class and one new line in plugins.txt. */
@PluginInfo(name = "Greeting", order = 20, description = "Greets the user by name")
public class GreetingPlugin implements Plugin {

    @Override
    public void run() {
        System.out.println("Hello from GreetingPlugin");
    }
}
