package com.poc.bookmanagement.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Entity
@Data
public class Books {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

    @NotBlank(message = "Book title cannot be null or empty")
	private String title;

    @NotBlank(message = "Book author cannot be null or empty")
	private String author;

	//new fields added
	private LocalDate issueDate;
    private LocalDate submissionDate;
    private String issuerName;

    @Enumerated(EnumType.STRING)  // Store the enum as a string in the database
    private BookStatus status;

    // Enum to represent the status of the book
    public enum BookStatus {
        ISSUED,
        SUBMITTED
    }

}
