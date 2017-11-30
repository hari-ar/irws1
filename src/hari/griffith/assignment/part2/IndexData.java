package hari.griffith.assignment.part2;

import java.util.HashMap;

/**
 *
 * This is the whole data required for query processing.
 * First map holds the tfidf values for each term in a list of documents
 *
 * Second map holds the magnitude of documents.Identified by document_id.
 *
 * **/

public class IndexData {
    HashMap<String,WordProperties> wordTfidfMap;
    HashMap<String,Double> documentMagnitudeMap;

    public HashMap<String, Double> getDocumentMagnitudeMap() {
        return documentMagnitudeMap;
    }

    public void setDocumentMagnitudeMap(HashMap<String, Double> documentMagnitudeMap) {
        this.documentMagnitudeMap = documentMagnitudeMap;
    }

    public HashMap<String, WordProperties> getWordTfidfMap() {

        return wordTfidfMap;
    }

    public void setWordTfidfMap(HashMap<String, WordProperties> wordTfidfMap) {
        this.wordTfidfMap = wordTfidfMap;
    }
}
