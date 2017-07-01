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
<title>Error page</title>
</head>
<body>
	<hpgTag:frame>
		<%-- Content --%>
		<div class="main_content">
			Some error happened. Error message: <span style="color: red"><c:out value="${not empty errorMessage ? errorMessage : 'Unspecified error'}"></c:out></span>. <br>
			<br>Please try again
		</div>
		<div class="container-footer">
			<a href='<c:url value="/home"></c:url>'>To home page</a>
		</div>
	</hpgTag:frame>
</body>
</html>