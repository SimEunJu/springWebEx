<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">
				<i class="fa fa-comments fa-fw"></i>댓글
			</div>
			
			<div class="panel-body">
				<div class="row reply-sec">
					<div class="col-sm-2">
						<sec:authorize access="isAuthenticated()">
							<div class="reply-secret">
								<label>비밀글</label>
								<input type="checkbox" name="secret">
							</div>
							<div class="logged"><input type="hidden" name="replyer"></div>
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
						<button class="btn btn-primary btn-xs" id="reply-reg">입력</button>
					</div>
				</div>
				<hr>
				<ul class="chat">
					
				</ul>
			</div>
			
			<div class="panel-footer">
			
			</div>
		</div>
	</div>
</div>