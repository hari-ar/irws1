package hari.griffith.assignment.part2;

/***
 * Holds Query information.
 * Used to pass between methods,
 * just to avoid multiple parameters for the method.
 *
 * ***/

public class QueryObject {

    String queryText;

    public int getQueryNumber() {
        return queryNumber;
    }

    int queryNumber;



    public String getQueryText() {
        return queryText;
    }

    public void setQueryText(String queryText) {
        this.queryText = queryText;
    }

    public void setQueryNumber(int queryNumber) {
        this.queryNumber = queryNumber;
    }
}
