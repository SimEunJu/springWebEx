<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<style>
 .fileDrop {
 	width: 80%;
 	height: 200px;
 	border: 1px solid blue;
 	background-color: lightslategrey;
 	margin: auto;
 }
</style>

<script id="template" type="template/handlebars">
<li>
	<span class="mailbox-attachment-icon has-img"><img src="{{imgsrc}}" alt="attachment"></span>
	<div class="mailbox-attachment-info">
		<a href="{{getLink}}" class="mailbox-attachment-name">{{fileName}}</a>
		<a href="{{fullName}}" class="btn btn-default btn-xs pull-right delbtn">
			<i class="fa fa-fw fa-remove"></i>
		</a>
	</div>
</li>
</script>

<script>

var template = Handlebars.compile($("#template").html());

$(".fileDrop").on("dragenter dragstart dragend dragleave dragover drag drop", function(e){
	e.preventDefault();
});

$(".fileDrop").on("drop", function(e){
	var files = e.originalEvent.dataTransfer.files;
	var file = files[0];
	console.log(file);
	var form = new FormData();
	form.append("file",file);
	$.ajax({
		url: "/uploadAjax",
		data: form,
		dataType: "text",
		processData: false,
		contentType: false,
		type: "post",
		success: function(data){
			var fileInfo = getFileInfo(data);
			var html = template(fileInfo);
			$(".uploadedList").append(html);
		}
	});
});

$(".uploadedList").on("click", ".delbtn", function(e){
	e.preventDefault();
	var parE = $(this).parent().parent();
	var self = $(this);
	console.log($(self).attr("href"));
	$.ajax({
		url: "/deleteFile",
		type: "post",
		data: {file: $(self).attr("href")},
		success: function(){
			parE.remove();
		}
	});
});

</script>