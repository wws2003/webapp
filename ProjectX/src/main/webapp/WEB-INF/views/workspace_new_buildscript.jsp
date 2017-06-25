<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="workspaceId" scope="page" value="${workspaceId}"></c:set>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/common.css'></c:url>"></link>
	<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/input.css'></c:url>"></link>
	<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/workspace_new_buildscript.css'></c:url>"></link>
	<script type="text/javascript" src="<c:url value='/resources/asset/jquery/jquery-1.9.1.min.js'></c:url>"></script>
	<script type="text/javascript" src="<c:url value='/resources/js/workspace_new_buildscript.js'></c:url>"></script>
</head>
<style type="text/css">
input, textarea  {
	margin-bottom: 15px;
}
input[type='text'] {
 	width: 180px;
}
</style>
<body>
	<div class="container">
		<div class="sidebar"></div>
		<div class="container_header">Simplest CI !</div>
		<div class="main_content">
			<input type="radio" id="rbtn_buildscript_write" name="buildscript_type" value="0">Write build script now
			<input type="radio" id="rbtn_buildscript_select" name="buildscript_type" checked="checked" value="1">Select build script from workspace folder<br>
					
			<div id="div_input">
				<div id="div_write">
					<form action=<c:url value="/workspace/update/${workspaceId}"></c:url>  enctype="multipart/form-data" method="POST">
						<span>Build script name: </span><input id="txt_buildscript_name" name="buildscriptname" type="text" value="autospring_build_script.sh"><br>
						Build script content: <br>
						<textarea  id="txta_buildscript_content" rows="24" cols="80" name="buildscriptcontent"></textarea>
						<br>
						<input type="submit" value="Submit">
					</form>
				</div>
				<div id="div_select">
					<div class="title">Choose one of files below as build script for new workspace <c:out value="${not empty workspaceId ? workspaceId : 'unknown'}"></c:out> </div>
					<div id="div_select_options">
					</div>
				</div>
			</div>
			
			<div class="container-footer" style="position: static">
				<a href='<c:url value="/home"></c:url>'>To home page</a>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	loadPossibleBuildScripts("${workspaceId}");
</script>
</html>