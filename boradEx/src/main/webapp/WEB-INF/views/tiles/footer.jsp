<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<footer class="bg-light">
</footer>

<script>
(function(){
	const csrfHeader = $("meta[name='_csrf_header']").attr("content");
	const csrfToken = $("meta[name='_csrf']").attr("content");
	$(document).ajaxSend(function(e, xhr, options){
		xhr.setRequestHeader(csrfHeader, csrfToken);
	});

	if(!window.bootstrap){
		const toggler = document.querySelector(".navbar-toggler");
		const toggleList = $("#navbar");
	
		toggler.addEventListener("click", function(){
			toggleList.toggleClass("show");
			const togglerVal = toggler.getAttribute("aria-expanded") === "true" ? "true" : "false";
			toggler.setAttribute("aria-expanded", togglerVal);
		});
	}
})();
</script>