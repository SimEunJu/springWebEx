<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div class="container">
	<div class="col-lg-12">
		<div class="card">
			<div class="card-head">
				<i class="fa fa-comments fa-fw"></i>댓글
			</div>
			
			<div class="card-body">
				<div class="row reply-form">
					<div class="col-sm-2">
						<sec:authorize access="isAuthenticated()">
							<div class="reply-secret">
								<label>비밀글</label>
								<input type="checkbox" name="secret">
							</div>
							<sec:authentication var="user" property="principal" />
							<div class="logged"><input type="hidden" name="replyer" value=${user.username}></div>
						</sec:authorize>
						<sec:authorize access="isAnonymous()">
							<div class="non-logged">
								<input type="text" name="replyer" style="width: 100%;" autocomplete="off" placeholder="이름">
								<input type="password" name="password" style="width: 100%;" placeholder="비밀번호" autocomplete="off">
							</div>
						</sec:authorize>	
					</div>
					<div class="col-sm-9">
						<div class="reply">
							<textarea style="width: 100%;" name="reply"></textarea>
						</div>
					</div>
					<div class="col-sm-1">
						<button class="btn btn-primary btn-xs reply-reg">입력</button>
					</div>
				<hr>
				</div>
				<ul class="reply-list">
					
				</ul>
			</div>
			
			<div class="card-footer pagination">
			
			</div>
		</div>
	</div>
</div>