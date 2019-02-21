<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>메시지</title>
</head>
<body>

<div class="container">
	<table class="table">
  		<thead class="thead-dark">
    		<tr>
      			<th scope="col"><input type="checkbox" name="msg" value="all" /></th>
     			<th scope="col">보낸 이</th>
      			<th scope="col">제목</th>
      			<th scope="col">날짜</th>
    		</tr>
  		</thead>
  		
 		<tbody>
    		<tr>
      			<th scope="row"><input type="checkbox" name="msg[0]" value="msg_no" /></th>
      			<td>Mark</td>
      			<td>관리자님께 문의드립니다.</td>
      			<td>2019/2/21</td>
    		</tr>
  		</tbody>
	</table>
</div>

</body>
</html>