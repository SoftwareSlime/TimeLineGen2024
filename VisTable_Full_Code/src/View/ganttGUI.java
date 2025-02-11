package View;

import Controller.Main;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ganttGUI extends JFrame {

    private DefaultTableModel model;
    private JTable table;
    private int weekCount = 5; // Initial number of weeks

    public ganttGUI() {
        setTitle("Gantt Chart");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make all cells editable except the top-left cell
                return true;
            }
        };

        table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setDefaultRenderer(JButton.class, new ButtonRenderer());
        table.setDefaultEditor(JButton.class, new ButtonEditor());

        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        createInitialTable();

        JButton addRowButton = new JButton("+ Add Row");
        addRowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRow();
            }
        });

        JButton addColumnButton = new JButton("+ Add Column");
        addColumnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addColumn();
            }
        });

        JButton removeRowButton = new JButton("- Remove Row");
        removeRowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeRow();
            }
        });

        JButton removeColumnButton = new JButton("- Remove Column");
        removeColumnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeColumn();
            }
        });

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetTable();
            }
        });

        JButton goBackButton = new JButton("Go Back");
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                Main.controller.homePage();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(addRowButton);
        buttonPanel.add(addColumnButton);
        buttonPanel.add(removeRowButton);
        buttonPanel.add(removeColumnButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(goBackButton);

        getContentPane().add(buttonPanel, BorderLayout.NORTH);
    }

    private void createInitialTable() {
        // Adding rows and columns
        for (int i = 0; i < weekCount; i++) {
            model.addColumn(i == 0 ? "Task Name" : "Week " + i); // Renameable first row
            model.addRow(new Object[]{i == 0 ? "" : "", "", "", "", ""}); // Renameable first column
        }

        // Set custom cell renderer for row and column headers
        table.getTableHeader().setDefaultRenderer(new UneditableCellRenderer());

        // Make the top left cell uneditable and grey
        table.getColumnModel().getColumn(0).setCellRenderer(new UneditableCellRenderer());
        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.setRowHeight(30);
    }

    private void addRow() {
        model.addRow(new Object[]{});
    }

    private void addColumn() {
        weekCount++;
        model.addColumn("Week " + weekCount);
    }

    private void removeRow() {
        if (model.getRowCount() > 1) {
            model.removeRow(model.getRowCount() - 1);
        }
    }

    private void removeColumn() {
        if (model.getColumnCount() > 1) {
            weekCount--;
            model.setColumnCount(weekCount);
        }
    }

    private void resetTable() {
        model.setRowCount(0); // Clear all rows
        model.setColumnCount(0); // Clear all columns
        weekCount = 5; // Reset week count
        createInitialTable(); // Recreate initial table
    }

    // Renderer for uneditable cells
    class UneditableCellRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            c.setBackground(Color.LIGHT_GRAY);
            c.setForeground(Color.BLACK);
            return c;
        }
    }

    // Custom renderer for buttons
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
            }
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    // Custom editor for buttons
    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;

        public ButtonEditor() {
            super(new JTextField());
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(table.getBackground());
            }
            button.setText((value == null) ? "" : value.toString());
            return button;
        }

        public Object getCellEditorValue() {
            return button.getText();
        }
    }

}

