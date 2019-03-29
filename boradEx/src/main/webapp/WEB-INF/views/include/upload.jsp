<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div class="row">
	<div class="col-md-12">
		<div class="card">
			<div class="card-header">첨부 파일</div>
			
			<div class="card-body">
				<div class="form-group upload">
					<input type="file" name="uploadFile" multiple="multiple">
				</div>
				
				<div class="upload-result">
					<ul>
					
					</ul>
				</div>
			</div>
		</div>
	</div>
</div>

<script id="file-info" type="text/x-handlebars-template">
{{#each this}}
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
			<img src='/resources/img/attach.png'>
		{{/if}}
	</div>
</li>
</script>

