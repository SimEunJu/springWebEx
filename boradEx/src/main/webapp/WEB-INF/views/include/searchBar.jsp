<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<table class="table table-bordered">
  <tr>
    <th>Search Board</th>
  </tr>
  <tr>
    <td>
    	<select name="searchType">
			<option value="n" ${cri.searchType==null?'selected':''}>---</option>
			<option value="t" ${cri.searchType eq 't'?'selected':''}>Title</option>
			<option value="c" ${cri.searchType eq 'c'?'selected':''}>Content</option>
			<option value="w" ${cri.searchType eq 'w'?'selected':''}>Writer</option>
			<option value="tc" ${cri.searchType eq 'tc'?'selected':''}>Title or Content</option>
			<option value="cw" ${cri.searchType eq 'cw'?'selected':''}>Content or Writer</option>
			<option value="tcw" ${cri.searchType eq 'tcw'?'selected':''}>Title or Content or Writer</option>
		</select>
		<input type="text" name="keyword" placeholder="Enter keyword..." value="${cri.keyword == null ? "" : cri.keyword }">
		<button id="searchBtn" class="btn btn-primary">Search</button>
		<button id="newBtn" class="btn btn-primary">New Board</button>
    </td>
  </tr>
</table>
<hr>
