package com.codechanakya.parallel;

import java.util.ArrayList;
import java.util.List;

/**
 * A slice of the data: from {@code from} up to, but not including, {@code to}.
 */
public record Chunk(int from, int to) {

    /**
     * Cuts {@code length} items into {@code parts} slices that are as even as possible. When the
     * items do not divide evenly, the first slices get one extra each.
     *
     * <p>Nothing is lost and nothing overlaps: the slices follow each other exactly.
     */
    public static List<Chunk> split(int length, int parts) {
        if (parts <= 0) {
            throw new IllegalArgumentException("Need at least one part");
        }
        List<Chunk> chunks = new ArrayList<>();
        int base = length / parts;
        int extra = length % parts;
        int start = 0;
        for (int i = 0; i < parts; i++) {
            int size = base + (i < extra ? 1 : 0);
            chunks.add(new Chunk(start, start + size));
            start += size;
        }
        return chunks;
    }
}
