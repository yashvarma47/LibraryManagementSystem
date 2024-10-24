package com.poc.bookmanagement.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poc.bookmanagement.entities.Books;

public interface BookRepository extends JpaRepository<Books, Integer> {


}
