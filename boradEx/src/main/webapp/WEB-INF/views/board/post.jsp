<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ include file="../common/header.jsp"%>

<form role="form" method="post" >
	<input type="hidden" name="bno" value="${board.bno }">
	<input type="hidden" name="page" value="${cri.page }">
	<input type="hidden" name="perPageNum" value="${cri.perPageNum }">
	<input type="hidden" name="searchType" value="${cri.searchType }">
	<input type="hidden" name="keyword" value="${cri.keyword }">
</form>

<div class="container">
	<div>
		<div class="form-group">
			<label for="title">제목</label> 
			<input type="text" readonly="readonly" value="${board.title }" name="title" class="form-control">
		</div>
		<div class="form-group">
			<label for="content">내용</label>
			<textarea class="form-control" readonly="readonly" row="3" name="content">${board.content }</textarea>
		</div>
		<div class="form-group">
			<label class="wrtier">글쓴이</label> 
			<input type="text" readonly="readonly" value="${board.writer }" name="writer" class="form-control">
		</div>
	</div>

	<div>
		<sec:authorize access="isAuthenticated()">
			<sec:authentication property="principal" var="loginUser" />
			<c:if test="${loginUser.username eq board.writer}">
				<button type="submit" id="boardModBtn" class="btn btn-outline-warning">수정</button>
				<button type="submit" id="boardRemBtn" class="btn btn-outline-danger">삭제</button>
			</c:if>
		</sec:authorize>
		<button type="submit" id="boardAllBtn" class="btn btn-primary">목록으로</button>

		<button class="like" class="btn btn-primary btn-sm" style="color: ${isUserLiked ? 'red' : 'black'}">
			<span style="font-size: 20px; font-weight: bold;">♥</span> 좋아요 
			<span class="like-num">${board.userLike}</span>
		</button>
	</div>

	<div class="row">
		<div class="col-md-12">
			<div class="card">
				<div class="card-head">첨부파일</div>

				<div class="card-body">
					<div class="upload-result">
						<ul>

						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<div class="modal" tabindex="-1" role="dialog">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        
      </div>
    </div>
  </div>
</div>

<%@ include file="../reply/replyList.jsp" %>

<%@ include file="../common/footer.jsp"%>

<script type="text/javascript" src="/resources/js/reply.js"></script>

<script type="text/javascript" src="/resources/js/file.js"></script>

