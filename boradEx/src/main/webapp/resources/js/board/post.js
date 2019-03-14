$(document).ready(function(){
				
		const name = nameTestAuth;
		const isLogged = isLoggedTestAuth;
	
		// handlebar 정리
		const handlebarTemplate = {};
		
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
			$.getJSON("/board/daily/"+board.bno+"/attach", function(res){
				$(res).each(function(i, attach){
					let filePath = "";
					let isImg = false;
					
					if(attach.fileType.includes("img")){
						filePath = encodeURIComponent(attach.uploadPath+"/s_"+attach.uuid+"_"+attach.fileName);
						isImg = true;	
					}
					attach.filePath = filePath;
				});
				const str = uploadItemTemplate(res)
				$(".upload-result ul").html(str);
			});
		})();
	
		const replyObj = {
				listSec: $(".reply-list"),
				pagination: $(".pagination"), 
				
				delBtn: $(".reply-del"),
				addedBtn: $(".reply-added"),
				reportBtn: $(".reply-report"),
				
				form: $(".reply-form"),
				page: 1,
				addedForm: $(".reply-form").clone()
		};
		replyObj.addedForm.find("button").removeClass("reply-reg").addClass("added-reply-reg");
		
		showReplyList();
		
		function showReplyList(){
			replyService.getList({bno: board.bno, page: replyObj.page}, function(res){
				
				const {replies, pagination} = res;
				// handlebar 적용 위해 선처리
				for(let i=0, len=replies.length||0; i<len; i++){
					
					replies[i].isNormal = false;
					
					// 삭제된 메시지라면
					if(replies[i].deleteFlag){
						let deleteMsg = "";
						
						switch(replies[i].deleteType){
						case 'R':
							deleteMsg = "댓글 작성자가 삭제한 댓글입니다.";
							break;
						case 'B':
							deleteMsg = "게시글 작성자가 삭제한 댓글입니다.";
							break;
						case 'A':
							deleteMsg = "관리자가 삭제한 댓글입니다.";
							break;
						default:
							deleteMsg = "삭제된 댓글입니다.";
							break;
						}
						replies[i].deleteMsg = deleteMsg;
					}
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
		
		function showReplyPagination(pagination){
			const str = paginationTemplate(pagination);
			replyObj.pagination.html(str);
		}
		
		replyObj.pagination.on("click", "li a", function(e){
			e.preventDefault();
			const targetPage = $(this).attr("href");
			replyObj.page = targetPage;
			showReplyList(replyObj.page);
		});
		
		replyObj.listSec.on("click", ".reply-del", handleReplyDelEvt);

		function handleReplyDelEvt(){
			if(confirm("정말 삭제하시겠습니다?")){
				const rno = $(this).parents(".reply").data("rno");
				// 해당 댓글 삭제하고
				replyService.remove({rno : rno, bno: board.bno}, 
					function(){
						// 댓글 목록 갱신
						showReplyList(replyObj.page);
				});
			}
		}
		
		replyObj.listSec.on("click", ".reply-report", function(e){
			if(confirm("정말 신고하시겠습니까? 허위 신고는 올바른 행위가 아닙니다.")){
				const rno = $(this).parents(".reply").data("rno");
				replyService.report({bno: board.bno, rno: rno}, function(){
					alert("신고가 접수되었습니다.")
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
				console.log(replyObj);
				addedFormSec.append(replyObj.addedForm);
				// 대댓글 목록이 이미 열려 있는 상태라면
				if(reply.get(0).dataset.open === "true") return;
			}
			// 대댓글 영역에 입력란이 있다면
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
			const addedReplyNum = parseInt(/\d+/.exec(reply.find("a").text())[0]);
		
			if(addedReplyNum === 0) return;
			
			const addedSec = reply.find(".added-replies");
			const reqPage = parseInt(addedSec.get(0).dataset.page);
			
			// 대댓글 가져오기
			$.getJSON('/board/daily/'+board.bno+'/reply/added/'+parRno+"/"+reqPage, function(replies){
				
				let replyHbData = {replies: replies};
				
				// 첫 페이지라면 버튼을 보여줘야 하기 때문
				if(reqPage === 1) replyHbData.isFirstPage = true;
				else replyHbData.isFirstPage = false;
				
				// 더보기 버튼 유무
				if(reqPage*10 >= addedReplyNum) replyHbData.displayMore = "none";
				else replyBbData.displayMore = "";
				
				// handlebar 적용
				const str = addedReplyTemplate(replyHbData);
				addedSec.append(str);
			});
		}

		function addReply(form){
			// 필요 사항 객체 생성
			let required = {
					reply: form.find("textarea[name='reply']").val(),
					replyer: form.find("input[name='replyer']").val(),
					writer: board.writer,
					bno: board.no,
			}
			if(isLogged && !checkInputVal(required)){
				alert("빈 칸을 채워주세요.");
				return;
			}
			else if(!isLogged){
				required.password = form.find("input[name='password']").val();
				if(!checkInputVal(required)){
					alert("빈 칸을 채워주세요.");
					return;
				}
			}
		
			required.parRno = form.data("parrno");
			required.secret = form.is(":checked");
			
			// 등록
			replyService.add(required, function(res){
				form.find("textarea").val("");
				form.find("input").val("");
				form.data("parrno","");
				showReplyList(1);
			});
		}
	
		function checkInputVal(inputList){
			for(let item in inputList){
				if(inputList[item]===null || inputList[item]===undefined || (''+inputList[item]).trim() === "") return false;
			}
			return true;
		}
	
		replyObj.listSec.on("click", ".reply-reg",function(){		
			addReply(replyObj.form);		
		});
		
		replyObj.listSec.on("click", ".added-reply-reg", function(e){
			e.stopPropagation();
			const reply = $(this).parents(".reply");
			const addedForm = $(this).find(".reply-form");
			addedForm.attr("data-parrno", reply.data("rno"));
			addReply(addedForm);
		});
		
		replyObj.listSec.on("click", ".added-reply-btns", function(e){
			const btn = $(e.target);
			const addedSec = btn.parents(".added-replies");
			const reply = addedSec.parents("li");
			
			if(btn.hasClass("fold")){
				addedSec.children().remove();
			
				reply.attr("data-open", "false");
				return;
			}
			else if(btn.hasClass("more")){
				getAddedList(reply);
			}			
		});
	
		
		function updateLike(like, likeCnt){
			if(!isLogged){ 
				alert("로그인이 필요합니다.");
				return;
			}
			$.ajax({
				method: "get",
				url: "/board/daily/"+board.bno+"/like",
				data: {
					bno: board.bno,
					likeCnt: likeCnt,
					username: encodeURIComponent(name)
					},
				success: function(){
					like.css("color", diff == 1 ? "red" : "black");
					var likeNum = like.find(".like-num");
					likeNum.text(parseInt(likeNum.text())+diff);
				}
			});
		}
		
		$(".like").on("click", function(){
			if($(this).css("color") === "red"){ 
				updateLike($(this), -1);
				return;
			}
			else{
				updateLike($(this), 1);
				return;
			}
		});
		
		$(".upload-result").on("click", "li", function(){
			var file = $(this);
			var path = encodeURIComponent(file.data("path")+"//"+file.data("uuid")+"_"+file.data("filename"));
			
			if(file.data("type")){
				showImage(path);
			}else {
				self.location = "/board/daily/file/download?fileName="+path;
			}
		});
		
		
		function showImage(path){
			$(".modal-body p").html("<img src='/board/daily/file?fileName="+path+"'>");
		}
	
		var formObj = $("form[role='form']");
		
		$("#boardModBtn").on("click",function(e){
			e.preventDefault();
			formObj.attr("action", "/board/daily/"+board.bno+"/mod");
			formObj.attr("method", "get");
			formObj.submit()
		});
		
		$("#boardRemBtn").on("click",function(e){
			e.preventDefault();
			var replyCnt = $(".chat li").length;
			console.log(replyCnt);
			if(replyCnt > 0){
				alert("댓글이 있는 게시물은 삭제할 수 없습니다.");
				return;
			}
			var files = fileService.getFilesInfo();
		
			if(files !== ""){
				$.post("/board/delete", 
						{files : files, name: name});
			}
			
			formObj.attr("action", "/board/daily/"+board.bno);
			formObj.attr("method", "DELETE");
			formObj.submit();
		});
		
		$("#boardAllBtn").on("click",function(e){
			e.preventDefault();
			formObj.attr("action", "/board/daily");
			formObj.attr("method", "get");
			formObj.submit();
		});
	});