package filemanager277;

//import filemanager.FilePanel.RenameListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;

class App extends JFrame {

    public final long BYTE_TO_GB_CONVERSION = 1073741824;
    JPanel panel, topPanel;
    JMenuBar menuBar;
    JToolBar toolBar, driveBar, statusBar;
    JDesktopPane desktop;
    MyFileManagerFrame myfm;
    String currentDrive;
    File[] paths;
    public App test = this;
    JLabel sizeLabel = new JLabel();
    JLabel driveLabel = new JLabel();
    JLabel usedSpaceLabel = new JLabel();
    JLabel freeSpaceLabel = new JLabel();
    String copiedPath;
    ArrayList<String> dirs = new ArrayList<String>();


    //constructing all the frames
    public App() {
        panel = new JPanel();   //opens the system panel but not file manager YET
        topPanel = new JPanel();
        menuBar = new JMenuBar();
        toolBar = new JToolBar();
        driveBar = new JToolBar();
        statusBar = new JToolBar();
        desktop = new JDesktopPane();

        currentDrive = this.getDrives()[0];
        myfm = new MyFileManagerFrame(currentDrive, this);
        panel.setLayout(new BorderLayout());
        topPanel.setLayout(new BorderLayout());

        this.setTitle("CECS 277 File Manager");
        this.setSize(1000, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void go() {
        buildMenu();
        buildtoolbar();
        buildstatusbar(currentDrive);
        desktop.add(myfm);
        topPanel.add(menuBar, BorderLayout.NORTH);
        topPanel.add(toolBar, BorderLayout.CENTER);
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(desktop, BorderLayout.CENTER);
        panel.add(statusBar, BorderLayout.SOUTH);
        add(panel);
    }

    private void buildMenu() {

        JMenu fileMenu, helpMenu, treeMenu, windowMenu;
        fileMenu = new JMenu("File");
        helpMenu = new JMenu("Help");
        treeMenu = new JMenu("Tree");
        windowMenu = new JMenu("Window");


        JMenuItem rename = new JMenuItem("Rename");
        JMenuItem copy = new JMenuItem("Copy");
        JMenuItem delete = new JMenuItem("Delete");
        JMenuItem run = new JMenuItem("Run");
        JMenuItem exit = new JMenuItem("Exit");
        JMenuItem expandBranch = new JMenuItem("Expand Branch");
        JMenuItem collapseBranch = new JMenuItem("Collapse Branch");
        JMenuItem New = new JMenuItem("New");
        JMenuItem cascade = new JMenuItem("Cascade");
        JMenuItem about = new JMenuItem("About");
        JMenuItem help = new JMenuItem("Help");

        //Action Listeners
        about.addActionListener(new AboutActionListener());
        help.addActionListener(new HelpActionListener());
        exit.addActionListener(new ExitActionListener());
        New.addActionListener(new NewActionListener());
        expandBranch.addActionListener(new ExpandCollapseActionListener());
        collapseBranch.addActionListener(new ExpandCollapseActionListener());
        rename.addActionListener(new MenuItemListener());
        copy.addActionListener(new MenuItemListener());
        delete.addActionListener(new MenuItemListener());
        run.addActionListener(new MenuItemListener());
        cascade.addActionListener(new CascadeActionListener());


        //Putting on Screen
        fileMenu.add(rename);
        fileMenu.add(copy);
        fileMenu.add(delete);
        fileMenu.add(run);
        fileMenu.add(exit);
        treeMenu.add(expandBranch);
        treeMenu.add(collapseBranch);
        windowMenu.add(New);
        windowMenu.add(cascade);
        helpMenu.add(help);
        helpMenu.add(about);

        menuBar.add(fileMenu);
        menuBar.add(treeMenu);
        menuBar.add(windowMenu);
        menuBar.add(helpMenu);


    }

    private void buildtoolbar() {
        JPanel toolpanel = new JPanel();
        JComboBox<String> drivesSelect = new JComboBox<String>(getDrives());
        JButton details = new JButton("Details");
        JButton simple = new JButton("Simple");

        ActionListener drivesSelectListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = (String) drivesSelect.getSelectedItem();

                MyFileManagerFrame myfm2 = new MyFileManagerFrame(s, test);
                myfm2.drive = s;
                desktop.add(myfm2);
            }
        };



        drivesSelect.addActionListener(drivesSelectListener);
        details.addActionListener(new DetailsActionListener());
        simple.addActionListener(new DetailsActionListener());


        toolpanel.add(drivesSelect);
        toolpanel.add(details);
        toolpanel.add(simple);
        toolBar.add(toolpanel);

    }

