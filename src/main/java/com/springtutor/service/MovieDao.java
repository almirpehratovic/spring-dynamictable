package com.springtutor.service;

import java.util.List;

import com.springtutor.model.Movie;

public interface MovieDao {
	List<Movie> findAll();
	Movie findById(int id);
	List<Movie> findAll(int first, int size, String id,String name, String description, String orderBy);
}
