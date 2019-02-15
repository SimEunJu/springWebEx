<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<link rel="stylesheet" type="text/css" href="/resources/css/upload.css" />

<%@ include file="../include/header.jsp" %>

<div class="box-body">
	<form role="form" id="registerForm" method="post">
		<div class="box-body">
			<div class="form-group">
				<label for="exampleInputEmail1">Title</label>
				<input type="text" name="title" class="form-control" placeholder="Enter Title">
			</div>
			<div class="form-group">
				<label for="exampleInputPassword1">Content</label>
				<textarea name="content" id="editor" name="content"></textarea>
			</div>
			<div class="form-group">
				<label class="exampleInputEmail1">Writer</label>
				
				<sec:authorize access="isAuthenticated()">
					<sec:authentication property="principal.username" var="writer" />
				</sec:authorize>
				<c:choose>
					<c:when test="${writer != null}">
						<input type="text" name="writer" class="form-control" value="${writer}" readonly>
					</c:when>
					<c:otherwise>
						<input type="text" name="writer" placeholder="Enter Writer" class="form-control" >
						<input type="password" name="password" 
							placeholder="4자리 비밀번호를 입력해주세요" class="form-control" >
					</c:otherwise>
				</c:choose>
			</div>
			<input type="hidden" value="${_csrf.token }" name="${_csrf.parameterName }">
		</div>
		<div class="box-footer">
			<button type="submit" class="btn btn-primary">Submit</button>
		</div>
	</form>
</div>

<%@ include file="../include/upload.jsp" %>

<%@ include file="../include/footer.jsp" %>
	
<script src="/resources/js/file.js"></script>

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