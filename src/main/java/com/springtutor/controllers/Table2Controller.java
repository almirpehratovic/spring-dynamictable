package com.springtutor.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.hibernate.QueryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
	
	private void setData(Model model,HttpServletRequest request,HttpServletResponse response,boolean isValidationError) {
		OceanDynamicTable odt = new OceanDynamicTable(request,response);
		
		List<Book> books = new ArrayList<Book>();
		
		if (odt.getSearchAttribute("HQL") != null && odt.getSearchAttribute("HQL").length() > 0) {
			try {
				books = bookDao.find(odt.getSearchAttribute("HQL"));
			} catch (QueryException e) {
				Message message = new Message(e.getMessage(), "error");
				model.addAttribute("tableMessage",message);
			}
		} else {
			books = bookDao.find(odt.getPaginationFirst(),odt.getPaginationSize(),
					odt.getSearchAttribute("title"),odt.getSearchAttribute("description"),
					odt.getOrderBy());
		}
		
		double averagePrice = books.size() == 0 ? 0 : books.stream().mapToDouble(w -> w.getPrice()).average().getAsDouble();
		double averageRating = books.size() == 0 ? 0 : books.stream().mapToDouble(w -> w.getRating()).average().getAsDouble();
		int reviewersSum = books.size() == 0 ? 0 : books.stream().mapToInt(w -> w.getNumberOfReviewers()).sum();
		
		model.addAttribute("books", books);
		model.addAttribute("averagePrice", averagePrice);
		model.addAttribute("averageRating", averageRating);
		model.addAttribute("sumReviewers", reviewersSum);
		
		if (!isValidationError && odt.getSelectedObjectId() != null && odt.getSelectedObjectId().length() > 0) {
			if (odt.getSelectedObjectId().equals("0")) {
				Book book = new Book();
				model.addAttribute("book",book);
			} else {
				Book book = bookDao.findById(Integer.parseInt(odt.getSelectedObjectId()));
				model.addAttribute("book",book);
			}
		}
	}
	
	// For each collection of data there has to be two methods. First method is default method which serves GET request,
	// and another is POST method that works with pagination and search
	
	@RequestMapping(method=RequestMethod.GET)
	public String showBooksDefault(Model model,HttpServletRequest request,HttpServletResponse response) {
		setData(model, request, response, false);
		return "books";
	}
	
	@RequestMapping(value="/getBooks",method=RequestMethod.POST)
	public String showMoviesSearchPagination(Model model,RedirectAttributes redirectAttributes,HttpServletRequest request,HttpServletResponse response) {
		return "redirect:/table2";
	}
	
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String edit(@Valid Book book, BindingResult bindingResult, Model model,RedirectAttributes redirectAttributes,HttpServletRequest request,HttpServletResponse response) {
		Message message = null;
		if (bindingResult.hasErrors()) {
			message = new Message("Object is NOT saved.", "error");
			setData(model, request, response, true);
			model.addAttribute("formMessage",message);
			model.addAttribute("book", book);
			return "books";
		} else {
			bookDao.save(book);
			(new OceanDynamicTable(request, response)).setSelectedObjectId(String.valueOf(book.getId()));
			message = new Message("Object sucessfully saved.", "normal");
			redirectAttributes.addFlashAttribute("formMessage", message);
			return "redirect:/table2";
		}
	}
	
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(Book book,Model model,RedirectAttributes redirectAttributes,HttpServletRequest request, HttpServletResponse response) {
		bookDao.delete(book);
		OceanDynamicTable odt = new OceanDynamicTable(request, response);
		odt.setSelectedObjectId(null);
		Message message = new Message("Object sucessfully deleted.", "normal");
		redirectAttributes.addFlashAttribute("tableMessage", message);
		return "redirect:/table2";
	}
	
	
	@Autowired
	public void setBookDao(BookDao bookDao) {
		this.bookDao = bookDao;
	}
	
	
}
