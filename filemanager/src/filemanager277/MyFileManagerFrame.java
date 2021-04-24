package filemanager277;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class MyFileManagerFrame extends JInternalFrame {

    JSplitPane splitPane;
    DirPanel dirPanel;
    FilePanel filePanel;
    String drive;
    File[] paths;
    public boolean showDetails = true;
    public File[] currentFileArray;

    public MyFileManagerFrame() {
        //this.drive = "C:/shit";
        //this.drive = "C:/";
        this.drive = getDrives()[0];
        filePanel = new FilePanel();
        dirPanel = new DirPanel();
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, dirPanel, filePanel);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(350);
        this.setTitle(drive);
        this.getContentPane().add(splitPane);
        this.setMaximizable(true);
        this.setClosable(true);
        this.setIconifiable(true);
        this.setSize(800, 600);
        this.setVisible(true);
    }

    public MyFileManagerFrame(String clickedDrive) {
        this.drive = clickedDrive;
        filePanel = new FilePanel();
        dirPanel = new DirPanel();
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, dirPanel, filePanel);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(350);
        this.setTitle(drive);

        this.getContentPane().add(splitPane);
        this.setMaximizable(true);
        this.setClosable(true);
        this.setIconifiable(true);
        this.setSize(800, 600);
        this.setVisible(true);
    }

    public void changeFilePanel(File[] fileList) {
        this.currentFileArray = fileList;
        FilePanel newPanel = new FilePanel(fileList);
        filePanel.removeAll();
        filePanel.repaint();
        filePanel.revalidate();

        filePanel.add(newPanel);
        filePanel.repaint();
        filePanel.revalidate();
    }

    public String[] getDrives() {
        paths = File.listRoots();
        String[] names = new String[paths.length];

        for (int i = 0; i < paths.length; i++) {
            names[i] = paths[i].getPath();
        }

        return names;
    }

    public void hideDetailsWhenClicked() {
        FilePanel newPanel = new FilePanel(currentFileArray);
        newPanel.getFilesArrayNoDetails(currentFileArray);
        filePanel.removeAll();
        filePanel.repaint();
        filePanel.revalidate();

        filePanel.add(newPanel);
        filePanel.repaint();
        filePanel.revalidate();
    }


    private class DirPanel extends JPanel {

        private JScrollPane dirPanelScrollPane = new JScrollPane();
        private JTree dirTree;
        DefaultTreeModel treemodel;
        File rootFile;

        public DirPanel() {
            dirTree = new JTree();
            dirTree.setEditable(true);
            dirTree.addTreeSelectionListener(new MyFileManagerFrame.DirPanel.DemoTreeSelectionListener());
            rootFile = new File(drive);
            buildTree();
            dirPanelScrollPane.setViewportView(dirTree);
            this.add(dirPanelScrollPane);
        }

        private void buildTree() {
            MyFileNode root = new MyFileNode(rootFile.getPath(), rootFile);
            DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(root);
            treemodel = new DefaultTreeModel(rootNode);
            dirTree.setRootVisible(true);
            createfileNodes(rootNode, rootFile);
            dirTree.setModel(treemodel);
        }

        private void createfileNodes (DefaultMutableTreeNode rootNode, File directory) {
            File[] filelist;
            if (directory.isDirectory()) {
                filelist = directory.listFiles();
                if (filelist == null) return;
                for (File file : filelist) {
                    if (file.isDirectory()) {
                        MyFileNode node = new MyFileNode(file.getName(), file);
                        DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(node);
                        rootNode.add(treeNode); } } } }

        private void expandTreeSelection(DefaultMutableTreeNode n, MyFileNode mfn) {
            if (n == null) return;
            File[] files = mfn.getFile().listFiles();
            if (files == null) return;
            for (File file : files) {
                if (file.isDirectory()) {
                    MyFileNode temp = new MyFileNode(file.getName(), file);
                    DefaultMutableTreeNode node = new DefaultMutableTreeNode(temp);

                    createfileNodes(node, file);
                    node.setAllowsChildren(true);
                    n.add(node); } } }

        private ArrayList<String> getSizeAndDate(File[] files) {
            assert files != null;
            ArrayList<String> fileAndSize = new ArrayList<String>();
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            DecimalFormat dformat = new DecimalFormat("#,###");


            for (File file : files) {
                if (file.isDirectory()) {
                    fileAndSize.add(file.getName() + " " + formatter.format(file.lastModified()) + " " + dformat.format(file.length()));
                }
            }
            return fileAndSize;
        }

        private void recurseNodes() {}

        class DemoTreeSelectionListener implements TreeSelectionListener {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) dirTree.getLastSelectedPathComponent();
                MyFileNode mfn = (MyFileNode) node.getUserObject();
                expandTreeSelection(node, mfn);
                System.out.println(Arrays.toString(mfn.getFileList()));
                System.out.println(getSizeAndDate(mfn.getFileList()));
                changeFilePanel(mfn.getFileList());
            }}
    }





    public class FilePanel extends JPanel {
        private JScrollPane filePanelScrollPane = new JScrollPane();
        JList<File> jList1;
        JList<String> stringList;
        DefaultListModel<String> model = new DefaultListModel<>();
        ArrayList<String> filePath = new ArrayList<>();
        File[] currentFileArray;
        MyFileManagerFrame myfm;



        public FilePanel() {
            this.jList1 = new JList<>();
            filePanelScrollPane.setViewportView(jList1);
            this.add(filePanelScrollPane);
        }

        /*public FilePanel(File[] f) {
            if (f == null) return;
            this.jList1 = new JList<>(f);
            filePanelScrollPane.setViewportView(jList1);
            this.add(filePanelScrollPane);
        }*/

        public FilePanel(File[] files) {
            clickAction(files);
            filePanelScrollPane.setViewportView(stringList);
            this.add(filePanelScrollPane);
        }

        public void clickAction(File[] files) {
            //this.currentFileArray = files;
            ArrayList<String> filesArray = getFilesArray(files);
            String[] ffiles = filesArray.toArray(new String[filesArray.size()]);
            String[] fileArrayPath = filePath.toArray(new String[filePath.size()]);
            stringList = new JList(filesArray.toArray());

                stringList.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent evt) {
                        JList stringList = (JList)evt.getSource();
                        if (evt.getClickCount() == 2) {
                            int index = stringList.locationToIndex(evt.getPoint());
                            Desktop desktop = Desktop.getDesktop();
                            try {
                                desktop.open(new File(fileArrayPath[index]));
                            } catch (IOException ex) {
                                System.out.println(ex.toString());
                            }
                        }
                    }
                });
            }


        public String getSizeAndDate2(File file) {
            String filex = "";
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            DecimalFormat dformat = new DecimalFormat("#,###");

            if (file.isFile()) {
                if (!showDetails) {
                    filex += file.getName();
                }
                else {
                    filex += file.getName() + " " + formatter.format(file.lastModified()) + " " + dformat.format(file.length());
                }
                model.addElement(filex);}
            return filex; }



        public ArrayList<String> getFilesArray(File[] file) {
            if (file == null) return null;
            ArrayList<String> files = new ArrayList<>();
            for (File value : file) {
                //files.add(value.getAbsolutePath());
                filePath.add(value.getAbsolutePath());
                files.add(getSizeAndDate2(value));
            }
            return files;
        }

        public ArrayList<String> getFilesArrayNoDetails(File[] file) {
            if (file == null) return null;
            ArrayList<String> files = new ArrayList<>();
            for (File value : file) {
                //files.add(value.getAbsolutePath());
                filePath.add(value.getAbsolutePath());
            }
            return files;
        }




    }

}