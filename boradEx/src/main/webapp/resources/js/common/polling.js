
function longPoll(url, callback) {  
	// 기본 간격 10분
	let term = 1000*60*10;
	$.ajax({
        url: url,
        type: 'GET',
        success: function() {
            term = callback()*60*10;
        },
        //timeout: 3000,
        complete: setTimeout(function() { longPoll(url, callback); }, term)
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
		msg.html("999+");
		return false;
	}
	poll.msg.innerHTML = cnt;
	return term;
});
