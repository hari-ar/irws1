package hari.griffith.assignment.part2;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static hari.griffith.assignment.part2.AppConstants.DELIMITER;
import static hari.griffith.assignment.part2.AppConstants.STOP_WORDS_LIST;

 class QueryProcessor {

     void processQuery(IndexData indexData, QueryObject queryObject) {
        Utils utils = new Utils();
        HashMap<String,Double> queryVector = new HashMap<>();
         double magnitudeSquare = 0;
        String[] queryArray = utils.removeSpecialCharecters(queryObject.getQueryText().trim()).split("\\s+");
        HashMap<String, Double> sumProductValuesForEachDoc = new HashMap<>();


        for (String word : queryArray) {
            if (!STOP_WORDS_LIST.contains(word)) {
                String key = utils.getStemmedWord(word);
                double idf = indexData.getWordTfidfMap().get(key).getIdfValue();
                queryVector.put(key,idf);
                magnitudeSquare+= idf * idf;
            }
        }

        queryVector.forEach((String key, Double value) ->{
           ArrayList<Document> documentData = indexData.getWordTfidfMap().get(key).getDocumentsList();
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
         BufferedWriter fileWriter = null;
         try {
             fileWriter = utils.getFileWriter("QueryResults.txt");
         } catch (IOException e) {
             e.printStackTrace();
         }
         BufferedWriter finalFileWriter = fileWriter;

         sumProductValuesForEachDoc.forEach((String key, Double value) ->{
            try {

               double cosineScore = value / (Math.sqrt(finalMagnitudeSquare *indexData.getDocumentMagnitudeMap().get(key)));
               finalFileWriter.write(queryObject.getQueryText()+DELIMITER+key+DELIMITER+cosineScore);
               finalFileWriter.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
         //utils.closeWriter(fileWriter);

    }
}
