# Spring Dynamic data table
## Preface
The main purpose of this project is to create data table that has pagination, sorting and search capabilities. All this features should be server based, or said in another words this table shouldn't get all data and then hide it, sorti it through JQuery; instead Spring MVC controller sends allready sorted, filtered and paginated data.

## Getting Started


Main idea is to have simple code for data table, for example:

```html
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
```

Application (more precisely JQUery script) should recognize these kinds of data tables (class="oceanDynamicTable") and automatically insert search bar, pagination bar and sorting features. After user does something with inserted features, page is submitted and all relevant data (for example page number, filters, sortings) are available to MVC controller.

```java
@RequestMapping(value="/getMovies",method=RequestMethod.POST)
public String showMoviesSearchPagination(
		@CookieValue(name="paginationSize",required=false,defaultValue="5") int pageSize,
		@CookieValue(name="paginationFirst",required=false,defaultValue="1") int first,
		@CookieValue(name="nameSearch",required=false) String name, 
		@CookieValue(name="descriptionSearch",required=false) String description,
		@CookieValue(name="orderBy",required=false) String orderBy,
		Model model,RedirectAttributes redirectAttributes) {
		
	List<Movie> movies = movieDao.findAll(first,pageSize,name,description,orderBy);
		
	redirectAttributes.addFlashAttribute("movies", movies);
	return "redirect:/table";
}
```
