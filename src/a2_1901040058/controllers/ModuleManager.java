package a2_1901040058.controllers;

import a2_1901040058.DBHelper;
import a2_1901040058.models.CompulsoryModule;
import a2_1901040058.models.ElectiveModule;
import a2_1901040058.models.Module;
import a2_1901040058.models.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModuleManager extends Manager {
    private JComboBox combo;
    private JTextField jName, jSem, jCre, jDep;
    private ArrayList<Module> modules;
    Connection connection;
    Statement statement;

    public ModuleManager(String title, Connection connection) {
        super(title);
        modules = new ArrayList<>();
        this.connection = connection;
        try {
            statement = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            assert moduleType != null;
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
    }

    @Override
    public Object createObject() throws Exception {
        Module m;
        if ((Objects.equals(combo.getSelectedItem(), "Compulsory"))
                && (jName.getText().isEmpty() || jSem.getText().isEmpty() || jCre.getText().isEmpty())) {
            showErrorMessage("Invalid input! Please check again (filed with * are required)");
        } else if (Objects.equals(combo.getSelectedItem(), "Elective") && (jName.getText().isEmpty() || jSem.getText().isEmpty()
                || jCre.getText().isEmpty() || jDep.getText().isEmpty())) {
            showErrorMessage("Invalid input! Please check again (filed with * are required)");
        } else {
            String name = jName.getText();
            String dept = jDep.getText();
            int semester = Integer.parseInt(jSem.getText());
            int credits = Integer.parseInt(jCre.getText());
            if (Objects.equals(combo.getSelectedItem(), "Compulsory")) {
                m = new CompulsoryModule(name, semester, credits);
                DBHelper.createCompulsoryModule((CompulsoryModule) m);
            } else {
                m = new ElectiveModule(name, semester, credits, dept);
                DBHelper.createElectiveModule((ElectiveModule) m);
            }
            modules.add(m);
            showMessage("created module=" + m);
            gui.setVisible(false);
        }
        return modules;
    }

    public Module getModuleByCode(String code) {
        try {
            List<Module> modules = DBHelper.getAllModules();
            for (Module module : modules) {
                if (module.getCode().equalsIgnoreCase(code)) {
                    return module;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void showListModule() {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM modules");
            ResultSet rs = ps.executeQuery();
            JFrame frame = new JFrame("List of the modules");
            String[] headers = {"Module Code", "Module Name", "Module Credits", "Module Semester"};
            Object[][] data = {};
            DefaultTableModel table = new DefaultTableModel(data, headers);

            int i = 0;
            while (rs.next()) {
                table.addRow(data);
                String code = rs.getString("code");
                String name = rs.getString("name");
                String semester = rs.getString("semester");
                String credits = rs.getString("credits");
                table.setValueAt(code, i, 0);
                table.setValueAt(name, i, 1);
                table.setValueAt(credits, i, 2);
                table.setValueAt(semester, i, 3);
                i++;
            }
            JTable tblContacts = new JTable(table);
            frame.add(new JScrollPane(tblContacts));
            frame.setPreferredSize(new Dimension(700, 400));
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frame.setLocation(new Point(0, 100));
            frame.setVisible(true);
            frame.pack();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
