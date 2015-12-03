<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="<c:url value='/resources/css/common.css'></c:url>"></link>
<link rel="stylesheet" type="text/css"
	href="<c:url value='/resources/css/round-button.css'></c:url>"></link>
</head>
<style type="text/css">
.round-buttons-line {
	margin: 0 auto;
	width: 70%;
}

.round-button {
	width:26%;
	display: inline-block;
	margin:0 10px 0 10px;
}

.round-button-circle {
    background: #81BEF7;
}

.round-button:hover {
    
}

.round-button-circle:hover {
	background: #819FF7;
}

</style>
<body>
	<div class="container">
		<div class="sidebar"></div>
		<div class="container_header">Simplest CI !</div>
		<div class="main_content">
			<div>
				<h4><b>Workspace description</b></h4>
				<p>${workspaceDescription}</p>
				<h4><b>Workspace status</b></h4>
				<p>Coming soon...</p>
			</div>
			<div class="round-buttons-div">
				<div class="round-buttons-line">
					<div class="round-button">
	    				<div class="round-button-circle"><a href="<c:url value="/browse/${workspaceRootBrowsingObjectId}"></c:url>" >Browse workspace</a>
	    				</div>
					</div>
					<div class="round-button">
	    				<div class="round-button-circle"><a href="<c:url value="/buildlist/${workspaceId}/"></c:url>" >Build list</a>
	    				</div>
					</div>
					<div class="round-button">
	    				<div class="round-button-circle"><a href="<c:url value="/testbuild/${workspaceId}/5"></c:url>" >Test build</a>
	    				</div>
					</div>
				</div>
			</div>
			<div class="container-footer">
				<a href='<c:url value="/workspace/edit/${workspaceId}"></c:url>'>Edit workspace</a>
		&nbsp;&nbsp;&nbsp;
				<a href='<c:url value="/workspace/list"></c:url>'>To workspace list</a>
		&nbsp;&nbsp;&nbsp;
				<a href='<c:url value="/home"></c:url>'>To home page</a>
			</div>
		</div>
	</div>
</body>
</html>
