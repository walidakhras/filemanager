package filemanager277;

import java.io.File;

public class MyFileNode {
    File f;
    String fileName;

    public MyFileNode(String filename) {
        f = new File(filename);
    }
    public MyFileNode(String n, File f) {
        this.f = f;
        this.fileName = n;
    }
    public File getFile() {
        return f;
    }
    public File[] getFileList() {
        return f.listFiles(); }
    public String toString() {
        if(f.getName().equals("")) return f.getPath();
        return f.getName();
    }

    public String mfngetAbsolutePath() {
        return f.getAbsolutePath();
    }
    public boolean isDirectory() {
        return f.isDirectory();
    }
}