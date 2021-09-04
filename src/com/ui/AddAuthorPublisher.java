package com.ui;

import com.DAO.AuthorDAO;
import com.DAO.PublisherDAO;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddAuthorPublisher extends JFrame {
    private JPanel panel1;
    private JTextField nameInput;
    private JTextField addressInput;
    private JButton cancelButton;
    private JButton addButton;

    private final String type;

    public static void main(String[] args) throws Exception {
        new AddAuthorPublisher("");
    }

    public AddAuthorPublisher(String type) {
        this.type = type;
        this.setupUI();
    }

    private void updateAddButtonText() {

        if (type.equalsIgnoreCase("author")) {
            addButton.setText("Add Author");
        } else {
            addButton.setText("Add Publisher");
        }
    }

    private void setupUI() {
        this.setContentPane(panel1);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.pack();
        this.setSize(475, 135);
        setLocationRelativeTo(null);
        this.setVisible(true);

        this.updateAddButtonText();
        this.bindAddListener();
        this.bindCancelListener();
    }

    private void bindCancelListener() {
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void bindAddListener() {
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameInput.getText().trim();
                String address = addressInput.getText().trim();

                try {

                    if (name.isEmpty()) {
                        JOptionPane.showMessageDialog(AddAuthorPublisher.this, "Name should not be empty");
                        return;
                    }

                    if (address.isEmpty()){
                        JOptionPane.showMessageDialog(AddAuthorPublisher.this, "Address should not be empty");
                        return;
                    }

                    if (type.equalsIgnoreCase("author")) {
                        AuthorDAO.getInstance().addAuthor(name, address);
                        JOptionPane.showMessageDialog(AddAuthorPublisher.this, "Author was added successfully");
                    } else {
                        PublisherDAO.getInstance().addPublisher(name, address);
                        JOptionPane.showMessageDialog(AddAuthorPublisher.this, "Publisher was added successfully");
                    }

                    dispose();

                } catch (Exception exception) {
                    exception.printStackTrace();
                }


            }
        });
    }
}
