<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>	
<%@ taglib prefix="cf" uri="http://ex.co.kr/format_local_datetime"%>

<!DOCTYPE html>

<html lang="ko">

<head>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>{title-> tiles}</title>
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
	crossorigin="anonymous">

</head>

<body>
	<nav class="navbar navbar-expand-md navbar-light bg-light">
		<div class="navbar-home">
			<a class="navbar-brand" href="/board/daily"> <img id="home-icon" src="/resources/img/ear.png">오늘 하루를 말해줘
			</a>
		</div>

		<button class="navbar-toggler" type="button" data-toggle="collapse"
			data-target="#navbar" aria-controls="navbar" aria-expanded="false"
			aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>

		<div class="collapse navbar-collapse justify-content-end" id="navbar">
			<ul class="navbar-nav">
				<sec:authorize access="isAuthenticated()"><sec:authentication property="principal.username"/>
					<sec:authorize access="hasRole('ROLE_ADMIN')">
						<c:set var="identity" value="admin" />
					</sec:authorize>
					<sec:authorize access="hasRole('ROLE_USER')">
						<c:set var="identity" value="user" />
					</sec:authorize>
					<li class="nav-item"><a class="nav-link" href="/board/logout">로그아웃</a></li>
					<li class="nav-item"><a class="nav-link"
						href="/board/${identity}/info">내 정보</a></li>
					<li class="nav-item">
						<a class="nav-link" href="/board/noti">알림<span class="badge badge-warning">?</span></a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="/board/msg">메시지<span class="badge badge-warning">?</span></a>
					</li>
				</sec:authorize>
				<sec:authorize access="isAnonymous()">
					<li class="nav-item"><a class="nav-link" href="/board/login">로그인</a></li>
				</sec:authorize>
			</ul>
		</div>
	</nav>