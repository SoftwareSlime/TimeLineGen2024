package View;

import Controller.Main;
import Model.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class signUpGUI extends JFrame{
    private JLabel signupLabel;
    private JLabel firstNameLabel;
    private JLabel surnameLabel;
    private JLabel ageLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JTextField firstNameTextField;
    private JTextField surnameTextField;
    private JTextField ageTextField;
    private JTextField emailTextField;
    private JTextField usernameTextField;
    private JPasswordField passwordTextField;
    private JButton signUpButton;
    private JButton goBackButton;
    private JPanel signUpPanel;
    private JPanel invisJPanel;

    public signUpGUI() {
    this.setTitle("Sign Up!");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setContentPane(signUpPanel);
    this.pack();

    firstNameTextField.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    });
    surnameTextField.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    });
    ageTextField.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    });
    /*emailTextField.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    });*/
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

    signUpButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (firstNameTextField.getText().equals("") ||
            surnameTextField.getText().equals("") || ageTextField.getText() == null
            //|| emailTextField.getText().equals("")
            || usernameTextField.getText().equals("")
            || passwordTextField.getText().equals("")
             )
            {
                JOptionPane.showMessageDialog(null, "Error, please fill all details and try again.");
            } else {
                String FirstName = firstNameTextField.getText();
                String Surname = surnameTextField.getText();
                int Age = Integer.parseInt(ageTextField.getText());
                String Email = emailTextField.getText();
                String Username = usernameTextField.getText();
                String Password = passwordTextField.getText();

               User newUser = new User(Email, Password, FirstName, Surname, Age, Username);;

                Main.controller.addUser(newUser);
                setVisible(false);
                Main.controller.logIn();
            }
        }
    });

        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                Main.controller.logIn();
            }
        });
    }


}
