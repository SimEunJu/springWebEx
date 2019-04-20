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
		
		const tabObj = {
			all : $(".nav .all"),
			hot : $(".nav .hot")
		}
		
		$(".nav a").on("click", function(e){
			const target = $(e.target);
			const type = target.attr("class").match(/all|hot/)[0];
			makeQuery.prototype.vals.type = type;
			window.location.href = "/board/daily?"+makeQuery();
		});
		
		$("#search-btn").on("click", function(e){
			
			let option = $("select option:selected").val();
			if(option === "n") option = "tcw";
			
			const keyword = $("input[name='new-keyword']").val();
			if(!keyword){
				alert("검색어를 입력하세요");
				return;
			}
			
			const vals = makeQuery.prototype.vals;
			window.location.href = "/board/daily?"+"page=1"+"&perPageNum="+vals.perPageNum
									+"&searchType=" + option
									+"&keyword=" + encodeURIComponent(keyword)
									+"&type="+vals.type;
		});
		
		$("#newBtn").on("click", function(){
			window.location.href = "/board/daily/new";
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
				page : 1,
				perPageNum : $("form input[name='perPageNum']").val() ? $("form input[name='perPageNum']").val() : 10,
				searchType : $("form input[name='searchType']").val(),
				keyword : $("form input[name='keyword']").val(),
				type: $("form input[name='type']").val()
		};
		console.dir(makeQuery);
	})