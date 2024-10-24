package com.poc.bookmanagement.services;

import java.util.List;

import com.poc.bookmanagement.entities.Books;

public interface BookService {
	public List<Books> getAllBooks();
	public Books getBookById(int id);
	public void deleteBook(int id);
	public Books addBook(Books book);
	public Books updateBook(Books book, int id);
	
}
