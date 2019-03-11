<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ include file="../common/header.jsp"%>

<div class="container">
	<table class="table mt-2">
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
    				<th scope="row" class="row-1"><input type="checkbox" name="noti" value="${noti.nno}" /></th>

					<td class="noti row-8" style="weight: ${noti.read_flag ? 'bold' : ''}">
						<a href="/board/daily/${noti.bno}?from=noti&rno=${noti.rno}">
							<c:choose>
								<c:when test="${not empty noti.title}">
      								[ ${noti.title} ]에 댓글이 달렸습니다.
      							</c:when>
								<c:otherwise>
									[ ${noti.reply} ]에 대댓글이 달렸습니다.
      							</c:otherwise>
							</c:choose>
						</a>
					</td>

					<td class="row-3">
      					${cf:formatLocalDateTime(noti.regdate, 'yyyy-MM-dd HH:mm:ss')}
      				</td>
      				</tr>
    			</c:forEach>
  		</tbody>
	</table>
	
	<c:if test="${empty noties}">
    	<div class="row-12 p-2 border text-center">등록된 알림이 없습니다.</div>
    </c:if>	
    			
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

<%@ include file="../common/footer.jsp"%>

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

<script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.1.0/handlebars.min.js"></script>
<script src="/resources/js/modal.js"></script>
<script>
$("document").ready(function(){
	const tableRowSekeleton   = document.getElementById("table-row").innerHTML;
	const template = Handlebars.compile(tableRowSekeleton);
	Handlebars.registerHelper("dateFormat", function(date){
		if(date === null) return;
		return date.year+"-"+date.monthValue+"-"+date.dayOfMonth+" "+date.hour+":"+date.minute;
	});
	
	
	
});
</script>
</body>
</html>