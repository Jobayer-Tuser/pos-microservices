package me.jobayeralmahmud.library.utils;

import java.text.Normalizer;
import java.util.Locale;

public class Slugify {
    public static String toSlug(String input) {
        if (input == null) return "";

        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replaceAll("[^\\w\\s-]", "")
                .replaceAll("^-|-$", "")
                .replace(" ", "-")
                .toLowerCase(Locale.ENGLISH);
    }
}