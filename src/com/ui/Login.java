package com.ui;

import com.DAO.UserDAO;
import com.entity.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Login extends JFrame {
    private JTextField userNameInput;
    private JLabel userNameLabel;
    private JLabel passwordLabel;
    private JButton signInButton;
    private JButton closeButton;
    private JPasswordField passwordInput;

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
