<!DOCTYPE html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
	<head>
		<meta charset="utf-8">
		<title>Welcome</title>
	</head> 
	<body>
		<c:url value="/table" var="tableUrl1" />
		<c:url value="/table2" var="tableUrl2" />
		
		<p><a href="${tableUrl1}">Dynamic Table example 1</a></p>
		<p><a href="${tableUrl2}">Dynamic Table example 2</a></p>
	</body>
</html>