<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<spring:url value="/" var="urlRoot" />
<spring:url value="/menu" var="urlMenu" />
<spring:url value="/j_spring_security_logout" var="urlLogout" />

<nav class="menu">

</nav>

<script type="text/javascript">
    $(document).ready(function(){
    	createMenu("${urlRoot}","${urlMenu}","${urlLogout}");
    });
</script>