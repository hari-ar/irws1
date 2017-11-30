package hari.griffith.assignment.part2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;

import static hari.griffith.assignment.part2.AppConstants.DELIMITER;
import static hari.griffith.assignment.part2.AppConstants.INPUT_FILE_NAME;

/**
 *
 * The pre processor step, reads the TF-IDF matrix
 * and generates an objects used for processing query.
 *
 * **/

 class PreQueryProcessor {

    IndexData preProcess(){
        HashMap<String,WordProperties> wordTfidfMap = new HashMap<>(); //TF-IDF Matrix
        HashMap<String,Double> documentMagnitudeSquared = new HashMap<>(); //Magnitudes of document vectors.
        IndexData indexData = new IndexData();

        // Iterate through file
        try(Stream<String> pathStream = Files.lines(Paths.get(INPUT_FILE_NAME))){

            //Read file line by line
            pathStream.forEach((String line) ->{
                WordProperties wordProperties;
                Document document = new Document();
                String[] lineArray = line.split(DELIMITER);
                String key = lineArray[0];
                double tfidfScore = Double.parseDouble(lineArray[1]);
                double idf = Double.parseDouble(lineArray[2]);
                String document_id = lineArray[3]+DELIMITER+lineArray[4];


                //Create tf-idf matrix
                //**********************************************************
                //*
                //* Check if the word is already present in matrix.,
                //* if present add the document to list,
                //* else create a new list of document and put it n the map
                //*
                //* *********************************************************/


                if(wordTfidfMap.containsKey(key)){
                    wordProperties = wordTfidfMap.get(key);
                    ArrayList<Document> listOfDocuments = wordProperties.getDocumentsList();
                    document.setTfIdf(tfidfScore);
                    document.setDocumentId(document_id);
                    listOfDocuments.add(document);
                    wordProperties.setDocumentsList(listOfDocuments);
                    wordProperties.setIdfValue(idf);
                    wordTfidfMap.put(key,wordProperties);
                }
                else{
                    ArrayList<Document> listOfDocuments = new ArrayList<>();
                    document.setTfIdf(tfidfScore);
                    document.setDocumentId(document_id);
                    listOfDocuments.add(document);
                    wordProperties = new WordProperties();
                    wordProperties.setDocumentsList(listOfDocuments);
                    wordProperties.setIdfValue(idf);
                    wordTfidfMap.put(key,wordProperties);
                }
                //****************************************
                // * This part processes the document magnitudes.
                // * Checks if key is present in map., if present,
                // * adds the square of magnitude to existing sum
                // * Else creates a new value of sum which is just
                // * product of tfidf.
                // ****************************************/

                if(documentMagnitudeSquared.containsKey(document_id)){
                    Double tfidsList = documentMagnitudeSquared.get(document_id);
                    tfidsList+= tfidfScore*tfidfScore;
                    documentMagnitudeSquared.put(document_id,tfidsList);
                }
                else {
                    documentMagnitudeSquared.put(document_id,tfidfScore*tfidfScore);
                }
            });
        }
        catch(IOException e){
            e.printStackTrace();
        }
        indexData.setDocumentMagnitudeMap(documentMagnitudeSquared);
        indexData.setWordTfidfMap(wordTfidfMap);
        return indexData;
    }


 }
