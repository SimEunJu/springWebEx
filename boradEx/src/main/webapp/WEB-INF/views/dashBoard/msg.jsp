<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://ex.co.kr/format_local_datetime" prefix="cf" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>메시지</title>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" 
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
</head>
</head>
<body>

<div class="container">
	<button type="button" class="btn btn-outline-warning pull-right">메시지 보내기</button>
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
    			<c:forEach var="msg" items="${msges}" varStatus="i">
      				<th scope="row"><input type="checkbox" name="msg" value="${msg.msgNo}" /></th>
      				<td>${msg.sender}</td>
      				<td>${msg.title}</td>
      				<td>
      					<td>${cf:formatLocalDateTime(msg.regdate, 'yyyy-MM-dd HH:mm:ss')}</td>
      				</td>
    			</c:forEach>
    		</tr>
  		</tbody>
	</table>
</div>
<div class="modal" tabindex="-1" role="dialog">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">메시지 보내기</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <div class="msg-host">
        	<sec:authentication var="user" property="principal" />
        	<p>보내는 이 : <span class="sender">${user.username}</span></p>
        	<p>받는 이 : <div class="receiver"></div></p>
        </div>
        <textarea class="msg"></textarea>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary send">보내기</button>
      </div>
    </div>
  </div>
</div>

<script src="table-row" type="text/x-handlebars-template">
{{#each msg}}	
	<tr>
		<th scope="row"><input type="checkbox" name="user" value="{{username}}" /></th>
      	<td>{{sender}}</td>
      	<td>{{title}}</td>
      	<td>{{#dateFormat regdate}}</td>
     </tr>
{{/each}}
</script>
<script src="https://code.jquery.com/jquery-3.3.1.min.js"
	integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8=" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.1.0/handlebars.min.js"></script>
<script src="/resources/js/checkboxHandle.js"></script>
</body>
</html>