package com.poc.bookmanagement.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.poc.bookmanagement.entities.Books;
import com.poc.bookmanagement.services.BookService;

public class ControllerTest {
	 @Mock
	    private BookService bookService;

	    @InjectMocks
	    private BooksController controller;

	    private Books book;

	    @BeforeEach
	    void setUp() {
	        MockitoAnnotations.openMocks(this);
	        book = new Books();
	        book.setId(1);
	        book.setTitle("Sample Book");
	        book.setAuthor("Sample Author");
	        book.setStatus(Books.BookStatus.SUBMITTED);
	    }

	    @Test
	    void testGetAllBooks() {
	        List<Books> books = new ArrayList<>();
	        books.add(book);

	        when(bookService.getAllBooks()).thenReturn(books);

	        ResponseEntity<List<Books>> response = controller.getAllBooks();
	        
	        assertEquals(HttpStatus.OK, response.getStatusCode());
	        assertEquals(1, response.getBody().size());
	    }

	    @Test
	    void testGetBookById() {
	        when(bookService.getBookById(1)).thenReturn(book);

	        ResponseEntity<Books> response = controller.getBookById(1);

	        assertEquals(HttpStatus.OK, response.getStatusCode());
	        assertEquals(book, response.getBody());
	    }

	    @Test
	    void testAddBook() {
	        when(bookService.addBook(any(Books.class))).thenReturn(book);

	        ResponseEntity<Books> response = controller.addBook(book);
	        
	        assertEquals(HttpStatus.CREATED, response.getStatusCode());
	        assertEquals(book, response.getBody());
	    }

	    @Test
	    void testUpdateBook() {
	        when(bookService.updateBook(any(Books.class), eq(1))).thenReturn(book);

	        ResponseEntity<Books> response = controller.updateBook(book, 1);
	        
	        assertEquals(HttpStatus.OK, response.getStatusCode());
	        assertEquals(book, response.getBody());
	    }

	    @Test
	    void testDeleteBook() {
	        doNothing().when(bookService).deleteBook(1);

	        ResponseEntity<Books> response = controller.deleteBook(1);
	        
	        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	    }

//	    @Test
//	    void testGetBookByIdNotFound() {
//	        // Arrange
//	        int bookId = 1;
//	        when(bookService.getBookById(bookId)).thenThrow(new ResourceNotFoundException("Book not found with id: " + bookId));
//
//	        // Act
//	        ResponseEntity<Books> response = controller.getBookById(bookId);
//
//	        // Assert
//	        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//	    }

//	    @Test
//	    void testAddBookValidationFailure() {
//	        // Assuming validation throws InvalidInputException
//	        when(bookService.addBook(any(Books.class))).thenThrow(new InvalidInputException("Invalid input"));
//
//	        ResponseEntity<Books> response = controller.addBook(book);
//	        
//	        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//	        assertEquals("Invalid input", response.getBody());
//	    }

//	    @Test
//	    void testGetByIdAndStatus() {
//	        when(bookService.getBookByIdAndStatus(anyInt(), any(Books.BookStatus.class))).thenReturn(book);
//
//	        ResponseEntity<Books> response = controller.getByIdAndStatus(book, 1);
//
//	        assertEquals(HttpStatus.OK, response.getStatusCode());
//	        assertEquals(book, response.getBody());
//	    }
	    
//	    @Test
//	    void testGetAllBooksEmpty() {
//	        when(bookService.getAllBooks()).thenThrow(new ResourceNotFoundException("No books found"));
//
//	        ResponseEntity<List<Books>> response = controller.getAllBooks();
//	        
//	        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//	    }

//	    @Test
//	    void testUpdateBookNotFound() {
//	        when(bookService.updateBook(any(Books.class), eq(999))).thenThrow(new ResourceNotFoundException("Book not found with id: 999"));
//
//	        ResponseEntity<Books> response = controller.updateBook(book, 999);
//	        
//	        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//	    }

//	    @Test
//	    void testDeleteBookNotFound() {
//	        when(bookService.deleteBook(999)).thenThrow(new ResourceNotFoundException("Book not found with id: 999"));
//
//	        ResponseEntity<Books> response = controller.deleteBook(999);
//	        
//	        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//	    }


}
