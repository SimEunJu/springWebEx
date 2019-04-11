$("document").ready(function(){

	const bno = $("input[name='bno']").val();
		
	$.getJSON("/board/daily/"+bno+"/attach", function(res){
		const files = fileService.showFiles(res);
		$(".upload-result ul").html(files);
	});
	
	var formObj = $("form[role='form']");
		
	$("button[type='submit']").on("click", function(e){
			
		e.preventDefault();
			
		var role = $(this).attr("role");
		
		 if(role === "modify"){
			formObj.attr("method", 'post');
			formObj.attr("action", "/board/daily/"+bno);
			
		}else{
			window.location.href = "/board/daily"+"${pageMaker.makeSearch(pagekMaker.cri.getPage)}";
			return;
		}
		
		formObj.submit();
	});
	
});
	