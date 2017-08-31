angular.module("app", ["ngMaterial", "common", "arApp", "apApp"])
	
    //All data services as indexed array
	.factory("dataServices", function (arInvoiceDataService, arConsignee, apInvoiceDataService) {
	    var dataServices = [];
		dataServices["arInvoic"] = arInvoiceDataService;
		dataServices["arConsignee"] = arConsignee;
		dataServices["apInvoice"] = apInvoiceDataService;
		
		return dataServices;
	});  
