//package com.poc.bookmanagement.exceptions;
//
//import static org.hamcrest.CoreMatchers.containsString;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.util.Collections;
//import java.util.Optional;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.FieldError;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//
//import com.poc.bookmanagement.dao.BookRepository;
//import com.poc.bookmanagement.entities.Books;
//
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class GlobalExceptionHandlerTest {
//
//	 @Autowired
//	    private MockMvc mockMvc2;
//
//	private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
//
//	@Test
//    void handleValidationExceptionTest() {
//        // Arrange
//        String errorMessage = "Book title cannot be null or empty";
//
//        // Create a mock BindingResult
//        BindingResult bindingResult = mock(BindingResult.class);
//        FieldError fieldError = new FieldError("books", "title", errorMessage);
//
//        // Set up the BindingResult to return the mocked FieldError
//        when(bindingResult.getAllErrors()).thenReturn(Collections.singletonList(fieldError));
//
//        // Mock the MethodArgumentNotValidException to return the mocked BindingResult
//        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
//        when(exception.getBindingResult()).thenReturn(bindingResult);
//
//        // Act
//        ResponseEntity<String> response = globalExceptionHandler.handleValidationException(exception);
//
//        // Assert
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        assertEquals("Validation error: " + errorMessage, response.getBody());
//    }
//	@MockBean
//	private BookRepository bookRepository;
//
//
////	@Test
////	void testHandleInvalidInputException() throws Exception {
////	    String errorMessage = "This book is already issued. Please wait until it is submitted before issuing again.";
////
////	    // Create a mock book object that should trigger the exception
////	    String bookJson = "{\"title\": \"Existing Book\", \"author\": \"Author Name\", \"status\": \"ISSUED\"}";
////
////	    // Simulate that the book with the title "Existing Book" is already issued
////	    // You'll need to set up the mock repository behavior for this
////	    Books existingBook = new Books();
////	    existingBook.setTitle("Existing Book");
////	    existingBook.setAuthor("Author Name");
////	    existingBook.setStatus(Books.BookStatus.ISSUED); // This book is already issued
////
////	    // Mock the repository to return the existing book when searching by title
////	    when(bookRepository.findById(anyInt())).thenReturn(Optional.of(existingBook)); // Ensure this is mocked correctly for the test case
////
////	    // Perform the request
////	    mockMvc2.perform(post("/api/books/add")
////	            .contentType(MediaType.APPLICATION_JSON)
////	            .content(bookJson)
////	    )
////	    .andExpect(status().isBadRequest())
////	    .andExpect(content().string(containsString(errorMessage))); // Expect the correct error message
////	}
////
//
//
//}
