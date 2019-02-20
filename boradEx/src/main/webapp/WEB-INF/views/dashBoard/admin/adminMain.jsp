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
  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
    <span class="navbar-toggler-icon"></span>
  </button>

  <div class="collapse navbar-collapse" id="navbarSupportedContent">
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
    <div class="col">
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
    		<p>사용자 유입/유출 현황</p>
    		<div>기준 일주일/ 달/ 년/</div>
    		<canvas id="chart_user_inout" width="400" height="400"></canvas>
    		
    		</div>
    		
    	</div>
		<div class="row">
			<p>사용자 방문 현황</p>
			<div>일/ 월/ 년</div>
			<canvas id="chart_user_visit" width="400" height="400"></canvas>
		</div>
		<div class="row">
			<p>게시글 현황</p>
			
			<div class="col">
				<p>등록된 게시글 수</p>
				<div>일/ 월/ 년</div>
				<canvas id="chart_user_board" width="400" height="400"></canvas>
			</div>
			
			<div class="col">
				<p>인기 게시글</p>
				<div>viewcnt 이상</div>
				<canvas id="chart_user_hot" width="400" height="400"></canvas>
			</div>
			
		</div>
    </div>
  </div>
</div>

<footer>
</footer>

<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.4.0/Chart.min.js"></script>
<script>
var ctx = document.getElementById('chart_user_inout').getContext('2d');
var chart = new Chart(ctx, {
    type: 'line',

    data: {
        labels: ["January", "February", "March", "April", "May", "June", "July"],
        datasets: [{
            label: "유입",
            fill: false,
            borderColor: 'rgb(255, 99, 132)',
            backgroundColor: 'rgb(255, 99, 132)',
            data: [0, 10, 5, 2, 20, 30, 45],
            yAxisId: "user-in",
        },
        {
        	label: "유출",
            fill: false,
            borderColor: 'rgb(66, 134, 244)',
            backgroundColor: 'rgb(66, 134, 244)',
            data: [1, 11, 6, 3, 21, 31, 46],
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
</script>
</body>
</html>