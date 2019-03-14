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
	            data: userJoinCnt,
	            yAxisId: "user-in",
	        },
	        {
	        	label: "유출",
	            fill: false,
	            borderColor: 'rgb(66, 134, 244)',
	            backgroundColor: 'rgb(66, 134, 244)',
	            data: userLeaveCnt,
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
	            data: visitCnt,
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
	            data: postCnt,
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
