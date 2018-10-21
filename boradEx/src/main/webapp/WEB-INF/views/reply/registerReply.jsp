<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<div class="row">
	<div class="col-md-12">
		<div class="box box-success">
			<div class="box-header"><h3 class="box-title">Add New Reply</h3></div>
			<c:if test="${not empty login}">
			<div class="box-body">
				<label for="newReplyer">Writer</label>
				<input id="newReplyer" name="replyer" type="text" placeholder="user ID" class="form-control"  value="${login.uid}" readonly> 
				<label for="newReplyText">ReplyText</label>
				<input id="newReplyText" name="replytext" type="text" placeholder="reply text" class="form-control"> 
			</div>
			<div class="box-footer">
				<button type="button" class="btn btn-primary" id="replyAddBtn">Register</button>
			</div>
			</c:if>
			<c:if test="${empty login }">
			<div class="box-body"><a href="/login">Login First</a></div>
			<!-- href="javascript:goLogin()" -->
			</c:if>
		</div>
	</div>
</div>
