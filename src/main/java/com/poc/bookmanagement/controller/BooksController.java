package com.poc.bookmanagement.controller;

import java.util.List;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poc.bookmanagement.entities.Books;
import com.poc.bookmanagement.services.BookService;

@RestController
@RequestMapping("/api")
public class BooksController {

	private static final Logger log = LoggerFactory.getLogger(BooksController.class);


	@Autowired
//	@Qualifier("bookServiceImpl2")
	private BookService bookService;

	@GetMapping("/books")
	public ResponseEntity<List<Books>> getAllBooks() {
		log.info("Received request to get all books");
		List<Books> books = bookService.getAllBooks();
		log.debug("Fetched {} books from database", books.size());
		return ResponseEntity.ok(books);
	}

	@GetMapping("/books/{id}")
	public ResponseEntity<Books> getBookById(@PathVariable int id) {
		log.info("Received request to get book with ID: {}", id);
		Books book = bookService.getBookById(id);
		log.debug("Fetched book details: {}", book);
		return ResponseEntity.ok(book);
	}

	@PostMapping("/books/add")
	public ResponseEntity<Books> addBook(@Valid @RequestBody Books book) {
		log.info("Received request to add a new book: {}", book);
		Books addedBook = bookService.addBook(book);
		log.info("Book added with ID: {}", addedBook.getId());
		return ResponseEntity.status(HttpStatus.CREATED).body(addedBook);
	}

	@PutMapping("/books/{id}")
	public ResponseEntity<Books> updateBook(@RequestBody Books book, @PathVariable int id) {
		log.info("Received request to update book with ID: {}", id);
		Books updatedBook = (Books) bookService.updateBook(book, id);
		log.debug("Updated book details: {}", updatedBook);
		return ResponseEntity.ok(updatedBook);
	}
	
	@DeleteMapping("/books/{id}")
	public ResponseEntity<Books> deleteBook(@PathVariable int id){
		log.info("Received request to delete book with ID: {}", id);
		bookService.deleteBook(id);
		log.info("Successfully deleted book with ID: {}", id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/getbyidstatus/{id}/{status}")
	public ResponseEntity<Books> getByIdAndStatus(@PathVariable int id,@PathVariable String status) {
		log.info("Received request to get book with ID: {} and status: {}", id, status);
		Books bk = bookService.getBookByIdAndStatus(id, Books.BookStatus.valueOf(status));
		log.debug("Fetched book details with status {}: {}", status, bk);
		return ResponseEntity.ok(bk);
	}

}
