<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ include file="../common/header.jsp"%>

<div class="container">
	
	<div class="row my-2 p-2 border">
		<div class="col">
			<input type="checkbox" id="all-mem" name="msg" value="all" /> 
			<label for="all-mem">전체</label>
		</div>
		
		<div class="float-right">
			<button type="button" class="btn btn-outline-warning" id="btn-del">삭제</button>
		</div>
	</div>

	<table class="table table-hover">
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

<script id="pagination-hb" type="text/x-handlebars-template">
{{#if prev}}
	<li class="page-item"><a class="page-link" href="">&laquo;</a></li>
{{/if}}
{{for startPage endPage}}
	<li class="page-item"><a class="page-link" href="{{this}}">{{this}}</a></li>
{{/for}}
{{#if next}}
	<li class="page-item"><a class="page-link" href="">&raquo;</a></li>
{{/if}}
</script>
<script id="table-row" type="text/x-handlebars-template">
{{#each this}}	
	<tr>
		<th scope="row"><input type="checkbox" name="reply" value="{{rno}}" /></th>
      	<td><a href="/board/daily/{{bno}}">{{reply}}</a></td>
      	<td>{{dateFormat regdate}}</td>
     </tr>
{{/each}}
</script>

<script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.1.0/handlebars.min.js"></script>
<script src="/resources/js/modal.js"></script>
<script>
$("document").ready(function(){
	const csrfToken = "${_csrf.token }";
	const csrfHeader = "${_csrf.headerName }";
	$(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(csrfHeader, csrfToken);
    });
	
	addHandlebarHelper();
	
	const tableRowSkeleton = document.getElementById("table-row").innerHTML;
	const tableRowTemplate = Handlebars.compile(tableRowSkeleton);
	
	const check = new Check("msg");
	const pagination = new Pagination();
	
	$("#btn-del").on("click", function(e){
		check.appendCheckVal(pagination.page);
		const msgNoList = flatObjToList(check.repo, 'msgNo');
		ajax({
			url: "/board/user/msg/del",
			method: "post",
			data: JSON.stringify(msgNoList),
			contentType: "application/json; charset=utf-8"
		}, function(res){
			console.log(res);
			check.tbody.html(tableRowTemplate(res));
		});
	});
});
</script>
</body>
</html>