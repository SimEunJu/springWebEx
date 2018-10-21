function checkImageType(fileName){
	var pattern = /jpg$|png$|gif$/i;
	return fileName.match(pattern);
}

function getFileInfo(fullName){
	var imgsrc, getLink, fileName;
	
	var fileLink;
	
	if(checkImageType(fullName)){
		imgsrc = "/displayFile?fileName="+fullName;
		fileLink = fullName.substring(14);
		var front  = fullName.substring(0,12);
		var end = fullName.substring(14);
		getLink = "/displayFile?fileName="+front+end;
	}else{
		imgsrc = "/resources/dist/img/file.png";
		fileLink = fullName.substring(12);
		getLink = "/displayFile?fileName="+fullName;
	}
	fileName = fileLink.substring(fileLink.indexOf("_")+1);
	return {
		imgsrc: imgsrc,
		getLink: getLink,
		fileName: fileName,
		fullName: fullName
	};
}