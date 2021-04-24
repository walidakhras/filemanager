package filemanager277;

//import filemanager.FilePanel.RenameListener;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    JPanel panel, topPanel;
    JMenuBar menuBar;
    JToolBar toolBar, driveBar, statusBar;
    JDesktopPane desktop;
    MyFileManagerFrame myfm, myfm2;
    //FileManagerFrame myf, myf2, myFrame;
    JButton toolDetails, toolSimple;
    String drive, freeSpace, usedSpace, totalSpace, currentDrive;
    File[] paths;



    public App() throws IOException {
        panel = new JPanel();
        topPanel = new JPanel();
        menuBar = new JMenuBar();
        toolBar = new JToolBar();
        driveBar = new JToolBar();
        statusBar = new JToolBar();
        desktop = new JDesktopPane();

        currentDrive = this.getDrives()[0];

        myfm = new MyFileManagerFrame();
        //myFrame = new FileManagerFrame(this);

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
        //desktop.add(myFrame);
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
        JLabel size = new JLabel("Total Space in GB: ");
        JLabel drive = new JLabel("Current Drive: " + currentDrive + "   ");
        JLabel usedSpace = new JLabel("Used Space: ");
        JLabel freeSpace = new JLabel("Free space: ");
        statusBar.add(drive);
        statusBar.add(freeSpace);
        statusBar.add(usedSpace);
        statusBar.add(size);
    }

    private void updateStatusbar() {

    }

    /*public void updateStatusBar(String currentDrive) {
        File file = new File(currentDrive);

        drive.setText("Current Drive: " + currentDrive);
        freeSpace.setText("Free Space: " + String.valueOf(file.getFreeSpace()/1073741824) + "GB");
        usedSpace.setText("Used Space: " + String.valueOf(file.getTotalSpace() - file.getFreeSpace()/1073741824) + "GB");
        totalSpace.setText("Total Space: " + String.valueOf(file.getFreeSpace()/1073741824) + "GB");



    }*/


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
                myfm.showDetails = true;
                myfm.changeFilePanel(myfm.currentFileArray);
                if (myfm2 != null) {
                    myfm2.showDetails = true;
                    myfm2.changeFilePanel(myfm.currentFileArray);
                }

            }
            if (e.getActionCommand().equals("Simple")) {
                myfm.showDetails = false;
                myfm.hideDetailsWhenClicked();
                if (myfm2 != null) {
                    myfm2.showDetails = false;
                    myfm2.hideDetailsWhenClicked();
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



}

