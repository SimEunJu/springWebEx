<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>

<form>
		<div class="form-group">
			<label for="title">제목</label> 
			<input type="text" readonly="readonly" value="${board.title }" name="title" class="form-control">
		</div>
		<div class="form-group">
			<label for="content">내용</label>
			<textarea class="form-control" id="editor" readonly="readonly" row="3" name="content"></textarea>
		</div>
		<div class="form-group">
			<label class="wrtier">글쓴이</label> 
			<input type="text" readonly="readonly" value="${board.writer }" name="writer" class="form-control">
		</div>
	</form>
	<div class="row">
		<div class="col-md-12">
			<div class="card">
				<div class="card-header">첨부파일</div>

				<div class="card-body">
					<div class="upload-result">
						<ul>

						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>

<script id="upload-item" type="text/x-handlebars-template">
{{#each attaches}}
<li data-path='{{uploadPath}}' data-uuid='{{uuid}}' data-filename='{{fileName}}' data-type='{{fileType}}'>
	<div>
		{{#if isImg}}
			<img src='/board/daily/file?fileName={{filePath}}'>
		{{else}}
			<img src='/resources/img/attach.png'>
		{{/if}}
		<span>{{fileName}}</span>
	</div>
</li>
{{/each}}
</script>

<script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.1.0/handlebars.min.js"></script>
<script src="https://cdn.ckeditor.com/ckeditor5/11.2.0/classic/ckeditor.js"></script>

<script>
    ClassicEditor
        .create(document.querySelector( '#editor' ))
        .then(
        	editor => {editor.setData("${board.content}"); editor.isReadOnly = true;}
        ).catch( 
        	error => {console.error( error );}
        );
</script>
