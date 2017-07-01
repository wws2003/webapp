<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ tag language="java" pageEncoding="UTF-8"%>
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
<footer>
	<span> TBC-HPG&copy;2017 </span> <span> <a href='#'>Sitemap</a> </span>
</footer>