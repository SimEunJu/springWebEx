<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>회원 관리</title>
</head>
<body>

// 경고 회원 정지 회원을 지정할 수 있도록

<div class="container">
	<div class="user_cri">
		<input type="radio" id="user-all" name="userType" value="all-type" checked>
  		<label for="user-sleep">전체 회원</label>
		
		<input type="radio" id="user-active" name="userType" value="active">
  		<label for="user-active">활동 회원</label>
		
		<input type="radio" id="user-banned" name="userType" value="report">
  		<label for="user-banned">정지 회원</label>
		
		<input type="radio" id="user-banned" name="userType" value="report">
  		<label for="user-banned">경고 회원</label>
		
		<input type="radio" id="user-sleep" name="userType" value="sleep">
  		<label for="user-sleep">휴면 회원</label>
  		
		<input type="radio" id="user-leave" name="userType" value="leave">
  		<label for="user-leave">탈퇴 회원</label>
	</div>
	<div class="option">
		<label>전체 선택 선택</label>
		<input type="checkbox" name="user" value="all" /> 
		
		<label>회원 검색</label>
		<input type="text" name="name" />
		<button type="button" class="btn btn-outline-primary btn-find">찾기</button>
		
		<button type="button" class="btn btn-outline-warning btn-msg">메시지 보내기</button>
		
		<button type="button" class="btn btn-outline-danger btn-ban">정지 회원으로 설정</button>
	</div>
	<table class="table">
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
 			<c:forEach var="user" items="users">
    		<tr>
      			<th scope="row"><input type="checkbox" name="user" value="${user.username}" /></th>
      			<td>${user.username}</td>
      			<td>${user.state }</td>
      			<td>${user.regdate }</td>
      			<td>${user.visitdate }</td>
      			<td>${user.reportCnt }</td>
    		</tr>
    		</c:forEach>
  		</tbody>
	</table>
	<nav aria-label="Page navigation">
  		<ul class="pagination">
  			<c:if test="${pageMaker.prev}">
    		<li class="page-item"><a class="page-link prev" href="">&laquo;</a></li>
    		</c:if>
    		
    		<c:forEach begin=1 end="${pageMaker.endPage>10 ? 10 : pageMaker.endPage}" varStatus="idx">
    		<li class="page-item"><a class="page-link class" href="${varStatus.count}">${idx}</a></li>
    		</c:forEach>
    		
    		<c:if test="${pageMaker.next}">
    		<li class="page-item"><a class="page-link next" href="">&raquo;</a></li>
  			</c:if>
  		</ul>
	</nav>
</div>
<div class="modal" id="modal" tabindex="-1" role="dialog">
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
        <input type="text" class="title" placeholder="제목을 입력해주세요"/>
        <textarea class="msg"></textarea>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary send">보내기</button>
      </div>
    </div>
  </div>
</div>

