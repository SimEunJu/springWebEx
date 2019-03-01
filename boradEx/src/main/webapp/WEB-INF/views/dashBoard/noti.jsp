<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>알림</title>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" 
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
</head>
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
    			<c:forEach var="noti" items="${noties}" varStatus="i">
    				<tr>
    				<th scope="row"><input type="checkbox" name="noti" value="${noti.nno}" /></th>
      				<c:choose>
      					<c:when test="${not empty noti.title}">
      						<a href="/board/daily/${noti.bno}?from=noti&rno=${noti.rno}"><td class="noti" style="weight: ${noti.read_flag ? 'bold' : ''}">[ ${noti.title} ]에 댓글이 달렸습니다.</td></a>
      					</c:when>
      					<c:otherwise>
      						<a href="/board/daily/${noti.bno}?from=noti&rno=${noti.rno}"><td class="noti" style="weight: ${noti.read_flag ? 'bold' : ''}">[ ${noti.reply} ]에 대댓글이 달렸습니다.</td></a>
      					</c:otherwise>
      				</c:choose>
      				<td>
      					<fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${noti.regdate}" />
      				</td>
      				</tr>
    			</c:forEach>
  		</tbody>
	</table>
	<nav aria-label="Page navigation">
  		<ul class="pagination">
  			<c:if test="${pagination.prev}">
    		<li class="page-item"><a class="page-link prev" href="">&laquo;</a></li>
    		</c:if>
    		
    		<c:forEach begin="1" end="${pagination.endPage>10 ? 10 : pagination.endPage}" varStatus="idx">
    		<li class="page-item"><a class="page-link" href="${idx.count}">${idx.count}</a></li>
    		</c:forEach>
    		
    		<c:if test="${pagination.next}">
    		<li class="page-item"><a class="page-link next" href="">&raquo;</a></li>
  			</c:if>
  		</ul>
	</nav>
</div>

<script id="table-row" type="text/x-handlebars-template">
{{#each users}}	
	<tr>
		<th scope="row"><input type="checkbox" name="noti" value="{{nno}}" /></th> 
    		{{#if title}}<a href="/board/daily/{{bno}}?from=noti&rno={{rno}}"><td class="noti" style="weight: {{#if readFlag}}'bold'{{else}}''{{/if}}">[ {{title}} ]에 댓글이 달렸습니다.</td></a>
      		{{else}}<a href="/board/daily/{{bno}}?from=noti&rno={{rno}}"><td class="noti" style="weight: {{#if readFlag}}'bold'{{else}}''{{/if}}">[ {{reply}} ]에 대댓글이 달렸습니다.</td></a>
      		{{/if}}
			<td>{{#dateFormat regdate}}</td>
     </tr>
{{/each}}
</script>

<script src="https://code.jquery.com/jquery-3.3.1.min.js"
	integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8=" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.1.0/handlebars.min.js"></script>
<script src="/resources/js/checkboxHandle.js"></script>
<script>
$("document").ready(function(){
	const tableRowSekeleton   = document.getElementById("table-row").innerHTML;
	const template = Handlebars.compile(tableRowSekeleton);
	Handlebars.registerHelper("dateFormat", function(date){
		if(date === null) return;
		return date.year+"-"+date.monthValue+"-"+date.dayOfMonth+" "+date.hour+":"+date.minute;
	});
	
	const check = checkServInitiator("noti", "/board/api/admin/user", template);
	
});
</script>
</body>
</html>