<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<%@ include file="../include/header.jsp" %>
	
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
	
	<button class="like" class="btn btn-primary btn-sm" style="scolor: white;">
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

<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="max-width: none">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">Reply</h4>
            </div>
            <div class="modal-body">
               <div class="form-group">
               		<label>Reply</label>
               		<input class="form-control" name="reply" readonly>
               </div>
               <div class="form-group non-logged">
               		<label>Replyer</label>
               		<input class="form-control" name="replyer" autocomplete="off">
               		<input type="password" name="password" placeholder="4자리 비밀번호를 입력해주세요" autocomplete="off">
               </div>
               <div class="form-group logged" style="display: none">
               		<label>Replyer</label>
               		<input class="form-control" name="replyer" readonly>
               </div>
               <div class="form-group">
               		<label>비밀글</label>
               		<input class="form-control" type="checkbox" name="secret">
               </div>
               <div class="form-group">
               		<label>Reply Date</label>
               		<input class="form-control" name="replyDate" readonly>
               </div>
            </div>
            <div class="modal-footer">
            	<button type="button" id="modal-mod" class="btn btn-warning">Modify</button>
            	<button type="button" id="modal-rem" class="btn btn-danger">Remove</button>
                <button type="button" id="modal-reg" class="btn btn-primary">Register</button>
                <!-- <a rel="modal:close" class="close-modal ">   -->     
               		<button type="button" id="modal-close"  class="btn btn-default">Close</button>
               	<!-- </a> -->
                
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- /.modal -->

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

<%@ include  file="../reply/replyList.jsp" %>

<%@ include file="../include/footer.jsp" %>

<script type="text/javascript" src="/resources/js/reply.js"></script>

<script type="text/javascript" src="/resources/js/file.js"></script>

