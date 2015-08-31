/*
 * Library that search for oceanDynamicTable and adds pagination and search support.
 * Author: Almir Pehratovic, August 2015
 */
$(document).ready(function() {
	
	$('.oceanDynamicTable table').each(function(){
			
		var html;
		
		// NO DATA MESSAGE
		var collectionSize = parseInt($(this).attr('data-collectionSize'));
		if (collectionSize == 0){
			var noDataMessage = $(this).attr('data-noDataMessage');
			html = '<tr><td colspan="20">' + noDataMessage + '</td></tr>'; 
			$(this).find('tbody').html(html);
		}
		
		// ACTION BAR
		html = '<div class="ui-widget-header ui-corner-all">';
		
		// pagination
		html += '<div class="pagination">';
		var paginationFirst = parseInt(readCookie('paginationFirst','1'));
		var paginationSize = parseInt($(this).attr('data-paginationSize'));
		setCookie("paginationSize",paginationSize);
		
		var dataUrl = $(this).attr('data-dataUrl');
		var paginationLabelNext = $(this).attr('data-paginationLabelNext');
		var paginationLabelPrev = $(this).attr('data-paginationLabelPrev');
		
		html += '<input type="hidden" name="first" value="' + paginationFirst + '"/>';
		
		html += '<input type="submit" class="prev" value="' + paginationLabelPrev + '">' ;
		
		var prevButtonFunction;
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
		html += '<span>' + paginationFirst + '-' + (paginationFirst + paginationSize) + '</span>';
		html += '<input type="submit" class="next" value="' + paginationLabelNext + '">' ;
		var nextButtonFunction;
		if (collectionSize != 0){
			nextButtonFunction = function(){
				$('.next').click(function(){
					paginationFirst = paginationFirst+paginationSize;
					//$('input[name="first"]').val(paginationFirst);
					setCookie("paginationFirst",paginationFirst);
				});
			};
		} else {
			nextButtonFunction = function(){
				$('.next').attr('disabled','disabled');
				
			};
		}
		html += '</div>'; 
		
		var htmlBottom = html + "</div>";
		
		// search
		var searchIcon = $(this).attr('data-searchIcon');
		html += '<div class="search">';
		
		html += '<div class="filters">';
		$('[data-search]').each(function(){
			cookieValue = readCookie($(this).attr('data-search')+'Search','null');
			if (cookieValue != 'null' && cookieValue != ''){
				html += $(this).attr('data-search') + '=' + cookieValue + '; ';
			}
		});
		html += '</div>';
		
		html += '<select name="searchField" value="' + readCookie('searchField','') + '">';
		$('[data-search]').each(function(){
			if (readCookie('searchField','') == $(this).attr('data-search')){
				html += '<option value="' + $(this).attr('data-search') + '" selected>' + $(this).text() + '</option>';
			} else {
				html += '<option value="' + $(this).attr('data-search') + '">' + $(this).text() + '</option>';
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
		
		// SORT
		$('.oceanDynamicTable th[data-search]').each(function(){
			var orderBy = readCookie('orderBy', 'null');
			if (orderBy != 'null' &&  orderBy.indexOf($(this).attr('data-search')) > -1){
				if (orderBy.indexOf($(this).attr('data-search') + ' asc') > -1){
					$(this).append('<span class="asc"></span>');
				} else {
					$(this).append('<span class="desc"></span>');
				}
			}
		});
		$('.oceanDynamicTable th[data-search]').dblclick(function(){
			var orderBy = readCookie('orderBy', 'null');
			if (orderBy != 'null' &&  orderBy.indexOf($(this).attr('data-search')) > -1){
				if (orderBy.indexOf($(this).attr('data-search') + ' asc') > -1){
					setCookie('orderBy', $(this).attr('data-search') + ' desc');
				} else {
					setCookie('orderBy', $(this).attr('data-search') + ' asc');
				}
			} else {
				setCookie('orderBy', $(this).attr('data-search') + ' asc');
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
			var searchField = $('.search select[name="searchField"]').val();
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
		$('.oceanDynamicTable select').selectmenu({
			change: function(event,data){
				$('.search input').val(readCookie(data.item.value+'Search',''));
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