package filemanager277;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;



public class MyFileManagerFrame extends JInternalFrame {

    JSplitPane splitPane;
    DirPanel dirPanel;
    FilePanel filePanel;
    String drive;
    App test;
    public boolean showDetails = true;
    private File[] currentFileArray;
    private String dirPanelCurrentDirectory;
    private String chosenFile;


    public MyFileManagerFrame(String clickedDrive, App a) {
        this.test = a;
        this.drive = clickedDrive;
        filePanel = new FilePanel();
        dirPanel = new DirPanel();
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, dirPanel, filePanel);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(350);
        this.addInternalFrameListener(new MyFileManagerFrameFocusListener());
        this.setTitle(drive);
        this.getContentPane().add(splitPane);
        this.setMaximizable(true);
        this.setClosable(true);
        this.setIconifiable(true);
        this.setSize(800, 600);
        this.setVisible(true);
    }
    public void changeFilePanel() {
        FilePanel newPanel = new FilePanel();
        filePanel.removeAll();
        filePanel.repaint();
        filePanel.revalidate();

        filePanel.add(newPanel);
        filePanel.repaint();
        filePanel.revalidate();
    }

    public void updateTitle(String title) {
        this.setTitle(title);
        this.repaint();
        this.revalidate();
    }


    public void hideDetailsWhenClicked() {
        FilePanel newPanel = new FilePanel();
        newPanel.getFilesArrayNoDetails(currentFileArray);
        filePanel.removeAll();
        filePanel.repaint();
        filePanel.revalidate();

        filePanel.add(newPanel);
        filePanel.repaint();
        filePanel.revalidate();
    }

    public File[] getCurrentFileArray(){
        return currentFileArray;
    }

    public void setCurrentFileArray(File[] f) {
        this.currentFileArray = f;
    }

    public String getDirPanelCurrentDirectory() { return dirPanelCurrentDirectory; }

    public void setDirPanelCurrentDirectory(String s) { this.dirPanelCurrentDirectory = s; }

    public String getChosenFile() { return chosenFile; }

    public void setChosenFile(String s) { this.chosenFile = s; }


    class MyFileManagerFrameFocusListener implements InternalFrameListener {

        @Override
        public void internalFrameOpened(InternalFrameEvent e) {

        }

        @Override
        public void internalFrameClosing(InternalFrameEvent e) {

        }

        @Override
        public void internalFrameClosed(InternalFrameEvent e) {

        }

        @Override
        public void internalFrameIconified(InternalFrameEvent e) {

        }

        @Override
        public void internalFrameDeiconified(InternalFrameEvent e) {

        }

        @Override
        public void internalFrameActivated(InternalFrameEvent e) {
            test.updateStatusBar();
        }

        @Override
        public void internalFrameDeactivated(InternalFrameEvent e) {

        }
    }


    public class DirPanel extends JPanel {

        private JScrollPane dirPanelScrollPane = new JScrollPane();
        private JTree dirTree;
        DefaultTreeModel treemodel;
        File rootFile;

        public DirPanel() {
            dirTree = new JTree();
            dirTree.setEditable(true);
            dirTree.addTreeSelectionListener(new MyFileManagerFrame.DirPanel.DemoTreeSelectionListener());
            dirTree.addTreeWillExpandListener(new MyFileManagerFrame.DirPanel.ExpansionListener());
            rootFile = new File(drive);
            buildTree();
            dirPanelScrollPane.setViewportView(dirTree);
            this.setLayout(new BorderLayout());
            this.add(dirPanelScrollPane, BorderLayout.CENTER);
            DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) dirTree.getCellRenderer();
            renderer.setLeafIcon(renderer.getClosedIcon());
        }

        private void buildTree() {
            MyFileNode root = new MyFileNode(rootFile.getPath(), rootFile);
            DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(root);

            treemodel = new DefaultTreeModel(rootNode);
            dirTree.setRootVisible(true);
            showSubdirectories(rootNode, root.getFile());
            dirTree.setModel(treemodel);
        }


        private void createNodes(DefaultMutableTreeNode node, File dir) {

            File[] files = dir.listFiles();
            if (files == null) return;
            for (File file : files) {
                if (file.isDirectory() && !(file.isHidden())) {
                    MyFileNode mfn = new MyFileNode(file.getName(), file);
                    DefaultMutableTreeNode test = new DefaultMutableTreeNode(mfn);
                    node.add(test);
                } }
        }

        private void showSubdirectories(DefaultMutableTreeNode node, File dir) {
            node.removeAllChildren();

            File[] files = dir.listFiles();
            if (files == null) return;
            for (File file : files) {
                if (file.isDirectory() && !(file.isHidden())) {
                    MyFileNode mfn = new MyFileNode(file.getName(), file);
                    DefaultMutableTreeNode test = new DefaultMutableTreeNode(mfn);
                    node.add(test);
                    createNodes(test, mfn.getFile());

                    } }
        }

        class DemoTreeSelectionListener implements TreeSelectionListener {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) dirTree.getLastSelectedPathComponent();
                if (node == null) return;
                MyFileNode mfn = (MyFileNode) node.getUserObject();
                dirPanelCurrentDirectory = mfn.mfngetAbsolutePath();
                currentFileArray = mfn.getFileList();
                updateTitle(dirPanelCurrentDirectory);
                changeFilePanel();
                test.dirs.add(dirPanelCurrentDirectory);
            }
        }

        class ExpansionListener implements TreeWillExpandListener {

            @Override
            public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
                TreePath path = event.getPath();
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                MyFileNode mfn = (MyFileNode) node.getUserObject();

                showSubdirectories(node, mfn.getFile());
            }

            @Override
            public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {

            }
        }

        public JTree getDirTree() {
            return this.dirTree;
        }


    }

    public class FilePanel extends JPanel {
        private JScrollPane filePanelScrollPane = new JScrollPane();
        JList<String> stringList;
        DefaultListModel model = new DefaultListModel();
        ArrayList<String> filePath = new ArrayList<>();
        String currentDirectory;
        ArrayList<String> filesArray;


        public FilePanel() {
            this.setDropTarget(new MyDropTarget());
            clickAction(currentFileArray);
            //System.out.println(Arrays.toString(currentFileArray));
            filePanelScrollPane.setViewportView(stringList);
            this.setLayout(new BorderLayout());
            this.add(filePanelScrollPane, BorderLayout.CENTER);

        }

        public void RenameChosenFile(String s) {
            File file = new File(chosenFile);
            File file2 = new File(s);

            boolean success = file.renameTo(file2);
        }

        public void CopyChosenFile(String s) {

            if (s == null) return;
            File from = new File(chosenFile);
            File to = new File(s);

            if (!(to.isDirectory())) return;

            Path source = from.toPath();
            Path dest = to.toPath();

            try {
                Files.copy(source, dest.resolve(source.getFileName()));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        public void clickAction(File[] files) {
            if (currentFileArray == null) {
                return;
            }
            filesArray = getFilesArray(files);

            String[] fileArrayPath = filePath.toArray(new String[filePath.size()]);
            stringList = new JList(filesArray.toArray());
            stringList.setDragEnabled(true);


                stringList.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent evt) {
                        JList stringList = (JList)evt.getSource();
                        if (evt.getClickCount() == 1) {
                            int index = stringList.locationToIndex(evt.getPoint());
                            chosenFile = fileArrayPath[index];
                            File testFile = new File(chosenFile);
                        }
                        if (evt.getClickCount() == 2) {
                            int index = stringList.locationToIndex(evt.getPoint());
                            Desktop desktop = Desktop.getDesktop();
                            try {
                                desktop.open(new File(fileArrayPath[index]));
                            } catch (IOException ex) {
                                System.out.println(ex.toString());
                            }
                        }
                        if (evt.getButton() == MouseEvent.BUTTON3) {
                            int index = stringList.locationToIndex(evt.getPoint());
                            chosenFile = fileArrayPath[index];
                            App.popupmenu popup = test.new popupmenu();
                            popup.show(evt.getComponent(), evt.getX(), evt.getY());
                        }
                    }
                });
            }


        public String getSizeAndDate(File file) {
            String filex = "";
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            DecimalFormat dformat = new DecimalFormat("#,###");

            if (file.isDirectory()) {

                filex += file.getName();
            }

            if (file.isFile()) {
                if (!showDetails) {
                    filex += file.getName() + " ";
                }
                else {
                    filex += file.getName() + "            " + "Date Last Modified: " + " " + formatter.format(file.lastModified()) + " " + "Size: " + " " + dformat.format(file.length());
                }
            }
            return filex; }



        public ArrayList<String> getFilesArray(File[] file) {
            if (file == null) return null;
            ArrayList<String> files = new ArrayList<>();
            for (File value : file) {
                if (value.isDirectory()) {
                    filePath.add(value.getAbsolutePath());
                    files.add(getSizeAndDate(value));
                }
            }
            for (File value : file) {
                if (!(value.isDirectory())) {
                    filePath.add(value.getAbsolutePath());
                    files.add(getSizeAndDate(value));
                    currentDirectory = value.getParent();
                }
            }
            return files;
        }

        public ArrayList<String> getFilesArrayNoDetails(File[] file) {
            if (file == null) return null;
            ArrayList<String> files = new ArrayList<>();
            for (File value : file) {
                filePath.add(value.getAbsolutePath());
            }
            return files;
        }

        public String getFileExtension(String s) {
            int index = s.lastIndexOf('.');
            String extension = s.substring(index + 1);
            String otherExtension = "";

            for (int i = 0; i < extension.length(); i++) {
                if (extension.charAt(i) == ' ') {
                    break;
                }
                else {
                    otherExtension += extension.charAt(i);
                }
            }
            String restOfString = s.substring(0, index);
            return restOfString + "." + otherExtension;
        }




        class MyDropTarget extends DropTarget {
            public void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    List result = new ArrayList();
                    if(evt.getTransferable().isDataFlavorSupported(DataFlavor.stringFlavor)) {
                        String temp = (String)evt.getTransferable().getTransferData(DataFlavor.stringFlavor);


                        String[] next = temp.split("\\n");
                        String InternalDnDFileString = "";
                        System.out.println(test.dirs);


                        for (String s : next) {
                            File curFile = new File(s);

                            if (curFile.isDirectory()) {
                                InternalDnDFileString = curFile.getAbsolutePath();
                            }

                            else {
                                InternalDnDFileString = getFileExtension(s);
                            }
                            InternalDnDFileString = test.dirs.get(test.dirs.size() - 1) + "\\" + InternalDnDFileString;
                            System.out.println(InternalDnDFileString);


                            File from = new File(InternalDnDFileString);
                            if (currentDirectory == null) {
                                currentDirectory = dirPanelCurrentDirectory;
                            }

                            File to = new File(currentDirectory);

                            Path source = from.toPath();
                            Path dest = to.toPath();

                            try {
                                Files.copy(source, dest.resolve(source.getFileName()));
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                        File shitgay = new File(currentDirectory);
                        currentFileArray = shitgay.listFiles();
                        changeFilePanel();
                    }


                    else {
                        result = (List)evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                        for(Object o : result) {

                            File from = new File(o.toString());
                            File to = new File(currentDirectory);

                            Path source = from.toPath();
                            Path dest = to.toPath();

                            try {
                                Files.copy(source, dest.resolve(source.getFileName()));
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                        File updatedArr = new File(currentDirectory);
                        currentFileArray = updatedArr.listFiles();
                        changeFilePanel();
                    }

                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

    }

}