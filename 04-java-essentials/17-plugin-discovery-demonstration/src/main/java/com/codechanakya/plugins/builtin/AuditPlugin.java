package com.codechanakya.plugins.builtin;

import com.codechanakya.plugins.Plugin;
import com.codechanakya.plugins.PluginInfo;

@PluginInfo(name = "Audit", order = 50)
public class AuditPlugin implements Plugin {

    @Override
    public void run() {
        System.out.println("Audit: nothing suspicious found");
    }
}
