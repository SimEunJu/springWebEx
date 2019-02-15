<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
 
<!DOCTYPE html>
<html lang="ko">

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
 	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
 	
    <title>daily 게시판</title>    
    
	<%@include file="../include/cssFiles.jsp" %>

</head>

<body>

<form role="form">
	<input type="hidden" name="page" value="${pageMaker.cri.page }">
	<input type="hidden" name="perPageNum" value="${pageMaker.cri.perPageNum }">
</form>

<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="myModalLabel">알림</h4>
            </div>
            <div class="modal-body">
               	처리가 완료되었습니다.
            </div>
            <div class="modal-footer">
            	<a rel="modal:close" class="close-modal ">
                	<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            	</a>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- /.modal -->

<%@ include file="../include/searchBar.jsp" %>	

<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default">
            <div class="panel-heading">
                Board
            </div>
            <!-- /.panel-heading -->
            <div class="panel-body">
                <table width="100%" class="table table-striped table-bordered table-hover" id="dataTables-example">
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
                    		<tr class="odd gradeX">
                    			<td>${board.bno}</td>
                    			<td><a href="/board/list/${board.bno}/${pageMaker.cri.makeSearch()}">${board.title}</a></td>
                    			<td>${board.writer}</td>
                    			<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${board.regdate}"></fmt:formatDate></td>
                    			<td>${board.viewcnt}</td>
                    		</tr>
                   		</c:forEach>
                        
                    </tbody>
                </table>
                <!-- /.table-responsive -->
            </div>
            <!-- /.panel-body -->
        </div>
        <!-- /.panel -->
    </div>
    <!-- /.col-lg-12 -->
</div>
<!-- /.row -->
	
<%@ include file="../common/pagination.jsp" %>

<%@ include file="../include/jsFiles.jsp" %>

</body>

<script type="text/javascript">
	$("document").ready(function(){
		
		var msg = "${msg}";
		if(msg === "success" && !history.state){
			$(".modal-body").html("게시글이 등록되었습니다.");
			let modal = $("#myModal");
			modal.removeClass("fade");
			modal.modal("show");
		}
		
		history.replaceState({}, null, null);
		
		var formObj = $("form[role='form']");
		
		$("#searchBtn").on("click", function(e){
			
			if(!$("select option:selected").val()){
				alter("검색 종류를 입력하세요");
				return;
			}
			
			if(!$("input[name='keyword']").val()){
				alter("검색 종류를 입력하세요");
				return;
			}
			
			window.location.href = "/board/daily"+"${pageMaker.cri.makeQuery()}"
									+"&searchType=" + $("select option:selected").val()
									+"&keyword=" + encodeURIComponent($("input[name='keyword']").val());
		});
		
		$("#newBtn").on("click", function(){
			window.location.href = "/board/daily/new";
		});
		
	})
</script>