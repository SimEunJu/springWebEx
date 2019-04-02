<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>	

 <div class="col-md-3 mt-3">
	<ul class="list-group">
		<li class="list-group-item"><a href="/board/${identity}">마이페이지</a></li>
		<li class="list-group-item"><a href="/board/${identity}/noti">알림</a></li>
		<li class="list-group-item"><a href="/board/${identity}/msg">메시지</a></li>
		<sec:authorize access="hasRole('ADMIN')">
			<li class="list-group-item"><a href="/board/admin/user">회원 관리</a></li>
		</sec:authorize>
		<li class="list-group-item"><a href="/board/${identity}/post">내가 쓴 글</a></li>
		<li class="list-group-item"><a href="/board/${identity}/reply">내가 쓴 댓글</a></li>
	</ul>
</div>