var fileService = (function(){
	function getFilesInfo(){
		var str = "";
		$(".upload-result ul li").each(function(i, obj){
			var fileObj = $(obj);
			str += "<input type='hidden' name='files["+i+"].fileName' value='"+fileObj.data("filename")+"'/>";
			str += "<input type='hidden' name='files["+i+"].uuid' value='"+fileObj.data("uuid")+"'/>";
			str += "<input type='hidden' name='files["+i+"].uploadPath' value='"+fileObj.data("path")+"'/>";
			str += "<input type='hidden' name='files["+i+"].fileType' value='"+fileObj.data("type")+"'/>";
		});
		
		return str;
	}
	function showFiles(res){
		if(!res || res.length === 0) return;
		var str = "";
		$(res).each(function(i, obj){
			if(obj.fileType){
				var filePath = encodeURIComponent(obj.uploadPath+"\\s_"+obj.uuid+"_"+obj.fileName);
				str += "<li data-path='"+obj.uploadPath+"' data-uuid='"+obj.uuid+"' data-filename='"+obj.fileName+"' data-type='"+obj.fileType+"'><div><span>"+obj.fileName+"</span><button type='button' data-file='"+filePath+"' data-type='image' class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
				str += "<img src='/displayFile?fileName="+filePath+"'></div></li>";
			}else{
				var filePath = encodeURIComponent(obj.uploadPath+"/"+obj.uuid+"_"+obj.fileName);
				str += "<li data-path='"+obj.uploadPath+"' data-uuid='"+obj.uuid+"' data-filename='"+obj.fileName+"' data-type='"+obj.fileType+"'><div><span>"+obj.fileName+"</span><button type='button' data-file='"+filePath+"' data-type='image' class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
				str += "<img src='/resources/img/attach.png'></div></li>";
			}
		});
		return str;
	}
	return {
		getFilesInfo: getFilesInfo,
		showFiles: showFiles
	};
	
})();