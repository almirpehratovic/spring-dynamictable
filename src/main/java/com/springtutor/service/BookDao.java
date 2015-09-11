package com.springtutor.service;

import java.util.List;

import org.hibernate.QueryException;

import com.springtutor.model.Book;

public interface BookDao {
	Book findById(int id);
	List<Book> find(int first, int size, String title, String description, String orderBy);
	List<Book> find(String HQL) throws QueryException;
	Book save(Book book);
	void delete(Book book);
}
