<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
 	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
 	
    <title>daily : ${boardVO.title}</title>    
    
	<%@include file="../include/cssFiles.jsp" %>

</head>

<body>

<div class="box-body">
<form role="form" method="post">
	<input type="hidden" name="page" value="${ cri.page}" >
	<input type="hidden" name="perPageNum" value="${ cri.perPageNum}" >
	<div class="form-group">
		<label for="exampleInputEmail1">BNO</label>
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
	
	<input type="hidden" value="${_csrf.token }" name="${_csrf.parameterName }">
	
	<div class="box-footer">
		<ul class="mailbox-attachments clearfix uploadedList">
		
		</ul>
		<button type="submit" role="modify" class="btn btn-primary">Save</button>
		<button type="submit" role="delete" class="btn btn-warning">Remove</button>
		<button type="submit" role="list" class="btn btn-info">List</button>
	</div>
</form>
</div>

<sec:authentication property="principal.username" var="name" />

<%@ include file="../include/upload.jsp" %>

<script type="text/javascript">

	$("document").ready(function(){
		
	var name = "${name}";
	
	var bno = $("input[name='bno']").val();
		
	$.getJSON("/board/getAttach/"+bno, function(res){
		var files = fileService.showFiles(res);
		$(".upload-result ul").html(files);
	});
	
	var formObj = $("form[role='form']");
		
	$("button[type='submit']").on("click", function(e){
			
		e.preventDefault();
			
		var role = $(this).attr("role");
			
		if(role === "delete"){
			formObj.append("<input type='hidden' name='name' value='"+name+"' />")
			formObj.attr("method", "DELETE")
			formObj.attr("action", "/board/daily/"+${bno});
			
		}else if(role === "modify"){
			formObj.attr("method", "PUT");
			formObj.attr("action", "/board/daily/"+${bno});
			
		}else{
			window.location.href = "/board/daily"+"${pageMaker.makeSearch(pagekMaker.cri.getPage)}"
			//+"&searchType=" + "${cri.searchType}"
			//+"&keyword=" + encodeURIComponent("${cri.keyword}");
			return;
		}
		
		formObj.submit();
	});
	
});
	
</script>
</body>
</html>