<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<form role="form" method="post">
	<input type="hidden" name="page" value="${cri.page}"> 
	<input type="hidden" name="perPageNum" value="${cri.perPageNum}">
	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
	
	<div class="form-group">
		<label for="bno">게시물 번호</label> 
		<input type="text" readonly="readonly" value="${board.bno }" name="bno" class="form-control">
	</div>
	
	<div class="form-group">
		<label for="title">제목</label> 
		<input type="text" value="${board.title }" name="title" class="form-control" placeholder="제목을 입력해주세요">
	</div>
	
	<div class="form-group content">
		<label for="content">내용</label>
		<textarea name="content" id="editor" name="content"></textarea>
	</div>
	
	<div class="form-group">
		<label class="writer">글쓴이</label> 
		<input type="text" value="${board.writer}" name="writer" class="form-control" readonly>
	</div>

	<div class="row-12">
		<ul class="uploadedList">

		</ul>
		<button type="submit" role="modify" class="btn btn-outline-primary">저장</button>
		<!-- <button type="submit" role="delete" class="btn btn-outline-warning">삭제</button> -->
		<button type="submit" role="list" class="btn btn-outline-info">목록으로</button>
	</div>
</form>

<%@ include file="/WEB-INF/views/include/upload.jsp" %>

<script id="file-info" type="text/x-handlebars-template">
{{#each files}}
	<input type='hidden' name='files[{{idx}}].fileName' value='{{fileName}}'/>";
	<input type='hidden' name='files[{{idx}}].uuid' value='{{uuid}}'/>";
	<input type='hidden' name='files[{{idx}}].uploadPath' value='{{uploadPath}}'/>";
	<input type='hidden' name='files[{{idx}}].fileType' value='{{fileType}}'/>";
{{/each}}
</script>
<script id="upload-item" type="text/x-handlebars-template">
<li data-path='{{uploadPath}}' data-uuid='{{uuid}}' data-filename='{{fileName}}' data-type='{{fileType}}'>
	<div>
		<span>{{fileName}}</span>
		<button type='button' data-file='{{filePath}}' data-type='{{fileType}}' class='btn btn-sm btn-warning'>
			&times;
		</button>
		<br>
		{{#if isImg}}
			<img src='/board/daily/file?fileName={{filePath}}'>
		{{else}}
			<img src='/resources/img/attach.png' style='height: 100px;'>
		{{/if}}
	</div>
</li>
</script>

<script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.1.0/handlebars.min.js"></script>
<script src="https://cdn.ckeditor.com/ckeditor5/11.2.0/classic/ckeditor.js"></script>

<script src="/resources/js/utils/file.js"></script>
<script>
    ClassicEditor
        .create(document.querySelector( '#editor' ),{
        	toolbar: [ 'bold', 'italic', 'bulletedList', 'numberedList', 'blockQuote' ],
        })
        .then(
        	editor => {editor.setData("${board.content}");}
        ).catch( 
        	error => {console.error( error );}
        );
</script>

<script src="/resources/js/include/upload.js"></script>