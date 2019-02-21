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
    		<label for="id">아이디</label>
    		<input type="text" class="form-control" id="id" readonly>
  		</div>
  		<div class="form-group">
    		<label for="state">상태</label>
    		<input type="text" class="form-control" id="state" readonly>
  		</div>
  		<div class="form-group">
    		<label for="reportCnt">방은 경고 횟수</label>
    		<input type="text" class="form-control" id="reportCnt" readonly>
  		</div>
  		<div class="form-group">
    		<label for="regdate">가입일</label>
    		<input type="text" class="form-control" id="regdate" readonly>
  		</div>
  		<button type="submit" class="btn btn-warning">탈퇴</button>
	</form>
</div>

</body>
</html>