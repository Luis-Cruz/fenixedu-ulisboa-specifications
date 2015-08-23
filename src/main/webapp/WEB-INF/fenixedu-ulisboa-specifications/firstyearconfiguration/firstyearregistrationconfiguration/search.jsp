<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<spring:url var="datatablesUrl" value="/javaScript/dataTables/media/js/jquery.dataTables.latest.min.js"/>
<spring:url var="datatablesBootstrapJsUrl" value="/javaScript/dataTables/media/js/jquery.dataTables.bootstrap.min.js"></spring:url>
<script type="text/javascript" src="${datatablesUrl}"></script>
<script type="text/javascript" src="${datatablesBootstrapJsUrl}"></script>
<spring:url var="datatablesCssUrl" value="/CSS/dataTables/dataTables.bootstrap.min.css"/>

<link rel="stylesheet" href="${datatablesCssUrl}"/>
<spring:url var="datatablesI18NUrl" value="/javaScript/dataTables/media/i18n/${portal.locale.language}.json"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/CSS/dataTables/dataTables.bootstrap.min.css"/>

${portal.toolkit()}

<link href="${pageContext.request.contextPath}/static/fenixedu-ulisboa-specifications/css/dataTables.responsive.css" rel="stylesheet"/>
<script src="${pageContext.request.contextPath}/static/fenixedu-ulisboa-specifications/js/dataTables.responsive.js"></script>
<link href="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/css/dataTables.tableTools.css" rel="stylesheet"/>
<script src="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/js/dataTables.tableTools.js"></script>
<link href="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/css/select2.min.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/js/select2.min.js"></script>						
<script type="text/javascript" src="${pageContext.request.contextPath}/webjars/bootbox/4.4.0/bootbox.js" ></script>
<script src="${pageContext.request.contextPath}/static/fenixedu-ulisboa-specifications/js/omnis.js"></script>



<%-- TITLE --%>
<div class="page-header">
	<h1><spring:message code="label.firstYearConfiguration.searchFirstYearRegistrationConfiguration" />
		<small></small>
	</h1>
</div>
<%-- NAVIGATION --%>
<div class="well well-sm" style="display:inline-block">
	<span class="glyphicon glyphicon-cog" aria-hidden="true"></span>&nbsp;<a class="" href="${pageContext.request.contextPath}/fenixedu-ulisboa-specifications/firstyearconfiguration/firstyearregistrationconfiguration/edit"  ><spring:message code="label.event.firstYearConfiguration.edit" /></a>	
</div>
	<c:if test="${not empty infoMessages}">
				<div class="alert alert-info" role="alert">
					
					<c:forEach items="${infoMessages}" var="message"> 
						<p> <span class="glyphicon glyphicon glyphicon-ok-sign" aria-hidden="true">&nbsp;</span>
  							${message}
  						</p>
					</c:forEach>
					
				</div>	
			</c:if>
			<c:if test="${not empty warningMessages}">
				<div class="alert alert-warning" role="alert">
					
					<c:forEach items="${warningMessages}" var="message"> 
						<p> <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true">&nbsp;</span>
  							${message}
  						</p>
					</c:forEach>
					
				</div>	
			</c:if>
			<c:if test="${not empty errorMessages}">
				<div class="alert alert-danger" role="alert">
					
					<c:forEach items="${errorMessages}" var="message"> 
						<p> <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true">&nbsp;</span>
  							${message}
  						</p>
					</c:forEach>
					
				</div>	
			</c:if>


<div class="panel panel-primary">
	<div class="panel-heading">
	</div>
	<div class="panel-body">
		<form method="post" class="form-horizontal">
			<table class="table">
				<tbody>
					<tr>
						<th scope="row" class="col-xs-2"><spring:message code="label.FirstYearRegistrationConfiguration.mod43TemplateFile"/></th> 
						<td class="col-xs-10">
							<c:if test="${not empty firstYearRegistrationGlobalConfiguration.mod43Template}"><c:out value='${firstYearRegistrationGlobalConfiguration.mod43Template.displayName}'/><a class="trash-link" href="${pageContext.request.contextPath}/fenixedu-ulisboa-specifications/firstyearconfiguration/firstyearregistrationconfiguration/cleanTemplate"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span>&nbsp;<spring:message code="action.delete"/></a></c:if>
							<c:if test="${empty firstYearRegistrationGlobalConfiguration.mod43Template}"><em><spring:message code="label.FirstYearRegistrationConfiguration.mod43TemplateFile.noTemplate"/></em></c:if>
						</td> 
					</tr>
				</tbody>
			</table>
		</form>
	</div>
