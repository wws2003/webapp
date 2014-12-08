<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<style type="text/css">
.ct-div {
	padding-left: 5%;
}
</style>
</head>
<body>
	<h2>Simplest CI !</h2>
	<div class="ct-div">
		Browsing structure is being constructed. Please return later
		<div>
			<a href='<c:url value="/home"></c:url>'>To home page</a>
		</div>
	</div>
</body>
</html>
