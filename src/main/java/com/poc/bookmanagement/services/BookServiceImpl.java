package com.poc.bookmanagement.services;

import java.time.LocalDate;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poc.bookmanagement.dao.BookRepository;
import com.poc.bookmanagement.entities.Books;
import com.poc.bookmanagement.entities.Books.BookStatus;
import com.poc.bookmanagement.exceptions.InvalidInputException;
import com.poc.bookmanagement.exceptions.ResourceNotFoundException;

import jakarta.transaction.Transactional;
@Service
public class BookServiceImpl implements BookService {

	private static final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);
	@Autowired
	private BookRepository bookRepository;

	@Override
	public List<Books> getAllBooks() {

		List<Books> books = bookRepository.findAll();
	    if (books.isEmpty()) {

	        throw new ResourceNotFoundException("No books found");
	    }

	    return books;
	}

	@Override
	public Books getBookById(int id) {
	    return  bookRepository.findById(id)
	    .orElseThrow(() -> new ResourceNotFoundException("Book not found with id : " + id));
	}

	@Override
	public Books getBookByIdAndStatus(int id, Books.BookStatus status) {
		return bookRepository.findByIdAndStatus(id, status)
				.orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id + " and status: " + status));
	}

	@Override
	public Books addBook(Books book) {

		//While adding book first check if the title and author are present.

		if (book.getStatus() == null) {
			book.setStatus(BookStatus.SUBMITTED);
		}
		
		if (book.getStatus() == BookStatus.SUBMITTED) {
			book.setSubmissionDate(LocalDate.now());
			book.setIssueDate(null);
		}
	    return bookRepository.save(book);
	}

	
	@Transactional
	@Override
	public Books updateBook(Books book, int id) {
		Books existingBook = bookRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
		
		// Update title and author if provided
		if (book.getTitle() != null && !book.getTitle().isEmpty()) {
			existingBook.setTitle(book.getTitle());
		} 
		
		if (book.getAuthor() != null && !book.getAuthor().isEmpty()) {
			existingBook.setAuthor(book.getAuthor());
		}
		// Handle status changes
		if (book.getStatus() != null) {
			//checking if Book Status are already ISSUED or SUBMITTED
			if (book.getStatus() == BookStatus.ISSUED){
				if (existingBook.getStatus() == BookStatus.ISSUED){
					throw new InvalidInputException("This book is already issued. Please wait until it is submitted before issuing again.");
				}
			}
			if (book.getStatus() == BookStatus.SUBMITTED){
				if(existingBook.getStatus() == BookStatus.SUBMITTED){
					throw new InvalidInputException("This book is already submitted.");
				}
			}
			//If Issued
			if (book.getStatus() == BookStatus.ISSUED) {
				existingBook.setStatus(BookStatus.ISSUED);
				existingBook.setIssueDate(LocalDate.now());
				existingBook.setSubmissionDate(null);
				
				if (book.getIssuerName() != null && !book.getIssuerName().isEmpty()) {
	                existingBook.setIssuerName(book.getIssuerName());
	            } else {
	                throw new InvalidInputException("Issuer name cannot be null or empty when issuing a book");
	            }
			} 
			//If Submitted
			else if (book.getStatus() == BookStatus.SUBMITTED) {
				existingBook.setStatus(book.getStatus());
				existingBook.setSubmissionDate(LocalDate.now());
				existingBook.setIssuerName(null);
				existingBook.setIssueDate(null);
			}
		}
	    return bookRepository.save(existingBook);
	}

	@Override
	public void deleteBook(int id) {
		Books existingBook = bookRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
		if (!existingBook.getStatus().equals(BookStatus.SUBMITTED)) {
			throw new InvalidInputException("Only submitted book can be deleted!");
		}
	bookRepository.delete(existingBook);
	}
}
