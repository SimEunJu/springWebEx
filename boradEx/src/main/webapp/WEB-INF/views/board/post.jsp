<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>	
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<form role="form" method="post" >
	<input type="hidden" name="bno" value="${board.bno }">
	<input type="hidden" name="page" value="${cri.page }">
	<input type="hidden" name="perPageNum" value="${cri.perPageNum }">
	<input type="hidden" name="searchType" value="${cri.searchType }">
	<input type="hidden" name="keyword" value="${cri.keyword }">
</form>

	<div class="row justify-content-end">
		<sec:authorize access="isAuthenticated()">
			<sec:authentication property="principal" var="loginUser" />
			<c:if test="${loginUser.username eq board.writer}">
				<button type="submit" id="boardModBtn" class="btn btn-outline-warning">수정</button>
				<button type="submit" id="boardRemBtn" class="btn btn-outline-danger">삭제</button>
			</c:if>
		</sec:authorize>
		<button type="submit" id="boardAllBtn" class="btn btn-outline-primary">목록으로</button>
	</div>
	<form>
		<div class="form-group">
			<label for="title">제목</label> 
			<input type="text" readonly="readonly" value="${board.title }" name="title" class="form-control">
		</div>
		<div class="form-group">
			<label for="content">내용</label>
			<textarea class="form-control" id="editor" readonly="readonly" row="3" name="content"></textarea>
		</div>
		<div class="form-group">
			<label class="wrtier">글쓴이</label> 
			<input type="text" readonly="readonly" value="${board.writer }" name="writer" class="form-control">
		</div>
	</form>

	<div class="row justify-content-center">
		<button class="btn btn-${isUserLiked? '' : 'outline-'}danger mb-4 like" style="color: ${isUserLiked ? 'white' : 'red'}">
			<span style="font-size: 20px; font-weight: bold;">♥</span> 좋아요 
			<span class="like-num">${board.userLike}</span>
		</button>
	</div>

	<div class="row">
		<div class="col-md-12">
			<div class="card">
				<div class="card-header">첨부파일</div>

				<div class="card-body">
					<div class="upload-result">
						<ul>

						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>


<div class="modal" tabindex="-1" role="dialog">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        
      </div>
    </div>
  </div>
</div>

<div class="card">
	<div class="card-header">
		<i></i>댓글
	</div>

	<div class="card-body">
		<div class="row card-body reply-form">
			<div class="col-sm-2">
				<sec:authorize access="isAuthenticated()">
					<div class="reply-secret">
						<label>비밀글</label> <input type="checkbox" name="secret">
					</div>
					<sec:authentication var="user" property="principal" />
					<div class="logged">
						<input type="hidden" name="replyer" value=${user.username}>
					</div>
				</sec:authorize>
				<sec:authorize access="isAnonymous()">
					<div class="non-logged">
						<input type="text" name="replyer" style="width: 100%;"
							autocomplete="off" placeholder="이름"> <input
							type="password" name="password" style="width: 100%;"
							placeholder="비밀번호" autocomplete="off">
					</div>
				</sec:authorize>
			</div>
			<div class="col-sm-8">
				<div class="reply">
					<textarea style="width: 100%;" name="reply"></textarea>
				</div>
			</div>
			<div class="col-sm-2">
				<button class="btn btn-primary btn-xs reply-reg">입력</button>
			</div>
			<hr>
		</div>
		<ul class="reply-list list-group" data-trigger="click" data-toggle="popover" data-content="신고">

		</ul>
	</div>

	<div class="card-footer pagination justify-content-center"></div>
</div>
	
<script type="text/javascript" >

$(document).ajaxSend(function(e, xhr, options){
	xhr.setRequestHeader("${_csrf.headerName}",  "${_csrf.token}");
});

let nameTestAuth = "";
let isLoggedTestAuth = false;

<sec:authorize access="isAuthenticated()">
	nameTestAuth = "${loginUser.username}";
	isLoggedTestAuth = true;
</sec:authorize>

const board = {
		wrtier: "${board.writer}",
		bno: "${board.bno}"
};
</script>

<script type="text/javascript" src="/resources/js/utils/reply.js"></script>

<script type="text/javascript" src="/resources/js/utils/file.js"></script>

<script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.1.0/handlebars.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" 
	integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" 
	integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>

<script id="reply-hb" type="text/x-handlebars-template">
{{#each this}}
	<li class="reply list-group-item" data-rno="{{rno}}" data-open="false" >
		{{#if isNormal}}
			<div>
				<div class="header">
					<span style="font-weight:bold" class="replyer" >{{replyer}}</span>
					<small class="float-right">{{dateFormat regdate}}</small>
					<div class="row justify-content-end mr-1">
						<small><a class="reply-added">대댓글</a></small>
						<small><a class="reply-del">|삭제</a></small>
						<small><a class="reply-report">|신고</a></small>
					</div>
				</div>
				<p>{{reply}}<a href="{{rno}}" class="added-cnt"> [{{addedCount}}]</a></p>
			</div>
			<div class="added-form-sec"></div>
			<div class="added-replies" data-page="1">
			</div>
		{{else}}
			<div>
				{{#if deleteFlag}}
					<p>{{deleteMsg}}</p>
				{{else if secret}}
					<p>비밀 댓글입니다.</p>
				{{/if}}
				<small class="float-right">{{dateFormat regdate}}</small>
		</div>
		{{/if}}
	</li>
{{/each}}
</script>
<script id="added-reply-hb" type="text/x-handlebars-template">
{{#each replies}}
<li class="added-reply list-group-item" data-rno="{{rno}}">
	<div>
		{{#if secret}}
			<p>비밀글입니다.</p>
			<small class="pull-right text-muted">{{dateFormat regdate}}</small>
		{{else}}
			<div class="header">
				<strong class="primary-font">{{replyer}}</strong>
				<small class="pull-right text-muted">{{dateFormat regdate}}</small>
			</div>
			<p>{{reply}}</p>
		{{/if}}
	</div>
</li>
{{/each}}
{{#if isFirstPage}}
	<div class="added-reply-btns">
		<button style="display: {{displayMore}}" class="more btn btn-primary btn-sm">더보기</button>
		<button class="fold btn btn-primary btn-sm pull-left">접기</button>
	</div>
{{/if}}
</script>

<script id="upload-item" type="text/x-handlebars-template">
{{#each attaches}}
<li data-path='{{uploadPath}}' data-uuid='{{uuid}}' data-filename='{{fileName}}' data-type='{{fileType}}'>
	<div>
		{{#if isImg}}
			<img src='/board/daily/file?fileName={{filePath}}'>
		{{else}}
			<img src='/resources/img/attach.png'>
		{{/if}}
		<span>{{fileName}}</span>
	</div>
</li>
{{/each}}
</script>
<script id="pagination-hb" type="text/x-handlebars-template">
{{#if prev}}
	<li class="page-item"><a class="page-link" href="">&laquo;</a></li>
{{/if}}
{{#for startPage endPage}}
	<li class="page-item"><a class="page-link" href="{{this}}">{{this}}</a></li>
{{/for}}
{{#if next}}
	<li class="page-item"><a class="page-link" href="">&raquo;</a></li>
{{/if}}
</script>

<script src="https://cdn.ckeditor.com/ckeditor5/11.2.0/classic/ckeditor.js"></script>

<script>
    ClassicEditor
        .create(document.querySelector( '#editor' ))
        .then(
        	editor => {editor.setData("${board.content}"); editor.isReadOnly = true;}
        ).catch( 
        	error => {console.error( error );}
        );
</script>

<script src="/resources/js/utils/handlebarHelper.js"></script>
