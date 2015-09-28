<!DOCTYPE html>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="my" uri="https://springtutor.wordpress.com"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<spring:url value="/resources/scripts/main.css" var="mainCss" />
<spring:url value="/resources/scripts/main.js" var="mainScript" />
<spring:url value="/resources/scripts/dynamictable.css"
	var="dynamicTableCss" />
<spring:url value="/resources/scripts/dynamictable.js"
	var="dynamicTableScript" />

<spring:url value="/resources/scripts/tagit/tag-it.min.js"
	var="tagitScript" />
<spring:url value="/resources/scripts/cookie/jquery.cookie.js"
	var="cookieScript" />
	
<spring:url value="/login" var="urlLogin" />

<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width">
<title><tiles:insertAttribute name="pageTitle" ignore="true" /></title>

<script src="http://code.jquery.com/jquery-1.11.3.min.js"></script>
<link rel="stylesheet"
	href="http://code.jquery.com/ui/1.11.4/themes/start/jquery-ui.css" />
<script src="http://code.jquery.com/ui/1.11.4/jquery-ui.min.js"></script>
<link rel="stylesheet" href="${mainCss}" />
<script src="${mainScript}"></script>
<script src="${cookieScript}"></script>
<link rel="stylesheet" href="${dynamicTableCss}" />
<script src="${dynamicTableScript}"></script>
</head>
<body>
	<security:authorize access="isAuthenticated()">
	<div class="page">
		<header>
			<tiles:insertAttribute name="header" ignore="true" />
		</header>
		<div class="content">
			<aside>
				<tiles:insertAttribute name="menu" ignore="true" />
			</aside>
			<div class="main">
				<tiles:insertAttribute name="body" />
				<footer>
					<tiles:insertAttribute name="footer" ignore="true" />
				</footer>
			</div>
		</div>
	</div>
	</security:authorize>
	<security:authorize access="isAnonymous()">
		<p>Unauthorized access. Please <a href="${urlLogin}">login</a></p>
	</security:authorize>
</body>
</html>