<script src="pagination-hb" type="text/x-handlebars-template">
{{#if prev}}
<li class="page-item"><a class="page-link" href="">&laquo;</a></li>
{{/if}}
{{#for startPage endPage}}
<li class="page-item"><a class="page-link" href="{{page}}">{{page}}</a></li>
{{/each}]
{{#for}}
<li class="page-item"><a class="page-link" href="">&raquo;</a></li>
{{/if}}
</script>
<script src="receiver-list-hb" type="text/x-handlebars-template">
<ul>
	{{#forArr start, end, msgData}}
	<li>{{this}}</li>
	{{#if showMsg}}
		<li>포함 {{msgNum}}명</li>
	{{/if}}
	{{/forArr}}
</ul>
</script>
<script src="table-row" type="text/x-handlebars-template">
{{#each users}}	
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

<script src="/checkboxHandle.js"></script>

<script>
$("document").ready(function(){
	const check	= checkServInitiator("user", "/board/api/admin/user", context);
	
	const tableRowSkeleton = document.getElementById("table-row").innerHTML;
	const tableRowTemplate = Handlebars.compile(tableRowSkeleton);
	Handlebars.registerHelper("dateFormat", function(date){
		return new Date(date).toJSON().replace("z", " ").substring(0,16);
	});
	
	const paginationSkeleton = document.getElementById("pagination-hb").innerHTML;
	const paginationTemplate = Handlebars.compile(paginationSkeleton);
	Handlebars.registerHelper("for", function(start, end, block){
		let acc = "";
		for(let i=start; i<=end; i++){
			acc += block.fn(i);
		}
	});
	
	const receiverListSkeleton = document.getElementById("receiver-list-hb").innerHTML;
	const receiverListTemplate = Handlebars.compile(receiverListSkeleton);
	Handlebars.registerHelper("forArr", function(start, end, item, block){
		let acc = "";
		for(let i=start; i<=end; i++){
			acc += block.fn(item[i]);
		}
	});
	
	$('#modal').on('shown.bs.modal', function () {
		  $('textarea').trigger('focus')
	});
	
	let checked = [];
	let checkedPage = {};
	$(".msg-btn").on("click", function(){
		
		appendChecked($(".pagination .active").find("a").attr("href"));
		
		const receiverList = "";
		
		if($("input[value='all']").is(":checked")) receiverList =  $("input[type='radio']:checked").html();
		else{
			const msgCnt = msgData.length;
			receiverList = receiverListTemplate({
				start: 1,
				end: msgCnt>10 ? 10 : msgCnt,
				showMsg: msgCnt>10,
				msgNum: msgCnt,
				msgData: msgData
			});
		}
		
		$(".recevier").html(receiverList);
		
		msgData = msgData.concat(checked);
	});
	
	$(".send").on("click", function(){
		if(checked === {}){
			alert("1명 이상 선택해주세요");
			return;
		}
		const modal = $("modal");
		const envelope = {
			receivers: checked,
			title: modal.find(".title").val(),
			content: modal.find(".msg").val()
		}
		$.post("/board/api/admin/user/msg", data.stringify(envelope))
			.done(function(){
				$(".msg").val("");
				$(".recevier").html("");
				checked = [];
				alert("메시지가 성공적으로 발송되었습니다.");
			});
	});
	
	$(".btn-ban").on("click", function(e){
		appendChecked($(".pagination .active").find("a").attr("href"));
		$.post("/board/api/admin/user/ban", data.stringify(msgData))
		.done(function(){
			checked = [];
			alert("선택된 회원을 정지 처리 하였습니다.");
		});
	});
	
	$(".btn-find").on("click", function(e){
		const keyword = this.sibilings("input[type='text']").val();
		if(keyword === "" || keyword.length === 0){
			alert("사용자 이름을 입력해 주세요");
			return;
		}
	})
	
	$("input[type='radio']").on("click", function(e){
		
		checked = [];
		const userType = e.currentTarget.val();
		
		$.getJSON("/board/api/admin/user?type="+userType)
			.done(function(users){
				msgData = [];
				const tableRow = tableRowTemplate(users);
				$("tbody").html(tableRow);
			})
	});
	
	const page = 1;
	const perPage = 10;
	function makeQuery(extra){
		let userQuery = "";
		for(let q in extra){
			userQuery = "&"+q+"="+extra.q;
		}
		return "page="+page+"&perPageNum="+perPageNum+userQuery;
	}
	
	function appendChecked(page){
		if(!checkedPage.hasOwnProperty(page)){
			checkedPage[page] = page;
			checked.concat(collectCheckVal());
		}
	}
	
	$(".pagination").on("click", function(e){
		e.preventDefault();
		
		const target = e.target;
		
		e.currentTarget.find(".active").removeClass("active");
		
		page = target.attr("href");
		target.parent().addClass("active");
		
		appendChecked(page);
		
		const userQuery;
		if($(target).hasClass("prev")) userQuery = {move: "prev"};
		else if($(target).hasClass("next")) userQuery = {move : "next"};
		
		const query = makeQuery(userQuery);
		
		$.getJSON("/board/api/user?"+query)
			.done(function(res){
				const tableRow = tableRowTemplate(res.users);
				$("tbody").html(tableRow);
				
				const pagination = paginationTemplate(res.pagination);
				const paginationSec = $(".pagination");
				paginationSec.html(pagination);
				paginationSec.find("a[href='"+page+"']").parent().addClass("active");
			});
	});
});
</script>
</body>
</html>