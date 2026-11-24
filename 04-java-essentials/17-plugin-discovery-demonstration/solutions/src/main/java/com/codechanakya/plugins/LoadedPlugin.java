package com.codechanakya.plugins;

/**
 * A plugin that passed every check, together with its annotation.
 *
 * Comparable orders plugins by phase first, then by the order number, so a
 * plain sort puts them in the order they should run.
 */
public class LoadedPlugin implements Comparable<LoadedPlugin> {

    private final Plugin plugin;
    private final PluginInfo info;

    public LoadedPlugin(Plugin plugin, PluginInfo info) {
        this.plugin = plugin;
        this.info = info;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public PluginInfo getInfo() {
        return info;
    }

    @Override
    public int compareTo(LoadedPlugin other) {
        int byPhase = info.phase().compareTo(other.info.phase());
        if (byPhase != 0) {
            return byPhase;
        }
        return Integer.compare(info.order(), other.info.order());
    }
}
