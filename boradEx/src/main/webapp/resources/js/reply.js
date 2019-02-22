const replyService = (function(){

	function add(reply, callback, error){
		$.ajax({
			type: 'post',
			url: '/board/daily/'+reply.bno+"/reply",
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
	function getList(param, callback, error){
		const bno = param.bno;
		const page = param.page || 1;
		$.getJSON(`/board/daily/${bno}/reply/${page}`, function(data){
			if(callback) callback(data);
		}).fail(function(xhr, status, err){
			if(error) error(err);
		});
	}
	
	function remove({param}, callback, error){
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
	function get(rno, callback, error){
		$.get(`/board/daily/${bno}/reply/${rno}`, function(result){
			if(callback) callback(result);
		}).fail(function(xhr, status, err){
			if(error) error(err);
		})
	}
	function displayTime(timeVal){
		
		var today = new Date();
		var gap = today.getTime() - timeVal;
		var dateObj = new Date(timeVal);
		var str = "";
		var day = 1000*60*60*24;
		if(gap < day){
			var hh = dateObj.getHours();
			var mi = dateObj.getMinutes();
			var ss = dateObj.getSeconds();
			return [(hh>9? '' : '0')+hh, ':',(mi>9? '' : '0')+mi, ':',(ss>9? '' : '0')+ss].join('');
		}else{
			var yy = dateObj.getFullYear();
			var mm = dateObj.getMonth()+1;
			var dd = dateObj.getDate();
			return [yy, '/',(mm>9? '' : '0')+mm, '/',(dd>9? '' : '0')+dd].join('');
		}
	}
	
	return {add: add,
		getList: getList,
		remove: remove,
		update: update,
		get: get,
		displayTime: displayTime}
})();