package com.springtutor.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/*
 * Enables displaying of error pages for various exception types
 */

@ControllerAdvice
public class ExceptionHandlingAdvice {
	@ExceptionHandler
	public ModelAndView handleDefaultError(Exception exception) {
		ModelAndView view = new ModelAndView("error");
		view.addObject("message", exception.getMessage());
		return view;
	}
}
