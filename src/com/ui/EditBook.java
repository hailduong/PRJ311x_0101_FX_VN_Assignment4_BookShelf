package com.ui;

import com.DAO.AuthorDAO;
import com.DAO.BookDAO;
import com.DAO.PublisherDAO;
import com.entity.Author;
import com.entity.Book;
import com.entity.Publisher;
import com.entity.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;

public class EditBook extends JFrame {
    private JPanel mainPanel;
    private JTextField bookIdInput;
    private JTextField bookTitleInput;
    private JComboBox publisherComboBox;
    private JList availableAuthorList;
    private JTextArea noteTextArea;
    private JButton addButton;
    private JButton removeButton;
    private JList selectedAuthorList;
    private JButton updateButton;
    private JButton closeButton;
    private JButton deleteButton;
    private final MyBook myBook;

    private final DefaultComboBoxModel<Publisher> modelPublishers = new DefaultComboBoxModel<>();
    private final DefaultListModel<Author> modelAvailableAuthor = new DefaultListModel<>();
    private final DefaultListModel<Author> modelSelectedAuthor = new DefaultListModel<>();

    private int oldBookId;

    public EditBook(MyBook myBook, int bookId) {

        this.myBook = myBook;
        oldBookId = bookId;

        this.setupUI();
        this.listPublishers();
        this.listAuthors();
        this.showBook(bookId);

        this.bindAddButtonListener();
        this.bindRemoveButtonListener();
        this.bindCloseButtonListener();
        this.bindDeleteButtonListener();
        this.bindUpdateButtonListener();
    }

    private void bindUpdateButtonListener() {
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSave(e);
            }
        });
    }

    private void bindDeleteButtonListener() {
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleDelete(e);
            }
        });
    }

    private void bindCloseButtonListener() {
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void bindRemoveButtonListener() {
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRemoveAuthorFromList();
            }
        });
    }

    private void bindAddButtonListener() {
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAddAuthorFromList();
            }
        });
    }

    private void setupUI() {
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.pack();
        this.setSize(720, 480);
        setLocationRelativeTo(null);
        this.setVisible(true);

        // Init the combobox models
        availableAuthorList.setModel(modelAvailableAuthor);
        selectedAuthorList.setModel(modelSelectedAuthor);

    }

    public void listAuthors() {
        try {
            List<Author> authorList = AuthorDAO.getInstance().getAllAuthors();
            for (Author author : authorList) {
                modelAvailableAuthor.addElement(author);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void listPublishers() {
        try {
            List<Publisher> publisherList = PublisherDAO.getInstance().getAllPublishers();
            for (Publisher publisher : publisherList) {
                modelPublishers.addElement(publisher);
            }

            publisherComboBox.setModel(modelPublishers);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void showBook(int bookId) {
        try {
            Book book = myBook.bookController.getBookById(bookId);
            bookIdInput.setText(String.valueOf(book.id));
            bookTitleInput.setText(book.title);
            noteTextArea.setText(book.notes);
            publisherComboBox.setSelectedItem(book.getPublisher());
            List<Author> authorList = book.getAuthors();
            for (Author author : authorList) {
                modelSelectedAuthor.addElement(author);
                modelAvailableAuthor.removeElement(author);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int number = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public boolean validateBook(Book book) throws Exception {
//        if (book.id == -1) {
//            JOptionPane.showMessageDialog(EditBook.this, "Book ID can not be empty", "Alert", JOptionPane.ERROR_MESSAGE);
//            bookIdInput.requestFocus();
//            return false;
//        }
//
//        if (BookDAO.getInstance().getBookById(book.id) != null) {
//            JOptionPane.showMessageDialog(EditBook.this, "Book ID must be unique", "Alert", JOptionPane.ERROR_MESSAGE);
//            bookIdInput.requestFocus();
//            return false;
//        }
        if (book.title.isEmpty()) {
            JOptionPane.showMessageDialog(EditBook.this, "Book Title can not be empty", "Alert", JOptionPane.ERROR_MESSAGE);
            bookTitleInput.requestFocus();
            return false;
        }

        if (modelSelectedAuthor.isEmpty()) {
            JOptionPane.showMessageDialog(EditBook.this, "Book Author can not be empty", "Alert", JOptionPane.ERROR_MESSAGE);
            bookTitleInput.requestFocus();
            return false;
        }

        return true;
    }

    public void handleSave(ActionEvent event) {
        try {
            int newBookId = Integer.parseInt(bookIdInput.getText().trim());
            String bookTitle = bookTitleInput.getText().trim();
            Publisher publisher = (Publisher) publisherComboBox.getSelectedItem();
            String notes = noteTextArea.getText().trim();

            List<Author> authorList = Collections.list(modelSelectedAuthor.elements());

            // Process user name
            User user = myBook.user;
            String userName = null;
            if (user == null) {
                userName = "admin";
            } else {
                userName = user.userName;
            }

            // Create a new book
            assert publisher != null;
            Book book = new Book(newBookId, bookTitle, publisher.id, notes, userName);

            // Validate book before persisting
            if (validateBook(book)) {
                myBook.bookController.editBook(book, authorList, oldBookId);
                JOptionPane.showMessageDialog(EditBook.this, "The book was updated successfully");

                // Reload the list
                myBook.showAllBooks();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * When users select an author from the left list
     */
    public void handleAddAuthorFromList() {
        //  Move selected Author from the left list to the right list
        int selectedRow = availableAuthorList.getSelectedIndex();
        if (selectedRow != -1) {
            modelSelectedAuthor.addElement(modelAvailableAuthor.get(selectedRow));
            modelAvailableAuthor.remove(selectedRow);
        }
    }

    /**
     * When user remove an author from the right list
     */
    public void handleRemoveAuthorFromList() {
        // Move author from the right to the left list
        int selectedRow = selectedAuthorList.getSelectedIndex();
        if (selectedRow != -1) {
            modelAvailableAuthor.addElement(modelSelectedAuthor.get(selectedRow));
            modelSelectedAuthor.remove(selectedRow);
        }
    }

    public void handleDelete(ActionEvent event) {
        try {
            if (JOptionPane.showConfirmDialog(
                    EditBook.this,
                    "Do you want to delete this book?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION) {
                myBook.bookController.deleteBook(Integer.parseInt(bookIdInput.getText()));
                JOptionPane.showMessageDialog(EditBook.this,
                        "The book '" + bookTitleInput.getText() + "' was deleted successfully");

                // Reload the list
                myBook.showAllBooks();

                // Close the dialog
                dispose();


            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
