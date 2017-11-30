package hari.griffith.assignment.part2;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static hari.griffith.assignment.part2.AppConstants.OUTPUT_DIRECTORY;

/**
 *
 *
 * Singleton utils class. This is done to avoid each query
 * overwriting the results in file and one point access to io
 * and closing buffer also becomes simpler.
 *
 * **/


class Utils {

    BufferedWriter fileWriter,rankedFileWriter;
    private static Utils utilsInstance = null;
    private Utils(){}

    public static Utils getUtilsInstance(){
        if(utilsInstance==null){
            utilsInstance = new Utils();
        }
        return utilsInstance;
    }

    ///Similar to part 1
   public String removeSpecialCharecters(String input){
       return input.replaceAll("[-]", " ").replaceAll("[^a-zA-Z0-9\\s]", "").toLowerCase();
   }
    ///Similar to part 1
   public String getStemmedWord(String word){
       Stemmer stemmer =new Stemmer();
       stemmer.add(word.toCharArray(),word.length());
       stemmer.stem();
       return stemmer.toString();
   }

    ///Common method to close buffered writer.
   public void closeWriters() {
       if(fileWriter!=null)
       {
           try {
               fileWriter.close();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
       if(rankedFileWriter!=null)
       {
           try {
               rankedFileWriter.close();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
   }

   //Sets filewriter instance with given file name.
    private void setFileWriter(String fileName)  {
        Path path = Paths.get(OUTPUT_DIRECTORY);
        try{
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            path = Paths.get(OUTPUT_DIRECTORY +"/"+fileName);
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            this.fileWriter = Files.newBufferedWriter(path);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


    //Sets filewriter instance with given file name.
    private void setRankedFileWriter(String fileName)  {
        Path path = Paths.get(OUTPUT_DIRECTORY);
        try{
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            path = Paths.get(OUTPUT_DIRECTORY +"/"+fileName);
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            this.rankedFileWriter = Files.newBufferedWriter(path);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    //Method to write into file. Uses the created writer.
    public void writeToFile( String data){
       if(fileWriter==null)
       {
           setFileWriter(AppConstants.QUERY_OUTPUT_FILE_NAME);
       }
       try {
            fileWriter.write(data);
            fileWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void writeToRankedFile( String data){
        if(rankedFileWriter==null)
        {
            setRankedFileWriter(AppConstants.RANKED_QUERY_FILE_NAME);
        }
        try {
            rankedFileWriter.write(data);
            rankedFileWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
