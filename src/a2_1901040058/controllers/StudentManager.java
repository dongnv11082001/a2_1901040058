package a2_1901040058.controllers;

import a2_1901040058.DBHelper;
import a2_1901040058.models.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentManager extends Manager {
    // view elements
    private JTextField jStudentName;
    private JTextField jDob;
    private JTextField jAddress;
    private JTextField jEmail;
    private ArrayList<Student> students;
    Connection connection;

    public StudentManager(String title, Connection connection) {
        super(title);
        students = new ArrayList<>();
        this.connection = connection;
    }

    @Override
    public void createMiddlePanel() {
        gui = new JFrame("Create new student");
        gui.setSize(400, 300);
        gui.addWindowListener(this);
        gui.setLocation(50, 200);
        // top
        JPanel pnlTop = new JPanel();
        gui.add(pnlTop, BorderLayout.NORTH);
        JLabel lblTitle = new JLabel("Enter student details:");
        pnlTop.add(lblTitle);
        // middle
        middlePanel = new JPanel();
        middlePanel.setLayout(new GridLayout(0, 2));
        middlePanel.setBorder(BorderFactory.createEtchedBorder());

        JLabel lblName = new JLabel("Name: (*)");
        JLabel lblDob = new JLabel("Date of birth: (*)");
        JLabel lblAdd = new JLabel("Address:");
        JLabel lblEmail = new JLabel("Email:");
        jStudentName = new JTextField(20);
        jDob = new JTextField(20);
        jAddress = new JTextField(20);
        jEmail = new JTextField(20);
        middlePanel.add(lblName);
        middlePanel.add(jStudentName);
        middlePanel.add(lblDob);
        middlePanel.add(jDob);
        middlePanel.add(lblAdd);
        middlePanel.add(jAddress);
        middlePanel.add(lblEmail);
        middlePanel.add(jEmail);

        gui.add(middlePanel, BorderLayout.CENTER);

        // bottom
        JPanel pnlBottom = new JPanel();
        gui.add(pnlBottom, BorderLayout.SOUTH);
        JButton btnOK = new JButton("OK");
        btnOK.addActionListener(this);
        pnlBottom.add(btnOK);
    }


    @Override
    public Object createObject() throws Exception {
        Student s;
        String name = jStudentName.getText();
        String dob = jDob.getText();
        String address = jAddress.getText();
        String email = jEmail.getText();
        if (name.isEmpty() || dob.isEmpty()) {
            showErrorMessage("Invalid input! Please check again (filed with * are required)");
        } else {
            s = new Student(name, dob, address, email);
            students.add(s);
            DBHelper.createStudent(s);
            showMessage("created student= " + s);
        }
        return students;
    }

    public Student getStudentByID(String id) {
        try {
            List<Student> students = DBHelper.getAllStudents();
            for (Student student : students) {
                if (student.getId().equalsIgnoreCase(id)) {
                    return student;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void showListStudent() {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM students");
            ResultSet rs = ps.executeQuery();
            JFrame frame = new JFrame("List of the students");
            String[] headers = {"Student ID", "Student Name", "Student Address", "Student Email", "Date of birth"};
            Object[][] data = {};
            DefaultTableModel table = new DefaultTableModel(data, headers);
            int i = 0;
            while (rs.next()) {
                table.addRow(data);
                String id = rs.getString("id");
                String name = rs.getString("name");
                String address = rs.getString("address");
                String email = rs.getString("email");
                String dob = rs.getString("dob");
                table.setValueAt(id, i, 0);
                table.setValueAt(name, i, 1);
                table.setValueAt(address, i, 2);
                table.setValueAt(email, i, 3);
                table.setValueAt(dob, i, 4);
                i++;
            }

            JTable studentTable = new JTable(table);
            frame.add(new JScrollPane(studentTable));
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frame.setPreferredSize(new Dimension(700, 400));
            frame.setLocation(new Point(0, 100));
            frame.setVisible(true);
            frame.pack();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
