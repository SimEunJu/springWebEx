<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="cf" uri="http://ex.co.kr/format_local_datetime"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<form role="form">
	<input type="hidden" name="page" value="${pagination.cri.page }">
	<input type="hidden" name="perPageNum"
		value="${pagination.cri.perPageNum }">
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

<%@ include file="../include/searchBar.jsp"%>

<div class="row p-2 m-2 border justify-cotent-end">
	<button id="newBtn" class="btn btn-outline-primary ml-auto">글쓰기</button>
</div>

<div class="col-lg-12">
	<table class="table table-hover">
		<thead>
			<tr>
				<th>번호</th>
				<th>제목</th>
				<th>작성자</th>
				<th>작성일</th>
				<th>조회수</th>
			</tr>
		</thead>
		<tbody>
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

<%@ include file="../include/pagination.jsp"%>
<div id="data" data-msg="${msg}"></div>
<script src="/resources/js/common/polling.js"></script>