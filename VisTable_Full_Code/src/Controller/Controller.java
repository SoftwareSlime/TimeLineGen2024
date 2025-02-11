package Controller;

import Model.Task;
import Model.User;
import View.*;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class Controller {
    logInGUI login;
    signUpGUI signup;
    homePageGUI homepage;
    ToDoGUI toDoGUI;
    ganttGUI ganttGUI;
    calendarGUI CalendarGUI;
    static User loggedIn;
    ArrayList<User> userList;

    public boolean ValidUser(String username, String password) {
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getUsername().equals(username) &&
                    userList.get(i).getPassword().equals(password)) {
                loggedIn = userList.get(i);
                System.out.println(loggedIn.getUsername());
                System.out.println(loggedIn.getPassword());
                System.out.println(loggedIn.getEmail());
                System.out.println(loggedIn.getFirstName());
                return true;
            }
        }
        return false;
    }

    public static User getLoggedInUser(){
        return loggedIn;
    }

    public void logIn() {
        login = new logInGUI();
        login.setVisible(true);
        userList = deserialize();
    }

    public void signUp() {
        signup = new signUpGUI();
        signup.setVisible(true);
    }

    public void homePage() {
        homepage = new homePageGUI();
        homepage.setVisible(true);
    }

    public void todoGui() {
        toDoGUI = new ToDoGUI();
        toDoGUI.setVisible(true);
    }

    public void ganttGUI(){
        ganttGUI = new ganttGUI();
        ganttGUI.setVisible(true);
    }

    public void setCalendarGUI(){
        CalendarGUI = new calendarGUI();
        CalendarGUI.setVisible(true);
    }

    public void setUser(User user) {
        this.loggedIn = user;
    }

    public void addUser(User user) {
        userList.add(user);
        serialize(userList);
    }

    public void updateUser(User user) {
        userList.add(user);
        serialize(userList);
    }

    public void serialize(ArrayList<User> myList) {
        try {
            FileOutputStream fileOut = new FileOutputStream("People.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(myList);
            out.close();
            fileOut.close();
            System.out.println("Serialized data saved");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public ArrayList<User> deserialize() {
        ArrayList<User> myList = new ArrayList<>();
        try {
            FileInputStream fileIn = new FileInputStream("People.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            myList = (ArrayList<User>) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("Data read successfully.");
            return myList;
        } catch (IOException i) {
            i.printStackTrace();
            return myList;
        } catch (ClassNotFoundException c) {
            System.out.println("Save data not found");
            c.printStackTrace();
            return myList;
        }
    }

    public ArrayList<Task> getTasksForDate(LocalDate date) {
        ArrayList<Task> tasksForDate = new ArrayList<>();
        String filePath = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/todo.csv";
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] taskDetails = line.split(",");
                if (taskDetails.length >= 3) { // Ensure there's a category field
                    try {
                        Date taskDate = dateFormat.parse(taskDetails[1]);
                        LocalDate taskLocalDate = LocalDate.of(taskDate.getYear() + 1900, taskDate.getMonth() + 1, taskDate.getDate());
                        if (taskLocalDate.equals(date)) {
                            tasksForDate.add(new Task(taskDetails[0], taskDetails[2])); // Assume category is the third column
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tasksForDate;
    }
}
