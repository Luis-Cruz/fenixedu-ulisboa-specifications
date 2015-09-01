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
	<h1><spring:message code="label.firstTimeCandidacy.fillFiliation" />
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
					<spring:message code="label.FiliationForm.nationality" />
				</div>

				<div class="col-sm-10">
					<select id="filiationForm_nationality"
						class="js-example-basic-single" name="nationality">
					</select>
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.FiliationForm.secondNationality" />
				</div>

				<div class="col-sm-10">
					<select id="filiationForm_secondNationality"
						class="js-example-basic-single" name="secondNationality">
						<option value=""></option>
					</select>
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.FiliationForm.dateOfBirth" />
				</div>

				<div class="col-sm-10">
					<input id="filiationForm_dateOfBirth" class="form-control"
						type="text" name="dateOfBirth" bennu-date
						value='<c:out value='${not empty param.dateofbirth ? param.dateofbirth : filiationForm.dateOfBirth }'/>' />
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.FiliationForm.countryOfBirth" />
				</div>

				<div class="col-sm-10">
					<select id="filiationForm_countryOfBirth"
						class="js-example-basic-single" name="countryOfBirth">
					</select>
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.FiliationForm.districtOfBirth" />
				</div>

				<div class="col-sm-10">
					<select id="filiationForm_districtOfBirth" class="js-example-basic-single" name="districtOfBirth">
						<option value=""></option>
					</select>
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message
						code="label.FiliationForm.districtSubdivisionOfBirth" />
				</div>

				<div class="col-sm-10">
					<select id="filiationForm_districtSubdivisionOfBirth" class="js-example-basic-single" name="districtSubdivisionOfBirth">
						<option value=""></option>
					</select>
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.FiliationForm.parishOfBirth" />
				</div>

				<div class="col-sm-10">
					<select id="filiationForm_parishOfBirth" class="js-example-basic-single"  name="parishOfBirth">
						<option value=""></option>
					</select>
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.FiliationForm.fatherName" />
				</div>

				<div class="col-sm-10">
					<input id="filiationForm_fatherName" class="form-control"
						type="text" name="fatherName" required
						value='<c:out value='${not empty param.fathername ? param.fathername : filiationForm.fatherName }'/>' />
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.FiliationForm.motherName" />
				</div>

				<div class="col-sm-10">
					<input id="filiationForm_motherName" class="form-control"
						type="text" name="motherName" required
						value='<c:out value='${not empty param.mothername ? param.mothername : filiationForm.motherName }'/>' />
				</div>
			</div>
		</div>
		<div class="panel-footer">
			<input type="submit" class="btn btn-default" role="button"
				value="<spring:message code="label.submit" />" />
		</div>
	</div>
</form>

