<!DOCTYPE html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:url value="/resources/scripts/cookie/jquery.cookie.js" var="cookieScript" />

<html>
	<head>
		<meta charset="utf-8">
		<title>Welcome</title>
		<script src="http://code.jquery.com/jquery-1.11.3.min.js"></script>
		<script src="${cookieScript}"></script>
		<script type="text/javascript">
			$(document).ready(function(){
				for (cookie in $.cookie()){
					if (cookie.indexOf('oc-odt-') != -1)
						$.removeCookie(cookie,{ path: '/' });
				}
			});
		</script>
	</head> 
	<body>
		<c:url value="/movies" var="tableUrl1" />
		<c:url value="/books" var="tableUrl2" />
		
		<p><a href="${tableUrl1}">Movies</a></p>
		<p><a href="${tableUrl2}">Books</a></p>
	</body>
</html>