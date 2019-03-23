$("document").ready(function(){
		const msg = $("#data").data("msg");
		
		if(msg === "success" && !history.state){
			$(".modal-body").html("게시글이 등록되었습니다.");
			const modal = $('#modal');
			$("body").toggleClass("modal-open");
			modal.toggleClass("show");
			modal.css("display", "none");
			modal.attr("aria-hidden", "true");
		}
		
		history.replaceState({}, null, null);
		
		let type = "all";
		$(".nav a").on("click", function(e){
			const target = $(e.target);
			type = target.attr("class").match(/all|hot/)[1];
			window.location.href = "/board/daily?"+makeQuery();
		});
		
		$("#search-btn").on("click", function(e){
			
			if(!$("select option:selected").val()){
				alert("검색 종류를 입력하세요");
				return;
			}
			
			if(!$("input[name='new-keyword']").val()){
				alert("검색어를 입력하세요");
				return;
			}
			
			const vals = makeQuery.prototype.vals;
			window.location.href = "/board/daily?"+"page="+vals.page+"&perPageNum="+vals.perPageNum
									+"&searchType=" + $("select option:selected").val()
									+"&keyword=" + encodeURIComponent($("input[name='new-keyword']").val())
									+"&type="+type;
		});
		
		$("#newBtn").on("click", function(){
			window.location.href = "/board/daily/new";
		});
		
		function makeQuery(){
			let query = "";
			for(let attr in vals){
				query += attr+"="+vals[attr]+"&";
			}
			query = query.splice(0, query.length-1);
			return query;
		}
		makeQuery.prototype.vals = {
				page : $("form input[name='page']").val() ? $("form input[name='page']").val() : 1,
				perPageNum : $("form input[name='perPageNum']").val() ? $("form input[name='perPageNum']").val() : 10,
				searchType : $("form input[name='searchType']").val(),
				keyword : $("form input[name='keyword']").val(),
				type: $("form input[name='type']").val()
		};
		console.dir(makeQuery);
	})