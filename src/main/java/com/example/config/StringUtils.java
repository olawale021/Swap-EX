package com.example.config;


public class StringUtils {
    public static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    public static String capitalizeWords(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        String[] words = str.split("\\s+");
        StringBuilder capitalized = new StringBuilder();
        for (String word : words) {
            if (capitalized.length() > 0) {
                capitalized.append(" ");
            }
            capitalized.append(capitalizeFirstLetter(word));
        }
        return capitalized.toString();
    }
}
