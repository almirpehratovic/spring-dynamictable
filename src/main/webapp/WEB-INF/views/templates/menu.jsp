<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<spring:url value="/" var="urlRoot" />
<spring:url value="/menu" var="urlMenu" />

<nav class="menu">

</nav>

<script type="text/javascript">
    $(document).ready(function(){
    	createMenu("${urlRoot}","${urlMenu}");
    });
</script>