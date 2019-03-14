<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div class="row">
	<ul class="pagination mx-auto">
		<c:if test="${pagination.prev}">
			<li class="page-item"><a class="page-link" href="/board/daily${pageMaker.cri.makeSearch(pageMaker.startPage-1) }">&laquo;</a></li>
		</c:if>

		<c:forEach begin="${pagination.startPage }" end="${pagination.endPage }" var="idx">
			<li class="page-item ${pagination.cri.page eq idx ? 'actvie' : '' }">
				<a class="page-link" href="/board/daily${pagination.cri.makeSearch(idx) }">${idx}</a>
			</li>
		</c:forEach>

		<c:if test="${pagination.next}">
			<li class="page-item"><a class="page-link" href="/board/daily${pageMaker.cri.makeSearch(pageMaker.startPage+1) }">&raquo;</a></li>
		</c:if>
	</ul>
</div>