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
			<form method="POST">
				Workspace name: <input type="text"><br> 
				Build script name: <input type="text"><br>
				Script content: <br>
				<textarea rows="25" cols="80" name="content">
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