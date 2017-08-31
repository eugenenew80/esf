angular.module("common")
	.directive("simpleControl", function ($filter, idField, dataServices, stateService, urlParamsBuilder) {
		return {

			link: function (scope, element, attrs) {
				scope.fieldValue=idField;
				scope.fieldDisplayName=scope.control.dictDisplayName;
				scope.fieldValueName=scope.control.dictValueName || idField;

				scope.templateURL="common/directives/complexFormControl/control/";
				switch(scope.control.control) {
					case "input":
						if(scope.control.dataType=="string")
							scope.templateURL=scope.templateURL + "stringInput.html";
						else if(scope.control.dataType=="integer")
							scope.templateURL=scope.templateURL + "integerInput.html";
						else if(scope.control.dataType=="number")
							scope.templateURL=scope.templateURL + "numberInput.html";
						else if(scope.control.dataType=="date")
							scope.templateURL=scope.templateURL + "dateInput.html";
						else
							scope.templateURL=scope.templateURL + "stringInput.html";

						break;

					case "select":
						if (scope.control.dataType=="object")
							scope.templateURL=scope.templateURL + "objectSelect.html";
						else if	(scope.control.dataType=="objectId")
							scope.templateURL=scope.templateURL + "objectIdSelect.html";
						else
							scope.templateURL=scope.templateURL + "objectSelect.html";

						break;

					case "checkbox":
						scope.templateURL=scope.templateURL + "inputCheckbox.html";
						break;

					case "radio":
						scope.templateURL=scope.templateURL + "inputRadio.html";
						break;
						
					case "textarea":
						scope.templateURL=scope.templateURL + "stringTextArea.html";
						break;

					case "typehead":
						if (scope.control.dataType=="object")
							scope.templateURL=scope.templateURL + "objectTypeHead.html";
						else if	(scope.control.dataType=="objectId")
							scope.templateURL=scope.templateURL + "objectIdTypeHead.html";
						else
							scope.templateURL=scope.templateURL + "objectTypeHead.html";

						break;

					case "listCheckBox":
						scope.templateURL=scope.templateURL + "listCheckBox.html";
						break;

					case "listCheckBoxSelect":
						scope.templateURL=scope.templateURL + "listCheckBoxSelect.html";
						break;

					default:
						scope.templateURL=scope.templateURL + "stringInput.html";
				}


				if (scope.control.label)
					scope.control.labelVisible=true;
				else
					scope.control.labelVisible=false;


				//Bootstrap UI datepicker options
				scope.datepicker={
					opened:false,
					options: {
						
					},
					
					format: "dd.MM.yyyy",
					
					modelOptions: {
						timezone: "0"
					}					
				};


				
				
				scope.dict={};

				//Autofill
				if (scope.control.autofill && !scope.currentElement[scope.control.name]) {
					var dataService=dataServices[scope.control.autofill.dataService];
					var methodName=scope.control.autofill.methodName;
					var fieldValue=scope.control.autofill.fieldValue;
					var fieldDisplayName=scope.control.autofill.fieldDisplayName;
					var params=urlParamsBuilder.build(scope.control.autofill.params, scope.currentElement);

					//run request
					dataService[methodName](params).$promise
						.then(function (data) {
							var item;
							if (angular.isArray(data) && data.length>0) {
								item = data[0];
								scope.fieldValueName = fieldValue;
								scope.fieldDisplayName = fieldDisplayName;
								scope.dict[scope.control.name] = data;
							}
							else
								item=data;

							if (item)
								scope.currentElement[scope.control.name] = item[fieldValue];
						});
				};

					
				if (scope.control.dictValues) {
					scope.$watch("currentElement['" + scope.control.name + "']", function (newValue, oldValue) {
						if (!newValue) return;
						
						/*var search={};
						search[scope.fieldValueName]=newValue;
						
						var item = dataServices[scope.control.dict].findElementLocal(search);						
						
						if (item[scope.fieldValueName]) {
							angular.forEach(scope.control.dictValues, function (value, key) {
								scope.currentElement[key]=item[value];
							})
						}*/
						var item = dataServices[scope.control.dict].getElementById(newValue);
						angular.forEach(scope.control.dictValues, function (value, key) {
							scope.currentElement[key]=item[value];
						})
					})
						
				}

				//Processing dictParams
				var dictParams={};
				if (scope.control.dictParams)
					dictParams=urlParamsBuilder.build(scope.control.dictParams, scope.currentElement);

				//Bind dict to select
				if (scope.control.dict && scope.control.control=="select")
					scope.dict[scope.control.name] = dataServices[scope.control.dict].getElements(dictParams);


				//Bind dict to listCheckbox
				if (scope.control.dict && scope.control.control=="listCheckBox") {

					
					scope.dict[scope.control.name] = dataServices[scope.control.dict].getElements(dictParams);

					scope.dict[scope.control.name].$promise
						.then(function (data) {
							scope.currentElement[scope.control.name]=scope.currentElement[scope.control.name] || [];
							angular.forEach(data, function (value) {
								scope.currentElement[scope.control.name].push(
									{
										id: value.id,
										name:  value[scope.fieldDisplayName],
										checked: false
									}
								);
							});
						});
				}


				//Bind dict to listCheckboxSelect
				if (scope.control.dict && scope.control.control=="listCheckBoxSelect") {
					scope.dict[scope.control.name] = dataServices[scope.control.dict].getElements(dictParams);

					scope.dict[scope.control.name].$promise
						.then(function (data) {
							scope.currentElement[scope.control.name]=scope.currentElement[scope.control.name] || [];
							angular.forEach(data, function (value) {
								
								var searchValue = $filter('filter')(scope.currentElement[scope.control.name], {code: value.type}, true)[0];
								if (searchValue) {
									searchValue.id=value.id;
									searchValue.name=value[scope.fieldDisplayName];
									searchValue.checked=true;
								}
								else {	
									scope.currentElement[scope.control.name].push({
										id: value.id,
										name:  value[scope.fieldDisplayName],
										checked: false,
										code: value.type,
										value: null
									});
								}

								scope.dict[scope.control.name + "#" + value.type]= dataServices[scope.control.dictValue].getElements({type: value.type});
							});
						});
				}

				//Watching for dependency fields
				angular.forEach(scope.control.dictParams, function(value) {
					if (value && angular.isString(value) && value.startsWith("@") && !value.substr(1).startsWith("@") ) {
						var watchedField=value.substr(1);
						scope.$watch("currentElement['" + watchedField + "']", function(newValue, oldValue) {

							if (!newValue) return;

							var dictParams=urlParamsBuilder.build(scope.control.dictParams, scope.currentElement);

							if (scope.control.control=="select")
								scope.dict[scope.control.name]=dataServices[scope.control.dict].getElements(dictParams);

							if (scope.control.control=="input") {
								dataServices[scope.control.dict].findElement(dictParams)
									.$promise
										.then(function (data) {
											if (data)
												scope.currentElement[scope.control.name]=data[scope.fieldDisplayName];
										});
							}

							if (scope.control.control=="listCheckBox") {
								scope.dict[scope.control.name] = dataServices[scope.control.dict].getElements(dictParams);

								scope.dict[scope.control.name].$promise
									.then(function (data) {
										scope.currentElement[scope.control.name]=[];
										angular.forEach(data, function (value) {
											scope.currentElement[scope.control.name].push(
												{
													id: value.id,
													name:  value[scope.fieldDisplayName],
													checked: false
												}
											);
										});
									});
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
				control: 		"=control"
			},

			template: "<ng-include src='templateURL'></ng-include>"
		}
	});