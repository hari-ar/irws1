package hari.griffith.assignment.part1;


import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 *
 * Holds Constants. Name of variable says it all..!!
 *
 **/


 interface AppConstants {
    Pattern LAST_INT_PATTERN = Pattern.compile("[^0-9]+([0-9]+)$");
    List<String> STOP_WORDS_LIST = Arrays.asList("","a","about","above","after","again","against","all","am","an","and","any","are","arent","as","at","be","because","been","before","being","below","between","both","but","by","cant","cannot","could","couldnt","did","didnt","do","does","doesnt","doing","dont","down","during","each","few","for","from","further","had","hadnt","has","hasnt","have","havent","having","he","hed","hell","hes","her","here","heres","hers","herself","him","himself","his","how","hows","i","id","ill","im","ive","if","in","into","is","isnt","it","its","its","itself","lets","me","more","most","mustnt","my","myself","no","nor","not","of","off","on","once","only","or","other","ought","our","ours","ourselves","out","over","own","same","shant","she","shed","shell","shes","should","shouldnt","so","some","such","than","that","thats","the","their","theirs","them","themselves","then","there","theres","these","they","theyd","theyll","theyre","theyve","this","those","through","to","too","under","until","up","very","was","wasnt","we","wed","well","were","weve","were","werent","what","whats","when","whens","where","wheres","which","while","who","whos","whom","why","whys","with","wont","would","wouldnt","you","youd","youll","youre","youve","your","yours","yourself","yourselves");
    String WORKING_DIRECTORY = System.getProperty("user.dir");
    String INPUT_DIRECTORY = WORKING_DIRECTORY +"/input/";
    String OUTPUT_DIRECTORY = WORKING_DIRECTORY +"/output/";
    String DELIMITER = "\t";
    String TFIDF_OUTPUT_FILE = "tfIdf.txt";
    String INVERTED_INDEX_FILE_NAME = "invertedIndex.txt";

}
