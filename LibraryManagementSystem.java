import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LibraryManagementSystem extends JFrame implements ActionListener {
    private JLabel label1, label2, label3, label4, label5, label6, label7;
    private JTextField textField1, textField2, textField3, textField4, textField5, textField6, textField7;
    private JButton addButton, viewButton, editButton, deleteButton, clearButton, exitButton;
    private JPanel panel;
    private Connection connection;

    public LibraryManagementSystem() {
        setTitle("Library Management System");
        setSize(600, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        label1 = new JLabel("Book ID");
        label2 = new JLabel("Book Title");
        label3 = new JLabel("Author");
        label4 = new JLabel("Publisher");
        label5 = new JLabel("Year of Publication");
        label6 = new JLabel("ISBN");
        label7 = new JLabel("Number of Copies");

        textField1 = new JTextField(10);
        textField2 = new JTextField(20);
        textField3 = new JTextField(20);
        textField4 = new JTextField(20);
        textField5 = new JTextField(10);
        textField6 = new JTextField(20);
        textField7 = new JTextField(10);

        addButton = new JButton("Add");
        viewButton = new JButton("View");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        clearButton = new JButton("Clear");
        exitButton = new JButton("Exit");

        addButton.addActionListener(this);
        viewButton.addActionListener(this);
        editButton.addActionListener(this);
        deleteButton.addActionListener(this);
        clearButton.addActionListener(this);
        exitButton.addActionListener(this);

        panel = new JPanel(new GridLayout(10, 2));
        panel.add(label1);
        panel.add(textField1);
        panel.add(label2);
        panel.add(textField2);
        panel.add(label3);
        panel.add(textField3);
        panel.add(label4);
        panel.add(textField4);
        panel.add(label5);
        panel.add(textField5);
        panel.add(label6);
        panel.add(textField6);
        panel.add(label7);
        panel.add(textField7);
        panel.add(addButton);
        panel.add(viewButton);
        panel.add(editButton);
        panel.add(deleteButton);
        panel.add(clearButton);
        panel.add(exitButton);

        add(panel);
        setVisible(true);

        // Establish database connection
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library_db", "root", "0sS@0210");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            try {
                String query = "INSERT INTO books VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, textField1.getText());
                preparedStatement.setString(2, textField2.getText());
                preparedStatement.setString(3, textField3.getText());
                preparedStatement.setString(4, textField4.getText());
                preparedStatement.setString(5, textField5.getText());
                preparedStatement.setString(6, textField6.getText());
                preparedStatement.setString(7, textField7.getText());
                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(this, "Book added successfully");
                clearFields();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        } else if (e.getSource() == viewButton) {
            try {
                String query = "SELECT * FROM books";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                StringBuilder resultText = new StringBuilder();
                while (resultSet.next()) {
                    resultText.append("Book_ID:").append(resultSet.getString(1)).append("\n");
                    resultText.append("Book_Title:").append(resultSet.getString(2)).append("\n");
                    resultText.append("Author:").append(resultSet.getString(3)).append("\n");
                    resultText.append("Publisher:").append(resultSet.getString(4)).append("\n");
                    resultText.append("Year_of_Publication:").append(resultSet.getString(5)).append("\n");
                    resultText.append("ISBN:").append(resultSet.getString(6)).append("\n");
                    resultText.append("Number_of_Copies:").append(resultSet.getString(7)).append("\n\n");
                }
                JOptionPane.showMessageDialog(this, resultText.toString(), "Books List", JOptionPane.PLAIN_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        } else if (e.getSource() == deleteButton) {
            try {
                String bookIDToDelete = textField1.getText();
                String query = "DELETE FROM books WHERE book_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, bookIDToDelete);
                int rowsDeleted = preparedStatement.executeUpdate();
                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(this, "Book deleted successfully");
                } else {
                    JOptionPane.showMessageDialog(this, "No book found with the specified ID");
                }
                clearFields();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        } else if (e.getSource() == clearButton) {
            clearFields();
        } else if (e.getSource() == exitButton) {
            System.exit(0);
        }
    }

    private void clearFields() {
        textField1.setText("");
        textField2.setText("");
        textField3.setText("");
        textField4.setText("");
        textField5.setText("");
        textField6.setText("");
        textField7.setText("");
    }

    public static void main(String[] args) {
        new LibraryManagementSystem();
    }
}
