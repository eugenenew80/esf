angular.module("arApp", ["ngMaterial", "ngResource", "ngRoute", "ngAnimate", "ui.bootstrap", "common"])
    .constant("rowsPerPage", 5)
    .constant("idField", "id")

    .factory("admBaseUrl", function(appSettings) {
    	return appSettings.baseUrl + "esf/webapi/"
    })
    
    .config(function ($httpProvider, $routeProvider, $locationProvider) {
    	
    	$locationProvider.html5Mode(true);
    	$locationProvider.hashPrefix("#");
    	
        $httpProvider.defaults.headers.common = {
            'Accept': 'application/json;charset=utf-8'
        };

        
		$routeProvider.when("/arInvoices/:status/list", {
			templateUrl: "ar/features/ar_invoices/views/list.html",
			controller: "arInvoiceListCtrl",
			
			resolve: {
				dataService: function(arInvoiceDataService) {
					return arInvoiceDataService;
				},
				
				descriptionService: function(arInvoiceDescriptionService) {
					return arInvoiceDescriptionService;
				}				
			}			
		}); 
		
        $routeProvider.otherwise({
            templateUrl: "logo.html"
        });
    });
