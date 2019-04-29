const replyService = (function(){

	function add(reply, callback, error){
		$.ajax({
			type: 'post',
			url: `/api/reply/${reply.bno}`,
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
		$.getJSON(`/api/reply/${reply.bno}?page=${reply.page||1}`, function(data){
			if(callback) callback(data);
		}).fail(function(xhr, status, err){
			if(error) error(err);
		});
	}
	
	function remove(reply, callback, error){
		$.ajax({
			type: 'delete',
			url: `/api/reply/${reply.bno}/${reply.rno}`,
			data: {replyer: param.replyer},
			success: function(result, status, xhr){
				if(callback) callback(result);
			},
			error: function(xhr, status, err){
				if(error) error(err);
			}
		})
	}
	
	function removeAnoymous(reply, callback, error){
		$.post(`/board/daily/${reply.bno}/${reply.rno}/pw`, {pw: reply.pw})
		.done(function(result, status, xhr){
			if(callback) callback(result);
		})
		.fail(function(xhr, status, err){
			if(xhr.status === 401) alert("비밀번호가 다릅니다.");
			if(error) error(err);
		});
	}
	
	function report(reply, callback, error){
		$.get(`/api/reply/${reply.bno}/${reply.rno}/report`, function(result){
			if(callback) callback(result);
		}).fail(function(xhr, status, err){
			if(error) error(err);
		})
	}
	
	return {add: add,
		getList: getList,
		remove: remove,
		removeAnoymous: removeAnoymous,
		report: report}
})();