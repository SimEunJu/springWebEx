<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ include file="../common/header.jsp"%>

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
  			<c:forEach var="msg" items="${msges}" varStatus="i">
    			<tr>
      				<th scope="row-1"><input type="checkbox" name="msg" value="${msg.msgNo}" /></th>
      				<td class="row-2">${msg.sender}</td>
      				<td class="row-6">${msg.title}</td>
      				<td class="row-3">${cf:formatLocalDateTime(msg.regdate, 'yyyy-MM-dd HH:mm:ss')}</td>
    			</tr>
			</c:forEach>
  		</tbody>
	</table>
	
	<c:if test="${empty msges}">
    	<div class="row-12 p-2 border text-center">등록된 알림이 없습니다.</div>
    </c:if>	
	
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

<%@ include file="../common/footer.jsp"%>

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

<script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.1.0/handlebars.min.js"></script>
<script src="/resources/js/checkboxHandle.js"></script>
</body>
</html>