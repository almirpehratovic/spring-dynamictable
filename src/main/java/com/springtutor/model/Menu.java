package com.springtutor.model;

import java.util.ArrayList;
import java.util.List;

public class Menu {
	private String id;
	private String title;
	private List<MenuItem> menuItems = new ArrayList<>();
	private List<Menu> submenus = new ArrayList<>();
	
	public Menu() {}
	
	public Menu(String id, String title) {
		this.id = id;
		this.title = title;
	}
	
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<MenuItem> getMenuItems() {
		return menuItems;
	}
	public void setMenuItems(List<MenuItem> menuItems) {
		this.menuItems = menuItems;
	}
	public List<Menu> getSubmenus() {
		return submenus;
	}
	public void setSubmenus(List<Menu> submenus) {
		this.submenus = submenus;
	}
	
	
}
