<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/common.css'></c:url>"></link>
	<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/round-button.css'></c:url>"></link>
	<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/home.css'></c:url>"></link>
	<script type="text/javascript" src="<c:url value='/resources/js/jquery-1.9.0.min.js'></c:url>"></script>
	<script type="text/javascript" src="<c:url value='/resources/js/home.js'></c:url>"></script>
</head>
<body>
	<div class="container">
		<div class="sidebar"></div>
		<div class="container_header">Simplest CI !</div>
		<div class="main_content">
			<div style="margin-bottom: 20px;">
				A sample CI application. It was triggered to create a simple continuous integration for my program solving the problem of
				Parallel Particles <a
					href="http://www.eecs.berkeley.edu/~carazvan/2010bootcamp/index.html">here</a>, and then extended to be able to handle multiple projects.
				<br><br> Multiple workspaces with build script are now available. Workspace browsing is also supported.
				
				<br><br> This web application would support visualization of build progress directly on browser soon.
			</div>
			
			<div class="round-buttons-div">
				<div class="round-buttons-line" id="div_btns_line">
					<div class="round-button" id="btn_new">
	    				<div class="round-button-circle"><a href="<c:url value="/workspace/new"></c:url>" >New workspace</a>
	    				</div>
					</div>
					<div class="round-button" id="btn_new_normal" style="display: none;">
	    				<div class="round-button-circle"><a href="<c:url value="/workspace/new"></c:url>" >New workspace from scratch</a>
	    				</div>
	    				<div class="vertical-explanation">
		    				- You have to configure your workspace from scratch by giving workspace name, content of build script etc. 
	    				</div>
					</div>
					<div class="round-button" id="btn_new_github" style="display: none;">
	    				<div class="round-button-circle"><a href="<c:url value="/workspace/github/new"></c:url>" >New workspace from Github</a>
	    				</div>
	    				<div class="vertical-explanation">
		    				- Project from GitHub will be auto cloned/pulled<br><br>
		    				- Your build script is almost configured, your work is much easier<br>
	    				</div>
					</div>
					<div class="round-button">
	    				<div class="round-button-circle"><a href="<c:url value="/workspace/list"></c:url>" >List workspaces</a>
	    				</div>
	    				<div class="vertical-explanation">
		    				List up all workspace built so far
	    				</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
