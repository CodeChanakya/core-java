package com.codechanakya.plugins.builtin;

import com.codechanakya.plugins.Plugin;
import com.codechanakya.plugins.PluginInfo;

/** I2: a plugin whose constructor needs an argument, so the loader cannot create it. */
@PluginInfo(name = "Locked", order = 5)
public class LockedPlugin implements Plugin {

    public LockedPlugin(String secret) {
    }

    @Override
    public void run() {
        System.out.println("never runs");
    }
}
