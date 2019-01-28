<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>    

<link rel="stylesheet" type="text/css" href="/resources/css/upload.css" />

<script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.0.12/handlebars.min.js"></script>

<%@ include file="../include/header.jsp" %>

<div class="box-body">
	<form role="form" id="registerForm" method="post">
		<div class="box-body">
			<div class="form-group">
				<label for="exampleInputEmail1">Title</label>
				<input type="text" name="title" class="form-control" placeholder="Enter Title">
			</div>
			<div class="form-group">
				<label for="exampleInputPassword1">Content</label>
				<textarea class="form-control" row="3" name="content" placeholder="Enter ..."></textarea>
			</div>
			<div class="form-group">
				<label class="exampleInputEmail1">Writer</label>
				<input type="text" name="writer" placeholder="Enter Writer" class="form-control" value="${login.uid}" readonly>
			</div>
		</div>
		<div class="box-footer">
			<ul class="mailbox-attachments clearfix uploadedList"></ul>
			<button type="submit" class="btn btn-primary">Submit</button>
		</div>
	</form>
</div>

<%@ include file="../include/upload.jsp" %>

<%@ include file="../include/footer.jsp" %>

<script type="text/javascript" src="/resources/js/upload.js"></script>
	
<script type="text/javascript">

var form = $("#registerForm");

form.on("submit", function(e){
	e.preventDefault();
	var str = "";
	$(".upload-result ul li").each(function(i, obj){
		var fileObj = $(obj);
		str += "<input type='hidden' name='attachList["+i+"].fileName' value='"+fileObj.data("filename")+"'";
		str += "<input type='hidden' name='attachList["+i+"].uuid' value='"+fileObj.data("uuid")+"'";
		str += "<input type='hidden' name='attachList["+i+"].uploadPath' value='"+fileObj.data("path")+"'";
		str += "<input type='hidden' name='attachList["+i+"].fileType' value='"+fileObj.data("type")+"'";
	});
	form.append(str).submit();
});

</script>
</body>
</html>