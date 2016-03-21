<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<style type="text/css">
.ct-div {
	padding-left: 5%;
	margin-bottom: 20px;
}
textarea {
	margin-top: 20px;
}
</style>
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/common.css'></c:url>"></link>
</head>
<body>
	<div class="container">
		<div class="sidebar"></div>
		<div class="container_header">Simplest CI !</div>
		<div class="main_content">
			<c:choose>
				<c:when test="${objectCanOpened}">
					<div class="ct-div">
						File content<br>
						<textarea rows="45" cols="105" name="objectContent"><c:out value="${objectContent}"></c:out></textarea>
					</div>
					<div class="ct-div">
						<a href='<c:url value="/home"></c:url>'>To home page</a>
					</div>
				</c:when>
				<c:otherwise>
					<div class="ct-div">File can't open on browser</div>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</body>
</html>