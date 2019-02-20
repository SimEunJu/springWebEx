<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Admin</title>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" 
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-light bg-light">
  <a class="navbar-brand" href="/board/daily">Daily</a>
  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbar" aria-controls="navbar" aria-expanded="false" aria-label="Toggle navigation">
    <span class="navbar-toggler-icon"></span>
  </button>

  <div class="collapse navbar-collapse" id="navbar">
    <ul class="navbar-nav mr-auto">
      <li class="nav-item">
        <a class="nav-link" href="#">로그인/로그아웃</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="#">내 정보</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="#">알림</a>
      </li>
    </ul>
  </div>
</nav>

<div class="container">
  <div class="row">
    <div class="col-12 col-md-3">
		<ul class="list-group">
			<li class="list-group-item">알림들</li>
			<li class="list-group-item">사용자 문의사항</li>
			<li class="list-group-item">회원관리</li>
			<li class="list-group-item">내가 쓴 글들</li>
			<li class="list-group-item">내가 쓴 댓글들</li>
			<li class="list-group-item">관리자 정보</li>
		</ul>
    </div>
    
    <div class="col">
    	<div class="row">
    		<div>
    		<p>사용자 유입/유출 현황</p>
    		<div>기준 일주일(default)/ 달/ 년/</div>
    		<canvas id="chart_user_inout" width="400" height="400"></canvas>
    		<div class="range">
    			<input type="radio" name="cri" value="d">일</input>
    			<input type="radio" name="cri" value="w">주</input>
    			<input type="radio" name="cri" value="m">월</input>
    			<input type="radio" name="cri" value="y">년</input>
    		</div>
    		</div>
    		
    		<div>
    		<p>사용자 방문 현황</p>
			
			<canvas id="chart_user_visit" width="400" height="400"></canvas>
			<div class="range">
    			<input type="radio" name="cri" value="d">일</input>
    			<input type="radio" name="cri" value="w">주</input>
    			<input type="radio" name="cri" value="m">월</input>
    			<input type="radio" name="cri" value="y">년</input>
    		</div>
    		</div>
    	</div>

		<div class="row">
			<p>게시글 현황</p>
			
			<div class="col">
				<p>등록된 게시글 수</p>
				
				<canvas id="chart_user_board" width="400" height="400"></canvas>
				<div class="range">
    			<input type="radio" name="cri" value="d">일</input>
    			<input type="radio" name="cri" value="w">주</input>
    			<input type="radio" name="cri" value="m">월</input>
    			<input type="radio" name="cri" value="y">년</input>
    		</div>
			</div>
			
			<div class="col">
				<p>인기 게시글</p>
				<div>viewcnt 이상</div>
			</div>
			
		</div>
    </div>
  </div>
</div>

<footer>
</footer>
<script src="https://code.jquery.com/jquery-3.3.1.min.js"
	integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8=" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.4.0/Chart.min.js"></script>
<script>
var chart_user_inout_ctx = document.getElementById('chart_user_inout').getContext('2d');
var chart_user_inout = new Chart(chart_user_inout_ctx, {
    type: 'line',

    data: {
        labels: labeling('d'),
        datasets: [{
            label: "유입",
            fill: false,
            borderColor: 'rgb(255, 99, 132)',
            backgroundColor: 'rgb(255, 99, 132)',
            data: [10,11,12,13,14,15,...${userJoinCnt}],
            yAxisId: "user-in",
        },
        {
        	label: "유출",
            fill: false,
            borderColor: 'rgb(66, 134, 244)',
            backgroundColor: 'rgb(66, 134, 244)',
            data: [0,1,2,3,4,5,...${userLeaveCnt}],
            yAxisId: "user-out",
        }],
    },

    options: {
    	title: {
    		display: true,
    		text: "사용자 유입/유출 현황"
    	},
    }
});

var chart_user_visit_ctx = document.getElementById('chart_user_visit').getContext('2d');
var chart_user_visit = new Chart(chart_user_visit_ctx, {
    type: 'bar',

    data: [{
        labels: labeling('d'),
        datasets: {
            label: "방문 수",
            fill: false,
            borderColor: 'rgb(255, 99, 132)',
            backgroundColor: 'rgb(255, 99, 132)',
            data: [1,2,3,4,5,6,7],
            yAxisId: "user-visit",
        },
    }],

    options: {
    	title: {
    		display: true,
    		text: "사용자 방문 수"
    	},
    }
});

var chart_user_board_ctx = document.getElementById('chart_user_board').getContext('2d');
var chart_user_board = new Chart(chart_user_board_ctx, {
    type: 'line',

    data: [{
        labels: labeling('d'),
        datasets: {
            label: "게시글 수",
            fill: false,
            borderColor: 'rgb(255, 99, 132)',
            backgroundColor: 'rgb(255, 99, 132)',
            data: [1,2,3,4,5,6,7],
            yAxisId: "user-board",
        },
    }],

    options: {
    	title: {
    		display: true,
    		text: "사용자 게시글 수"
    	},
    }
});
// 1. 주기 선택에 따른 api 요청
// 2. label function

function labeling(type){
	
	var now = new Date();
	var today = new Date(now.getFullYear(), now.getMonth()-1, now.getDate());
	var month = today.getMonth();
	var year = today.getFullYear();
	
	var labels = [];
	for(var i=0; i<7; i++) labels.push(null);
	
	switch(type){
	case "d":
		
		var day = today;
		var label = "";
		var nMonth, nYear, nLabel;
		for(let i=6; i>=0; i--){
			day.setDate(day.getDate() - 1);
			
			nYear = day.getFullYear();
			if(year !== nYear){
				label += nYear+"년 ";
				year = nYear;
			}
			nMonth = day.getMonth();
			if(month !== nMonth){
				label += nMonth+"월 ";
				month = nMonth;
			}
			
			label += day.getDate()+"일";
			labels[i] = label;
			label = "";
		}
		
		break;
	
	case "w":
	
		var sunday = today.setDate(today.getDate()-today.getDay());
		
		var label = "";
		var nthWeek;
		
		for(let i=6; i>=0; i--){
			sunday.setDate(sunday.getDate() - (6-i)*7);
			
			nYear = sunday.getFullYear();
			if(year !== nYear){
				label += nYear+"년 ";
				year = nYear;
			}
			nMonth = sunday.getMonth();
			if(month !== nMonth){
				label += nMonth+"월 ";
				month = nMonth;
			}
			
			nthWeek = Math.floor(sunday.getDate()/7 + 1);
			
			labels[i] = label+nthWeek+"주";
			label = "";
			
		}
		break;
	case "m":
		
		var nYear;
		var label = "";
		
		for(let i=5; i>=0; i--){ 
			
			nYear = day.getFullYear();
			if(year !== nYear){
				label += nYear+"년 ";
				year = nYear;
			}
			
			label += today.setDate(today.getMonth() - (6-i)).getMonth()+"월";
			labels[i] = label;
			label = "";
		}
		break;
		
	case "y":
		for(let i=5; i>=0; i--) labels[i] = today.setDate(today.getFullYear()-1).getFullYear()+"년";
		break;
	default: return []		
	}
	return labels;
}

var toggler = document.querySelector(".navbar-toggler");
var toggleList = $("#navbar");
toggler.addEventListener("click", function(){
	toggleList.toggleClass("show");
	var togglerVal = toggler.getAttribute("aria-expanded") === "true" ? "true" : "false";
	toggler.setAttribute("aria-expanded", togglerVal);
})
</script>
</body>
</html>