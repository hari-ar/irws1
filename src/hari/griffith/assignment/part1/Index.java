package hari.griffith.assignment.part1;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 *
 * Main Class.
 *
 * **/

 class Index {
    public static void main(String[] args) {
        String path = args[0];
        Indexer indexer = new Indexer(); //Indexer : The class where magic happens..!!
        Utils utils = new Utils(); //Common utilities
        //This reads all the fines under current directory.. Java8 Streams are so far the efficient way to read from directories
        try(Stream<Path> pathStream = Files.walk(Paths.get(path), FileVisitOption.FOLLOW_LINKS)){
            //Checks for txt files.. returns only txt file. Java 8 filter
            pathStream.filter(utils.isValidTextFile()).forEach((Path filePath) -> {
                // Process files one by one
                indexer.processFiles(filePath);
            });
            //Second step of processing. This will be done after reading all files, since we need document count
            indexer.processMatrixAndSetTFIDFValues();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
