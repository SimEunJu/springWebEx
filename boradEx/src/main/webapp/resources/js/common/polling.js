
function longPoll(url, callback) {  
	let term = 10*60*10;
	$.ajax({
        url: url,
        type: 'GET',
        dataType: 'json',
        success: function() {
            term = callback()*60*10;
        },
        //timeout: 3000,
        complete: setTimeout(function() { longPoll(); }, term)
    });
}
const polled = {
		msg, noti
}
longPoll("/board/polling/msg", function(){
	const cookie = document.cookie.match(/msg=(\d+)z(\d+)z(\d+)/);
	const no = cookie[1];
	const cnt = cookie[2];
	const term = cookie[3];
	
	if(parseInt(cnt) > 999){
		msg.html("999+");
		return false;
	}
	msg.html(cnt);
	return term;
});
