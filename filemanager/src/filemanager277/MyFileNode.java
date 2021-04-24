package filemanager277;

import java.io.File;

public class MyFileNode {
    File f;
    public MyFileNode(String filename) {
        f = new File(filename);
    }
    public MyFileNode(String n, File f) {
        this.f = f;
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
    public boolean isDirectory() {
        return f.isDirectory();
    }
}
