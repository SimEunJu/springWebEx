<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">File Attach</div>
			
			<div class="panel-body">
				<div class="form-group upload-div">
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

<script src="/resources/js/file.js"></script>

<script>

var regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$");
var maxSize = 5242880;

function checkExtension(fileName, fileSize){
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
	var formData = new FormData();
	var inputFile = $("input[name='uploadFile']");
	var files = inputFile[0].files;
	for(var i=0; i<files.length; i++){
		if(!checkExtension(files[i].name, files[i].size)) return false;
		formData.append("uploadFile", files[i]);
	}
	
	$.ajax({
		url: "/uploadAjax",
		data: formData,
		dataType: "json",
		processData: false,
		contentType: false,
		type: "post",
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
	console.log(li);
	$.ajax({
		url: "/deleteFile",
		type: "post",
		dataType: "json",
		data: {fileName: fileName, type: type},
		success: function(){
			console.log(li);
			li.remove();
		},
		error: function(xhr, status, err){
			console.dir(xhr, status, err);
		}
	});
});

</script>