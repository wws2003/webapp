<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<meta http-equiv="refresh" content="6;url=<c:url value="${redirectPage}"></c:url>">
<head>
<link rel="stylesheet" type="text/css"
	href="<c:url value='/resources/css/common.css'></c:url>"></link>
</head>
<body>
	<div class="container">
		<div class="sidebar"></div>
		<div class="container_header">Simplest CI !</div>
		<div class="main_content">
			Your request has been processed. Please wait a moment to see the result
		</div>
		<div class="container-footer">
			<a href='<c:url value="/home"></c:url>'>To home page</a>
		</div>
	</div>
</body>
</html>