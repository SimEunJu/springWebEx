<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="cf" uri="http://ex.co.kr/format_local_datetime"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="col-md-9">
	<div class="row my-2 p-2 border">
		<div class="col">
			<input type="checkbox" id="all-mem" name="msg" value="all" /> 
			<label for="all-mem">전체</label>
		</div>
		
		<div class="float-right">
			<button type="button" class="btn btn-outline-danger" id="btn-notice">공지시항 작성</button>
			<button type="button" class="btn btn-outline-info" id="btn-all">모든 글</button>
			<button type="button" class="btn btn-outline-warning" id="btn-del">삭제</button>
		</div>
	</div>
	
	<table class="table table-hover">
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
	
	<c:if test="${empty posts}">
    	<div class="row-12 p-2 border text-center">작성한 게시글이 없습니다.</div>
    </c:if>	
	
</div>

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
		<th scope="row"><input type="checkbox" name="post" value="{{bno}}" /></th>
      	<td><a href="/board/daily/{{bno}}">{{title}}[{{replyCnt}}]</a></td>
		<td>{{viewcnt}}</td>
      	<td>{{dateFormat regdate}}</td>
     </tr>
{{/each}}
</script>

<script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.1.0/handlebars.min.js"></script>
<script src="/resources/js/utils/common.js"></script>