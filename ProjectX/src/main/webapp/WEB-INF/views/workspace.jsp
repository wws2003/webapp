<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/common.css'></c:url>"></link>
	<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/input.css'></c:url>"></link>
	<script type="text/javascript" src="<c:url value='/resources/js/jquery-1.9.0.min.js'></c:url>"></script>
	<script type="text/javascript" src="<c:url value='/resources/js/workspace.js'></c:url>"></script>
</head>
<style type="text/css">
input, textarea  {
	margin-bottom: 15px;
}
</style>
<body>
	<div class="container">
		<div class="sidebar"></div>
		<div class="container_header">Simplest CI !</div>
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
			method="POST">
				<c:if test="${empty edittingWorkspace}">
					<span>[Optional] Upload zip file of workspace:</span><input type="file" name="zipFile" accept=".zip"><br>
					
					<input type="radio" id="rbtn_buildscript_later" name="buildscript_timing" value="0">Choose/Submit build script later
					<input type="radio" id="rbtn_buildscript_submit" checked="checked" name="buildscript_timing" value="1">Submit build script now<br>
					
					<span class="input_label">Workspace name: </span><input name="workspacename" type="text" class="input_field"><br>
					<span class="input_label">Build script name: </span><input id="txt_buildscript_name" name="buildscriptname" type="text" class="input_field" value="autospring_build_script.sh"><br>
				</c:if>
				
				Workspace description: <br>
				<textarea rows="5" cols="80" name="workspacedescription"><c:out value="${not empty edittingWorkspace ? edittingWorkspace.description : ''}"></c:out></textarea><br>
				
				Build script content: <br>
				<textarea  id="txta_buildscript_content" rows="24" cols="80" name="buildscriptcontent"><c:out value="${not empty edittingWorkspace ? scriptContent : ''}"></c:out></textarea>
					
				<br> <input type="submit" value="Submit"> <br>
			</form>
			<div class="container-footer" style="position: static">
				<a href='<c:url value="/home"></c:url>'>To home page</a>
			</div>
		</div>
	</div>
</body>
</html>