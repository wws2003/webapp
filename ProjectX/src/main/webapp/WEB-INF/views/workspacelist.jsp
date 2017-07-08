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
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/workspacelist.css'></c:url>"></link>
<%-- Page JS --%>
<title>Workspace list</title>
</head>
<body>
	<hpgTag:frame>
		<%-- Breadcrumb --%>
		<hpgTag:breadcrumb bc1="/home,Home,false,1" bc2="/,Workspace list,true,2" />
		<%-- Content --%>
		<div class="main_content">
			<c:forEach items="${workspaces}" var="element">
				<div class="workspace_div">
					<h4>Workspace ${element.id}: ${element.name}</h4>
					<p>${element.description}</p>
					<h4>Details:</h4>
					<div>
						<div>- Workspace directory: ${element.directoryPath}</div>
						<div>- Workspace build script path:${element.scriptFilePath}</div>
						<span> <a href='<c:url value="/workspace/detail/${element.id}"></c:url>'>More ..</a>
						</span>
					</div>
				</div>
			</c:forEach>
			<div class="container-footer" style="position: static;">
				<a href='<c:url value="/home"></c:url>'>To home page</a>
			</div>
		</div>
	</hpgTag:frame>
</body>
</html>