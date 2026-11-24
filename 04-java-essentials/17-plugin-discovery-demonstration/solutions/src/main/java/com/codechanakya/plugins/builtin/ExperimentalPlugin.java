package com.codechanakya.plugins.builtin;

import com.codechanakya.plugins.Plugin;
import com.codechanakya.plugins.PluginInfo;

/** Fully written, but switched off. The loader finds it and leaves it alone. */
@PluginInfo(name = "Experimental", order = 1, enabled = false)
public class ExperimentalPlugin implements Plugin {

    @Override
    public void run() {
        System.out.println("Experimental: this should never print");
    }
}
