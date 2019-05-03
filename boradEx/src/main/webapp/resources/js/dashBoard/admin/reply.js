$("document").ready(function(){

	addHandlebarHelper();
	
	const tableRowSkeleton = document.getElementById("table-row").innerHTML;
	const tableRowTemplate = Handlebars.compile(tableRowSkeleton);
	
	const check = new Check("reply");
	const pagination = new Pagination();
	
	$("#btn-del").on("click", function(e){
		check.appendCheckVal(pagination.page);
		const replyNoList = check.flatToList(check.repo);
		
		$.ajax({
			url: "/api/reply/del",
			method: "DELETE",
			data: JSON.stringify(replyNoList),
			contentType: "application/json; charset=utf-8"
		}).done(function(res){
			check.resetRepo();
			if(queryObj.type !== "self"){
				res.foreach(r => r.showReplyer = true);
			}
			check.tbody.html(tableRowTemplate(res));
			alert("정상적으로 삭제되었습니다.");
		});
	});
	
	
	const queryObj = {type: $("form input[name='type']").val()};
	
	$("nav .pagination").on("click", "li", function(e){
		e.preventDefault();
		const page = e.target.getAttribute("href");
		const user = window.location.pathname.match(/user|admin/)[0];
		window.location.href = `/board/${user}/reply?page=${page}&type=${queryObj.type}`;
	});
	
	// 신고순으로 정렬
	$("input[type='radio']").on("click", function(e){
		const replyType = $(e.target).val();
		queryObj.type = replyType;
		window.location.href = `/board/admin/reply?page=1&type=${queryObj.type}`;
	});
	
});