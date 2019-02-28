<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>관리자 메인페이지</title>
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
        <a class="nav-link" href="/board/login">로그인/로그아웃</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="/board/admin/info">내 정보</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="/board/admin/noti">알림</a>
      </li>
    </ul>
  </div>
</nav>

<div class="container">
  <div class="row">
    <div class="col-12 col-md-3">
		<ul class="list-group">
			<a href="/board/admin/noti"><li class="list-group-item">알림들</li></a>
			<a href="/board/admin/msg"><li class="list-group-item">메시지함</li></a>
			<a href="/board/admin/user"><li class="list-group-item">회원관리</li></a>
			<a href="/board/admin/post"><li class="list-group-item">내가 쓴 글들</li></a>
			<a href="/board/admin/reply"><li class="list-group-item">내가 쓴 댓글들</li></a>
			<a href="/board/admin/info"><li class="list-group-item">관리자 정보</li></a>
		</ul>
    </div>
    
    <div class="col">
    	<div class="row">
    		<div>
    		<p>사용자 유입/유출 현황</p>
    		<canvas id="chart_user_inout" width="400" height="400"></canvas>
    		<div class="range">
    			<input type="radio" name="inout-cri" value="d" checked>일</input>
    			<input type="radio" name="inout-cri" value="w">주</input>
    			<input type="radio" name="inout-cri" value="m">월</input>
    		</div>
    		</div>
    		
    		<div>
    		<p>사용자 방문 현황</p>
			
			<canvas id="chart_user_visit" width="400" height="400"></canvas>
			<div class="range">
    			<input type="radio" name="visit-cri" value="d" checked>일</input>
    			<input type="radio" name="visit-cri" value="w">주</input>
    			<input type="radio" name="visit-cri" value="m">월</input>
    		</div>
    		</div>
    	</div>

		<div class="row">
			<p>게시글 현황</p>
			
			<div class="col">
				<p>등록된 게시글 수</p>
				
				<canvas id="chart_user_board" width="400" height="400"></canvas>
				<div class="range">
    			<input type="radio" name="board-cri" value="d" checked>일</input>
    			<input type="radio" name="board-cri" value="w">주</input>
    			<input type="radio" name="board-cri" value="m">월</input>
    		</div>
			</div>
			
			<div class="col">
				<p>인기 게시글</p>
				<table class="table">
  					<thead class="thead-dark">
    					<tr>
      						<th scope="col">#</th>
      						<th scope="col">제목</th>
      						<th scope="col">글쓴이</th>
      						<th scope="col">조회수</th>
      						<th scope="col">날짜</th>
    					</tr>
  					</thead>
  					<tbody>
  						<c:forEach var="post" items="${hotPost}">
    					<tr>
      						<th scope="row">${post.bno}</th>
      						<td>${post.title}</td>
      						<td><a href="/board/daily/${post.bno}">${post.writer}</a></td>
      						<td>${post.viewcnt}</td>
      						<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${post.regdate}"></fmt:formatDate></td>
    					</tr>
    					</c:forEach>
  					</tbody>
				</table>
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
$("document").ready(function(){
	const chart_user_inout_ctx = document.getElementById('chart_user_inout').getContext('2d');
	const chart_user_inout_config =	{
	    type: 'line',

	    data: {
	        labels: labeling('d'),
	        datasets: [{
	            label: "유입",
	            fill: false,
	            borderColor: 'rgb(255, 99, 132)',
	            backgroundColor: 'rgb(255, 99, 132)',
	            data: ${userJoinCnt},
	            yAxisId: "user-in",
	        },
	        {
	        	label: "유출",
	            fill: false,
	            borderColor: 'rgb(66, 134, 244)',
	            backgroundColor: 'rgb(66, 134, 244)',
	            data: ${userLeaveCnt},
	            yAxisId: "user-out",
	        }],
	    },

	    options: {
	    	title: {
	    		display: true,
	    		text: "사용자 유입/유출 현황"
	    	},
	    }
	};
	const chart_user_inout = new Chart(chart_user_inout_ctx, chart_user_inout_config);
	
	const chart_user_visit_ctx = document.getElementById('chart_user_visit').getContext('2d');
	const chart_user_visit_config = {
	    type: 'bar',
	    data: {
	        labels: labeling('d'),
	        datasets: [{
	            label: "방문 수",
	            borderWidth: 1,
	            borderColor: 'rgb(255, 99, 132)',
	            backgroundColor: 'rgb(255, 99, 132)',
	            data: [10,10,10,10,10,10],
	            yAxisId: "user-visit"
	        }],
	    },
	    options: {
	    	title: {
	    		display: true,
	    		text: "사용자 방문 수"
	    	},
	    }
	};
	const chart_user_visit = new Chart(chart_user_visit_ctx, chart_user_visit_config);

	const chart_user_board_ctx = document.getElementById('chart_user_board').getContext('2d');
	const chart_user_board_config = {
	    type: 'line',

	    data: {
	        labels: labeling('d'),
	        datasets: [{
	            label: "게시글 수",
	            fill: false,
	            borderColor: 'rgb(255, 99, 132)',
	            backgroundColor: 'rgb(255, 99, 132)',
	            data: ${postCnt},
	            yAxisId: "user-board",
	        }],
	    },
	    options: {
	    	title: {
	    		display: true,
	    		text: "사용자 게시글 수"
	    	},
	    }
	}
	const chart_user_board = new Chart(chart_user_board_ctx, chart_user_board_config);
	
	function ajax(url, config, target){
		$.getJSON(url, function(res){
			if(url.includes("inout")){
				config.data.datasets[0].data = res.join;
				config.data.datasets[1].data = res.leave;
				
			}else if(url.includes("board")){
				config.data.datasets.data = res.board;
			}
			const regexArr = /[dwm]$/.exec(url);
			const type = regexArr[0];
			config.data.labels = labeling(type);
			
			target.update();
		}).fail(function(xhr, textStatus, error){
			console.warn(error);
		})
	}
	
	$("input[type='radio']").on("click", function(e){
		const choice = $(e.target);
		const rootUrl = "/board/api/admin/";
		switch(choice.attr("name")){
		case "inout-cri":
			ajax(rootUrl+"inout?type="+choice.val(), chart_user_inout_config, chart_user_inout);
			break;
		case "visit-cri":
			ajax(rootUrl+"visit?type="+choice.val(), chart_user_visit_config, chart_user_visit);
			break;
		case "board-cri":
			ajax(rootUrl+"board?type="+choice.val(), chart_user_board_config, chart_user_board);
			break;
		default:
			console.warn("wrong cri choice", e.currentTarget);
		}
	})
	
	function labeling(type){
		
		const now = new Date();
		let today = new Date(now.getFullYear(), now.getMonth(), now.getDate()+1);
		let month = today.getMonth();
		let year = today.getFullYear();
		
		let labels = [];
		for(var i=0; i<6; i++) labels.push(null);
		let label = "";
		
		switch(type){
		case "d":
			{
			let day = today;
			
			let nMonth, nYear, nLabel;
			for(let i=5; i>=0; i--){
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
			}
			break;
		
		case "w":
			{
			const sunday = new Date(today.setDate(today.getDate()-today.getDay()+7));
			console.log(sunday);
			
			let label = "";
			let nthWeek;
			
			for(let i=5; i>=0; i--){
				sunday.setDate(sunday.getDate() - 7);
				
				nYear = sunday.getFullYear();
				if(year !== nYear){
					label += nYear+"년 ";
					year = nYear;
				}
				nMonth = sunday.getMonth();
				if(month !== nMonth){
					label += (nMonth+1) +"월 ";
					month = nMonth;
				}
				
				nthWeek = Math.floor(sunday.getDate()/7 + 1);
				
				labels[i] = label+nthWeek+"주";
				label = "";
			}
			}
			break;
		case "m":
			{
			let nYear;
			let label = "";
			
			for(let i=5; i>=0; i--){ 
				
				nYear = today.getFullYear();
				if(year !== nYear){
					label += nYear+"년 ";
					year = nYear;
				}
				today.setMonth(today.getMonth() - 1)
				label += (today.getMonth()+1) +"월";
				labels[i] = label;
				label = "";
			}
			}
			break;
		default: return []		
		}
		return labels;
	}

	const toggler = document.querySelector(".navbar-toggler");
	const toggleList = $("#navbar");
	toggler.addEventListener("click", function(){
		toggleList.toggleClass("show");
		const togglerVal = toggler.getAttribute("aria-expanded") === "true" ? "true" : "false";
		toggler.setAttribute("aria-expanded", togglerVal);
	})
})

</script>
</body>
</html>