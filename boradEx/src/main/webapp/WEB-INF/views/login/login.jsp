<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


	<div class="row border m-2 p-2">
		<form method="post" action="/login" class="col-md">
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			<div class="form-group">
				<input class="form-control" placeholder="아이디" type="text" name="username" /> 
			</div>
			<div class="form-group">
				<input class="form-control" placeholder="비밀번호" type="password" name="password" />
			</div>
			<div class="form-group">
				<div class="col-xs-8">
					<label> 
						<input type="checkbox" name="remember-me" /> Remember Me
					</label>
				</div>
				<div class="col-xs-4 float-right">
					<button type="submit" class="btn btn-outline-primary">Login</button>
				</div>
			</div>
		</form>
		
		<div class="col-md align-self-center text-center">
			<a href="/board/oauth2/authorization/google">Google로 로그인</a>
		</div>
	</div>

