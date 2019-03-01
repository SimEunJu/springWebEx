<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://ex.co.kr/format_local_datetime" prefix="cf" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>회원 관리</title>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" 
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
</head>
<body>

<div class="container">
	<div class="user_cri">
		<input type="radio" id="user-all" name="userType" value="all-type" checked>
  		<label for="user-sleep">전체 회원</label>
		
		<input type="radio" id="user-active" name="userType" value="active">
  		<label for="user-active">활동 회원</label>
		
		<input type="radio" id="user-banned" name="userType" value="banned">
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
 			<c:forEach var="u" items="${users}">
    		<tr>
      			<th scope="row"><input type="checkbox" name="user" value="${u.username}" /></th>
      			<td>${u.username}</td>
      			<td>${u.state }</td>
      			<%-- <td>${u.visitdate }</td> --%>
      			<td>${cf:formatLocalDateTime(u.regdate, 'yyyy-MM-dd HH:mm:ss')}</td>
      			<td>${u.reportCnt }</td>
    		</tr>
    		</c:forEach>
  		</tbody>
	</table>
	<nav aria-label="Page navigation">
  		<ul class="pagination">
  			<c:if test="${pagination.prev}">
    		<li class="page-item"><a class="page-link prev" href="">&laquo;</a></li>
    		</c:if>
    		
    		<c:forEach begin="1" end="${pagination.endPage>10 ? 10 : pagination.endPage}" varStatus="idx">
    		<li class="page-item ${idx.count==1 ? 'active' : '' }"><a class="page-link class" href="${idx.count}">${idx.count}</a></li>
    		</c:forEach>
    		
    		<c:if test="${pagination.next}">
    		<li class="page-item"><a class="page-link next" href="">&raquo;</a></li>
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

<script id="pagination-hb" type="text/x-handlebars-template">
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

<script src="https://code.jquery.com/jquery-3.3.1.min.js"
	integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8=" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.1.0/handlebars.min.js"></script>
<script src="/resources/js/checkboxHandle.js"></script>
<script>
$("document").ready(function(){
	
	const csrfToken = "${_csrf.token }";
	const csrfHeader = "${_csrf.headerName }";
	$(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(csrfHeader, csrfToken);
    });
	
	const check	= checkServInitiator("user", "/board/api/admin/user");
	
	const tableRowSkeleton = document.getElementById("table-row").innerHTML;
	const tableRowTemplate = Handlebars.compile(tableRowSkeleton);
	Handlebars.registerHelper("dateFormat", function(date){
		if(date === null) return;
		return date.year+"-"+date.monthValue+"-"+date.dayOfMonth+" "+date.hour+":"+date.minute;
	});
	
	const paginationSkeleton = document.getElementById("pagination-hb").innerHTML;
	const paginationTemplate = Handlebars.compile(paginationSkeleton);
	Handlebars.registerHelper("for", function(start, end, block){
		let acc = "";
		for(let i=start; i<=end; i++){
			acc += block.fn(i);
		}
		return acc;
	});
	
	const receiverListSkeleton = document.getElementById("receiver-list-hb").innerHTML;
	const receiverListTemplate = Handlebars.compile(receiverListSkeleton);
	Handlebars.registerHelper("forArr", function(start, end, item, block){
		let acc = "";
		for(let i=start; i<=end; i++){
			acc += block.fn(item[i-1]);
		}
		return acc;
	});
	
	$('#modal button[data-dismiss]').on("click", function(){
		const modal = $('#modal');
		$("body").toggleClass("modal-open");
		modal.toggleClass("show");
		modal.css("display", "none");
		modal.attr("aria-hidden", "true");
	});
	
	let checked = [];
	let checkedPage = {};
	$(".btn-msg").on("click", function(){
		
		const modal = $("#modal");
		$("body").toggleClass("modal-open");
		modal.toggleClass("show");
		modal.css("display", "block");
		modal.attr("aria-hidden", "false");
		modal.find('textarea').trigger('focus');
		
		appendChecked($(".pagination .active").find("a").attr("href"));
		let receiverList = "";

		if($("input[value='all']").is(":checked")) receiverList = $("input[type='radio']:checked").html();
		else{
			const checkedCnt = checked.length;
			receiverList = receiverListTemplate({
				start: 1,
				end: checkedCnt>10 ? 10 : checkedCnt,
				showMsg: checkedCnt>10,
				receiverNum: checkedCnt,
				receiver: checked
			});
		}
		console.log(modal.find(".recevier"));
		modal.find(".receiver").html(receiverList);
	});
	
	$(".send").on("click", function(){
		if(checked === {}){
			alert("1명 이상 선택해주세요");
			return;
		}
		const modal = $("#modal");
		const envelope = {
			receivers: checked,
			title: modal.find(".title").val(),
			content: modal.find(".msg").val()
		}
		
		$.post({
			url: "/board/api/admin/user/msg",
			data: JSON.stringify(envelope),
			contentType: "application/json; charset=utf-8",
	        dataType: "json",
		})
			.done(function(){
				$(".msg").val("");
				$(".recevier").html("");
				checked = [];
				checkedPage = {};
				alert("메시지가 성공적으로 발송되었습니다.");
			});
	});
	
	$(".btn-ban").on("click", function(e){
		appendChecked($(".pagination .active").find("a").attr("href"));
	
		$.post({
			url: "/board/api/admin/user/ban",
			data: JSON.stringify(checked),
			contentType: "application/json; charset=utf-8",
	        dataType: "json",
		})
		.done(function(){
			checked = [];
			checkedPage = {};
			alert("선택된 회원을 정지 처리 하였습니다.");
		});
	});
	
	$(".btn-find").on("click", function(e){
		const keyword = $(this).siblings("input[type='text']").val();
		if(keyword === "" || keyword.length === 0){
			alert("사용자 이름을 입력해 주세요");
			return;
		}
		$.getJSON("/board/api/admin/user/find?keyword="+encodeURIComponent(keyword))
		.done(function(users){
			checked = [];
			checkedPage = {};
			const tableRow = tableRowTemplate(users);
			$("tbody").html(tableRow);
		})
	})
	
	$("input[type='radio']").on("click", function(e){
		
		checked = [];
		checkedPage = {};
		const userType = $(e.target).val();
		
		$.getJSON("/board/api/admin/user?type="+userType)
			.done(function(users){
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
			checked = checked.concat(collectCheckVal());
			
		}
	}
	
	$(".pagination").on("click", function(e){
		e.preventDefault();
		
		const target = e.target;
		
		e.currentTarget.find(".active").removeClass("active");
		
		page = target.attr("href");
		target.parent().addClass("active");
		
		appendChecked(page);
		
		let userQuery;
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