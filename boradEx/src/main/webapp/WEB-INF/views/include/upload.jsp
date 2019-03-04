<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div class="row">
	<div class="col-md-12">
		<div class="card">
			<div class="card-title">첨부 파일</div>
			
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
		<button type='button' data-file='{{filePath}}' data-type='{{fileType}}' class='btn btn-warning btn-circle'>
			<i class='fa fa-times'></i>
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

<script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.1.0/handlebars.min.js"></script>
<script src="/resources/js/file.js"></script>

<script type="text/javascript">

const csrfHeader = "${_csrf.headerName}";
const csrfTokenVal = "${_csrf.token}";

const uploadItemSkeleton = document.getElementById("upload-item").innerHTML;
const uploadItemTemplate = Handlebars.compile(uploadItemSkeleton);

const fileInfoSkeleton = document.getElementById("file-info").innerHTML;
const fileIntoTemplate = Handlebars.compile(fileInfoSkeleton);

const regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$");
const maxSize = 5242880;

function checkFile(fileName, fileSize){
	if(fileSize >= maxSize){
		alert("사이즈 초과");
		return false;
	}
	if(regex.test(fileName)){
		alert("지원하지 않는 확장자입니다.");
		return false;
	}
	return true;
}

$("input[type='file']").change(function(e){
	let formData = new FormData();
	const inputFile = $("input[name='uploadFile']");
	const files = inputFile[0].files;
	for(let i=0; i<files.length; i++){
		if(!checkFile(files[i].name, files[i].size)) return false;
		formData.append("uploadFile", files[i]);
	}
	
	$.ajax({
		url: "/board/daily/file",
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeader, csrfTokenVal);
		},
		data: formData,
		dataType: "json",
		processData: false,
		contentType: false,
		method: "post",
		success: function(data){
			showUploadResult(data);
		}
	});
});

function showUploadResult(res){
	var files = fileService.showFiles(res);
	$(".upload-result ul").append(files);
}

$(".upload-result").on("click", "button", function(e){
	var fileName = $(this).data("file");
	var type = $(this).data("type");
	var li = $(this).closest("li");
	
	$.ajax({
		url: "/board/daily/file",
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeader, csrfTokenVal);
		},
		method: "delete",
		dataType: "json",
		data: {fileName: fileName, type: type}
	}).done(function(){
		li.remove();
		
	}).fail(function(xhr, status, err){
		console.error(xhr, status, err);
	
	});
});

</script>