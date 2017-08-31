angular.module("common")
	.directive("complexView", function () {
		return {			
			
			controller: function($scope) {
				
				$scope.complexView={
					config: $scope.descriptionService
				};
				
			},
			
			restrict: "EA",
			replace: true,
			templateUrl: "common/directives/complexView/compexViewTemplate.html"
		}
	});
