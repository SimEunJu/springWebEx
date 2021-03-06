<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>	

<form role="form" id="register-form" action="/board/daily/new" method="post">
	<div>
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
		<div class="form-group">
			<label for="title">제목</label> 
			<input type="text" name="title" class="form-control" placeholder="제목" required>
		</div>
		
		<div class="form-group">
			<label for="content">내용</label>
			<textarea name="content" id="editor" required></textarea>
		</div>
		
		<div class="form-group">
			<sec:authorize access="isAuthenticated()">
				<sec:authentication property="principal.username" var="writer" />
			</sec:authorize>
			<c:choose>
				<c:when test="${writer eq null}">
					<label class="exampleInputEmail1">Writer</label>
					<input type="text" name="writer" value="익명" class="form-control"
						readonly>
					<input type="password" name="password" placeholder="비밀번호를 입력해주세요"
						class="form-control">
				</c:when>
			</c:choose>
		</div>
	</div>
	
	<div>
		<button type="submit" class="btn btn-outline-primary">등록</button>
	</div>
</form>

<%@ include file="/WEB-INF/views/include/upload.jsp" %>

<script src="https://cdn.ckeditor.com/ckeditor5/11.2.0/classic/ckeditor.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.1.0/handlebars.min.js"></script>

<script src="/resources/js/utils/file.js"></script>
<script src="/resources/js/include/upload.js"></script>

<script>
let editor;
    ClassicEditor
        .create( document.querySelector( '#editor' ), {
        	toolbar: [ 'bold', 'italic', 'bulletedList', 'numberedList', 'blockQuote' ],
        	/* removePlugins: [ 'Image', 'MediaEmbed' ], */
        })
        .then( newEditor => {
       		editor = newEditor;
    	})
        .catch( error => {
            console.error( error );
        } );
   
</script>
