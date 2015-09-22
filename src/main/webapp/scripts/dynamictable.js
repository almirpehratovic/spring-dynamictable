/*
 * Library that search for oceanDynamicTable and adds pagination and search support.
 * Author: Almir Pehratovic, August 2015
 */
$(document).ready(function() {
	
	$('.oceanDynamicTable table').each(function(){
			
		var html, htmlBottom, noDataMessage, dataUrl, paginationLabelNext, paginationLabelPrev, searchIcon, orderBy, 
		    searchField, selectedObject, searchValue;
		var cnt=0, collectionSize, paginationFirst, paginationSize;
		var pageSizes = [5,10,15,100,1000,0];
		var prevButtonFunction, nextButtonFunction;
		
		// NO DATA MESSAGE
		collectionSize = parseInt($(this).attr('ocean-collectionSize'));
		if (collectionSize == 0){
			noDataMessage = $(this).attr('ocean-noDataMessage');
			html = '<tr><td colspan="20">' + noDataMessage + '</td></tr>'; 
			$(this).find('tbody').html(html);
		}
		
		// ACTION BAR
		html = '<div class="ui-widget-header ui-corner-all">';
		
		// pagination
		html += '<div class="pagination">';
		paginationFirst = parseInt(readCookie('odt-paginationFirst','1'));
		paginationSize = parseInt(readCookie('odt-paginationSize','-1'));
		if (paginationSize == -1){
			paginationSize = parseInt($(this).attr('ocean-paginationSize'));
			setCookie("odt-paginationSize",paginationSize);
		}
		
		dataUrl = $(this).attr('ocean-dataUrl');
		paginationLabelNext = $(this).attr('ocean-paginationLabelNext');
		paginationLabelPrev = $(this).attr('ocean-paginationLabelPrev');
		paginationLabelNew = $(this).attr('ocean-paginationLabelNew');
		
		html += '<input type="hidden" name="first" value="' + paginationFirst + '"/>';
		
		html += '<input type="submit" class="prev" value="' + paginationLabelPrev + '"/>' ;
		
		if ((paginationFirst-paginationSize) > 0){
			prevButtonFunction = function(){
				$('.prev').click(function(){
					paginationFirst = paginationFirst-paginationSize;
					//$('input[name="first"]').val(paginationFirst);
					setCookie("odt-paginationFirst",paginationFirst);
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
		
		html += '<input type="submit" class="next" value="' + paginationLabelNext + '"/>' ;

		if (collectionSize == paginationSize){
			nextButtonFunction = function(){
				$('.next').click(function(){
					paginationFirst = paginationFirst+paginationSize;
					setCookie("odt-paginationFirst",paginationFirst);
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
		
		html += '<button class="new">' + paginationLabelNew + '</button>' ;
		
		html += '</div>'; 
		
		htmlBottom = html + "</div>";
		
		// search
		searchIcon = $(this).attr('ocean-searchIcon');
		html += '<div class="search">';
		
		html += '<div class="filters">';
		if (readCookie('odt-HQLSearch','null') != 'null'){
			html += readCookie('odt-HQLSearch','null');
		} else {
			$('[ocean-search-property]').each(function(){
				cookieValue = readCookie('odt-'+$(this).attr('ocean-search-property')+'Search','null');
				if (cookieValue != 'null' && cookieValue != ''){
					html += $(this).attr('ocean-search-property') + '=' + cookieValue + '; ';
				}
			});
		}
		html += '</div>';
		
		html += '<select name="searchField" value="' + readCookie('odt-searchField','') + '">';
		$('[ocean-search-property]').each(function(){
			if (readCookie('odt-searchField','') == $(this).attr('ocean-search-property')){
				html += '<option value="' + $(this).attr('ocean-search-property') + '" selected>' + $(this).text() + '</option>';
			} else {
				html += '<option value="' + $(this).attr('ocean-search-property') + '">' + $(this).text() + '</option>';
			}
		});
		
		if (readCookie('odt-searchField','') == 'HQL'){
			html += '<option value="HQL" selected>HQL</option>';
		} else {
			html += '<option value="HQL">HQL</option>';
		}
		
		html += '</select>';
		html += '<div class="searchInput">';
		html += '<input type="text" name="searchValue" value="' + readCookie('odt-searchValue','') + '"/>';
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
		$('.oceanDynamicTable th[ocean-search-property]').each(function(){
			orderBy = readCookie('odt-orderBy', 'null');
			if (orderBy == 'null'){
				$(this).parents('table[ocean-orderBy]').each(function(){
					orderBy = $(this).attr('ocean-orderBy');
					setCookie('odt-orderBy', orderBy);
				});
			}
			
			if (orderBy != 'null' &&  orderBy.indexOf($(this).attr('ocean-search-property')) > -1){
				if (orderBy.indexOf($(this).attr('ocean-search-property') + ' asc') > -1){
					$(this).append('<span class="asc"></span>');
				} else {
					$(this).append('<span class="desc"></span>');
				}
			}
		});
		$('.oceanDynamicTable th[ocean-search-property]').dblclick(function(){
			orderBy = readCookie('odt-orderBy', 'null');
			if (orderBy != 'null' &&  orderBy.indexOf($(this).attr('ocean-search-property')) > -1){
				if (orderBy.indexOf($(this).attr('ocean-search-property') + ' asc') > -1){
					setCookie('odt-orderBy', $(this).attr('ocean-search-property') + ' desc');
				} else {
					setCookie('odt-orderBy', $(this).attr('ocean-search-property') + ' asc');
				}
			} else {
				setCookie('odt-orderBy', $(this).attr('ocean-search-property') + ' asc');
			}
			
			$('.searchForm:eq(0)').submit();
		});
		
		prevButtonFunction();
		nextButtonFunction();
		
		$('.search .searchLink').click(function(){
			searchField = $('.search select[name="searchField"]').val();
			searchValue = $('.search input[name="searchValue"]').val();
			setCookie('odt-searchField', searchField);
			setCookie('odt' + searchField + 'Search', searchValue);
			setCookie('odt-searchValue', searchValue);
			$(this).parents('form').submit();
		});
		
		$('.search input[name="searchValue"]').keypress(function(event){
			if(event.which == 13){
				searchField = $('.search select[name="searchField"]').val();
				setCookie('odt-searchField', searchField);
				setCookie('odt-searchValue', $(this).val());
				setCookie('odt-' + searchField + 'Search', $(this).val());
				$(this).parents('form').submit();
			}
        });
		
		$('.oceanDynamicTable .pagination .prev').button();
		$('.oceanDynamicTable .pagination .next').button();
		$('.oceanDynamicTable .pagination .new').button();
		
		$('.oceanDynamicTable .search select').selectmenu({
			change: function(event,data){
				$('.search input').val(readCookie('odt-'+data.item.value+'Search',''));
				$(this).val(data.item.value);
			}
		});
		
		// page size
		$('.oceanDynamicTable .pagination select').selectmenu({
			change: function(event,data){
				setCookie("odt-paginationSize",data.item.value);
				setCookie('odt-paginationFirst','1');
				$(this).parents('form').submit();
			}
		});
		
		// EDIT, DELETE, INSERT
		$(this).find('td[ocean-link]').each(function(){
			$(this).html('<a href="javascript:selectObject(' + $(this).attr('ocean-link') + ')">'+$(this).text() + '</a>');
		});
		
		selectedObject = readCookie('odt-selectedObject','null');
		if (selectedObject != 'null'){
			showForm(selectedObject);
		}
		
		$('.oceanDynamicForm .deleteButton').click(function(){
			var result = confirm($(this).attr('ocean-message'));
			if (!result){
				return false;
			}
		});
		
		$('.oceanDynamicTable .pagination .new').click(function(){
			selectObject(0);
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
			$(this).prepend('<span class="mobileLabel">' + $(header).text() + ':</span>');
		});
	}
}

var selectObject = function(objectId){
	setCookie('odt-selectedObject',objectId);
	$('form:eq(0)').submit();
}

var showForm = function(objectId){
	
	var w = $(window).width()*0.7;
	if ($(window).width() <= 480){
		w = $(window).width()*0.9;
	}
	
	var dialogTitle = '';
	if (objectId == '0')
		dialogTitle = $('.oceanDynamicForm').attr('ocean-title-new');
	else
		dialogTitle = $('.oceanDynamicForm').attr('ocean-title-edit');
	
	$('.oceanDynamicTable tbody td[ocean-link="' + objectId + '"]').each(function(){
		$(this).parent().addClass('selected');
	});
	$('.oceanDynamicForm').dialog({
		title: dialogTitle,
		modal: true,
		width: w,
		beforeClose: function(event,ui){
			deleteCookie("odt-selectedObject");
			$('.oceanDynamicTable tbody tr[class="selected"]').each(function(){
				$(this).removeClass('selected');
			});
		}
	});
}