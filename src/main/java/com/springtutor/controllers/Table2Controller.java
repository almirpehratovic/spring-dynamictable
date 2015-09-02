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

import com.springtutor.model.Book;
import com.springtutor.model.Movie;
import com.springtutor.service.BookDao;
import com.springtutor.service.MovieDao;

@Controller
@RequestMapping("/table2")
public class Table2Controller {
	private final int PAGE_SIZE = 5;
	private BookDao bookDao;
	
	// For each collection of data there has to be two methods. First method is default method which serves GET request,
	// and another is POST method that works with pagination and search
	
	@RequestMapping(method=RequestMethod.GET)
	public String showBooksDefault(Model model,
			@CookieValue(name="paginationSize",required=false,defaultValue="5") int pageSize,
			@CookieValue(name="paginationFirst",required=false,defaultValue="1") int first,
			@CookieValue(name="titleSearch",required=false) String title, 
			@CookieValue(name="descriptionSearch",required=false) String description,
			@CookieValue(name="orderBy",required=false) String orderBy) {
		
		if (!model.containsAttribute("books")) {
			List<Book> books = bookDao.findAll(first,pageSize,title,description,orderBy);
			double averagePrice = books.size() == 0 ? 0 : books.stream().mapToDouble(w -> w.getPrice()).average().getAsDouble();
			double averageRating = books.size() == 0 ? 0 : books.stream().mapToDouble(w -> w.getRating()).average().getAsDouble();
			int reviewersSum = books.size() == 0 ? 0 : books.stream().mapToInt(w -> w.getNumberOfReviewers()).sum();
			model.addAttribute("books", books);
			model.addAttribute("averagePrice", averagePrice);
			model.addAttribute("averageRating", averageRating);
			model.addAttribute("sumReviewers", reviewersSum);
		}
		return "books";
	}
	
	@RequestMapping(value="/getBooks",method=RequestMethod.POST)
	public String showMoviesSearchPagination(
			@CookieValue(name="paginationSize",required=false,defaultValue="5") int pageSize,
			@CookieValue(name="paginationFirst",required=false,defaultValue="1") int first,
			@CookieValue(name="titleSearch",required=false) String title, 
			@CookieValue(name="descriptionSearch",required=false) String description,
			@CookieValue(name="orderBy",required=false) String orderBy,
			Model model,RedirectAttributes redirectAttributes) {
		
		List<Book> books = bookDao.findAll(first,pageSize,title,description,orderBy);
		
		/*double averagePrice = 0; double averageRating = 0; double reviewersSum = 0;
		for (Book book : books) {
			averagePrice += book.getPrice();
			averageRating += book.getRating();
			reviewersSum += book.getNumberOfReviewers();
		}
		averagePrice = books.size() == 0 ? 0 : averagePrice / books.size();
		averageRating = books.size() == 0 ? 0 : averageRating / books.size();*/
		
		double averagePrice = books.size() == 0 ? 0 : books.stream().mapToDouble(w -> w.getPrice()).average().getAsDouble();
		double averageRating = books.size() == 0 ? 0 : books.stream().mapToDouble(w -> w.getRating()).average().getAsDouble();
		int reviewersSum = books.size() == 0 ? 0 : books.stream().mapToInt(w -> w.getNumberOfReviewers()).sum();
		
		redirectAttributes.addFlashAttribute("books", books);
		redirectAttributes.addFlashAttribute("averagePrice", averagePrice);
		redirectAttributes.addFlashAttribute("averageRating", averageRating);
		redirectAttributes.addFlashAttribute("sumReviewers", reviewersSum);
		
		return "redirect:/table2";
	}
	
	
	
	@Autowired
	public void setBookDao(BookDao bookDao) {
		this.bookDao = bookDao;
	}
	
	
}
