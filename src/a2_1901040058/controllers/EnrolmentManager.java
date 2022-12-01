package a2_1901040058.controllers;

import a2_1901040058.DBHelper;
import a2_1901040058.exceptions.NotPossibleException;
import a2_1901040058.models.Enrolment;
import a2_1901040058.models.Module;
import a2_1901040058.models.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class EnrolmentManager extends Manager {
    private JTextField jStu, jMod, jInternal, jExam;
    private final StudentManager studentManager;
    private final ModuleManager moduleManager;
    Connection connection;

    public EnrolmentManager(String title, StudentManager studentManager, ModuleManager moduleManager, Connection connection) {
        super(title);
        this.studentManager = studentManager;
        this.moduleManager = moduleManager;
        this.connection = connection;
    }

    @Override
    protected void createMiddlePanel() {
        gui = new JFrame("Create new enrolment");
        gui.setSize(400, 300);
        gui.addWindowListener(this);
        gui.setLocation(50, 200);
        // top
        JPanel pnlTop = new JPanel();
        gui.add(pnlTop, BorderLayout.NORTH);
        JLabel lblTitle = new JLabel("Enter details:");
        pnlTop.add(lblTitle);
        // middle
        middlePanel = new JPanel();
        middlePanel.setLayout(new GridLayout(0, 2));
        middlePanel.setBorder(BorderFactory.createEtchedBorder());
        JLabel lblStu = new JLabel("Student ID(*):");
        JLabel lblMod = new JLabel("Module Code(*):");
        JLabel lblIn = new JLabel("Internal mark:");
        JLabel lblEx = new JLabel("Exam mark:");
        jStu = new JTextField(20);
        jMod = new JTextField(20);
        jInternal = new JTextField(20);
        jExam = new JTextField(20);
        middlePanel.add(lblStu);
        middlePanel.add(jStu);
        middlePanel.add(lblMod);
        middlePanel.add(jMod);
        middlePanel.add(lblIn);
        middlePanel.add(jInternal);
        middlePanel.add(lblEx);
        middlePanel.add(jExam);
        gui.add(middlePanel, BorderLayout.CENTER);
        // bottom
        JPanel pnlBottom = new JPanel();
        gui.add(pnlBottom, BorderLayout.SOUTH);
        JButton btnOK = new JButton("OK");
        btnOK.addActionListener(this);
        pnlBottom.add(btnOK);
    }

    public void initialReport() {
        try {
            JFrame frame = new JFrame("List of the initial enrolments");
            String[] headers = {"Student ID", "Student Name", "Module Code", "Module Name"};
            Object[][] data = {};
            DefaultTableModel table = new DefaultTableModel(data, headers);
            JTable initialReportTable = new JTable(table);
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM enrolments");
            ResultSet rs = ps.executeQuery();
            List<Enrolment> enrolments = DBHelper.getAllEnrolments();
            int row = 0;
            while (rs.next()) {
                table.addRow(data);
                Enrolment e = enrolments.get(row);
                table.setValueAt(e.getStudent().getId(), row, 0);
                table.setValueAt(e.getStudent().getName(), row, 1);
                table.setValueAt(e.getModule().getCode(), row, 2);
                table.setValueAt(e.getModule().getName(), row, 3);
                row++;
            }
            frame.add(new JScrollPane(initialReportTable));
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frame.setPreferredSize(new Dimension(700, 400));
            frame.setLocation(new Point(0, 100));
            frame.setVisible(true);
            frame.pack();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void assessmentReport() {
        try {
            JFrame frame = new JFrame("List of the assessed enrolments");
            String[] headers = {"Student ID", "Module Code", "Internal mark", "Examination mark", "Final grade"};
            Object[][] data = {};
            DefaultTableModel table = new DefaultTableModel(data, headers);
            List<Enrolment> enrolments = DBHelper.getAllEnrolments();
            System.out.println(enrolments);
            for (Enrolment e : enrolments) {
                Object[] obj = {
                        e.getStudent().getId(),
                        e.getModule().getCode(),
                        e.getInternalMark(),
                        e.getExaminationMark(),
                        e.getFinalGrade()
                };
                table.addRow(obj);
            }
            JTable tblContacts = new JTable(table);
            frame.add(new JScrollPane(tblContacts));
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frame.setPreferredSize(new Dimension(700, 400));
            frame.setLocation(0, 100);
            frame.setVisible(true);
            frame.pack();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    protected Object createObject() throws NotPossibleException, SQLException {
        Enrolment enrolment = null;
        Module module;
        Student student;

        String studentId = jStu.getText();
        String moduleCode = jMod.getText();
        float internalMark = Float.parseFloat(jInternal.getText());
        float examMark = Float.parseFloat(jExam.getText());

        student = studentManager.getStudentByID(studentId);
        System.out.println(student);
        module = moduleManager.getModuleByCode(moduleCode);
        if (student == null) {
            showErrorMessage("Not found Student with id=" + studentId);
        } else if (module == null) {
            showErrorMessage("Not found Module with code=" + moduleCode);
        } else {
            enrolment = new Enrolment(student, module, internalMark, examMark);
            objects.add(enrolment);
            DBHelper.createEnrolment(enrolment);
            showMessage("created object=Enrolment(" + enrolment + ", " + enrolment.getInternalMark() + ", "
                    + enrolment.getExaminationMark() + ")");
            gui.setVisible(false);
        }
        return enrolment;
    }
}
