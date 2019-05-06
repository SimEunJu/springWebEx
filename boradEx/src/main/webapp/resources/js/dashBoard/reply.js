$("document").ready(function(){
	
	addHandlebarHelper();
	
	const tableRowSkeleton = document.getElementById("table-row").innerHTML;
	const tableRowTemplate = Handlebars.compile(tableRowSkeleton);
	
	const check = new Check("reply");
	const pagination = new Pagination();
	
	$("#btn-del").on("click", function(e){
		check.appendCheckVal(pagination.page);
		const replyNoList = flatToList(check.repo, 'reply');
		$.ajax({
			url: "/api/reply/del",
			method: "delete",
			data: JSON.stringify(replyNoList),
			contentType: "application/json; charset=utf-8"
		}).then(function(res){
			console.log(res);
			check.tbody.html(tableRowTemplate(res));
		});
	});
	
	$("nav .pagination").on("click", "li", function(e){
		e.preventDefault();
		const page = e.target.getAttribute("href");
		console.log(page);
		const user = window.location.pathname.match(/user|admin/)[0];
		window.location.href = `/board/${user}/reply?`+pagination.makeQuery(page);
	})
});