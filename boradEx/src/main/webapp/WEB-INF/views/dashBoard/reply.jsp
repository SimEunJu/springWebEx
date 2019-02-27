<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>내가 쓴 댓글</title>
</head>
<body>

<div class="container">
	<table class="table">
  		<thead class="thead-dark">
    		<tr>
      			<th scope="col"><input type="checkbox" name="noti" value="all" /></th>
      			<th scope="col">댓글</th>
      			<th scope="col">날짜</th>
    		</tr>
  		</thead>
  		
 		<tbody>
    		<c:forEach var="reply" items="${replies}">
    		<tr>
      			<th scope="row"><input type="checkbox" name="reply" value="${reply.rno}" /></th>
      			<td><a href="/board/daily/${reply.bno}">${reply.reply}</a></td>
      			<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${reply.regdate}" /></td>
    		</tr>
    		</c:forEach>
  		</tbody>
	</table>
</div>
<script>
$("document").ready(function(){
	
});
</script>
</body>
</html>