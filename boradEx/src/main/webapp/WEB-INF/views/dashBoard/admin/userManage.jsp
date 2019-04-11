<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cf" uri="http://ex.co.kr/format_local_datetime"%>

<div class="col-md-9">
	<div class="row border my-2 p-2">
		<div class="user_cri col-12">

			<label>회원 종류</label> 
			
			<input type="radio" id="user-all" name="userType" value="all-type" checked> 
			<label for="user-sleep">전체</label> 
			
			<input type="radio" id="user-active" name="userType" value="active"> 
			<label for="user-active">활동</label>
			
			<input type="radio" id="user-banned" name="userType" value="banned">
			<label for="user-banned">정지</label> 
			
			<input type="radio" id="user-banned" name="userType" value="report"> 
			<label for="user-banned">경고</label> 
			
			<input type="radio" id="user-sleep" name="userType" value="sleep"> 
			<label for="user-sleep">휴면</label>
			
			<input type="radio" id="user-leave" name="userType" value="leave">
			<label for="user-leave">탈퇴</label>
			
		</div>
		
		<div class="col-12">
			<div class="float-right">
				<input type="text" name="name" placeholder="회원명" for="#mem-search"/>
				<button type="button" class="btn btn-outline-primary btn-find" id="mem-search">찾기</button>
			</div>
		</div>
	</div>
	<div class="option row my-2 p-2 border">
		<select name="select" class="ml-auto mr-2">
  			<option value="" selected>--------</option> 
 			<option value="active">활동</option>
  			<option value="banned">정지</option>
  			<option value="report">경고</option>
  			<option value="sleep">휴면</option>
  			<option value="leave">탈퇴</option>
		</select>
		<button type="button" class="btn btn-outline-danger btn-usertype">회원 상태 변경</button>
	</div>
	<div class="option row my-2 p-2 border">	
		
			<div class="col">
				<input type="checkbox" id="all-mem" name="user" value="all" /> 
				<label for="all-mem">전체 회원</label>
			</div>
	
			<div class="align-self-end">
				<button type="button" class="btn btn-outline-warning btn-msg">메시지 전송</button>
				<button type="button" class="btn btn-outline-success btn-mail">메일 전송</button>
			</div>
		
	</div>
	
	<table class="table table-hover">
  		<thead class="thead-dark">
    		<tr>
      			<th scope="col"><input type="checkbox" name="user" value="all-showed" /></th>
      			<th scope="col">회원 아이디</th>
      			<th scope="col">상태</th>
      			<th scope="col">최근 방문일</th>
      			<th scope="col">회원 가입일</th>
      			<th scope="col">경고 횟수</th>
    		</tr>
  		</thead>
  		
 		<tbody>
 			<c:forEach var="u" items="${users}">
    		<tr>
      			<th scope="row"><input type="checkbox" name="user" value="${u.username}" /></th>
      			<td>${u.username}</td>
      			<td>${u.state }</td>
      			<td>visitdate</td>
      			<%-- <td>${u.visitdate }</td> --%>
      			<td>${cf:formatLocalDateTime(u.regdate, 'yyyy-MM-dd HH:mm:ss')}</td>
      			<td>${u.reportCnt }</td>
    		</tr>
    		</c:forEach>
  		</tbody>
	</table>
	<nav aria-label="Page navigation" class="row justify-content-center">
  		<ul class="pagination">
  			<c:if test="${pagination.prev}">
    		<li class="page-item prev"><a class="page-link" href="">&laquo;</a></li>
    		</c:if>
    		
    		<c:forEach begin="1" end="${pagination.endPage>10 ? 10 : pagination.endPage}" varStatus="idx">
    		<li class="page-item ${idx.count==1 ? 'active' : '' }"><a class="page-link class" href="${idx.count}">${idx.count}</a></li>
    		</c:forEach>
    		
    		<c:if test="${pagination.next}">
    		<li class="page-item next"><a class="page-link" href="">&raquo;</a></li>
  			</c:if>
  		</ul>
	</nav>
</div>
<div class="modal fade" id="modal" tabindex="-1" role="dialog">
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
        	<p>보내는 이 : <span class="sender">관리자</span></p>
        	<p>받는 이 : <div class="receiver"></div></p>
        </div>
        <input type="text" class="title col-12 mb-2" placeholder="제목을 입력해주세요"/>
        <textarea class="msg col-12"></textarea>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary modal-send">보내기</button>
      </div>
    </div>
  </div>
</div>
<script id="pagination-hb" type="text/x-handlebars-template">
{{#if prev}}
	<li class="page-item"><a class="page-link" href="">&laquo;</a></li>
{{/if}}
{{for startPage endPage}}
	<li class="page-item"><a class="page-link" href="{{this}}">{{this}}</a></li>
{{/for}}
{{#if next}}
	<li class="page-item"><a class="page-link" href="">&raquo;</a></li>
{{/if}}
</script>
<script id="receiver-list-hb" type="text/x-handlebars-template">
<ul>
	{{#forArr start end receiver}}
		<li>{{this}}</li>
	{{/forArr}}	
	{{#if showMsg}}
		<li>포함 {{receiverNum}}명</li>
	{{/if}}
</ul>
</script>
<script id="table-row" type="text/x-handlebars-template">
{{#each users}}	
	<tr>
		<th scope="row"><input type="checkbox" name="user" value="{{username}}" /></th>
      	<td>{{username}}</td>
      	<td>{{state}}</td>
		<td>visitdate</td>
      	<td>{{dateFormat regdate}}</td>
      	<td>{{reportCnt}}</td>
     </tr>
{{/each}}
</script>

<script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.1.0/handlebars.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" 
	integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
<script src="/resources/js/utils/common.js"></script>
