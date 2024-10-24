package com.poc.bookmanagement.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poc.bookmanagement.dao.BookRepository;
import com.poc.bookmanagement.entities.Books;
import com.poc.bookmanagement.exceptions.InvalidInputException;
import com.poc.bookmanagement.exceptions.ResourceNotFoundException;
import com.poc.bookmanagement.services.BookService;

@RestController
@RequestMapping("/api")
public class Controller {

	@Autowired
	private BookService bookService;

	@GetMapping("/books")
	public ResponseEntity<List<Books>> getAllBooks() {
		List<Books> books = bookService.getAllBooks();
		return ResponseEntity.ok(books);
	}

	@GetMapping("/books/{id}")
	public ResponseEntity<Books> getBookById(@PathVariable int id) {
		Books book = bookService.getBookById(id);
		return ResponseEntity.ok(book);
	}

	@PostMapping("/books/add")
	public ResponseEntity<Books> addBook(@RequestBody Books book) {
		Books addedBook = bookService.addBook(book);
		return ResponseEntity.status(HttpStatus.CREATED).body(addedBook);
	}

	@PutMapping("/books/{id}")
	public ResponseEntity<Books> updateBook(@RequestBody Books book, @PathVariable int id) {
		Books updatedBook = bookService.updateBook(book, id);
		return ResponseEntity.ok(updatedBook);
	}
	
	@DeleteMapping("/books/{id}")
	public ResponseEntity<Books> deleteBook(@PathVariable int id){
		bookService.deleteBook(id);
		return ResponseEntity.noContent().build();
	}
	
	
	// Generic exception handler for this controller
				@ExceptionHandler(Exception.class)
				public ResponseEntity<String> handleGlobalException(Exception ex) {
					return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
				}
				
				@ExceptionHandler(InvalidInputException.class)
				public ResponseEntity<String> handleInvalidInputException(InvalidInputException ex) {
					return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
				}

				 // Exception handler for this controller
		 		@ExceptionHandler(ResourceNotFoundException.class)
		 		public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
		 			return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
		 		}
	
	
	
	
	
	
	
	
	
	
	
	

	

		

	
}
