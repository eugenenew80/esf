angular.module("common")
	.directive("complexFormControl", function (idField, dataServices) {
		return {
			
			link: function (scope, element, attrs) {
				
				scope.id_field=idField;
				scope.templateURL="common/directives/complexFormControl/complexFormControlTemplate.html";
				
				//generate contols' names
				for (var i=0; i<scope.field.controls.length; i++) {
					var control = scope.field.controls[i]; 
					control.controlName = control.dataType + control.name.charAt(0).toUpperCase() + control.name.slice(1) + Math.floor(Math.random()*100000);
				
					if (control.label) 
						control.labelVisible=true;
					else
						control.labelVisible=false;
					
					if (control.defaultValue && !scope.currentElement[control.name])
						scope.currentElement[control.name]=control.defaultValue;
				}
				
				if (scope.field.label) 
					scope.field.labelVisible=true;
				else
					scope.field.labelVisible=false;
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