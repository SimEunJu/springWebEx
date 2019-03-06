<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="../common/header.jsp"%>

<div class="container">
	<form role="form">
		<input type="hidden" name="page" value="${pagination.cri.page }">
		<input type="hidden" name="perPageNum"
			value="${pagination.cri.perPageNum }">
	</form>

	<div class="modal fade" id="modal" tabindex="-1" role="dialog">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					
    			</div>
  			</div>
		</div>
	</div>
	<%@ include file="../include/searchBar.jsp"%><div class="row">
		<div class="col-lg-12">
			<table class="table table-hover">
				<thead>
					<tr>
						<th>번호</th>
						<th>제목</th>
						<th>작성자</th>
						<th>작성일</th>
						<th>조회수</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="board" items="${boardList}">
						<tr>
							<td>${board.bno}</td>
							<td>
								<a href="/board/daily/${board.bno}/${pagination.cri.makeSearch()}">${board.title}</a>
								[<a href="javascript:void(0)">${board.replyCnt}</a>]
							</td>
							<td>${board.writer}</td>
							<td>${cf:formatLocalDateTime(board.regdate, 'yyyy-MM-dd HH:mm:ss')}</td>
							<td>${board.viewcnt}</td>
						</tr>
					</c:forEach>

				</tbody>
			</table>
		</div>
	</div>
	
	<%@ include file="../common/pagination.jsp"%>

</div>

<%@ include file="../common/footer.jsp"%>

<script type="text/javascript">
	$("document").ready(function(){
		
		const msg = "${msg}";
		if(msg === "success" && !history.state){
			$(".modal-body").html("게시글이 등록되었습니다.");
			const modal = $('#modal');
			$("body").toggleClass("modal-open");
			modal.toggleClass("show");
			modal.css("display", "none");
			modal.attr("aria-hidden", "true");
		}
		
		history.replaceState({}, null, null);
		
		$("#searchBtn").on("click", function(e){
			
			if(!$("select option:selected").val()){
				alter("검색 종류를 입력하세요");
				return;
			}
			
			if(!$("input[name='keyword']").val()){
				alter("검색 종류를 입력하세요");
				return;
			}
			
			window.location.href = "/board/daily"+"${pagination.cri.makeQuery()}"
									+"&searchType=" + $("select option:selected").val()
									+"&keyword=" + encodeURIComponent($("input[name='keyword']").val());
		});
		
		$("#newBtn").on("click", function(){
			window.location.href = "/board/daily/new";
		});
		
	})
</script>