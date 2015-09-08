package com.springtutor.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.Cookie;
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
public class Table2Controller{
	private BookDao bookDao;
	
	// For each collection of data there has to be two methods. First method is default method which serves GET request,
	// and another is POST method that works with pagination and search
	
	@RequestMapping(method=RequestMethod.GET)
	public String showBooksDefault(Model model,HttpServletRequest request) {

		if (!model.containsAttribute("books")) {
			OceanDynamicTable odt = new OceanDynamicTable(request);
			List<Book> books = bookDao.findAll(odt.getPaginationFirst(),odt.getPaginationSize(),
					odt.getSearchAttribute("title"),odt.getSearchAttribute("description"),
					odt.getOrderBy());
			
			double averagePrice = books.size() == 0 ? 0 : books.stream().mapToDouble(w -> w.getPrice()).average().getAsDouble();
			double averageRating = books.size() == 0 ? 0 : books.stream().mapToDouble(w -> w.getRating()).average().getAsDouble();
			int reviewersSum = books.size() == 0 ? 0 : books.stream().mapToInt(w -> w.getNumberOfReviewers()).sum();
			model.addAttribute("books", books);
			model.addAttribute("averagePrice", averagePrice);
			model.addAttribute("averageRating", averageRating);
			model.addAttribute("sumReviewers", reviewersSum);
			if (odt.getSelectedObjectId() != null && odt.getSelectedObjectId().length() > 0) {
				Book book = bookDao.findById(Integer.parseInt(odt.getSelectedObjectId()));
				model.addAttribute("book",book);
			}
		}
		return "books";
	}
	
	@RequestMapping(value="/getBooks",method=RequestMethod.POST)
	public String showMoviesSearchPagination(Model model,RedirectAttributes redirectAttributes,HttpServletRequest request) {
		
		OceanDynamicTable odt = new OceanDynamicTable(request);
		List<Book> books = bookDao.findAll(odt.getPaginationFirst(),odt.getPaginationSize(),
				odt.getSearchAttribute("title"),odt.getSearchAttribute("description"),
				odt.getOrderBy());
		
		
		double averagePrice = books.size() == 0 ? 0 : books.stream().mapToDouble(w -> w.getPrice()).average().getAsDouble();
		double averageRating = books.size() == 0 ? 0 : books.stream().mapToDouble(w -> w.getRating()).average().getAsDouble();
		int reviewersSum = books.size() == 0 ? 0 : books.stream().mapToInt(w -> w.getNumberOfReviewers()).sum();
		
		redirectAttributes.addFlashAttribute("books", books);
		redirectAttributes.addFlashAttribute("averagePrice", averagePrice);
		redirectAttributes.addFlashAttribute("averageRating", averageRating);
		redirectAttributes.addFlashAttribute("sumReviewers", reviewersSum);
		
		if (odt.getSelectedObjectId() != null && odt.getSelectedObjectId().length() > 0) {
			Book book = bookDao.findById(Integer.parseInt(odt.getSelectedObjectId()));
			redirectAttributes.addFlashAttribute("book",book);
		}
		
		return "redirect:/table2";
	}
	
	
	
	@Autowired
	public void setBookDao(BookDao bookDao) {
		this.bookDao = bookDao;
	}
	
	
}