<script id="reply-hb" type="text/x-handlebars-template">
{{#each replies}}
<li class="left clearfix" data-rno="{{rno}}" data-secret="true">
	<div>
		{{#if isScret}}
			<p>비밀글입니다.</p>
			<small class="pull-right text-muted">{{#dateFormat regdate}}</small>
		{{else}}
			<div class="header">
				<strong class="primary-font">{{replyer}}</strong>
				<small class="pull-right text-muted">{{#dateFormat regdate}}</small>
			</div>
			<p>{{reply}}</p>
		{{/if}}
	</div>
</li>
{{/each}}
{{#if isFirstPage}}
	<div class="reply-btns">
		<button style="display: {{displayMore}}" class="more btn btn-primary btn-sm">더보기</button>
		<button class="fold btn btn-primary btn-sm pull-left">접기</button>
</div>
{{/if}}
</script>

<script id="upload-item" type="text/x-handlebars-template">
<li data-path='{{uploadPath}}' data-uuid='{{uuid}}' data-filename='{{fileName}}' data-type='{{fileType}}'>
	<div>
		<span>{{fileName}}</span>
		<br>
		{{#if isImg}}
			<img src='/board/daily/file?fileName={{filePath}}'>
		{{else}}
			<img src='/resources/img/attach.png'>
		{{/if}}
	</div>
</li>
</script>
<script id="pagination-hb" type="text/x-handlebars-template">
{{#if prev}}
	<li class="page-item"><a class="page-link" href="">&laquo;</a></li>
{{/if}}
{{#for startPage endPage}}
	<li class="page-item"><a class="page-link" href="{{this}}">{{this}}</a></li>
{{/for}}
{{#if next}}
	<li class="page-item"><a class="page-link" href="">&raquo;</a></li>
{{/if}}
</script>
<script type="text/javascript">
	$(document).ready(function(){
			
		const csrfHeader = "${_csrf.headerName}";
		const csrfTokenVal = "${_csrf.token}";
		
		/* $(document).ajaxSend(function(e, xhr, options){
			xhr.setRequestHeader(csrfHeader, csrfTokenVal);
		}); */
	
		const bno = "${boardVO.bno}";
		let nameTestAuth = "";
		let isLoggedTestAuth = false;
		
		<sec:authorize access="isAuthenticated()">
			nameTestAuth = "${loginUser.username}";
			isLoggedTestAuth = true;
		</sec:authorize>
		
		const name = nameTestAuth;
		const isLogged = isLoggedTestAuth;
		
		const replySkeleton = document.getElementById("reply-hb").innerHTML;
		const replyTemplate = Handlebars.compile(replySkeleton);
		
		const uploadItemSkeleton = document.getElementById("upload-item").innerHTML;
		const uploadItemTemplate = Handlebars.compile(uploadItemSkeleton);
		
		// 첨부파일(이미지 등) 불러오기
		(function(){
			$.getJSON("/board/daily/"+bno+"/attach", function(res){
				$(res).each(function(i, attach){
					let filePath;
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
		
		// 댓글 불러오기
		let pageNum = 1;
		const replyFooter = $(".card-footer");
		function showReplyPagination(){
			// ajax로 가져올때 pagination 정보도 같이 가져와야
			replyFooter.html(str);
		}
		
		const replyUl = $(".chat");
		
		showList(1);
		function showList(page){
			replyService.getList({bno: bno, page: page||1}, function(res){
				
				const {replies} = res;	
				let str = "";		
				
				if(replies === null || replies.length === 0){
					replyUl.html = "";
					return;
				}
				let isSecret = false;
				for(let i=0, len=replies.length||0; i<len; i++){
					isSecret = replies[i].reply === null ? true : false;
					if(replies[i].deleteFlag){
						switch(replies[i].deleteFlag){
						case('R'):
							str += '<li class="left clearfix" data-rno="'+replies[i].rno+'" data-secret="true"><div><p>'+"댓글 작성자가 삭제한 댓글입니다."+'</p><small class="pull-right text-muted">'+replyService.displayTime(replies[i].regdate)+'</small></div></li>'; 
							break;
						case('B'):
							str += '<li class="left clearfix" data-rno="'+replies[i].rno+'" data-secret="true"><div><p>'+"게시글 작성자가 삭제한 댓글입니다."+'</p><small class="pull-right text-muted">'+replyService.displayTime(replies[i].regdate)+'</small></div></li>'; 
							break;
						case('A'):
							str += '<li class="left clearfix" data-rno="'+replies[i].rno+'" data-secret="true"><div><p>'+"관리자가 삭제한 댓글입니다."+'</p><small class="pull-right text-muted">'+replyService.displayTime(replies[i].regdate)+'</small></div></li>'; 
							break;
						}
					}
					else if(isSecret){
						str += '<li class="left clearfix" data-rno="'+replies[i].rno+'" data-secret="true"><div><p>'+"비밀 댓글입니다."+'</p><small class="pull-right text-muted">'+replyService.displayTime(replies[i].regdate)+'</small></div></li>'; 
					}
					else{
						str += '<li class="left clearfix" data-open="false" data-rno="'+replies[i].rno+'"><div><div class="header"><strong class="primary-font">'+replies[i].replyer+'</strong><small class="pull-right text-muted">'+replyService.displayTime(replies[i].regdate)+'</small>';
						str += '<div><span class="pull-right report">|신고</span>';
						if(replies[i].deleteFlag) str += '<span class="pull-right reply-del">|삭제</span>';
						str += '<span class="pull-right reply-added" data-open="false">대댓글</span></div>';
						str += '</div><p>'+replies[i].reply+'<a href="'+replies[i].rno+'"> ['+replies[i].addedCount+']'+'</a></p></div><div class="added-form"></div><div class="added-replies" data-page="1"></div></li>';
					}
					
				}
				replyUl.html(str);
				showReplyPagination();

			});
		}

		replyUl.on("click", ".reply-del",function(e){
			const rno = $(this).parents("li").data("rno");
			replyService.remove({rno : rno, bno: bno}, 
				function(res){
				showList(pageNum);
			});
		});
		
		const replyForm = $(".reply-form" ).clone()
		replyForm.find("button").removeClass('reply-reg').addClass('added-reply-reg');
		
		replyUl.on("click", ".reply-added", function(e){
			const reply = $(this).parents("li");
			const replyFormSec = reply.find(".added-form");
			
			if(replyFormSec.find(".reply-form").length === 0){
				$(replyFormSec).append(replyForm);
				if(reply.get(0).dataset.open === "false") toggleAddedReply($(this));
			}
			else{
				if(toggleAddedReply($(this)) === false) replyFormSec.children().remove();
			}
		});
		function toggleAddedReply(self){
			var reply = $(self).parents("li");
			if(reply.get(0).dataset.open === "true") {
				reply.find(".added-replies").children().remove();
				reply.attr("data-open","false")
				return false;
			}
			
			reply.attr("data-open","true");
			getAddedList($(self).parents("li"));
			return true;
		}
		replyUl.on("click", "a", function(e){
			e.preventDefault();
			toggleAddedReply($(this));
		});
		
		replyUl.on("click", ".added-reply-reg", function(e){
			e.preventDefault();
			const replySec = $(e.currentTarget).parents("li");
			const replyForm = replySec.find(".reply-form");
			replyForm.attr("data-parrno", replySec.data("rno"));
			addReply(replyForm);
		});
		
		replyUl.on("click", ".reply-btns", function(e){
			const target = $(e.target);
			const addedSection = $(e.currentTarget).parents(".added-replies");
			
			if(target.hasClass("fold")){
				addedSection.children().remove();
				const reply = addedSection.parents("li");
				reply.attr("data-open", "false");
				addedSection.siblings(".added-form").remove();
				return;
			}
			if(target.hasClass("more")){
				getAddedList(addedSection.parent());
			}
			
		});
		
		function updateLike(like, likeCnt){
			if(!isLogged){ 
				alert("로그인이 필요합니다.");
				return;
			}
			$.ajax({
				method: "get",
				url: "/board/daily/"+bno+"/like",
				data: {
					bno: bno,
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
		
		const replySec = $(".reply-form");
		const reply = replySec.find("textarea[name='reply']");
		
		const replyer = replySec.find("input[name='replyer']");
		const replyPassword = replySec.find("input[name='password']");
		const replySecret = replySec.find("input[name='secret']");
		
		const replyReg = replySec.find(".reply-reg");
		
		function checkInputVal(inputList){
			
			for(let item in inputList){
				
				if(inputList[item]===null || inputList[item]===undefined || (''+inputList[item]).trim() === "") return false;
			}
			return true;
		}
		function addReply(checkArea){
			var required = {
					reply: checkArea.find("textarea[name='reply']").val(),
					replyer: checkArea.find("input[name='replyer']").val(),
					bno: bno,
			}
			if(isLogged && !checkInputVal(required)){
				alert("빈 칸을 채워주세요.");
				return;
			}
			else if(!isLogged){
				required.password = checkArea.find("input[name='password']").val();
				console.log(required);
				if(!checkInputVal(required)){
					alert("빈 칸을 채워주세요.");
					return;
				}
			}
		
			required.parRno = checkArea.data("parrno");
			required.secret = checkArea.is(":checked");
			console.log(required);
			
			replyService.add(required, function(res){
				checkArea.find("input").val("");
				checkArea.data("parrno","");
				showList(1);
			});
		}
		replyReg.on("click", function(){
			addReply(replySec);		
		});
		
		function getAddedList(eachReply){
		
			var parRno = eachReply.data("rno");
			var regex = new RegExp(/\d+/);
			var totalNum = parseInt(/\d+/.exec(eachReply.find("a").text())[0]);
		
			if(totalNum === 0) return;
			
			var eachReplySection = eachReply.find(".added-replies");
			var reqPage = parseInt(eachReplySection.get(0).dataset.page);
			
			$.getJSON('/board/daily/'+bno+'/reply/added/'+parRno+"/"+reqPage, function(replies){
				
				let replyHbData = {};
				for(let i=0, len=replies.length||0; i<len; i++){
					replies[i].isSecret = (replies[i].reply === null);
				}
				replyHbData.replies = replies;
				
				if(reqPage === 1) replyHbData.isFirstPage = true;
				else replyHbData.isFirstPage = false;
				
				if(reqPage*10 >= totalNum) replyBbData.displayMore = "none";
				else replyBbData.displayMore = "";
				
				const str = replyTemplate(replyHbData);
				eachReplySection.append(str);
				
			});
		}
	
		replyFooter.on("click", "li a", function(e){
			e.preventDefault();
			var targetPage = $(this).attr("href");
			pageNum = targetPAge;
			showList(pageNum);
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
			formObj.attr("action", "/board/daily/"+bno+"/mod");
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
			var files = chatService.getFilesInfo();
		
			if(files !== ""){
				$.post("/board/delete", 
						{files : files, name: name});
			}
			
			formObj.attr("action", "/board/daily/"+bno);
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
</script>
</body>
</html>