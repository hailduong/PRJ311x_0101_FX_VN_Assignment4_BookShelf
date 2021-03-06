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
import java.awt.event.*;
import java.util.List;

public class MyBook extends JFrame {
    private JTable bookTable;
    private JPanel myBookPanel;
    private JPanel searchBookPanel;
    private JLabel searchByLabel;
    private JComboBox searchOptionCombo;
    private JTextField keywordInput;
    private JButton searchButton;
    private JLabel keywordLabel;
    private JButton addBookButton;
    private JLabel welcomeLabel;
    private JButton addAuthorButton;
    private JButton addPublisherButton;

    public User user;

    public static void main(String[] args) throws Exception {
        new MyBook();
    }

    public MyBook() throws Exception {
        this.setupUI();
        this.bookController = new BookController();
    }

    private void bindAddAuthorButtonListener() {
        addAuthorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddAuthorPublisher("author");
            }
        });
    }

    private void bindAddPublisherButtonListener() {
        addPublisherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddAuthorPublisher("publisher");
            }
        });
    }

    private void bindTableCellListener() {
        bookTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                handleTableRowDoubleClick(e);
            }
        });
    }

    private void bindSearchActionListener() {
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSearch();
            }
        });
    }

    private void bindAddNewEventListener() {
        addBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAddNew(e);
            }
        });
    }

    private void setupUI() throws Exception {
        this.setContentPane(myBookPanel);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.renderComboBoxOptions();
        this.showAllBooks();
        this.pack();
        setLocationRelativeTo(null);
        this.setVisible(true);


        this.bindSearchActionListener();
        this.bindAddNewEventListener();
        this.bindTableCellListener();
        this.bindAddPublisherButtonListener();
        this.bindAddAuthorButtonListener();
        this.clearKeywordInputPlacholderOnFocus();
    }

    public void clearKeywordInputPlacholderOnFocus() {
        keywordInput.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if (keywordInput.getText().trim().equalsIgnoreCase("Input anything...")) {
                    keywordInput.setText("");
                }
            }
        });
    }

    public void setUser(User user) {
        this.user = user;
        welcomeLabel.setText("Welcome " + user.userName);
    }

    DefaultTableModel tableModel;
    BookController bookController;

    public void searchBooks() {
        try {
            String[] columns = {"book.id", "book.title", "publisher.name", "author.name", "book.notes"};
            String columnName = columns[searchOptionCombo.getSelectedIndex()];
            String keyword = keywordInput.getText();

            // Search and output the result
            List<Book> bookList = bookController.search(columnName, keyword);
            this.renderBookTable(bookList);
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

    public void handleSearch() {
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
            int bookId = Integer.parseInt((String) tableModel.getValueAt(row, 0));
            EditBook editBook = new EditBook(this, bookId);
            editBook.setVisible(true);
        }
    }

    private void renderComboBoxOptions() {
        String[] options = {"Book ID", "Book Title", "Publisher Name", "Author Name", "Notes"};
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(options);
        searchOptionCombo.setModel(model);
    }

    public void showAllBooks() throws Exception {
        List<Book> bookList = BookDAO.getInstance().getAllBooks();
        this.renderBookTable(bookList);
    }

    private void renderBookTable(List<Book> bookList) throws Exception {

        // Clear data
        if (tableModel != null) tableModel.setColumnCount(0);

        // Process new data
        Object[][] dataList = new String[bookList.size()][5];
        int index = 0;
        for (Book book : bookList) {
            dataList[index] = book.toDataRow();
            index++;
        }

        String[] columnNames = {"Book ID", "Book Title", "Publisher", "Authors", "Notes"};
        //  Make cell un-editable
        tableModel = new DefaultTableModel(dataList, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Set the model
        bookTable.setModel(tableModel);

        // Resize the column widths
        resizeColumnWidth(bookTable);
    }

}
