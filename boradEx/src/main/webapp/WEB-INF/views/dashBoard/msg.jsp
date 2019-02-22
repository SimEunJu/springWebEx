<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>메시지</title>
</head>
<body>

<div class="container">
	<table class="table">
  		<thead class="thead-dark">
    		<tr>
      			<th scope="col"><input type="checkbox" name="msg" value="all" /></th>
     			<th scope="col">보낸 이</th>
      			<th scope="col">제목</th>
      			<th scope="col">날짜</th>
    		</tr>
  		</thead>
  		
  		<tbody>
    		<tr>
    			<c:forEach var="m" items="msg" varStatus="i">
      				<th scope="row"><input type="checkbox" name="msg[${i}]" value="${m.msgNo}" /></th>
      				<td>${m.sender}</td>
      				<td>${m.title}</td>
      				<td>
      					<fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${m.regdate}" />
      				</td>
    			</c:forEach>
    		</tr>
  		</tbody>
	</table>
</div>

</body>
</html>