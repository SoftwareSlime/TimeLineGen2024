package View;

import Controller.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class logInGUI extends JFrame {
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JTextField usernameTextField;
    private JPasswordField passwordTextField;
    private JButton loginButton;
    private JButton SignupButton;
    private JButton ForgotPassButton;

    public logInGUI() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Login Page! ");
        this.setContentPane(mainPanel);
        this.pack();
        usernameTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });


        passwordTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (usernameTextField.getText().equals("") || passwordTextField.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Error, please fill both username and password fields and try again.");
                } else {
                    String input = passwordTextField.getText();
                    if (Main.controller.ValidUser(usernameTextField.getText(), input)) {
                        JOptionPane.showMessageDialog(null, "Sign In Successful " + usernameTextField.getText());
                        setVisible(false);
                        Main.controller.homePage();
                    } else {
                        JOptionPane.showMessageDialog(null, "Wrong username or password, try again.");
                    }

                }
            }
        });

        ForgotPassButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Yet to be added!");
            }
        });

        SignupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                Main.controller.signUp();
            }
        });
    }
}