<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url value="/j_spring_security_logout" var="urlLogout" />

<a href="${urlLogout}">Logout</a>