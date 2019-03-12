<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ include file="../common/header.jsp"%>

<div class="container">
	<form role="form" id="register-form" action="/board/daily/new" method="post">
		<div>
			<div class="form-group">
				<label for="title">제목</label>
				<input type="text" name="title" class="form-control" placeholder="Enter Title">
			</div>
			<div class="form-group">
				<label for="content">내용</label>
				<textarea name="content" id="editor" name="content"></textarea>
			</div>
			<div class="form-group">
				<sec:authorize access="isAuthenticated()">
					<sec:authentication property="principal.username" var="writer" />
				</sec:authorize>
				<c:choose>
					<c:when test="${writer eq null}">
						<label class="exampleInputEmail1">Writer</label>
						<input type="text" name="writer" value="익명" class="form-control" readonly>
						<input type="password" name="password" 
							placeholder="비밀번호를 입력해주세요" class="form-control" >
					</c:when>
				</c:choose>
			</div>
			<input type="hidden" value="${_csrf.token }" name="${_csrf.parameterName }">
		</div>
		<div>
			<button type="submit" class="btn btn-outline-primary">Submit</button>
		</div>
	</form>
</div>

<%@ include file="../common/footer.jsp"%>
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

var form = $("#register-form");

$("button[type='submit']").on("click", function(e){
	e.preventDefault();
	var str = fileService.getFilesInfo(fileinfoTemplate);
	form.append(str).submit();
});
</script>
</body>
</html>