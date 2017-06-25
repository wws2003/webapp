<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="title" required="true"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><c:out value="${title}"></c:out></title>
<%-- CSS --%>
<link rel="stylesheet" type="text/css"
	href="<c:url value='/resources/asset/bootstrap/css/bootstrap.min.css'></c:url>"></link>
<link rel="stylesheet" type="text/css"
	href="<c:url value='/resources/css/frame.css'></c:url>"></link>
<%-- JS --%>
<script type="text/javascript"
	src="<c:url value='/resources/asset/jquery/jquery-1.9.1.min.js'></c:url>"></script>
<script type="text/javascript"
	src="<c:url value='/resources/asset/bootstrap/js/bootstrap.min.js'></c:url>"></script>
<script type="text/javascript"
	src="<c:url value='/resources/js/frame.js'></c:url>"></script>
</head>
<body>
	<%-- Header --%>
	<header> Simplest CI </header>
	<%-- Main --%>
	<main class="row"> <%-- Main section --%>
	<section class="col-xs-12">
		<%-- Left side bar --%>
		<aside class="col-xs-1"></aside>
		<%-- Main article (to be rendered) --%>
		<article class="col-xs-11">
			<jsp:doBody></jsp:doBody>
		</article>
	</section>
	</main>
	<%-- Footer --%>
	<footer> TBC-HPG&copy;2017 </footer>
</body>
</html>