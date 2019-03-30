<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>내가 쓴 글</title>
</head>
<body>

<div class="container">
	<form>
  		<div class="form-group">
    		<label for="id">이메일</label>
    		<input type="text" class="form-control" value="${user.username}" id="id" readonly>
  		</div>
  		<div class="form-group">
    		<label for="state">상태</label>
    		<input type="text" class="form-control" value="${user.state}" id="state" readonly>
  		</div>
  		<div class="form-group">
    		<label for="reportCnt">방은 경고 횟수</label>
    		<input type="text" class="form-control" value="${user.reportCnt}" id="reportCnt" readonly>
  		</div>
  		<div class="form-group">
    		<label for="regdate">가입일</label>
    		<input type="text" class="form-control" value="${user.regdate}" id="regdate" readonly>
  		</div>
  		<button type="submit" class="btn btn-warning btn-leave">탈퇴</button>
	</form>
</div>

</body>
</html>