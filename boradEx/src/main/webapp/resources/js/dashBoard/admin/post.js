$("document").ready(function(){
	
	const check	= new Check("user");
	const pagination = new Pagination();
	
	const tableRowSkeleton = document.getElementById("table-row").innerHTML;
	const tableRowTemplate = Handlebars.compile(tableRowSkeleton);
	// 선택한 게시글 삭제
	$(".btn-usertype").on("click", function(e){
		
		// 체크된 게시물 번호 목록 생성
		check.appendCheckVal(pagination.page);
		check.flatToList(check.repo);
		
		const showType = $("input[type='radio']:checked").val();
		$.post({
			url: "/board/api/rem",
			data: JSON.stringify(check.list),
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
		
		let option = $("select option:selected").val();
		if(option === "n") option = "tcw";
		
		const keyword = $("input[name='new-keyword']").val();
		if(!keyword){
			alert("검색어를 입력하세요");
			return;
		}
		
		const vals = makeQuery.prototype.vals;
		window.location.href = "/board/admin/post?"+"page="+vals.page+"&perPageNum="+vals.perPageNum
								+"&searchType=" + option
								+"&keyword=" + encodeURIComponent(keyword)
								+"&type="+vals.type;
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
			type: ""
	};
});