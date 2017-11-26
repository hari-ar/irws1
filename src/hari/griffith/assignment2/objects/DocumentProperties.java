package hari.griffith.assignment2.objects;

public class DocumentProperties {
    private String documentTitle;
    private int documentNumber;
    private int totalNumberOfWords;

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public int getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(int documentNumber) {
        this.documentNumber = documentNumber;
    }

    public int getTotalNumberOfWords() {
        return totalNumberOfWords;
    }

    public void setTotalNumberOfWords(int totalNumberOfWords) {
        this.totalNumberOfWords = totalNumberOfWords;
    }

    @Override
    public String toString() {
        StringBuffer builder = new StringBuffer();
        builder.append("Name is : ");
        builder.append(getDocumentTitle());
        builder.append(" ");
        builder.append("Number is : ");
        builder.append(getDocumentNumber());
        builder.append(" ");
        builder.append("Number of words : ");
        builder.append(getTotalNumberOfWords());
        return builder.toString();
    }
}
