package hari.griffith.assignment2.main;

import hari.griffith.assignment2.objects.Document;
import hari.griffith.assignment2.objects.TFIDFDocumentProperties;
import hari.griffith.assignment2.utils.Stemmer;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Indexer {

    private final static Pattern lastIntPattern = Pattern.compile("[^0-9]+([0-9]+)$");
    private static int documentNumber=0;
    private final static List<String> STOPWORDLIST = Arrays.asList("a","about","above","after","again","against","all","am","an","and","any","are","arent","as","at","be","because","been","before","being","below","between","both","but","by","cant","cannot","could","couldnt","did","didnt","do","does","doesnt","doing","dont","down","during","each","few","for","from","further","had","hadnt","has","hasnt","have","havent","having","he","hed","hell","hes","her","here","heres","hers","herself","him","himself","his","how","hows","i","id","ill","im","ive","if","in","into","is","isnt","it","its","its","itself","lets","me","more","most","mustnt","my","myself","no","nor","not","of","off","on","once","only","or","other","ought","our","ours","ourselves","out","over","own","same","shant","she","shed","shell","shes","should","shouldnt","so","some","such","than","that","thats","the","their","theirs","them","themselves","then","there","theres","these","they","theyd","theyll","theyre","theyve","this","those","through","to","too","under","until","up","very","was","wasnt","we","wed","well","were","weve","were","werent","what","whats","when","whens","where","wheres","which","while","who","whos","whom","why","whys","with","wont","would","wouldnt","you","youd","youll","youre","youve","your","yours","yourself","yourselves");
    private HashMap<String,Document> documentMap = new HashMap<>();
    private HashMap<String,TFIDFDocumentProperties> finalMatrix = new HashMap<>();

    public static void main(String[] args) {

        String path = args[0];
        System.out.println("Starting");
        Indexer indexer = new Indexer();
        try(Stream<Path> pathStream = Files.walk(Paths.get(path),FileVisitOption.FOLLOW_LINKS)){
            pathStream.filter(isValidTextFile()).forEach(filePath-> indexer.processFiles(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void processFiles(Path filePath) {
        boolean isTitle = false;
        try(BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            String line;
            Document document = null;
            String title = null;
            int lineCount = 0;
            int wordCountPerDocument =0;

            while ((line = reader.readLine()) != null) {
                String processedLine = line.replaceAll("[-]", " "); //Replacing hyphen with space.
                processedLine = processedLine.replaceAll("[^a-zA-Z0-9\\s]", ""); //Removing all special characters.
                processedLine = processedLine.toLowerCase();
                String[] wordsArray = processedLine.split("\\s+");

                lineCount++;
                    if(lineCount==1){ //Get Document Number
                        document = new Document();
                        documentNumber++;
                        document.setDocumentNumber(parseAndGetDocumentNumber(line));
                        isTitle = true;
                        title = "";
                    }
                    else{
                        if(isTitle){
                            title=title+line+" ";
                            for (String word:wordsArray) {
                                wordCountPerDocument+=2; //Increase 2 for each word in title to increase weight.
                                if(!STOPWORDLIST.contains(word)){
                                    String currentWord = getStemmedWord(word);
                                    checkAndAddWord(currentWord,true);
                                }
                            }
                            if(line.isEmpty()){ //Detect End of title.!!
                                //System.out.println("Empty String");
                                document.setDocumentTitle(title);
                                //System.out.println("Title is : "+document.getDocumentTitle() + " and doc number is "+document.getDocumentNumber());
                                title="";
                                isTitle = false;
                            }
                        }

                        else { //Else for isTitle
                            if(line.startsWith("******") && line.endsWith("******"))
                            {
                                lineCount = 0;
                                document.setTotalNumberOfWords(wordCountPerDocument);
                                wordCountPerDocument=0;
                                document.setWordCountMap(documentMap);
                                addDocumentToIndex(document);
                                //System.out.println("Title is : "+document.getDocumentTitle() + " and doc number is "+document.getDocumentNumber() + " and total words are "+document.getTotalNumberOfWords());

                            }
                            for (String word:wordsArray) {
                                wordCountPerDocument++;
                                if(!STOPWORDLIST.contains(word)){
                                    String currentWord = getStemmedWord(word);
                                    checkAndAddWord(currentWord,true);
                                }
                            }
                            //get Stemmed list of words
                            //Add title to title string buffer
                            // check if its present in map if not Add into tempDocMap with word as key., if preset set the count++
                        }
                    }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void addDocumentToIndex(Document document){
        documentMap.forEach((key,value)->{
            float normalisedTF = value/document.getTotalNumberOfWords();
            buildTfIDFDoc(key,document);

        });
    }

    private void buildTfIDFDoc(String key,Document document) {
        TFIDFDocumentProperties tfidfDocumentProperties = new TFIDFDocumentProperties();
        ArrayList<Document> documentsContainingThisWord = new ArrayList<>();
        if(finalMatrix.containsKey(key)){
            tfidfDocumentProperties = finalMatrix.get(key);
            documentsContainingThisWord = tfidfDocumentProperties.getDocumentsList();
            documentsContainingThisWord.add(document);
            tfidfDocumentProperties.setDocumentsList(documentsContainingThisWord);
            finalMatrix.put(key,tfidfDocumentProperties);
        }
        else{
            documentsContainingThisWord.add(document);
            tfidfDocumentProperties.setDocumentsList(documentsContainingThisWord);
            finalMatrix.put(key,tfidfDocumentProperties);
        }
    }

    private void printFinalMap(){

    }

    private void checkAndAddWord(String currentWord, boolean isTitle) {
        if(isTitle){
            if(checkIfKeyPresentInHashMap(currentWord)){
                documentMap.put(currentWord,documentMap.get(currentWord)+2);
            }else{
                documentMap.put(currentWord,2f);
            }
        }
        else{
            if(checkIfKeyPresentInHashMap(currentWord)){
                documentMap.put(currentWord,documentMap.get(currentWord)+1);
            }else{
                documentMap.put(currentWord,1f);
            }
        }
    }

    private  boolean checkIfKeyPresentInHashMap(String key){
        return documentMap.containsKey(key);
    }


    private  String getStemmedWord(String word){
        Stemmer stemmer =new Stemmer();
        stemmer.add(word.toCharArray(),word.length());
        stemmer.stem();
        return stemmer.toString();
    }

    private  int parseAndGetDocumentNumber(String line) {
        Matcher matcher = lastIntPattern.matcher(line);
        if (matcher.find()) {
            String documentNumberInString = matcher.group(1);
            return Integer.parseInt(documentNumberInString);
        }
        else //Corner case if document number is missing ..!!
        {
            return documentNumber;
        }
    }


    public static Predicate<Path> isValidTextFile() {
        return p -> !p.toFile().isDirectory() && p.toFile().getAbsolutePath().endsWith("txt");
    }




}
