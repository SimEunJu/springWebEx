<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
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
			<div class="form-group">
				<label for="exampleInputEmail1">File Drop Here</label>
				<div class="fileDrop"></div>
			</div>
		</div>
		<div class="box-footer">
			<div><hr></div>
			<ul class="mailbox-attachments clearfix uploadedList"></ul>
			<button type="submit" class="btn btn-primary">Submit</button>
		</div>
	</form>
</div>

<%@ include file="../include/upload.jsp" %>

<%@ include file="../include/footer.jsp" %>

<script type="text/javascript" src="/resources/js/upload.js"></script>
	
<script type="text/javascript">

$("#registerForm").on("submit", function(e){
	e.preventDefault();
	var self = $(this);
	var str = "";
	$(".uploadedList .delbtn").each(function(idx){
		str += "<input type='hidden' name='files["+idx+"]' value='"+$(this).attr("href")+"'>";
	});
	self.append(str);
	console.log(self);
	self.get(0).submit();
});

</script>
</body>
</html>