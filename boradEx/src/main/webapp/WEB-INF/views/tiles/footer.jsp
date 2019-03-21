<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<script>
(function(){
	const csrfHeader = $("meta[name='_csrf_header']").attr("content");
	const csrfToken = $("meta[name='_csrf']").attr("content");
	$(document).ajaxSend(function(e, xhr, options){
		xhr.setRequestHeader(csrfHeader, csrfToken);
	});
})();
</script>