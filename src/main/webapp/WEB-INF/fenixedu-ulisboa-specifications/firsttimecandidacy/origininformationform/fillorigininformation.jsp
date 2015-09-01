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
	<h1><spring:message code="label.firstTimeCandidacy.fillOriginInformation" />
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
					<spring:message
						code="label.OriginInformationForm.countryWhereFinishedPreviousCompleteDegree" />
				</div>

				<div class="col-sm-10">
					<select
						id="originInformationForm_countryWhereFinishedPreviousCompleteDegree"
						class="form-control"
						name="countryWhereFinishedPreviousCompleteDegree">
					</select>

				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message
						code="label.OriginInformationForm.districtWhereFinishedPreviousCompleteDegree" />
				</div>

				<div class="col-sm-10">
					<select
						id="originInformationForm_districtWhereFinishedPreviousCompleteDegree"
						class="form-control"
						name="districtWhereFinishedPreviousCompleteDegree">
						<option value=""></option>
					</select>

				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message
						code="label.OriginInformationForm.districtSubdivisionWhereFinishedPreviousCompleteDegree" />
				</div>

				<div class="col-sm-10">
					<select
						id="originInformationForm_districtSubdivisionWhereFinishedPreviousCompleteDegree"
						class="form-control"
						name="districtSubdivisionWhereFinishedPreviousCompleteDegree">
						<option value=""></option>
					</select>

				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.OriginInformationForm.schoolLevel" />
				</div>

				<div class="col-sm-4">
					<select id="originInformationForm_schoolLevel" class="form-control"
						name="schoolLevel">
						<option value=""></option>
						<c:forEach items="${schoolLevelValues}" var="field">
							<option value='<c:out value='${field}'/>'><c:out
									value='${field.localizedName}' /></option>
						</c:forEach>
					</select>
					<script>
						$("#originInformationForm_schoolLevel").val('<c:out value='${not empty param.schoollevel ? param.schoollevel : originInformationForm.schoolLevel }'/>');
					</script>
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.OriginInformationForm.otherSchoolLevel" />
				</div>

				<div class="col-sm-10">
					<input id="originInformationForm_otherSchoolLevel"
						class="form-control" type="text" name="otherSchoolLevel"
						value='<c:out value='${not empty param.otherschoollevel ? param.otherschoollevel : originInformationForm.otherSchoolLevel }'/>' />
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.OriginInformationForm.institution" />
				</div>

				<div class="col-sm-10">
					<select id="originInformationForm_institution" class="form-control" name="institutionOid">
						<option value=""></option>
						<c:if test="${originInformationForm.institutionOid != null}">
							<option value="${originInformationForm.institutionOid}" selected><c:out value='${originInformationForm.institutionName}'/></option>
						</c:if>
					</select>
				</div>
			</div>
			<div class="form-group row"
				id="originInformationForm_degreeDesignation_row">
				<div class="col-sm-2 control-label">
					<spring:message
						code="label.OriginInformationForm.degreeDesignation" />
				</div>

				<div class="col-sm-10">
					<input id="originInformationForm_degreeDesignation"
						class="form-control" type="text" name="degreeDesignation"
						value='<c:out value='${not empty param.degreedesignation ? param.degreedesignation : originInformationForm.degreeDesignation }'/>' />
				</div>
			</div>
			<div class="form-group row"
				id="originInformationForm_raidesDegreeDesignation_row">
				<div class="col-sm-2 control-label">
					<spring:message
						code="label.OriginInformationForm.raidesDegreeDesignation" />
				</div>

				<div class="col-sm-10">
					<select id="originInformationForm_raidesDegreeDesignation" class="form-control" name="raidesDegreeDesignation">
						<option value="${originInformationForm.raidesDegreeDesignation.externalId}" selected><c:out value='${originInformationForm.raidesDegreeDesignation.description}'/></option>
					</select>
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.OriginInformationForm.conclusionGrade" />
				</div>

				<div class="col-sm-10">
					<input id="originInformationForm_conclusionGrade"
						class="form-control" type="text" name="conclusionGrade" required pattern="\d{2}"
						value='<c:out value='${not empty param.conclusiongrade ? param.conclusiongrade : originInformationForm.conclusionGrade }'/>' />
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.OriginInformationForm.conclusionYear" />
				</div>

				<div class="col-sm-10">
					<input id="originInformationForm_conclusionYear"
						class="form-control" type="text" name="conclusionYear" required pattern="\d{4}"
						value='<c:out value='${not empty param.conclusionyear ? param.conclusionyear : originInformationForm.conclusionYear }'/>' />
				</div>
			</div>
			<div class="form-group row"
				id="originInformationForm_highSchoolType_row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.OriginInformationForm.highSchoolType" />
				</div>

				<div class="col-sm-4">
					<select id="originInformationForm_highSchoolType"
						class="form-control" name="highSchoolType">
						<option value=""></option>
						<c:forEach items="${highSchoolTypeValues}" var="field">
							<option value='<c:out value='${field}'/>'><spring:message code="${field.name}"/></option>
						</c:forEach>
					</select>
					<script>
						$("#originInformationForm_highSchoolType").val('<c:out value='${not empty param.highschooltype ? param.highschooltype : originInformationForm.highSchoolType }'/>');
					</script>
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
	//setup country options	             		
	country_options = [
	             			<c:forEach items="${countries}" var="element"> 
	             				{
	             					text : "<c:out value='${element.name}'/>",  
	             					id : "<c:out value='${element.externalId}'/>"
	             				},
	             			</c:forEach>
	             		];
   		$("#originInformationForm_countryWhereFinishedPreviousCompleteDegree").select2(
   			{
   				data : country_options,
   			}	  
   	    );
   	    $("#originInformationForm_countryWhereFinishedPreviousCompleteDegree").select2().select2('val', '<c:out value='${originInformationForm.countryWhereFinishedPreviousCompleteDegree.externalId}'/>');

   		$("#originInformationForm_schoolLevel").trigger("change");
   		
   		updateDegreeDesignationsUrl();
	});
	//setup units provider
	ajaxData = {
		url : "${pageContext.request.contextPath}/fenixedu-ulisboa-specifications/firsttimecandidacy/origininformationform/raidesUnit/",	    
	    dataType: 'json',
	    delay: 250,
	    data: function (params) {
	      return {
	        namePart: params.term, // search term
	        page: params.page
	      };
	    },
	    processResults: function (data, page) {
	      newData = []
	      for(var result in data){
	    	  newData[result] = {
	    			  text : data[result]["unitName"],
	    			  id : data[result]["unitExternalId"],
	    	  }
	      }
	      return {
	        results: newData
	      };
	    },
	    cache: true
	  };
	
	
	updateHighSchoolType = function(){
 		schoolLevel = $("#originInformationForm_schoolLevel").val();
		country = $("#originInformationForm_countryWhereFinishedPreviousCompleteDegree option:selected").text();
		if(schoolLevel == "HIGH_SCHOOL_OR_EQUIVALENT" && country == "Portugal"){
			$("#originInformationForm_highSchoolType_row").show();
		}
		else {
			$("#originInformationForm_highSchoolType_row").hide();
		}
	}
	
	$("#originInformationForm_institution").select2({ajax: ajaxData});
	$("#originInformationForm_institution").select2('val', '<c:out value='${originInformationForm.institutionOid}'/>');
	
	$("#originInformationForm_highSchoolType_row").hide();
	
	
	schoolLevelChangeCount = 0;
	//Catch school level change events
	$("#originInformationForm_schoolLevel").on("change", function(e){
		//array of courses which are considered by fenix as being higher education
		var higherEducation = [
		                   <c:forEach items="${schoolLevelValues}" var="field">
			                   	<c:if test="${field.higherEducation}">
			                   		"${field.name}",
			                   	</c:if>
							</c:forEach>
		                   ]
		updateHighSchoolType();
		
		val = $("#originInformationForm_schoolLevel").val();
		if($.inArray(val, higherEducation) != -1){
			$("#originInformationForm_raidesDegreeDesignation_row").show();
			$("#originInformationForm_degreeDesignation_row").hide();
			ajaxData.url = "${pageContext.request.contextPath}/fenixedu-ulisboa-specifications/firsttimecandidacy/origininformationform/raidesUnit/",
			$("#originInformationForm_institution").select2({ajax: ajaxData});
		}
		else{
			ajaxData.url = "${pageContext.request.contextPath}/fenixedu-ulisboa-specifications/firsttimecandidacy/origininformationform/externalUnit/",
			$("#originInformationForm_institution").select2({ajax: ajaxData});
			$("#originInformationForm_raidesDegreeDesignation_row").hide();
			$("#originInformationForm_degreeDesignation_row").show();
		}
		
		if (schoolLevelChangeCount > 1) {
			$("#originInformationForm_institution").val("").trigger("change")
			$("#originInformationForm_raidesDegreeDesignation").val("");
			$("#originInformationForm_degreeDesignation").val("");
		}
		schoolLevelChangeCount++;
		
		if(val == "OTHER"){
			$("#originInformationForm_otherSchoolLevel").attr('readonly', false);
		}
		else{
			$("#originInformationForm_otherSchoolLevel").attr('readonly', true);
			$("#originInformationForm_otherSchoolLevel").val("");
		}
	});
	//trigger change to update screen state
	$("#originInformationForm_schoolLevel").trigger("change");
	
	ajaxDataForDegreesDesignations = {
		    dataType: 'json',
		    delay: 250,
		    data: function (params) {
		      return {
		        namePart: params.term, // search term
		        page: params.page
		      };
		    },
		    processResults: function (data, page) {
		      newData = []
		      for(var result in data){
		    	  newData[result] = {
		    			  text : data[result]["degreeDesignationText"],
		    			  id : data[result]["degreeDesignationId"],
		    	  }
		      }
		      return {
		        results: newData
		      };
		    },
		    cache: true
		  };
	
	updateDegreeDesignationsUrl = function(){
		ajaxDataForDegreesDesignations.url = "${pageContext.request.contextPath}/fenixedu-ulisboa-specifications/firsttimecandidacy/origininformationform/degreeDesignation/" + $("#originInformationForm_institution").val(); 
		$("#originInformationForm_raidesDegreeDesignation").select2({ajax: ajaxDataForDegreesDesignations});
	}
	updateDegreeDesignationsUrl();
	$("#originInformationForm_institution").select2().on("select2:select", function(e) {
		updateDegreeDesignationsUrl();
	});
	
	$("#originInformationForm_countryWhereFinishedPreviousCompleteDegree").select2().on("select2:select", function(e) {
		updateHighSchoolType();
	});
	
	
	
	
	//setup districts
	district_options = [
           			<c:forEach items="${districts_options}" var="element"> 
           				{
           					text : "<c:out value='${element.name}'/>",  
           					id : "<c:out value='${element.externalId}'/>"
           				},
           			</c:forEach>
           		];

           	   $("#originInformationForm_districtWhereFinishedPreviousCompleteDegree").select2(
	             			{
	             				data : district_options,
	             			}	  
	             	    );
	             	    
	             	    $("#originInformationForm_districtWhereFinishedPreviousCompleteDegree").select2().select2('val', '<c:out value='${originInformationForm.districtWhereFinishedPreviousCompleteDegree.externalId}'/>');
	             	 $("#originInformationForm_districtWhereFinishedPreviousCompleteDegree").select2().on("select2:select", function(e) {
	                   populateSubDistricts(e);
	                 })
	             	    
  	 populateSubDistricts = function(){
  		 oid = $("#originInformationForm_districtWhereFinishedPreviousCompleteDegree")[0].value; 
  		 $.ajax({url : "${pageContext.request.contextPath}/fenixedu-ulisboa-specifications/firsttimecandidacy/filiationform/district/" + oid, 
  				success: function(result){
  					 //$("#originInformationForm_districtSubdivisionOfBirth").select2("destroy");
  					 $("#originInformationForm_districtSubdivisionWhereFinishedPreviousCompleteDegree").children().remove();
  					 $("#originInformationForm_districtSubdivisionWhereFinishedPreviousCompleteDegree").select2(
		             			{
		             				data : result,
		             			}	  
		             	    );
  					$("#originInformationForm_districtSubdivisionWhereFinishedPreviousCompleteDegree").select2();
  					$("#originInformationForm_districtSubdivisionWhereFinishedPreviousCompleteDegree").select2().select2('val', '');
  		 		}
  		 });
  		 
  	 }

 	//setup sub-districts
 	$("#originInformationForm_districtSubdivisionWhereFinishedPreviousCompleteDegree").select2();
 	<c:if test="${not empty originInformationForm.districtWhereFinishedPreviousCompleteDegree}">
 	subDistrictOptions = [
            			<c:forEach items="${originInformationForm.districtWhereFinishedPreviousCompleteDegree.districtSubdivisions}" var="element"> 
            				{
            					text : "<c:out value='${element.name}'/>",  
            					id : "<c:out value='${element.externalId}'/>"
            				},
            			</c:forEach>
            		];

            	   $("#originInformationForm_districtSubdivisionWhereFinishedPreviousCompleteDegree").select2(
	             			{
	             				data : subDistrictOptions,
	             			}	  
	             	    );
	             	    
	             	    $("#originInformationForm_districtSubdivisionWhereFinishedPreviousCompleteDegree").select2().select2('val', '<c:out value='${originInformationForm.districtSubdivisionWhereFinishedPreviousCompleteDegree.externalId}'/>');

   	</c:if>
	
</script>
