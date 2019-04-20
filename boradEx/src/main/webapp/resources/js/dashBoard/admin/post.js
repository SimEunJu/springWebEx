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
			url: "/board/api/admin/rem",
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
	
	
	// 신고순으로 정렬
	$("input[type='radio']").on("click", function(e){
		const boardType = $(e.target).val();
		makeQuery.prototype.vals.type = boardType;
		window.location.href = "/board/admin/post?"+makeQuery();
	});
	
	// 검색
	$("#search-btn").on("click", function(e){
		
		let option = $("select[name='searchType'] option:selected").val();
		if(option === "n") option = "tcw";
		
		const keyword = $("input[name='new-keyword']").val();
		if(!keyword){
			alert("검색어를 입력하세요");
			return;
		}
		
		const vals = makeQuery.prototype.vals;
		window.location.href = "/board/admin/post?"+"page=1"+"&perPageNum="+vals.perPageNum
								+"&searchType=" + option
								+"&keyword=" + encodeURIComponent(keyword)
								+"&type="+vals.type;
	});
	
	$("nav .pagination").on("click", "li", function(e){
		e.preventDefault();
		const page = e.target.getAttribute("href");
		console.log(page);
		const user = window.location.pathname.match(/user|admin/)[0];
		window.location.href = `/board/${user}/post?`+pagination.makeQuery(page);
	});
	
	function makeQuery(){
		let query = "";
		const vals = makeQuery.prototype.vals;
		for(let attr in vals){
			query += attr+"="+vals[attr]+"&";
		}
		query = query.slice(0, query.length-1);
		return query;
	}
	makeQuery.prototype.vals = {
			page : $("form input[name='page']").val() ? $("form input[name='page']").val() : 1,
			perPageNum : $("form input[name='perPageNum']").val() ? $("form input[name='perPageNum']").val() : 10,
			searchType : $("form input[name='searchType']").val(),
			keyword : $("form input[name='keyword']").val(),
			type: $("form input[name='type']").val(),
	};
});