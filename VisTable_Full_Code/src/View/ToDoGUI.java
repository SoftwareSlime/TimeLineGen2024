package View;

import Controller.Main;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ToDoGUI extends JFrame {

    private DefaultTableModel model;
    private JTable table;
    private JButton addTaskButton, editTaskButton, deleteTaskButton, saveTaskButton, goBackButton, importCSVButton;
    private JTextField taskNameField, taskCategoryField;
    private JSpinner taskDateSpinner;
    private boolean isEditing = false;

    private static final String FILE_PATH = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/todo.csv";

    public ToDoGUI() {
        setTitle("Todo List");
        setSize(700, 400); // Adjusted size to accommodate the new button
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        model = new DefaultTableModel(new Object[]{"Task Name", "Due Date", "Category"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return isEditing;
            }
        };

        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        addTaskButton = new JButton("Add Task");
        editTaskButton = new JButton("Edit Task");
        deleteTaskButton = new JButton("Delete Task");
        saveTaskButton = new JButton("Save Changes");
        goBackButton = new JButton("Go Back");
        importCSVButton = new JButton("Import CSV"); // New button for importing CSV

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addTaskButton);
        buttonPanel.add(editTaskButton);
        buttonPanel.add(deleteTaskButton);
        buttonPanel.add(saveTaskButton);
        buttonPanel.add(goBackButton);
        buttonPanel.add(importCSVButton); // Added import CSV button to the panel

        taskNameField = new JTextField(15);
        taskCategoryField = new JTextField(10);

        taskDateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(taskDateSpinner, "MM/dd/yyyy");
        taskDateSpinner.setEditor(dateEditor);
        taskDateSpinner.setValue(new Date());

        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.add(new JLabel("Task Name:"));
        inputPanel.add(taskNameField);
        inputPanel.add(new JLabel("Due Date:"));
        inputPanel.add(taskDateSpinner);
        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(taskCategoryField);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(inputPanel, BorderLayout.NORTH);

        addTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTask();
            }
        });

        editTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleEditing();
            }
        });

        saveTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveTasksToFile();
            }
        });

        deleteTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteTask();
            }
        });

        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                Main.controller.homePage();
            }
        });

        importCSVButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                importCSV();
            }
        });

        loadTasksFromFile();
    }

    private void addTask() {
        String taskName = taskNameField.getText();
        Date taskDate = (Date) taskDateSpinner.getValue();
        String taskCategory = taskCategoryField.getText();

        if (taskName.isEmpty() || taskCategory.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Task Name and Category cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = dateFormat.format(taskDate);

        model.addRow(new Object[]{taskName, formattedDate, taskCategory});

        taskNameField.setText("");
        taskCategoryField.setText("");
    }

    private void toggleEditing() {
        isEditing = !isEditing;
        if (isEditing) {
            editTaskButton.setText("Stop Editing");
        } else {
            editTaskButton.setText("Edit Task");
            saveTasksToFile();  // Save changes when stopping editing mode
        }
        model.fireTableStructureChanged();
    }

    private void deleteTask() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            model.removeRow(selectedRow);
            saveTasksToFile();  // Save changes immediately after deleting a task
        }
    }

    private Object[][] getData() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(FILE_PATH));
            ArrayList<String> list = new ArrayList<>();
            String str;
            while ((str = br.readLine()) != null) {
                list.add(str);
            }
            br.close();
            int n = list.get(0).split(",").length; // Using "," as the breaking point for data and extracting
            Object[][] data = new Object[list.size()][n];
            for (int i = 0; i < list.size(); i++) {
                data[i] = list.get(i).split(",");
            }
            return data;
        } catch (Exception x) {
            x.printStackTrace();
            return null;
        }
    }

    private void loadTasksFromFile() {
        Object[][] data = getData();
        if (data != null) {
            for (Object[] row : data) {
                model.addRow(row);
            }
        }
    }

    private void saveTasksToFile() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH));
            for (int i = 0; i < model.getRowCount(); i++) {
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < model.getColumnCount(); j++) {
                    sb.append(model.getValueAt(i, j) != null ? model.getValueAt(i, j).toString() : "");
                    if (j < model.getColumnCount() - 1) {
                        sb.append(",");
                    }
                }
                bw.write(sb.toString());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void importCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
                String line;
                model.setRowCount(0); // Clear existing data
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length == 3) { // Ensure the CSV has the correct number of columns
                        model.addRow(data);
                    } else {
                        JOptionPane.showMessageDialog(this, "CSV file format is incorrect.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                saveTasksToFile(); // Save imported data to default file
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error reading CSV file.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}
