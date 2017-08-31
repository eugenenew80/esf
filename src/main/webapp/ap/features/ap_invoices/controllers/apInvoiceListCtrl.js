angular.module("apApp")
	.controller("apInvoiceListCtrl", function ($scope, $routeParams, $location, $filter, fieldBuilder, urlParamsBuilder, breadcrumbBuilder, stateService, handleActionService, descriptionService, dataService) {
			
        //Save route params
        angular.forEach($routeParams, function(value, key) {
            stateService.getRouteParams()[key]=value;
        }); 		
		
		
        //Add behaviours to scope
        
        //Selected row index
        var selectedRowIndex;
        $scope.setSelectedRow=function(newRowIndex) { selectedRowIndex=newRowIndex;};
        $scope.getSelectedRow=function() { return selectedRowIndex; };

        //Apply search button
        $scope.applySearch = function() {    
        	$scope.data.elements=dataService.findAll($scope.data.state.searchModel);
        	$scope.data.state.isApplySearch = true;
        }
		
		
        //Export selected invoices to xml file
		$scope.importFromXML = function() {

			var file = document.getElementById("file").files[0];
			if (file) {
			    var reader = new FileReader();
			    reader.readAsText(file, "UTF-8");
			    reader.onload = function (evt) {
			        
			    	var param = { 
			    		data: window.btoa(unescape(encodeURIComponent( evt.target.result ))) 
			    	};
			    	
					dataService.importFromXML(param).$promise.then(
						function(data) {
							 $scope.applySearch();
						},
						
						function(error) {$scope.data.error = error;} 
					);			    	
			    	
			    }
			    
			    reader.onerror = function (evt) { $scope.data.error = "Ошибка чтения файла";}
			}
		}    	
        

		
        //Clear search form
		$scope.resetSearch = function() {
			angular.forEach(descriptionService.sections.main.search.fields, function(value, key) {
				$scope.data.state.searchModel[value.name] = undefined;
			})
		}

		
		
        //Add data to scope
        
        //description service
        $scope.descriptionService=descriptionService;
		
        //scope data
        $scope.data={};
        $scope.data.path=breadcrumbBuilder.build(descriptionService.sections.header.path.items);
        $scope.data.error = null;
        
        if (stateService.getRouteParams()["status"] == "new")
        	$scope.data.path.push("Новые");
        else if (stateService.getRouteParams()["status"] == "checked")
        	$scope.data.path.push("Проверенные");
        else
        	$scope.data.path.push("Все");
        
        
		//page's state
		$scope.data.state = stateService.getState(descriptionService.name);
		$scope.data.state.rowsPerPage   = descriptionService.sections.main.table.rowsPerPage || $scope.data.state.rowsPerPage;      
        $scope.data.state.isApplySearch = $scope.data.state.isApplySearch || true;
        $scope.data.state.searchModel 	= $scope.data.state.searchModel || stateService.getRouteParams() || {};

        
		//model data
		$scope.data.elements=[];
		if ($scope.data.state.isApplySearch)
			$scope.applySearch();

		
		
		
        //Action processing
        $scope.handleAction = function(source, action, item) {

            //go to view
            if (action.typeAction=="redirect" && action.redirect && action.redirect.url) {
                
            	var url = action.redirect.url;
                if (action.redirect.params) {
                    angular.forEach(action.redirect.params, function (value, key) {
                        
                    	if (value.startsWith("@@"))
                        	url = url.replace(":" + key, stateService.getRouteParams()[value.substr(2)]);
                        
                    	else if (value.startsWith("@"))
                        	url = url.replace(":" + key, item[value.substr(1)]);
                    });
                }
                
                $location.path(url);
            }

            
	        //Type action - form
	        if (action.typeAction == "form" && action.form) {
	            var form = descriptionService.forms[action.form.name];
	            handleActionService.openForm(action, form, item, dataService, descriptionService);
	        }


            //call controller method
            if (action.typeAction=="controllerMethod" && action.controllerMethod)
                $scope[action.controllerMethod.name]();
        }

		
        
        $scope.handle= function(action) {
            $scope.handleAction(null, action);
        }
	
	});	
