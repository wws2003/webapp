<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="hpgTag"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%-- Common resources --%>
<%@ include file="commonHeader.jsp"%>
<%-- Page resources --%>
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/round-button.css'></c:url>"></link>
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/home.css'></c:url>"></link>
<script type="text/javascript" src="<c:url value='/resources/js/home.js'></c:url>"></script>
<title>Home</title>
</head>
<body>
	<hpgTag:frame>
		<div class="main_content">
			<%-- Some simple message --%>
			<div>
				Yet-a-very-simple so-called Continuous-Integration application, <strong>solely for self-education purpose</strong> . Focused techniques are not limited in web
				developing techniques using Spring framework, but extended to other fields in Java. In particular, concurrency handling is critical to support queued build tasks and
				read-write synchronization for the SQLite DBMS (deliberately chosen for practicing purpose).
			</div>
			<%-- Menu buttons --%>
			<div class="round-buttons-div">
				<div class="round-buttons-line" id="div_btns_line">
					<div class="round-button" id="btn_new">
						<div class="round-button-circle">
							<a href="<c:url value="/workspace/new"></c:url>">New workspace</a>
						</div>
					</div>
					<div class="round-button" id="btn_new_normal" style="display: none;">
						<div class="round-button-circle">
							<a href="<c:url value="/workspace/new"></c:url>">New workspace from scratch</a>
						</div>
						<div class="vertical-explanation">- You have to configure your workspace from scratch by giving workspace name, content of build script etc.</div>
					</div>
					<div class="round-button" id="btn_new_github" style="display: none;">
						<div class="round-button-circle">
							<a href="<c:url value="/workspace/github/new"></c:url>">New workspace from Github</a>
						</div>
						<div class="vertical-explanation">
							- Project from GitHub will be auto cloned/pulled<br> <br> - Your build script is almost configured, your work is much easier<br>
						</div>
					</div>
					<div class="round-button" id="btn_list">
						<div class="round-button-circle">
							<a href="<c:url value="/workspace/list"></c:url>">List workspaces</a>
						</div>
						<div class="vertical-explanation">List up all workspace built so far</div>
					</div>
				</div>
			</div>
			<%-- Some notes --%>
			<div class="foot-note-div">
				<div>
					<h4>Feature milestones</h4>
					[2014/08] Getting started with single workspace, "workspace build" means running user-written Unix script <br> [2014/11] Multiple workspaces with different build
					scripts are now available. Workspace browsing is also supported. <br> [2015/01] Helper to import project from GitHub easier, including sparse-checkout <br>
					[2016/03] Real-time export workspace build output to browser. Add simple build status notification via Google Chrome Extension <br> 
					[2016/03] Allow user to upload workspace local zip file then select build script while creating workspace.<br> 
					[2017/07] Add small display stuffs like breadcrumb, grid layout using JSP tag and Bootstrap3. Fix some bugs related to Zip file extraction
				</div>
				<div>
					<h4>TODO List</h4>
					[1] First of all: Migration into JavaEE7 MVC on GlassFish. Other improvements are later stories<br>
				</div>
			</div>
		</div>
	</hpgTag:frame>
</body>
</html>