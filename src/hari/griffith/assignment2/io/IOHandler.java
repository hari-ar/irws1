package hari.griffith.assignment2.io;


import java.io.File;

public class IOHandler {

    public File getFile() {
        return file;
    }

    public void setFile(String folder) {
        this.file = new File(folder);
    }

    private File file;



}
