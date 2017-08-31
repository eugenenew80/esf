angular.module("arApp")
	.controller("arInvoiceListCtrl", function ($scope, $routeParams, $location, $filter, $uibModal, fieldBuilder, urlParamsBuilder, breadcrumbBuilder, stateService, handleActionService, descriptionService, dataService) {
			
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
        	
        	//show progress bar
            var modalProgress = $uibModal.open({
                templateUrl: "common/views/progress.html",
                windowClass: "x-progress-bar",
                backdrop  : 'static',
                keyboard  : false
            });        	
        	        	
        	$scope.data.elements=dataService.findAll($scope.data.state.searchModel);
        	$scope.data.state.isApplySearch = true;        	  
        	
        	$scope.data.elements.$promise.then(
        		function(data) {
        			modalProgress.close("ok");
        			$scope.data.error = null;
        		}, 
        		
        		function(error) {
        			modalProgress.close("ok");
        			$scope.data.error = error;
        		}
        	);
        	
        	$scope.data.state.selectedPage = 1;
        }
		
		
        //Export selected invoices to xml file
		$scope.exportToXML = function() {
			
			var param = {};
			param.invoices = [];
	        angular.forEach($scope.data.elements, function(value, key) {
	        	if (value.isSelected == "Y")
	        		param.invoices.push(value.id);
	        }); 
	        
			dataService.exportToXML(param).$promise.then(
				function(data) {
					var content = decodeURIComponent(escape(window.atob(data.data)));					
					var now = new Date();
					var strDate = now.getFullYear() + '-' + ('0' + (now.getMonth() + 1)).slice(-2) + '-' + ('0' + now.getDate()).slice(-2)
					download("export_" + strDate + ".xml", content);
					
					$scope.applySearch();
				},
				
				function(error) {$scope.data.error = error;} 
			);			
		}    	
        


		$scope.importFromExternal = function() {
			
            //show progress bar
            var modalProgress = $uibModal.open({
                templateUrl: "common/views/progress.html",
                windowClass: "x-progress-bar",
                backdrop  : 'static',
                keyboard  : false
            });
            
			
			dataService.importFromExternal().$promise.then(
				function(data) {
					modalProgress.close("ok");
					$scope.data.error = null;
					$scope.applySearch();
				},
				
				function(error) {
					modalProgress.close("ok");
					$scope.data.error = error;
				} 
			);			
		}  
		
		
		
        //Select all records
		$scope.selectAll = function() {
	        angular.forEach($scope.data.elements, function(value, key) {
	        	value.isSelected = "Y";
	        }); 
		}
		

		
        //Clear search form
		$scope.resetSearch = function() {
			angular.forEach(descriptionService.sections.main.search.fields, function(value, key) {
				$scope.data.state.searchModel[value.name] = undefined;
			})
			
			$scope.data.state.searchModel.orgId = stateService.getCurrentResp().orgId;
			
	        if ($scope.data.state.showAllDocs=="N")
	        	$scope.data.state.searchModel.userId = stateService.getCurrentResp().userId;
	        else
	        	$scope.data.state.searchModel.userId = null;
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
        else if (stateService.getRouteParams()["status"] == "exported")
        	$scope.data.path.push("Экспортированные");
        else
        	$scope.data.path.push("Все");
        
        
		//page's state
		$scope.data.state = stateService.getState(descriptionService.name);
		$scope.data.state.rowsPerPage   = descriptionService.sections.main.table.rowsPerPage || $scope.data.state.rowsPerPage;      
        $scope.data.state.isApplySearch = $scope.data.state.isApplySearch || true;        
        $scope.data.state.showAllDocs = $scope.data.state.showAllDocs || "N";        
        $scope.data.state.searchModel 	= $scope.data.state.searchModel || stateService.getRouteParams() || {};
        $scope.data.state.searchModel.orgId = stateService.getCurrentResp().orgId;
        
        if ($scope.data.state.showAllDocs=="N")
        	$scope.data.state.searchModel.userId = stateService.getCurrentResp().userId;
        else
        	$scope.data.state.searchModel.userId = null;
        
        
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
	

		$scope.getPageClass = function (page) {
			return $scope.data.state.selectedPage == page ? "btn-info" : "";
		}        
        
        
        function download(filename, text) {
    	  var element = document.createElement('a');
    	  element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(text));
    	  element.setAttribute('download', filename);

    	  element.style.display = 'none';
    	  document.body.appendChild(element);

    	  element.click();

    	  document.body.removeChild(element);
    	}    
        
        

        $scope.$watch("data.state.showAllDocs", function(newValue, oldValue) {
        	if (newValue==oldValue)
        		return;;
        	
	        if ($scope.data.state.showAllDocs=="N")
	        	$scope.data.state.searchModel.userId = stateService.getCurrentResp().userId;
	        else
	        	$scope.data.state.searchModel.userId = null;        	
        	
        	$scope.applySearch();
        });
	});	
