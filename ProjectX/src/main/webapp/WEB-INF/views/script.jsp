<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<style type="text/css">
.ct-div {
	padding-left: 5%;
	margin-bottom: 20px;
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
				<c:when test="${scriptFileAvailable}">
					<div class="ct-div">Service available</div>
					<c:if test="${scriptFileContentUpdated}">
						<div class="ct-div">
						 <b>Script file content updated. Continue to edit or return to home</b>
						</div>
					</c:if>
					<div class="ct-div">
						Script content
						<form action=<c:url value="/script/submit"></c:url> method="POST">
							<textarea rows="25" cols="80" name="content"><c:out value="${scriptFileContent}"></c:out></textarea>
							<br>
							<input type="submit" value="Submit">
							<br>
						</form>
					</div>
					<div class="ct-div">
						<a href='<c:url value="/home"></c:url>'>To home page</a>
					</div>
				</c:when>
				<c:otherwise>
					<div class="ct-div">Service not available</div>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</body>
</html>