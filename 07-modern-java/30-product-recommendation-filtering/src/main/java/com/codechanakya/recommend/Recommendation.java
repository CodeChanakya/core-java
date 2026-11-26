package com.codechanakya.recommend;

/** A product, how well it scored, and a plain-language note. */
public record Recommendation(Product product, double score, String note) {
}
