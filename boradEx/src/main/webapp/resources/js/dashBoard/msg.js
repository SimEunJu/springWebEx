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
		msgModal.title.attr("readonly", false);
		msgModal.msg.attr("readonly", false);
		msgModal.modal.find(".modal-footer .modal-send").show();
		msgModal.modal.find(".modal-header .modal-title").html("메시지 보내기");
	});

	msgModal.userDefinedReceiver.on("keydown", function(e){
		if(e.originalEvent.key !== "Enter") return;
		msgModal.receiver.find("ul .user-defined-list").prepend(`<li>${this.value}<span class="pl-5">x</span></li>`);
		const receiverNumEle = $(this).parents("ul").find("li .receiver-num");
		if(receiverNumEle){
			const receiverNum = parseInt(receiverNumEle.text());
			receiverNumEle.text(receiverNum+1);
		}
	});
	
	msgModal.receiver.on("click", ".user-defined-list span", function(){
		$(this).parent().remove();
		const receiverNumEle = $(this).parents("ul").find("li .receiver-num");
		if(receiverNumEle){
			const receiverNum = parseInt(receiverNumEle.text());
			receiverNumEle.text(receiverNum-1);
		}
	});
	
	$(".modal-send").on("click", function(){
		msgModal.receiver.find(".user-defined-list li").each((idx, r) => check.list.push(r.innerText.replace(/x$/,"")));
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
			msgModal.title.attr("readonly", true);
			msgModal.msg.val(res.content);
			msgModal.msg.attr("readonly", true);
			msgModal.sender.html(res.sender);
			msgModal.receiver.html(res.receiver);
			
			msgModal.modal.find(".modal-footer .modal-send").hide();
			msgModal.modal.find(".modal-header .modal-title").html("받은 메시지");
			
			target.css("font-weight","inherit");
			target.attr("data-read", true);		
			
			longPollObj.initialize("msg");
		})
	});
	
	
	$("#btn-del").on("click", function(e){
		check.appendCheckVal(pagination.page);
		const msgNoList = flatObjToList(check.repo, 'msgNo');
		$.ajax({
			url: "/board/user/msg/del",
			method: "post",
			data: JSON.stringify(msgNoList),
			contentType: "application/json; charset=utf-8"
		}).then(function(res){
			longPollObj.initialize("msg");
			const user = window.location.pathname.match(/user|admin/)[0];
			window.location.href = `/board/${user}/msg?`+pagination.makeQuery(1);
		});
	});
	
	$("nav .pagination").on("click", "li", function(e){
		e.preventDefault();
		const page = e.target.getAttribute("href");
		console.log(page);
		const user = window.location.pathname.match(/user|admin/)[0];
		window.location.href = `/board/${user}/msg?`+pagination.makeQuery(page);
	})
	
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