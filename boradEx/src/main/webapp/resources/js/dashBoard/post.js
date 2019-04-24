$("document").ready(function(){
	
	const check	= new Check("post");
	const pagination = new Pagination();
	
	const tableRowSkeleton = document.getElementById("table-row").innerHTML;
	const tableRowTemplate = Handlebars.compile(tableRowSkeleton);
	// 선택한 게시글 삭제
	$("#btn-del").on("click", function(e){
		
		// 체크된 게시물 번호 목록 생성
		check.appendCheckVal(pagination.page);
		check.flatToList(check.repo);
		
		if(check.list.length === 0){
			alert("선택된 항목이 없습니다.");
			return;
		}
		
		const showType = $("input[type='radio']:checked").val();
		$.post({
			url: "/api/board/rem",
			data: JSON.stringify({bnoList: check.list, reason: $("select[name='delete'] option:selected").val()}),
			contentType: "application/json; charset=utf-8"
		})
		.done(function(posts){
			const tableRow = tableRowTemplate(posts);
			check.tbody.html(tableRow);

			check.resetRepo();
			alert("정상적으로 삭제되었습니다.");
	
		}).fail(showAjaxError);
	});
	
	$("nav .pagination").on("click", "li", function(e){
		e.preventDefault();
		const page = e.target.getAttribute("href");
		console.log(page);
		const user = window.location.pathname.match(/user|admin/)[0];
		window.location.href = `/board/${user}/post?`+pagination.makeQuery(page);
	});
	
});