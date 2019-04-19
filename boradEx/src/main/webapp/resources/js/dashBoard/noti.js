$("document").ready(function(){
	
	addHandlebarHelper();
	
	const tableRowSkeleton = document.getElementById("table-row").innerHTML;
	const tableRowTemplate = Handlebars.compile(tableRowSkeleton);
	
	const check = new Check("msg");
	const pagination = new Pagination();

	check.tbody.on("click", "tr a", function(e){
		e.preventDefault();
		// 페이지 이동이 발생하는데 정상적으로 작동할까?
		longPollObj.initialize("noti");
		window.location.href = e.target.href;
	})
	
	$("#btn-del").on("click", function(e){
		check.appendCheckVal(pagination.page);
		const notiNoList = check.flatToList(check.repo);
		$.ajax({
			url: "/board/user/noti/del",
			method: "post",
			data: JSON.stringify(notiNoList),
			contentType: "application/json; charset=utf-8"
		}).done(function(res){
			console.log(res);
			check.tbody.html(tableRowTemplate(res));
			
			longPollObj.initialize("noti");
		});
	});
	
	$("nav .pagination").on("click", "li", function(e){
		e.preventDefault();
		const page = e.target.getAttribute("href");
		console.log(page);
		const user = window.location.pathname.match(/user|admin/)[0];
		window.location.href = `/board/${user}/noti?`+pagination.makeQuery(page);
	})
});