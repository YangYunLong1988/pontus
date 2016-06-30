/**
 * 
 */
$(function(){

	
	$("a[app-pn]").on("click",function(){
		$("#pageable_form #pn").val($(this).attr("app-pn"));
		$("#pageable_form").submit();
	});
	
	$("*[app-submit]").on("click",function(){
		var formName = $(this).attr("app-submit");
		$("#"+formName).submit();
	});
	$("*[app-bind]").on("click",function(){
		var bindName = $(this).attr("app-bind");
		$("#"+bindName).val($(this).attr("app-value"));
	});
	$("a[app-href]").one("click",function(){
		var url =  $(this).attr("app-href");
		location.href = url;
	});
});