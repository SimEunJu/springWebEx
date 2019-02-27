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
    			<c:forEach var="n" items="noti" varStatus="i">
    				<tr>
    				<th scope="row"><input type="checkbox" name="noti" value="${n.nno}" /></th>
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
      				</tr>
    			</c:forEach>
  		</tbody>
	</table>
	<nav aria-label="Page navigation">
  		<ul class="pagination">
  			<c:if test="${pageMaker.prev}">
    		<li class="page-item"><a class="page-link prev" href="">&laquo;</a></li>
    		</c:if>
    		
    		<c:forEach begin=1 end="${pageMaker.endPage>10 ? 10 : pageMaker.endPage}" varStatus="idx">
    		<li class="page-item"><a class="page-link" href="${varStatus.count}">1</a></li>
    		</c:forEach>
    		
    		<c:if test="${pageMaker.next}">
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

<script src="/radioInputHandle.js"></script>
<script>
$("document").ready(function(){
	const tableRowSekeleton   = document.getElementById("table-row").innerHTML;
	const template = Handlebars.compile(tableRowSekeleton);
	Handlebars.registerHelper("dateFormat", function(date){
		return new Date(date).toJSON().replace("z", " ").substring(0,16);
	});
	
	const check = checkServInitiator("noti", "/board/api/admin/user", template);
	
});
</script>
</body>
</html>