<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="datatables" uri="http://github.com/dandelion/datatables"%>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>

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
	<h1><spring:message code="label.firstTimeCandidacy.fillDisabilities" />
		<small></small>
	</h1>
</div>

<%-- NAVIGATION --%>
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

<form method="post" class="form-horizontal">
	<div class="panel panel-default">
		<div class="panel-body">
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.DisabilitiesForm.hasDisabilities" />
				</div>

				<div class="col-sm-2">
					<select id="disabilitiesForm_hasDisabilities" name="hasDisabilities" class="form-control">
						<option value="false"><spring:message code="label.no" /></option>
						<option value="true"><spring:message code="label.yes" /></option>
					</select>
					<script>
						$("#disabilitiesForm_hasDisabilities").val('<c:out value='${not empty param.hasdisabilities ? param.hasdisabilities : disabilitiesForm.hasDisabilities }'/>');
					</script>
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.DisabilitiesForm.disabilityType" />
				</div>

				<div class="col-sm-4">
					<select id="disabilitiesForm_disabilityType" class="form-control" name="disabilityType">
						<option value=""></option>
						<c:forEach items="${disabilityTypeValues}" var="disabilityTypeValue">
							<option value='<c:out value='${disabilityTypeValue.externalId}'/>'><c:out value='${disabilityTypeValue.description.content}' /></option>
						</c:forEach>
					</select>
					<script>
						$("#disabilitiesForm_disabilityType").val('<c:out value='${not empty param.disabilitytype ? param.disabilitytype : disabilitiesForm.disabilityType.externalId }'/>');
					</script>
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.DisabilitiesForm.otherDisabilityType" />
				</div>

				<div class="col-sm-10">
					<input id="disabilitiesForm_otherDisabilityType" class="form-control" type="text" name="otherDisabilityType"
						value='<c:out value='${not empty param.otherdisabilitytype ? param.otherdisabilitytype : disabilitiesForm.otherDisabilityType }'/>' />
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message
						code="label.DisabilitiesForm.needsDisabilitySupport" />
				</div>

				<div class="col-sm-2">
					<select id="disabilitiesForm_needsDisabilitySupport" name="needsDisabilitySupport" class="form-control">
						<option value=""></option>
						<option value="false"><spring:message code="label.no" /></option>
						<option value="true"><spring:message code="label.yes" /></option>
					</select>
					<script>
						$("#disabilitiesForm_needsDisabilitySupport").val('<c:out value='${not empty param.needsdisabilitysupport ? param.needsdisabilitysupport : disabilitiesForm.needsDisabilitySupport }'/>');
					</script>
				</div>
			</div>
		</div>
		<div class="panel-footer">
			<input type="submit" class="btn btn-default" role="button" value="<spring:message code="label.submit" />" />
		</div>
	</div>
</form>

<style>
	.form-control[disabled] {
    	background: #dddddd;
	}
</style>
<script>
$(document).ready(function() {
		function changeFieldsState(bool){
			$("#disabilitiesForm_disabilityType").attr("disabled", bool);
			$("#disabilitiesForm_otherDisabilityType").attr("disabled", bool);
			$("#disabilitiesForm_needsDisabilitySupport").attr("disabled", bool);
		}
		<c:if test="${!disabilitiesForm.hasDisabilities}">
			changeFieldsState(true);
		</c:if>
		
		$("#disabilitiesForm_hasDisabilities").on("change", function(){
			if($("#disabilitiesForm_hasDisabilities").val() == "true"){
				changeFieldsState(false);
			}
			else{
				changeFieldsState(true);	
			}
		});
});
</script>
