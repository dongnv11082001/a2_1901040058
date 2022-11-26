package a2_1901040058.controllers;

import a2_1901040058.models.CompulsoryModule;
import a2_1901040058.models.ElectiveModule;
import a2_1901040058.models.Module;
import a2_1901040058.models.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ModuleManager extends Manager {
    private JComboBox<String> combo;
    private JTextField jName, jSem, jCre, jDep;

    public ModuleManager(String title, String storageFile) {
        super(title, storageFile);
    }

    @Override
    public void createMiddlePanel() {
        gui = new JFrame("Create new module");
        gui.setSize(400, 300);
        gui.addWindowListener(this);
        gui.setLocation(900, 200);
        JPanel pnlTop = new JPanel();
        gui.add(pnlTop, BorderLayout.NORTH);
        JLabel lblTitle = new JLabel("Enter module details:");
        pnlTop.add(lblTitle);
        JPanel pnlMiddle3 = new JPanel();
        pnlMiddle3.setLayout(new GridLayout(1, 2));
        pnlMiddle3.setBorder(BorderFactory.createEtchedBorder());
        JPanel pnlMiddle = new JPanel(new BorderLayout());
        JPanel pnlCombo = new JPanel(new GridLayout(1, 2));
        String[] types = {"Compulsory", "Elective"};
        combo = new JComboBox<>(types);
        JLabel lblMtype = new JLabel("Module type: (*)");
        pnlCombo.add(lblMtype);
        pnlCombo.add(combo);
        pnlMiddle.add(pnlCombo, BorderLayout.NORTH);
        middlePanel = new JPanel(new GridLayout(0, 2));
        JLabel lblName = new JLabel("Name: (*)");
        JLabel lblSem = new JLabel("Semester: (*)");
        JLabel lblCre = new JLabel("Credits: (*)");
        JLabel lblDep = new JLabel("Department name: (*)");
        jName = new JTextField(20);
        jSem = new JTextField(20);
        jCre = new JTextField(20);
        jDep = new JTextField(20);
        lblDep.setVisible(false);
        jDep.setVisible(false);
        middlePanel.add(lblName);
        middlePanel.add(jName);
        middlePanel.add(lblSem);
        middlePanel.add(jSem);
        middlePanel.add(lblCre);
        middlePanel.add(jCre);
        middlePanel.add(lblDep);
        middlePanel.add(jDep);

        pnlMiddle.add(middlePanel, BorderLayout.CENTER);
        pnlMiddle3.add(pnlMiddle, BorderLayout.CENTER);

        combo.addActionListener(e -> {
            combo = (JComboBox) e.getSource();
            String moduleType = (String) combo.getSelectedItem();
            if (moduleType.equals("Elective")) {
                lblDep.setVisible(true);
                jDep.setVisible(true);
            } else {
                lblDep.setVisible(false);
                jDep.setVisible(false);
            }
        });

        gui.add(pnlMiddle3);

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
        Module m = null;
        if ((combo.getSelectedItem().equals("Compulsory"))
                && (jName.getText().isEmpty() || jSem.getText().isEmpty() || jCre.getText().isEmpty())) {
            showErrorMessage("Invalid input! Please check again (filed with * are required)");

        } else if (combo.getSelectedItem().equals("Elective") && (jName.getText().isEmpty() || jSem.getText().isEmpty()
                || jCre.getText().isEmpty() || jDep.getText().isEmpty())) {
            showErrorMessage("Invalid input! Please check again (filed with * are required)");

        } else {
            String name = jName.getText();
            String dept = jDep.getText();
            int semester = Integer.parseInt(jSem.getText());
            int credits = Integer.parseInt(jCre.getText());

            if (combo.getSelectedItem().equals("Compulsory")) {
                m = new CompulsoryModule(name, semester, credits);
            } else {
                m = new ElectiveModule(name, semester, credits, dept);
            }
            objects.add(m);
            showMessage("created module=" + m.toString());
            gui.setVisible(false);
        }

        return null;
    }

    public Module getModuleByCode(String code) {
        Module module = null;
        for (Object object : objects) {
            Module m = (Module) object;
            if (m.getCode().equalsIgnoreCase(code)) {
                module = m;
            }
        }
        return module;
    }

    public void showListModule() {
        JFrame frame = new JFrame("List of the modules");
        String[] headers = {"#", "Module ID", "Module Code", "Module Code", "Module Credits", "Module Semester"};
        Object[][] data = {};
        DefaultTableModel table = new DefaultTableModel(data, headers);
        for (int i = 0; i < objects.size() - 1; i++) {
            Module s = (Module) objects.get(i);
            table.addRow(data);
            table.setValueAt(i + 1, i, 0);
            table.setValueAt(s.getCode(), i, 1);
            table.setValueAt(s.getName(), i, 2);
            table.setValueAt(s.getCredits(), i, 3);
            table.setValueAt(s.getSemester(), i, 4);
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
