package com.springtutor.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springtutor.model.Menu;
import com.springtutor.model.MenuItem;

@Controller
@RequestMapping("/menu")
public class MenuController {
	private Menu main = new Menu("0","Main");
	
	public MenuController() {
		createStaticMenu();
	}

	private void createStaticMenu() {
		Menu m1 = new Menu("1","Entertainment");
		Menu m11 = new Menu("11","Books");
		MenuItem m11i1 = new MenuItem("Books Catalog", "books");
		m11.getMenuItems().add(m11i1);
		
		Menu m12 = new Menu("12","Movies");
		MenuItem m12i1 = new MenuItem("Movies Catalog", "movies");
		m12.getMenuItems().add(m12i1);
		
		Menu m13 = new Menu("13","Games");
		
		m1.getSubmenus().add(m11);
		m1.getSubmenus().add(m12);
		m1.getSubmenus().add(m13);
		
		Menu m2 = new Menu("2","Work");
		Menu m3 = new Menu("3","Family");
		
		main.getSubmenus().add(m1);
		main.getSubmenus().add(m2);
		main.getSubmenus().add(m3);
		
	}
	
	@RequestMapping(method=RequestMethod.GET)
	@ResponseBody
	public Menu getMenu() {
		return main;
	}
}
