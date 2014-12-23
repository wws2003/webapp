<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="<c:url value='/resources/css/common.css'></c:url>"></link>
<link rel="stylesheet" type="text/css"
	href="<c:url value='/resources/css/round-button.css'></c:url>"></link>
</head>
<body>
	<div class="container">
		<div class="sidebar"></div>
		<div class="container_header">Simplest CI !</div>
		<div class="main_content">
			<div style="margin-bottom: 20px;">
				A sample project to support CI. It was triggered by my program solving the problem of
				Parallel Particles <a
					href="http://www.eecs.berkeley.edu/~carazvan/2010bootcamp/index.html">here</a>, and extended to be able to handle multiple projects.
				<br> Multiple workspaces with build script are now available. Workspace browsing is also supported.
				
				<br><br> This web application would support visualization of build progress directly on browser soon.
			</div>
			
			<div class="round-buttons-div">
				<div class="round-buttons-line">
					<div class="round-button">
	    				<div class="round-button-circle"><a href="<c:url value="/workspace/new"></c:url>" >New workspace</a>
	    				</div>
					</div>
					<div class="round-button">
	    				<div class="round-button-circle"><a href="<c:url value="/workspace/list"></c:url>" >List workspaces</a>
	    				</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
