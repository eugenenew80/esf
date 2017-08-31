(function () {

    angular.module("common")
        .factory("handleActionService", function ($uibModal, $filter, $http, stateService, urlParamsBuilder) {    		
        	
        	var showAlert = function(text) {	
    		    var modalInstance = $uibModal.open({
    			      animation: true,
    			      templateUrl: "common/dialogs/alert/alert.html",
    			      controller:  "alertCtrl",
    	  
    			      resolve: {
    						text: function() { 
    							return text; 
    						}
    			        }		      
    			    });	
    		};
    		
        	
    		var runCustomRequest = function(action, request, item, descriptionService) {    			
    			var requestResult= 
					    			$http({
										method: request.method, 
										url: request.url,
										
										headers: {
											'Accept': 'application/json;charset=utf-8'
										},
									    
										data: item
									});   
    			
    			requestResult.then(
					function(newItem) {
						if (request.message) {
							var message = $filter('filter')(descriptionService.messages, {code: request.message}, true)[0];
							showAlert(message.message);
						}								
					}, 
				
					function(error) {}
				);	    			
    			
    			return requestResult; 
    		};
    		
            
    		var runResourceAction = function(button, item, dataService, descriptionService)  {
    			
    			//Data service action
				var dataServiceAction;
				if (button.action)
					dataServiceAction = button.action.substr(1);
				else
					dataServiceAction = button.method.substr(1);

				//parameters
				var params = null;
				if (button.params) 
					params = urlParamsBuilder.build(button.params, item);

				
				//run action
				var runResult= dataService[dataServiceAction](item, params);				
				runResult.then(
							function(newItem) {	
								if(dataServiceAction === "verification" || dataServiceAction === "createVersion") {
									console.log("export data to xls");
									for(i = 0; i < newItem.$promise.$$state.value.length; i++) {
										console.log("export value = " + newItem.$promise.$$state.value[i].error_msg);
									}
								}
								if (button.message) {
									var message = $filter('filter')(descriptionService.messages, {code: button.message}, true)[0];
									showAlert(message.message);
								}								
							}, 
							
							function(error) {}
						);					
				
				//return promise object
				return runResult;
    		};
    		
    		
    		var openForm = function(action, form, item, dataService, descriptionService, handle) {
            	    			
    			//opening modal form
    			var modalInstance = $uibModal.open({
  			      animation: true,
  			      backdrop  : 'static',
  			      templateUrl: form.templateURL,
  			      controller:  form.controller,
  	  
  			      resolve: {
  						dataService: function() { return dataService; },
  						descriptionService: function() { return descriptionService; },
  						form: function() { return form; },
  						action: function() { return action; },
  						handle: function() { return handle; }, 
  						
  						currentElement: function () { 
  							var resolvedItem={};
  							
  							//new empty element
  							if (action.form.data=="@newElement") 
  								resolvedItem={};
  							
  							//current element
  							if (action.form.data=="@currentElement") 
								resolvedItem = item;

							//clone of current element
  							if (action.form.data=="@cloneElement")
								resolvedItem = angular.copy(item);

  							//init data
							if (action.form.initData) {
								angular.forEach(action.form.initData, function(value, key) {  									
									if (value && angular.isString(value) && value.startsWith("@@@")) 
										resolvedItem[key]=stateService.getUser()[value.substr(3)];
									else if (value && angular.isString(value) && value.startsWith("@@")) 
										resolvedItem[key] = descriptionService.search.criteria[value.substr(2)];
									else if (value && angular.isString(value) && value.startsWith("@")) 
										resolvedItem[key] = item[value.substr(1)];
									else
										resolvedItem[key]=value;
  								});    							
  							}
  							
  							if (!resolvedItem)
  								resolvedItem=item;

  							if (action.form.data=="@currentElement" && resolvedItem.$get)
  								return resolvedItem.$get();
  							else	
  								return resolvedItem;
  						}
  			      }		      
  			    });

  			    
    		    //waiting result
    		    modalInstance.result.then(
  		    		function (selectedItem) {}, 
  		    		function () {}
	    		);		
  			    
			    //return promise object
			    return modalInstance.result;
            };        	
            	

    		var openConfirm = function(action, confirm, item, dataService, descriptionService) {
    		    
    			//opening modal form
    			var modalInstance = $uibModal.open({
    			      animation: true,
    			      templateUrl: confirm.templateURL,
    			      controller:  confirm.controller,
    	  
    			      resolve: {
    			    	  text: function() { 
    							return confirm.text; 
    						}
    			        }		      
    			});  

    			
    			//waiting result
			    modalInstance.result.then(
    		    		function (result) {
    		    			runResourceAction(action.confirm[result], item, dataService, descriptionService); 
    		    		}, 
    		    		
    		    		function () {}
			    	);
    			    
    			
			    //return promise object
			    return modalInstance.result;
    		};	            

    		
    		var runAction=function(action, item, dataService, descriptionService, form, button) {
    			//Form
    			if (action.form) {
    				var button = action.form[button || "ok"];
    				
    				//Data service action
    				if (button.action.startsWith("@")) {
    					var resultAction = runResourceAction(button, item, dataService, descriptionService);
    					return resultAction;
    				}
    				
    				//Custom request action
    				if (button.action=="request") {
    					var resultAction = runCustomRequest(action, button.request, item, descriptionService);
    					return resultAction;
    				}
    			}    			
    		};

                            		
    		//return handle action service
    		return {
        		openForm:          	openForm,	
        		openConfirm:		openConfirm,
        		showAlert:			showAlert,
        		runCustomRequest:  	runCustomRequest,
        		runResourceAction: 	runResourceAction,
        		runAction:			runAction
            }
        });
})();

