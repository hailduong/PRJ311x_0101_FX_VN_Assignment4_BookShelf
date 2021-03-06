package com.ui;

import com.DAO.UserDAO;
import com.entity.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {
    private JTextField userNameInput;
    private JLabel userNameLabel;
    private JLabel passwordLabel;
    private JButton signInButton;
    private JButton closeButton;
    private JPasswordField passwordInput;
    private JPanel loginPanel;

    public static void main(String[] args) {
        new Login();
    }

    public Login() {
        this.setupUI();
    }

    private void setupUI() {
        this.setContentPane(loginPanel);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.pack();
        setLocationRelativeTo(null);
        this.setVisible(true);

        this.addActionListenerToCloseButton();
        this.addActionListenerToSignInButton();
    }

    private void addActionListenerToSignInButton() {
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                handleSignIn(event);
            }
        });
    }

    private void addActionListenerToCloseButton() {
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void handleSignIn(ActionEvent event) {
        try {
            String userName = userNameInput.getText();
            String password = new String(passwordInput.getPassword());

            User user = new User(userName, password);
            if (UserDAO.getInstance().validateUser(user)) {
                // Show the main screen
                MyBook myBook = new MyBook();
                myBook.setUser(user);
                myBook.setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(Login.this, "Username or password is incorrect");
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
