function checkServInitiator(keyword, url, context){
	const checkObj = {
			checkes: $("input[name='"+keyword+"']"),
			remBtn: $("#rem-btn"),
			context: context
	};
	addCheckEvtListener.call(checkObj.checkes);
	addRemBtnEvtListenr.call(checkObj.remBtn, url);
	return checkObj;
}
function updateTable(){
	const tableRow = template(this.context);
	$("tbody").html(tableRow);
}
function addCheckEvtListener(){
	checkes.on("click", function(){
		switch($(this).val()){
		case 'all':
			$("input[value='all-showed]").prop(":checked", false);
		case 'all-showed':
			if($(self).is("checked")) checkes.prop(":checked", true);
			else checkes.prop(":checked", false);
			$("input[value='all]").prop(":checked", false);
			return;
		default: 
			return;
		}
	})
}
function addRemBtnEvtListener(url){
	self.on("click", remBtnEvtHandler(this, url));
}
function remBtnEvtHandler(self, url){
	
	return function(url) {
		const data = collectCheckVal();
			
		$.ajax({
			url: url,
			type: "DELETE",
			data: data
		}).done(function(){
			updateTable();
		})
	}
}
function collectCheckVal(){
	let data = [];
	
	if(($("input[value='all]").is(":checked") === true) && (confirm("정말 모두 삭제하시겠습니가? 삭제 후 되돌릴 수 없습니다.")) === true) url += "?type=all";
	else{
		$("input[value='"+keyword+"']").each(function(r){
			if(r.is(":checked") && r.val() !== "all-showed") data.push(r.val());
		})
	}
	return data;
}