<script type="text/javascript">
	$(document).ready(function(){
	
		var csrfHeader = "${_csrf.headerName}";
		var csrfTokenVal = "${_csrf.token}";
		
		/* $(document).ajaxSend(function(e, xhr, options){
			xhr.setRequestHeader(csrfHeader, csrfTokenVal);
		}); */
	
		var bno = "${boardVO.bno}";
		var name = "";
		<sec:authorize access="isAuthenticated()">
			name = "${loginUser.username}";
		</sec:authorize>
		var isLogged = name === "" ? false : true;
		
		(function(){
			$.getJSON("/board/getAttach/"+bno, function(res){
				var str = "";
				$(res).each(function(i, attach){
					if(attach.fileType){
						var filePath = encodeURIComponent(attach.uploadPath+"/s_"+attach.uuid+"_"+attach.fileName);
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
		
		var pageNum = 1;
		var replyFooter = $(".panel-footer");
		function showReplyPagination(){
			var str = "<ul class='pagination pull-right'>";
			if("${pm.prev}"){ 
				str += "<li class='page-item'><a class='page-link' href='"+"${pm.startPage-1}"+"'>Prev</a></li>";
			}
			var startPage = parseInt("${startPage}");
			var endPage = parseInt("${endPage}");
			for(let i=startPage; i<=endPage; i++){
				var active = pageNum == i ? "active" : "";
				str += "<li class='page-item "+active+"'><a class='page-link' href='"+i+"</a></li>";
			}
			if("${pm.next}"){ 
				str += "<li class='page-item'><a class='page-link' href='"+"${pm.endPage+1}"+"'>Prev</a></li>";
			}
			str += "</ul></div>";
			replyFooter.html(str);
		}
		
		var replyUl = $(".chat");
		showList(1);
		function showList(page){
			replyService.getList({bno: bno, page: page||1}, function(res){

				var {replies} = res;	
				var str = "";
				if(replies === null || replies.length === 0){
					replyUl.html = "";
					return;
				}
				let isSecret = false;
				for(let i=0, len=replies.length||0; i<len; i++){
					isSecret = replies[i].reply === null;
					if(isSecret){
						str += '<li class="left clearfix" data-rno="'+replies[i].rno+'" data-secret="true"><div><p>'+"비밀글입니다."+'</p><small class="pull-right text-muted">'+replyService.displayTime(replies[i].regdate)+'</small></div></li>'; 
					}
					else{
						str += '<li class="left clearfix" data-rno="'+replies[i].rno+'"><div><div class="header"><strong class="primary-font">'+replies[i].replyer+'</strong><small class="pull-right text-muted">'+replyService.displayTime(replies[i].regdate)+'</small>';
						str += '<button class="added btn btn-primary btn-xs pull-right">대댓글</button></div><p>'+replies[i].reply+'<a data-open="false" href="'+replies[i].rno+'"> ['+replies[i].addedCount+']'+'</a></p></div><div class="added-replies" data-page="1"></div></li>';
					}
					
				}
				replyUl.html(str);
				showReplyPagination();
			})
		}

		function updateLike(like, diff){
			if(name === ""){ 
				alert("로그인이 필요합니다.");
				return;
			}
			$.ajax({
				method: "get",
				url: "/board/like",
				data: {
					bno: bno,
					diff: diff,
					username: name
					},
				success: function(){
					like.css("color", diff == 1 ? "red" : "white");
					console.log(like);
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
		
		var modal = $(".modal");
		var modalReply = modal.find("input[name='reply']");
		
		function activatedReplyer(){
			if(name === "") return modal.find(".non-logged input[name='replyer']");
			else return modal.find(".logged input[name='replyer']");
		}
		var modalReplyer = activatedReplyer();
		
		var modalReplyDate = modal.find("input[name='replyDate']");
		var modalPassword = modal.find("input[name='password']");
		var modalSecret = modal.find("input[name='secret']");
		
		var modalMod = modal.find("#modal-mod");
		var modalRem = modal.find("#modal-rem");
		var modalReg = modal.find("#modal-reg");
		var modalClose = modal.find("#modal-close");
		
		function showReplyer(type){
			if(type === "nonLogged"){
				modal.find(".logged").css("display", "none");
				modal.find(".non-logged").css("display", "");
				return;
			}
			if(type === "logged"){
				modal.find(".logged").css("display", "");
				modal.find(".non-logged").css("display", "none");
				modalReplyer.val(name);
				return;
			}
		}
		
		function showReplyAddModal(){
			modalReply.val("").attr("readonly", false);
			
			if(name === "") showReplyer("nonLogged"); 
			else showReplyer("logged");
			
			modalReplyDate.closest("div").hide();
			modalSecret.parent().show();
			modal.find("button[id!='modal-close']").hide();
			modalReg.show();
			
			modal.removeClass("fade");
			modal.modal("show");
		}
		
		$('#addReplyBtn').on("click", function(){
			showReplyAddModal();
		});
		
		modalClose.on("click", function(){
			$.modal.close();
		});
		
		function checkInputVal(inputList){
			
			for(let item in inputList){
				
				if(inputList[item]===null || inputList[item]===undefined || (''+inputList[item]).trim() === "") return false;
			}
			return true;
		}
		
		modalReg.on("click", function(){
			var required = {
					reply: modalReply.val(),
					replyer: modalReplyer.val(),
					bno: bno,
			}
			if(isLogged && !checkInputVal(required)){
				alert("빈 칸을 채워주세요.");
				return;
			}
			else if(!isLogged){
				required.password = modalPassword.val();
				if(!checkInputVal(required)){
					alert("빈 칸을 채워주세요.");
					return;
				}
			}
			
			required.parRno = modal.data("parRno");
			required.secret = modalSecret.is(":checked");
			
			replyService.add(required, function(res){
				modal.find("input").val("");
				$.modal.close();
				showList(1);
			});
		});
		
		modalMod.on("click", function(){
			var reply = {
				rno: modal.data('rno'),
				reply: modalReply.val(),
				replyer: modalReplyer.val()
			};
			replyService.update(reply, function(res){
				modal.modal("hide");
				showList(pageNum);
			});
		});
		
		modalRem.on("click", function(){
			replyService.remove(modal.data("rno"), modalReplyer.val(), function(res){
				modal.modal("hide");
				showList(pageNum);
			});
		});
		
		replyUl.on("click", ".added", function(e){
			showReplyAddModal();
			modal.data("parRno", $(this).parents("li").data("rno"));
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
		
		function getAddedList(eachReply){
		
			var parRno = eachReply.data("rno");
			var regex = new RegExp(/\d+/);
			var totalNum = parseInt(/\d+/.exec(eachReply.find("a").text())[0]);
		
			if(totalNum === 0) return;
			
			var eachReplySection = eachReply.find(".added-replies");
			var reqPage = parseInt(eachReplySection.get(0).dataset.page);
			
			$.getJSON('/replies/added/'+parRno+"/"+reqPage, function(replies){
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
			$(".big-picture-wrapper").css("display","flex").show();
			$(".big-picture").html("<img src='/displayFile?fileName="+path+"'>").animate({width: "100%", height: "100%"}, 1000);
		}
		
		var pictureWrapper = $(".big-picture-wrapper");
		pictureWrapper.on("click", function(){
			$(".big-picture").animate({width: "0", height: "0"}, 1000);
			setTimeout(function(){
				pictureWrapper.hide();
			}, 1000);
		});
	
		var formObj = $("form[role='form']");
		
		$("#boardModBtn").on("click",function(){
			formObj.attr("action", "/board/modify");
			formObj.attr("method", "get");
			formObj.submit();
		});
		
		$("#boardRemBtn").on("click",function(){
			
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
			
			formObj.attr("action", "/board/delete");
			formObj.attr("method", "post");
			formObj.submit();
		});
		
		$("#boardAllBtn").on("click",function(){
			formObj.attr("action", "/board/list");
			formObj.attr("method", "get");
			formObj.submit();
		});
	});
</script>
