package com.poc.bookmanagement.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.poc.bookmanagement.dao.BookRepository;
import com.poc.bookmanagement.entities.Books;
import com.poc.bookmanagement.entities.Books.BookStatus;
import com.poc.bookmanagement.exceptions.InvalidInputException;
import com.poc.bookmanagement.exceptions.ResourceNotFoundException;
import com.poc.bookmanagement.services.BookServiceImpl;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

	@Mock
	private BookRepository bookRepository;

	@InjectMocks
	private BookServiceImpl bookService;

	private Books existingBook;

	private Books book;

	@BeforeEach
	public void setUp() {
	    MockitoAnnotations.openMocks(this);
	    
	    // Initialize the existing book which will be used in certain tests
	    existingBook = new Books();
	    existingBook.setId(1);
	    existingBook.setTitle("Test Book");
	    existingBook.setAuthor("Author Name");
	    existingBook.setStatus(BookStatus.SUBMITTED);
	    existingBook.setSubmissionDate(LocalDate.now());

	    // Optionally initialize another book instance for tests
	    book = new Books();
	    book.setTitle("Sample Book");
	    book.setAuthor("Author Name");
	    book.setStatus(BookStatus.SUBMITTED);
	}


	@Test
	void testGetAllBooks() {
		when(bookRepository.findAll()).thenReturn(Arrays.asList(book));

		List<Books> books = bookService.getAllBooks();

		assertNotNull(books);
		assertEquals(1, books.size());
		assertEquals("Sample Book", books.get(0).getTitle());
	}

	@Test
	void testGetAllBooksEmpty() {
		when(bookRepository.findAll()).thenReturn(Arrays.asList());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			bookService.getAllBooks();
		});

		assertEquals("No books found", exception.getMessage());
	}

	@Test
	void testGetBookById() {
		when(bookRepository.findById(1)).thenReturn(Optional.of(book));

		Books foundBook = bookService.getBookById(1);

		assertNotNull(foundBook);
		assertEquals("Sample Book", foundBook.getTitle());
	}

	@Test
	void testGetBookByIdNotFound() {
		when(bookRepository.findById(999)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			bookService.getBookById(999);
		});

		assertEquals("Book not found with id : 999", exception.getMessage());
	}

	@Test
	void testAddBook() {
		when(bookRepository.save(any(Books.class))).thenReturn(book);

		Books addedBook = bookService.addBook(book);

		assertNotNull(addedBook);
		assertEquals("Sample Book", addedBook.getTitle());
		verify(bookRepository, times(1)).save(book);
	}

	@Test
	void testUpdateBook() {
		Books updatedBook = new Books();
		updatedBook.setTitle("Updated Book");
		updatedBook.setAuthor("Updated Author");
		updatedBook.setStatus(BookStatus.ISSUED);
		updatedBook.setIssuerName("John Doe");

		when(bookRepository.findById(1)).thenReturn(Optional.of(book));
		when(bookRepository.save(any(Books.class))).thenReturn(updatedBook);

		Books result = bookService.updateBook(updatedBook, 1);

		assertEquals("Updated Book", result.getTitle());
		assertEquals("Updated Author", result.getAuthor());
		assertEquals(BookStatus.ISSUED, result.getStatus());
		assertNotNull(result.getIssuerName());
	}

	@Test
	void testUpdateBookNotFound() {
		when(bookRepository.findById(999)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			bookService.updateBook(book, 999);
		});

		assertEquals("Book not found with id: 999", exception.getMessage());
	}

	@Test
	void testDeleteBook() {
		book.setStatus(BookStatus.SUBMITTED);
		when(bookRepository.findById(1)).thenReturn(Optional.of(book));
		doNothing().when(bookRepository).delete(book);

		assertDoesNotThrow(() -> bookService.deleteBook(1));
		verify(bookRepository, times(1)).delete(book);
	}

	@Test
	void testDeleteBookNotFound() {
		when(bookRepository.findById(999)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			bookService.deleteBook(999);
		});

		assertEquals("Book not found with id: 999", exception.getMessage());
	}

	@Test
	void testDeleteBookInvalidStatus() {
		book.setStatus(BookStatus.ISSUED);
		when(bookRepository.findById(1)).thenReturn(Optional.of(book));

		Exception exception = assertThrows(InvalidInputException.class, () -> {
			bookService.deleteBook(1);
		});

		assertEquals("Only submitted book can be deleted!", exception.getMessage());
	}

	@Test
	void testGetBookByIdAndStatus() {
		when(bookRepository.findByIdAndStatus(1, BookStatus.SUBMITTED)).thenReturn(Optional.of(book));

		Books foundBook = bookService.getBookByIdAndStatus(1, BookStatus.SUBMITTED);

		assertNotNull(foundBook);
		assertEquals("Sample Book", foundBook.getTitle());
	}

	@Test
	void testGetBookByIdAndStatusNotFound() {
		when(bookRepository.findByIdAndStatus(999, BookStatus.SUBMITTED)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			bookService.getBookByIdAndStatus(999, BookStatus.SUBMITTED);
		});

		assertEquals("Book not found with id: 999 and status: SUBMITTED", exception.getMessage());
	}

	@Test
	public void testUpdateBook_AlreadySubmitted() {
	    // Arrange
	    Books bookToUpdate = new Books();
	    bookToUpdate.setStatus(BookStatus.SUBMITTED); // Trying to submit an already submitted book

	    // Mock the behavior of the repository
	    when(bookRepository.findById(1)).thenReturn(Optional.of(existingBook));

	    // Act & Assert
	    InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
	        bookService.updateBook(bookToUpdate, 1);
	    });

	    // Check the exception message
	    assertEquals("This book is already submitted.", exception.getMessage());
	}

}
