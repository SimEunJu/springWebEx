<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>알림</title>
</head>
<body>

<div class="container">
	<table class="table">
  		<thead class="thead-dark">
    		<tr>
      			<th scope="col"><input type="checkbox" name="noti" value="all" /></th>
      			<th scope="col">알림</th>
      			<th scope="col">날짜</th>
    		</tr>
  		</thead>
  		
 		<tbody>
    		<tr>
    			<c:forEach var="n" items="noti" varStatus="i">
    				<th scope="row"><input type="checkbox" name="noti[${i}]" value="${n.nno}" /></th>
      				<c:choose>
      					<c:when test="${not empty n.title}">
      						<a href="/board/daily/${n.bno}?from=noti&rno=${n.rno}"><td class="noti" style="weight: ${n.read_flag ? 'bold' : ''}">[ ${title} ]에 댓글이 달렸습니다.</td></a>
      					</c:when>
      					<c:otherwise>
      						<a href="/board/daily/${n.bno}?from=noti&rno=${n.rno}"><td class="noti" style="weight: ${n.read_flag ? 'bold' : ''}">[ ${reply} ]에 대댓글이 달렸습니다.</td></a>
      					</c:otherwise>
      				</c:choose>
      				<td>
      					<fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${n.regdate}" />
      				</td>
    			</c:forEach>
    		</tr>
  		</tbody>
	</table>
</div>

<script>
$("document").ready(function(){
	$(".noti").on("click", function(){
		
	});
});
</script>
</body>
</html>