<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">File Attach</div>
			
			<div class="panel-body">
				<div class="form-group upload-div">
					<input type="file" name="upload-file" multiple>
				</div>
				
				<div class="upload-result">
					<ul>
					
					</ul>
				</div>
			</div>
		</div>
	</div>
</div>

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
	var inputFile = $("input[name='upload-file']");
	var files = inputFile[0].files;
	for(var i=0; i<files.length; i++){
		if(!checkExtension(files[i].name, files[i].size)) return false;
		formData.append("uploadFile", files[i]);
	}
	$.ajax({
		url: "/uploadAjax",
		data: form,
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
	if(!res || res.length === 0) return;
	var uploadUl = $(".upload-result ul");
	var str = "";
	$(res).each(function(i, obj){
		if(obj.image){
			var filePath = encodeURIComponent(obj.uploadPath+"/s_"+obj.uuid+"_"+obj.fileName);
			str += "<li data-path='"+obj.uploadPath"' data-uuid='"+obj.uuid+"' data-filename='"+obj.fileName+"' data-type='"+obj.image+"'><div><span>"+obj.fileName+"</span><button type='button' data-file='"+filePath+"' data-type='image' class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
			str += "<img src='/displyFile?fileName="+filePath+"'></div></li>";
		}else{
			str += "<li data-path='"+obj.uploadPath"' data-uuid='"+obj.uuid+"' data-filename='"+obj.fileName+"' data-type='"+obj.image+"'><div><span>"+obj.fileName+"</span><button type='button' data-file='"+filePath+"' data-type='image' class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
			str += "<img src='/resources/img/attach.png'></div></li>";
		}
	});
	uploadUl.html(str);
}

$(".uploadResult").on("click", "button", function(e){
	var file = $(this).data("file");
	var type = $(this).data("type");
	var li = $(this).closest("li");
	$.ajax({
		url: "/deleteFile",
		type: "post",
		dataType: "json",
		data: {file: file, type: type},
		success: function(){
			li.remove();
		}
	});
});

</script>