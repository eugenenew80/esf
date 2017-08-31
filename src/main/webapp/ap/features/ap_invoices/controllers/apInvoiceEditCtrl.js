angular.module("apApp")
	.controller("apInvoiceEditCtrl", function ($scope, $http, $uibModal, $uibModalInstance, urlParamsBuilder, handleActionService, dataService, descriptionService, form, action, currentElement) {		

		$scope.data = {};
        $scope.descriptionService = descriptionService;
        $scope.form = form;        
        $scope.actions = {};
        $scope.currentElement = angular.copy(currentElement);
        
        $scope.currentElement.invoiceDate = new Date($scope.currentElement.invoiceDate);	
        $scope.currentElement.turnoverDate = new Date($scope.currentElement.turnoverDate);
        $scope.currentElement.contractDate = new Date($scope.currentElement.contractDate);
        
        
        //Save the current element and switch to table mode
        $scope.actions.save = function () {
			dataService.update($scope.currentElement).$promise.then(
				function(newItem) {
					if (currentElement.$get) currentElement.$get();					
					$uibModalInstance.close("ok");
				},

				function(error) {
					$scope.data.error=error;
				}
			);
        }        
        
        //Cancel changes and switch to table mode
        $scope.actions.cancel = function () {
            $scope.data.file = null;
            $uibModalInstance.dismiss("cancel");
        }
	});	
