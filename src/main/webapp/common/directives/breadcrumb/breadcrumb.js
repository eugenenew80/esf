angular.module("common")
	.directive("breadcrumb", function () {
		return {
			restrict: "EA",
			replace: true,
			
			scope: {
				paths: "=paths"
			},
			
			templateUrl: "common/directives/breadcrumb/breadcrumbTemplate.html"
		}
	});