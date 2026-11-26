package com.codechanakya.queue;

/** How urgently an order should be handled. Express orders jump ahead of standard ones. */
public enum Priority {
    EXPRESS, STANDARD, BULK      // I1: BULK sits last, so it is served last
}
