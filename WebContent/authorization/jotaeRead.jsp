<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../container/header.jsp"%>
<form id="formwrite">
<c:set var="doc" value="${requestScope.docvo_list}" />
<c:set var="doc_conf" value="${requestScope.doc_detail_list}"/>
<c:set var="session" value="${sessionScope.loginInfo}"></c:set>	
<div>
	<div class="title" align="center">
		<h2>조퇴계/병가</h2><!--doc_kind값에 따라 if문으로 보여주고 말고 결정할거임 둘중하나 보여주는거 -->
	</div>
	<div class="table">
		<table class="table table-bordered">
			<tr>
				<th>문서번호</th>
				<td>${doc.doc_num}</td>
				<th rowspan="2">결재</th>
					<c:forEach var="cn" items="${doc_conf}" >
					<td rowspan="2">
						<div>${cn.members.name}</div>
						<c:choose>
					 	<c:when test="${cn.acs_yn eq '0'}">
						<div>${cn.acs_yn} 대기</div> 
						</c:when>
					 	<c:when test="${cn.acs_yn eq '1'}">
						<span style="font-weight:bold;color:red">${cn.acs_yn}결재</span>
						</c:when>
						</c:choose>
					</td>
						</c:forEach>
						</tr>
			<tr>
				<th>문서종류</th>
				<td>${doc.doc_kindvo.doc_name}</td>
			</tr>
			<tr>
				<th>기안일</th>
				<td>${doc.start_date}</td>
				<th>수신부서</th>
				<td>${doc.rcv_dept}</td>
			</tr>
			<tr>
				<th>기안자</th>
				<td>${doc.members.name}(${doc.gradeinfo.position_name})</td>
				<th>부서</th>
				<td colspan="3">${doc.deptinfo.dept_name},${session.name}</td>
			</tr>
			<tr>
				<th>참조자</th>
				<td colspan="5">${doc.refer}</td>
			</tr>
			<tr>
				<th>제목</th>
				<td colspan="5">${doc.doc_title}</td>
			</tr>
			<tr>
				<th>기간</th>
				<td>${doc.doc_gigan.start_date} &nbsp;~&nbsp; ${doc.doc_gigan.end_date}</td>
			</tr>

			<tr>
				<th>대체근무자</th>
				<!-- db 값 가져와야함 대체근무자 컬럼 없음 -->
					<td>null</td>
			</tr>
			<tr>
				<th>사유</th>
				<td colspan = "12" height ="200">${doc.doc_content}</td>
			</tr>
			<tr>
				<th>첨부파일</th>
				<td colspan="5"><input type="text">

					<button>첨부파일</button></td>
			</tr>


			<tr>
				<td colspan="6" align="center">상기와 같은 사유로 인하여 조퇴계를 제출하오니
					재가바랍니다.</td>
			</tr>
			<tr>	
					<td colspan="6" align="center">
					<c:set var="sname" value="${session.name}" />
					<c:set var="dname" value="${doc.members.name}" />
					<c:choose>
					<c:when test="${doc.doc_state ne '2'}">
						<c:if test="${sname eq dname}">
						<input type="button" value="수정" id="edit" onclick = "editdocnum('${doc.doc_num}')"> 
					    <input type="button" value="삭제" id="del" onclick = "deldocnum('${doc.doc_num}')">
						</c:if>
						</c:when>
					</c:choose>
						<input type="button" value="승인" id="ok" onclick = "gjdocnum(${doc_conf[0].acs_yn}${doc_conf[1].acs_yn}${doc_conf[2].acs_yn},'${doc.doc_num}')">
						<input type="button" value="반려" id="down" onclick = "downdocnum(${doc_conf[0].acs_yn}${doc_conf[1].acs_yn}${doc_conf[2].acs_yn},'${doc.doc_num}')">
						<input type="button" value="뒤로가기" id="back">

					</td>
				</tr>
		</table>
	</div>
</div>
</form>
<script>
function editdocnum(data) {
	location.href= "doceditcj.do?doc_num="+data+"&mode=read"
	console.log(data);
}
function deldocnum(data) {
	location.href= "docdelcj.do?doc_num="+data;
	console.log(data);
}
function gjdocnum(data, data2) {
	console.log(data);
	console.log(data2);
	location.href= "docgjupdate.do?doc_num="+data2+"&mode="+data+"&kind=up";
	console.log(data);
}
function downdocnum(data, data2) {
	console.log(data);
	console.log(data2);
	location.href= "docgjupdate.do?doc_num="+data2+"&mode="+data+"&kind=down";
	console.log(data);
}
$(function() {
	
	$("#back").click(function() {
		history.back();
	return false;
	});

	var className = 'authorization';
	$('div#menutab li.' + className).addClass('active');
	console.log($('div#menutab li.' + className));
	$('ul#side-menu').find('li.' + className).show();
});

</script>
<%@include file="../container/footer.jsp"%>