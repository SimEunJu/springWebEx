<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div class="box-body">
	<div>
		<a href="/board/list"><button style="margin-bottom: 15px;">HOME</button></a>
	</div>
	<form method="post" action="/board/signin" >
		<div class="form-group has-feedback">
			<input class="form-control" placeholder="user id" type="text" name="username" />
			<span class="glyphicon glyphicon-envelope form-control-feedback"></span>
		</div>
		<div class="form-group has-feedback">
			<input class="form-control" placeholder="user password" type="password" name="password" />
			<span class="glyphicon glyphicon-lock form-control-feedback"></span>
		</div>
		<div class="row">
			<div class="col-xs-4">
				<button type="submit" class="btn btn-primary btn-black btn-flat">Sign in</button>
			</div>
		</div>
	</form>
</div>
