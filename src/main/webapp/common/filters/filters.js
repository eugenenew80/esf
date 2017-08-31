(function () {
	angular.module("common", [])
	
		.filter("cellFilter", function ($filter) {
			
			return function (value, field) {
				
				if (angular.isNumber(value) && field && field.dataType=="date") 
					return $filter("date")(new Date (value), "dd.MM.yyyy");
				
				if (angular.isString(value) && field && field.dataType=="date") 
					return $filter("date")(value, "dd.MM.yyyy");

				if (angular.isDate(value))
					return $filter("date")(value, "dd.MM.yyyy");

				if (angular.isNumber(value) && field && field.dataType=="timestamp")
					return $filter("date")(value, "dd.MM.yyyy HH:mm:ss");

				
				if (angular.isObject(value) && field && field.dictDisplayName)
					return value[field.dictDisplayName];
				
				return value;
			};
		})
			
		.filter("rangeFilter", function ($filter) {
			return function (data, page, size) {
				
				if (angular.isArray(data)) {
					var startIndex = (page - 1) * size;
					return $filter("limitTo")(data.slice(startIndex), size);
				}
				
				return data;
			}
		})
		
		.filter("paginationFilter", function () {
			
			var currentStartPage=1;
			var currentEndPage=10;
			
			return function (data, selectedPage, rowsPerPage) {
				
				var countPages = 5;
				var max = Math.ceil(data.length / rowsPerPage);
				
				if (selectedPage==currentEndPage) 				
					currentEndPage=selectedPage+(countPages-2);				
				
				if (selectedPage==currentStartPage) {
					currentEndPage=selectedPage+1;
					if (currentEndPage<=countPages)
						currentEndPage=countPages;
				}			
				
				
				if (currentEndPage>max) {
					currentEndPage=max;
				}
				
				currentStartPage=currentEndPage-(countPages-1);
				
				if (currentStartPage<1)
					currentStartPage=1;
				
				if (currentEndPage<currentStartPage) {
					currentStartPage=1;
					currentEndPage=max;
				}

				
				pages = [];
				for (var i = currentStartPage; i <= currentEndPage ; i++) {
					pages.push(i);
				}			
	
				return pages;
			}
		})
		
		.filter('sum', function () {
			return function (items, prop) {

				if (items == null) {
			        return 0;
			    }
				
			    return items.reduce(function (a, b) {
			        return b[prop] == null ? a : a + b[prop];
			    }, 0);				
				
			};
		})
		
		.filter('unique', function () {
			return function (items, filterOn) {
				
				if (filterOn === false) {
					return items;
				}
				
				if ((filterOn || angular.isUndefined(filterOn)) && angular.isArray(items)) {
					var hashCheck = {}, newItems = [];
					var extractValueToCompare = function (item) {
						if (angular.isObject(item) && angular.isString(filterOn)) {
							return item[filterOn];
						} else {
							return item;
						}
					};
					
					angular.forEach(items, function (item) {
						var valueToCheck, isDuplicate = false;
						for (var i = 0; i < newItems.length; i++) {
							if (angular.equals(extractValueToCompare(newItems[i]), extractValueToCompare(item))) {
								isDuplicate = true;
								break;
							}
						}
						
						if (!isDuplicate) {
							newItems.push(item);
						}
					});
					items = newItems;
				}
				
				return items;
			};
		})
		
		.filter("actionFilter", function () {			
			return function (actions, actionFilterFunction, item) {
				
				if (!actionFilterFunction) 
					return actions;
					
				var newActions = actions.filter(function(action) {					
					return actionFilterFunction(action, item);
				});
					
				return newActions;
			};
		})
		
		.filter("liveSearchFilter", function ($filter) {			
			return function (items, liveSearch) {
				
				var filtered = items;
				angular.forEach(liveSearch, function(value, key) {
					
					console.log(key);
					console.log(value);
					
					if (angular.isString(value) && value!="")
						filtered = $filter("startsWith")(filtered, key, value);
				});
				
				return filtered;
			};
		})	
		
		.filter('startsWith', function () {
		  return function (items, field, letter) {
		    var letterMatch = new RegExp(letter, 'i');
		    
			return items.filter(function(item) {	
				if (!item[field])
					return false;
				return letterMatch.test(item[field].substring(0, letter.length))
			});		    
		  };
		});		
				
})();	
