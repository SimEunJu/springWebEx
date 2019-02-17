<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html lang="ko">

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
 	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
 	
    <title>daily 새 글</title>    
    
	<%@include file="../include/cssFiles.jsp" %>
	<link rel="stylesheet" type="text/css" href="/resources/css/upload.css" />

</head>

<body>

<div class="box-body">
	<form role="form" id="registerForm" action="/board/daily/new" method="post">
		<div class="box-body">
			<div class="form-group">
				<label for="exampleInputEmail1">제목</label>
				<input type="text" name="title" class="form-control" placeholder="Enter Title">
			</div>
			<div class="form-group">
				<label for="exampleInputPassword1">Content</label>
				<textarea name="content" id="editor" name="content"></textarea>
			</div>
			<div class="form-group">
				<sec:authorize access="isAuthenticated()">
					<sec:authentication property="principal.username" var="writer" />
				</sec:authorize>
				<c:choose>
					<c:when test="${writer eq null}">
						<label class="exampleInputEmail1">Writer</label>
						<input type="text" name="writer" placeholder="Enter Writer" class="form-control" >
						<input type="password" name="password" 
							placeholder="4자리 비밀번호를 입력해주세요" class="form-control" >
					</c:when>
				</c:choose>
			</div>
			<input type="hidden" value="${_csrf.token }" name="${_csrf.parameterName }">
		</div>
		<div class="box-footer">
			<button type="submit" class="btn btn-primary">Submit</button>
		</div>
	</form>
</div>

<%@ include file="../include/jsFiles.jsp" %>

<%@ include file="../include/upload.jsp" %>

<script src="https://cdn.ckeditor.com/ckeditor5/11.2.0/classic/ckeditor.js"></script>

<script>
    ClassicEditor
        .create( document.querySelector( '#editor' ) )
        .catch( error => {
            console.error( error );
        } );
</script>

<script type="text/javascript">

var form = $("#registerForm");

$("button[type='submit']").on("click", function(e){
	e.preventDefault();
	console.dir(form);
	var str = fileService.getFilesInfo();
	form.append(str).submit();
});
</script>
</body>
</html>