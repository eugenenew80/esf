angular.module("apApp", ["ngResource", "ngRoute", "ngAnimate", "ui.bootstrap", "common"])
    .constant("rowsPerPage", 5)
    .constant("idField", "id")

    .factory("apBaseUrl", function(appSettings) {
    	return appSettings.baseUrl + "esf/webapi/"
    })
    
    .config(function ($httpProvider, $routeProvider, $locationProvider) {

    	$locationProvider.html5Mode(true);
    	$locationProvider.hashPrefix("#");
    	
    	
        $httpProvider.defaults.headers.common = {
            'Accept': 'application/json;charset=utf-8'
        };

        
		$routeProvider.when("/apInvoices/:status/list", {
			templateUrl: "ap/features/ap_invoices/views/list.html",
			controller: "apInvoiceListCtrl",
			
			resolve: {
				dataService: function(apInvoiceDataService) {
					return apInvoiceDataService;
				},
				
				descriptionService: function(apInvoiceDescriptionService) {
					return apInvoiceDescriptionService;
				}				
			}			
		}); 
		
		
        $routeProvider.otherwise({
            templateUrl: "logo.html"
        });
    });
