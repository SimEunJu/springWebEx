$("document").ready(function(){

	const bno = $("input[name='bno']").val();
		
	$.getJSON("/api/board/"+bno+"/attach", function(res){
		const files = fileService.showFiles(res);
		$(".upload-result ul").html(files);
	});
	
	var formObj = $("form[role='form']");
		
	$("button[type='submit']").on("click", function(e){
			
		e.preventDefault();
			
		var role = $(this).attr("role");
		
		 if(role === "modify"){
			const str = fileService.getFilesInfo(fileinfoTemplate);
			formObj.append(str);
			formObj.attr("method", 'post');
			formObj.attr("action", "/board/daily/"+bno+"/mod");
			
		}else{
			window.location.href = "/board/daily";
			return;
		}
		
		formObj.submit();
	});
	
});
	