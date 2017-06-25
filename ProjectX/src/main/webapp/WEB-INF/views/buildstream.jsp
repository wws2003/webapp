<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="<c:url value='/resources/css/common.css'></c:url>"></link>
<style type="text/css">
.ct-div {
	padding-left: 5%;
	margin-bottom: 20px;
}
</style>
</head>
<body>
	<div class="container">
		<div class="sidebar"></div>
		<div class="container_header">Simplest CI !</div>
		<div class="main_content">

			<div class="ct_div">
				Building stream<br>
				<br>
				<textarea id="txta_buildstream" rows="35" cols="120" name="objectContent"><c:out
						value="Build output is loading for workspace ${param['workspaceid']}..."></c:out></textarea>
			</div>

			<div class="container-footer">
				<a href='<c:url value="/workspace/detail/${param['workspaceid']}"></c:url>'>To
					workspace page</a> &nbsp;&nbsp;&nbsp; 
				<a href='<c:url value="/home"></c:url>'>To home page</a>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="<c:url value='/resources/asset/jquery/jquery-1.9.1.min.js'></c:url>"></script>
	<script type="text/javascript" src="<c:url value='/resources/js/buildstream.js'></c:url>"></script>
	<script type="text/javascript">
		refreshBuildStreamContent(<c:out value="${param['workspaceid']}"></c:out>);
	</script>
</body>
</html>
