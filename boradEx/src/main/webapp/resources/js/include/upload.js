
const uploadItemSkeleton = document.getElementById("upload-item").innerHTML;
const uploadItemTemplate = Handlebars.compile(uploadItemSkeleton);

const fileInfoSkeleton = document.getElementById("file-info").innerHTML;
const fileinfoTemplate = Handlebars.compile(fileInfoSkeleton);

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
	var fileName = this.dataset.file;
	var type = this.dataset.type;
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