<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


	<div class="row border m-2 p-2" style="height: 60vh;">
		<form method="post" action="/login" class="col-6 m-auto">
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
	
			<div class="form-group">
				<input class="form-control" placeholder="아이디" type="text" name="username" /> 
			</div>
			<div class="form-group">
				<input class="form-control" placeholder="비밀번호" type="password" name="password" />
			</div>
			<div class="form-group">
				<!-- <div class="col-xs-8">
					<label> 
						<input type="checkbox" name="remember-me" /> 로그인 상태 유지
					</label>
				</div> -->
				<div class="col-xs-4 float-right">
					<button type="submit" class="btn btn-outline-primary">로그인</button>
				</div>
			</div>
		</form>
	</div>

