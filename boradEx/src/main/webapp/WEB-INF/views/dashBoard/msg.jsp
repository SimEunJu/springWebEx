<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ include file="../common/header.jsp"%>

<div class="container">
	<div class="row my-2 p-2 border">
		<div class="col">
			<input type="checkbox" id="all-mem" name="msg" value="all" /> 
			<label for="all-mem">전체 회원</label>
		</div>
		
		<div class="float-right">
			<button type="button" class="btn btn-outline-primary" id="btn-msg">메시지 작성</button>
			<button type="button" class="btn btn-outline-warning" id="btn-del">삭제</button>
		</div>
	</div>
	
	<table class="table">
  		<thead class="thead-dark">
    		<tr>
      			<th scope="col"><input type="checkbox" name="msg" value="all-showed" /></th>
     			<th scope="col">보낸 이</th>
      			<th scope="col">제목</th>
      			<th scope="col">날짜</th>
    		</tr>
  		</thead>
  		
  		<tbody>
  			<c:forEach var="msg" items="${msges}" varStatus="i">
    			<tr>
      				<th scope="row-1"><input type="checkbox" name="msg" value="${msg.msgNo}" /></th>
      				<td class="row-2" class="sender">${msg.sender}</td>
      				<td class="row-6 title" style="font-weight: ${msg.receiverReadFlag ? '' : 'bold'}">${msg.title}</td>
      				<td class="row-3">${cf:formatLocalDateTime(msg.regdate, 'yyyy-MM-dd HH:mm:ss')}</td>
    			</tr>
			</c:forEach>
  		</tbody>
	</table>
	
	<c:if test="${empty msges}">
    	<div class="row-12 p-2 border text-center">등록된 알림이 없습니다.</div>
    </c:if>	
    
	<nav aria-label="Page navigation">
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
        	<p>보내는 이 : <span class="sender">${user.username}</span></p>
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

<%@ include file="../common/footer.jsp"%>

<script id="table-row" type="text/x-handlebars-template">
{{#each msg}}	
	<tr>
		<th scope="row-1"><input type="checkbox" name="msg" value="{{msgNo}}" /></th>
      	<td class="row-2">{{sender}}</td>
      	<td class="row-6" style="font-weight: {{#if receiverReadFlag}} '' {{else}} 'bold' {{/if}}">{{title}}</td>
      	<td class="row-3">{{#dateFormat regdate}}</td>
     </tr>
{{/each}}
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
<script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.1.0/handlebars.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" 
	integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
<script src="/resources/js/modal.js"></script>
<script>
// 1. send msg 2. delete msg 3. read msg 4. pagination
$("document").ready(function(){
	
	// 리팩토링, 읽음 표시, 삭제
	
	const csrfToken = "${_csrf.token }";
	const csrfHeader = "${_csrf.headerName }";
	$(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(csrfHeader, csrfToken);
    });
	
	addHandlebarHelper();
	
	const tableRowSkeleton = document.getElementById("table-row").innerHTML;
	const tableRowTemplate = Handlebars.compile(tableRowSkeleton);
	
	const receiverListSkeleton = document.getElementById("receiver-list-hb").innerHTML;
	const receiverListTemplate = Handlebars.compile(receiverListSkeleton);
	
	const check = new Check("msg");
	const msgModal = new MsgModal();
	const pagination = new Pagination();
	
	$("#btn-msg").on("click", function(){
		msgModal.openMsgModal(check, pagination.page, receiverListTemplate);
	});

	$(".modal-send").on("click", function(){
		msgModal.sendMsg(check.list, "/board/user/msg");
	});
	
	check.collectCheckVal = function(){
		let msgNo = [];
		let user = [];
		
		if(this.allCheck.is(":checked")){
			const flag = "all-check";
			msgNo.push(flag);
			user.push(flag);
		}
		else{
			this.tbody.find($("input[type='checkbox']")).each(function(idx, c){
				const check = $(c);
				if(check.is(":checked") && check.val() !== "all-showed"){
					msgNo.push(check.val());
					// 보낸 이 이름
					user.push(check.parent().next().text());
				}
			});
		}
		return {msgNo, user};
	}
	
	check.flatObjToList = function(obj){
		return flatObjToListOpt(obj, 'user');
	}

	function flatObjToListOpt(obj, opt){
		let data = [];
		for(let prop in obj){
			data = data.concat(obj[prop][opt]);
		}
		check.list = data;
		return data;
	}
	
	check.tbody.on("click", "tr .title", function(e){
		const target = $(e.target);
		ajax({
			url: "/board/user/msg/"+target.siblings("th").children("input").val(),
			method: "get",
			
		}, function(res){
			msgModal.toggleModal();
			
			msgModal.title.val(res.title);
			msgModal.msg.val(res.content)
			msgModal.sender.html(res.sender);
			msgModal.receiver.html(res.receiver);
			
			target.css("font-weight","inherit");
			target.attr("data-read", true);			
		})
	});
	
	
	$("#btn-del").on("click", function(e){
		check.appendCheckVal(pagination.page);
		const msgNoList = flatObjToListOpt(check.repo, 'msgNo');
		ajax({
			url: "/board/user/msg/del",
			method: "post",
			data: JSON.stringify(msgNoList),
			contentType: "application/json; charset=utf-8"
		}, function(res){
			check.tbody.html(tableRowTemplate(res));
		});
	});
	
	function ajax(setting, callback){
		$.ajax({
			url: setting.url,
			method: setting.method,
			data: setting.data,
			dataType: "text",
			contentType: setting.contentType
		}).done(function(res){
			callback(JSON.parse(res));
		}).fail(showAjaxError);
		
	}
});
</script>
</body>
</html>