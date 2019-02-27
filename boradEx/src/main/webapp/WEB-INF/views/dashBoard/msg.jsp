<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>메시지</title>
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
    			<c:forEach var="m" items="msg" varStatus="i">
      				<th scope="row"><input type="checkbox" name="msg[${i}]" value="${m.msgNo}" /></th>
      				<td>${m.sender}</td>
      				<td>${m.title}</td>
      				<td>
      					<fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${m.regdate}" />
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
      	<td>{{username}}</td>
      	<td>{{state}}</td>
      	<td>{{regdate}}</td>
      	<td>{{#dateFormat visitdate}}</td>
      	<td>{{reportCnt}}</td>
     </tr>
{{/each}}
</script>
</body>
</html>