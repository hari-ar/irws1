package hari.griffith.assignment.part2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;

import static hari.griffith.assignment.part2.AppConstants.DELIMITER;
import static hari.griffith.assignment.part2.AppConstants.INPUT_FILE_NAME;

 class PreQueryProcessor {

    public IndexData preProcess(){
        HashMap<String,WordProperties> wordTfidfMap = new HashMap<>();
        HashMap<String,Double> documentMagnitudeSquared = new HashMap<>();
        IndexData indexData = new IndexData();
        try(Stream<String> pathStream = Files.lines(Paths.get(INPUT_FILE_NAME))){
            pathStream.forEach((String line) ->{
                WordProperties wordProperties = null;
                Document document = new Document();;
                String[] lineArray = line.split(DELIMITER);
                String key = lineArray[0];
                double tfidfScore = Double.parseDouble(lineArray[1]);
                double idf = Double.parseDouble(lineArray[2]);
                String document_id = lineArray[3]+DELIMITER+lineArray[4];
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