    public String[] getDrives() {
        paths = File.listRoots();
        String[] names = new String[paths.length];

        for (int i = 0; i < paths.length; i++) {
            names[i] = paths[i].getPath();
        }

        return names;
    }

    private void buildstatusbar(String drive) {
        File file = new File(drive);
        long totalSpace = file.getTotalSpace();
        long freeSpace = file.getFreeSpace();
        long usedSpace = totalSpace - freeSpace;

        sizeLabel.setText("Total Space: " + totalSpace/BYTE_TO_GB_CONVERSION + " GB    ");
        driveLabel.setText("Current Drive: " + currentDrive + "   ");
        usedSpaceLabel.setText("Used Space: " + usedSpace/BYTE_TO_GB_CONVERSION + " GB    ");
        freeSpaceLabel.setText("Free space: " + freeSpace/BYTE_TO_GB_CONVERSION + " GB    ");
        statusBar.add(driveLabel);
        statusBar.add(freeSpaceLabel);
        statusBar.add(usedSpaceLabel);
        statusBar.add(sizeLabel);
    }

    public void updateStatusBar() {
        MyFileManagerFrame activeFrame = (MyFileManagerFrame) desktop.getSelectedFrame();
        dirs.add(activeFrame.getDirPanelCurrentDirectory());
        this.currentDrive = activeFrame.drive;
        buildstatusbar(currentDrive);
        statusBar.repaint();
        statusBar.revalidate();
    }

    public void renameOrCopy(RenameDLG renamedlg, String command) {
        String tofield = "";
        MyFileManagerFrame activeFrame = (MyFileManagerFrame) desktop.getSelectedFrame();
        MyFileManagerFrame.FilePanel activeFP = activeFrame.filePanel;
        File fileName = new File(activeFrame.getChosenFile());
        String fileStringName = fileName.getName();
        renamedlg.setFromField(fileStringName);

        File testFile = new File(activeFrame.getChosenFile());
        String directory = testFile.getParent();
        renamedlg.setCurrentDirectory(directory);

        File parentFile = new File(directory);

        renamedlg.setVisible(true);
        if (command.equals("Rename")) {
            tofield += directory + "\\" + renamedlg.getToField();
            activeFP.RenameChosenFile(tofield);
        }
        if (command.equals("Copy")) {

            copiedPath = activeFrame.getDirPanelCurrentDirectory() + "\\" + renamedlg.getFromField();
            renamedlg.copyFileDiffDirectory(directory);
            tofield += renamedlg.getToField();
            activeFP.CopyChosenFile(tofield);
        }
        activeFrame.setCurrentFileArray(parentFile.listFiles());
        activeFrame.changeFilePanel();
    }

