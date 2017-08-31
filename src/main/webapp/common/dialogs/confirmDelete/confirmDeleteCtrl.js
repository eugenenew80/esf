angular.module("common")
	.controller("confirmDeleteCtrl", function ($scope, $uibModalInstance) {
		
		//Yes button
		$scope.yes = function() {		
			$uibModalInstance.close("yes");
		}


		//No button
		$scope.no = function() {		
			$uibModalInstance.dismiss("no");
		}
	});

