<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="hpgTag"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<c:set var="workspaceId" scope="page" value="${workspaceId}"></c:set>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%-- Common resources --%>
<%@ include file="commonHeader.jsp"%>
<%-- Page CSS --%>
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/input.css'></c:url>"></link>
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/workspace_new_buildscript.css'></c:url>"></link>
<%-- Page JS --%>
<script type="text/javascript" src="<c:url value='/resources/js/workspace_new_buildscript.js'></c:url>"></script>
<title>Workspace creating</title>
</head>
<body>
	<hpgTag:frame>
		<%-- Breadcrumb --%>
		<hpgTag:breadcrumb bc1="/home,Home,false,1" bc2="/workspace/list,Workspace list,false,2" bc3="/,Input workspace,true,3" />
		<%--Content --%>
		<div class="main_content">
			<%-- Choosing area --%>
			<div class="lo_builscript_check_area">
				<label><input type="radio" id="rbtn_buildscript_write" name="buildscript_type" value="0">Write build script now </label> <label><input type="radio"
					id="rbtn_buildscript_select" name="buildscript_type" checked="checked" value="1"
				>Select build script from workspace folder</label>
			</div>
			<%-- Main area --%>
			<div id="div_input" class="lo_builscript_input_area">
				<div class="col-xs-12">
					<%-- Select panel --%>
					<div id="div_select" class="col-xs-5 lo_buildscript_select_area">
						<div>
							Choose one of files below as build script for new workspace
							<c:out value="${not empty workspaceId ? workspaceId : 'unknown'}"></c:out>
						</div>
						<div id="div_select_options" class="lo_buildscript_select_options_area"></div>
					</div>
					<%-- Show panel --%>
					<div id="div_write" class="col-xs-7 lo_buildscript_write_area">
						<form action=<c:url value="/workspace/update/${workspaceId}"></c:url> enctype="multipart/form-data" method="POST">
							<span>Build script name: </span><input id="txt_buildscript_name" name="buildscriptname" type="text" value="autospring_build_script.sh"><br> Build script
							content: <br>
							<textarea id="txta_buildscript_content" rows="16" cols="80" name="buildscriptcontent"></textarea>
							<br> <input type="submit" value="Submit">
						</form>
					</div>
				</div>
			</div>
		</div>
	</hpgTag:frame>
</body>
<script type="text/javascript">
	loadPossibleBuildScripts("${workspaceId}");
</script>
</html>
