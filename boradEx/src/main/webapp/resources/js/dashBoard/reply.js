$("document").ready(function(){
	
	
	addHandlebarHelper();
	
	const tableRowSkeleton = document.getElementById("table-row").innerHTML;
	const tableRowTemplate = Handlebars.compile(tableRowSkeleton);
	
	const check = new Check("reply");
	const pagination = new Pagination();
	
	$("#btn-del").on("click", function(e){
		check.appendCheckVal(pagination.page);
		const msgNoList = flatToList(check.repo, 'reply');
		$.ajax({
			url: "/board/user/reply/",
			method: "delete",
			data: JSON.stringify(msgNoList),
			contentType: "application/json; charset=utf-8"
		}).then(function(res){
			console.log(res);
			check.tbody.html(tableRowTemplate(res));
		});
	});
});