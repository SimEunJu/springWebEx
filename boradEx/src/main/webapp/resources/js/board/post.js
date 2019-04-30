$(document).ready(function(){
				
		const name = nameTestAuth;
		const isLogged = isLoggedTestAuth;
		
		// handlebars template
		const replySkeleton = document.getElementById("reply-hb").innerHTML;
		const replyTemplate = Handlebars.compile(replySkeleton);
		
		const addedReplySkeleton = document.getElementById("added-reply-hb").innerHTML;
		const addedReplyTemplate = Handlebars.compile(addedReplySkeleton);
		
		const uploadItemSkeleton = document.getElementById("upload-item").innerHTML;
		const uploadItemTemplate = Handlebars.compile(uploadItemSkeleton);
		
		const paginationSkeleton = document.getElementById("pagination-hb").innerHTML;
		const paginationTemplate = Handlebars.compile(paginationSkeleton);
		
		// 첨부파일(이미지 등) 불러오기
		(function(){
			$.getJSON("/api/board/"+board.bno+"/attach", function(res){
				$(res).each(function(i, attach){
					let filePath = "";
					let isImg = false;
					
					if(attach.fileType.includes("image")){
						filePath = encodeURIComponent(attach.uploadPath+"/s_"+attach.uuid+"_"+attach.fileName);
						isImg = true;	
					}
					attach.isImg = isImg;
					attach.filePath = filePath;
				});
				
				const str = uploadItemTemplate(res)
				$(".upload-result ul").html(str);
			});
		})();
	
		const replyObj = {
				replySec: $(".card"),
				delPopover: $(".card #del-popover"),
				listSec: $(".card .reply-list"),
				pagination: $(".card .pagination"), 
				
				delBtn: $(".reply-del"),
				addedBtn: $(".reply-added"),
				reportBtn: $(".reply-report"),
				
				page: 1,
				form: $(".reply-form"),
				addedForm: $(".reply-form").clone()
		};
		
		// reply 관련 초기화, 불러오기 
		(function(){
			// 대댓글 입력용 form
			replyObj.addedForm.find("button").removeClass("reply-reg").addClass("added-reply-reg");
			// 댓글 불러오기
			showReplyList();
		})();
		
		function showReplyList(page){
			replyService.getList({bno: board.bno, page: page||1}, function(res){
				
				const {replies, pagination} = res;
				// handlebar 적용 위해 선처리
				for(let i=0, len=replies.length||0; i<len; i++){
					
					replies[i].isNormal = false;
					
					// 삭제된 메시지라면
					if(replies[i].deleteFlag) replies[i].deleteMsg = getDeleteMsg(replies[i].deleteType);
					else if(replies[i].secret === false){
						replies[i].isNormal = true;
					}
				};
				// 문자열 생성해 적용
				const str = replyTemplate(replies);
				replyObj.listSec.html(str);
				
				// 댓글 페이징 -> 개선 필요
				showReplyPagination(pagination);
			});
		}
		function getDeleteMsg(deleteType){
			switch(deleteType){
			case 'R':
				return "댓글 작성자가 삭제한 댓글입니다.";
			case 'B':
				return "게시글 작성자가 삭제한 댓글입니다.";
			case 'A':
				return "관리자가 삭제한 댓글입니다.";
			default:
				return "삭제된 댓글입니다.";
			}
		}
		
		function showReplyPagination(pagination){
			const str = paginationTemplate(pagination);
			replyObj.pagination.html(str);
		}
		
		replyObj.pagination.on("click", "li a", function(e){
			e.preventDefault();
			const targetPage = $(this).attr("href");
			showReplyList(targetPage);
			
			replyObj.page = targetPage;
		});
		
		// 댓글 삭제 popover
		replyObj.delPopover.popover({
			selector: ".reply-del",
			html: true,
			trigger: "click"
		});
		
		replyObj.listSec.on("click", ".reply-del", handleReplyDelEvt);
		
		function handleReplyDelEvt(e){
			
			const target = $(e.target);
			
			const reply = target.parents(".reply");
			const rno = reply.data("rno");
			const replyer = reply.find(".header .replyer").text();
			
			// 비밀번호 입력 popover trigger
			if(replyer === "익명") return;
			
			// 비밀번호 입력 popover trigger x
			e.stopImmediatePropagation();
			if(confirm("정말 삭제하시겠습니까?")){
				replyService.remove({rno : rno, bno: board.bno}, 
					function(){
					// 댓글 목록 갱신
					showReplyList(replyObj.page);
				});
			}
		}
		
		replyObj.listSec.on("click", ".replyer", function(e){
			if(this.innerHTML === "익명"){
				// 익명인 경우 신고 할 수 없기 때문에 신고 popover trigger x
				e.stopImmediatePropagation();
			}
		});
		
		// 댓글 작성자에 대한 신고 popover
		replyObj.listSec.popover({selector: ".replyer"});
		
		// popover evt handler
		$("body").on("click", ".popover", function(e){
			
			const target = e.target;
			let targetClassName = "";
			
			// 익명 댓글 삭제 비밀번호 popover
			if(target.tagName === "BUTTON"){
				// 해당 popover과 연결된 버튼 element를 찾아낸다
				const targetBtn = replyObj.listSec.find(".reply-del").filter((idx,r) => 
					{ return r.getAttribute("aria-describedby") === this.getAttribute("id")}
				);
				
				const pw = target.previousElementSibling.value;
				const parEle = targetBtn.parents("li");
				const rno = parEle.data("rno");
				
				replyService.removeAnoymous({
					rno: rno, bno: board.bno, pw: pw
				}, () => {
					const popover = $(e.currentTarget);
					popover.remove();
					showReplyList(replyObj.page);
				});
			
			// 댓글 작성자 신고 popover의 경우
			}else if(target.tagName === "SPAN"){
				const targetReplyer = replyObj.listSec.find(".replyer").filter((idx,r) => 
					{ return r.getAttribute("aria-describedby") === this.getAttribute("id")}
				);
				
				// 이전에 신고했다면 다시 신고 못하게
				if(targetReplyer.get(0).dataset.report) return;
				targetReplyer.get(0).dataset.report = true;
				
				const rno = targetReplyer.parents("li").data("rno");
				report("/board/user/report", {
					username: targetReplyer.text(),
					rno : rno,
					diff : 1
				});
			}		
		});
		
		function report(url, data){
			if(isLogged == false){
				alert("로그인 해주세요.");
				return;
			}
			$.ajax(url,{
				method: "post",
				data: data,
			}).done(function(){
				alert("신고가 완료되었습니다.")
			}).fail(function(jqXHR, textStatus, errorThrown){
				alert("신고에 실패하였습니다.")
				console.error(jqXHR, textStatus, errorThrown);
			});
		}
		
		// 댓글 신고
		replyObj.listSec.on("click", ".reply-report", function(e){
			if(isLogged === false){
				alert("로그인 해주세요.");
				return;
			}
			
			if(this.dataset.report === "true") return;
			
			this.dataset.report = true;
			
			if(confirm("정말 신고하시겠습니까? 허위 신고는 올바른 행위가 아닙니다.")){
				const rno = $(this).parents(".reply").data("rno");
				replyService.report({bno: board.bno, rno: rno}, function(){
					alert("신고가 접수되었습니다.");
				}, function(){
					alert("신고에 실패하였습니다.");
				});
			}
		});
		
		// 대댓글 입력 없이 대댓글만 보고 싶을 때
		replyObj.listSec.on("click", ".added-cnt", function(e){
			e.preventDefault();
			toggleAddedReplySec($(this).parents(".reply"));
		});
		
		// 대댓글 입력 + 대댓글 목록
		replyObj.listSec.on("click", ".reply-added", handleAddedBtnClkEvt);
		
		function handleAddedBtnClkEvt(){
			const reply = $(this).parents(".reply");
			const addedFormSec = reply.find(".added-form-sec");
			
			// 대댓글 영역에 입력란이 없다면
			if(addedFormSec.find(".reply-form").length === 0){
				// 입력란 추가
				addedFormSec.append(replyObj.addedForm);
				// 대댓글 목록이 이미 열려 있는 상태라면
				if(reply.get(0).dataset.open === "true") return;
			}
			
			// 대댓글 목록이 닫혀 있다면  
			toggleAddedReplySec(reply);
		}
		
		function toggleAddedReplySec(reply){
			if(reply.get(0).dataset.open === "true") {
				// 대댓글 영역이 열여 있다면 자식 모두 삭제(대댓글, 입력란)
				reply.find(".added-form-sec").children().remove();
				reply.find(".added-replies").children().remove();
				reply.attr("data-open","false")
				return false;
			}
			reply.attr("data-open","true");
			getAddedList(reply);
			return true;
		}
		
		function getAddedList(reply){
			// 필요 정보 추출
			const parRno = reply.data("rno");
			// 숫자만 추출
			const regex = new RegExp(/\d+/);
			const addedReplyNum = parseInt(/\d+/.exec(reply.find(".added-cnt").text())[0]);
			// 대댓글이 없다면 종료
			if(addedReplyNum === 0) return;
			
			const addedSec = reply.find(".added-replies");
			const reqPage = parseInt(addedSec.get(0).dataset.page);
			
			// 대댓글 가져오기
			$.getJSON(`/api/reply/${board.bno}/${parRno}/added?page=${reqPage}`, function(replies){
				const replyHbData = {replies: replies};
				
				// 첫 페이지라면 더보기/접기 버튼을 보여줘야 하기 때문
				if(reqPage === 1) replyHbData.isFirstPage = true;
				else replyHbData.isFirstPage = false;
				
				// 더보기 버튼 유무
				if(reqPage*10 >= addedReplyNum) replyHbData.displayMore = "none";
				else replyBbData.displayMore = "";
				
				for(let i=0; i<replies.length; i++){
					if(replies[i].deleteFlag) replies[i].reply = getDeleteMsg(replies[i].deleteType);
				}
				// handlebars 적용
				const str = addedReplyTemplate(replyHbData);
				addedSec.append(str);
			});
		}

		function addReply(form){
			// 필수 사항 값 체크 위한 객체 생성
			let required = {
					reply: form.find("textarea[name='reply']").get(0).value,
					replyer: form.find("input[name='replyer']").get(0).value,
					writer: board.writer,
					bno: board.bno,
			}
			
			// 입력값 체크 
			if(isLogged && !checkInputVal(required)){
				alert("빈 칸을 채워주세요.");
				return;
			}
			else if(!isLogged){
				required.password = form.find("input[name='password']").get(0).value;
				if(!checkInputVal(required)){
					alert("빈 칸을 채워주세요.");
					return;
				}
			}
		
			// 부가 정보 추가
			required.parRno = form.data("parrno");
			required.secret = form.is(":checked");
			
			// 등록
			replyService.add(required, function(res){
				form.find("textarea").val("");
				form.find("input").val("");
				form.data("parrno","");
				// 대댓글 추가 후 추가된 화면 유지 해야
				showReplyList(1);
			});
		}
	
		function checkInputVal(inputList){
			for(let item in inputList){
				if(inputList[item]===null || inputList[item]===undefined || (''+inputList[item]).trim() === "") return false;
			}
			return true;
		}
		
		// 댓글 추가
		replyObj.replySec.on("click", ".reply-reg",function(){		
			addReply(replyObj.form);		
		});
		
		// 대댓글 추가
		replyObj.listSec.on("click", ".added-reply-reg", function(e){
			//e.stopPropagation();
			const reply = $(e.target).parents(".reply");
			const addedForm = reply.find(".reply-form");
			addedForm.attr("data-parrno", reply.data("rno"));
			addReply(addedForm);
		});
		
		// 대댓글 보기 버튼 클릭 시 
		replyObj.listSec.on("click", ".added-reply-btns", function(e){
			const btn = $(e.target);
			const addedSec = btn.parents(".added-replies");
			const reply = addedSec.parents("li");
			
			// 대댓글 닫기
			if(btn.hasClass("fold")){
				// 대댓글 영역 닫기
				addedSec.children().remove();
				reply.attr("data-open", "false");
			
				return;
			}
			// 대댓글 열기
			else if(btn.hasClass("more")){
				getAddedList(reply);
			}			
		});
	
		
		function updateLike(like, likeDiff){
			if(!isLogged){ 
				alert("로그인이 필요합니다.");
				return;
			}
			
			likeDiff = likeDiff > 0 ? 1 : -1;
			
			$.ajax({
				method: "get",
				url: `/api/board/${board.bno}/like?diff=${likeDiff}`,
			}).done(function(){
				const WHITE = "white";
				const RED = "#dc3545";
				
				let color = RED;
				let backgroundColor = WHITE;
				if(likeDiff == 1){
					// 색상 반전
					color = WHITE;
					backgroundColor = RED;
				}
				
				like.style.color = WHITE;
				like.style.backgroundColor = RED;
				
				const likeNum = like.querySelector(".like-num");
				likeNum.innterText = parseInt(likeNum.text())+likeDiff;
			}).fail(function(){
				log.error("좋아요 수 업데이트에 실패했습니다.");
			})
		}
		
		$(".board-like").on("click", function(){
			// 좋아요 중복 방지
			let setLike = null;
			let diff = null;
			if(this.dataset.like === "true"){
				setLike = false;
				diff = -1;
			}
			else{
				setLike = true;
				diff = 1;
			}
			this.dataset.like = setLike;
			updateLike(this, diff);
		});
		
		$(".board-report").on("click", function(){
			// 중복 신고 방지 
			// 서버에서 한 번 더 검증할 필요가 있을까?
			if(this.dataset.report === "true") return;
			this.dataset.report = true;
			report("/api/board/"+board.bno+"/report", {diff: 1});
		})
		
		$(".upload-result").on("click", "li", function(){
			const file = $(this);
			const path = encodeURIComponent(file.data("path")+"/"+file.data("uuid")+"_"+file.data("filename"));
			
			if(file.data("type").includes("image")){
				window.location.href = `/board/daily/file?fileName=${path}`;
				// showImage(path);
			}else {
				window.location.href = `/board/daily/file/download?fileName=${path}`
			}
		});
		

		// 모달을 통해 이미지 보여줄 때
		function showImage(path){

		}
	
		const formObj = $("form[role='form']");
		
		// search parameter 유지
		$("#boardModBtn").on("click",function(e){
			e.preventDefault();
			boardModAndDel(`/api/board/${board.bno}/mod`, "/board/daily/"+board.bno+"/mod"+window.location.search);
		});
		$("#boardRemBtn").on("click",function(e){
			e.preventDefault();
			boardModAndDel(`/api/board/${board.bno}/rem`, "/board/daily"+window.location.search);
		});
		
		function boardModAndDel(url, successUrl){
			let pw = null;
			if(isLogged === false){
				// 비밀번호를 입력하니까 안 보이게 해야 하지 않을까?
				pw = prompt("비밀번호를 입력해주세요");
				if(pw === null) return;
			}
			$.post(url, {password: pw})
			.fail(function(jqXHR, textStatus, errorThrown){
				if(jqXHR.status === 401) alert("잘못된 비밀번호입니다.");
			})
			.done(function(){
				window.location.href=successUrl;
			})
		}
		
		$("#boardAllBtn").on("click",function(e){
			e.preventDefault();
			formObj.attr("action", "/board/daily"+window.location.search);
			formObj.attr("method", "get");
			formObj.submit();
		});
	
	});