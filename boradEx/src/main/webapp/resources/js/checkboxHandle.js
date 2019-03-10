
function checkServ(keyword, url){
	const checkObj = {
			checkes: $("input[name='"+keyword+"']"),
			remBtn: $("#rem-btn")
	};
	addCheckEvtListener(checkObj.checkes);
	addRemBtnEvtListener(checkObj.remBtn, url);
	return checkObj;
}
function updateTable(){
	const tableRow = template(this.context);
	$("tbody").html(tableRow);
}
function addCheckEvtListener(checkes){
	checkes.on("click", function(e){
		
		switch($(this).val()){
		case 'all':
			$("input[value='all-showed']").prop("checked", false);
			return;
		case 'all-showed':
			if($(this).is(":checked")) checkes.prop("checked", true);
			else checkes.prop("checked", false);
			$("input[value='all']").prop("checked", false);
			return;
		default: 
			return;
		}
	})
}
function addRemBtnEvtListener(remBtn, url){
	remBtn.on("click", remBtnEvtHandler(url));
}
function remBtnEvtHandler(url){
	
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
	
	if(($("input[value='all']").is(":checked") === true) && (confirm("모두 선택하시겠습니가?")) === true) url += "?type=all";
	else{
		$("input[type='checkbox']").each(function(idx, c){
			const check = $(c);
			if(check.is(":checked") && check.val() !== "all-showed") data.push(check.val());
		})
	}
	return data;
}
function appendChecked(repo, page){
	repo[page] = collectCheckVal();
	return repo;
}

function flatObjToList(obj){
	let list = [];
	for(let page in obj){
		list = list.concat(obj[page]);
	}
	return list;
}
