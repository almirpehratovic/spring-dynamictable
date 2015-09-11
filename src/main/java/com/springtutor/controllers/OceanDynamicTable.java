package com.springtutor.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class OceanDynamicTable {
	private static final String COOKIE_PREFIX = "odt-";
	private HttpServletRequest request = null;
	private HttpServletResponse response = null;
	
	public OceanDynamicTable(HttpServletRequest request,HttpServletResponse response) {
		this.request = request;
		this.response = response;
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
	
	public void setSelectedObjectId(String id) {
		setCookieValue("selectedObject", id);
	}
	
	public String getSelectedObjectId() {
		return getCookieValue("selectedObject", null);
	}
	
	private String getCookieValue(String cookieName,String defaultValue) {
		String value = defaultValue;
		for (Cookie cookie : request.getCookies()) {
			if (cookie.getName().equals(COOKIE_PREFIX + cookieName)) {
				try {
					return URLDecoder.decode(cookie.getValue(),"UTF-8");
				} catch (UnsupportedEncodingException e) {
					return null;
				}
			}
		}
		return value;
	}
	
	private void setCookieValue(String cookieName, String cookieValue) {
		for (Cookie cookie : request.getCookies()) {
			if (cookie.getName().equals(COOKIE_PREFIX + cookieName)) {
				cookie.setValue(cookieValue);
				cookie.setPath("/");
				response.addCookie(cookie);
				break;
			}
		}
	}
}
