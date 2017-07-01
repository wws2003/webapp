<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="hpgTag"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="refresh" content="6;url=<c:url value="${redirectPage}"></c:url>">
<%-- Common resources --%>
<%@ include file="commonHeader.jsp"%>
<%-- Page CSS --%>
<%-- Page JS --%>
<title>Inform page</title>
</head>
<body>
	<hpgTag:frame>
		<%--Content --%>
		<div class="main_content">Your request has been processed. Please wait a moment to see the result</div>
	</hpgTag:frame>
</body>
</html>