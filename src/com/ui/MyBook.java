package com.ui;

import com.DAO.BookDAO;
import com.controller.BookController;
import com.entity.Book;
import com.entity.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyBook extends JFrame {
    private JTable bookTable;
    private JPanel myBookPanel;
    private JPanel searchBookPanel;
    private JLabel searchByLabel;
    private JComboBox searchOptionCombo;
    private JTextField keywordInput;
    private JButton searchButton;
    private JLabel keywordLabel;
    private JButton addNewButton;
    private JLabel welcomeLabel;

    public User user;

    public static void main(String[] args) throws Exception {
        new MyBook();
    }

    public MyBook() throws Exception {
        this.setupUI();
    }

    private void setupUI() throws Exception {
        this.setContentPane(myBookPanel);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.renderComboBoxOptions();
        this.renderBookTable();
        this.pack();
        setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void setUser(User user) {
        this.user = user;
        welcomeLabel.setText("Welcome " + user.userName);
    }

    DefaultTableModel tableModel;
    BookController bookController;

    public void showAllBooks() throws Exception {
        bookController = new BookController();
        try {
            bookController.listBooks(bookTable);
        } catch (Exception exception) {
            Logger.getLogger(MyBook.class.getName()).log(Level.SEVERE, null, exception);
        }
    }

    public void searchBooks() {
        try {
            String[] columns = {"book.id", "book.title", "publisher.name", "author.name", "book.notes"};
            String columnName = columns[searchOptionCombo.getSelectedIndex()];
            String keyword = keywordInput.getText();

            // Search and output the result
            bookController.search(columnName, keyword, bookTable);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        if (tableModel.getRowCount() <= 0) {
            JOptionPane.showMessageDialog(MyBook.this, "Books not found");
        }
    }

    public void resizeColumnWidth(JTable table) {
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 45;
            for (int row = 1; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component component = table.prepareRenderer(renderer, row, column);
                width = Math.max(component.getPreferredSize().width + 1, width);
            }
            if (width > 150) {
                width = 150;
            }
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }

    public int getSelectedBookId() {
        int row = bookTable.getSelectedRow();
        int column = 0;
        return Integer.parseInt(bookTable.getValueAt(row, column).toString());
    }

    private void handleSearch(ActionEvent event) {
        searchBooks();
    }

    private void handleAddNew(ActionEvent event) {
        AddBook addBook = new AddBook(this);
        addBook.setVisible(true);
    }

    private void handleTableRowDoubleClick(MouseEvent event) {
        JTable table = (JTable) event.getSource();
        Point point = event.getPoint();
        int row = table.rowAtPoint(point);
        if (event.getClickCount() == 2 && row != -1) {
            EditBook editBook = new EditBook(this);
            editBook.setVisible(true);
        }
    }

    private void renderComboBoxOptions() {
        String[] options = {"Book ID", "Book Title", "Publisher Name", "Author Name", "Notes"};
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(options);
        searchOptionCombo.setModel(model);
    }

    private void renderBookTable() throws Exception {

        List<Book> bookList = BookDAO.getInstance().getAllBooks();
        Object[][] dataList = new String[bookList.size()][5];
        int index = 0;
        for (Book book : bookList) {
            dataList[index] = book.toDataRow();
            index++;
        }

        String[] columnNames = {"Book ID", "Book Title", "Publisher", "Authors", "Notes"};
        tableModel = new DefaultTableModel(dataList, columnNames);
        bookTable.setModel(tableModel);
    }

}
