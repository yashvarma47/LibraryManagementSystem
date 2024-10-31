package com.poc.bookmanagement.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poc.bookmanagement.entities.Books;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Books, Integer> {
    Optional<Books> findByIdAndStatus(int id, Books.BookStatus status);
    Optional<Books> findByIdAndStatusAndAuthor(int id, Books.BookStatus bookStatus, String author);
}
