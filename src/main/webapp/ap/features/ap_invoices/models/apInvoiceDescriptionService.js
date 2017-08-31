(function () {
    angular.module("apApp")
        .factory("apInvoiceDescriptionService", function ($filter, stateService, buttonBuilder, fieldBuilder, tableFieldBuilder, responsiveTableFieldBuilder) {

			//List fields description for search
			var searchFieldsDef = [
				
				fieldBuilder.build({
					name: "startDate",
					labelDesc: "Дата с",
                    labelClass: "col-sm-2",
                    controlClass: "col-sm-2",
                    controlDataType: "date"
				}),
				
				fieldBuilder.build({
					name: "endDate",
					labelDesc: "Дата по",
                    labelClass: "col-sm-2",
                    controlClass: "col-sm-2",
                    controlDataType: "date"
				}),		
				
				
				fieldBuilder.build({
					name: "consigneeId",
					labelDesc: "Получатель счёта",
                    labelClass: "col-sm-2",
                    controlClass: "col-sm-4",					
					dictName: "apConsignee"
				}),	
				
				
				fieldBuilder.build({
					name: "contractNum",
					labelDesc: "№ договора",
                    labelClass: "col-sm-2",
                    controlClass: "col-sm-2"
				}),	
				
				fieldBuilder.build({
					name: "contractDate",
					labelDesc: "Дата договора",
                    labelClass: "col-sm-2",
                    controlClass: "col-sm-2",
                    controlDataType: "date"
				}),					
			];
        	
        	
			//List actions for search
			var searchActionsDef = [
				{
					filter: {
						roles: ["expert", "user"]
					},

                    action: "applySearch",
                    typeAction: "controllerMethod",

                    controllerMethod: {
                        name: "applySearch"
                    },
                    
                    trigger: "button",
					button: {
						desc: "Применить",
						tooltip: "Применить",
						classes: "btn btn-primary btn-xs pull-left",
						style: "margin-left: 3px;",
						glyphicon: "glyphicon glyphicon-search",
						disabled: false
					}
				},

				{
					filter: {
						roles: ["expert", "user"]
					},

                    action: "resetSearch",
                    typeAction: "controllerMethod",

                    controllerMethod: {
                        name: "resetSearch"
                    },					
                    
                    trigger: "button",
					button: {
						desc: "Сбросить",
						tooltip: "Сбросить",
						classes: "btn btn-warning btn-xs pull-left",
						style: "margin-left: 3px;",
						glyphicon: "glyphicon glyphicon-off",
						disabled: false
					}
				}			                        
			];    
			
			
            //List fields description for table
			var tableFieldsDef = [

	            responsiveTableFieldBuilder.build({
		            name: "isSelected",
		            desc: "Экспорт",
		            headerStyle: "min-width: 50px;",
		            dataType: "Yes",
		            cellClass: "text-center"
	            }),	

  	            responsiveTableFieldBuilder.build({
		            name: "status",
		            desc: "Статус",
		            headerStyle: "min-width: 50px;",
	            }),
	            
	            
  	            responsiveTableFieldBuilder.build({
		            name: "num",
		            desc: "№ СФ",
		            headerStyle: "min-width: 50px;",
	            }),

  	            responsiveTableFieldBuilder.build({
		            name: "invoiceDate",
		            desc: "Дата СФ",
		            headerStyle: "min-width: 50px;",
		            dataType: "date"
	            }),
	            
  	            responsiveTableFieldBuilder.build({
		            name: "turnoverDate",
		            desc: "Дата сов. оборота",
		            headerStyle: "min-width: 50px;",
		            dataType: "date"
	            }),	            
	          
  	            responsiveTableFieldBuilder.build({
		            name: "consigneeName",
		            desc: "Получатель счёта",
		            headerStyle: "min-width: 70px;",
	            }),	       
 	
	            
  	            responsiveTableFieldBuilder.build({
		            name: "contractNum",
		            desc: "№ договора",
		            headerStyle: "min-width: 70px;",
	            }),	  	  
	            
  	            responsiveTableFieldBuilder.build({
		            name: "contractDate",
		            desc: "Дата договора",
		            headerStyle: "min-width: 50px;",
		            dataType: "date"
	            }),	  	
	            
  	            responsiveTableFieldBuilder.build({
		            name: "destination",
		            desc: "Адрес доставки",
		            headerStyle: "min-width: 120px;",
		            dataType: "date"
	            }),		  
	            

  	            responsiveTableFieldBuilder.build({
		            name: "totalPriceWithoutTax",
		            desc: "Сумма без НДС",
		            headerStyle: "min-width: 90px;",
		            cellClass: "text-right",
		            dataType: "number"
	            }),	
	            
  	            responsiveTableFieldBuilder.build({
		            name: "totalNdsAmount",
		            desc: "НДС",
		            headerStyle: "min-width: 90px;",
		            cellClass: "text-right",
		            dataType: "number"
	            }),	

	            responsiveTableFieldBuilder.build({
		            name: "totalPriceWithTax",
		            desc: "Сумма с НДС",
		            headerStyle: "min-width: 90px;",
		            cellClass: "text-right",
		            dataType: "number"
	            }),		             
			];
			
		
			
            //List actions after search
            var tableActionsDef = [
                
                {
                    action: "importFromXML",
                    typeAction: "controllerMethod",

                    controllerMethod: {
                        name: "importFromXML"
                    },

                    trigger: "button",
                    button: {
                        desc: "Импорт из файла",
                        tooltip: "Имортировать новые СФ из файла XML",
                        classes: "btn btn-primary btn-xs",
                        style: "margin-left: 3px;",
                        glyphicon: "glyphicon",
                        disabled: false
                    }
                }
            ];
			
            
            //List actions for row
            var rowActionsDef = [     
				
                {
                    action: "view",
                    typeAction: "form",

                    form: {
                        name: "edit",
                        data: "@currentElement",

                        ok: {
                            action: "@close",
                            data: "@element"
                        },

                        cancel: {
                            action: "@close"
                        }
                    },	

                    trigger: "button",
					button: buttonBuilder.build({
						caption: "Просмотр",
						tooltip: "Просмореть детальные сведения о документе",
						glyphicon: "glyphicon-pencil"
					})
                }	                
            ];


            //return description service
            return {
                name: "apInvoice",
                desc: "Входящие Счёта-фактуры",
                
                sections: {
                	
                	//header section
                	header: {
                		path: {
                			type: "breadcrumb",
                			items: ["Исходящие СФ"],			
                		}
                	},

                	
					//Main section
                	main: {
                		
                		//Search form
                		search: {
                			type: "form",
                			templateURL: "common/directives/form/formTemplate.html",
                			header: "Панель фильтров",
                            fields:  searchFieldsDef,
                            actions: searchActionsDef,                			
                            
                            enable: false,
                            auto: true,
                            collapsable: true,
                            isCollapse: true,
                            criteria: {},
                            entity: {}
                		},
                		
                		
		                table: {
		                	//params
		                	type: "table",
		                	data: "elements",
		                    tableType: "responsive",
		                    templateURL: "common/directives/complexView/complexViewTable/complexViewTableTemplate.html",
		                    tableClass: "table table-hover table-condensed table-bordered",
		                    rowsPerPage: 10,
		                    liveSearch: false,
		                    
		                    //fields
		                    fields: tableFieldsDef,

		                    //table actions
	                		tableActions: {
	                			type: "actions",		                    
								items: tableActionsDef
	                		},
                			
	                		//row actions
                            rowActions: {
                                type: "actions",
                                items: rowActionsDef                             
                            }
	                		
		                }
		                		               
                	}                	
                },

                //Modal forms
                forms: {
                    //Form edit user
                    edit: {
                        type: "modalForm",
                        templateURL: "common/views/edit.html",
                        controller: "apInvoiceEditCtrl",
                        header: "Исходящий Счет-фактура",
                        
                        panels: [
                        	{
                        		name:   "base",
                        		title:  "Общие данные"  	
                        	}                  	
                        ],
                        
                        fields: [

            				fieldBuilder.build({
            					name: "num",
            					labelDesc: "№ СФ",
                                labelClass: "col-sm-4",
                                controlClass: "col-sm-4",
                                panel: "base",
                                editable: false
            				}),
            				
            				fieldBuilder.build({
            					name: "invoiceDate",
            					labelDesc: "Дата СФ",
                                labelClass: "col-sm-4",
                                controlClass: "col-sm-4",
                                panel: "base",
                                editable: false,
                                controlDataType: "date"
            				}),
            				
            				fieldBuilder.build({
            					name: "turnoverDate",
            					labelDesc: "Дата сов. оборота",
                                labelClass: "col-sm-4",
                                controlClass: "col-sm-4",
                                panel: "base",
                                editable: false,
                                controlDataType: "date"
            				}),
            				
            				fieldBuilder.build({
            					name: "consigneeName",
            					labelDesc: "Получатель счёта",
                                labelClass: "col-sm-4",
                                controlClass: "col-sm-8",
                                panel: "base",
                                editable: false
            				}),
            				
            				fieldBuilder.build({
            					name: "contractNum",
            					labelDesc: "№ договора",
                                labelClass: "col-sm-4",
                                controlClass: "col-sm-4",
                                panel: "base",
                                editable: false
            				}),
            				
            				fieldBuilder.build({
            					name: "contractDate",
            					labelDesc: "Дата договора",
                                labelClass: "col-sm-4",
                                controlClass: "col-sm-4",
                                panel: "base",
                                editable: false,
                                controlDataType: "date"
            				}),            				
                        ],
                        
                        
                        actions: [
    							{
    			                    action: "cmdSave",
    			                    typeAction: "controllerMethod",

    			                    controllerMethod: {
    			                        name: "save"
    			                    },	
    			                    
    								trigger: "button",
    								button: {
    									desc: "Сохранить",
    									tooltip: "Сохранить изменения",
    									classes: "btn btn-primary btn-sm",
    									disabled: false
    								}
    							},
    							
    							{
    			                    action: "cmdCancel",
    			                    typeAction: "controllerMethod",

    			                    controllerMethod: {
    			                        name: "cancel"
    			                    },	
    			                    
    								trigger: "button",
    								button: {
    									desc: "Закрыть",
    									tooltip: "Закрыть окно",
    									classes: "btn btn-warning btn-sm",
    									disabled: false
    								}
    							}								                          
                        ]                          
                    }
                                       
                    
                }
            }
        });
})();