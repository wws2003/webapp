<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="<c:url value='/resources/css/common.css'></c:url>"></link>
</head>
<body>
	<div class="container">
		<div class="sidebar"></div>
		<div class="container_header">Simplest CI !</div>
		<div class="main_content">
			<form action=<c:url value="/workspace/new"></c:url> method="POST">
				Workspace name: <input name="workspacename" type="text"><br> 
				Build script name: <input name="buildscriptname" type="text"><br>
				Script content: <br>
				<textarea rows="25" cols="80" name="buildscriptcontent">
				</textarea>
				<br> <input type="submit" value="Submit"> <br>
			</form>
		</div>
		<div class="container-footer">
			<a href='<c:url value="/home"></c:url>'>To home page</a>
		</div>
	</div>
</body>
</html>