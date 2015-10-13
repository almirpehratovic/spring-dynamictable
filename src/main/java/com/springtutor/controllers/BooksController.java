package com.springtutor.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.hibernate.QueryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springtutor.model.Book;
import com.springtutor.model.Message;
import com.springtutor.service.BookDao;
import com.springtutor.service.OceanDynamicTable;

@Controller
@RequestMapping("/books")
public class BooksController{
	private BookDao bookDao;
	
	private void setData(Model model,HttpServletRequest request,HttpServletResponse response,boolean isValidationError) {
		OceanDynamicTable odt = (OceanDynamicTable) request.getAttribute("odt");
		
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
		return "redirect:/books";
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
			OceanDynamicTable odt = (OceanDynamicTable) request.getAttribute("odt");
			odt.setSelectedObjectId(String.valueOf(book.getId()));
			message = new Message("Object sucessfully saved.", "normal");
			redirectAttributes.addFlashAttribute("formMessage", message);
			return "redirect:/books";
		}
	}
	
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(Book book,Model model,RedirectAttributes redirectAttributes,HttpServletRequest request, HttpServletResponse response) {
		bookDao.delete(book);
		OceanDynamicTable odt = (OceanDynamicTable) request.getAttribute("odt");
		odt.setSelectedObjectId(null);
		Message message = new Message("Object sucessfully deleted.", "normal");
		redirectAttributes.addFlashAttribute("tableMessage", message);
		return "redirect:/books";
	}
	
	
	@Autowired
	public void setBookDao(BookDao bookDao) {
		this.bookDao = bookDao;
	}
	
	
}
