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
<form role="form">
	<input type="hidden" name="page" value="${pageMaker.cri.page }">
	<input type="hidden" name="perPageNum" value="${pageMaker.cri.perPageNum }">
</form>
<div class="box-body">
	<%@ include file="../include/header.jsp" %>
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
			<td><a href="${boardVO.bno }">${boardVO.title }</a></td>
			<td>${boardVO.writer }</td>
			<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${boardVO.regdate }" /></td>
			<td>${boardVO.viewcnt }</td>
		</tr>
		</c:forEach>		
	</table>
	
	<div class="text-center">
		<ul class="pagination">
			<c:if test="${pageMaker.prev}">
				<li><a href="${pageMaker.startPage-1 }">&laquo;</a></li>
			</c:if>
			
			<c:forEach begin="${pageMaker.startPage }" end="${pageMaker.endPage }" var="idx">
				<li <c:out value="${pageMaker.cri.page eq idx ? 'class=actvie' : '' }" />>
				<a data-href="${idx }">${idx }</a>
				</li>
			</c:forEach>
			
			<c:if test="${pageMaker.next }">
				<li><a href="${pageMaker.endPage+1 }">&raquo;</a></li>
			</c:if>
		</ul>
	</div>
</div>
<%@ include file="../include/footer.jsp" %>

<script type="text/javascript">
	$("document").ready(function(){
		var formObj = $("form[role='form']");
		
		$(".pagination li a").on("click", function(){
			var targetPage = $(this).attr("data-href");
			formObj.find("[name='page']").val(targetPage);
			formObj.attr("action", "/board/list");
			formObj.attr("method", "get");
			formObj.submit();
		});
		
		$("td a").on("click", function(e){
			e.preventDefault();
			var targetBno = $(this).attr("href");
			formObj.attr("action", "/board/list/"+targetBno);
			formObj.attr("method", "get");
			formObj.submit();
		});
	})
</script>
</body>
</html>