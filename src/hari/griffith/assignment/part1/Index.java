package hari.griffith.assignment.part1;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static hari.griffith.assignment.part1.AppConstants.INPUT_DIRECTORY;
import static hari.griffith.assignment.part1.AppConstants.OUTPUT_DIRECTORY;

/**
 *
 * Main Class.
 *
 * **/

 class Index {
    public static void main(String[] args) {
        Indexer indexer = new Indexer(); //Indexer : The class where magic happens..!!
        Utils utils = new Utils(); //Common utilities
        System.out.println("Reading all the files ending with .txt in "+INPUT_DIRECTORY);
        //This reads all the fines under current directory.. Java8 Streams are so far the efficient way to read from directories
        try(Stream<Path> pathStream = Files.walk(Paths.get(INPUT_DIRECTORY), FileVisitOption.FOLLOW_LINKS)){
            //Checks for txt files.. returns only txt file. Java 8 filter
            pathStream.filter(utils.isValidTextFile()).forEach((Path filePath) -> {
                //Process files one by one
                System.out.println("Processing File "+filePath);
                indexer.processFiles(filePath);
                System.out.println("Processed the File "+filePath);
            });
            //Second step of processing. This will be done after reading all files, since we need document count
            System.out.println("Reading all files Complete..!! Indexing will be done in a breeze..!! Fasten Seat belts..!");
            indexer.processMatrixAndSetTFIDFValues();
            System.out.println("All done..!! Outputs are present at "+OUTPUT_DIRECTORY);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
