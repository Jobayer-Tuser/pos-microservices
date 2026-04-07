package me.jobayeralmahmud.library.utils;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public class Slugify {
    public static String toSlug(String input) {
        if (input.isBlank()) return "";

        Pattern nonLatin = Pattern.compile("[^\\w-]");
        Pattern whitespace = Pattern.compile("\\s");

        var newInput = whitespace.matcher(input).replaceAll("-");

        var normalized = Normalizer.normalize(newInput, Normalizer.Form.NFD);

        String slug  = nonLatin.matcher(normalized).replaceAll("");

        return slug = input.trim().toLowerCase(Locale.ENGLISH).replaceAll("^-+|-+$", "");
    }
}