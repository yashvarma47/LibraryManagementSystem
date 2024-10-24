package com.poc.bookmanagement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.poc.bookmanagement.dao.BookRepository;
import com.poc.bookmanagement.entities.Books;
import com.poc.bookmanagement.entities.Books.BookStatus;
import com.poc.bookmanagement.exceptions.InvalidInputException;
import com.poc.bookmanagement.exceptions.ResourceNotFoundException;
import com.poc.bookmanagement.services.BookServiceImpl;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void testGetAllBooks_ThrowsResourceNotFound() {
        // Simulate an empty repository
        when(bookRepository.findAll()).thenReturn(new ArrayList<>());
        
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            bookService.getAllBooks();
        });
        
        assertEquals("No books found", exception.getMessage());
    }
    
    @Test
    void testGetAllBooks_Success() {
        List<Books> books = List.of(new Books());
        when(bookRepository.findAll()).thenReturn(books);
        
        List<Books> result = bookService.getAllBooks();
        
        assertEquals(books.size(), result.size());
    }
    
    @Test
    void testGetBookById_BookNotFound() {
        when(bookRepository.findById(anyInt())).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            bookService.getBookById(1);
        });
    }

    @Test
    void testGetBookById_Success() {
        Books book = new Books();
        when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        
        Books result = bookService.getBookById(1);
        
        assertNotNull(result);
    }

    @Test
    void testAddBook_ThrowsInvalidInput() {
        Books book = new Books();
        book.setTitle(null); // Invalid title

        assertThrows(InvalidInputException.class, () -> bookService.addBook(book));
    }

    @Test
    void testAddBook_Success() {
        Books book = new Books();
        book.setTitle("Title");
        book.setAuthor("Author");

        when(bookRepository.save(book)).thenReturn(book);

        Books result = bookService.addBook(book);

        assertEquals("Title", result.getTitle());
        assertEquals(BookStatus.SUBMITTED, result.getStatus()); // Default status should be submitted
    }
    
//    @Test
//    void testUpdateBook_BookNotFound() {
//        when(bookRepository.findById(anyInt())).thenReturn(Optional.empty());
//        
//        Books book = new Books();
//        assertThrows(ResourceNotFoundException.class, () -> bookService.updateBook(book, 1));
//    }

    @Test
    void testUpdateBook_Success() {
        // Given
        Books existingBook = new Books();
        existingBook.setId(1);
        existingBook.setTitle("Original Title"); // Initialize title or other properties as needed
        existingBook.setAuthor("Original Author"); // Initialize other properties if necessary

        // Mock the repository to return the existing book
        when(bookRepository.findById(1)).thenReturn(Optional.of(existingBook));

        // Mock the save method to return the updated book
        when(bookRepository.save(existingBook)).thenReturn(existingBook);

        // New book details for update
        Books bookToUpdate = new Books();
        bookToUpdate.setTitle("Updated Title"); // Set the new title

        // When
        Books result = bookService.updateBook(bookToUpdate, 1);

        // Then
        assertNotNull(result); // Check if result is not null
        assertEquals("Updated Title", result.getTitle()); // Check that the title has been updated
    }



    @Test
    void testDeleteBook_BookNotFound() {
        when(bookRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.deleteBook(1));
    }

    @Test
    void testDeleteBook_NotSubmitted() {
        Books book = new Books();
        book.setStatus(BookStatus.ISSUED); // Not submitted, hence should fail
        
        when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        
        assertThrows(InvalidInputException.class, () -> bookService.deleteBook(1));
    }

    @Test
    void testDeleteBook_Success() {
        Books book = new Books();
        book.setStatus(BookStatus.SUBMITTED); // Valid for deletion
        
        when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        
        bookService.deleteBook(1);
        
        verify(bookRepository, times(1)).delete(book); // Verifies that delete was called exactly once
    }
    
//    @Test
//    void testUpdateBook_IssuedBook_Success() {
//        // Given
//        Books existingBook = new Books();
//        existingBook.setId(1);
//        existingBook.setTitle("Original Title");
//        existingBook.setAuthor("Original Author");
//        existingBook.setStatus(BookStatus.SUBMITTED); // Initially submitted
//
//        // Mock the repository to return the existing book
//        when(bookRepository.findById(1)).thenReturn(Optional.of(existingBook));
//
//        // New book details for update to issued
//        Books bookToUpdate = new Books();
//        bookToUpdate.setStatus(BookStatus.ISSUED);
//        bookToUpdate.setIssuerName("John Doe"); // Set the issuer name
//
//        // When
//        Books result = bookService.updateBook(bookToUpdate, 1);
//
//        // Then
//        assertNotNull(result);
//        assertEquals(BookStatus.ISSUED, result.getStatus());
//        assertEquals("John Doe", result.getIssuerName());
//        assertNotNull(result.getIssueDate()); // Issue date should be set
//        assertNull(result.getSubmissionDate()); // Submission date should be null
//    }

//    @Test
//    void testUpdateBook_SubmittedBook_Success() {
//        // Given
//        Books existingBook = new Books();
//        existingBook.setId(1);
//        existingBook.setTitle("Original Title");
//        existingBook.setAuthor("Original Author");
//        existingBook.setStatus(BookStatus.ISSUED); // Initially issued
//
//        // Mock the repository to return the existing book
//        when(bookRepository.findById(1)).thenReturn(Optional.of(existingBook));
//
//        // New book details for update to submitted
//        Books bookToUpdate = new Books();
//        bookToUpdate.setStatus(BookStatus.SUBMITTED);
//
//        // When
//        Books result = bookService.updateBook(bookToUpdate, 1);
//
//        // Then
//        assertNotNull(result);
//        assertEquals(BookStatus.SUBMITTED, result.getStatus());
//        assertNull(result.getIssuerName()); // Issuer name should be null
//        assertNull(result.getIssueDate()); // Issue date should be null
//        assertNotNull(result.getSubmissionDate()); // Submission date should be set
//    }

    @Test
    void testUpdateBook_BookNotFound() {
        when(bookRepository.findById(anyInt())).thenReturn(Optional.empty());

        Books book = new Books();
        assertThrows(ResourceNotFoundException.class, () -> bookService.updateBook(book, 1));
    }

    @Test
    void testUpdateBook_IssuerNameMissing() {
        // Given
        Books existingBook = new Books();
        existingBook.setId(1);
        existingBook.setTitle("Original Title");
        existingBook.setAuthor("Original Author");
        existingBook.setStatus(BookStatus.SUBMITTED); // Initially submitted

        // Mock the repository to return the existing book
        when(bookRepository.findById(1)).thenReturn(Optional.of(existingBook));

        // New book details for update to issued without issuer name
        Books bookToUpdate = new Books();
        bookToUpdate.setStatus(BookStatus.ISSUED); // Set status to issued

        // When & Then
        assertThrows(InvalidInputException.class, () -> bookService.updateBook(bookToUpdate, 1));
    }

    
    
}