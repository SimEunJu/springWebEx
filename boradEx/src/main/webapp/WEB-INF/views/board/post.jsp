<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
 	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
 	
    <title>daily : ${boardVO.title}</title>    
    
	<%@include file="../include/cssFiles.jsp" %>

</head>
	
<form role="form" method="post" >
	<input type="hidden" name="bno" value="${boardVO.bno }">
	<input type="hidden" name="page" value="${cri.page }">
	<input type="hidden" name="perPageNum" value="${cri.perPageNum }">
	<input type="hidden" name="searchType" value="${cri.searchType }">
	<input type="hidden" name="keyword" value="${cri.keyword }">
</form>

<div class="box-body">
	<div class="form-group">
		<label for="exampleInputEmail1">Title</label>
		<input type="text" readonly="readonly" value="${boardVO.title }"name="title" class="form-control" placeholder="Enter Title">
	</div>
	<div class="form-group">
		<label for="exampleInputPassword1">Content</label>
		<textarea class="form-control" readonly="readonly" row="3" name="content" placeholder="Enter ...">${boardVO.content }</textarea>
	</div>
	<div class="form-group">
		<label class="exampleInputEmail1">Writer</label>
		<input type="text" readonly="readonly" value="${boardVO.writer }" name="writer" placeholder="Enter Writer" class="form-control">
	</div>
</div>

<div class="box-footer">
	<sec:authentication property="principal" var="loginUser" />
	
	<sec:authorize access="isAuthenticated()">
		<c:if test="${loginUser.username eq boardVO.writer}">
			<button type="submit" id="boardModBtn" class="btn btn-warning">Modify</button>
			<button type="submit" id="boardRemBtn" class="btn btn-danger">Remove</button>
		</c:if>
	</sec:authorize>
	<button type="submit" id="boardAllBtn" class="btn btn-primary">List All</button>
	
	<button class="like" class="btn btn-primary btn-sm" style="color: ${isUserLiked ? 'red' : 'white'}">
		<span style="font-size: 20px; font-weight: bold;">♥</span>
		좋아요 
		<span class="like-num">${boardVO.userLike}</span>
	</button>
</div>
	
<p></p>
	
<div class="big-picture-wrapper">
	<div class="big-picture">
	
	</div>
</div>

<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">File Attach</div>
			
			<div class="panel-body">
				<div class="upload-result">
					<ul>
					
					</ul>
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

<%@ include file="../include/jsFiles.jsp" %>

<script type="text/javascript" src="/resources/js/reply.js"></script>

