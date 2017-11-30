package hari.griffith.assignment.part2;

import java.util.ArrayList;
import java.util.HashMap;

import static hari.griffith.assignment.part2.AppConstants.DELIMITER;
import static hari.griffith.assignment.part2.AppConstants.STOP_WORDS_LIST;

/****
 *
 * This class is used for processing query
 * and printing the output with the cosine
 * similarity scores.
 *
 * ****/


class QueryProcessor {

     void processQuery(IndexData indexData, QueryObject queryObject) {
        Utils utils = Utils.getUtilsInstance();
        HashMap<String,Double> queryVector = new HashMap<>();
         double magnitudeSquare = 0;
        String[] queryArray = utils.removeSpecialCharecters(queryObject.getQueryText().trim()).split("\\s+");
        HashMap<String, Double> sumProductValuesForEachDoc = new HashMap<>();


        //Step similar to processing the word
        for (String word : queryArray) {
            //Checks for stop words
            if (!STOP_WORDS_LIST.contains(word)) {
                String key = utils.getStemmedWord(word);
                // Check if term is present in our index., no point of processing otherwise
                if(indexData.getWordTfidfMap().containsKey(key)){
                    double idf = indexData.getWordTfidfMap().get(key).getIdfValue();
                    queryVector.put(key,idf);
                    magnitudeSquare+= idf * idf;
                }
            }
        }

        // Iterate through each term
         // and get the tfidf list of all documents,

        queryVector.forEach((String key, Double value) ->{
           ArrayList<Document> documentData = indexData.getWordTfidfMap().get(key).getDocumentsList();

           //For each documnet, multiply tfidf with idf of query and add to previous value,,
            // this is dot product operation Store the score for each doc in a map.
           documentData.forEach(document -> {
                String documentId = document.getDocumentId();
                if(sumProductValuesForEachDoc.containsKey(documentId)){
                    double currentSumProduct = sumProductValuesForEachDoc.get(documentId);
                    sumProductValuesForEachDoc.put(documentId,(currentSumProduct + (document.getTfIdf()*value)));
                }
                else {
                    sumProductValuesForEachDoc.put(documentId,document.getTfIdf()*value);
                }
            });
        });


        //Final Step of calculating score
         double finalMagnitudeSquare = magnitudeSquare;

        //No matching Documents.
         if(queryVector.size()==0)
         {
                 utils.writeToFile(queryObject.getQueryText()+DELIMITER+"No matching Documents..!!");
         }

         //Calculate and print the score
         sumProductValuesForEachDoc.forEach((String key, Double value) ->{
               double cosineScore = value / (Math.sqrt(finalMagnitudeSquare *indexData.getDocumentMagnitudeMap().get(key)));
               utils.writeToFile(queryObject.getQueryText()+DELIMITER+key+DELIMITER+cosineScore);


        });
    }
}
