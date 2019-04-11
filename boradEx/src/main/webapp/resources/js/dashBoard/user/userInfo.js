$("document").ready(function(){
	$(".btn-leave").on("click", function(e){
		e.preventDefault();
		
		if(confirm("정말 탈퇴하시겠습니까?")){
			
			$.get("/board/user/leave")
			.done(function(){
				window.location.href="/board/daily";
			}).fail(function(){
				alert("탈퇴에 실해파였습니다. 다시 시도해주세요.");
			});
		}
		
	});
});