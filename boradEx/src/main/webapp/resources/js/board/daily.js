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
		
		$("#searchBtn").on("click", function(e){
			
			if(!$("select option:selected").val()){
				alter("검색 종류를 입력하세요");
				return;
			}
			
			if(!$("input[name='keyword']").val()){
				alter("검색 종류를 입력하세요");
				return;
			}
			
			window.location.href = "/board/daily"+"${pagination.cri.makeQuery()}"
									+"&searchType=" + $("select option:selected").val()
									+"&keyword=" + encodeURIComponent($("input[name='keyword']").val());
		});
		
		$("#newBtn").on("click", function(){
			window.location.href = "/board/daily/new";
		});
		
	})