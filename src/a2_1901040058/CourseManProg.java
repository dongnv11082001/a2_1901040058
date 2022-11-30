package a2_1901040058;

import a2_1901040058.controllers.EnrolmentManager;
import a2_1901040058.controllers.ModuleManager;
import a2_1901040058.controllers.StudentManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

public class CourseManProg extends WindowAdapter implements ActionListener {
    private final StudentManager studentManager; // the student manager
    private final ModuleManager moduleManager; // the module manager
    private final EnrolmentManager enrolmentManager; // the module manager
    private JFrame gui;

    public CourseManProg() {
        createGUI();
        studentManager = new StudentManager("StudentManager");
        moduleManager = new ModuleManager("ModuleManager");
        enrolmentManager = new EnrolmentManager("EnrolmentManager", studentManager, moduleManager);
    }

    public void createGUI() {
        // 1. create window object
        gui = new JFrame("CourseMan");
        // 2. setup window
        gui.setSize(400, 460);
        // 3. handle window events
        gui.addWindowListener(this);
        gui.setLocationRelativeTo(null);
        // 4. add components
        // menu
        JMenuBar menubar = new JMenuBar();
        gui.setJMenuBar(menubar);

        JMenu mFile = new JMenu("File");
        menubar.add(mFile);

        JMenu mStudent = new JMenu("Student");
        menubar.add(mStudent);

        JMenu mModule = new JMenu("Module");
        menubar.add(mModule);

        JMenu mEnrol = new JMenu("Enrolment");
        menubar.add(mEnrol);

        JMenuItem miExit = new JMenuItem("Exit");
        miExit.addActionListener(this);
        mFile.add(miExit);

        JMenuItem miStudent = new JMenuItem("New Student");
        miStudent.addActionListener(this);
        JMenuItem miStudentList = new JMenuItem("List Student");
        miStudentList.addActionListener(this);
        mStudent.add(miStudent);
        mStudent.add(miStudentList);

        JMenuItem miModule = new JMenuItem("New Module");
        miModule.addActionListener(this);
        JMenuItem miModuleList = new JMenuItem("List Module");
        miModuleList.addActionListener(this);
        mModule.add(miModule);
        mModule.add(miModuleList);

        JMenuItem miEnrol = new JMenuItem("New Enrolment");
        miEnrol.addActionListener(this);
        JMenuItem miIni = new JMenuItem("Initial Report");
        miIni.addActionListener(this);
        JMenuItem miAss = new JMenuItem("Assessment Report");
        miAss.addActionListener(this);

        mEnrol.add(miEnrol);
        mEnrol.add(miIni);
        mEnrol.add(miAss);

        // content
        // top
        JPanel pnlTop = new JPanel();
        gui.add(pnlTop, BorderLayout.NORTH);

        JPanel pnlMiddle = new JPanel();
        pnlMiddle.setLayout(new GridLayout(0, 2));
        pnlMiddle.setBorder(BorderFactory.createEtchedBorder());
        gui.add(pnlMiddle, BorderLayout.CENTER);

        ImageIcon image = new ImageIcon(Objects.requireNonNull(getClass().getResource("/a2_1901040058/images/doge_point.jpg")));
        JLabel imageLabel = new JLabel(image) {
            public void paintComponent(Graphics g) {
                g.drawImage(image.getImage(), 0, 0, 385, 380, null);
            }
        };
        gui.add(imageLabel);
    }

    public void display() {
        gui.setVisible(true);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        shutDown();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        try {
            switch (cmd) {
                case "New Student" -> studentManager.display();
                case "List Student" -> studentManager.showListStudent();
                case "New Module" -> moduleManager.display();
                case "List Module" -> moduleManager.showListModule();
                case "New Enrolment" -> enrolmentManager.display();
                case "Initial Report" -> enrolmentManager.initialReport();
                case "Assessment Report" -> enrolmentManager.assessmentReport();
                case "Exit" -> shutDown();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void startUp() throws Exception {
        studentManager.startUp();
        moduleManager.startUp();
        enrolmentManager.startUp();
    }

    public void shutDown() {
        studentManager.shutDown();
        moduleManager.shutDown();
        enrolmentManager.shutDown();
        System.exit(1);
    }

    public static void main(String[] args) throws Exception {
        CourseManProg app = new CourseManProg();
        app.startUp();
        app.display();
    }
}
