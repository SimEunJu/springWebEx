<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>	

	<nav class="navbar navbar-expand-md navbar-light bg-light">
		<div class="navbar-home">
			<a class="navbar-brand" href="/board/daily">
				오늘 하루를 말해줘
				<img id="home-icon" src="/resources/img/bird.png">
			</a>
		</div>

		<button class="navbar-toggler" type="button" data-toggle="collapse"
			data-target="#navbar" aria-controls="navbar" aria-expanded="false"
			aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>

		<div class="collapse navbar-collapse justify-content-end" id="navbar">
			<ul class="navbar-nav">
				<sec:authorize access="isAuthenticated()">
					<sec:authorize access="hasRole('USER')">
						<c:set var="identity" value="user" scope="request" />
					</sec:authorize>
					<sec:authorize access="hasRole('ADMIN')">
						<c:set var="identity" value="admin" scope="request" />
					</sec:authorize>
					<li class="nav-item">
						<form method="post" action="/board/logout">
							<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
							<a class="nav-link" onclick="this.parentNode.submit()">로그아웃</a>
						</form>
						
					</li>
					<li class="nav-item"><a class="nav-link" href="/board/${identity}">내 정보</a></li>
					<li class="nav-item">
						<a class="nav-link" href="/board/${identity}/noti">알림<span class="notibadge badge badge-warning">0</span></a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="/board/${identity}/msg">메시지<span class="msgbadge badge badge-warning">0</span></a>
					</li>
				</sec:authorize>
				<sec:authorize access="isAnonymous()">
					<li class="nav-item"><a class="nav-link" href="/board/login">로그인</a></li>
				</sec:authorize>
			</ul>
		</div>
	</nav>