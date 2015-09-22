# Spring Dynamic data table
## Preface
The main purpose of this project is to create data table that has pagination, sorting and search capabilities. All this features should be server based, or said in another words this table shouldn't get all data and then hide it, sorti it through JQuery; instead Spring MVC controller sends allready sorted, filtered and paginated data.

## Getting Started


Main idea is to have simple code for data table, for example:

```html
<div class="oceanDynamicTable">
	<c:if test="${not empty tableMessage}">
		<div class="${tableMessage.cssClass}">${tableMessage.text}</div>
	</c:if>
	<table ocean-dataUrl="${urlPagination}"
		ocean-collectionSize="${books.size()}"
		ocean-orderBy="price asc,rating desc" ocean-paginationSize="5"
		ocean-paginationLabelNext="Next" ocean-paginationLabelPrev="Prev"
		ocean-paginationLabelNew="New Book"
		ocean-searchIcon="${urlSearchIcon}" ocean-noDataMessage="No data">
		<thead>
			<tr>
				<th ocean-search-property="id">No</th>
				<th ocean-search-property="title">Title</th>
				<th ocean-search-property="releaseDate">Released</th>
				<th ocean-search-property="price" ocean-type="number">Price</th>
				<th ocean-search-property="rating" ocean-type="number">Rating</th>
				<th ocean-search-property="numberOfReviewers" ocean-type="number">Reviewers</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="book" items="${books}" varStatus="count">
				<tr>
					<td>${book.id}</td>
					<td ocean-link="${book.id}">${book.title}</td>
					<td><fmt:formatDate pattern="dd.MM.yyyy"
							value="${book.releaseDate}" /></td>
					<td class="num"><fmt:formatNumber type="currency"
							currencyCode="USD" currencySymbol="$" maxIntegerDigits="9"
							maxFractionDigits="2" value="${book.price}" /></td>
					<td class="num"><fmt:formatNumber type="number"
							maxIntegerDigits="1" maxFractionDigits="2" minFractionDigits="2"
							value="${book.rating}" /></td>
					<td class="num"><fmt:formatNumber type="number"
							value="${book.numberOfReviewers}" /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
```

Application (more precisely JQUery script) should recognize these kinds of data tables (class="oceanDynamicTable") and automatically insert search bar, pagination bar and sorting features. After user does something with inserted features, page is submitted and all relevant data (for example page number, filters, sortings) are available to MVC controller.

```java
@Controller
@RequestMapping("/books")
public class BooksController{
	private BookDao bookDao;
	
	private void setData(Model model,HttpServletRequest request,HttpServletResponse response,boolean isValidationError) {
		OceanDynamicTable odt = new OceanDynamicTable(request,response);
		
		List<Book> books = new ArrayList<Book>();
		
		if (odt.getSearchAttribute("HQL") != null && odt.getSearchAttribute("HQL").length() > 0) {
			try {
				books = bookDao.find(odt.getSearchAttribute("HQL"));
			} catch (QueryException e) {
				Message message = new Message(e.getMessage(), "error");
				model.addAttribute("tableMessage",message);
			}
		} else {
			books = bookDao.find(odt.getPaginationFirst(),odt.getPaginationSize(),
					odt.getSearchAttribute("title"),odt.getSearchAttribute("description"),
					odt.getOrderBy());
		}
		
		double averagePrice = books.size() == 0 ? 0 : books.stream().mapToDouble(w -> w.getPrice()).average().getAsDouble();
		double averageRating = books.size() == 0 ? 0 : books.stream().mapToDouble(w -> w.getRating()).average().getAsDouble();
		int reviewersSum = books.size() == 0 ? 0 : books.stream().mapToInt(w -> w.getNumberOfReviewers()).sum();
		
		model.addAttribute("books", books);
		model.addAttribute("averagePrice", averagePrice);
		model.addAttribute("averageRating", averageRating);
		model.addAttribute("sumReviewers", reviewersSum);
		
		if (!isValidationError && odt.getSelectedObjectId() != null && odt.getSelectedObjectId().length() > 0) {
			if (odt.getSelectedObjectId().equals("0")) {
				Book book = new Book();
				model.addAttribute("book",book);
			} else {
				Book book = bookDao.findById(Integer.parseInt(odt.getSelectedObjectId()));
				model.addAttribute("book",book);
			}
		}
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String showBooksDefault(Model model,HttpServletRequest request,HttpServletResponse response) {
		setData(model, request, response, false);
		return "books";
	}
	
	@RequestMapping(value="/getBooks",method=RequestMethod.POST)
	public String showMoviesSearchPagination(Model model,RedirectAttributes redirectAttributes,HttpServletRequest request,HttpServletResponse response) {
		return "redirect:/books";
	}
	
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String edit(@Valid Book book, BindingResult bindingResult, Model model,RedirectAttributes redirectAttributes,HttpServletRequest request,HttpServletResponse response) {
		Message message = null;
		if (bindingResult.hasErrors()) {
			message = new Message("Object is NOT saved.", "error");
			setData(model, request, response, true);
			model.addAttribute("formMessage",message);
			model.addAttribute("book", book);
			return "books";
		} else {
			bookDao.save(book);
			(new OceanDynamicTable(request, response)).setSelectedObjectId(String.valueOf(book.getId()));
			message = new Message("Object sucessfully saved.", "normal");
			redirectAttributes.addFlashAttribute("formMessage", message);
			return "redirect:/table2";
		}
	}
	
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(Book book,Model model,RedirectAttributes redirectAttributes,HttpServletRequest request, HttpServletResponse response) {
		bookDao.delete(book);
		OceanDynamicTable odt = new OceanDynamicTable(request, response);
		odt.setSelectedObjectId(null);
		Message message = new Message("Object sucessfully deleted.", "normal");
		redirectAttributes.addFlashAttribute("tableMessage", message);
		return "redirect:/books";
	}
	
	
	@Autowired
	public void setBookDao(BookDao bookDao) {
		this.bookDao = bookDao;
	}	
}
```
