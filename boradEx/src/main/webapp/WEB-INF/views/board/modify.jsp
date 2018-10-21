<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>BoradEx</title>
</head>

<script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.0.12/handlebars.min.js"></script>

<body>

<%@ include file="../include/header.jsp" %>

<div class="box-body">
<form role="form" method="post">
	<input type="hidden" name="page" value="${ cri.page}" >
	<input type="hidden" name="perPageNum" value="${ cri.perPageNum}" >
	<div class="form-group">
		<label for="exampleInputEmail1">Bno</label>
		<input type="text" readonly="readonly" value="${boardVO.bno }"name="bno" class="form-control">
	</div>
	<div class="form-group">
		<label for="exampleInputEmail1">Title</label>
		<input type="text" value="${boardVO.title }"name="title" class="form-control" placeholder="Enter Title">
	</div>
	<div class="form-group">
		<label for="exampleInputPassword1">Content</label>
		<textarea class="form-control" row="3" name="content" placeholder="Enter ...">${boardVO.content }</textarea>
	</div>
	<div class="form-group">
		<label class="exampleInputEmail1">Writer</label>
		<input type="text" value="${boardVO.writer }" name="writer" placeholder="Enter Writer" class="form-control" value=${login.uid} readonly>
	</div>
	<div class="form-group">
		<label for="exampleInputEmail1">File Drop Here</label>
		<div class="fileDrop"></div>
	</div>
	
	
	<div class="box-footer">
		<ul class="mailbox-attachments clearfix uploadedList">
		
		</ul>
		<button type="submit" class="btn btn-primary">Save</button>
		<button type="submit" class="btn btn-warning">Cancel</button>
	</div>
</form>
</div>

<%@ include file="../include/footer.jsp" %>
<%@ include file="../include/upload.jsp" %>

<script type="text/javascript" src="/resources/js/upload.js"></script>

<script type="text/javascript">
	$("document").ready(function(){
		
	var bno = $("input[name='bno']").val();
	//var template = Handlebars.compile($("#template").html());
		
		$.getJSON("/board/getAttach/"+bno, function(list){
			$(list).each(function(){
				var fileInfo = getFileInfo(this);
				var html = template(fileInfo);
				$(".uploadedList").append(html);
			});
		});
		
		var formObj = $("form[role='role']");
		
		$(".btn-primary").on("click", function(){
			formObj.attr("action","/board/modify")
			formObj.submit();
		});
		
		$(".btn-warning").on("click", function(){
			window.location.href = "/board/list"+"${pageMaker.makeQuery(pagekMaker.cri.getPage)}"
			+"&searchType=" + "${cri.searchType}"
			+"&keyword=" + encodeURIComponent("${cri.keyword}")
		});
	});
	
</script>
</body>
</html>