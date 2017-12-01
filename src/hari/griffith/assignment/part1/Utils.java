package hari.griffith.assignment.part1;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;

import static hari.griffith.assignment.part1.AppConstants.*;
/**
 *
 * A common place for all the utilities. Heard this is good practice.
 *
 **/


 class Utils {


     //A predicate to check if given file is a text file / ends with extension txt
    public static Predicate<Path> isValidTextFile() {
        return p -> !p.toFile().isDirectory() && p.toFile().getAbsolutePath().endsWith("txt"); // Uncomment this line to read out only text files
    }

    //Returns string by removing all the special charecters except hyphen which is converted to space and space itself..!
    public String removeSpecialCharecters(String input){
        return input.replaceAll("[-]", " ").replaceAll("[^a-zA-Z0-9\\s]", "").toLowerCase();
    }

    //A method used to access Stemmer and get stemmed word.
    public String getStemmedWord(String word){
        Stemmer stemmer =new Stemmer();
        stemmer.add(word.toCharArray(),word.length());
        stemmer.stem();
        return stemmer.toString();
    }


    // Method to get file writer.
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

    // Method to write into file. Removes multiple try catches in code.
     public void writeToFile( String data, BufferedWriter writer){
         try {
             writer.write(data);
             writer.newLine();
         } catch (IOException e) {
             e.printStackTrace();
         }
     }


     //Closing resources.
    public void closeWriter (BufferedWriter writer) throws IOException {
        if(writer!=null)
        {
            writer.close();
        }
    }

}
