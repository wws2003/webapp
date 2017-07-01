<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="hpgTag"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%-- Common resources --%>
<%@ include file="commonHeader.jsp"%>
<%-- Page resources --%>
<link rel="stylesheet" type="text/css"
	href="<c:url value='/resources/css/logon.css'></c:url>"></link>
<title>Logon</title>
</head>
<body>
	<hpgTag:frame>
		<div>
			<form class="lo_logon_form">
				<%-- User code --%>
				<div class="form-group">
					<label for="txtUserCode">User code</label> <input type="text"
						id="txtUserCode" class="form-control">
				</div>
				<%-- Password --%>
				<div class="form-group">
					<label for="txtPassword">Password</label> <input type="password"
						id="txtPassword" class="form-control">
				</div>
				<%-- Remember me --%>
				<div class="checkbox">
					<label><input type="checkbox">Remember me</label>
				</div>
				<%-- Submit button --%>
				<button type="button" class="btn btn-default">Logon</button>
			</form>
		</div>
	</hpgTag:frame>
</body>
</html>