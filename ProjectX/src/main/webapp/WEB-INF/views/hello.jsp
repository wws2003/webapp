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
				Parallel Particles, and now being extended to be able to handle multiple projects <a
					href="http://www.eecs.berkeley.edu/~carazvan/2010bootcamp/index.html">Here</a>
				<br> It would support visualization of the result of the
				program on user-browser soon...
			</div>
			<!--
			<div>
				<div style="margin-bottom: 20px;">
					Browse source code <br> 
					Link: <a
						href="<c:url value="/browse/1"></c:url>"><c:url
							value="/browse/1"></c:url></a>
				</div>
				<div style="margin-bottom: 20px;">
					A list of all build tasks so far <br> 
					Link: <a
						href="<c:url value="/buildlist"></c:url>"><c:url
							value="/buildlist"></c:url></a>
				</div>
				<div style="margin-bottom: 20px;">
					Edit the build script here <br> 
					Link: <a
						href="<c:url value="/script/edit"></c:url>"><c:url
							value="/script/edit"></c:url></a>
				</div>
				<div>
					You can test this webapp by adding 5 build tasks and waiting for
					them to be built. The auto-build can also be triggered by a Git
					push hook <br>
					Link: <a href="<c:url value="/testbuild/5"></c:url>"><c:url
							value="/testbuild/5"></c:url></a>
				</div>
			</div>
			  -->
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
