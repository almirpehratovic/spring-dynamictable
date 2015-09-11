package com.springtutor.controllers;

public class Message {
	private String text;
	private String cssClass;
	
	public Message(String text, String cssClass) {
		this.text = text;
		this.cssClass = cssClass;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getCssClass() {
		return cssClass;
	}
	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}
	
	
}
