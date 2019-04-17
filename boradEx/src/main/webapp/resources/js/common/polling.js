
function longPoll(url, callback) {  
	// 기본 간격 30초
	let term = 3*1000;
	$.ajax({
        url: url,
        type: 'GET',
        success: function() {
            term = callback()*1000;
            setTimeout(function() { longPoll(url, callback); }, term)
        },
    });
}

const poll = {
		msg: document.querySelector("nav .navbar-nav .msgbadge"),
		noti: document.querySelector("nav .navbar-nav .notibadge")
};

longPoll("/board/polling/msg", function(){
	console.log(document.cookie);
	const cookie = document.cookie.match(/msgPoll=(\d+)z(\d+)z(\d+)/);
	const no = cookie[1];
	const cnt = cookie[2];
	const term = cookie[3];
	
	if(parseInt(cnt) > 999){
		poll.msg.innerHTML = "999+";
		return false;
	}
	poll.msg.innerHTML = cnt;
	return term;
});

longPoll("/board/polling/noti", function(){
	console.log(document.cookie);
	const cookie = document.cookie.match(/notiPoll=(\d+)z(\d+)z(\d+)/);
	const no = cookie[1];
	const cnt = cookie[2];
	const term = cookie[3];
	
	if(parseInt(cnt) > 999){
		poll.noti.innerHTML = "999+";
		return false;
	}
	poll.noti.innerHTML = cnt;
	return term;
});
