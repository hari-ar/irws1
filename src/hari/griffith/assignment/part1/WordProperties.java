package hari.griffith.assignment.part1;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * The POJO class which hold all the data related to a search term
 * Have a list of Documents
 * And idf values.
 *
 **/


class WordProperties implements Serializable {
    private ArrayList<Document> documentsList;
    private double idfValue;




    //private int numberOfTimesOccuredInDocuments ;

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


    public int getNumberOfTimesOccuredInDocuments() {
        return documentsList.size();
    }

   /*
   *  @Override
        public String toString() {
        StringBuffer builder = new StringBuffer();
        builder.append("idf value is : ");
        builder.append(getIdfValue());
        for (Document document:getDocumentsList()) {
            builder.append(document);
        }
        return builder.toString();
    }*/

}
