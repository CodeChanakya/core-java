package com.codechanakya.plugins;

/** When in the program's life a plugin runs. The declaration order is the running order. */
public enum PluginPhase {
    STARTUP,
    MAIN,
    CLEANUP
}
