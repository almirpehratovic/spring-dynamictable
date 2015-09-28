package com.springtutor.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/login")
public class SecurityController {
	@RequestMapping(method=RequestMethod.GET)
	public String login() {
		return "index";
	}
}
