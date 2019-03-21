
function Check(keyword) {
		this.tbody = $("tbody");
		this.checkes = this.tbody.find("input[name='"+keyword+"']");
		this.allCheck = $("input[value='all']");
		this.allShowedCheck = $("input[value='all-showed']");
		this.repo = {};
		this.list = [];
		
		this.allCheck.on("click", () => {
			this.allShowedCheck.prop("checked", false);
			this.checkes.prop("checked", false);
			this.resetRepo();
		});
		this.allShowedCheck.on("click", (e) => {
			if($(e.target).is(":checked")) this.checkes.prop("checked", true);
			else this.checkes.prop("checked", false);
			
			this.allCheck.prop("checked", false);
		});
		this.tbody.on("click", () => {
			this.allCheck.prop("checked", false);
		})
}
Check.prototype.collectCheckVal = collectCheckVal;
Check.prototype.appendCheckVal = appendChecked;
Check.prototype.flatToList = flatToList;
Check.prototype.resetRepo = resetRepo;
Check.prototype.isListEmpty = isListEmpty;

function isListEmpty(){
	return this.list.length === 0;
}
function collectCheckVal(){
	let data = [];
	if(this.allCheck.is(":checked")) data.push("all-check");
	else{
		this.tbody.find($("input[type='checkbox']")).each(function(idx, c){
			const check = $(c);
			if(check.is(":checked") && check.val() !== "all-showed") data.push(check.val());
		});
	}
	
	return data;
}
function appendChecked(prop){
	this.repo[prop] = this.collectCheckVal();
	return this.repo;
}
function flatToList(obj){
	let list = [];
	for(let prop in obj){
		list = list.concat(obj[prop]);
	}
	this.list = list;
	return this.list;
}
function resetRepo(){
	this.repo = {};
	this.list = [];
}

function Pagination(){
	this.ele= $(".pagination");
	this.pageEle = this.ele.find(".active");
	this.page = this.pageEle.children("a").attr("href");
	this.perPageNum = 10;
}
Pagination.prototype.makeQuery = makeQuery;
function makeQuery(addition){
	let query = "";
	for(let q in addition){
		query = "&"+q+"="+addition[q];
	}
	return "page="+this.page+"&perPageNum="+this.perPageNum+query;
}
function changePage(target){
	if(target === this.pageEle) return;
	this.pageEle.removeClass("active");
	
	target.addClass("active");
	this.page = target.find("a").attr("href");
	this.pageEle = target;
}

function Modal(){
	this.modal = $('#modal');
	this.title = this.modal.find(".title")
	this.msg = this.modal.find(".msg");
	this.receiver = this.modal.find(".receiver");
	this.sender = this.modal.find(".sender");
	
	this.modal.modal({
		keyboard: false,
		focus: false,
		show: false
	});
}
Modal.prototype.toggleModal = toggleModal;
function toggleModal(){
	this.msg.val("");
	this.modal.modal("toggle");
}

function showAjaxError(jqXHR, textStatus, errorThrown){
	console.error(jqXHR, textStatus, errorThrown);
}

function MsgModal(){
	Modal.call(this);
}
MsgModal.prototype = Object.create(Modal.prototype);
MsgModal.prototype.constructor = MsgModal;
MsgModal.prototype.openMsgModal = openMsgModal;
MsgModal.prototype.sendMsg = sendMsg;

function openMsgModal(check, page, template){

	// 선택된 회원 리스트 생성   
	check.appendCheckVal(page);
	const list = check.flatToList(check.repo);
	
	// 회원이 한 명도 선택되지 않았다면
	if(check.isListEmpty()){
		alert("회원을 1명 이상 선택해 주세요.");
		return;
	}
	
	// 메시지를 작성할 모달을 활성화
	this.toggleModal();
	
	let receiverList = "";

	// 모든 회원 선택이라면
	if(check.allCheck.is(":checked")){
		const userType = $("input[type='radio']:checked");
		if(userType.length !== 0) receiverList = userType.get(0).nextElementSibling.innerText+" 회원";
		else receiverType = "전체 회원";
	}
	else{
		const checkedCnt = list.length;
		receiverList = template({
			start: 1,
			end: checkedCnt>10 ? 10 : checkedCnt,
			showMsg: checkedCnt>10,
			receiverNum: checkedCnt,
			receiver: list
		});
	}
	
	// 메시지 모달에 수신자 목록 생성
	this.receiver.html(receiverList);
}
function sendMsg(receivers, url){
	const envelope = {
		receivers: receivers,
		title: this.title.val(),
		content: this.msg.val()
	};
	const self = this;
	$.post({
		url: url,
		data: JSON.stringify(envelope),
		contentType: "application/json; charset=utf-8"
			
		}).done(function(){
			// 메시지 전송 후 모달 닫기
			self.toggleModal();
			alert("메시지가 성공적으로 발송되었습니다.");
		
		}).fail(showAjaxError);
}

function addHandlebarHelper(){
	Handlebars.registerHelper("dateFormat", function(date){
		if(date === null) return;
		return date.year+"-"+date.monthValue+"-"+date.dayOfMonth+" "+date.hour+":"+date.minute;
	});
	Handlebars.registerHelper("for", function(start, end, block){
		let acc = "";
		for(let i=start; i<=end; i++){
			acc += block.fn(i);
		}
		return acc;
	});
	Handlebars.registerHelper("forArr", function(start, end, item, block){
		let acc = "";
		for(let i=start; i<=end; i++){
			acc += block.fn(item[i-1]);
		}
		return acc;
	});
}
