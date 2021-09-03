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

public class AddBook extends JFrame {
    private JPanel mainPanel;
    private JTextField bookIdInput;
    private JTextField bookTitleInput;
    private JComboBox publisherComboBox;
    private JList availableAuthorList;
    private JTextArea noteTextArea;
    private JButton addButton;
    private JButton removeButton;
    private JList selectedAuthorList;
    private JButton saveButton;
    private JButton closeButton;
    private final MyBook myBook;

    private final DefaultComboBoxModel<Publisher> modelPublishers = new DefaultComboBoxModel<>();
    private final DefaultListModel<Author> modelAvailableAuthor = new DefaultListModel<>();
    private final DefaultListModel<Author> modelSelectedAuthor = new DefaultListModel<>();


    public AddBook(MyBook myBook) {
        this.setupUI();
        this.myBook = myBook;

        this.listPublishers();
        this.listAuthors();

        this.bindAddButtonListener();
        this.bindRemoveButtonListener();
        this.bindCloseButtonListener();
        this.bindSaveButtonListener();
    }

    private void bindSaveButtonListener() {
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSave(e);
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
        if (book.title.isEmpty()) {
            JOptionPane.showMessageDialog(AddBook.this, "Book Title can not be empty", "Alert", JOptionPane.ERROR_MESSAGE);
            bookTitleInput.requestFocus();
            return false;
        }

        if (modelSelectedAuthor.isEmpty()) {
            JOptionPane.showMessageDialog(AddBook.this, "Book Author can not be empty", "Alert", JOptionPane.ERROR_MESSAGE);
            bookTitleInput.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * When user click on the save button
     *
     * @param event
     */
    public void handleSave(ActionEvent event) {
        try {
            // We do not need an id, when we add a new book
            // But we need to get the other properties
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
            Book book = new Book(bookTitle, publisher.id, notes, userName);

            // Validate book before persisting
            if (validateBook(book)) {

                myBook.bookController.addBook(book, authorList);
                JOptionPane.showMessageDialog(AddBook.this, "The book was added");

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
        // TODO: add handling code
        // Move author from the right to the left list
        int selectedRow = selectedAuthorList.getSelectedIndex();
        if (selectedRow != -1) {
            modelAvailableAuthor.addElement(modelSelectedAuthor.get(selectedRow));
            modelSelectedAuthor.remove(selectedRow);
        }
    }

}
