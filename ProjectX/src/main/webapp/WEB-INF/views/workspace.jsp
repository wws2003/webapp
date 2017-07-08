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
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/input.css'></c:url>"></link>
<%-- Page JS --%>
<script type="text/javascript" src="<c:url value='/resources/js/workspace.js'></c:url>"></script>
<title></title>
</head>
<body>
	<hpgTag:frame>
		<%-- Breadcrumb --%>
		<c:set var="homePath" value="/home,Home,false,1"></c:set>
		<c:choose>
			<c:when test="${empty edittingWorkspace}">
				<hpgTag:breadcrumb bc1="${homePath}" bc2="/,New workspace,true,2" />
			</c:when>
			<c:otherwise>
				<hpgTag:breadcrumb bc1="${homePath}" bc2="/workspace/list,Workspace list,false,2" bc3="/workspace/detail/${edittingWorkspace.id},Workspace info,false,3"
					bc4="/,Editing,true,4"
				/>
			</c:otherwise>
		</c:choose>
		<%-- Content --%>
		<div class="main_content input_container">
			<form
				<c:choose> 
					<c:when test="${empty edittingWorkspace}">
						action=<c:url value="/workspace/create"></c:url>  enctype="multipart/form-data"
					</c:when>
					<c:otherwise>
						action=<c:url value="/workspace/update/${edittingWorkspace.id}"></c:url>
					</c:otherwise>
				</c:choose>
				method="POST"
			>
				<%-- In the case workspace --%>
				<c:if test="${empty edittingWorkspace}">
					<span>[Optional] Upload zip file of workspace:</span>
					<input type="file" name="zipFile" accept=".zip">
					<br>
					<input type="radio" id="rbtn_buildscript_later" name="buildscript_timing" value="0">Choose/Submit build script later
					<input type="radio" id="rbtn_buildscript_submit" checked="checked" name="buildscript_timing" value="1">Submit build script now<br>
					<span class="input_label">Workspace name: </span>
					<input name="workspacename" type="text" class="input_field">
					<br>
					<span class="input_label">Build script name: </span>
					<input id="txt_buildscript_name" name="buildscriptname" type="text" class="input_field" value="autospring_build_script.sh">
					<br>
				</c:if>
				<%-- New workspace / Editing workspace --%>
				<label>Workspace description (HTML format):</label> <br>
				<textarea rows="5" cols="80" name="workspacedescription"><c:out value="${not empty edittingWorkspace ? edittingWorkspace.description : ''}"></c:out></textarea>
				<br> <label>Build script content:</label> <br>
				<textarea id="txta_buildscript_content" rows="16" cols="80" name="buildscriptcontent"><c:out value="${not empty edittingWorkspace ? scriptContent : ''}"></c:out></textarea>
				<br> <input type="submit" value="Submit"> <br>
			</form>
		</div>
	</hpgTag:frame>
</body>
</html>