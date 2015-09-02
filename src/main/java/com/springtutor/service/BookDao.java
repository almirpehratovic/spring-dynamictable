package com.springtutor.service;

import java.util.List;

import com.springtutor.model.Book;

public interface BookDao {
	List<Book> findAll(int first, int size, String title, String description, String orderBy);
}
