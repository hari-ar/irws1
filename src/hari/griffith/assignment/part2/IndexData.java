package hari.griffith.assignment.part2;

import java.util.HashMap;

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
