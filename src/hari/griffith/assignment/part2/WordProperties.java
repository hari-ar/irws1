package hari.griffith.assignment.part2;

import hari.griffith.assignment.part2.Document;

import java.util.ArrayList;

class WordProperties {
    private ArrayList<Document> documentsList;
    private double idfValue;

    public ArrayList<Document> getDocumentsList() {
        return documentsList;
    }

    public void setDocumentsList(ArrayList<Document> documentsList) {
        this.documentsList = documentsList;
    }

    public double getIdfValue() {
        return idfValue;
    }

    public void setIdfValue(double idfValue) {
        this.idfValue = idfValue;
    }
}
