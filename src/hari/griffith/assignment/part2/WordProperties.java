package hari.griffith.assignment.part2;

import java.util.ArrayList;

/**
 *
 * The POJO class which hold all the data related to a search term
 * Have a list of Documents
 * And idf values.
 *
 **/

class WordProperties {
    private ArrayList<Document> documentsList;
    private double idfValue;

    ArrayList<Document> getDocumentsList() {
        return documentsList;
    }

    void setDocumentsList(ArrayList<Document> documentsList) {
        this.documentsList = documentsList;
    }

    double getIdfValue() {
        return idfValue;
    }

    void setIdfValue(double idfValue) {
        this.idfValue = idfValue;
    }
}
