package a2_1901040058.controllers;

import a2_1901040058.models.Enrolment;
import a2_1901040058.models.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StudentManager extends Manager {
    // view elements
    private JTextField jStudentName;
    private JTextField jDob;
    private JTextField jAddress;
    private JTextField jEmail;

    public StudentManager(String title, String storageFile) {
        super(title, storageFile);
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
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(this);
        pnlBottom.add(btnCancel);
    }

    @Override
    public Object createObject() throws Exception {
        Student s = null;
        String name = jStudentName.getText();
        String dob = jDob.getText();
        String address = jAddress.getText();
        String email = jEmail.getText();
        if (name.isEmpty() || dob.isEmpty()) {
            showErrorMessage("Invalid input! Please check again (filed with * are required)");
        } else {
            s = new Student(name, dob, address, email);
            objects.add(s);
            showMessage("created student= " + s);
            gui.setVisible(false);
        }

        return null;
    }

    public Student getStudentByID(String id) {
        Student student = null;
        for (Object object : objects) {
            Student s = (Student) object;
            if (s.getId().equalsIgnoreCase(id)) {
                student = s;
            }
        }
        return student;
    }

    public void showListStudent() {
        JFrame frame = new JFrame("List of the students");
        String[] headers = {"#", "Student ID", "Student Name", "Student Address", "Student Email"};
        Object[][] data = {};
        DefaultTableModel table = new DefaultTableModel(data, headers);
        for (int i = 0; i < objects.size() - 1; i++) {
            Student s = (Student) objects.get(i);
            table.addRow(data);
            table.setValueAt(i + 1, i, 0);
            table.setValueAt(s.getId(), i, 1);
            table.setValueAt(s.getName(), i, 2);
            table.setValueAt(s.getAddress(), i, 3);
            table.setValueAt(s.getEmail(), i, 4);
        }
        JTable tblContacts = new JTable(table);
        frame.add(new JScrollPane(tblContacts));
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(700, 400));
        frame.setLocation(new Point(0, 100));
        frame.setVisible(true);
    }
}
