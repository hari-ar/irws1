package hari.griffith.assignment.part2;


/**
 *
 * This class Holds Document related info like tf-idf, document_id which holds number and title delimited.
 * List of this object is stored to build the tfidf matrix
 *
 * **/


public class Document {
    private double tfIdf;
    private String documentId;

    public double getTfIdf() {
        return tfIdf;
    }

    public void setTfIdf(double tfIdf) {
        this.tfIdf = tfIdf;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
