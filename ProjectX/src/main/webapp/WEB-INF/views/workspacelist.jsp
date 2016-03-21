<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="<c:url value='/resources/css/common.css'></c:url>"></link>
</head>
<link rel="stylesheet" type="text/css"
	href="<c:url value='/resources/css/workspacelist.css'></c:url>"></link>
</head>
<body>
	<div class="container">
		<div class="sidebar"></div>
		<div class="container_header">Simplest CI !</div>
		<div class="main_content">
				<c:forEach items="${workspaces}" var="element">
					<div class="workspace_div">
						<h4>Workspace ${element.id}: ${element.name} </h4>
						<p>
							${element.description}
						</p>
						<h4>Details:</h4>
						<div>
							<div>- Workspace directory: ${element.directoryPath}</div>
							<div>- Workspace build script path:${element.scriptFilePath} </div>
							<span>
								<a href='<c:url value="/workspace/detail/${element.id}"></c:url>'>More ..</a>
							</span>
						</div>
					</div>
				</c:forEach>
				<div class="container-footer" style="position: static;">
					<a href='<c:url value="/home"></c:url>'>To home page</a>
				</div>
		</div>
	</div>
</body>
</html>