package hari.griffith.assignment.part1;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;

import static hari.griffith.assignment.part1.AppConstants.*;

 class Utils {
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


    public BufferedWriter getFileWriter(String fileName) throws IOException {

        Path path = Paths.get(OUTPUT_DIRECTORY);
        if (!Files.exists(path)) {
                Files.createDirectories(path);
        }
        path = Paths.get(OUTPUT_DIRECTORY +"/"+fileName);
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        BufferedWriter writer = Files.newBufferedWriter(path);
        return writer;
    }

    public void closeWriter (BufferedWriter writer) throws IOException {
        if(writer!=null)
        {
            writer.close();
        }

    }

}
