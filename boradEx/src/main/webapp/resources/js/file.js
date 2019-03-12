

const fileService = (function(){
	function getFilesInfo(template){
		let files = [];
		$(".upload-result ul li").each(function(i, obj){
			const fileObj = obj;
			files.push({
				idx: i,
				fileName: fileObj.dataset.filname,
				uuid: fileObj.dataset.uuid,
				uploadPath: fileObj.dataset.path,
				fileType: fileObj.dataset.type
			})
		});
		str = template(files);
		return str;
	}
	function showFiles(res){
		if(!res || res.length === 0) return;
		$(res).each(function(i, obj){
			let filePath = "";
			if(obj.fileType.includes("image")){
				filePath = encodeURIComponent(obj.uploadPath+"/s_"+obj.uuid+"_"+obj.fileName);
				obj.isImg = true;
				
			}else{
				filePath = encodeURIComponent(obj.uploadPath+"/"+obj.uuid+"_"+obj.fileName);
				obj.isImg = false;
			}
			obj.filePath = filePath;
		});
		return uploadItemTemplate(res[0]);
	}
	return {
		getFilesInfo: getFilesInfo,
		showFiles: showFiles
	};
})();