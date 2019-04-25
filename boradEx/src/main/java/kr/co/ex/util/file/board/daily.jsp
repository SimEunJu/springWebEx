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

<div class="modal fade" id="modal" tabindex="-1" role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body"></div>
		</div>
	</div>
</div>

<%@ include file="/WEB-INF/views/include/searchBar.jsp"%>

<div class="row p-2 m-2 border justify-cotent-end">
	<button id="newBtn" class="btn btn-outline-primary ml-auto">글쓰기</button>
</div>

<ul class="nav nav-tabs">
  <li class="nav-item">
    <a class="nav-link all ${pagination.cri.type.typeInitial eq 'all' ? 'active' : '' }" href="javascript:void(0)">게시글</a>
  </li>
  <li class="nav-item">
    <a class="nav-link hot ${pagination.cri.type.typeInitial eq 'hot' ? 'active' : '' }" href="javascript:void(0)">인기글</a>
  </li>
</ul>
<div class="col-lg-12">
	<table class="table table-hover">
		<thead>
			<tr>
				<th class="border-top-0">번호</th>
				<th class="border-top-0">제목</th>
				<th class="border-top-0">작성자</th>
				<th class="border-top-0">작성일</th>
				<th class="border-top-0">조회수</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="notice" items="${noticeList}">
				<tr class="table-info">
					<td>${notice.bno}</td>
					<td><a
						href="/board/daily/${notice.bno}${pagination.cri.makeSearch()}">${notice.title}</a>
					</td>
					<td>${notice.writer}</td>
					<td>${cf:formatLocalDateTime(notice.regdate, 'yyyy-MM-dd HH:mm:ss')}</td>
					<td>${notice.viewcnt}</td>
				</tr>
			</c:forEach>
			
			<c:forEach var="board" items="${boardList}">
				<tr>
					<td>${board.bno}</td>
					<td><a
						href="/board/daily/${board.bno}/${pagination.cri.makeSearch()}">${board.title}</a>
						[<a href="javascript:void(0)">${board.replyCnt}</a>]</td>
					<td>${board.writer}</td>
					<td>${cf:formatLocalDateTime(board.regdate, 'yyyy-MM-dd HH:mm:ss')}</td>
					<td>${board.viewcnt}</td>
				</tr>
			</c:forEach>

		</tbody>
	</table>
</div>

<%@ include file="/WEB-INF/views/include/pagination.jsp"%>
<div id="data" data-msg="${msg}"></div>