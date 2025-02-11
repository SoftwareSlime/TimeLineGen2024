package View;

import Controller.Main;
import Model.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;

public class calendarGUI extends JFrame {
    private YearMonth currentYearMonth;
    private JLabel monthLabel;
    private JPanel calendarPanel;

    public calendarGUI() {
        setTitle("Calendar Page");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        currentYearMonth = YearMonth.now();

        JPanel controlPanel = new JPanel();
        JButton previousButton = new JButton("Previous");

        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentYearMonth = currentYearMonth.minusMonths(1);
                updateCalendar();
            }
        });
        controlPanel.add(previousButton);

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentYearMonth = currentYearMonth.plusMonths(1);
                updateCalendar();
            }
        });
        controlPanel.add(nextButton);

        JButton goBackButton = new JButton("Go Back");
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                Main.controller.homePage();
            }
        });
        controlPanel.add(goBackButton);

        monthLabel = new JLabel();
        controlPanel.add(monthLabel);

        add(controlPanel, BorderLayout.NORTH);

        calendarPanel = new JPanel(new GridLayout(0, 7));
        add(calendarPanel, BorderLayout.CENTER);

        updateCalendar();
    }

    private void updateCalendar() {
        monthLabel.setText(currentYearMonth.getMonth().toString() + " " + currentYearMonth.getYear());
        calendarPanel.removeAll();

        LocalDate date = currentYearMonth.atDay(1);
        int offset = date.getDayOfWeek().getValue() % 7;
        int daysInMonth = currentYearMonth.lengthOfMonth();

        for (int i = 0; i < offset; i++) {
            calendarPanel.add(new JLabel(""));
        }

        for (int i = 1; i <= daysInMonth; i++) {
            LocalDate currentDate = LocalDate.of(currentYearMonth.getYear(), currentYearMonth.getMonth(), i);
            JButton dayButton = new JButton(Integer.toString(i));

            // Fetch tasks for the current date and display them
            ArrayList<Task> tasksForDate = Main.controller.getTasksForDate(currentDate);
            if (!tasksForDate.isEmpty()) {
                StringBuilder tooltip = new StringBuilder("<html>");
                for (Task task : tasksForDate) {
                    tooltip.append("Task: ").append(task.getName()).append("<br>Category: ").append(task.getCategory()).append("<br>");
                }
                tooltip.append("</html>");
                dayButton.setToolTipText(tooltip.toString());
                dayButton.setForeground(Color.BLUE); // Change the color of the button to indicate tasks available
            }

            dayButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    StringBuilder message = new StringBuilder("You clicked on: " + dayButton.getText() + "\nTasks:\n");
                    for (Task task : tasksForDate) {
                        message.append("Task: ").append(task.getName()).append("\nCategory: ").append(task.getCategory()).append("\n");
                    }
                    JOptionPane.showMessageDialog(calendarGUI.this, message.toString());
                }
            });
            calendarPanel.add(dayButton);
        }

        revalidate();
        repaint();
    }
}
