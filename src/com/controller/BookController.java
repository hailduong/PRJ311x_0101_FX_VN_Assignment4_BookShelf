package com.controller;

import com.DAO.BookDAO;
import com.entity.Author;
import com.entity.Book;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class BookController {
    public void addBook(Book book, List<Author> authorList) throws Exception {
        BookDAO.getInstance().addBook(book, authorList);
    }

    public void listBooks(JTable jBookTable) throws Exception {
        DefaultTableModel tableModel = (DefaultTableModel) jBookTable.getModel();

        // Clear data
        tableModel.setColumnCount(0);

        // Get all books
        List<Book> bookList = BookDAO.getInstance().getAllBooks();
        for (Book book : bookList) {
            tableModel.addRow(book.toDataRow());
        }
    }


    public List<Book> search(String columnName, String keyword) throws Exception {
        System.out.println("columnName: " + columnName);
        System.out.println("keyword: " + keyword);

        // Get data
        List<Book> bookList = BookDAO.getInstance().getBookBy(columnName, keyword);
        return bookList;

    }

    public Book getBookById(int bookId) throws Exception {
        return BookDAO.getInstance().getBookById(bookId);
    }

    public void editBook(Book book, List<Author> authorList, int oldBookId) throws Exception {
        BookDAO.getInstance().editBook(book, authorList, oldBookId);
    }

    public void deleteBook(int bookId) throws Exception {
        BookDAO.getInstance().deleteBook(bookId);
    }

}
