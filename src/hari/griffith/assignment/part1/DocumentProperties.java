package hari.griffith.assignment.part1;

import java.io.Serializable;

 class DocumentProperties implements Serializable{
    private String documentTitle;
    private int documentNumber;
    private int totalNumberOfWords;


    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public int getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(int documentNumber) {
        this.documentNumber = documentNumber;
    }


    public void setTotalNumberOfWords(int totalNumberOfWords) {
        this.totalNumberOfWords = totalNumberOfWords;
    }

   /* @Override
    public String toString() {
        StringBuffer builder = new StringBuffer();
        builder.append(getDocumentTitle());
        builder.append(",");
        builder.append(getDocumentNumber());
        builder.append(",");
        builder.append(getTotalNumberOfWords());
        return builder.toString();
    }*/
}
