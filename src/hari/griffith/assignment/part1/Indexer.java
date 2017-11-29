package hari.griffith.assignment.part1;


import java.io.BufferedReader;
import java.io.BufferedWriter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;

import static hari.griffith.assignment.part1.AppConstants.*;


 class Indexer {

    private HashMap<String,WordProperties> tfidfMap = new HashMap<>();
    private Utils utils = new Utils();
    private int wordCountForGivenDoc =0;
    private int documentCount = 0;


    public void processFiles(Path filePath) {
        try(BufferedReader bufferedReader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)){
            String line;
            boolean isFirstLine = true;
            boolean isTitle = false;
            DocumentProperties documentProperties = null;
            StringBuilder titleBuilder = new StringBuilder();
            StringBuilder dataBuilder = new StringBuilder();
            HashSet<String> checkForDuplicateDocuments = new HashSet<>();
            int documentNumber = documentCount;
            while ((line = bufferedReader.readLine())!=null){

                if(line.trim().isEmpty()){ //End of title block
                    isTitle = false;
                }
                if(isTitle){
                    titleBuilder.append(line);
                    titleBuilder.append(" ");
                }
                if(!isTitle && !isFirstLine ){
                    dataBuilder.append(line);
                    dataBuilder.append(" ");
                }
                if(isFirstLine){
                    documentNumber = parseAndGetDocumentNumber(line);
                    isTitle = true;
                    isFirstLine = false;
                }
                if(line.startsWith("******") && line.endsWith("******")){
                    isTitle = false;
                    isFirstLine = true;
                    String title = titleBuilder.toString().trim();


                    if(!checkForDuplicateDocuments.contains(documentNumber+title))
                    {
                        documentCount++;
                        documentProperties = new DocumentProperties();
                        documentProperties.setDocumentNumber(documentNumber);
                        documentProperties.setDocumentTitle(title);
                        dataBuilder.append(" ");
                        dataBuilder.append(title);
                        String documentData = dataBuilder.toString();
                        checkForDuplicateDocuments.add(documentNumber+title);
                        processCurrentDocument(documentProperties,documentData);
                    }
                    //Reset Buffers
                    titleBuilder.setLength(0);
                    dataBuilder.setLength(0);
                    //throw new IOException("test");
                }
            }
            System.out.println(documentCount);
        } catch (IOException e) { //Read Failure.
            e.printStackTrace();
        }

    }


    private void processCurrentDocument(DocumentProperties documentProperties, String documentData) {
        final Document[] document = new Document[1];
        HashMap<String,Float> wordsWithTermFrequencyMap = processDocument(documentData);
        String[] processedDataArray = utils.removeSpecialCharecters(documentData.trim()).split("\\s+");
        for(String word:processedDataArray){
            if (STOP_WORDS_LIST.contains(word)) {
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

        wordsWithTermFrequencyMap.forEach((String key, Float termFrequency) ->{
            document[0] = new Document();
            document[0].setDocumentProperties(documentProperties);
            document[0].setNormalisedTermFrequency(termFrequency/wordCountForGivenDoc);
            if(tfidfMap.containsKey(key)){
                tfidfMap.get(key).getDocumentsList().add(document[0]);
            }
            else{
                WordProperties wordProperties = new WordProperties();
                ArrayList<Document> documentsList = new ArrayList<>();
                documentsList.add(document[0]);
                wordProperties.setDocumentsList(documentsList);
                tfidfMap.put(key,wordProperties);
            }
        });
        wordCountForGivenDoc=  0;
    }


    private HashMap<String,Float> processDocument(String data) {
        String[] processedStringArray  = utils.removeSpecialCharecters(data).split("\\s+");
        HashMap<String,Float> dataMap = new HashMap<>();
        for (String word:processedStringArray) {
            if(STOP_WORDS_LIST.contains(word)){
                continue;
            }
            else{
                String key = utils.getStemmedWord(word);
                wordCountForGivenDoc++;
                if(dataMap.containsKey(key)){
                    dataMap.put(key,dataMap.get(key)+1f);
                }
                else{
                    dataMap.put(key,1f);
                }
            }
        }
        return dataMap;
    }





    private  int parseAndGetDocumentNumber(String line) {
        Matcher matcher = LAST_INT_PATTERN.matcher(line);
        if (matcher.find()) {
            String documentNumberInString = matcher.group(1);
            return Integer.parseInt(documentNumberInString);
        }
        else //Corner case if document number is missing ..!!
        {
            return (documentCount+1)*1000;
        }
    }


    public void processMatrixAndSetTFIDFValues() throws IOException {
        int documentCount = this.documentCount;
        HashMap<Integer,ArrayList<Double>> magnitudeOfTfidfs = new HashMap<>();
        IndexTable indexTable = new IndexTable();
        BufferedWriter invertedIndexWriter = utils.getFileWriter(INVERTED_INDEX_FILE_NAME);
        BufferedWriter tfIdfMatrixWriter = utils.getFileWriter(TFIDF_OUTPUT_FILE);
        DecimalFormat df = new DecimalFormat("#.0000");
        invertedIndexWriter.write("Word,n_i,idf");
        invertedIndexWriter.newLine();
        //tfIdfMatrixWriter.write("Word,IDF,TFIDF,Document Number, DocumentTitle");
        //tfIdfMatrixWriter.newLine();

        tfidfMap.forEach((String key, WordProperties value) ->{
            double idfValue = Math.log( ((double) documentCount)/value.getDocumentsList().size());

            value.setIdfValue(idfValue); // Set IDF for each word
            try {
                invertedIndexWriter.write(key+","+value.getDocumentsList().size()+","+df.format(idfValue));
                invertedIndexWriter.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Processing for TF-IDF Values
            ArrayList<Document> documents = new ArrayList<>();
            value.getDocumentsList().forEach(((Document document) -> {
                double tfidf = idfValue*document.getNormalisedTermFrequency();
                document.setTfIdf(tfidf);
                documents.add(document);
                try
                {
                    tfIdfMatrixWriter.write(key+ DELIMITER +idfValue+ DELIMITER +tfidf+ DELIMITER +document.getDocumentProperties().getDocumentNumber()+ DELIMITER +document.getDocumentProperties().getDocumentTitle()+ DELIMITER);
                    tfIdfMatrixWriter.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));
            value.setDocumentsList(documents);
        });
        indexTable.setTable(tfidfMap);
        utils.closeWriter(invertedIndexWriter);
    }
}
