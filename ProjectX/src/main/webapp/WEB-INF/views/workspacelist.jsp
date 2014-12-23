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
						<div>Workspace id: ${element.id}</div>
						<div>Workspace directory: ${element.directoryPath}</div>
						<div>Workspace build script path:${element.scriptFilePath} </div>
						<div><a href='<c:url value="/workspace/detail/${element.id}"></c:url>'>Workspace details</a></div>
						<div><a href='<c:url value="/workspace/edit/${element.id}"></c:url>'>Workspace edit</a></div>
					</div>
				</c:forEach>
				<div class="container-footer" style="position: static;">
					<a href='<c:url value="/home"></c:url>'>To home page</a>
				</div>
		</div>
	</div>
</body>
</html>