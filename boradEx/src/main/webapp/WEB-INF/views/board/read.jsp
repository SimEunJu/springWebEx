<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>BoardEX</title>
</head>
<script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.0.12/handlebars.min.js"></script>
<body>

<style>
	.popup{ position: absolute;}
	.back { background-color: gray; opacity: 0.5; width: 100%; height: 300%;
		overflow:hidden; z-index: 1101;}
	.front{	z-index: 1100; opacity: 1; border: 1px; margin: auto;}
	.show{ position:absolute; max-width: 1200px; max-height: 800px; overflow: auto;}
</style>

<%@ include file="../include/header.jsp" %>

<div id="modifyModal" class="modal modal-primary fade" role="dialog">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="modal-title"></h4>
			</div>
			<div class="modal-body">
				<p><input type="text" id="modifiedReply" class="form-control"></p>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-info" id="replyModBtn">Modify</button>
				<button type="button" class="btn btn-danger" id="replyDelBtn">Delete</button>
				<button type="button" class="btn btn-default" id="replyCloBtn" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div>

<div class="pop back" style="display: none;"></div>
<div id="popup_front" class="popup front" style="display:none;">
	<img id="popup_img">
</div>
	
<form role="form" method="post">
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
		
	<ul class="mailbox-attachments clearfix uploadedList">
		
	</ul>
</div>

<div class="box-body">
<div class="box-footer">
	<c:if test="${login.uid eq boardVO.writer}">
	<button type="submit" id="boardModBtn" class="btn btn-warning">Modify</button>
	<button type="submit" id="boardRemBtn" class="btn btn-danger">Remove</button>
	</c:if>
	<button type="submit" id="boardAllBtn" class="btn btn-primary">List All</button>
</div>
	
<p></p>
	
<%@ include file="../reply/registerReply.jsp" %>

<%@include  file="../reply/replyList.jsp" %>

<%@ include file="../include/footer.jsp" %>
</div>

