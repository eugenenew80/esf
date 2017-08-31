angular.module("common")
	.controller("alertCtrl", function ($scope, $uibModalInstance, text) {
		
		$scope.text=text;
		
		//Yes button
		$scope.ok = function() {		
			$uibModalInstance.close("ok");
		}

	});