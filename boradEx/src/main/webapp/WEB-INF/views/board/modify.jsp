<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="../common/header.jsp"%>

<div class="container">
	<form role="form" method="post">
		<input type="hidden" name="page" value="${cri.page}"> 
		<input type="hidden" name="perPageNum" value="${cri.perPageNum}">
		
		<div class="form-group">
			<label for="bno">게시물 번호</label> 
			<input type="text" readonly="readonly" value="${board.bno }" name="bno" class="form-control">
		</div>
		<div class="form-group">
			<label for="title">제목</label> 
			<input type="text" value="${board.title }" name="title" class="form-control" placeholder="제목을 입력해주세요">
		</div>
		<div class="form-group content">
			<label for="content">내용</label>
			<textarea name="content" id="editor" name="content"></textarea>
		</div>
		<div class="form-group">
			<label class="writer">글쓴이</label> <input type="text" value="${board.writer}" name="writer" class="form-control" readonly>
		</div>

		<input type="hidden" value="${_csrf.token }" name="${_csrf.parameterName }">

		<div class="row-12">
			<ul class="uploadedList">

			</ul>
			<button type="submit" role="modify" class="btn btn-outline-primary">Save</button>
			<button type="submit" role="delete" class="btn btn-outline-warning">Remove</button>
			<button type="submit" role="list" class="btn btn-outline-info">List</button>
		</div>
	</form>
</div>

<%@ include file="../common/footer.jsp"%>

<%@ include file="../include/upload.jsp"%>

<script src="https://cdn.ckeditor.com/ckeditor5/11.2.0/classic/ckeditor.js"></script>

<script>
    ClassicEditor
        .create(document.querySelector( '#editor' ))
        .then(
        	editor => {editor.setData("${board.content}");}
        ).catch( 
        	error => {console.error( error );}
        );
</script>

<script type="text/javascript">

$("document").ready(function(){
	
	<sec:authentication property="principal.username" var="name" />
	const name = "${name}";
	
	const bno = $("input[name='bno']").val();
		
	$.getJSON("/board/daily/"+bno+"/attach", function(res){
		const files = fileService.showFiles(res);
		$(".upload-result ul").html(files);
	});
	
	var formObj = $("form[role='form']");
		
	$("button[type='submit']").on("click", function(e){
			
		e.preventDefault();
			
		var role = $(this).attr("role");
			
		if(role === "delete"){
			formObj.append("<input type='hidden' name='name' value='"+name+"' />");
			formObj.attr("method", "DELETE");
			formObj.attr("action", "/board/daily/"+bno);;
			
		}else if(role === "modify"){
			formObj.attr("method", 'post');
			formObj.attr("action", "/board/daily/"+bno);
			
		}else{
			window.location.href = "/board/daily"+"${pageMaker.makeSearch(pagekMaker.cri.getPage)}";
			return;
		}
		
		formObj.submit();
	});
	
});
	
</script>
</body>
</html>