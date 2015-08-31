
<!DOCTYPE html>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="my" uri="https://springtutor.wordpress.com"%>

<c:url value="/resources/styles/main.css" var="mainCss" />
<c:url value="/resources/scripts/dynamictable.css" var="dynamicTableCss" />
<c:url value="/resources/scripts/dynamictable.js" var="dynamicTableScript" />

<c:url value="/resources/scripts/tagit/tag-it.min.js" var="tagitScript" />
<c:url value="/resources/scripts/cookie/jquery.cookie.js" var="cookieScript" />

<html>
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width">
		<title>Dynamic table</title>
		<link rel="stylesheet" href="${mainCss}" />
		<script src="http://code.jquery.com/jquery-1.11.3.min.js"></script>
		<link rel="stylesheet" href="http://code.jquery.com/ui/1.11.4/themes/start/jquery-ui.css" />
		<script src="http://code.jquery.com/ui/1.11.4/jquery-ui.min.js"></script>
		<script src="${tagitScript}"></script>
		<script src="${cookieScript}"></script>
		<link rel="stylesheet" href="${dynamicTableCss}" />
		<script src="${dynamicTableScript}"></script>
	</head>
	<body>
		<c:url value="/table/image" var="urlImage" />
		<c:url value="/table/getMovies" var="urlData" />
		<c:url value="/resources/images/search-icon24.png" var="urlSearchIcon" />
		
		 
		 <div class="oceanDynamicTable">
		 	<table data-dataUrl="${urlData}" data-collectionSize="${movies.size()}"
		 			data-paginationSize="5" data-paginationLabelNext="Next" data-paginationLabelPrev="Prev" 
		 			data-searchIcon="${urlSearchIcon}" data-noDataMessage="No data" >
		 		<thead>
		 			<tr>
		 				<th data-search="id">No</th>
		 				<th data-search="name">Name</th>
		 				<th data-search="description">Description</th>
		 				<th>Poster</th>
		 			</tr>
		 		</thead>
		 		<tbody>
		 			<c:forEach var="movie" items="${movies}">
		 				<tr>
		 					<td>${movie.id}</td>
		 					<td>${movie.name}</td>
		 					<td>${movie.description}</td>
		 					<td><img src="${urlImage}/${movie.id}" /></td>	
		 				</tr>
		 			</c:forEach>
		 		</tbody>
		 	</table>
		 </div>
	</body>
</html>