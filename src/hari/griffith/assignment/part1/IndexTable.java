package hari.griffith.assignment.part1;

import java.util.ArrayList;
import java.util.HashMap;

public class IndexTable {
    int totalNumberOfDocuments;
    HashMap<Integer,ArrayList<Double>> magnitudeOfTfids = new HashMap<>();
    HashMap<String,WordProperties> table;

    public void setTotalNumberOfDocuments(int totalNumberOfDocuments) {
        this.totalNumberOfDocuments = totalNumberOfDocuments;
    }

    public void setTable(HashMap<String, WordProperties> table) {
        this.table = table;
    }

    public HashMap<Integer,ArrayList<Double>> getMagnitudeOfTfids() {

        return magnitudeOfTfids;
    }

    public void setMagnitudeOfTfids(HashMap<Integer,ArrayList<Double>> magnitudeOfTfids) {
        this.magnitudeOfTfids = magnitudeOfTfids;
    }


    public int getTotalNumberOfDocuments() {
        return totalNumberOfDocuments;
    }

    public HashMap<String, WordProperties> getTable() {
        return table;
    }
}
