<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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

<div class="box-body">
<div class="box-footer">
	<%-- <c:if test="${login.uid eq boardVO.writer}"> --%>
	<button type="submit" id="boardModBtn" class="btn btn-warning">Modify</button>
	<button type="submit" id="boardRemBtn" class="btn btn-danger">Remove</button>
	<%-- </c:if> --%>
	<button type="submit" id="boardAllBtn" class="btn btn-primary">List All</button>
</div>
	
<p></p>
	
<div class="big-picture-wrapper">
	<div class="big-picture">
	
	</div>
</div>

<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">Add Reply</h4>
            </div>
            <div class="modal-body">
               <div class="form-group">
               		<label>Reply</label>
               		<input class="form-control" name="reply">
               </div>
               <div class="form-group">
               		<label>Replyer</label>
               		<input class="form-control" name="replyer">
               </div>
               <div class="form-group">
               		<label>Reply Date</label>
               		<input class="form-control" name="replyDate">
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

<script type="text/javascript" src="/resources/js/chat.js"></script>

<script type="text/javascript">
	$(document).ready(function(){
		
		var bno = "${boardVO.bno}";
		
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
				for(let i=0, len=replies.length||0; i<len; i++){
			
					str += '<li class="left clearfix" data-rno="'+replies[i].rno+'"><div><div class="header"><strong class="primary-font">'+replies[i].replyer+'</strong><small class="pull-right text-muted">'+replyService.displayTime(replies[i].regdate)+'</small>';
					str += '</div><p>'+replies[i].reply+'</p></div></li>';
				}
				replyUl.html(str);
				showReplyPagination();
			})
		}
		
		var modal = $(".modal");
		var modalReply = modal.find("input[name='reply']");
		var modalReplyer = modal.find("input[name='replyer']");
		var modalReplyDate = modal.find("input[name='replyDate']");
		
		var modalMod = modal.find("#modal-mod");
		var modalRem = modal.find("#modal-rem");
		var modalReg = modal.find("#modal-reg");
		var modalClose = modal.find("#modal-close");
		
		$('#addReplyBtn').on("click", function(){
			modal.find("input").val("");
			modal.removeClass("fade");
			modalReplyDate.closest("div").hide();
			modal.find("button[id!='modal-close']").hide();
			modalReg.show();
			modal.modal("show");
		});
		
		modalClose.on("click", function(){
			$.modal.close();
		})
		
		modalReg.on("click", function(){
			var reply = {
				reply: modalReply.val(),
				replyer: modalReplyer.val(),
				bno: bno
			};
			replyService.add(reply, function(res){
				modal.find("input").val("");
				$.modal.close();
				showList(1);
			});
		});
		
		modalMod.on("click", function(){
			var reply = {
				rno: modal.data('rno'),
				reply: modalReply.val()
			};
			replyService.update(reply, function(res){
				modal.modal("hide");
				showList(pageNum);
			});
		});
		
		modalRem.on("click", function(){
			replyService.remove(modal.data("rno"), function(res){
				modal.modal("hide");
				showList(pageNum);
			});
		});
		
		replyUl.on("click", "li", function(e){
			var rno = $(this).data("rno");
			console.log(rno);
			replyService.get(rno, function(res){
				modalReply.val(res.reply);
				modalReplyer.val(res.replyer);
				modalReplyDate.val(replyService.displayTime(res.replyDate)).attr("readonly", true);
				modal.data("rno", res.rno);
				
				modal.find("button[id!='modal-close']").hide();
				modalMod.show();
				modalRem.show();
				
				modal.modal("show");
			});
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
			console.log(files);
			if(files !== ""){
				$.post("/deleteAllFiles", {files : files});
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
