$("document").ready(function(){
	
	addHandlebarHelper();
	
	const tableRowSkeleton = document.getElementById("table-row").innerHTML;
	const tableRowTemplate = Handlebars.compile(tableRowSkeleton);
	
	const check = new Check("msg");
	const pagination = new Pagination();

	$("#btn-del").on("click", function(e){
		check.appendCheckVal(pagination.page);
		const msgNoList = flatObjToList(check.repo, 'msgNo');
		ajax({
			url: "/board/user/msg/del",
			method: "post",
			data: JSON.stringify(msgNoList),
			contentType: "application/json; charset=utf-8"
		}, function(res){
			console.log(res);
			check.tbody.html(tableRowTemplate(res));
		});
	});
});