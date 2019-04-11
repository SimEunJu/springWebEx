<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>	

<form role="form" id="register-form" action="/board/daily/notice/new" method="post">
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
        .create( document.querySelector( '#editor' ) )
        .then( newEditor => {
       		editor = newEditor;
    	})
        .catch( error => {
            console.error( error );
        } );
</script>

<script src="/resources/js/board/new.js"></script>