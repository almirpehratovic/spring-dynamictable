package com.springtutor.controllers;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.springtutor.service.OceanDynamicTable;

/*
 * Default interceptor that forward to every handler method utility objects (OceanDynamicTable)
 */
public class Interceptor implements HandlerInterceptor{
	
	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res,
			Object handler) throws Exception {
		OceanDynamicTable odt = new OceanDynamicTable(req,res);
		req.setAttribute("odt",odt);
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest req, HttpServletResponse res,
			Object handler, ModelAndView modelAndView) throws Exception {
		req.removeAttribute("odt");
		modelAndView.addObject("currentDateTime",new Date());
	}

	@Override
	public void afterCompletion(HttpServletRequest req,
			HttpServletResponse res, Object handler, Exception ex)
			throws Exception {
		
	}
	

}
