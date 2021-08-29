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
import java.util.Collections;
import java.util.List;

public class EditBook extends JFrame {
    private JPanel panel1;
    private JTextField bookIdInput;
    private JTextField bookTitleInput;
    private JComboBox publisherComboBox;
    private JList availableAuthorList;
    private JTextArea noteTextArea;
    private JButton removeButton;
    private JButton addButton;
    private JList selectedAuthorList;
    private JButton updateButton;
    private JButton closeButton;
    private JButton deleteButton;
    private final MyBook myBook;

    private final DefaultComboBoxModel<Publisher> modelPublishers = new DefaultComboBoxModel<>();
    private final DefaultListModel<Author> modelAvailableAuthor = new DefaultListModel<>();
    private final DefaultListModel<Author> modelSelectedAuthor = new DefaultListModel<>();

    public EditBook(MyBook myBook) {
        this.myBook = myBook;
        this.listPublishers();
        this.listAuthors();
    }

    public void listAuthors() {
        try {
            List<Author> authorList = AuthorDAO.getInstance().getAllAuthors();
            for (Author author : authorList) {
                modelAvailableAuthor.addElement(author);
            }

            availableAuthorList.setModel(modelAvailableAuthor);
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
        if (book.id == -1) {
            JOptionPane.showMessageDialog(EditBook.this, "Book ID can not be empty", "Alert", JOptionPane.ERROR_MESSAGE);
            bookIdInput.requestFocus();
            return false;
        }

        if (BookDAO.getInstance().getBookById(book.id) != null) {
            JOptionPane.showMessageDialog(EditBook.this, "Book ID must be unique", "Alert", JOptionPane.ERROR_MESSAGE);
            bookIdInput.requestFocus();
            return false;
        }
        if (book.title.isEmpty()) {
            JOptionPane.showMessageDialog(EditBook.this, "Book Title can not be empty", "Alert", JOptionPane.ERROR_MESSAGE);
            bookTitleInput.requestFocus();
            return false;
        }

        if (modelSelectedAuthor.isEmpty()) {
            JOptionPane.showMessageDialog(EditBook.this, "Book Title can not be empty", "Alert", JOptionPane.ERROR_MESSAGE);
            bookTitleInput.requestFocus();
            return false;
        }

        return true;
    }

    public void handleSave(ActionEvent event) {
        try {
            String stringBookId = bookIdInput.getText().trim();
            int bookId;
            if (this.isNumeric(stringBookId)) {
                bookId = Integer.parseInt(bookIdInput.getText().trim());
            } else {
                bookId = -1;
            }
            String bookTitle = bookTitleInput.getText().trim();
            Publisher publisher = (Publisher) publisherComboBox.getSelectedItem();
            String notes = noteTextArea.getText().trim();

            List<Author> authorList = Collections.list(modelSelectedAuthor.elements());

            User user = myBook.user;
            assert publisher != null;
            Book book = new Book(bookId, bookTitle, publisher.id, notes, user.userName);

            if (validateBook(book)) {
                myBook.bookController.addBook(book);
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
        // TODO: Add handling code

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
        // TODO: add handling code
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
                        "A book " + bookIdInput.getText() + " has been deleted");
                dispose();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
