package hari.griffith.assignment.part2;

/**
 *
 * This class holds the score of document and title
 * this implements Comparable as we need to provide
 * the Treeset the info about the order.
 *
 * **/

public class QueryResultObject implements Comparable<QueryResultObject> {

    private double score;
    private String documentIdentifier;

    public QueryResultObject(double score, String documnetIdentifier) {
        this.score = score;
        this.documentIdentifier = documnetIdentifier;
    }

    public double getScore() {
        return score;
    }

    public String getDocumentIdentifier() {
        return documentIdentifier;
    }

    @Override
    public int compareTo(QueryResultObject o) {
        if(this.score<o.score)
        {
            return +1;
        }
        else if ((this.score>o.score))
        {
            return -1;
        }

        //Handles corner case where two documents have same score.
        //If they have same score, the document is sorted based on document number.

        String[] thisArray = this.documentIdentifier.split(AppConstants.DELIMITER);
        double thisDocNumber = Double.parseDouble(thisArray[0]);
        String[] oArray = o.documentIdentifier.split(AppConstants.DELIMITER);
        double oDocNumber = Double.parseDouble(oArray[0]);

        if(thisDocNumber<=oDocNumber){
            return +1;
        }
        return -1;
    }
}
