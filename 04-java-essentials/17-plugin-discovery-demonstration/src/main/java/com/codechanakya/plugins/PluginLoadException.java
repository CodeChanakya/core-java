package com.codechanakya.plugins;

/** Thrown when a plugin class exists and looks right but cannot be created. */
public class PluginLoadException extends Exception {

    public PluginLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
