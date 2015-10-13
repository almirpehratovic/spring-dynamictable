<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url value="/logout" var="urlLogout" />

<form id="logoutForm" method="post" action="${urlLogout}">
	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
	<input type="submit" value="Logout" />
</form>