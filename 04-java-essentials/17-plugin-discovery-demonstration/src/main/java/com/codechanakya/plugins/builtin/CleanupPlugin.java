package com.codechanakya.plugins.builtin;

import com.codechanakya.plugins.Plugin;
import com.codechanakya.plugins.PluginInfo;
import com.codechanakya.plugins.PluginPhase;

@PluginInfo(name = "Cleanup", order = 10, phase = PluginPhase.CLEANUP)
public class CleanupPlugin implements Plugin {

    @Override
    public void run() {
        System.out.println("Cleanup: temporary files removed");
    }
}
