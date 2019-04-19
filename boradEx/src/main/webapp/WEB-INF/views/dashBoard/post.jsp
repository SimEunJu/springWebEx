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
	</div>
	
	<table class="table table-hover">
  		<thead class="thead-dark">
    		<tr>
      			<th scope="col"><input type="checkbox" name="noti" value="all" /></th>
      			<th scope="col">제목</th>
      			<th scope="col">글쓴이</th>
      			<th scope="col">조회수</th>
      			<th scope="col">날짜</th>
      			<th scope="col">신고 수</th>
    		</tr>
  		</thead>
  		
 		<tbody>
 			<c:forEach var="post" items="${posts}">
    		<tr>
      			<th scope="row"><input type="checkbox" name="post" value="${post.bno}" /></th>
      			<td><a href="/board/daily/${post.bno}">${post.title}[${post.replyCnt}]</a></td>
				<td>${post.writer}</td>
				<td>${post.viewcnt}</td>
      			<td>${cf:formatLocalDateTime(post.regdate, 'yyyy-MM-dd HH:mm:ss')}</td>
    			<td>${post.reportCnt}</td>
    		</tr>
    		</c:forEach>
  		</tbody>
	</table>
	
	<c:if test="${empty posts}">
    	<div class="row-12 p-2 border text-center">작성한 게시물이 없습니다.</div>
    </c:if>	
	
	<nav aria-label="Page navigation">
  		<ul class="pagination justify-content-center">
  			<c:if test="${pagination.prev}">
    		<li class="page-item"><a class="page-link prev" href="">&laquo;</a></li>
    		</c:if>
    		
    		<c:forEach begin="1" end="${pagination.endPage>10 ? 10 : pagination.endPage}" varStatus="idx">
    		<li class="page-item ${idx.count==pagination.cri.page ? 'active' : '' }"><a class="page-link" href="${idx.count}">${idx.count}</a></li>
    		</c:forEach>
    		
    		<c:if test="${pagination.next}">
    		<li class="page-item"><a class="page-link next" href="">&raquo;</a></li>
  			</c:if>
  		</ul>
	</nav>
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
		<td>{{writer}}</td>	
		<td>{{viewcnt}}</td>
      	<td>{{dateFormat regdate}}</td>
		<td>{{reportCnt}}</td>
     </tr>
{{/each}}
</script>

<script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.1.0/handlebars.min.js"></script>
<script src="/resources/js/utils/common.js"></script>
