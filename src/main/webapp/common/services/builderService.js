(function() {

	angular.module("common")

		.factory("tableHeadersBuilder", function(responsiveTableFieldBuilder) {
			
			return {
				build: function (columns) {
					
					var headers = [];
					
					headers.push(
		  				responsiveTableFieldBuilder.build({
							name: "name",
							desc: "Наименование",
							headerStyle: "min-width: 150px;",
							editable: false
						})
					);					
					
					angular.forEach(columns, function(column, key) {
						
		  				var header = responsiveTableFieldBuilder.build({
							name: "column_" + column.id,
							desc: column.name,
							headerStyle: "min-width: 30px; max-width: 120px;",
							editable: true
						});
		  				
		  				if (column.id==1 || column.id==2)
		  					header.editable = false;
		  				
						headers.push(header);
					});
					
					return headers;
				}
			}
		}) 
	
		
		.factory("tableDataBuilder", function($filter) {
			
			return {
				build: function (columns, rows, cells) {
					
					var tableData = [];
					angular.forEach(rows, function(row) {
						var tableRow={};
						tableRow.rowId=row.id;
						tableRow.name = row.name; 
						tableRow.name = row.name; 
							
						angular.forEach(columns, function(column) {
							var cell = $filter('filter')(cells, {rowId: row.id, columnId: column.id}, true);							
							if (cell.length>0) {
								tableRow["column_" + column.id] = cell[0].value;
								tableRow.finYearId = cell[0].finYearId;
								tableRow.isCoef = cell[0].isCoef;
								tableRow.lineId = cell[0].lineId;
								tableRow.transfertTypeId = cell[0].transfertTypeId;
							}
						});
						
						tableData.push(tableRow);
					});

					return tableData;
				}
			}
		}) 		
	
		.factory("cellDataBuilder", function($filter) {
			
			return {
				build: function (columns, rows, tableData) {
					
					var cells = [];
					angular.forEach(tableData, function(tableRow) {
						
						angular.forEach(columns, function(column) {
							
							if (column.id>2) {
								var cell = {};
								cell.rowId = tableRow.rowId;															
								cell.columnId = column.id;
								cell.value = tableRow["column_" + column.id];		
								
								cell.finYearId = tableRow.finYearId;
								cell.isCoef = tableRow.isCoef;
								cell.lineId = tableRow.lineId;
								cell.transfertTypeId = tableRow.transfertTypeId;
								
								cells.push(cell);
							}
						});
					});
					
					return cells;
				}
			}
		}) 	
		
		.factory("buttonBuilder", function () {
			return {
				build: function (config) {
					var button = {};
					button.accessControlGuid=config.accessControlGuid;
					button.caption=config.caption;
					//button.classes=config.classes || "btn btn-default btn-sm";
					button.style=config.style || "text-align: left; cursor: pointer;";
					button.glyphicon="glyphicon " + config.glyphicon;
					button.disabled=false;
					return button;
				}
			}
		})


		.factory("breadcrumbBuilder", function(stateService) {
			return {
				build: function(breadcrumbTemplate) {
					var breadcrumb=[];
					angular.forEach(breadcrumbTemplate, function(value, key) {

						if (value && angular.isString(value) && value.startsWith("@@budgetVer")) {
							var n = value.indexOf(".");
							breadcrumb.push(stateService.getBudgetVer()[value.substr(n+1)]);
						}
						else
							breadcrumb.push(value);
					});
					
					return breadcrumb;
				}
			}
		})

		.factory("urlParamsBuilder", function(stateService) {
			return {
				build: function(paramsTemplate, item) {
					var params={};
					angular.forEach(paramsTemplate, function(value, key) {
						if (value && angular.isString(value) && value.startsWith("@@@"))
							params[key] = stateService.getUser()[value.substr(3)];
						else if (value && angular.isString(value) && value.startsWith("@@"))
							params[key] = stateService.getRouteParams()[value.substr(2)];
						else if (value && angular.isString(value) && value.startsWith("@")) {

							if (item[value.substr(1)])
								params[key] = item[value.substr(1)];
							else
								params[key]=-1;
						}
						else
							params[key]=value;
					});

					return params;
				}
			}
		})

		.factory("periodFieldBuilder", function(fieldBuilder) {
			return {
				build: function(config) {

					return fieldBuilder.build({
						name: "period",
						controls: [
							{
								label: {
									labelClass: "col-sm-2",
									desc: "Дата начала"
								},
								name: "beginDate",
								dataType: "date",
								controlClass: "col-sm-4",
								required: config ? config.required : true,
								editable: config ? config.editable : true
							},

							{
								label: {
									labelClass: "col-sm-2",
									desc: "Дата окончания"
								},
								name: "endDate",
								dataType: "date",
								controlClass: "col-sm-4",
								required: config ? config.required : true,
								editable: config ? config.editable : true
							}
						]
					})
				}
			}
		})

		.factory("ebkFieldBuilder", function(fieldBuilder) {
			return {
				build: function(conf) {

					var control1 = {
						name: conf.controlNames[0],
						dataType: "objectId",
						controlClass: "col-sm-4",
						control: conf.control,
						editable: false,
						post: false,
					};

					control1.autofill = {
						dataService: conf.ebkType || "ekr",
						methodName: "getNextCodes",
						fieldValue: conf.controlNames[0],
						fieldDisplayName: conf.controlNames[0],
						params: {
							budgetVerId: "@@budgetVerId",
							parentId: conf.parentId
						}
					}

					
					var control2 = {
						name: conf.controlNames[1],
						dataType: "string",
						controlClass: "col-sm-6",
						control: conf.control || "input",
						editable: false,
						post: false
					}; 

					control2.autofill= {
						dataService: conf.dictName,
							methodName: "findElement",
							fieldValue: conf.fieldDisplayName || "name",
							params : {
								id: "@" + conf.controlNames[2],
								budgetVerId: "@@budgetVerId",
							}
					};

					
					var control3={
						name: conf.controlNames[2],
						dataType: "integer",
						visible: false,
						post: true
					};
					
					return fieldBuilder.build({
						name: conf.name,
						labelDesc: conf.labelDesc,
						controls: [
							control1,
							control2,
							control3
						]
					})
				}
			}
		})

		.factory("tableFieldBuilder", function () {
			return {
				build: function (config) {
					var field = {};
					field.name=config.name;
					field.desc=config.desc;
					field.dataType=config.dataType || "string";
					field.headerClasses=config.headerClasses || "col-md-1";
					return field;					
				}
			}
		})
			
		.factory("responsiveTableFieldBuilder", function () {
			return {
				build: function (config) {
					var field = {};

					field.name=config.name; 
					field.desc=config.desc;
					field.headerClass=config.headerClass || "text-center"; 
					field.headerStyle=config.headerStyle;
					field.cellClass=config.cellClass || "text-left";
					
					if (!angular.isUndefined(config.header))
						field.header=config.header; 
					else
						field.header=true;

					if (!angular.isUndefined(config.cell))
						field.cell=config.cell; 
					else
						field.cell=true;
					
					if (!angular.isUndefined(config.editable))
						field.editable=config.editable; 
					else
						field.editable=false;
					
					if (!angular.isUndefined(config.visible))
						field.visible=config.visible; 
					else
						field.visible=true;
					
					if (!angular.isUndefined(config.expandButton))
						field.expandButton=config.expandButton; 
					else
						field.expandButton=false;
					
					if (!angular.isUndefined(config.collapseButton))
						field.collapseButton=config.collapseButton; 
					else
						field.collapseButton=false;
					
					field.expandBlock=config.expandBlock;
					field.collapseBlock=config.collapseBlock;
					
					field.row=config.row || 1; 
					field.dataType=config.dataType || "string"; 
					field.rowspan=config.rowspan || 1; 
					field.colspan=config.colspan || 1;
					return field;
				}
			}
		})
		
		
		.factory("controlBuilder", function() {
			return {
				build: function(config) {

					conf = config || {name: "field"};

					var control = {};
					control.name = conf.name;
					control.controlClass = conf.controlClass || "col-sm-4";

					if (conf.controlDisplayValue)
						control.displayValue = conf.controlDisplayValue;

					if (conf.controlLabelDesc) {
						control.label = {};
						control.label.desc = conf.controlLabelDesc;
						control.label.labelClass = conf.controlLabelClass;
					}

					if (conf.dictName) {
						control.dataType = conf.controlDataType || conf.dataType || "objectId";
						control.control = conf.control || "select";
						control.dict = conf.dictName;
						control.dictDisplayName = conf.dictDisplayName || "name";
						control.dictParams = conf.dictParams || {};
					}
					else {
						control.dataType = conf.controlDataType || "string";
						var controlDef = "input";
						if (control.dataType == "checkbox")
							controlDef = "checkbox";

						control.control = conf.control || controlDef;
					}

					control.required = angular.isUndefined(conf.required) ? true : conf.required;
					control.editable = angular.isUndefined(conf.editable) ? true : conf.editable;
					control.post = angular.isUndefined(conf.post) ? true : conf.post;
					control.visible = angular.isUndefined(conf.visible) ? true : conf.visible;

					control.autofill=angular.copy(conf.autofill);
					return control;
				}
			}
		})

		.factory("fieldBuilder", function() {
			return {
				build: function(config) {

					conf = config || {name: "field"};

					var field = {};
					field.name = conf.name;

					field.visible =  angular.isUndefined(conf.visible)  ? true:  conf.visible;
					field.required = angular.isUndefined(conf.required) ? false: conf.required;
					field.editable = angular.isUndefined(conf.editable) ? true:  conf.editable;
					field.post     = angular.isUndefined(conf.post)     ? true:  conf.post;
					field.visible  = angular.isUndefined(conf.visible)  ? true:  conf.visible;
					field.containerClass = conf.containerClass || "form-group row";
					field.panel = conf.panel || "base";


					if (conf.label) {
						field.label=conf.label;
						field.label.labelClass = field.label.labelClass || "col-sm-2";
					}
					else {
						if (conf.labelDesc) {
							field.label = {};
							field.label.desc = conf.labelDesc;
							field.label.labelClass = conf.labelClass || "col-sm-2";
						}
					}

					if (conf.controls) {
						field.controls=conf.controls;

						angular.forEach(field.controls, function(control) {
							control.name=control.name || conf.name;
							control.controlClass=control.controlClass || conf.controlClass || "col-sm-4";
							control.displayValue = control.displayValue || conf.controlDisplayValue;

							if (control.dict) {
								control.dataType=control.dataType || conf.controlDataType || conf.dataType  || "objectId";
								control.control = control.control || conf.control || "select";
								control.dictDisplayName = control.dictDisplayName || conf.dictDisplayName || "name";
								control.dictValueName = control.dictValueName || conf.dictValueName || "id";
								control.dictParams=control.dictParams || conf.dictParams || {};
							}
							else {
								control.dataType=control.dataType || conf.controlDataType || conf.dataType || "string";
								control.control = control.control || conf.control || "input";
							}

							control.required = angular.isUndefined(control.required) ? field.required: control.required;
							control.editable = angular.isUndefined(control.editable) ? field.editable: control.editable;
							control.post     = angular.isUndefined(control.post)     ? field.post:     control.post;
							control.visible  = angular.isUndefined(control.visible)  ? field.visible:  control.visible;
						});
					}
					else {
						field.controls = [];

						var control = {};
						control.name = conf.controlName || conf.name;
						control.controlClass = conf.controlClass || "col-sm-4";

						if (conf.controlDisplayValue)
							control.displayValue = conf.controlDisplayValue;
						
						if (conf.dependencyField)
							control.dependencyField = conf.dependencyField;

						if (conf.controlLabelDesc) {
							control.label = {};
							control.label.desc = conf.controlLabelDesc;
							control.label.labelClass = conf.controlLabelClass;
						}

						if (conf.dictName) {
							control.dataType = conf.controlDataType || conf.dataType || "objectId";
							control.control = conf.control || "select";
							control.dict = conf.dictName;
							control.dictDisplayName = conf.dictDisplayName || "name";
							control.dictValueName = conf.dictValueName || "id";
							control.dictParams = conf.dictParams || {};
							control.dictValues=conf.dictValues;
						}
						else {
							control.dataType = conf.controlDataType || "string";
							var controlDef = "input";
							if (control.dataType == "checkbox")
								controlDef = "checkbox";

							control.control = conf.control || controlDef;
						}

						control.required = angular.isUndefined(conf.required) ? field.required : conf.required;
						control.editable = angular.isUndefined(conf.editable) ? field.editable : conf.editable;
						control.post = angular.isUndefined(conf.post) ? field.post : conf.post;
						control.visible = angular.isUndefined(conf.visible) ? field.visible : conf.visible;

						field.controls.push(control);
					}

					return field;
				}
			}
		});
})()