package com.codechanakya.plugins.builtin;

import com.codechanakya.plugins.Plugin;
import com.codechanakya.plugins.PluginInfo;

// Only the name is given. Phase defaults to MAIN and order to 100, as declared in PluginInfo.
@PluginInfo(name = "Report")
public class ReportPlugin implements Plugin {

    @Override
    public void run() {
        System.out.println("Report: runs last among the MAIN plugins, just before Cleanup");
    }
}
