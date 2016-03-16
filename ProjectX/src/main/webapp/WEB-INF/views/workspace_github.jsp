<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/common.css'></c:url>"></link>
	<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/input.css'></c:url>"></link>
	<script type="text/javascript" src="<c:url value='/resources/js/jquery-1.9.0.min.js'></c:url>"></script>
	<script type="text/javascript" src="<c:url value='/resources/js/workspace_github.js'></c:url>"></script>
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
			<form action='<c:url value="/workspace/new"></c:url>' method="POST">
				<span class="input_label">Workspace name: </span><input name="workspacename" type="text" class="input_field"><br>
				
				<span class="input_label">Build script name: </span><input name="buildscriptname" type="text" class="input_field"><br>
				
				<span class="input_label">Github repository: </span><input id="txt_github_repository" type="text" class="long-text input_field">
				<span style="display: none;" id="spn_subdir_checkout"> Sub-directory <input id="txt_subdir_checkout" type="text"></span>
				<button id="btn_github_properties_confirm" value="Confirm">Confirm</button>
				<br>
				
				Workspace description: <br>
				<textarea rows="4" cols="80" name="workspacedescription"><c:out value="${not empty edittingWorkspace ? edittingWorkspace.description : ''}"></c:out></textarea><br>
				
				<input type="radio" id="rbtn_dense_checkout" checked="checked" name="pull_scope" value="0">Pull the whole repository
				<input type="radio" id="rbtn_sparse_checkout" name="pull_scope" value="1">Pull only sub directory
				<input type="radio" id="rbtn_reset" name="pull_scope" value="2">Reset script <br>

				Build script content: <br>
				<textarea id="txtarea_scriptcontent" rows="25" cols="80" name="buildscriptcontent"><c:out value="${not empty edittingWorkspace ? scriptContent : ''}"></c:out></textarea>
					
				<br> <input type="submit" value="Submit"> <br>
			</form>
			<div class="container-footer" style="position: static;">
				<a href='<c:url value="/home"></c:url>'>To home page</a>
			</div>
		</div>
	</div>
</body>
</html>