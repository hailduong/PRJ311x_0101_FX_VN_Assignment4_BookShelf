package com.controller;

import com.DAO.BookDAO;
import com.entity.Book;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class BookController {
    public void addBook(Book book) throws Exception {
        BookDAO.getInstance().addBook(book);
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


    public void search(String column, String keyword, JTable jBookTable) throws Exception {
        DefaultTableModel tableModel = (DefaultTableModel) jBookTable.getModel();

        // Clear data
        tableModel.setColumnCount(0);

        // Get data
//        List<Book> bookList = BookDAO.getInstance().getBookBy(column, keyword);
//        for (Book book : bookList) {
//            tableModel.addRow(book.toDataRow());
//        }

    }

    public Book getBookById(int bookId) throws Exception {
        return BookDAO.getInstance().getBookById(bookId);
    }

    public void editBook(Book book) throws Exception {
        BookDAO.getInstance().editBook(book);
    }

    public void deleteBook(int bookId) throws Exception {
        BookDAO.getInstance().deleteBook(bookId);
    }

}
