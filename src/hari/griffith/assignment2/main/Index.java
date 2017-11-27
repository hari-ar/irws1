package hari.griffith.assignment2.main;

import hari.griffith.assignment2.objects.IndexTable;
import hari.griffith.assignment2.processor.Indexer;
import hari.griffith.assignment2.utils.Utils;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Index {
    public static void main(String[] args) {
        String path = args[0];
        Indexer indexer = new Indexer();
        Utils utils = new Utils();
        try(Stream<Path> pathStream = Files.walk(Paths.get(path), FileVisitOption.FOLLOW_LINKS)){
            pathStream.filter(utils.isValidTextFile()).forEach((Path filePath) -> {
                indexer.processFiles(filePath);
            });
            IndexTable indexTable = indexer.processMatrix();
            System.out.println(indexTable.getMagnitudeOfTfids().keySet().size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
