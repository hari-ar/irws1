package hari.griffith.assignment.part1;

import java.io.Serializable;

 class Document implements Serializable{
    private DocumentProperties documentProperties;
    private float normalisedTermFrequency;
    private double tfIdf;

    public double getTfIdf() {
        return tfIdf;
    }

    public void setTfIdf(double tfIdf) {
        this.tfIdf = tfIdf;
    }

    public float getNormalisedTermFrequency() {
        return normalisedTermFrequency;
    }

    public DocumentProperties getDocumentProperties() {
        return documentProperties;
    }

    public void setDocumentProperties(DocumentProperties documentProperties) {
        this.documentProperties = documentProperties;
    }

    public void setNormalisedTermFrequency(float normalisedTermFrequency) {
        this.normalisedTermFrequency = normalisedTermFrequency;

    }

    /*@Override
    public String toString() {
        StringBuffer builder = new StringBuffer();
        builder.append("Frequency is : ");
        builder.append(getNormalisedTermFrequency());
        builder.append("\n DocumentData is ");
        builder.append(getDocumentProperties().toString());
        return builder.toString();
    }*/
}
