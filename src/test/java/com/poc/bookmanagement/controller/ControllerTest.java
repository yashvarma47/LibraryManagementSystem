package com.poc.bookmanagement.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.bookmanagement.entities.Books;
import com.poc.bookmanagement.entities.Books.BookStatus;
import com.poc.bookmanagement.exceptions.InvalidInputException;
import com.poc.bookmanagement.exceptions.ResourceNotFoundException;
import com.poc.bookmanagement.services.BookService;

public class ControllerTest {
	private MockMvc mockMvc;

    @Mock
    private BookService bookService;

    @InjectMocks
    private Controller bookController;

    private Books book1;
    private Books book2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController)
        		.setControllerAdvice(bookController)  // Include exception handling
                .build();

        // Sample book instances
        book1 = new Books();
        book1.setId(1);
        book1.setTitle("Book One");
        book1.setAuthor("Author One");
        book1.setStatus(BookStatus.SUBMITTED);

        book2 = new Books();
        book2.setId(2);
        book2.setTitle("Book Two");
        book2.setAuthor("Author Two");
        book2.setStatus(BookStatus.ISSUED);
        book2.setIssueDate(LocalDate.now());
        book2.setIssuerName("Issuer");
    }
    
 // Helper method to convert object to JSON string
    private String asJsonString(Object obj) throws Exception {
        return new ObjectMapper().writeValueAsString(obj);
    }

    @Test
    void testGetAllBooks_Success() throws Exception {
        List<Books> books = Arrays.asList(book1, book2);

        when(bookService.getAllBooks()).thenReturn(books);

        mockMvc.perform(get("/api/books")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Book One"))
                .andExpect(jsonPath("$[1].author").value("Author Two"));

        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void testGetBookById_Success() throws Exception {
        when(bookService.getBookById(1)).thenReturn(book1);

        mockMvc.perform(get("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Book One"))
                .andExpect(jsonPath("$.author").value("Author One"));

        verify(bookService, times(1)).getBookById(1);
    }

    @Test
    void testGetBookById_NotFound() throws Exception {
        when(bookService.getBookById(1)).thenThrow(new ResourceNotFoundException("Book not found with id: 1"));

        mockMvc.perform(get("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect((ResultMatcher) content().string("Book not found with id: 1"));  // This should work fine.

        verify(bookService, times(1)).getBookById(1);
    }
    
    @Test
    void testAddBook_Success() throws Exception {
        when(bookService.addBook(any(Books.class))).thenReturn(book1);

        mockMvc.perform(post("/api/books/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(book1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Book One"))
                .andExpect(jsonPath("$.status").value("SUBMITTED"));

        verify(bookService, times(1)).addBook(any(Books.class));
    }

    @Test
    void testAddBook_InvalidInput() throws Exception {
        when(bookService.addBook(any(Books.class))).thenThrow(new InvalidInputException("Invalid book input"));

        mockMvc.perform(post("/api/books/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(book1)))
                .andExpect(status().isBadRequest())
                .andExpect((ResultMatcher) content().string("Invalid book input"));

        verify(bookService, times(1)).addBook(any(Books.class));
    }

    @Test
    void testUpdateBook_Success() throws Exception {
        when(bookService.updateBook(any(Books.class), eq(1))).thenReturn(book1);

        mockMvc.perform(put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(book1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Book One"))
                .andExpect(jsonPath("$.author").value("Author One"));

        verify(bookService, times(1)).updateBook(any(Books.class), eq(1));
    }

    @Test
    void testUpdateBook_NotFound() throws Exception {
        when(bookService.updateBook(any(Books.class), eq(1)))
                .thenThrow(new ResourceNotFoundException("Book not found with id: 1"));

        mockMvc.perform(put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(book1)))
                .andExpect(status().isNotFound())
                .andExpect((ResultMatcher) content().string("Book not found with id: 1"));

        verify(bookService, times(1)).updateBook(any(Books.class), eq(1));
    }

    @Test
    void testDeleteBook_Success() throws Exception {
        mockMvc.perform(delete("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(bookService, times(1)).deleteBook(1);
    }

    @Test
    void testDeleteBook_NotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Book not found with id: 1"))
                .when(bookService).deleteBook(1);

        mockMvc.perform(delete("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect((ResultMatcher) content().string("Book not found with id: 1"));

        verify(bookService, times(1)).deleteBook(1);
    }

    @Test
    void testDeleteBook_InvalidInput() throws Exception {
        doThrow(new InvalidInputException("Only submitted book can be deleted!"))
                .when(bookService).deleteBook(1);

        mockMvc.perform(delete("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect((ResultMatcher) content().string("Only submitted book can be deleted!"));

        verify(bookService, times(1)).deleteBook(1);
    }
    
    @Test
    void testGlobalExceptionHandler() throws Exception {
        // Simulate a situation where an unexpected exception occurs
        when(bookService.getAllBooks()).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/api/books")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An error occurred: Unexpected error"));

        verify(bookService, times(1)).getAllBooks();
    }

}
