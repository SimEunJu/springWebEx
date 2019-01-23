<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

    <div class="text-center">
		<ul class="pagination">
			<c:if test="${pageMaker.prev}">
				<li><a href="/board/list${pageMaker.makeSearch(pageMaker.startPage-1) }">&laquo;</a></li>
			</c:if>
			
			<c:forEach begin="${pageMaker.startPage }" end="${pageMaker.endPage }" var="idx">
				<li <c:out value="${pageMaker.cri.page eq idx ? 'class=actvie' : '' }" />>
				<a href="/board/list${pageMaker.makeSearch(idx) }">${idx }</a>
				</li>
			</c:forEach>
			
			<c:if test="${pageMaker.next }">
				<li><a href="/board/list${pageMaker.makeSearch(pageMaker.startPage+1) }">&raquo;</a></li>
			</c:if>
		</ul>
	</div>