<script id="replyTemplate" type="template/handlebars">
	{{#each .}}
	<li class="replyLi" data-rno={{rno}}>
		<i class="fa fa-comments bg-blud"></i>
		<div class="timeline-item">
			<span class="time"><i class="fa fa-clock-o"></i>{{pretifyDate regdate}}</span>
			<h3 class="timeline-header"><strong>{{rno}}</strong>--{{replyer}}</h3>
			<div class="timeline-body">{{replytext}}</div>
			
			{{#eqReplyer replyer}}
			<div class="timeline-footer">
				<a class="btn btn-primary btn-xs" data-toggle="modal" data-target="#modifyModal">Modify</a>
			</div>
			{{/eqReplyer}}
		</div>
	</li>
	{{/each}}
</script>

<script id="fileTemplate" type="template/handlebars">
<li data-src="{{fullName}}">
	<span class="mailbox-attachment-icon has-img"><img src="{{imgsrc}}" alt="attachment"></span>
	<div class="mailbox-attachment-info">
		<a href="{{getLink}}" class="mailbox-attachment-name">{{fileName}}</a>
	</div>
</li>	
</script>

<script type="text/javascript" src="/resources/js/upload.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		
		var bno = ${boardVO.bno};
		var pageOffset = 1;
		
		var fileTemplate = Handlebars.compile($("#fileTemplate").html());
		
		$.getJSON("/board/getAttach/"+bno, function(list){
			$(list).each(function(){
				var fileInfo = getFileInfo(this);
				var html = fileTemplate(fileInfo);
				$(".uploadedList").append(html);
			});
		});
		
		Handlebars.registerHelper("eqReplyer", function(replyer, block){
			var acc = "";
			if(replyer === "${login.uid}") acc += block.fn();
			return acc;
		});
		
		Handlebars.registerHelper("pretifyDate", function(timeVal){
			var dateObj = new Date(timeVal);
			var year = dateObj.getFullYear();
			var month = dateObj.getMonth()+1;
			var date = dateObj.getDate();
			return year+"/"+month+"/"+date;
		});
		
		$(".uploadedList").on("click", ".mailbox-attachment-info a" ,function(){
			var fileLink = $(this).attr("href");
			if(checkImageType(fileLink)){
				e.preventDefault();
				var imgTag = $("#popup_img");
				imgTag.attr("src", fileLink);
				$(".popup").show("slow");
				imgTag.addClass("show");
			}
		});
		
		$("#popup_img").on("click", function(){
			$(".popup").hide("slow");
		});
		
		function printData(reply, target, template){
			var template = Handlebars.compile(template.html());
			var ret = template(reply);
			$(".replyLi").remove();
			target.after(ret);
		}
		
		function printPaging(page, target){
			var str = "";
			if(page.prev){
				str += "<li><a href='"+(page.startPage-1)+"'> << </a></li>";
			}
			for(var i=page.startPage; i<=page.endPage; i++){
				str += "<li><a href='"+i+"'>"+i+"</a></li>";
			}
			if(page.next){
				str += "<li><a href='"+(page.startEnd+1)+"'> << </a></li>";
			}
			target.html(str);
		}
		
		function getPage(uri){
			$.getJSON(uri, function(data){
				printData(data.replies, $(".time-label"), $("#replyTemplate"));
				printPaging(data.paging, $("#pagination"));
				$("#modifyModal").modal("hide");	
			});
		}
		
		$("#repliesDiv").on("click", function(e){
			if($(".timeline li").size() > 1) return;
			getPage("/replies/"+bno+"/1");
		});
		
		$("#pagination").on("click","li a", function(e){
			e.preventDefault();
			var page = $(this).attr("href");
			pageOffset = page;
			getPage("/replies/"+bno+"/"+pageOffset);
		});
		
		$("#replyAddBtn").on("click", function(e){
			e.preventDefault();
			$.ajax({
				type: "post",
				url: "/replies",
				headers: {
					"Content-Type": "application/json",
					"X-HTTP-Method-Override": "POST"
				},
				dataType: "text",
				data: JSON.stringify({
					bno: bno,
					replyer: $("#newReplyer").val(),
					replytext: $('#newReplyText').val()
				}),
				success: function(data){
					if(data === 'success') alert("success");
					pageOffset = 1;
					getPage("/replies/"+bno+"/1");
					$("#newReplyer").val("");
					$('#newReplyText').val("");
					var cnt = $("#replycntSmall").html();
					$("#replycntSmall").html(parseInt(cnt)+1);
				}
			});	
		});
		
		$(".timeline").on("click",".replyLi", function(){
			$(".modal-title").text($(this).attr("data-rno"));
			$("#modifiedReply").val($(this).find(".timeline-body").text());
		})
		
		$("#replyModBtn").on("click", function(e){
			var rno = $(".modal-title").text();
			$.ajax({
				type: "put",
				url: "/replies/"+rno,
				headers: {
					"Content-Type": "application/json",
					"X-HTTP-Method-Override": "PUT"
				},
				dataType: "text",
				data: JSON.stringify({
					replytext: $("#modifiedReply").val()
				}),
				success: function(data){
					if(data === 'success') alert("success");
					getPage("/replies/"+bno+"/"+pageOffset);
				}
			});	
		});
		
		$("#replyDelBtn").on("click", function(){
			var rno = $(".modal-title").text();
			$.ajax({
				type: "delete",
				url: "/replies/"+rno,
				headers: {
					"Content-Type": "application/json",
					"X-HTTP-Method-Override": "DELETE"
				},
				dataType: "text",
				success: function(data){
					if(data === 'success') alert("success");
					getPage("/replies/"+bno+"/"+pageOffset);
				}
			});	
		});
		
		var formObj = $("form[role='form']");
		
		$("#boardModBtn").on("click",function(){
			formObj.attr("action", "/board/modify");
			formObj.attr("method", "get");
			formObj.submit();
		});
		
		$("#boardRemBtn").on("click",function(){
			// $("#replycntSmall").html().replace(/^[0-9]/g,"");
			var replycnt = $("#replycntSmall").html();
			if(parseInt(replycnt) > 0){
				alert("댓글이 있는 게시물은 삭제할 수 없습니다.");
				return;
			}
			
			var arr = [];
			$(".uploadedList li").each(function(idx){
				arr.push($(this).attr("data-src"));
			});
			if(arr.length > 0){
				$.post("/deleteAllFiles",{files : arr}, function(){});
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
</body>
</html>