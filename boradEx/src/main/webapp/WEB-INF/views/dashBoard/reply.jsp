<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ include file="../common/header.jsp"%>

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
      			<td>${cf:formatLocalDateTime(reply.regdate, 'yyyy-MM-dd HH:mm:ss')}</td>
    		</tr>
    		</c:forEach>
  		</tbody>
	</table>
	
	<c:if test="${empty replies}">
    	<div class="row-12 p-2 border text-center">등록된 알림이 없습니다.</div>
    </c:if>	
</div>

<%@ include file="../common/footer.jsp"%>

<script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.1.0/handlebars.min.js"></script>
<script src="/resources/js/checkboxHandle.js"></script>
<script>
$("document").ready(function(){
	
});
</script>
</body>
</html>