package hari.griffith.assignment2.utils;

import java.nio.file.Path;
import java.util.function.Predicate;

public class Utils {
    public static Predicate<Path> isValidTextFile() {
        return p -> !p.toFile().isDirectory() && p.toFile().getAbsolutePath().endsWith("txt");
    }

    public String removeSpecialCharecters(String input){
        return input.replaceAll("[-]", " ").replaceAll("[^a-zA-Z0-9\\s]", "").toLowerCase();
    }

    public String getStemmedWord(String word){
        Stemmer stemmer =new Stemmer();
        stemmer.add(word.toCharArray(),word.length());
        stemmer.stem();
        return stemmer.toString();
    }


}
