package a2_1901040058.controllers;

import a2_1901040058.DBHelper;
import a2_1901040058.exceptions.NotPossibleException;
import a2_1901040058.models.*;
import a2_1901040058.models.Module;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class EnrolmentManager extends Manager {
    private JTextField jStu, jMod, jInternal, jExam;
    private final StudentManager studentManager;
    private final ModuleManager moduleManager;
    Connection connection;
    Statement statement;
    ResultSet rs;

    public EnrolmentManager(String title, StudentManager studentManager, ModuleManager moduleManager) {
        super(title);
        this.studentManager = studentManager;
        this.moduleManager = moduleManager;

        try {
            connection = DBHelper.getConnection();
            statement = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(this);
        pnlBottom.add(btnCancel);
    }

    public void initialReport() throws SQLException {
        rs = statement.executeQuery("SELECT * FROM enrolments");
        while (rs.next()) {
            String sid = rs.getString("s_id");
            String mcode = rs.getString("m_code");
            double internal = rs.getDouble("internal");
            double examination = rs.getDouble("examination");
            Enrolment enrolment = new Enrolment(studentManager.getStudentByID(sid), moduleManager.getModuleByCode(mcode), (float) internal, (float) examination);
            objects.add(enrolment);
        }
        JFrame frame = new JFrame("List of the initial enrolments");
        String[] headers = {"#", "Student ID", "Student Name", "Module Code", "Module Name"};
        Object[][] data = {};
        DefaultTableModel table = new DefaultTableModel(data, headers);
        JTable initialReportTable = new JTable(table);
        for (int i = 0; i < objects.size(); i++) {
            Enrolment e = (Enrolment) objects.get(i);
            table.addRow(data);
            table.setValueAt(i + 1, i, 0);
            table.setValueAt(e.getStudent().getId(), i, 1);
            table.setValueAt(e.getStudent().getName(), i, 2);
            table.setValueAt(e.getModule().getCode(), i, 3);
            table.setValueAt(e.getModule().getName(), i, 4);
        }
        frame.add(new JScrollPane(initialReportTable));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(700, 400));
        frame.setLocation(new Point(0, 100));
        frame.setVisible(true);
        frame.pack();
    }

    public void assessmentReport() throws SQLException {
        rs = statement.executeQuery("SELECT * FROM enrolments");
        while (rs.next()) {
            String sid = rs.getString("s_id");
            String mcode = rs.getString("m_code");
            double internal = rs.getDouble("internal");
            double examination = rs.getDouble("examination");
            Enrolment enrolment = new Enrolment(studentManager.getStudentByID(sid), moduleManager.getModuleByCode(mcode), (float) internal, (float) examination);
            objects.add(enrolment);
        }
        JFrame frame = new JFrame("List of the assessed enrolments");
        String[] headers = {"#", "Student ID", "Module Code", "Internal mark", "Examination mark", "Final grade"};
        DefaultTableModel table = new DefaultTableModel(headers, 0);
        for (int i = 0; i < objects.size(); i++) {
            Enrolment e = (Enrolment) objects.get(i);
            Object[] obj = {
                    i + 1,
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
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(700, 400));
        frame.setLocation(0, 100);
        frame.setVisible(true);
    }

    @Override
    protected Object createObject() throws NotPossibleException {
        Enrolment enrolment = null;
        Module module;
        Student student;

        String studentId = jStu.getText();
        String moduleCode = jMod.getText();
        float internalMark = Float.parseFloat(jInternal.getText());
        float examMark = Float.parseFloat(jExam.getText());

        student = studentManager.getStudentByID(studentId);
        module = moduleManager.getModuleByCode(moduleCode);
        if (student == null) {
            showErrorMessage("Not found Student with id=" + studentId);
        } else if (module == null) {
            showErrorMessage("Not found Module with code=" + moduleCode);
        } else {
            enrolment = new Enrolment(student, module, internalMark, examMark);
            objects.add(enrolment);
            showMessage("created object=Enrolment(" + enrolment + ", " + enrolment.getInternalMark() + ", "
                    + enrolment.getExaminationMark() + ")");
            try {
                String sql = "INSERT INTO enrolments(s_id,s_name,m_code,m_name,internal,examination) VALUES(?,?,?,?,?,?)";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, studentId);
                ps.setString(2, student.getName());
                ps.setString(3, moduleCode);
                ps.setString(4, module.getName());
                ps.setDouble(5, internalMark);
                ps.setDouble(6, examMark);
                ps.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
            gui.setVisible(false);
        }

        return enrolment;
    }
}
