angular.module("app")
    .controller("appController", function ($scope, $uibModal, stateService, $rootScope, $http, admBaseUrl) {
        
    	$scope.data = {};
		$rootScope.isAuthenticated = false;
		
		$scope.logout = function () {
			$rootScope.isAuthenticated=false;
		};
		
        $scope.data.app = {
        		header: "Модуль для интеграции с системой ЭСФ",
        		user:	stateService.getUser(),
        		current: "ar",
        		topMenu:
	        	        [
	        	        	{
								accessControlGuid: "ar",
	        	        		name:  "ar",
	        	        		desc:   "Исходящие СФ",
	        	        		url:   "#",
	        	        		active: true
	        	        	},
	        	        	
	        	        	{
								accessControlGuid: "ap",
	        	        		name:  "ap",
	        	        		desc:   "Входящие СФ",
	        	        		url:   "#",
	        	        		active: true
	        	        	}	        	        	
	        	        ]        			
        };
        
        
        $scope.data.auth = {
          isAuth: false,
          user: ""          
        };
        
        
        $scope.login = function() {
        	
        	if (angular.isUndefined($scope.data.auth.user) || $scope.data.auth.user === null) {
        		$scope.data.error = {
        			data: {
        				errMsg: "Введите имя пользователя"
        			}	
        		}
        		return;
        	}
        	
        	if (angular.isUndefined($scope.data.auth.pass) || $scope.data.auth.pass === null) {
        		$scope.data.error = {
        			data: {
        				errMsg: "Введите пароль"
        			}	
        		}
        		return;
        	}
        	
        	
            //show progress bar
            var modalProgress = $uibModal.open({
                templateUrl: "common/views/progress.html",
                windowClass: "x-progress-bar",
                backdrop  : 'static',
                keyboard  : false
            });
            
        	
        	$http({
    		  method: 'GET',
    		  url: admBaseUrl + "auth",
    		  
    		  headers: {
    			   "userName": window.btoa(unescape(encodeURIComponent($scope.data.auth.user))),
    			   "password": window.btoa(unescape(encodeURIComponent($scope.data.auth.pass))),
    		  },
    		  
    		})
    		.then(
    			function successCallback(response) {
    				modalProgress.close("ok");
    				
    				$scope.data.error = undefined;
    				$scope.data.auth.isAuth = true;
    				$scope.data.auth.pass = undefined;
    				
    				$scope.data.auth.allResps = response.data;
    				if (response.data.length>0) 
    					$scope.data.auth.currentResp = response.data[0];
    				
    				stateService.setCurrentResp(response.data[0]);
    			}, 
    			
    			function errorCallback(response) {
    				modalProgress.close("ok");
    				
    				$scope.data.auth.isAuth = false;
    				$scope.data.auth.pass = undefined;
    				$scope.data.error = response;
    			}
    		);        	
        }
        
        
        $scope.logout = function() {
        	$scope.data.auth.isAuth = false;
        }
    });
	
