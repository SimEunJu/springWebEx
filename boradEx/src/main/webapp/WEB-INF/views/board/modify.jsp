<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ include file="../include/header.jsp" %>

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
	
	
	<div class="box-footer">
		<ul class="mailbox-attachments clearfix uploadedList">
		
		</ul>
		<button type="submit" role="modify" class="btn btn-primary">Save</button>
		<button type="submit" role="delete" class="btn btn-warning">Remove</button>
		<button type="submit" role="list" class="btn btn-info">List</button>
	</div>
</form>
</div>

<%@ include file="../include/footer.jsp" %>

<%@ include file="../include/upload.jsp" %>

<script type="text/javascript" src="/resources/js/upload.js"></script>

<script type="text/javascript">

	$("document").ready(function(){
		
	var bno = $("input[name='bno']").val();
		
		$.getJSON("/board/getAttach/"+bno, function(list){
			$(list).each(function(){
				var fileInfo = getFileInfo(this);
				var html = template(fileInfo);
				$(".uploadedList").append(html);
			});
		});
		
		var formObj = $("form[role='form']");
		
		$("button[type='submit']").on("click", function(e){
			
			e.preventDefault();
			
			var role = $(this).attr("role");
			
			if(role === "delete"){
				formObj.attr("action", "/board/delete");
			
			}else if(role === "modify"){
				formObj.attr("action", "/board/modify");
			
			}else{
				window.location.href = "/board/list"+"${pageMaker.makeSearch(pagekMaker.cri.getPage)}"
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