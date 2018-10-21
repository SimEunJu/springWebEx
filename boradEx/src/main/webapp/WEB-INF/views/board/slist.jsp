<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 <%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
 
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>BoardEx</title>
</head>
<script type="text/javascript">
	var result = "${msg}";
	if(msg === "success") alert("처리가 정상적으로 완료되었습니다.");
</script>
<body>
<%@ include file="../include/header.jsp" %>
<form role="form">
	<input type="hidden" name="page" value="${pageMaker.cri.page }">
	<input type="hidden" name="perPageNum" value="${pageMaker.cri.perPageNum }">
</form>
<div class="box-body">
	<%@ include file="../include/searchBar.jsp" %>
	<table class="table table-bordered">
		<tr>
			<th style="width:10px">BNO</th>
			<th>TITLE</th>
			<th>WRITER</th>
			<th>REGDATE</th>
			<th style="width:40px">VIEWCNT</th>
		</tr>
		<c:forEach var="boardVO" items="${list}">
		<tr>
			<td>${boardVO.bno }</td>
			<td><a href="/board/list/${boardVO.bno }${pageMaker.makeSearch(cri.page)}">${boardVO.title }</a></td>
			<td>${boardVO.writer }</td>
			<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${boardVO.regdate }" /></td>
			<td>${boardVO.viewcnt }</td>
		</tr>
		</c:forEach>		
	</table>
	
	<%@ include file="../common/pagination.jsp" %>
</div>
<%@ include file="../include/footer.jsp" %>

<script type="text/javascript">
	$("document").ready(function(){
		var formObj = $("form[role='form']");
		
		$("#searchBtn").on("click", function(e){
			window.location.href = "/board/list"+"${pageMaker.makeQuery(pagekMaker.cri.getPage)}"
									+"&searchType=" + $("select option:selected").val()
									+"&keyword=" + encodeURIComponent($("input[name='keyword']").val());
		});
		
		$("#newBtn").on("click", function(){
			window.location.href = "/board/register";
		});
	})
</script>
</body>
</html>