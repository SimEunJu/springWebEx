$(document).ready(function(){
	const uploadItemSkeleton = document.getElementById("upload-item").innerHTML;
	const uploadItemTemplate = Handlebars.compile(uploadItemSkeleton);
	
	(function(){
		$.getJSON("/board/daily/"+board.bno+"/attach", function(res){
			$(res).each(function(i, attach){
				let filePath = "";
				let isImg = false;
				
				if(attach.fileType.includes("img")){
					filePath = encodeURIComponent(attach.uploadPath+"/s_"+attach.uuid+"_"+attach.fileName);
					isImg = true;	
				}
				attach.filePath = filePath;
			});
			const str = uploadItemTemplate(res)
			$(".upload-result ul").html(str);
		});
	})();
});