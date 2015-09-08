package com.springtutor.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


public class OceanDynamicTable {
	
	private HttpServletRequest request = null;
	
	

	public OceanDynamicTable(HttpServletRequest request) {
		this.request = request;
	}

	public int getPaginationFirst() {
		return Integer.parseInt(getCookieValue("paginationFirst", "1"));
	}
	
	public int getPaginationSize() {
		return Integer.parseInt(getCookieValue("paginationSize","5"));
	}
	
	public String getOrderBy() {
		return getCookieValue("orderBy",null);
	}
	
	public String getSearchAttribute(String attributeName) {
		return getCookieValue(attributeName + "Search", null);
	}
	
	public String getSelectedObjectId() {
		return getCookieValue("selectedObject", null);
	}
	
	private String getCookieValue(String cookieName,String defaultValue) {
		String value = defaultValue;
		for (Cookie cookie : request.getCookies()) {
			if (cookie.getName().equals(cookieName)) {
				try {
					return URLDecoder.decode(cookie.getValue(),"UTF-8");
				} catch (UnsupportedEncodingException e) {
					return null;
				}
			}
		}
		return value;
	}
}
