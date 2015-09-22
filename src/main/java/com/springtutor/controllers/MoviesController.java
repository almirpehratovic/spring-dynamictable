package com.springtutor.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.springtutor.model.Book;
import com.springtutor.model.Movie;
import com.springtutor.service.MovieDao;
import com.springtutor.service.OceanDynamicTable;

@Controller
@RequestMapping("/movies")
public class MoviesController {
	private final int PAGE_SIZE = 5;
	private MovieDao movieDao;
	
	// For each collection of data there has to be two methods. First method is default method which serves GET request,
	// and another is POST method that works with pagination and search
	
	@RequestMapping(method=RequestMethod.GET)
	public String showMoviesDefault(Model model, HttpServletRequest request,HttpServletResponse response) {
		
			
		if (!model.containsAttribute("movies")) {
			OceanDynamicTable odt = new OceanDynamicTable(request,response);
			model.addAttribute("movies", movieDao.findAll(odt.getPaginationFirst(),odt.getPaginationSize(),
					odt.getSearchAttribute("id"),odt.getSearchAttribute("name"),odt.getSearchAttribute("description"),
					odt.getOrderBy()));
		}
		return "movies";
	}
	
	@RequestMapping(value="/getMovies",method=RequestMethod.POST)
	public String showMoviesSearchPagination(Model model,RedirectAttributes redirectAttributes,HttpServletRequest request,HttpServletResponse response) {
		
		//System.out.println("ABC  post kontroler " + orderBy );
		OceanDynamicTable odt = new OceanDynamicTable(request,response);
		List<Movie> movies = movieDao.findAll(odt.getPaginationFirst(),odt.getPaginationSize(),
				odt.getSearchAttribute("id"),odt.getSearchAttribute("name"),odt.getSearchAttribute("description"),
				odt.getOrderBy());
		
		redirectAttributes.addFlashAttribute("movies", movies);
		return "redirect:/movies";
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
