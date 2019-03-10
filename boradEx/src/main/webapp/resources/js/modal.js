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
Check.prototype.flatObjToList = flatObjToList;
Check.prototype.resetRepo = resetRepo;
Check.prototype.isListEmpty = isListEmpty;

function isListEmpty(){
	return this.list.length === 0;
}
function collectCheckVal(){
	let data = [];
	if(this.allCheck.is(":checked")) data.push($("input[type='radio']:checked").get(0).nextElementSibling.innerText);
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
function flatObjToList(obj){
	for(let prop in obj){
		this.list = this.list.concat(obj[prop]);
	}
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
