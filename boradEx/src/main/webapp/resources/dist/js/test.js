
$.getJSON(`/replies/${bno}/${page}`, function(data){
	let replyList = data.replies.reduce((acc, r) => 
		acc += `<li data-rno=${r.rno} class="replyLi">${r.rno} : ${r.replytext}</li>`,"");
	$("#replies").html(replyList);
});

$.ajax({
	type: 'post',
	url: '/replies',
	headers: {
		'Content-Type': 'application/json',
		'X-HTTP-Method-Override' : 'POST'
	},
	dataType : 'text',
	data : JSON.stringfy({
		bno: bno,
		replyer: replyer,
		replytext: replytext
	}),
	success: function(data){
		if(data === 'success') alert('success');
	}
});

