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
	<h1><spring:message code="label.firstTimeCandidacy.fillResidenceInformation" />
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
						code="label.ResidenceInformationForm.countryOfResidence" />
				</div>

				<div class="col-sm-10">
					<select id="residenceInformationForm_countryOfResidence"
						class="js-example-basic-single" name="countryOfResidence">
					</select>
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.ResidenceInformationForm.address" />
				</div>

				<div class="col-sm-10">
					<input id="residenceInformationForm_address" class="form-control"
						type="text" name="address"
						value='<c:out value='${not empty param.address ? param.address : residenceInformationForm.address }'/>' />
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.ResidenceInformationForm.areaCode" />
				</div>

				<div class="col-sm-10">
					<select id="residenceInformationForm_areaCode" class="form-control" name="areaCode" >
						<c:if test="${not empty residenceInformationForm.areaCode}">
							<option selected value="${residenceInformationForm.areaCode}">${residenceInformationForm.areaCode}</option> 
						</c:if>
					</select>
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.ResidenceInformationForm.area" />
				</div>

				<div class="col-sm-10">
					<input id="residenceInformationForm_area" class="form-control"
						type="text" name="area"
						value='<c:out value='${not empty param.area ? param.area : residenceInformationForm.area }'/>' />
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message
						code="label.ResidenceInformationForm.districtOfResidence" />
				</div>

				<div class="col-sm-10">
					<select id="residenceInformationForm_districtOfResidence"
						class="js-example-basic-single" name="districtOfResidence">
						<option value=""></option>
					</select>
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message
						code="label.ResidenceInformationForm.districtSubdivisionOfResidence" />
				</div>

				<div class="col-sm-10">
					<select
						id="residenceInformationForm_districtSubdivisionOfResidence"
						class="js-example-basic-single"
						name="districtSubdivisionOfResidence">
						<option value=""></option>
					</select>
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message
						code="label.ResidenceInformationForm.parishOfResidence" />
				</div>

				<div class="col-sm-10">
					<select id="residenceInformationForm_parishOfResidence" class="form-control" name="parishOfResidence">
						<option value=""></option>
					</select>
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message
						code="label.ResidenceInformationForm.dislocatedFromPermanentResidence" />
				</div>

				<div class="col-sm-2">
					<select
						id="residenceInformationForm_dislocatedFromPermanentResidence"
						name="dislocatedFromPermanentResidence" class="form-control">
						<option value="false"><spring:message code="label.no" /></option>
						<option value="true"><spring:message code="label.yes" /></option>
					</select>
					<script>
						$("#residenceInformationForm_dislocatedFromPermanentResidence").val('<c:out value='${not empty param.dislocatedfrompermanentresidence ? param.dislocatedfrompermanentresidence : residenceInformationForm.dislocatedFromPermanentResidence }'/>');
					</script>
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message
						code="label.ResidenceInformationForm.schoolTimeAddress" />
				</div>

				<div class="col-sm-10">
					<input id="residenceInformationForm_schoolTimeAddress"
						class="form-control" type="text" name="schoolTimeAddress"
						value='<c:out value='${not empty param.schooltimeaddress ? param.schooltimeaddress : residenceInformationForm.schoolTimeAddress }'/>' />
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message
						code="label.ResidenceInformationForm.schoolTimeAreaCode" />
				</div>

				<div class="col-sm-10">
					<select id="residenceInformationForm_schoolTimeAreaCode" class="form-control"  name="schoolTimeAreaCode">
						<c:if test="${not empty residenceInformationForm.schoolTimeAreaCode}">
							<option selected value="${residenceInformationForm.schoolTimeAreaCode}">${residenceInformationForm.schoolTimeAreaCode}</option> 
						</c:if>
					</select>
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message
						code="label.ResidenceInformationForm.schoolTimeArea" />
				</div>

				<div class="col-sm-10">
					<input id="residenceInformationForm_schoolTimeArea"
						class="form-control" type="text" name="schoolTimeArea"
						value='<c:out value='${not empty param.schooltimearea ? param.schooltimearea : residenceInformationForm.schoolTimeArea }'/>' />
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message
						code="label.ResidenceInformationForm.schoolTimeDistrictOfResidence" />
				</div>

				<div class="col-sm-10">
					<select id="residenceInformationForm_schoolTimeDistrictOfResidence"
						class="js-example-basic-single"
						name="schoolTimeDistrictOfResidence">
						<option value=""></option>
					</select>
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message
						code="label.ResidenceInformationForm.schoolTimeDistrictSubdivisionOfResidence" />
				</div>

				<div class="col-sm-10">
					<select
						id="residenceInformationForm_schoolTimeDistrictSubdivisionOfResidence"
						class="js-example-basic-single"
						name="schoolTimeDistrictSubdivisionOfResidence">
						<option value=""></option>
					</select>
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message
						code="label.ResidenceInformationForm.schoolTimeParishOfResidence" />
				</div>

				<div class="col-sm-10">
					<select id="residenceInformationForm_schoolTimeParishOfResidence" class="form-control" name="schoolTimeParishOfResidence">
						<option value=""></option>
					</select>
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.ResidenceInformationForm.schoolTimeResidenceType" />
				</div>

				<div class="col-sm-4">
					<select id="residenceInformationForm_schoolTimeResidenceType" class="form-control" name="schoolTimeResidenceType">
						<option value=""></option>
						<c:forEach items="${residenceTypeValues}" var="residenceTypeValue">
							<option value='<c:out value='${residenceTypeValue.externalId}'/>'><c:out value='${residenceTypeValue.description.content}' /></option>
						</c:forEach>
					</select>
					<script>
						$("#residenceInformationForm_schoolTimeResidenceType").val('<c:out value='${not empty param.schooltimeresidencetype ? param.schooltimeresidencetype : residenceInformationForm.schoolTimeResidenceType.externalId }'/>');
					</script>
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.ResidenceInformationForm.otherSchoolTimeResidenceType" />
				</div>

				<div class="col-sm-10">
					<input id="residenceInformationForm_otherSchoolTimeResidenceType" class="form-control" type="text" name="otherSchoolTimeResidenceType"
						value='<c:out value='${not empty param.otherschooltimeresidencetype ? param.otherschooltimeresidencetype : residenceInformationForm.otherSchoolTimeResidenceType }'/>' />
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
	             			<c:forEach items="${countries_options}" var="element"> 
	             				{
	             					text : "<c:out value='${element.name}'/>",  
	             					id : "<c:out value='${element.externalId}'/>"
	             				},
	             			</c:forEach>
	             		];
	             		$("#residenceInformationForm_countryOfResidence").select2(
	             			{
	             				data : country_options,
	             			}	  
	             	    );
	             	    
	             	    $("#residenceInformationForm_countryOfResidence").select2().select2('val', '<c:out value='${residenceInformationForm.countryOfResidence.externalId}'/>');
    
     	 //setup districts of residence
        	district_options = [
  	             			<c:forEach items="${districts_options}" var="element"> 
  	             				{
  	             					text : "<c:out value='${element.name}'/>",  
  	             					id : "<c:out value='${element.externalId}'/>"
  	             				},
  	             			</c:forEach>
  	             		];
  	
  	             	   $("#residenceInformationForm_districtOfResidence").select2(
  		             			{
  		             				data : district_options,
  		             			}	  
  		             	    );
  		             	    
  		             	    $("#residenceInformationForm_districtOfResidence").select2().select2('val', '<c:out value='${residenceInformationForm.districtOfResidence.externalId}'/>');
		             	 $("#residenceInformationForm_districtOfResidence").select2().on("select2:select", function(e) {
		                   populateSubDistrictsOfResidence(e);
		                 })
 		             	    
          	 populateSubDistrictsOfResidence = function(){
          		 oid = $("#residenceInformationForm_districtOfResidence")[0].value; 
          		 $.ajax({url : "${pageContext.request.contextPath}/fenixedu-ulisboa-specifications/firsttimecandidacy/residenceinformationform/district/" + oid, 
          				success: function(result){
          					 $("#residenceInformationForm_districtSubdivisionOfResidence").children().remove();
          					 $("#residenceInformationForm_districtSubdivisionOfResidence").select2(
        		             			{
        		             				data : result,
        		             			}	  
        		             	    );
          					$("#residenceInformationForm_districtSubdivisionOfResidence").select2().select2('val', '<c:out value='${residenceInformationForm.districtSubdivisionOfResidence.externalId}'/>');

          					$("#residenceInformationForm_districtSubdivisionOfResidence").select2().select2('val', '');
          					$("#residenceInformationForm_parishOfResidence").select2().select2('val', '');
          		 		}
          		 });
          		 
          	 }
    
         	//setup sub-districts of residence
         	$("#residenceInformationForm_districtSubdivisionOfResidence").select2();
         	<c:if test="${not empty residenceInformationForm.districtOfResidence}">
         	subdistrict_options = [
   	             			<c:forEach items="${residenceInformationForm.districtOfResidence.districtSubdivisions}" var="element"> 
   	             				{
   	             					text : "<c:out value='${element.name}'/>",  
   	             					id : "<c:out value='${element.externalId}'/>"
   	             				},
   	             			</c:forEach>
   	             		];
   	
   	             	   $("#residenceInformationForm_districtSubdivisionOfResidence").select2(
   		             			{
   		             				data : subdistrict_options,
   		             			}	  
   		             	    );
   		             	    
   		             	    $("#residenceInformationForm_districtSubdivisionOfResidence").select2().select2('val', '<c:out value='${residenceInformationForm.districtSubdivisionOfResidence.externalId}'/>');
   	
           	</c:if>
           	
       	 $("#residenceInformationForm_districtSubdivisionOfResidence").select2().on("select2:select", function(e) {
       		populateParishesOfResidence(e);
           })
       	    
		 populateParishesOfResidence = function(){
			 oid = $("#residenceInformationForm_districtSubdivisionOfResidence")[0].value; 
			 $.ajax({url : "${pageContext.request.contextPath}/fenixedu-ulisboa-specifications/firsttimecandidacy/residenceinformationform/districtSubdivision/" + oid, 
					success: function(result){
						 //$("#filiationForm_districtSubdivisionOfBirth").select2("destroy");
						 $("#residenceInformationForm_parishOfResidence").children().remove();
						 $("#residenceInformationForm_parishOfResidence").select2(
			             			{
			             				data : result,
			             			}	  
			             	    );
						$("#residenceInformationForm_parishOfResidence").select2();
      					$("#residenceInformationForm_parishOfResidence").select2().select2('val', '');
			 		}
			 });
			 
		 }
     	
     	//setup parishes of residence
	   	$("#residenceInformationForm_parishOfResidence").select2();
	   	<c:if test="${not empty residenceInformationForm.districtSubdivisionOfResidence}">
	   	subDistrictOptions = [
		             			<c:forEach items="${residenceInformationForm.districtSubdivisionOfResidence.parish}" var="element"> 
		             				{
		             					text : "<c:out value='${element.name}'/>",  
		             					id : "<c:out value='${element.externalId}'/>"
		             				},
		             			</c:forEach>
		             		];
		
		             	   $("#residenceInformationForm_parishOfResidence").select2(
			             			{
			             				data : subDistrictOptions,
			             			}	  
			             	    );
			             	    
			             	    $("#residenceInformationForm_parishOfResidence").select2().select2('val', '<c:out value='${residenceInformationForm.parishOfResidence.externalId}'/>');
		
	     	</c:if>
	     	       	
            //setup districts of residence in school time
        	  	
  	             	   $("#residenceInformationForm_schoolTimeDistrictOfResidence").select2(
  		             			{
  		             				data : district_options,
  		             			}	  
  		             	    );
  		             	    
  		             	    $("#residenceInformationForm_schoolTimeDistrictOfResidence").select2().select2('val', '<c:out value='${residenceInformationForm.schoolTimeDistrictOfResidence.externalId}'/>');
  		             	 $("#residenceInformationForm_schoolTimeDistrictOfResidence").select2().on("select2:select", function(e) {
  		                   populateSubDistrictsOfResidenceInSchoolTime(e);
  		                 })
  		             	    
          	 populateSubDistrictsOfResidenceInSchoolTime = function(){
          		 oid = $("#residenceInformationForm_schoolTimeDistrictOfResidence")[0].value; 
          		 $.ajax({url : "${pageContext.request.contextPath}/fenixedu-ulisboa-specifications/firsttimecandidacy/residenceinformationform/district/" + oid, 
          				success: function(result){
          					 $("#residenceInformationForm_schoolTimeDistrictSubdivisionOfResidence").children().remove();
          					 $("#residenceInformationForm_schoolTimeDistrictSubdivisionOfResidence").select2(
        		             			{
        		             				data : result,
        		             			}	  
        		             	    );
          					$("#residenceInformationForm_schoolTimeDistrictSubdivisionOfResidence").select2().select2('val', '<c:out value='${residenceInformationForm.schoolTimeDistrictSubdivisionOfResidence.externalId}'/>');
          					$("#residenceInformationForm_schoolTimeDistrictSubdivisionOfResidence").select2().select2('val', '');
          					$("#residenceInformationForm_schoolTimeParishOfResidence").select2().select2('val', '');
          		 		}
          		 });
          		 
          	 }
    
         	//setup sub-districts of residence in school time
         	$("#residenceInformationForm_schoolTimeDistrictSubdivisionOfResidence").select2();
         	<c:if test="${not empty residenceInformationForm.schoolTimeDistrictOfResidence}">
         	subdistrict_options_schoolTime = [
   	             			<c:forEach items="${residenceInformationForm.schoolTimeDistrictOfResidence.districtSubdivisions}" var="element"> 
   	             				{
   	             					text : "<c:out value='${element.name}'/>",  
   	             					id : "<c:out value='${element.externalId}'/>"
   	             				},
   	             			</c:forEach>
   	             		];
   	
   	             	   $("#residenceInformationForm_schoolTimeDistrictSubdivisionOfResidence").select2(
   		             			{
   		             				data : subdistrict_options_schoolTime,
   		             			}	  
   		             	    );
   		             	    
   		             	    $("#residenceInformationForm_schoolTimeDistrictSubdivisionOfResidence").select2().select2('val', '<c:out value='${residenceInformationForm.schoolTimeDistrictSubdivisionOfResidence.externalId}'/>');
   	
           	</c:if>
           	
           	
           	//Setup postal codes
           	
    		$("#residenceInformationForm_areaCode").select2(
    				{
    				  ajax: {
    				    url: "${pageContext.request.contextPath}/fenixedu-ulisboa-specifications/firsttimecandidacy/residenceinformationform/postalCode",
    				    dataType: 'json',
    				    delay: 250,
    				    data: function (params) {
    				      return {
   				    	  	postalCodePart: params.term, // search term
    				        page: params.page
    				      };
    				    },
    				    processResults: function (data, page) {
    				      newData = []
    				      for(var result in data){
    				    	  newData[result] = {
    				    			  text : data[result],
    				    			  id : data[result],
    				    	  }
    				      }
    				      return {
    				        results: newData
    				      };
    				    },
    				    cache: true
    				  }});
           	

    		$("#residenceInformationForm_schoolTimeAreaCode").select2(
    				{
    				  ajax: {
    				    url: "${pageContext.request.contextPath}/fenixedu-ulisboa-specifications/firsttimecandidacy/residenceinformationform/postalCode",
    				    dataType: 'json',
    				    delay: 250,
    				    data: function (params) {
    				      return {
   				    	  	postalCodePart: params.term, // search term
    				        page: params.page
    				      };
    				    },
    				    processResults: function (data, page) {
    				      newData = []
    				      for(var result in data){
    				    	  newData[result] = {
    				    			  text : data[result],
    				    			  id : data[result],
    				    	  }
    				      }
    				      return {
    				        results: newData
    				      };
    				    },
    				    cache: true
    				  }});
	});
	
	 $("#residenceInformationForm_schoolTimeDistrictSubdivisionOfResidence").select2().on("select2:select", function(e) {
    		populateParishesOfResidenceInSchoolTime(e);
        })
    	    
		 populateParishesOfResidenceInSchoolTime = function(){
			 oid = $("#residenceInformationForm_schoolTimeDistrictSubdivisionOfResidence")[0].value; 
			 $.ajax({url : "${pageContext.request.contextPath}/fenixedu-ulisboa-specifications/firsttimecandidacy/residenceinformationform/districtSubdivision/" + oid, 
					success: function(result){
						 //$("#filiationForm_districtSubdivisionOfBirth").select2("destroy");
						 $("#residenceInformationForm_schoolTimeParishOfResidence").children().remove();
						 $("#residenceInformationForm_schoolTimeParishOfResidence").select2(
			             			{
			             				data : result,
			             			}	  
			             	    );
						$("#residenceInformationForm_schoolTimeParishOfResidence").select2();
      					$("#residenceInformationForm_schoolTimeParishOfResidence").select2().select2('val', '');
			 		}
			 });
			 
		 }
  	
  	//setup parishes of residence
	   	$("#residenceInformationForm_schoolTimeParishOfResidence").select2();
	   	<c:if test="${not empty residenceInformationForm.districtSubdivisionOfResidence}">
	   	subDistrictOptions = [
		             			<c:forEach items="${residenceInformationForm.schoolTimeDistrictSubdivisionOfResidence.parish}" var="element"> 
		             				{
		             					text : "<c:out value='${element.name}'/>",  
		             					id : "<c:out value='${element.externalId}'/>"
		             				},
		             			</c:forEach>
		             		];
		
		             	   $("#residenceInformationForm_schoolTimeParishOfResidence").select2(
			             			{
			             				data : subDistrictOptions,
			             			}	  
			             	    );
			             	    
			             	    $("#residenceInformationForm_schoolTimeParishOfResidence").select2().select2('val', '<c:out value='${residenceInformationForm.schoolTimeParishOfResidence.externalId}'/>');
		
	     	</c:if>
	
</script>
