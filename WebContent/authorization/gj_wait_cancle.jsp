<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../container/header.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pb" value="${requestScope.pagebean }" />
<div id="div1"></div>
<div class="container">
	<h2>취소 기안 문서함</h2>
	<div>&nbsp;</div>
	<div class="selectbutton">
		&nbsp;
		<button class='all'>전체</button>
		<button class='prog'>진행</button>
		<button class='comp'>완료</button>
		<button class='cancle'>취소</button>
	</div>
	<table class="table table-striped table-hover">
		<thead class="thead-light">
			<tr class="table-primary">
				<td>기안일</td>
				<td>문서제목</td>
				<td>문서번호</td>
				<td>문서상태</td>
			</tr>
		</thead>
		<c:set var="list" value="${pb.list}" />
		<c:forEach var="b" items="${list}">
			<%-- <c:forEach begin="1" end="${b.level}">▷</c:forEach> --%>
			<tr>
				<td>${b.start_date}</td>
				<td><a href="javascript:functionrt(${b.doc_kind},'${b.doc_num}');">${b.doc_title}</a></td>
				<td>${b.doc_num}</td>
				<td>${b.doc_state}</td>
			</tr>
		</c:forEach>
	</table>

	<div class="pagination">
		<c:set var="startPage" value="${pb.startPage}" />
		<c:set var="endPage" value="${pb.endPage}" />
		<c:if test="${startPage > 1}">
			<Button>이전</Button>
		</c:if>
		<c:forEach begin="${startPage}" end="${endPage}" var="i">
			<button type="button">${i}</button>
		</c:forEach>
		<c:if test="${endPage < pb.totalPage}">
			<Button>다음</Button>
		</c:if>
	</div>
</div>
<style>
.container {
	padding-right: 350px;
}

p {
	padding-top: 50px;
	padding-right: 30px;
	padding-bottom: 50px;
	padding-left: 80px;
}

body {
	width: 100%;
	margin: 0;
	padding: 0;
}

pd {
	padding-right: 200px;
}

button {
	background-color: #337ab7; /* Green */
	border: none;
	color: white;
	padding: 8px 10px;
	border-radius: 5px;
	text-align: center;
	text-decoration: none;
	display: inline-block;
	font-size: 16px;
	margin: 4px 2px;
	cursor: pointer;
}

thead {
	background-color: #337ab7;
	color: white;
	font-weight: bold;
}
</style>

<c:set var="prePage" value="${requestScope.prePage}" />
<c:set var="nextPage" value="${requestScope.nextPage}" />
<script>
	function functionrt(data, data1) {
		console.log(data);
		console.log(data1);
		switch (data) {
		case 10:
			//기안서
			location.href = "docreadcj.do?doc_num=" + data1;
			break;
		case 20:
			//품의
			location.href = "docreadcj.do?doc_num=" + data1;
			break;
		case 30:
			//발주서
			location.href = "docreadcj.do?doc_num=" + data1;
			break;
		case 40:
			//출장
			location.href = "docreadcj.do?doc_num=" + data1;
			break;
		case 50:
			//휴가
			location.href = "docreadcj.do?doc_num=" + data1;
			break;
		case 60:
			//병가
			location.href = "docreadcj.do?doc_num=" + data1;
			break;
		}
	}

	$(function() {
		$('.selectbutton button').click(function() {
			var page;
			if ($(this).text() == '진행') {
				location.href = "gjwaitlisting.do"
			} else if ($(this).text() == '완료') {
				location.href = "gjwaitlistok.do"
			} else if ($(this).text() == '취소') {
				location.href = "gjwaitlistcancle.do";
			} else {
				location.href = "gjwaitlist.do"
			}

			return false;
		});

		$('.pagination button').click(function() {
			var page;
			if ($(this).text() == '이전') {
				page = ${prePage};
				location.href = "gjwaitlist.do?page=" + page;
			} else if ($(this).text() == '다음') {
				page = ${nextPage};
				location.href = "gjwaitlist.do?page=" + page;
			} else {
				page = $(this).text();
				location.href = "gjwaitlist.do?page=" + page;
			}
			return false;
		});

		$('.pagination a').each(function(index, element) {
			if ($(element).text() == '${pb.currentPage}') {
				$(element).addClass('active');
			}
		});

		var className = 'authorization';
		$('div#menutab li.' + className).addClass('active');
		console.log($('div#menutab li.' + className));
		$('ul#side-menu').find('li.' + className).show();
	});
</script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
<%@include file="../container/footer.jsp"%>