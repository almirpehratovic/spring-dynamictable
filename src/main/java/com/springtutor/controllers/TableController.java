package com.springtutor.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springtutor.model.Movie;
import com.springtutor.service.MovieDao;

@Controller
@RequestMapping("/table")
public class TableController {
	private final int PAGE_SIZE = 5;
	private MovieDao movieDao;
	
	// For each collection of data there has to be two methods. First method is default method which serves GET request,
	// and another is POST method that works with pagination and search
	
	@RequestMapping(method=RequestMethod.GET)
	public String showMoviesDefault(Model model,
			@CookieValue(name="paginationSize",required=false,defaultValue="5") int pageSize,
			@CookieValue(name="paginationFirst",required=false,defaultValue="1") int first,
			@CookieValue(name="nameSearch",required=false) String name, 
			@CookieValue(name="descriptionSearch",required=false) String description,
			@CookieValue(name="orderBy",required=false) String orderBy) {
		
		if (!model.containsAttribute("movies")) {
			model.addAttribute("movies", movieDao.findAll(first,pageSize,name,description,orderBy));
		}
		return "default";
	}
	
	@RequestMapping(value="/getMovies",method=RequestMethod.POST)
	public String showMoviesSearchPagination(
			@CookieValue(name="paginationSize",required=false,defaultValue="5") int pageSize,
			@CookieValue(name="paginationFirst",required=false,defaultValue="1") int first,
			@CookieValue(name="nameSearch",required=false) String name, 
			@CookieValue(name="descriptionSearch",required=false) String description,
			@CookieValue(name="orderBy",required=false) String orderBy,
			Model model,RedirectAttributes redirectAttributes) {
		
		System.out.println("ABC  post kontroler " + orderBy );
		List<Movie> movies = movieDao.findAll(first,pageSize,name,description,orderBy);
		
		redirectAttributes.addFlashAttribute("movies", movies);
		return "redirect:/table";
	}
	
	@RequestMapping(value="/image/{id}",method=RequestMethod.GET)
	@ResponseBody
	public byte[] downloadImage(@PathVariable("id") int id) {
		Movie movie = movieDao.findById(id);
		return movie.getImage();
	}
	
	@Autowired
	public void setMovieDao(MovieDao movieDao) {
		this.movieDao = movieDao;
	}
	
	
}
