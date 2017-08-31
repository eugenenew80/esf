angular.module("common")
	.directive("formControl", function (idField, dataServices, urlParamsBuilder) {
		return {
			
			link: function (scope, element, attrs) {
				scope.id_field=idField;
			
				var formControl = {};
				formControl.id=scope.field.name;
				formControl.name=scope.field.name;
				formControl.desc=scope.field.desc;
				formControl.dataType=scope.field.dataType;
				formControl.model=scope.field.name;
				formControl.dict=scope.field.dict;
				formControl.dictDisplayName=scope.field.dictDisplayName;
				formControl.dependencyField=scope.field.dependencyField;
				
				if (!angular.isUndefined(scope.field.editable))
					formControl.editable=scope.field.editable;
				else
					formControl.editable=true;
				
				
				formControl.classes = attrs["classes"] || scope.field.classes;
				formControl.labelClasses = attrs["labelClasses"] || scope.field.labelClasses;
				formControl.control=attrs["control"] || scope.field.control;
				formControl.required = attrs["required"] ? (attrs["required"] == "true") : scope.field.required;

				formControl.id  =formControl.dataType + formControl.id.charAt(0).toUpperCase()   + formControl.id.slice(1);
				formControl.name=formControl.dataType + formControl.name.charAt(0).toUpperCase() + formControl.name.slice(1) + Math.floor(Math.random()*100000);				
				scope.formControl=formControl;
				

				//Bootstrap UI datepicker options
				scope.datepicker={
					opened:false,
					options: {},
					format: "dd.MM.yyyy"
				}	
				
				var template = "common/directives/formControl/";
				switch(formControl.dataType) {				 
				  case "string":  
 						template=template + "stringInput.html";  
					    break;

				  case "integer":  
						template=template + "integerInput.html";    
					    break;

				  case "number":  
						template=template + "numberInput.html";    
					    break;
					    
				  case "date": 
						template=template + "dateInput.html";
						break;

				  case "object": 						
					  	template=template + (formControl.control=="typehead" ? "objectTypeHead.html" : "objectSelect.html");
						break;
						
				  case "objectId": 						
					  	template=template + (formControl.control=="typehead" ? "objectIdTypeHead.html" : "objectIdSelect.html");
						break;
						
				  default:
					  	template=template + "stringInput.html";
				    	break;
				}					
				scope.templateURL=template;
				
				
				//Processing dictParams
				var dictParams={};
				if (scope.field.dictParams) 
					dictParams=urlParamsBuilder.build(scope.field.dictParams, scope.currentElement);
									
				scope.dict={};
				if (formControl.dict) 
					scope.dict[formControl.model]=dataServices[formControl.dict].getElements(dictParams);

					
				//Watching for dependency fields
				angular.forEach(scope.field.dictParams, function(value) {
					if (value && angular.isString(value) && value.startsWith("@") && !value.substr(1).startsWith("@") ) {
						scope.$watch("currentElement['" + value.substr(1) + "']", function(newValue, oldValue) {
							
							var dictParams=urlParamsBuilder.build(scope.field.dictParams, scope.currentElement);
							scope.dict[formControl.model]=dataServices[formControl.dict].getElements(dictParams);
							
							if (newValue && !formControl.editable) {						
								scope.dict[formControl.model].$promise
									.then(function(items) {
										if (items.length>0)
											scope.currentElement[formControl.model]=items[0][idField];
								})	
							}														
						});
					}
				});
			
			},
			
			restrict: "EA",
			replace: true,
			
			scope: {
				currentElement: "=element",
				form: 			"=form",
				field: 			"=field"
			},
			
			template: "<ng-include src='templateURL'></ng-include>"
		}
	});