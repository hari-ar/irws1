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

     //This is the main variable which holds the complete data.
     // Used hashmap as it returns at O(1). Inside is list of documents. which have their properties.
     // This one variable holds the complete tfidf matrix
    private HashMap<String,WordProperties> tfidfMap = new HashMap<>();


    private Utils utils = new Utils();
    private int wordCountForGivenDoc =0;
    private int documentCount = 0;

    // Method responsible for reading data from file and create inverted Index matrix and term frequencies for each file.
     void processFiles(Path filePath) {
        //Read the file path
        try(BufferedReader bufferedReader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)){
            String line; //Stores the current line info.

            //Flags
            boolean isFirstLine = true;
            boolean isTitle = false;

            //Other Objects
            DocumentProperties documentProperties;
            StringBuilder titleBuilder = new StringBuilder();
            StringBuilder dataBuilder = new StringBuilder();
            int documentNumber = documentCount;

            //Set exclusively for checking duplicate documents. This avoids reading document with same number and title twice.
            HashSet<String> checkForDuplicateDocuments = new HashSet<>();

            while ((line = bufferedReader.readLine())!=null){

                //Check if title block ends
                if(line.trim().isEmpty()){ //End of title block
                    isTitle = false;
                }
                //Check if the line belongs to title
                if(isTitle){
                    titleBuilder.append(line);
                    titleBuilder.append(" ");
                }
                //Check if its data
                if(!isTitle && !isFirstLine ){
                    dataBuilder.append(line);
                    dataBuilder.append(" ");
                }
                //Check if its first line, used to extract document number
                if(isFirstLine){
                    documentNumber = parseAndGetDocumentNumber(line);
                    isTitle = true;
                    isFirstLine = false;
                }
                //Check for document seperator.
                if(line.startsWith("******") && line.endsWith("******")){
                    isTitle = false;
                    isFirstLine = true;
                    String title = titleBuilder.toString().trim();
                    //Additional check to avoid duplicate documents.
                    if(!(titleBuilder.toString().trim().length()==0 || dataBuilder.toString().trim().length()==0)){
                        if(!checkForDuplicateDocuments.contains(documentNumber+title))
                        {
                            documentCount++;
                            //Set document title number in DocumentProperties class
                            documentProperties = new DocumentProperties();
                            documentProperties.setDocumentNumber(documentNumber);
                            documentProperties.setDocumentTitle(title);
                            dataBuilder.append(" ");
                            //Add title to data., No special treatment for title for now..!!
                            dataBuilder.append(title);
                            String documentData = dataBuilder.toString();
                            //Add to set which checks for duplicates..
                            checkForDuplicateDocuments.add(documentNumber+title);
                            //Process the document and build n_i, tf tables.
                            processCurrentDocument(documentProperties,documentData);
                        }
                    }

                    //Reset string builders
                    titleBuilder.setLength(0);
                    dataBuilder.setLength(0);
                }
            }
            //Debug .. can be commented
            System.out.println(documentCount);
        } catch (IOException e) { //Read Failure.
            e.printStackTrace();
        }

    }


    private void processCurrentDocument(DocumentProperties documentProperties, String documentData) {
        final Document[] document = new Document[1]; //Java 8 streams final variable
        wordCountForGivenDoc = 0; //This is word count per document
        //Calls method which populates the occurances count
        HashMap<String,Float> wordsWithTermFrequencyMap = processDocument(documentData); // Temporary Map storing the frequency or number of occurance of each term
        //documentProperties.setTotalNumberOfWords(wordCountForGivenDoc); //Set total number of words.., calculated in previous step

        //Processing begins.. checks if the key exists in map., if exists, adds doc to list else creates a new list
        wordsWithTermFrequencyMap.forEach((String key, Float termFrequency) ->{
            document[0] = new Document();
            document[0].setDocumentProperties(documentProperties); //Set doc properties
            document[0].setNormalisedTermFrequency(termFrequency/wordCountForGivenDoc); //Set TF - normalised.
            if(tfidfMap.containsKey(key)){
                tfidfMap.get(key).getDocumentsList().add(document[0]); //Adds document to exisitng list
            }
            else{
                WordProperties wordProperties = new WordProperties();
                ArrayList<Document> documentsList = new ArrayList<>(); //New Array list created to be added to map
                documentsList.add(document[0]);
                wordProperties.setDocumentsList(documentsList);
                tfidfMap.put(key,wordProperties);
            }
        });

        //Reset word count for next doc. Redundant but was done to fix a nasty bug.
        wordCountForGivenDoc=  0;
    }

     //Processing begins.. checks if the key exists in map.,
     // if exists, adds doc to list else creates a new list
     // This method creates occurrences matrix.
    private HashMap<String,Float> processDocument(String data) {
        //Remove special charecters
        String[] processedStringArray  = utils.removeSpecialCharecters(data).split("\\s+");

        //Temp map to hold occurances
        HashMap<String,Float> dataMap = new HashMap<>();

        for (String word:processedStringArray) {
            if (!STOP_WORDS_LIST.contains(word)) {
                String key = utils.getStemmedWord(word); //Get stemmed word
                wordCountForGivenDoc++;
                if(dataMap.containsKey(key)){ //Check if word already exists in matrix
                    dataMap.put(key,dataMap.get(key)+1f); // Increment count
                }
                else{ //Else put 1 as this is first occurrence.
                    dataMap.put(key,1f);
                }
            }
        }
        return dataMap; // Returns occurrence matrix
    }


    //Method to get the documnet number
    private  int parseAndGetDocumentNumber(String line) {
        Matcher matcher = LAST_INT_PATTERN.matcher(line);
        if (matcher.find()) {
            String documentNumberInString = matcher.group(1);
            return Integer.parseInt(documentNumberInString);
        }
        else //Corner case if document number is missing ..!!
        {
            return (documentCount+1)*1000; //Returns random number
        }
    }


    //Method where TF-IDF is calculated. This should be called after all the documents are read.
    void processMatrixAndSetTFIDFValues() throws IOException {
        final int documentCount = this.documentCount; //temp variable hold document count
        BufferedWriter invertedIndexWriter = utils.getFileWriter(INVERTED_INDEX_FILE_NAME);
        BufferedWriter tfIdfMatrixWriter = utils.getFileWriter(TFIDF_OUTPUT_FILE);
        DecimalFormat df = new DecimalFormat("#.0000"); //Format for just printing not storing.
        invertedIndexWriter.write("Word,n_i,idf"); //Header in csv
        invertedIndexWriter.newLine();

        //For each entry in the occurance matrix, calculate tfidf.,
        // which calculated tf-idf for all the search terms and store in map.
        // This is not required but built just that if
        // in case query is also added to same project, this object needs to be transferred.
        tfidfMap.forEach((String key, WordProperties value) ->{ //For each word
            double idfValue = Math.log10( ((double) documentCount)/value.getDocumentsList().size());

            value.setIdfValue(idfValue); // Set IDF for each word

            utils.writeToFile(key+","+value.getDocumentsList().size()+","+df.format(idfValue),invertedIndexWriter);

            //Processing for TF-IDF Values
            ArrayList<Document> documents = new ArrayList<>();

            //Each document associated to that word
            value.getDocumentsList().forEach(((Document document) -> {

                double tfidf = idfValue*document.getNormalisedTermFrequency(); //Calculate tf-idf for that doc
                document.setTfIdf(tfidf);
                documents.add(document);

                //Print out data to file
                utils.writeToFile(key+ DELIMITER +idfValue+ DELIMITER +tfidf+ DELIMITER +document.getDocumentProperties().getDocumentNumber()+ DELIMITER +document.getDocumentProperties().getDocumentTitle()+ DELIMITER,tfIdfMatrixWriter);
            }));
            value.setDocumentsList(documents); // Add modified documents back
        });

        //Close resources
        utils.closeWriter(invertedIndexWriter);
        utils.closeWriter(tfIdfMatrixWriter);
    }
}