const form = $("#register-form");
const formObj = {
	title : $("form input[name='title']"),
	content : editor,
	
}
$("button[type='submit']").on("click", function(e){
	e.preventDefault();
	if(formObj.title.val().trim().length === 0){
		alert("제목을 입력해주세요");
		return;
	}
	console.log(formObj.content.getData());
	if(formObj.content.getData().trim().length === 0){
		alert("내용을 입력해주세요");
		return;
	}
	var str = fileService.getFilesInfo(fileinfoTemplate);
	form.append(str).submit();
});