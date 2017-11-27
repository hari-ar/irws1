package hari.griffith.assignment2.processor;

import hari.griffith.assignment2.constants.AppContstants;
import hari.griffith.assignment2.objects.Document;
import hari.griffith.assignment2.objects.DocumentProperties;
import hari.griffith.assignment2.objects.IndexTable;
import hari.griffith.assignment2.objects.WordProperties;
import hari.griffith.assignment2.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;


public class Indexer {

    private HashMap<String,WordProperties> tfidfMap = new HashMap<>();
    private Utils utils = new Utils();
    private int wordCountForGivenDoc =0;
    private int documentCount = 0;
    private IndexTable indexTable;

    public void processFiles(Path filePath) {
        try(BufferedReader bufferedReader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)){
            String line;
            boolean isFirstLine = true;
            boolean isTitle = false;
            DocumentProperties documentProperties = null;
            StringBuilder titleBuilder = new StringBuilder();
            StringBuilder dataBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine())!=null){

                if(line.trim().isEmpty()){ //End of title block
                    isTitle = false;
                }
                if(isTitle){ //Add title to buffer to process it later
                    titleBuilder.append(line);
                    titleBuilder.append(" ");
                }
                if(!isTitle && !isFirstLine ){
                    dataBuilder.append(line);
                    dataBuilder.append(" ");

                }

                if(isFirstLine){

                    documentProperties = new DocumentProperties();
                    documentProperties.setDocumentNumber(parseAndGetDocumentNumber(line));
                    isTitle = true;
                    isFirstLine = false;
                }
                if(line.startsWith("******") && line.endsWith("******")){
                    documentCount++;
                    isTitle = false;
                    isFirstLine = true;
                    documentProperties.setDocumentTitle(titleBuilder.toString());
                    titleBuilder.setLength(0);
                    String documentData = dataBuilder.toString();
                    dataBuilder.setLength(0);
                    processCurrentDocument(documentProperties,documentData);
                    //throw new IOException("test");
                }
            }
        } catch (IOException e) { //Read Failure.
            e.printStackTrace();
        }

    }

    /*private void printMap() {
        tfidfMap.forEach((key,value)->{
            value.getDocumentsList().forEach(item->{
                String title = item.getDocumentProperties().getDocumentTitle();
                System.out.println(title);
            });
        });
    }*/

    private void processCurrentDocument(DocumentProperties documentProperties, String documentData) {
        final Document[] document = new Document[1];
        HashMap<String,Float> wordsWithTermFrequencyMap = processTitle(documentProperties);
        String[] processedDataArray = utils.removeSpecialCharecters(documentData.trim()).split("\\s+");
        for(String word:processedDataArray){
            if (AppContstants.STOPWORDLIST.contains(word)) {
                continue;
            }
            String key = utils.getStemmedWord(word);
            wordCountForGivenDoc++;
            if(wordsWithTermFrequencyMap.containsKey(key)){
                wordsWithTermFrequencyMap.put(key,wordsWithTermFrequencyMap.get(key)+1);
            }
            else{
                wordsWithTermFrequencyMap.put(key,1f);
            }
        }
        documentProperties.setTotalNumberOfWords(wordCountForGivenDoc);

        wordsWithTermFrequencyMap.forEach((key,termFrequency)->{
            document[0] = new Document();
            document[0].setDocumentProperties(documentProperties);
            document[0].setNormalisedTermFrequency(termFrequency/wordCountForGivenDoc);
            if(tfidfMap.containsKey(key)){
                WordProperties wordProperties = tfidfMap.get(key);
                ArrayList<Document> documentsList = wordProperties.getDocumentsList();
                documentsList.add(document[0]);
                wordProperties.setDocumentsList(documentsList);
                tfidfMap.put(key,wordProperties);
            }
            else{
                WordProperties wordProperties = new WordProperties();
                ArrayList<Document> documentsList = new ArrayList<>();
                documentsList.add(document[0]);
                wordProperties.setDocumentsList(documentsList);
                tfidfMap.put(key,wordProperties);
            }
        });
        //System.out.println(documentProperties);
        wordCountForGivenDoc=  0;

    }

    private HashMap<String,Float> processTitle(DocumentProperties documentProperties) {
        String[] processedStringArray  = utils.removeSpecialCharecters(documentProperties.getDocumentTitle()).split("\\s+");
        HashMap<String,Float> titleMap = new HashMap<>();
        for (String word:processedStringArray) {
            if(!AppContstants.STOPWORDLIST.contains(word)){
                String key = utils.getStemmedWord(word);
                wordCountForGivenDoc+=2;
                if(titleMap.containsKey(key)){
                    titleMap.put(key,titleMap.get(key)+2);
                }
                else{
                    titleMap.put(key,2f);
                }
            }
        }
        return titleMap;
    }






    private  int parseAndGetDocumentNumber(String line) {
        Matcher matcher = AppContstants.lastIntPattern.matcher(line);
        if (matcher.find()) {
            String documentNumberInString = matcher.group(1);
            return Integer.parseInt(documentNumberInString);
        }
        else //Corner case if document number is missing ..!!
        {
            return documentCount+1;
        }
    }


    public IndexTable processMatrix() {
        int documentCount = this.documentCount;
        HashMap<Integer,ArrayList<Double>> magnitudeOfTfidfs = new HashMap<>();
        IndexTable indexTable = new IndexTable();
        tfidfMap.forEach((key,value)->{
            WordProperties newwordProperties = value;
            System.out.println("Key is "+key);

            double idfValue = Math.log( ((double) documentCount)/value.getDocumentsList().size());
            System.out.println("idf  "+idfValue+" Which is "+documentCount+" / " + value.getDocumentsList().size());

            newwordProperties.setIdfValue(idfValue); // Set IDF for each word
            ArrayList<Document> documents = new ArrayList<>();
            value.getDocumentsList().forEach(((Document document) -> {
                double tfidf = idfValue*document.getNormalisedTermFrequency();
                document.setTfIdf(tfidf);
                documents.add(document);
                int documentNumber = document.getDocumentProperties().getDocumentNumber();
                if(magnitudeOfTfidfs.containsKey(documentNumber)){
                    ArrayList<Double> listOfTfIdfs = magnitudeOfTfidfs.get(documentNumber);
                    listOfTfIdfs.add(tfidf);
                    magnitudeOfTfidfs.put(documentNumber,listOfTfIdfs);
                }
                else {
                    ArrayList<Double> listOfTfIdfs = new ArrayList<>();
                    listOfTfIdfs.add(tfidf);
                    magnitudeOfTfidfs.put(documentNumber,listOfTfIdfs);
                }
            }));
            newwordProperties.setDocumentsList(documents);
            tfidfMap.put(key,newwordProperties);
        });
        indexTable.setTable(tfidfMap);
        indexTable.setTotalNumberOfDocuments(documentCount);
        indexTable.setMagnitudeOfTfids(magnitudeOfTfidfs);
        return indexTable;
    }
}
