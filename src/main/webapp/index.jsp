<!DOCTYPE html>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<c:url value="/resources/scripts/cookie/jquery.cookie.js" var="cookieScript" />
<spring:url value="/resources/scripts/login.css" var="loginCss" />

<html>
<head>
<meta charset="utf-8">
<title>Welcome</title>
<link rel="stylesheet" href="${loginCss}" />
<script src="http://code.jquery.com/jquery-1.11.3.min.js"></script>
<script src="${cookieScript}"></script>
<script type="text/javascript">
	$(document).ready(function() {
		for (cookie in $.cookie()) {
			if (cookie.indexOf('oc-odt-') != -1)
				$.removeCookie(cookie, {
					path : '/'
				});
		}
	});
</script>
</head>
<body>
    <spring:url value="/j_spring_security_check" var="loginUrl" />
    <spring:url value="/j_spring_security_logout" var="logoutUrl" />
    <div class="strip">
    	&nbsp;
	</div>
	<div class="login">
		<security:authorize access="isAnonymous()">
			<form name="loginForm" action="${loginUrl}" method="post">
				<label>Username</label>
				<input type="text" name="j_username" autofocus"/> <br>
				<label>Password</label>
				<input type="password" name="j_password" /> <br>
				<label></label>
				<input name="submit" type="submit" value="Login" />
			</form>
		</security:authorize>
		<security:authorize access="isAuthenticated()">
			<a href="${logoutUrl}">Logout</a>
		</security:authorize>
	</div>
</body>
</html>