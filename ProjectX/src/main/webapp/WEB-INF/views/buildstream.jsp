<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="hpgTag"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%-- Common resources --%>
<%@ include file="commonHeader.jsp"%>
<%-- Page CSS --%>
<%-- Page JS --%>
<script type="text/javascript" src="<c:url value='/resources/js/buildstream.js'></c:url>"></script>
<title>Building stream</title>
</head>
<body>
	<hpgTag:frame>
		<%-- Breadcrumb --%>
		<ol class="breadcrumb">
			<li><a href='<c:url value="/home"></c:url>'>Home</a></li>
			<li><a href='<c:url value="/workspace/list"></c:url>'>Workspace list</a></li>
			<li><a href='<c:url value="/workspace/detail/${param['workspaceid']}"></c:url>'>Workspace info</a></li>
			<li><a href='<c:url value="/buildlist/${param['workspaceid']}/"></c:url>'>Building list</a></li>
			<li class="breadcrumb-item active">Building stream</li>
		</ol>
		<%--Content --%>
		<div class="main_content">
			<div class="ct_div">
				<p>
					<label>Output</label>
				</p>
				<textarea id="txta_buildstream" rows="22" cols="120" name="objectContent"><c:out value="Build output is loading for workspace ${param['workspaceid']}..."></c:out></textarea>
			</div>
		</div>
		<%-- Refresh script --%>
		<script type="text/javascript">
			refreshBuildStreamContent(<c:out value="${param['workspaceid']}"></c:out>);
		</script>
	</hpgTag:frame>
</body>
</html>