</div>


<c:choose>
	<c:when test="${not empty searchfirstyearregistrationconfigurationResultsDataSet}">
		<table id="searchfirstyearregistrationconfigurationTable" class="table responsive table-bordered table-hover">
			<thead>
				<tr>
					<%--!!!  Field names here --%>
					<th><spring:message code="label.FirstYearRegistrationConfiguration.degreeName"/></th>
					<th><spring:message code="label.FirstYearRegistrationConfiguration.degreeCode"/></th>
<th><spring:message code="label.FirstYearRegistrationConfiguration.requiresVaccination"/></th>
<th><spring:message code="label.FirstYearRegistrationConfiguration.requiresCoursesEnrolment"/></th>
<th><spring:message code="label.FirstYearRegistrationConfiguration.requiresClassesEnrolment"/></th>
<th><spring:message code="label.FirstYearRegistrationConfiguration.requiresShiftsEnrolment"/></th>
				</tr>
			</thead>
			<tbody>
				
			</tbody>
		</table>
	</c:when>
	<c:otherwise>
				<div class="alert alert-warning" role="alert">
					
					<p> <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true">&nbsp;</span>			<spring:message code="label.noResultsFound" /></p>
					
				</div>	
		
	</c:otherwise>
</c:choose>

<style>
.trash-link {
	margin-left: 20px;
}
</style>

<script>
	var searchfirstyearregistrationconfigurationDataSet = [
			<c:forEach items="${searchfirstyearregistrationconfigurationResultsDataSet}" var="searchResult">
				{
				"DT_RowId" : '<c:out value='${searchResult.degreeExternalId}'/>',
"degreeName" : "${searchResult.degreeName}",
"degreeCode" : "${searchResult.degreeCode}",
"requiresvaccination" : "<c:if test="${searchResult.requiresVaccination}"><spring:message code="label.true" /></c:if><c:if test="${not searchResult.requiresVaccination}"><spring:message code="label.false" /></c:if>",
"requirescoursesenrolment" : "<c:if test="${searchResult.requiresCoursesEnrolment}"><spring:message code="label.true" /></c:if><c:if test="${not searchResult.requiresCoursesEnrolment}"><spring:message code="label.false" /></c:if>",
"requiresclassesenrolment" : "<c:if test="${searchResult.requiresClassesEnrolment}"><spring:message code="label.true" /></c:if><c:if test="${not searchResult.requiresClassesEnrolment}"><spring:message code="label.false" /></c:if>",
"requiresshiftsenrolment" : "<c:if test="${searchResult.requiresShiftsEnrolment}"><spring:message code="label.true" /></c:if><c:if test="${not searchResult.requiresShiftsEnrolment}"><spring:message code="label.false" /></c:if>",
			},
            </c:forEach>
    ];
	
	$(document).ready(function() {

	


		var table = $('#searchfirstyearregistrationconfigurationTable').DataTable({language : {
			url : "${datatablesI18NUrl}",			
		},
		"columns": [
			{ data: 'degreeName' },
			{ data: 'degreeCode' },
			{ data: 'requiresvaccination' },
			{ data: 'requirescoursesenrolment' },
			{ data: 'requiresclassesenrolment' },
			{ data: 'requiresshiftsenrolment' },
			
		],
		"data" : searchfirstyearregistrationconfigurationDataSet,
		//Documentation: https://datatables.net/reference/option/dom
"dom": '<"col-sm-6"l><"col-sm-3"f><"col-sm-3"T>rtip', //FilterBox = YES && ExportOptions = YES
//"dom": 'T<"clear">lrtip', //FilterBox = NO && ExportOptions = YES
//"dom": '<"col-sm-6"l><"col-sm-6"f>rtip', //FilterBox = YES && ExportOptions = NO
//"dom": '<"col-sm-6"l>rtip', // FilterBox = NO && ExportOptions = NO
        "tableTools": {
            "sSwfPath": "${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/swf/copy_csv_xls_pdf.swf"        	
        }
		});
		table.columns.adjust().draw();
		
		  $('#searchfirstyearregistrationconfigurationTable tbody').on( 'click', 'tr', function () {
		        $(this).toggleClass('selected');
		    } );
		  
	}); 
</script>