<script type="text/javascript" src="/resources/js/file.js"></script>

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
		
		// 첨부파일(이미지 등) 불러오기
		(function(){
			$.getJSON("/board/daily/"+bno+"/attach", function(res){
				let str = "";
				$(res).each(function(i, attach){
					if(attach.fileType.includes("img")){
						const filePath = encodeURIComponent(attach.uploadPath+"/s_"+attach.uuid+"_"+attach.fileName);
						str += "<li data-path='"+attach.uploadPath+"' data-uuid='"+attach.uuid+"' data-filename='"+attach.fileName+"' data-type='"+attach.fileType+"'><div>";
						str += "<img src='/displayFile?fileName="+filePath+"'></div></li>";
					}else{
						str += "<li data-path='"+attach.uploadPath+"' data-uuid='"+attach.uuid+"' data-filename='"+attach.fileName+"' data-type='"+attach.fileType+"'><div><span>"+attach.fileName+"</span><br>";
						str += "<img src='/resources/img/attach.png'></div></li>";
					}
				});
				$(".upload-result ul").html(str);
			});
		})();
		
		// 댓글 불러오기
		let pageNum = 1;
		const replyFooter = $(".panel-footer");
		function showReplyPagination(){
			let str = "<ul class='pagination pull-right'>";
			if("${pm.prev}"){ 
				str += "<li class='page-item'><a class='page-link' href='"+"${pm.startPage-1}"+"'>Prev</a></li>";
			}
			const startPage = parseInt("${startPage}");
			const endPage = parseInt("${endPage}");
			for(let i=startPage; i<=endPage; i++){
				const active = pageNum == i ? "active" : "";
				str += "<li class='page-item "+active+"'><a class='page-link' href='"+i+"</a></li>";
			}
			if("${pm.next}"){ 
				str += "<li class='page-item'><a class='page-link' href='"+"${pm.endPage+1}"+"'>Prev</a></li>";
			}
			str += "</ul></div>";
			replyFooter.html(str);
		}
		
		const replyUl = $(".chat");
		const option = '<div class=""><span class="pull-right report">|신고</span><span id="reply-del" class="pull-right">|삭제</span><span id="reply-added" class="pull-right">대댓글</span></div>';
		
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
					if(isSecret){
						str += '<li class="left clearfix" data-rno="'+replies[i].rno+'" data-secret="true"><div><p>'+"비밀 댓글입니다."+'</p><small class="pull-right text-muted">'+replyService.displayTime(replies[i].regdate)+'</small></div></li>'; 
					}
					else{
						
						str += '<li class="left clearfix" data-rno="'+replies[i].rno+'"><div><div class="header"><strong class="primary-font">'+replies[i].replyer+'</strong><small class="pull-right text-muted">'+replyService.displayTime(replies[i].regdate)+'</small>';
						str += option
						str += '</div><p>'+replies[i].reply+'<a data-open="false" href="'+replies[i].rno+'"> ['+replies[i].addedCount+']'+'</a></p></div><div class="added-replies" data-page="1"></div></li>';
					}
					
				}
				replyUl.html(str);
				showReplyPagination();

			});
		}

		replyUl.on("click", "#reply-del",function(e){
			const rno = $(this).parents("li").data("rno");
			replyService.remove({rno : rno, bno: bno}, 
				function(res){
				showList(pageNum);
			});
		});
		
		const replyForm = $(".reply" ).clone();
		console.log(replyForm);
		let curShowedReplyForm = null;
		
		replyUl.on("click", "#reply-added", function(e){
			getAddedList($(this).parents("li"));
			replySec.data("parRno", $(this).parents("li").data("rno"));
			if(curShowedReplyForm) curShowedReplyForm.remove();
			replyUl.parents("li").find(".reply-btns").prepend(replyForm);
			
		});
		
		replyUl.on("click", "a", function(e){
			e.preventDefault();
			if($(this).get(0).dataset.open === "true") {
				var rno = $(this).attr("href");
				var reply = $(this).parents("li[data-rno='"+rno+"']");
				reply.find("li").remove();
				reply.find("button").hide();
				$(this).attr("data-open","false")
				return;
			}
			
			$(this).attr("data-open","true");
			getAddedList($(this).parents("li"));
		});
		
		replyUl.on("click", ".reply-btns", function(e){
			var target = $(e.target);
			var addedSection = $(e.currentTarget).parent();
			
			if(target.hasClass("fold")){
				addedSection.children().remove();
				var reply = addedSection.parent();
				reply.find("a").attr("data-open", "false");
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
					like.css("color", diff == 1 ? "red" : "white");
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
		
		const replySec = $(".reply");
		const reply = replySec.find("textarea[name='reply']");
		
		const replyer = replySec.find("input[name='replyer']");
		const replyPassword = replySec.find("input[name='password']");
		const replySecret = replySec.find("input[name='secret']");
		
		const replyReg = replySec.find("#reply-reg");
		
		function checkInputVal(inputList){
			
			for(let item in inputList){
				
				if(inputList[item]===null || inputList[item]===undefined || (''+inputList[item]).trim() === "") return false;
			}
			return true;
		}
		
		replyReg.on("click", function(){
			var required = {
					reply: reply.val(),
					replyer: replyer.val(),
					bno: bno,
			}
			if(isLogged && !checkInputVal(required)){
				alert("빈 칸을 채워주세요.");
				return;
			}
			else if(!isLogged){
				required.password = replyPassword.val();
				if(!checkInputVal(required)){
					alert("빈 칸을 채워주세요.");
					return;
				}
			}
			
			//required.parRno = replySec.data("parRno");
			required.secret = replySecret.is(":checked");
			required.bno = bno;
			
			console.log(required);
			
			replyService.add(required, function(res){
				replySec.find("input").val("");
				replySec.data("parRno","");
				showList(1);
			});
			
		});
		
		function getAddedList(eachReply){
		
			var parRno = eachReply.data("rno");
			var regex = new RegExp(/\d+/);
			var totalNum = parseInt(/\d+/.exec(eachReply.find("a").text())[0]);
		
			if(totalNum === 0) return;
			
			var eachReplySection = eachReply.find(".added-replies");
			var reqPage = parseInt(eachReplySection.get(0).dataset.page);
			
			$.getJSON('/board/daily/'+bno+'/reply/added/'+parRno+"/"+reqPage, function(replies){
				var str = "";
				
				for(let i=0, len=replies.length||0; i<len; i++){
					isSecret = replies[i].reply === null;
					if(isSecret){
						str += '<li class="left clearfix" data-rno="'+replies[i].rno+'" data-secret="true"><div><p>'+"비밀글입니다."+'</p><small class="pull-right text-muted">'+replyService.displayTime(replies[i].regdate)+'</small></div></li>'; 
					}
					else{
						str += '<li class="left clearfix" data-rno="'+replies[i].rno+'"><div><div class="header"><strong class="primary-font">'+replies[i].replyer+'</strong><small class="pull-right text-muted">'+replyService.displayTime(replies[i].regdate)+'</small>';
						str += '</div><p>'+replies[i].reply+'</p></div></li>';
					}
				}
				
				if(reqPage === 1) str += '<div class="reply-btns"><button class="more btn btn-primary btn-sm">더보기</button><button class="fold btn btn-primary btn-sm pull-left">접기</button></div>';
				eachReplySection.append(str);
				
				if(reqPage*10 >= totalNum) eachReplySection.find(".more").hide();
				
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
			var path = encodeURIComponent(file.data("path")+"\\"+file.data("uuid")+"_"+file.data("filename"));
			
			if(file.data("type")){
				showImage(path);
			}else {
				self.location = "/download?fileName="+path;
			}
		});
		
		
		function showImage(path){
			$(".modal-body p").html("<img src='/displayFile?fileName="+path+"'>");
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