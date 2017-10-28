package com.randomappsinc.aroundme.utils;

public class StringUtils {

    /**
     *  Given a string of words, capitalizes the first letter in each word
     *  and lowercases the rest.
     *
     *  @param givenString The input string
     *  @return The formatted string
     */
    public static String capitalizeWords(String givenString) {
        String[] words = givenString.split(" ");
        StringBuilder capitalizedWords = new StringBuilder();

        for (String word : words) {
            String trimmed = word.trim();

            if (trimmed.isEmpty()) {
                continue;
            }

            capitalizedWords
                    .append(Character.toUpperCase(trimmed.charAt(0)))
                    .append(word.substring(1))
                    .append(" ");
        }
        return capitalizedWords.toString().trim();
    }
}
