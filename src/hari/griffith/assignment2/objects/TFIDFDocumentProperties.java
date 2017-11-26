package hari.griffith.assignment2.objects;

import java.util.ArrayList;

public class TFIDFDocumentProperties {
    private ArrayList<Document> documentsList;
    private float idfValue;
    private float tf;

    //private int numberOfTimesOccuredInDocuments ;

    public ArrayList<Document> getDocumentsList() {
        return documentsList;
    }

    public void setDocumentsList(ArrayList<Document> documentsList) {
        this.documentsList = documentsList;
    }

    public float getIdfValue() {
        return idfValue;
    }

    public void setIdfValue(float idfValue) {
        this.idfValue = idfValue;
    }


    public int getNumberOfTimesOccuredInDocuments() {
        return documentsList.size();
    }


}
