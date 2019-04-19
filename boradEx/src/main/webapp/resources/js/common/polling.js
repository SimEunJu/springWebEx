const longPollObj = {
	timeoutId: null,
	updateEle: {
		msg: document.querySelector("nav .navbar-nav .msgbadge"),
		noti: document.querySelector("nav .navbar-nav .notibadge")
	},
	updateUrl: {
		msg: "/board/polling/msg",
		noti: "/board/polling/noti"
	},
	msgCallback: msgLongPollCallback,
	notiCallback: notiLongPollCallback,
	initialize: initialize,
}

longPoll(longPollObj.updateUrl.msg, msgLongPollCallback);
longPoll(longPollObj.updateUrl.noti, notiLongPollCallback);

function longPoll(url, callback) {  
	// 기본 간격 30초
	let term = 3*1000;
	$.ajax({
        url: url,
        type: 'GET',
        success: function() {
            term = callback()*1000;
            longPollObj.timeoutId = setTimeout(function() { longPoll(url, callback); }, term)
        },
    });
}

function msgLongPollCallback(){
	console.log(document.cookie);
	const cookie = document.cookie.match(/msgPoll=(\d+)z(\d+)z(\d+)/);
	const no = cookie[1];
	const cnt = cookie[2];
	const term = cookie[3];
	
	if(parseInt(cnt) > 999){
		longPollObj.updateEle.msg.innerHTML = "999+";
		return false;
	}
	longPollObj.updateEle.msg.innerHTML = cnt;
	return term;
}

function notiLongPollCallback(){
	console.log(document.cookie);
	const cookie = document.cookie.match(/notiPoll=(\d+)z(\d+)z(\d+)/);
	const no = cookie[1];
	const cnt = cookie[2];
	const term = cookie[3];
	
	if(parseInt(cnt) > 999){
		longPollObj.updateEle.noti.innerHTML = "999+";
		return false;
	}
	longPollObj.updateEle.noti.innerHTML = cnt;
	return term;
};

function initialize(type){
	
	if(longPollObj.timeoutID != null) window.clearTimeout(longPollObj.timeoutID);
	switch (type){
	case 'msg':
		document.cookie = "msgPoll=; expires=Wed 01 Jan 1970; path=/";
		longPoll(longPollObj.updateUrl.msg, msgLongPollCallback);
		break;
	case 'noti':
		document.cookie = "notiPoll=; expires=Wed 01 Jan 1970; path=/";
		longPoll(longPollObj.updateUrl.noti, notiLongPollCallback);
		break;
	default:
		log.error("정의되지 않은 long poll 타입입니다.");
	}
}