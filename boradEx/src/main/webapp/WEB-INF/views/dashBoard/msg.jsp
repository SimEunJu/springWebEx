<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ include file="../common/header.jsp"%>

<div class="container">
	<div class="btns row justify-content-end">
		<button type="button" class="btn btn-outline-primary pull-right" id="msg-btn">메시지 작성</button>
		<button type="button" class="btn btn-outline-warning" id="del-btn">삭제</button>
	</div>
	
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
      				<td class="row-2" class="sender">${msg.sender}</td>
      				<td class="row-6" style="font-weight: ${msg.receiverReadFlag ? '' : 'bold'}">${msg.title}</td>
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
		<th scope="row-1"><input type="checkbox" name="msg" value="{{msgNo}}" /></th>
      	<td class="row-2">{{sender}}</td>
      	<td class="row-6" style="font-weight: {{#if receiverReadFlag}} '' {{else}} 'bold' {{/if}}}">{{title}}</td>
      	<td class="row-3">{{#formatDate regdate}}</td>
     </tr>
{{/each}}
</script>

<script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.1.0/handlebars.min.js"></script>
<script src="/resources/js/checkboxHandle.js"></script>
<script>
// 1. send msg 2. delete msg 3. read msg 4. pagination
$("document").ready(function(){
	
	let checkedRepo = {};
	let checked = [];
	const checkObj = checkServ(keyword, url);
	
	function collectCheckVal(){
		let data = [];
		let users = [];
		
		if(($("input[value='all']").is(":checked") === true) && (confirm("모두 선택하시겠습니까?")) === true) url += "?type=all";
		else{
			$("input[type='checkbox']").each(function(idx, c){
				const check = $(c);
				if(check.is(":checked") && check.val() !== "all-showed"){
					data.push(check.val());
					users.push(check.parent().next().text());
				}
			})
		}
		return [data, users];
	}
	
	function appendChecked(repo, page){
		repo[page] = collectCheckVal();
		return repo;
	}

	function flatObjToList(obj, list, opt){
		let idx = 0;
		if(opt === 'send') idx = 1; 
		list = [];
		for(let page in obj){
			list = list.concat(obj[page][idx]);
		}
		return list;
	}
	const modalObj = {
			modal: $("#modal"),
			textarea: this.modal.find("textarea")
			footer: this.modal.find(".modal-footer")
	}
	
	modalObj.footer("click", function(e){
		const target = e.target;
		if(target.hasClass("send")){
			setting = {
					url: "/board/daily/msg",
					method: "DELETE",
					data: JSON.stringify(checked)
				};
		}
		
	})
	$(".btns").on("click", function(e){
		appendChecked(checkedRepo, $(".pagination .active").find("a").attr("href"));
		flatObjToList(checkedRepo, checked);
		
		const target = e.target;
		let setting;
		// msg no
		if(target.attr("id") === "msg-btn"){
			modalObj.modal.modal("toggle");
		}
		// user, msg
		else{
			let msges = {
				users: checked,
				msg: modalObj.val()
			};
			setting = {
				url: "/board/daily/msg",
				method: "POST",
				data: JSON.stringify(msges)
			};
		}
		
		ajax(setting);
	});
	function ajax(setting){
		$.ajax({
			url: setting.url,
			method: setting.method,
			data: setting.data,
			dataType: "text",
			contentType: "text/plain; charset=UTF-8"
		}).done(function(){
			alert("완료되었습니다.");
		}).fail(function(qXHR, textStatus){
			console.error(qXHR, textStatus);
		})
		
	}
});
</script>
</body>
</html>