package filemanager277;

//import filemanager.FilePanel.RenameListener;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JComboBox;

class App extends JFrame {

    //initializing all the frames aggregated from JFrame
    JPanel panel, topPanel;
    JMenuBar menuBar;
    JToolBar toolBar, driveBar, statusBar;
    JDesktopPane desktop;
    MyFileManagerFrame myfm, myfm2;
    JButton toolDetails, toolSimple;
    String drive, freeSpace, usedSpace, totalSpace, currentDrive;
    File[] paths;



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
        myfm = new MyFileManagerFrame();
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
        buildstatusbar();

        desktop.add(myfm);
        topPanel.add(menuBar, BorderLayout.NORTH);
        topPanel.add(toolBar, BorderLayout.CENTER);
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(desktop, BorderLayout.CENTER);
        panel.add(statusBar, BorderLayout.SOUTH);
        add(panel);

    }

    private void buildMenu() {

        /*buildFileMenu();
        buildTreeMenu();
        buildWindowMenu();
        buildHelpMenu();*/

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

                myfm2 = new MyFileManagerFrame(s);
                myfm2.drive = s;
                desktop.add(myfm2);
                /*MyFileManagerFrame active = (MyFileManagerFrame) desktop.getSelectedFrame();
                desktop.add(new MyFileManagerFrame(s));*/
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

    private void buildstatusbar() {
        this.currentDrive = getDrives()[0];
        File file = new File(currentDrive);
        long totalSpace = file.getTotalSpace();
        long freeSpace = file.getFreeSpace();
        long usedSpace = totalSpace - freeSpace;

        JLabel sizeLabel = new JLabel("Total Space in GB: " + totalSpace + "    ");
        JLabel driveLabel = new JLabel("Current Drive: " + currentDrive + "   ");
        JLabel usedSpaceLabel = new JLabel("Used Space: " + usedSpace + "    ");
        JLabel freeSpaceLabel = new JLabel("Free space: " + freeSpace + "    ");
        statusBar.add(driveLabel);
        statusBar.add(freeSpaceLabel);
        statusBar.add(usedSpaceLabel);
        statusBar.add(sizeLabel);
    }

    private void updateStatusbar(String currentDrive) {
        MyFileManagerFrame activeFrame = (MyFileManagerFrame) desktop.getSelectedFrame();
        System.out.println(activeFrame.drive);
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

                if (activeFrame.currentFileArray != null) {
                    activeFrame.showDetails = true;
                    activeFrame.changeFilePanel(myfm.currentFileArray);
                }

            }
            if (e.getActionCommand().equals("Simple")) {
                MyFileManagerFrame activeFrame = (MyFileManagerFrame) desktop.getSelectedFrame();
                if (activeFrame.currentFileArray != null) {
                    activeFrame.showDetails = false;
                    activeFrame.hideDetailsWhenClicked();
                }

            }
        }
    }

    private class NewActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            MyFileManagerFrame newMFM = new MyFileManagerFrame();
            desktop.add(newMFM);
        }
    }

    private class MyFocusListener implements FocusListener {

        @Override
        public void focusGained(FocusEvent e) {

        }

        @Override
        public void focusLost(FocusEvent e) {

        }
    }



}

