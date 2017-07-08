<%@tag import="java.util.TreeMap"%>
<%@tag import="java.util.SortedMap"%>
<%@tag import="java.util.Comparator"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ tag dynamic-attributes="breadCrumbModelMap"%>
<%@ tag import="java.util.List"%>
<%@ tag import="java.util.Collections"%>
<%@ tag import="java.util.Comparator"%>
<%@ tag import="java.util.ArrayList"%>
<%@ tag import="java.util.Map"%>
<%@ tag import="java.util.TreeMap"%>
<%@ tag import="com.techburg.autospring.model.web.BreadCrumbModel"%>
<%
	//Bad practice to use scriptlet, but try first
	@SuppressWarnings("unchecked")
	Map<String, String> breadCrumbModelMap = (Map<String, String>) jspContext
			.getAttribute("breadCrumbModelMap");
			
	//Use a sorted map in order to try to avoid the backward compability of Comparator		
	SortedMap<Integer, BreadCrumbModel> breadCrumbModelOrderedMap = new TreeMap<Integer, BreadCrumbModel>();
			
	//Loop through the dynamic attributes
	int defaultOrderNo = 0;
	for (Map.Entry<String, String> entry : breadCrumbModelMap
			.entrySet()) {
		//Parsing BreadCrumbModel from CSV
		String breadCrumbModelCSV = entry.getValue();
		String[] breadCrumbParts = breadCrumbModelCSV.split(",");

		//Construct the breadcrumb model
		String path = breadCrumbParts[0];
		String dispName = breadCrumbParts[1];
		boolean isCurrentPage = Boolean.valueOf(breadCrumbParts[2]);
		int orderNo = breadCrumbParts.length > 3 ? Integer
				.valueOf(breadCrumbParts[3]) : defaultOrderNo++;
		BreadCrumbModel breadCrumbModel = new BreadCrumbModel(path,
				dispName, isCurrentPage, orderNo);

		//Add to ordered map
		breadCrumbModelOrderedMap.put(orderNo, breadCrumbModel);
	}

	//Pushback into jsp context to use in JSTL
	jspContext.setAttribute("breadCrumbModelOrderedMap", breadCrumbModelOrderedMap);
%>
<ol class="breadcrumb">
	<c:forEach items="${breadCrumbModelOrderedMap}" var="breadCrumbModelEntry">
		<c:set var="breadCrumbModel" value="${breadCrumbModelEntry.value}"></c:set>
		<c:choose>
			<c:when test="${breadCrumbModel.bCurrentPage}">
				<li class="breadcrumb-item active"><c:out value="${breadCrumbModel.dispName}"></c:out></li>
			</c:when>
			<c:otherwise>
				<li><a href='<c:url value="${breadCrumbModel.path}"></c:url>'>${breadCrumbModel.dispName}</a></li>
			</c:otherwise>
		</c:choose>
	</c:forEach>
</ol>