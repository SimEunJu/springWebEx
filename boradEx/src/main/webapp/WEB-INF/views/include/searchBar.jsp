<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="row">
	<div class="col-lg">
		<select class="custom-select" name="searchType">
			<option value="n" ${cri.searchType eq null?'selected':''}>검색 옵션</option>
			<option value="t" ${cri.searchType eq 't'?'selected':''}>제목</option>
			<option value="c" ${cri.searchType eq 'c'?'selected':''}>내용</option>
			<option value="w" ${cri.searchType eq 'w'?'selected':''}>글쓴이</option>
			<option value="tc" ${cri.searchType eq 'tc'?'selected':''}>제목 또는 내용</option>
			<option value="tcw" ${cri.searchType eq 'tcw'?'selected':''}>제목 또는 내용 또는 글쓴이</option>
		</select>
	</div>
	<div class="col-lg">
		<input class="form-control mr-sm-2" type="search" aria-label="Search" value="${cri.keyword eq null ? '' : cri.keyword}">
	</div>
	<div class="col-lg">
		<button class="btn btn-outline-success my-2 my-sm-0" type="submit">검색</button>
		<button id="newBtn" class="btn btn-primary pull-right">글쓰기</button>
	</div>
</div>
