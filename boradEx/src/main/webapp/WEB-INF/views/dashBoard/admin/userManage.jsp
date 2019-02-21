<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>회원 관리</title>
</head>
<body>

<div class="container">
	<div class="user_cri">
		<input type="radio" id="user-all" name="userType" value="all" checked>
  		<label for="user-sleep">전체 회원</label>
		
		<input type="radio" id="user-active" name="userType" value="active">
  		<label for="user-active">활동 회원</label>
		
		<input type="radio" id="user-banned" name="userType" value="banned">
  		<label for="user-banned">정지 회원</label>
		
		<input type="radio" id="user-sleep" name="userType" value="sleep">
  		<label for="user-sleep">휴면 회원</label>
  		
		<input type="radio" id="user-leave" name="userType" value="leave">
  		<label for="user-leave">탈퇴 회원</label>
	</div>
	
	<table class="table">
  		<thead class="thead-dark">
    		<tr>
      			<th scope="col"><input type="checkbox" name="noti" value="all" /></th>
      			<th scope="col">회원 아이디</th>
      			<th scope="col">상태</th>
      			<th scope="col">최근 방문일</th>
      			<th scope="col">회원 가입일</th>
      			<th scope="col">경고 횟수</th>
    		</tr>
  		</thead>
  		
 		<tbody>
    		<tr>
      			<th scope="row"><input type="checkbox" name="noti[0]" value="nno" /></th>
      			<td>wndmstla</td>
      			<td>활동중</td>
      			<td>2019/02/21</td>
      			<td>2018/08/18</td>
      			<td>0</td>
    		</tr>
  		</tbody>
	</table>
</div>

</body>
</html>