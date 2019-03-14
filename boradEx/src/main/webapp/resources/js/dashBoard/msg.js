// 1. send msg 2. delete msg 3. read msg 4. pagination
$("document").ready(function(){
	

	
	addHandlebarHelper();
	
	const tableRowSkeleton = document.getElementById("table-row").innerHTML;
	const tableRowTemplate = Handlebars.compile(tableRowSkeleton);
	
	const receiverListSkeleton = document.getElementById("receiver-list-hb").innerHTML;
	const receiverListTemplate = Handlebars.compile(receiverListSkeleton);
	
	const check = new Check("msg");
	const msgModal = new MsgModal();
	const pagination = new Pagination();
	
	
	$("#btn-msg").on("click", function(){
		msgModal.openMsgModal(check, pagination.page, receiverListTemplate);
		msgModal.modal.find(".modal-footer .modal-send").show();
		msgModal.modal.find(".modal-header .modal-title").html("메시지 보내기");
		msgModal.sender.html(username);
	});

	$(".modal-send").on("click", function(){
		msgModal.sendMsg(check.list, "/board/user/msg");
	});
	
	check.collectCheckVal = function(){
		let msgNo = [];
		let user = [];
		
		if(this.allCheck.is(":checked")){
			const flag = "all-check";
			msgNo.push(flag);
			user.push(flag);
		}
		else{
			this.tbody.find($("input[type='checkbox']")).each(function(idx, c){
				const check = $(c);
				if(check.is(":checked") && check.val() !== "all-showed"){
					msgNo.push(check.val());
					// 보낸 이 이름
					user.push(check.parent().next().text());
				}
			});
		}
		return {msgNo, user};
	}
	
	check.flatToList = flatSetToList;
	check.flatObjToList = flatObjToList;

	function flatSetToList(obj){
		let data = new Set();
		for(let prop in obj){
			for(let user of obj[prop].user) data.add(user);
		}
		check.list = Array.from(data);
		return check.list;
	}
	
	function flatObjToList(obj){
		let data = [];
		for(let prop in obj){
			data = data.concat(obj[prop].msgNo);
		}
		check.list = data;
		return data;
	}
	
	check.tbody.on("click", "tr .title", function(e){
		const target = $(e.target);
		// 방법1. 클라이언트에서 체크 
		// 방법2. 서버에서 체크 
		// 한번에 처리 req || 두 번에 걸쳐 처리 req
		// 가장 비싼 처리는 무엇? 네트워크 -> 한번에 req -> 파라미터 추가
		let isRead = false;
		if(target.get(0).dataset.read === true) isRead = true; 
		ajax({
			url: "/board/user/msg/"+target.siblings("th").children("input").val()+"?isRead="+isRead,
			method: "get",
			
		}, function(res){
			msgModal.toggleModal();
			
			msgModal.title.val(res.title);
			msgModal.msg.val(res.content)
			msgModal.sender.html(res.sender);
			msgModal.receiver.html(res.receiver);
			
			msgModal.modal.find(".modal-footer .modal-send").hide();
			msgModal.modal.find(".modal-header .modal-title").html("받은 메시지");
			
			target.css("font-weight","inherit");
			target.attr("data-read", true);		
		})
	});
	
	
	$("#btn-del").on("click", function(e){
		check.appendCheckVal(pagination.page);
		const msgNoList = flatObjToList(check.repo, 'msgNo');
		ajax({
			url: "/board/user/msg/del",
			method: "post",
			data: JSON.stringify(msgNoList),
			contentType: "application/json; charset=utf-8"
		}, function(res){
			console.log(res);
			check.tbody.html(tableRowTemplate(res));
		});
	});
	
	function ajax(setting, callback){
		$.ajax({
			url: setting.url,
			method: setting.method,
			data: setting.data,
			dataType: "text",
			contentType: setting.contentType
		}).done(function(res){
			callback(JSON.parse(res));
		}).fail(showAjaxError);
		
	}
});