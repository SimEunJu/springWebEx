$("document").ready(function(){
	
	const tableRowSkeleton = document.getElementById("table-row").innerHTML;
	const tableRowTemplate = Handlebars.compile(tableRowSkeleton);
	
	const paginationSkeleton = document.getElementById("pagination-hb").innerHTML;
	const paginationTemplate = Handlebars.compile(paginationSkeleton);
	
	const receiverListSkeleton = document.getElementById("receiver-list-hb").innerHTML;
	const receiverListTemplate = Handlebars.compile(receiverListSkeleton);
	
	addHandlebarHelper();
	
	const check	= new Check("user");
	const msgModal = new MsgModal();
	const pagination = new Pagination();
	
	// 선택한 회원에게 메시지 보내기
	$(".btn-msg").on("click", function(){
		msgModal.title.innerHTML("메시지 전송");
		msgModal.type = "msg";
		msgModal.openMsgModal(check, pagination.page, receiverListTemplate);
	});
	
	// 선택한 회원에게 메일 보내기
	$(".btn-mail").on("click", function(){
		msgModal.title.innserHTML("메일 전송")
		msgModal.type = "mail";
		msgModal.openMsgModal(check, pagination.page, receiverListTemplate);
	})
	
	// 메시지 모달의 메시지 전송 버큰 클릭 시
	$(".modal-send").on("click", function(){
		let url = "";
		if(msgModal.type === "msg"){
			url = "/board/user/msg";
		}
		else if(msgModal.type === "mail"){
			url = "/board/api/admin/mail";
		}
		msgModal.sendMsg(check.list, url);
	});
	
	// 회원 상태 변경 버튼 클릭 시
	$(".btn-usertype").on("click", function(e){
		
		// 체크된 회원 목록 생성
		check.appendCheckVal(pagination.page);
		check.flatToList(check.repo);
		
		const type = $("select option:selected").val();
		if(type === "") return;
		if(!confirm("선택하신 회원의 상태를 정말 변경하시겠습니까?")) return;
		
		const showType = $("input[type='radio']:checked").val();
		$.post({
			url: "/board/api/admin/usertype?type="+type+"&showType="+showType,
			data: JSON.stringify(check.list),
			contentType: "application/json; charset=utf-8"
		})
		.done(function(users){
			const tableRow = tableRowTemplate(users);
			check.tbody.html(tableRow);

			check.reset();
			alert("선택된 회원의 상태를 변경 하였습니다.");
	
		}).fail(showAjaxError);
	});
	
	// 회원 찾기 버튼 클릭 시 
	$(".btn-find").on("click", function(e){
		
		const keyword = $(this).siblings("input[type='text']").val();
		if(keyword === "" || keyword.length === 0){
			alert("사용자 이름을 입력해 주세요");
			return;
		}
		
		$.getJSON("/board/api/admin/user/find?keyword="+encodeURIComponent(keyword))
			.done(function(users){
			
				check.resetRepo();
				const tableRow = tableRowTemplate(users);
				$("tbody").html(tableRow);
			
			}).fail(showAjaxError);
	})
	
	// 회원 유형 변경 시
	$("input[type='radio']").on("click", function(e){
		
		const userType = $(e.target).val();
		
		$.getJSON("/board/api/admin/user?type="+userType)
			.done(function(users){
				
				check.resetRepo();
				const tableRow = tableRowTemplate(users);
				$("tbody").html(tableRow);
			
			}).fail(showAjaxError);
	});
	
	// 페이징
	$(".pagination").on("click", function(e){
		e.preventDefault();
		
		const target = e.target.parent();
	
		let addedQuery = "";
		if(target.hasClass("prev")) addedQuery = {move: "prev"}; 
		else if(target.hasClass("next")) addedQuery = {move : "next"};
		else pagination.chanePage(target);

		check.appendCheckVal(pagination.page);
		
		const query = makeQuery(addedQuery);
		
		$.getJSON("/board/api/user?"+query)
			.done(function(res){
				const tableRow = tableRowTemplate(res.users);
				check.tbody.html(tableRow);
				
				// next, prev 선택 시 pagination 재생성
				if(addedQuery === ""){
					const pagination = paginationTemplate(res.pagination);
					pagination.ele.html(pagination);
					
					const startPage = res.pagination.match(/startPage=d+/)[0];
					pagination.chagePage(pagination.ele.find("a[href='"+startPage+"']"));
				}
			}).fail(showAjaxError);
	});
});