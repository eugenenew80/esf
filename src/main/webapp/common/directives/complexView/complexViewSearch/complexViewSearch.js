angular.module("common")
	.directive("complexViewSearch", function () {
		return {
			restrict: "EA",
			replace: true,
			template: "<ng-include src='complexView.config.search.templateURL'></ng-include>"
		}
	});