<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="refresh" content="2;url=<c:url value="/workspace/list"></c:url>">
<style type="text/css">
.ct-div {
	padding-left: 5%;
}
</style>
</head>
<body>
	<h2>Simplest CI !</h2>
	<div class="ct-div">
		Your request has been queued to be processed. Please wait to see the result
		<div>
			<a href='<c:url value="/home"></c:url>'>To home page</a>
		</div>
	</div>
</body>
</html>
