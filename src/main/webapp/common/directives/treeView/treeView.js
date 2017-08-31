angular.module("common")
.directive("treeView", function () {
	
	return {
		link: function (scope, element, attrs) {

			var addChildNodes = function(elem, node) {
				for (var i = 0; i < node.nodes.length; i++) {
					
					var liElem = angular.element('<li>');
					liElem.addClass("list-group-item");
					elem.append(liElem);
					
					var aElem = angular.element('<a>').text(node.nodes[i].desc);
					aElem.css({"padding-top": "2px", "padding-bottom": "2px"});
					liElem.append(aElem);
					
					if (node.nodes[i].hasNodes) {
						aElem.attr("id", node.nodes[i].name);
						aElem.attr("data-toggle", "collapse");					
						aElem.attr("data-target", "#" + node.nodes[i].name + "_submenu");
						aElem.attr("aria-expanded", "false");
						aElem.attr("role", "button");
						aElem.addClass("text-muted");
						
						var spanElem = angular.element("<span>");
						spanElem.addClass("caret");
						aElem.append(spanElem);					
	
						var ulElem = angular.element("<ul>");
						ulElem.attr("id", node.nodes[i].name + "_submenu");
						ulElem.addClass("nav collapse");		
						if (!node.nodes[i].isCollapsed)
							ulElem.addClass("in");
						
						ulElem.attr("role", "menu");
						ulElem.attr("aria-labelledby", node.nodes[i].name);
						liElem.append(ulElem);
						
						addChildNodes(ulElem, node.nodes[i]);
					}
					else {				
						var params="";
						if (node.nodes[i].params) {
							angular.forEach(node.nodes[i].params, function(param) {
								params=params + "/" + param.value;
							});
						}
						
						aElem.attr("href", "#/" + node.nodes[i].name + params + "/list/");
						aElem.addClass("text-primary");
					}				
				}
			}
			
			scope.$watch("menu", function() {
				var data = scope[attrs["treeData"]];		
				
				if (!data || !data.nodes || data.nodes.length==0)
					return;
				
				var navElem = angular.element("<nav>");
				navElem.addClass("navbar navbar-primary");
				navElem.attr("role", "navigation");
				element.append(navElem);		
				
				var ulElem = angular.element("<ul>");
				ulElem.addClass("nav list-group");
				navElem.append(ulElem);
				
				addChildNodes(ulElem, data);
			})
		},
		
		replace: true,
		restrict: "EA"
	}
})