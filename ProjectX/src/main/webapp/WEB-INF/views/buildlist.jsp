<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<c:if test="${not empty buildingList || not empty waitingList}">
<meta http-equiv="refresh" content="5;url=<c:url value="/buildlist/${not empty builtInfoPage ? builtInfoPage.workspaceId : 1}/"></c:url>">
</c:if>
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/buildlist.css'></c:url>"></link> 
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/common.css'></c:url>"></link> 
</head>
<body>
<div class="container">
<div class="sidebar"></div>
<div class="container_header">Simplest CI !</div>
<div class="main_content">
	<c:choose>
		<c:when test="${serviceAvailable}">
			<!-- Service available -->
			
			<div class="build_list_div">
				<b>Building list</b>
				<div class="build_list_row build_list_head">
					<div>Id</div>
					<div>Status</div>
					<div>Begin time</div>
					<div style="width: 48%">Log file path</div>
				</div>
			
				<c:forEach items="${buildingList}" var="element">
					<div class="build_list_row">
						<div>${element.id}</div>
						<div>Building</div>
						<div>${element.beginTimeStamp}</div>
						<div style="width: 48%">${element.logFilePath}</div>
					</div>
				</c:forEach>
			</div>
			
			<div class="build_list_div">
				<b>Waiting list</b>
				<div class="build_list_row build_list_head">
					<div>Id</div>
					<div>Status</div>
				</div>
				<c:forEach items="${waitingList}" var="element">
					<div class="build_list_row">
						<div>${element.id}</div>
						<div>${element.status == 0 ? "Waiting" : "Cancelled"}</div>
						<div><a href='<c:url value="/cancel/${element.id}"></c:url>'>Cancel</a></div>
					</div>
				</c:forEach>
			</div>
			
			<c:if test="${not empty builtInfoPage}">
				<div class="build_list_div">
					<b>Built list</b>
					
					<div class="build_list_row build_list_head">
						<div>Id</div>
						<div>Status</div>
						<div>Begin time</div>
						<div>End time</div>
						<div>Log file path</div>
						<div>Log detail</div>
					</div>
					
					<c:forEach items="${builtInfoPage.builtInfoList}" var="element">
							<div class=<c:choose><c:when test="${element.status == 0}">"build_list_row"</c:when><c:otherwise>"build_list_row error"</c:otherwise></c:choose>>
								<div>${element.id}</div>
								<div>${element.status == 0 ? "OK" : (element.status == 2 && empty element.beginTimeStamp ? "Cancelled by user" : "NG")}</div>
								<div>${not empty element.beginTimeStamp ? element.beginTimeStamp : "---"}</div>
								<div>${not empty element.endTimeStamp ? element.endTimeStamp : "---"}</div>
								<div>${element.logFilePath}</div>
								<c:url value="/log/${element.id}" var="logUrl"></c:url>
								<div><c:choose><c:when test="${not empty element.beginTimeStamp}"><a href='${logUrl}'>Detail</a></c:when><c:otherwise>---</c:otherwise></c:choose></div>
							</div>
					</c:forEach>
				</div>
				
				<div class="page_line">
					<a href='<c:url value="/buildlist/${builtInfoPage.workspaceId}/?page=1"></c:url>'>First </a>
					&nbsp;
					<c:if test="${builtInfoPage.page > 1}"><a href='<c:url value="/buildlist/${builtInfoPage.workspaceId}/?page=${builtInfoPage.page - 1}"></c:url>'>Prev </a></c:if>
					&nbsp;
					<c:out value="${builtInfoPage.page} / ${builtInfoPage.maxPageNumber}"></c:out>
					&nbsp;
					<c:if test="${builtInfoPage.page < builtInfoPage.maxPageNumber}"><a href='<c:url value="/buildlist/${builtInfoPage.workspaceId}/?page=${builtInfoPage.page + 1}"></c:url>'>Next </a></c:if>
					&nbsp;
					<a href='<c:url value="/buildlist/${builtInfoPage.workspaceId}/?page=${builtInfoPage.maxPageNumber}"></c:url>'> Last</a>
				</div>
				
			</c:if>
		</c:when>
		<c:otherwise>
			Service not available
		</c:otherwise>
	</c:choose>
	
	<div class="container-footer" style="position: static;">
	<c:if test="${not empty builtInfoPage}">
		<a href='<c:url value="/workspace/detail/${builtInfoPage.workspaceId}"></c:url>'>To workspace page</a>
		&nbsp;&nbsp;&nbsp;
	</c:if>
	<a href='<c:url value="/home"></c:url>'>To home page</a>
	
</div>
</div>
</div>
</body>
</html>
