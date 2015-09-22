<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="my" uri="https://springtutor.wordpress.com"%>


<c:url value="/movies/image" var="urlImage" />
<c:url value="/movies/getMovies" var="urlData" />
<c:url value="/resources/images/search-icon24.png" var="urlSearchIcon" />


<div class="oceanDynamicTable">
	<table ocean-dataUrl="${urlData}" ocean-collectionSize="${movies.size()}"
		ocean-paginationSize="5" ocean-paginationLabelNext="Next"
		ocean-paginationLabelPrev="Prev" ocean-paginationLabelNew="New Movie"
		ocean-searchIcon="${urlSearchIcon}" ocean-noDataMessage="No data">
		<thead>
			<tr>
				<th ocean-search-property="id">No</th>
				<th ocean-search-property="name">Name</th>
				<th ocean-search-property="description">Description</th>
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