    public void pasteFile(String fileToPaste) {
        MyFileManagerFrame activeFrame = (MyFileManagerFrame) desktop.getSelectedFrame();

        File from = new File(fileToPaste);
        File to = new File(activeFrame.getDirPanelCurrentDirectory());

        File testFile = new File(activeFrame.getChosenFile());
        String directory = testFile.getParent();
        File parentFile = new File(directory);

        Path source = from.toPath();
        Path dest = to.toPath();

        try {
            Files.copy(source, dest.resolve(source.getFileName()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        activeFrame.setCurrentFileArray(parentFile.listFiles());
        activeFrame.changeFilePanel();
    }


    public class popupmenu extends JPopupMenu {

        JMenuItem popupRename = new JMenuItem("Rename");
        JMenuItem popupCopy = new JMenuItem("Copy");
        JMenuItem popupPaste = new JMenuItem("Paste");
        JMenuItem popupDelete = new JMenuItem("Delete");

        public popupmenu() {
            popupRename.addActionListener(new MenuItemListener());
            popupCopy.addActionListener(new MenuItemListener());
            popupPaste.addActionListener(new PasteActionListener());
            popupDelete.addActionListener(new MenuItemListener());
            this.add(popupRename);
            this.add(popupCopy);
            this.add(popupPaste);
            this.add(popupDelete);
        }
    }

    private class MenuItemListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("Rename")) {
                RenameDLG renamedlg = new RenameDLG(null, true);
                renamedlg.setTitle("Rename");

                renameOrCopy(renamedlg, "Rename");
            }


            if (e.getActionCommand().equals("Copy")) {
                RenameDLG renamedlg = new RenameDLG(null, true);
                renamedlg.setTitle("Copy");

                renameOrCopy(renamedlg, "Copy");
                System.out.println("Copied Path: " + copiedPath);

            }
            if (e.getActionCommand().equals("Run")) {
                MyFileManagerFrame activeFrame = (MyFileManagerFrame) desktop.getSelectedFrame();

                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.open(new File(activeFrame.getChosenFile()));

                } catch (IOException ex) {
                    System.out.println(ex.toString());
                }
            }
            if (e.getActionCommand().equals("Delete")) {
                MyFileManagerFrame activeFrame = (MyFileManagerFrame) desktop.getSelectedFrame();


                int deleteChoice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this File: " + activeFrame.getChosenFile(), "Deleting!", JOptionPane.YES_NO_OPTION);
                if (deleteChoice == JOptionPane.YES_OPTION) {
                    File deleteFile = new File(activeFrame.getChosenFile());
                    boolean successfullyDeleted = deleteFile.delete();
                    activeFrame.changeFilePanel();
                }
                else {
                    System.out.println("Sike");
                }
            }
        }
    }

    private class PasteActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            pasteFile(copiedPath);
            System.out.println("Coped file: " + copiedPath);
        }
    }



    private class ExpandCollapseActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            MyFileManagerFrame activeFrame = (MyFileManagerFrame) desktop.getSelectedFrame();
            JTree tree = activeFrame.dirPanel.getDirTree();

            int row = tree.getMinSelectionRow();

            if (e.getActionCommand().equals("Expand Branch")) {
                tree.expandRow(row);
            }
            if(e.getActionCommand().equals("Collapse Branch")) {
                tree.collapseRow(row);
            }
        }
    }

    private class ExitActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    private class AboutActionListener implements ActionListener {


        @Override
        public void actionPerformed(ActionEvent e) {
            AboutDlg dlg = new AboutDlg(null, true);
            dlg.setVisible(true);
        }
    }

    private class HelpActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            HelpDlg helpdlg = new HelpDlg(null, true);
            helpdlg.setVisible(true);
        }
    }

    private class DetailsActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            if (e.getActionCommand().equals("Details")) {
                MyFileManagerFrame activeFrame = (MyFileManagerFrame) desktop.getSelectedFrame();

                if (activeFrame.getCurrentFileArray() != null) {
                    activeFrame.showDetails = true;
                    activeFrame.changeFilePanel();
                }

            }
            if (e.getActionCommand().equals("Simple")) {
                MyFileManagerFrame activeFrame = (MyFileManagerFrame) desktop.getSelectedFrame();
                if (activeFrame.getCurrentFileArray() != null) {
                    activeFrame.showDetails = false;
                    activeFrame.hideDetailsWhenClicked();
                }

            }
        }
    }

    private class CascadeActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int x = 30;
            int y = 30;
            JInternalFrame[] allFramesArray = desktop.getAllFrames();
            for (int i = 0; i < allFramesArray.length; i++) {
                allFramesArray[i].setLocation(x * i, y * i);
            }
        }
    }

    private class NewActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            MyFileManagerFrame newMFM = new MyFileManagerFrame(getDrives()[0], test);
            desktop.add(newMFM);
        }
    }
}

