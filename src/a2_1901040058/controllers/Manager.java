package a2_1901040058.controllers;

import a2_1901040058.exceptions.NotPossibleException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public abstract class Manager extends WindowAdapter implements ActionListener {
    protected String title;
    protected ArrayList<Object> objects;
    protected JFrame gui;
    protected JPanel middlePanel;

    public Manager(String title) {
        this.title = title;
        objects = new ArrayList<>();
        createGUI();
    }

    protected void createGUI() {
        createMiddlePanel();
    }

    protected abstract void createMiddlePanel();

    public void display() {
        gui.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("OK")) {
            try {
                doTask();
                clearInput(middlePanel);
            } catch (NotPossibleException ex) {
                System.out.println(ex.getMessage());
            }
        } else if (cmd.equals("Cancel")) {
            clearInput(middlePanel);
        } else {
            clearInput(middlePanel);
        }
    }

    protected void doTask() {
        try {
            Object object = createObject();
            if (object != null) {
                objects.add(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract Object createObject() throws Exception;

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(gui, message, this.title, JOptionPane.INFORMATION_MESSAGE);
    }

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(gui, message, this.title, JOptionPane.ERROR_MESSAGE);
    }

    private void clearInput(JPanel panel) {
        for (Component component : panel.getComponents()) {
            if (component instanceof JTextField) {
                ((JTextField) component).setText(null);
            } else if (component instanceof JPanel) { // clear input in the inner panels (if any)
                clearInput((JPanel) component);
            }
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        gui.setVisible(false);
    }

    public void startUp() {
        System.out.println("Starting up...");
    }

    public void shutDown() {
        System.out.println("Shutting down...");
    }
}
