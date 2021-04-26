

/*package filemanager277;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import java.util.ArrayList;
import java.util.Arrays;


public class FileManagerFrame extends JInternalFrame {

    JSplitPane splitPane;
    JPanel panel;
    DirPanel dirPanel;
    FilePanel filePanel;
    private String frameTitle;
    static String currentSelected;
    static int lastSelectedRow;
    String thisCurrentDrive;
    App theApp;

    String drive;
    File[] paths;

    public void setFrameTitle(String ft) {
        frameTitle = ft;
        setTitle(frameTitle);
        this.repaint();
    }

    public FileManagerFrame(App theApp) throws IOException {
        this.theApp = theApp;
        filePanel = new FilePanel();
        dirPanel = new DirPanel(filePanel, this);
        dirPanel.setFilePanel(filePanel);
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, dirPanel, filePanel);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(250);
        splitPane.setSize(500,500);
        this.getContentPane().add(splitPane);

        setTitle(frameTitle);
        this.setVisible(true);
        this.setMaximizable(true);
        this.setClosable(true);
        this.setIconifiable(true);


    }

    public FileManagerFrame() {
        this.drive = "C:/shit";
        //this.drive = getDrives()[0];
        filePanel = new FilePanel();
        dirPanel = new DirPanel(drive, filePanel, this);
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new DirPanel(drive), new FilePanel());
        this.setTitle(drive);

        this.getContentPane().add(splitPane);
        this.setMaximizable(true);
        this.setClosable(true);
        this.setIconifiable(true);

        this.setSize(700, 500);
        this.setVisible(true);
    }

    public FileManagerFrame(String clickedDrive) {
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new DirPanel(clickedDrive), new FilePanel());
        this.setTitle(clickedDrive);

        this.getContentPane().add(splitPane);
        this.setMaximizable(true);
        this.setClosable(true);
        this.setIconifiable(true);

        this.setSize(700, 500);
        this.setVisible(true);
    }

    private class DirPanel extends JPanel {
        private JScrollPane scrollPane = new JScrollPane();
        private JTree dirTree;
        JSplitPane splitPane;

        DefaultTreeModel treemodel;
        FileManagerFrame myFrame;
        FilePanel filepanel;
        File rootFile;


        public DirPanel(String s, FilePanel fp, FileManagerFrame mf) {
            dirTree = new JTree();
            dirTree.setEditable(true);
            dirTree.addTreeSelectionListener(new filemanager277.FileManagerFrame.DirPanel.DemoTreeSelectionListener());
            rootFile = new File(s);
            buildTree();
            scrollPane.setViewportView(dirTree);
            this.add(scrollPane);

            filepanel = fp;
            myFrame = mf;
            rootFile = new File("C:/");
        }

        public DirPanel(String s) {
            dirTree = new JTree();
            dirTree.setEditable(true);
            dirTree.addTreeSelectionListener(new filemanager277.FileManagerFrame.DirPanel.DemoTreeSelectionListener());
            rootFile = new File(s);
            buildTree();
            scrollPane.setViewportView(dirTree);
            this.add(scrollPane);
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
                if (filelist == null) {
                    return;
                }
                for (File file : filelist) {
                    if (file.isDirectory()) {
                        MyFileNode node = new MyFileNode(file.getName(), file);
                        DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(node);
                        rootNode.add(treeNode);
                    }
                }
            }
        }


        private void expandTreeSelection(DefaultMutableTreeNode n, MyFileNode mfn) {
            if (n == null) {
                return;
            }

            File[] files = mfn.getFile().listFiles();
            if (files == null) {
                return;
            }
            for (File file : files) {
                if (file.isDirectory()) {
                    MyFileNode temp = new MyFileNode(file.getName(), file);
                    DefaultMutableTreeNode node = new DefaultMutableTreeNode(temp);

                    createfileNodes(node, file);
                    node.setAllowsChildren(true);
                    n.add(node);
                }
            }
        }

        public void setFilePanel(FilePanel filePanel) {

        }


        class DemoTreeSelectionListener implements TreeSelectionListener {

            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) dirTree.getLastSelectedPathComponent();
                MyFileNode mfn = (MyFileNode) node.getUserObject();
                expandTreeSelection(node, mfn);
                String[] test = {"test", "test2", "test3", "test4", "test5"};
                FilePanel newPanel = new FilePanel(test);

                System.out.println(dirTree.getMinSelectionRow());
                System.out.println(dirTree.getSelectionPath());
                System.out.println(Arrays.toString(mfn.getFileList()));
            }
        }
    }

    public class FilePanel extends JPanel {
        private JScrollPane scrollPane = new JScrollPane();
        String[] test = {"test", "test2", "test3"};
        DefaultListModel<String> model = new DefaultListModel<>();
        ArrayList<File> fileArrayList;
        File[] files;
        String[] test2;
        JList<String> jList1;


        private JList<File> fileList;
        private JList<String> testList;

        public FilePanel() {
            JList testList = new JList<>(test);
            scrollPane.setViewportView(testList);
            this.add(scrollPane);

        }

        public FilePanel(String[] f) {
            this.test = f;
            JList testList = new JList<>(test);
            scrollPane.setViewportView(testList);
            this.add(scrollPane);

        }



    public void buildList(String[] f) {

        for (String value : f) {
            model.addElement(value);
        }
        for (File file : f) {
            if (file.isFile()) {
                String fileStats = file.toString();
                model.addElement(fileStats);
                fileArrayList.add(file);
            }
        }
        jList1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        jList1 = new JList<>(f);
        scrollPane.setViewportView(jList1);
        this.add(scrollPane);
    }



}*/
