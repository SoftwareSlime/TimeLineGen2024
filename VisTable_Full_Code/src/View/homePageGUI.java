package View;

import Controller.Main;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class homePageGUI extends JFrame {

    private JPanel homepagePanel;
    private JButton ganttButton;
    private JButton toDoButton;
    private JPanel descriptionJPanel;
    private JPanel timeLineDisplayJPanel;
    private JTextArea welcomeToAppYourTextArea;
    private JButton signOutButton;
    private JButton calendarButton;
    private static final String FILE_PATH = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/todo.csv";

    public homePageGUI() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(homepagePanel);
        this.pack();

        // Initialize the timeline display panel with a BoxLayout
        timeLineDisplayJPanel.setLayout(new BoxLayout(timeLineDisplayJPanel, BoxLayout.Y_AXIS));

        signOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                Main.controller.logIn();
            }
        });
        toDoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                Main.controller.todoGui();
            }
        });
        ganttButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                Main.controller.ganttGUI();
            }
        });

        calendarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                Main.controller.setCalendarGUI();
            }
        });



        // Print the current working directory
        System.out.println("Current working directory: " + System.getProperty("user.dir"));

        // Initialize the timeline display on homepage load
        generateTimelineDisplayFromCSV();
    }

    private void generateTimelineDisplayFromCSV() {
        // Clear the existing timeline display
        timeLineDisplayJPanel.removeAll();

        // Read data from CSV and generate timeline display
        String line;
        String cvsSplitBy = ",";
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        List<Task> tasks = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split(cvsSplitBy);
                if (data.length >= 2) { // Ensure there are at least 2 elements
                    String eventName = data[0];
                    String eventDate = data[1];

                    // Parse date to validate and format
                    Date date = dateFormat.parse(eventDate);
                    tasks.add(new Task(eventName, date));
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        // Sort tasks by date
        tasks.sort((t1, t2) -> t1.getDate().compareTo(t2.getDate()));

        // Display tasks in the sorted order
        for (Task task : tasks) {
            String formattedDate = new SimpleDateFormat("MMM dd, yyyy").format(task.getDate());
            JLabel eventLabel = new JLabel(task.getName() + " - " + formattedDate);
            eventLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            timeLineDisplayJPanel.add(eventLabel);
        }

        // Refresh the panel to reflect changes
        timeLineDisplayJPanel.revalidate();
        timeLineDisplayJPanel.repaint();
    }


    private static class Task {
        private final String name;
        private final Date date;
        private final String category;

        public Task(String name, Date date) {
            this(name, date, "");
        }

        public Task(String name, Date date, String category) {
            this.name = name;
            this.date = date;
            this.category = category;
        }

        public String getName() {
            return name;
        }

        public Date getDate() {
            return date;
        }

        public String getCategory() {
            return category;
        }
    }

}
