package com.poc.bookmanagement.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;


@Entity
@Data
public class Books {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String title;
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
