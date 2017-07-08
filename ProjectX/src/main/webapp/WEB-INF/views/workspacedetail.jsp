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
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/round-button.css'></c:url>"></link>
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/workspacedetail.css'></c:url>"></link>
<%-- Page JS --%>
<title>Workspace details</title>
</head>
<body>
	<hpgTag:frame>
		<%-- Breadcrumb --%>
		<hpgTag:breadcrumb bc1="/home,Home,false,1" bc2="/workspace/list,Workspace list,false,2" bc3="/,Workspace info,true,3"/>
		<%-- Content --%>
		<div class="main_content">
			<%-- Workspace basic info --%>
			<%-- Description --%>
			<div class="panel-group">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
							<a data-toggle="collapse" href="#collapse1">Workspace description</a>
						</h4>
					</div>
					<div id="collapse1" class="panel-collapse collapse in">
						<div class="panel-body">
							<p>${workspaceDescription}</p>
							<p>
								<a href='<c:url value="/workspace/edit/${workspaceId}"></c:url>'>Edit</a>
							</p>
						</div>
					</div>
				</div>
			</div>
			<%-- Status (not available yet) --%>
			<div class="panel-group">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
							<a data-toggle="collapse" href="#collapse2">Workspace status</a>
						</h4>
					</div>
					<div id="collapse2" class="panel-collapse collapse in">
						<div class="panel-body">
							<p>Coming soon...</p>
						</div>
					</div>
				</div>
			</div>
			<%-- Action menu --%>
			<div class="round-buttons-div">
				<div class="round-buttons-line">
					<div class="round-button">
						<div class="round-button-circle">
							<a href="<c:url value="/workspace/browse/${workspaceId}"></c:url>">Browse workspace</a>
						</div>
					</div>
					<div class="round-button">
						<div class="round-button-circle">
							<a href="<c:url value="/buildlist/${workspaceId}/"></c:url>">Build list</a>
						</div>
					</div>
					<div class="round-button">
						<div class="round-button-circle">
							<a href="<c:url value="/testbuild/${workspaceId}/5"></c:url>">Test build</a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</hpgTag:frame>
</body>
</html>