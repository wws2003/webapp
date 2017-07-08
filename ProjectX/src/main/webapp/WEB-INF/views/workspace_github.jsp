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
<script type="text/javascript" src="<c:url value='/resources/js/workspace_github.js'></c:url>"></script>
<title>GitHub workspace</title>
</head>
<body>
	<hpgTag:frame>
		<%-- Breadcrumb --%>
		<hpgTag:breadcrumb bc1="/home,Home,false,1" bc2="/,New workspace from GitHub,true,2"/>
		<%--Content --%>
		<div class="main_content input_container">
			<form action='<c:url value="/workspace/create_github"></c:url>' method="POST">
				<span class="input_label">Workspace name: </span><input name="workspacename" id="txt_workspace_name" type="text" class="input_field"><br> <span
					class="input_label"
				>Build script name: </span><input name="buildscriptname" type="text" class="input_field" value="autospring_build_script.sh"><br> <span class="input_label">Github
					repository: </span><input id="txt_github_repository" type="text" class="long-text input_field"> <span style="display: none;" id="spn_subdir_checkout">
					Sub-directory <input id="txt_subdir_checkout" type="text">
				</span>
				<button id="btn_github_properties_confirm" value="Confirm">Confirm</button>
				<br> Workspace description: <br>
				<textarea rows="4" cols="80" name="workspacedescription"><c:out value="${not empty edittingWorkspace ? edittingWorkspace.description : ''}"></c:out></textarea>
				<br> <input type="radio" id="rbtn_dense_checkout" checked="checked" name="pull_scope" value="0">Pull the whole repository <input type="radio"
					id="rbtn_sparse_checkout" name="pull_scope" value="1"
				>Pull only sub directory <input type="radio" id="rbtn_reset" name="pull_scope" value="2">Reset script <br> Build script content: <br>
				<textarea id="txtarea_scriptcontent" rows="18" cols="80" name="buildscriptcontent"><c:out value="${not empty edittingWorkspace ? scriptContent : ''}"></c:out></textarea>
				<br> <input type="submit" value="Submit"> <br>
			</form>
		</div>
	</hpgTag:frame>
</body>
</html>