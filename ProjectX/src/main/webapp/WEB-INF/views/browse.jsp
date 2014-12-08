<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/common.css'></c:url>"></link>
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/browse.css'></c:url>"></link>
</head>
</head>
<body>
	<div class="container">
		<div class="sidebar"></div>
		<div class="container_header">Simplest CI !</div>
		<div class="main_content">
			<c:choose>
				<c:when test="${!isFileObject}">
					<div>
						Content of directory <strong><i><c:out value="${browsingObjectPath}"></c:out></i></strong>:
						<div style="margin-top: 30px; margin-bottom: 30px">
							<table>
								<tr>
									<th>Name</th>
									<th>Last modified</th>
								</tr>
								<c:forEach items="${childBrowsingObjects}" var="element">
									<tr>
										<c:choose>
											<c:when test="${element.objectType == 0}">
												<td style="text-align: left;">---------------<a
													href="<c:url value="/open/${element.id}"></c:url>">${element.name}</a></td>
											</c:when>
											<c:otherwise>
												<td style="text-align: left;"><a
													href="<c:url value="/browse/${element.id}"></c:url>">${element.name}</a>/</td>
											</c:otherwise>
										</c:choose>
										<td>${element.modifiedTime}</td>
									</tr>
								</c:forEach>
							</table>
						</div>
					</div>
					<div>
						<a href='<c:url value="/home"></c:url>'>To home page</a>
					</div>
				</c:when>
				<c:otherwise>
			File object can't be browsed. Open instead
		</c:otherwise>
			</c:choose>
		</div>
	</div>
</body>
</html>
