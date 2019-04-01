<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="cf" uri="http://ex.co.kr/format_local_datetime"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<form role="form">
	<input type="hidden" name="page" value="${pagination.cri.page }">
	<input type="hidden" name="perPageNum" value="${pagination.cri.perPageNum }">
	<input type="hidden" name="keyword" value="${pagination.cri.keyword }">
	<input type="hidden" name="searchType" value="${pagination.cri.searchType }">
	<input type="hidden" name="type" value="${pagination.cri.type.typeInitial }">	 
</form>

<div class="col-md-9">
	<div class="row my-2 p-2 border">
		<div class="ml-auto">
			<a href="/board/daily/notice/new">
				<button type="button" class="btn btn-outline-danger" id="btn-notice">공지시항 작성</button>
			</a>
		</div>
	</div>
	
	<div class="row my-2 p-2 border">
		<%@include file="/WEB-INF/views/include/searchBar.jsp" %>
		
		<div class="col-12">
			<input type="radio" id="board-all" name="boardType" value="all" ${pagination.cri.type.typeInitial eq 'all' ? 'checked' : ''}>
			<label for="board-all">모든 글</label>
			
			<input type="radio" id="board-self" name="boardType" value="self" ${pagination.cri.type.typeInitial eq 'self' ? 'checked' : ''}>
			<label for="board-all">내가 쓴 글</label>
		
			<input type="radio" id="board-report" name="boardType" value="report" ${pagination.cri.type.typeInitial eq 'report' ? 'checked' : ''}>
			<label for="board-report">신고 순</label>
		</div>
	</div>
	
	<div class="row my-2 p-2 border">
		<div class="col-12">
			<input type="checkbox" id="all-mem" name="post" value="all" /> 
			<label for="all-mem">전체</label>		
			
			<div class="float-right">
				<select name="delete" class="ml-auto mr-2">
  					<option value="" selected>--------</option> 
 					<option value="report">신고 횟수 누적</option>
  					<option value="inappropriate">부적절한 내용</option>
				</select>
				<button type="button" class="btn btn-outline-warning" id="btn-del">삭제</button>
			</div>
		</div>
	</div>
	
	<table class="table table-hover">
  		<thead class="thead-dark">
    		<tr>
      			<th scope="col"><input type="checkbox" name="post" value="all-showed" /></th>
      			<th scope="col">제목</th>
      			<th scope="col">글쓴이</th>
      			<th scope="col">조회수</th>
      			<th scope="col">날짜</th>
      			<th scope="col">신고 수</th>
    		</tr>
  		</thead>
  		
 		<tbody>
 			<c:forEach var="post" items="${boardList}">
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
	
	<c:if test="${empty boardList}">
    	<div class="row-12 p-2 border text-center">작성한 게시글이 없습니다.</div>
    </c:if>	
	
</div>

<%@ include file="/WEB-INF/views/include/pagination.jsp"%>

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