/*
 * Library that search for oceanDynamicTable and adds pagination and search support.
 * Author: Almir Pehratovic, August 2015
 */
$(document).ready(function() {
	
	$('.oceanDynamicTable table').each(function(){
			
		var html, htmlBottom, noDataMessage, dataUrl, paginationLabelNext, paginationLabelPrev, searchIcon, orderBy, searchField;
		var cnt=0, collectionSize, paginationFirst, paginationSize;
		var pageSizes = [5,10,15,100,1000,0];
		var prevButtonFunction, nextButtonFunction;
		
		// NO DATA MESSAGE
		collectionSize = parseInt($(this).attr('data-collectionSize'));
		if (collectionSize == 0){
			noDataMessage = $(this).attr('data-noDataMessage');
			html = '<tr><td colspan="20">' + noDataMessage + '</td></tr>'; 
			$(this).find('tbody').html(html);
		}
		
		// ACTION BAR
		html = '<div class="ui-widget-header ui-corner-all">';
		
		// pagination
		html += '<div class="pagination">';
		paginationFirst = parseInt(readCookie('paginationFirst','1'));
		paginationSize = parseInt(readCookie('paginationSize','-1'));
		if (paginationSize == -1){
			paginationSize = parseInt($(this).attr('data-paginationSize'));
			setCookie("paginationSize",paginationSize);
		}
		
		dataUrl = $(this).attr('data-dataUrl');
		paginationLabelNext = $(this).attr('data-paginationLabelNext');
		paginationLabelPrev = $(this).attr('data-paginationLabelPrev');
		
		html += '<input type="hidden" name="first" value="' + paginationFirst + '"/>';
		
		html += '<input type="submit" class="prev" value="' + paginationLabelPrev + '">' ;
		
		if ((paginationFirst-paginationSize) > 0){
			prevButtonFunction = function(){
				$('.prev').click(function(){
					paginationFirst = paginationFirst-paginationSize;
					//$('input[name="first"]').val(paginationFirst);
					setCookie("paginationFirst",paginationFirst);
				});
			};
		} else {
			prevButtonFunction = function(){
				$('.prev').attr('disabled','disabled');
			};
		}
		
		if (paginationSize == 0){
			html += '<span class="current">' + '1-' + collectionSize + '</span>';
		} else {
			if (paginationSize > collectionSize){
				html += '<span class="current">' + paginationFirst + '-' + (paginationFirst + collectionSize - 1) + '</span>';
			} else {
				html += '<span class="current">' + paginationFirst + '-' + (paginationFirst + paginationSize - 1) + '</span>';
			}
			
		}
		
		html += '<input type="submit" class="next" value="' + paginationLabelNext + '">' ;

		if (collectionSize == paginationSize){
			nextButtonFunction = function(){
				$('.next').click(function(){
					paginationFirst = paginationFirst+paginationSize;
					setCookie("paginationFirst",paginationFirst);
				});
			};
		} else {
			nextButtonFunction = function(){
				$('.next').attr('disabled','disabled');
				
			};
		}
		html += '<select value="' + paginationSize + '">';
		
		for (cnt = 0; cnt < pageSizes.length; cnt++){
			
			if (pageSizes[cnt] == 0){
				label = "ALL";
			} else {
				label = pageSizes[cnt];
			}
			
			if (pageSizes[cnt] == paginationSize){
				html += '<option value="' + pageSizes[cnt] + '" selected>' + label + '</option>';
			} else {
				html += '<option value="' + pageSizes[cnt] + '">' + label + '</option>';
			}
		}
		html += '</select>';
		html += '</div>'; 
		
		htmlBottom = html + "</div>";
		
		// search
		searchIcon = $(this).attr('data-searchIcon');
		html += '<div class="search">';
		
		html += '<div class="filters">';
		$('[data-search-property]').each(function(){
			cookieValue = readCookie($(this).attr('data-search-property')+'Search','null');
			if (cookieValue != 'null' && cookieValue != ''){
				html += $(this).attr('data-search-property') + '=' + cookieValue + '; ';
			}
		});
		html += '</div>';
		
		html += '<select name="searchField" value="' + readCookie('searchField','') + '">';
		$('[data-search-property]').each(function(){
			if (readCookie('searchField','') == $(this).attr('data-search-property')){
				html += '<option value="' + $(this).attr('data-search-property') + '" selected>' + $(this).text() + '</option>';
			} else {
				html += '<option value="' + $(this).attr('data-search-property') + '">' + $(this).text() + '</option>';
			}
		});
		
		html += '</select>';
		html += '<div class="searchInput">';
		html += '<input type="text" name="searchValue" value="' + readCookie('searchValue','') + '"/>';
		html += '<a href="" class="searchLink"><img src="' + searchIcon + '"/></a>';
		html += '</div>';
		html += '</div>';
		
		html += '</div>'; // actionbar
		
		html = '<form class="searchForm" action="' + dataUrl + '" method="POST">' + html + '</form>';
		htmlBottom = '<form class="searchForm" action="' + dataUrl + '" method="POST">' + htmlBottom + '</form>';
		
		$(this).before(html);
		$(this).after(html);
		
		// Hide aggregation row if there is a pagination
		if (paginationSize != 0 && (paginationFirst!=1) || (paginationFirst==1 && collectionSize == paginationSize)){
			$('td.aggr:eq(0)').parent('tr').remove();
		}
		
		// SORT
		$('.oceanDynamicTable th[data-search-property]').each(function(){
			orderBy = readCookie('orderBy', 'null');
			if (orderBy == 'null'){
				$(this).parents('table[data-orderBy]').each(function(){
					orderBy = $(this).attr('data-orderBy');
					setCookie('orderBy', orderBy);
				});
			}
			
			if (orderBy != 'null' &&  orderBy.indexOf($(this).attr('data-search-property')) > -1){
				if (orderBy.indexOf($(this).attr('data-search-property') + ' asc') > -1){
					$(this).append('<span class="asc"></span>');
				} else {
					$(this).append('<span class="desc"></span>');
				}
			}
		});
		$('.oceanDynamicTable th[data-search-property]').dblclick(function(){
			orderBy = readCookie('orderBy', 'null');
			if (orderBy != 'null' &&  orderBy.indexOf($(this).attr('data-search-property')) > -1){
				if (orderBy.indexOf($(this).attr('data-search-property') + ' asc') > -1){
					setCookie('orderBy', $(this).attr('data-search-property') + ' desc');
				} else {
					setCookie('orderBy', $(this).attr('data-search-property') + ' asc');
				}
			} else {
				setCookie('orderBy', $(this).attr('data-search-property') + ' asc');
			}
			
			$('.searchForm:eq(0)').submit();
		});
		
		prevButtonFunction();
		nextButtonFunction();
		
		$('.search .searchLink').click(function(){
			setCookie('searchField', $('.search select[name="searchField"]').val());
			$(this).parents('form').submit();
		});
		
		$('.search input[name="searchValue"]').keyup(function(event){
			searchField = $('.search select[name="searchField"]').val();
			if(event.which == 13){
				setCookie('searchField', searchField);
				$(this).parents('form').submit();
			} else {
				setCookie('searchValue', $(this).val());
				setCookie(searchField + 'Search', $(this).val());
			}
        });
		
		$('.oceanDynamicTable .pagination .prev').button();
		$('.oceanDynamicTable .pagination .next').button();
		
		$('.oceanDynamicTable .search select').selectmenu({
			change: function(event,data){
				$('.search input').val(readCookie(data.item.value+'Search',''));
			}
		});
		
		$('.oceanDynamicTable .pagination select').selectmenu({
			change: function(event,data){
				setCookie("paginationSize",data.item.value);
				setCookie('paginationFirst','1');
				$(this).parents('form').submit();
			}
		});
	});
	
	// TABLE REFLOW
	reflowTable();
	$(window).resize(function(){
		reflowTable();
	});
	
	
	
});

var reflowTable = function(){
	$('.mobileLabel').remove();
	if ($(window).width() <= 480) {
		$('.oceanDynamicTable table tbody td').each(function(){
			var index = $(this).index();
			var header = $(this).closest('table').find('thead th').get(index);
			console.log($(header).text());
			$(this).prepend('<span class="mobileLabel">' + $(header).text() + ':</span>');
		});
	}
}

var readCookie = function(cookieName,defaultValue){
	val = $.cookie(cookieName);
	console.log("cookie: " + val);
	if (val === undefined){
		return defaultValue;
	}
	return val;
}

var setCookie = function(cookieName,cookieValue){
	$.cookie(cookieName,cookieValue);
}