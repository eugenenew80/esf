angular.module("common")
	.controller("confirmYesNoCtrl", function ($scope, $uibModalInstance, text) {
		
		$scope.text=text;
		
		//Yes button
		$scope.yes = function() {		
			$uibModalInstance.close("yes");
		}


		//No button
		$scope.no = function() {		
			$uibModalInstance.dismiss("no");
		}

	});