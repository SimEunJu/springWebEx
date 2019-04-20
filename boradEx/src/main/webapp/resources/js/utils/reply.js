const replyService = (function(){

	function add(reply, callback, error){
		$.ajax({
			type: 'post',
			url: '/board/daily/'+reply.bno+"/reply?writer="+reply.writer,
			data: JSON.stringify(reply),
			contentType: 'application/json; charset=utf-8',
			success: function(result, status, xhr){
				if(callback) callback(result);
			},
			error: function(xhr, status, err){
				if(error) error(err);
			}
		})
	}
	function getList(reply, callback, error){
		const bno = reply.bno;
		const page = reply.page || 1;
		$.getJSON(`/board/daily/${bno}/reply/${page}`, function(data){
			if(callback) callback(data);
		}).fail(function(xhr, status, err){
			if(error) error(err);
		});
	}
	
	function remove(param, callback, error){
		$.ajax({
			type: 'delete',
			url: `/board/daily/${param.bno}/reply/${param.rno}`,
			data: {replyer: param.replyer},
			success: function(result, status, xhr){
				if(callback) callback(result);
			},
			error: function(xhr, status, err){
				if(error) error(err);
			}
		})
	}
	
	function removeAnoymous(param, callback, error){
		$.post(`/board/daily/${param.bno}/reply/${param.rno}/pw`, {pw: param.pw})
		.done(function(result, status, xhr){
			if(callback) callback(result);
		})
		.fail(function(xhr, status, err){
			if(status == 401) alert("비밀번호가 다릅니다.");
			if(error) error(err);
		});
	}
	
	function update(reply, callback, error){
		$.ajax({
			type: 'put',
			url: `/board/daily/${reply.bno}/reply/${reply.rno}`,
			data: JSON.stringify(reply),
			contentType: 'application/json, charset=utf-8',
			success: function(result, status, xhr){
				if(callback) callback(result);
			},
			error: function(xhr, status, err){
				if(error) error(err);
			}
		})
	}
	function report(pathVal, callback, error){
		$.get(`/board/daily/${pathVal.bno}/reply/report/${pathVal.rno}`, function(result){
			if(callback) callback(result);
		}).fail(function(xhr, status, err){
			if(error) error(err);
		})
	}
	
	return {add: add,
		getList: getList,
		remove: remove,
		removeAnoymous: removeAnoymous,
		update: update,
		report: report}
})();