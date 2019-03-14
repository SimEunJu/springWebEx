<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="cf" uri="http://ex.co.kr/format_local_datetime"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row mt-2">
	<div class="col">
		<div class="row border rounded m-2 p-1">
			<header class="col-12 p-3 bg-light">사용자 현황</header>

			<div class="col-lg-6">
				<canvas id="chart_user_inout" width="400" height="400"></canvas>
				<div class="range row justify-content-center m-1">
					<input type="radio" name="inout-cri" value="d" checked>일</input> 
					<input type="radio" name="inout-cri" value="w">주</input> 
					<input type="radio" name="inout-cri" value="m">월</input>
				</div>
			</div>

			<div class="col-lg-6">
				<canvas id="chart_user_visit" width="400" height="400"></canvas>
				<div class="range row justify-content-center m-1">
					<input type="radio" name="visit-cri" value="d" checked>일</input> 
					<input type="radio" name="visit-cri" value="w">주</input> 
					<input type="radio" name="visit-cri" value="m">월</input>
				</div>
			</div>
		</div>

		<div class="row border rounded m-2 p-1">
			<header class="col-12 p-3 bg-light">게시글 현황</header>

			<div class="col-lg-6">
				<canvas id="chart_user_board" width="400" height="400"></canvas>
				<div class="range row justify-content-center m-1">
					<input type="radio" name="board-cri" value="d" checked>일</input> 
					<input type="radio" name="board-cri" value="w">주</input> 
					<input type="radio" name="board-cri" value="m">월</input>
				</div>
			</div>

			<div class="col-lg-6">
				<p>인기 게시글</p>
				<table class="table">
					<thead class="thead-dark">
						<tr>
							<th scope="col">#</th>
							<th scope="col">제목</th>
							<th scope="col">글쓴이</th>
							<th scope="col">조회수</th>
							<th scope="col">날짜</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="post" items="${hotPost}">
							<tr>
								<th scope="row">${post.bno}</th>
								<td>${post.title}</td>
								<td><a href="/board/daily/${post.bno}">${post.writer}</a></td>
								<td>${post.viewcnt}</td>
								<td>${cf:formatLocalDateTime(post.regdate, 'yyyy-MM-dd HH:mm:ss')}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>


<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.4.0/Chart.min.js"></script>
<script>
const userJoinCnt = ${userJoinCnt};
const userLeaveCnt = ${userLeaveCnt};
const postCnt = ${postCnt};
const visitCnt = ${visitCnt};
</script>
