<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://ex.co.kr/format_local_datetime" prefix="cf" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>내가 쓴 글</title>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" 
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
</head>
<body>

<div class="container">
	<table class="table">
  		<thead class="thead-dark">
    		<tr>
      			<th scope="col"><input type="checkbox" name="noti" value="all" /></th>
      			<th scope="col">제목</th>
      			<th scope="col">조회수</th>
      			<th scope="col">날짜</th>
    		</tr>
  		</thead>
  		
 		<tbody>
 			<c:forEach var="post" items="${posts}">
    		<tr>
      			<th scope="row"><input type="checkbox" name="post" value="${post.bno}" /></th>
      			<td><a href="/board/daily/${post.bno}">${post.title}[${post.replyCnt}]</a></td>
				<td>${post.viewcnt}</td>
      			<td>${cf:formatLocalDateTime(post.regdate, 'yyyy-MM-dd HH:mm:ss')}</td>
    		</tr>
    		</c:forEach>
  		</tbody>
	</table>
</div>
<script src="https://code.jquery.com/jquery-3.3.1.min.js"
	integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8=" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.1.0/handlebars.min.js"></script>
<script src="/resources/js/checkboxHandle.js"></script>
<script>
$("document").ready(function(){
	
});
</script>
</body>
</html>