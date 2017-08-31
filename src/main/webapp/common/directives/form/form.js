angular.module("common")
	.directive("complexForm", function () {
		return {
			
			scope: {
				config: "=config",
				model:  "=model",
				handle: "=handle"
			},
			
			restrict: "EA",
			replace: true,
			template: "<ng-include src='config.templateURL'></ng-include>"
		}
	});
