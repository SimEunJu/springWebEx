var form = $("#register-form");

$("button[type='submit']").on("click", function(e){
	e.preventDefault();
	var str = fileService.getFilesInfo(fileinfoTemplate);
	form.append(str).submit();
});