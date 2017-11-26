package hari.griffith.assignment2.processor;

import hari.griffith.assignment2.constants.AppContstants;
import hari.griffith.assignment2.objects.Document;
import hari.griffith.assignment2.objects.DocumentProperties;
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

    public void processFiles(Path filePath) {
        try(BufferedReader bufferedReader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)){
            String line;
            boolean isFirstline = true;
            boolean isTitle = false;
            DocumentProperties documentProperties = null;
            StringBuffer titleBuilder = new StringBuffer();
            StringBuffer dataBuilder = new StringBuffer();
            while ((line = bufferedReader.readLine())!=null){

                if(line.trim().isEmpty()){ //End of title block
                    isTitle = false;
                }
                if(isTitle){
                    titleBuilder.append(line);
                    titleBuilder.append(" ");
                }
                if(!isTitle && !isFirstline ){
                    dataBuilder.append(line);
                    dataBuilder.append(" ");
                }

                if(isFirstline){
                    documentProperties = new DocumentProperties();
                    documentProperties.setDocumentNumber(parseAndGetDocumentNumber(line));
                    isTitle = true;
                    isFirstline = false;
                }
                if(line.startsWith("******") && line.endsWith("******")){
                    isTitle = false;
                    isFirstline = true;
                    documentProperties.setDocumentTitle(titleBuilder.toString());
                    titleBuilder.setLength(0);
                    String documentData = dataBuilder.toString();
                    dataBuilder.setLength(0);
                    processCurrentDocument(documentProperties,documentData);
                    //throw new IOException("test");
                }

                printMap();

            }
        } catch (IOException e) { //Read Failure.

        }
    }

    private void printMap() {
        tfidfMap.forEach((key,value)->{
            System.out.println(key);
            System.out.println(value);
        });
    }

    private void processCurrentDocument(DocumentProperties documentProperties, String documentData) {
        final Document[] document = new Document[1];
        HashMap<String,Float> wordsWithTermFrequencyMap = processTitle(documentProperties);
        String[] processedDataArray = utils.removeSpecialCharecters(documentData.trim()).split("\\s+");
        for(String word:processedDataArray){
            if(!AppContstants.STOPWORDLIST.contains(wordsWithTermFrequencyMap)){
                String key = utils.getStemmedWord(word);
                wordCountForGivenDoc++;
                if(wordsWithTermFrequencyMap.containsKey(key)){
                    wordsWithTermFrequencyMap.put(key,wordsWithTermFrequencyMap.get(key)+1);
                }
                else{
                    wordsWithTermFrequencyMap.put(key,1f);
                }
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
            return 0;
        }
    }





}
