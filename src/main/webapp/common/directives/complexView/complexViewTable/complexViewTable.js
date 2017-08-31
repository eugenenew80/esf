angular.module("common")
	.directive("complexViewTable", function () {
		return {

			controller: function($scope, $parse) {
				/*
				var selectedRowIndex=1;

				$scope.setSelectedRow=function(rowIndex) {
					selectedRowIndex=rowIndex;
				};

				$scope.getSelectedRow=function() {
					return selectedRowIndex;
				};
				*/
			},

			restrict: "EA",
			replace: true,
			template: "<ng-include src='complexView.config.table.templateURL'></ng-include>"
		}
	});
