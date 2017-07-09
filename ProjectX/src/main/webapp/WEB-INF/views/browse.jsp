<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="hpgTag"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%-- Common resources --%>
<%@ include file="commonHeader.jsp"%>
<%-- Page CSS --%>
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/browse.css'></c:url>"></link>
<%-- Page JS --%>
<title>Workspace browse</title>
</head>
<body>
	<hpgTag:frame>
		<%-- Breadcrumb --%>
		<hpgTag:breadcrumb bc1="/home,Home,false,1" bc2="/workspace/list,Workspace list,false,2" bc3="/workspace/detail/${workspaceId},Workspace info,false,3"
			bc4="/,Browsing,true,4"
		/>
		<%-- Content --%>
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
												<td style="text-align: left;">---------------<a href="<c:url value="/open/${workspaceId}/${element.id}"></c:url>">${element.name}</a></td>
											</c:when>
											<c:otherwise>
												<td style="text-align: left;"><a href="<c:url value="/browse/${workspaceId}/${element.id}"></c:url>">${element.name}</a>/</td>
											</c:otherwise>
										</c:choose>
										<td>${element.modifiedTime}</td>
									</tr>
								</c:forEach>
							</table>
						</div>
					</div>
				</c:when>
				<c:otherwise>
					File object can't be browsed. Open instead
				</c:otherwise>
			</c:choose>
		</div>
	</hpgTag:frame>
</body>
</html>