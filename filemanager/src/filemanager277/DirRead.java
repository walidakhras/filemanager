package filemanager277;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class DirRead {
    /*File file;
    String filename;

    public DirRead(String filename) {
        file = new File(filename);
    }

    public DirRead(String name, File f) {
        filename = name;
        file = f;
    }

    public File getFile() {
        return file;
    }

    public String toString() {
        if (file.getName().equals(""))
            return file.getPath();
        return file.getName();
    }

    public boolean isDirectory() {
        return file.isDirectory();
    }*/

    public static void main(String[] args) {


        File[] paths;

        paths = File.listRoots();

        for (File path: paths) {
            System.out.println(path);
        }

        //Executing files
        /*Desktop desktop = Desktop.getDesktop();
        try {
            desktop.open(new File("C:/shit/caca.txt"));
        } catch (IOException ex){
            System.out.println(ex.toString());
        }*/

        //Reading directories
        File file = new File("C:/shit");
        File[] files;
        files = file.listFiles();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        DecimalFormat dformat = new DecimalFormat("#,###");

        for (int i = 0; i < files.length; i++) {




            if (files[i].isDirectory())
                System.out.println("Directory: " + files[i].getAbsolutePath() + " Date: " + formatter.format(files[i].lastModified())
                + " Size: " + dformat.format(files[i].length()));
            else
                System.out.println("File: " + files[i].getAbsolutePath() + " Date: " + formatter.format(files[i].lastModified())
                        + " Size: " + dformat.format(files[i].length()));

        }
    }


}
