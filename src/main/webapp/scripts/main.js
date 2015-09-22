$(document).ready(function() {
	$('header > h2').text(document.title);
	
	$('.drawer').click(function() {
		$('.content > aside').each(function() {
			if ($(this).css('width') == '0px') {
				setCookie('showMenu', '1');
				if ($(window).width() <= 480){
					$(this).css('width', '95%');
					$('.main').css('display', 'none');
				} else if ($(window).width() <= 760) {
					$(this).css('width', '60%');
					$('.main').css('display', 'none');
				} else {
					$(this).css('width', '25%');
					$('.main').css('margin-left', '26%');
				}
				$(this).css('overflow', 'auto');
				
			} else {
				setCookie('showMenu', '0');
				$(this).css('width', '0');
				$(this).css('overflow', 'hidden');
				$('.main').css('display', 'block');
				$('.main').css('margin-left', '1%');
			}
		});

	});
	
	if (readCookie('showMenu', '0') == '1'){
		$('.drawer').trigger('click');
	}
});

var createMenu = function(urlRoot,urlMenu){
	var level=0, html, lastSubmenu = readCookie('lastParentId', 'null');
	$('.menu').each(function(){
		$.ajax({
			url: urlMenu,
			success: function(result){
				html = createMenuList(urlRoot,result,level);
				$('.menu').html(html);
				if (lastSubmenu != 'null'){
					transformMenu(html,lastSubmenu);
				} else {
					lastSubmenu = $('.menu ul:eq(0)').attr('odm-id');
					transformMenu(html,lastSubmenu);
				}
				$('.menu .option').click(function(){
					for (cookie in $.cookie()){
						if (cookie.indexOf('oc-odt-') != -1){
							$.removeCookie(cookie,{ path: '/' });
						}
					}
					setCookie('lastParentId', $(this).parents('ul:eq(0)').attr('odm-id'));
					return true;
				});
			}
		});
	});
}

var createMenuList = function (urlRoot,menu,level){
	var i, html = '<ul odm-level="' + level + '" odm-id="' + menu.id + '">';
	for (i = 0; i < menu.menuItems.length; i++){
		html += '<li odm-level="' + level + '"><a href="' + urlRoot + menu.menuItems[i].url + '" class="option">' + menu.menuItems[i].title + '</a></li>';
	}
	for (i = 0; i < menu.submenus.length; i++){
		html += '<li odm-id="' +  menu.submenus[i].id +'" odm-level="' + level + '" class="submenu">' + menu.submenus[i].title;
		html += '<span>&gt;</span>';
		html += createMenuList(urlRoot,menu.submenus[i],level+1);
		html += '</li>';
	}
	html += '</ul>';
	return html;
}

// kad kliknemo na opciju ovaj metod se izvrsi opet???
var transformMenu = function(html,parentId){
	var level = 0;
	$('.menu').html($('<div />').append(html).find('ul[odm-id="' + parentId + '"]').parent().html());
	$('.menu').children('ul:eq(0)').each(function(){
		level = parseInt($(this).attr('odm-level'));
		if (level > 0){
			$(this).append('<li class="back"><a href="#">Back</a></li>');
			$('.menu .back').click(function(){
				var grandarentId = $(html).find('ul[odm-id="' + parentId + '"]').parents('ul:eq(0)').attr('odm-id');
				transformMenu(html,grandarentId);
			});
		}
	});
	$('.menu li[odm-level="'+(level+1)+'"]').hide();
	$('.menu li[odm-level="'+(level)+'"]').each(function(){
		$(this).show();
		if ($(this).hasClass('submenu')){
			$(this).click(function(){
				transformMenu(html,$(this).attr('odm-id'));
			});
		}
	});
}

var readCookie = function(cookieName,defaultValue){
	val = $.cookie('oc-' + cookieName);
	if (val === undefined || val == ''){
		return defaultValue;
	}
	return val;
}

var setCookie = function(cookieName,cookieValue){
	$.cookie('oc-' + cookieName,cookieValue,{path:'/'});
}

var deleteCookie = function(cookieName){
	$.removeCookie("oc-" + cookieName,{ path: '/' });
}