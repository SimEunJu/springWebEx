<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>	

<tiles:importAttribute name="titleKey" ignore="true"/>
<tiles:importAttribute name="path" ignore="true" />
<spring:eval expression="@title['${titleKey}']" var="title" scope="request"/>

<!DOCTYPE html>

<html lang="ko">

<head>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<sec:csrfMetaTags />

<c:choose>
	<c:when test="${empty title}">
		<title>${specificTitle}</title>
	</c:when>
	<c:otherwise>
		<title>${title}</title>
	</c:otherwise>
</c:choose>

<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
	crossorigin="anonymous">
<script src="https://code.jquery.com/jquery-3.3.1.min.js"
	integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
	crossorigin="anonymous"></script>
</head>

<body>
<tiles:insertAttribute name="header" />

<div class="container">
	<tiles:insertAttribute name="body" /> 
</div> 
<tiles:insertAttribute name="footer" />

<script type="text/javascript" src="/resources/js/${path}.js"></script>
<sec:authorize access="isAuthenticated()">
	<script src="/resources/js/common/polling.js"></script>
</sec:authorize>
</body>
</html>