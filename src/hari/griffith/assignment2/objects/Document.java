package hari.griffith.assignment2.objects;

import java.util.HashMap;

public class Document {
    private int documentNumber;
    private String documentTitle;
    private int totalNumberOfWords;
    private int tf;

    public String getDocumentTitle() {
        return documentTitle;
    }

    public int getTf() {
        return tf;
    }

    public void setTf(int tf) {
        this.tf = tf;
    }

    public int getTotalNumberOfWords() {
        return totalNumberOfWords;
    }

    public void setTotalNumberOfWords(int totalNumberOfWords) {
        this.totalNumberOfWords = totalNumberOfWords;
    }

    public void setDocumentNumber(int documentNumber) {
        this.documentNumber = documentNumber;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }


    public int getDocumentNumber() {
        return documentNumber;
    }




    /*@Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Document title is :");
            stringBuilder.append(documentTitle);
            stringBuilder.append(" ");
            stringBuilder.append("Document number  is :");
            stringBuilder.append(documentNumber);
            wordCountMap.forEach((word,normalizedFrequency)->{
                stringBuilder.append(word);
                stringBuilder.append(" ");
                stringBuilder.append(normalizedFrequency);
                stringBuilder.append("\n");
            });

         return stringBuilder.toString();

    }*/
}
