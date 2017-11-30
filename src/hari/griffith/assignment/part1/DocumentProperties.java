package hari.griffith.assignment.part1;

import java.io.Serializable;

/**
 *
 * This class holds the document title, number
 *
 * This is created to avoid storing multiple strings of same info when one object can do., this helps in saving space.
 *
 * As only one instance per document is created. instead of terms * documents.
 *
 * **/


class DocumentProperties implements Serializable{
    private String documentTitle;
    private int documentNumber;

     String getDocumentTitle() {
        return documentTitle;
    }

     void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    int getDocumentNumber() {
        return documentNumber;
    }

     void setDocumentNumber(int documentNumber) {
        this.documentNumber = documentNumber;
    }




   /* @Override //Used this for testing.
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
