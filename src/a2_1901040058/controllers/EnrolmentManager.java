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

public class EnrolmentManager extends Manager {
    private JComboBox comboStudent, comboModule;
    private JTextField jInternal, jExamination;
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
        String[] listStudentId = null;
        String[] listModuleId = null;
        gui = new JFrame("Create new enrolment");
        gui.setSize(400, 300);
        gui.addWindowListener(this);
        gui.setLocation(50, 200);
        JPanel pnlTop = new JPanel();
        gui.add(pnlTop, BorderLayout.NORTH);
        JLabel lblTitle = new JLabel("Enter details:");
        pnlTop.add(lblTitle);
        middlePanel = new JPanel();
        middlePanel.setLayout(new GridLayout(0, 2));
        middlePanel.setBorder(BorderFactory.createEtchedBorder());
        JPanel panelComboStudent = new JPanel(new GridLayout(1, 2));
        try {
            listStudentId = DBHelper.getAllStudentsID();
            listModuleId = DBHelper.getAllModulesCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert listStudentId != null;
        comboStudent = new JComboBox<>(listStudentId);
        JLabel lblStu = new JLabel("Student ID(*):");
        panelComboStudent.add(lblStu);
        panelComboStudent.add(comboStudent);

        JPanel panelComboModule = new JPanel(new GridLayout(1, 2));
        assert listModuleId != null;
        comboModule = new JComboBox<>(listModuleId);
        JLabel lblMod = new JLabel("Module Code(*):");
        panelComboModule.add(lblMod);
        panelComboModule.add(comboModule);

        JLabel lblIn = new JLabel("Internal mark:");
        JLabel lblEx = new JLabel("Examination mark:");
        jInternal = new JTextField(20);
        jExamination = new JTextField(20);
        middlePanel.add(lblStu);
        middlePanel.add(panelComboStudent);
        middlePanel.add(lblMod);
        middlePanel.add(panelComboModule);
        middlePanel.add(lblIn);
        middlePanel.add(jInternal);
        middlePanel.add(lblEx);
        middlePanel.add(jExamination);
        gui.add(middlePanel, BorderLayout.CENTER);
        JPanel pnlBottom = new JPanel();
        gui.add(pnlBottom, BorderLayout.SOUTH);
        JButton btnOK = new JButton("OK");
        btnOK.addActionListener(this);
        pnlBottom.add(btnOK);

        comboStudent.addActionListener(e -> {
            comboStudent = (JComboBox) e.getSource();
            String sid = (String) comboStudent.getSelectedItem();
            System.out.println(sid);
        });

        comboModule.addActionListener(e -> {
            comboModule = (JComboBox) e.getSource();
            String mcode = (String) comboModule.getSelectedItem();
            System.out.println(mcode);
        });
    }

    public void initialReport() {
        try {
            JFrame frame = new JFrame("List of the initial enrolments");
            String[] headers = {"#","Student ID", "Student Name", "Module Code", "Module Name"};
            Object[][] data = {};
            DefaultTableModel table = new DefaultTableModel(data, headers);
            JTable initialReportTable = new JTable(table);
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM enrolments");
            ResultSet rs = ps.executeQuery();
            int row = 0;
            while (rs.next()) {
                table.addRow(data);
                String id = rs.getString("s_id");
                String sName = rs.getString("s_name");
                String code = rs.getString("m_code");
                String mName = rs.getString("m_name");
                table.setValueAt(row + 1, row, 0);
                table.setValueAt(id, row, 1);
                table.setValueAt(sName, row, 2);
                table.setValueAt(code, row, 3);
                table.setValueAt(mName, row, 4);
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
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM enrolments");
            ResultSet rs = ps.executeQuery();
            int row = 0;
            JFrame frame = new JFrame("List of the assessment enrolments");
            Object[][] data = {};
            String[] headers = {"#","Student ID", "Student Name", "Module Code", "Module Name", "Internal", "Examination", "Final Grade"};
            DefaultTableModel table = new DefaultTableModel(data, headers);
            while (rs.next()) {
                table.addRow(data);
                String id = rs.getString("s_id");
                String sName = rs.getString("s_name");
                String code = rs.getString("m_code");
                String mName = rs.getString("m_name");
                float internal = (float) rs.getDouble("internal");
                float examination = (float) rs.getDouble("examination");
                String finalGrade = rs.getString("finalGrade");
                table.setValueAt(row + 1, row, 0);
                table.setValueAt(id, row, 1);
                table.setValueAt(sName, row, 2);
                table.setValueAt(code, row, 3);
                table.setValueAt(mName, row, 4);
                table.setValueAt(internal, row, 5);
                table.setValueAt(examination, row, 6);
                table.setValueAt(finalGrade, row, 7);
                row++;
            }
            JTable assessmentReportTable = new JTable(table);
            frame.add(new JScrollPane(assessmentReportTable));
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

        String studentId = (String) comboStudent.getSelectedItem();
        String moduleCode = (String) comboModule.getSelectedItem();
        float internalMark = (float) Double.parseDouble(jInternal.getText());
        float examMark = (float) Double.parseDouble(jExamination.getText());

        student = studentManager.getStudentByID(studentId);
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
