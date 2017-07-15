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
<title></title>
</head>
<body>
	<hpgTag:frame>
		<%-- Breadcrumb --%>
		<hpgTag:breadcrumb bc1="/home,Home,false,1" bc2="/workspace/list,Workspace list,false,2" bc3="/workspace/detail/${workspaceId},Workspace info,false,3"
			bc4="/,Opening,true,4"
		/>
		<%--Content --%>
		<div class="main_content">
			<c:choose>
				<c:when test="${objectCanOpened}">
					<div class="ct-div">
						<label>File content</label><br>
						<textarea rows="20" cols="105" name="objectContent"><c:out value="${objectContent}"></c:out></textarea>
					</div>
				</c:when>
				<c:otherwise>
					<div class="ct-div">File can't open on browser</div>
				</c:otherwise>
			</c:choose>
		</div>
	</hpgTag:frame>
</body>
</html>