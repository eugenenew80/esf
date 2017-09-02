(function() {

	angular.module("common")
		.factory("stateService", function($filter) {

			var budgetVer = {};
			var states = [];
			var routeParams={};
			var currentResp = {};
			
			var user = {
				name: 'user'
			};

			return {
				states: states,

				getState: function(entityName) {

					var stateDef = {
						name:			entityName,
						isSearchOpened: false,
						isApplySearch:	false,
						rowsPerPage: 	5,
						selectedPage:	1,
						search:			{}
					}

					var state = $filter('filter')(states, {name: entityName}, true)[0];
					if (!state)
						states.push(stateDef);

					return $filter('filter')(states, {name: entityName}, true)[0];
				},

				getUser: function() {
					return user;
				},
				
				setUser: function(newUser) {
					user.name=newUser.userName;
					user.role=newUser.roleName;
					user.roleNameRu=newUser.nameRu;
				},

				getRouteParams: function() {
					return routeParams;
				},
				
				setRouteParams: function(newRouteParams) {
					routeParams = newRouteParams;
				},
				
				
				getCurrentResp: function() {
					return currentResp;
				},
				
				setCurrentResp: function (newCurrentResp) {
					currentResp = newCurrentResp;
				}
			}
		})

		.factory("appSettings", function() {
			return {
				//baseUrl: "http://10.9.41.187:8080/"
				//baseUrl: "http://10.9.40.29:8080/"	
				baseUrl: "http://localhost:49365/"
			}
		});
})()