<script>
$(document).ready(function() {
	//setup country of birth	             		
	country_options = [
	             			<c:forEach items="${countries_options}" var="element"> 
	             				{
	             					text : "<c:out value='${element.name}'/>",  
	             					id : "<c:out value='${element.externalId}'/>"
	             				},
	             			</c:forEach>
	             		];
	             		$("#filiationForm_countryOfBirth").select2(
	             			{
	             				data : country_options,
	             			}	  
	             	    );
	             	    
	             	    $("#filiationForm_countryOfBirth").select2().select2('val', '<c:out value='${filiationForm.countryOfBirth.externalId}'/>');
    
    //setup nationalities
    	nationality_options = [
	             			<c:forEach items="${countries_options}" var="element"> 
	             				{
	             					text : "<c:out value='${element.nationality}'/>",  
	             					id : "<c:out value='${element.externalId}'/>"
	             				},
	             			</c:forEach>
	             		];
	
	             	   $("#filiationForm_nationality").select2(
		             			{
		             				data : nationality_options,
		             			}	  
		             	    );
		             	    
	             	    $("#filiationForm_nationality").select2().select2('val', '<c:out value='${filiationForm.nationality.externalId}'/>');
		             	    
    //setup secondNationalities
    	secondNationality_options = [
	             			<c:forEach items="${countries_options}" var="element"> 
	             				{
	             					text : "<c:out value='${element.nationality}'/>",  
	             					id : "<c:out value='${element.externalId}'/>"
	             				},
	             			</c:forEach>
	             		];
	
	             	   $("#filiationForm_secondNationality").select2(
		             			{
		             				data : secondNationality_options,
		             			}	  
		             	    );
		             	    
		             	    $("#filiationForm_secondNationality").select2().select2('val', '<c:out value='${filiationForm.secondNationality.externalId}'/>');
		             			             	    
     	 //setup districts
        	district_options = [
  	             			<c:forEach items="${districts_options}" var="element"> 
  	             				{
  	             					text : "<c:out value='${element.name}'/>",  
  	             					id : "<c:out value='${element.externalId}'/>"
  	             				},
  	             			</c:forEach>
  	             		];
  	
  	             	   $("#filiationForm_districtOfBirth").select2(
  		             			{
  		             				data : district_options,
  		             			}	  
  		             	    );
  		             	    
  		             	    $("#filiationForm_districtOfBirth").select2().select2('val', '<c:out value='${filiationForm.districtOfBirth.externalId}'/>');
  		             	 $("#filiationForm_districtOfBirth").select2().on("select2:select", function(e) {
  		                   populateSubDistricts(e);
  		                 })
  		             	    
          	 populateSubDistricts = function(){
          		 oid = $("#filiationForm_districtOfBirth")[0].value; 
          		 $.ajax({url : "${pageContext.request.contextPath}/fenixedu-ulisboa-specifications/firsttimecandidacy/filiationform/district/" + oid, 
          				success: function(result){
          					 //$("#filiationForm_districtSubdivisionOfBirth").select2("destroy");
          					 $("#filiationForm_districtSubdivisionOfBirth").children().remove();
          					 $("#filiationForm_districtSubdivisionOfBirth").select2(
        		             			{
        		             				data : result,
        		             			}	  
        		             	    );
          					$("#filiationForm_districtSubdivisionOfBirth").select2();
          					$("#filiationForm_districtSubdivisionOfBirth").select2().select2('val', '');
          					$("#filiationForm_parishOfBirth").select2().select2('val', '');
          		 		}
          		 });
          		 
          	 }
    
         	//setup sub-districts
         	$("#filiationForm_districtSubdivisionOfBirth").select2();
         	<c:if test="${not empty filiationForm.districtOfBirth}">
         	subDistrictOptions = [
   	             			<c:forEach items="${filiationForm.districtOfBirth.districtSubdivisions}" var="element"> 
   	             				{
   	             					text : "<c:out value='${element.name}'/>",  
   	             					id : "<c:out value='${element.externalId}'/>"
   	             				},
   	             			</c:forEach>
   	             		];
   	
   	             	   $("#filiationForm_districtSubdivisionOfBirth").select2(
   		             			{
   		             				data : subDistrictOptions,
   		             			}	  
   		             	    );
   		             	    
   		             	    $("#filiationForm_districtSubdivisionOfBirth").select2().select2('val', '<c:out value='${filiationForm.districtSubdivisionOfBirth.externalId}'/>');
   	
           	</c:if>
           	
        	 $("#filiationForm_districtSubdivisionOfBirth").select2().on("select2:select", function(e) {
                   populateParishes(e);
                 })
             	    
			 populateParishes = function(){
				 oid = $("#filiationForm_districtSubdivisionOfBirth")[0].value; 
				 $.ajax({url : "${pageContext.request.contextPath}/fenixedu-ulisboa-specifications/firsttimecandidacy/filiationform/districtSubdivision/" + oid, 
						success: function(result){
							 //$("#filiationForm_districtSubdivisionOfBirth").select2("destroy");
							 $("#filiationForm_parishOfBirth").children().remove();
							 $("#filiationForm_parishOfBirth").select2(
				             			{
				             				data : result,
				             			}	  
				             	    );
							$("#filiationForm_parishOfBirth").select2();
          					$("#filiationForm_parishOfBirth").select2().select2('val', '');
				 		}
				 });
				 
			 }
           	
           	//setup parishes
         	$("#filiationForm_parishOfBirth").select2();
         	<c:if test="${not empty filiationForm.districtSubdivisionOfBirth}">
         	subDistrictOptions = [
   	             			<c:forEach items="${filiationForm.districtSubdivisionOfBirth.parish}" var="element"> 
   	             				{
   	             					text : "<c:out value='${element.name}'/>",  
   	             					id : "<c:out value='${element.externalId}'/>"
   	             				},
   	             			</c:forEach>
   	             		];
   	
   	             	   $("#filiationForm_parishOfBirth").select2(
   		             			{
   		             				data : subDistrictOptions,
   		             			}	  
   		             	    );
   		             	    
   		             	    $("#filiationForm_parishOfBirth").select2().select2('val', '<c:out value='${filiationForm.parishOfBirth.externalId}'/>');
   	
           	</c:if>
           	       	
           	
	});
</script>