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
	<table class="table">
  		<thead class="thead-dark">
    		<tr>
      			<th scope="col"><input type="checkbox" name="noti" value="all" /></th>
      			<th scope="col">댓글</th>
      			<th scope="col">날짜</th>
    		</tr>
  		</thead>
  		
 		<tbody>
    		<tr>
      			<th scope="row"><input type="checkbox" name="noti[0]" value="nno" /></th>
      			<td>댓글입니다.</td>
      			<td>2019/2/21</td>
    		</tr>
  		</tbody>
	</table>
</div>

</body>
</html>