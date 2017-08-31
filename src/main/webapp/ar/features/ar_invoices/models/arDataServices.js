(function () {
    angular.module("arApp")
        .factory('arResourceFactory', function ($resource, admBaseUrl) {
            return {
                newInstance: function (path) {

                    return $resource(
                    	admBaseUrl + path + "/" + ":entityId",

                        {entityId: "@id"},

                        {
                            findAll: {
                                method: 'GET',
                                isArray: true		
                            },

                            findById: {
                                method: 'GET'	
                            },                            
                            
                            findByName: {
                                method: 'GET',
                                url:	admBaseUrl + path + "/" + ":entityName",
                                param: 	{entityName: "@entityName"}
                            },  
                            
                            create: {
                                method: "POST",
                                url:	admBaseUrl + path	
                            },

                            update: {
                                method: "PUT"
                            },
                            
                            remove: {
                                method: "DELETE"
                            },
                            
                            exportToXML: {
                                method: "PUT",
                                url:	admBaseUrl + path + "/export/xml"
                            },            
                            
                            importFromXML: {
                                method: "PUT",
                                url:	admBaseUrl + path + "/import/xml"
                            },                               
                            
                            importFromExternal: {
                                method: "PUT",
                                url:	admBaseUrl + path + "/import/external"
                            },                              
                        }
                    );
                }
            }
        })
        
        .factory("arDataServiceFactory", function ($filter) {
            return {
                newInstance: function (resource) {

                    var elements = null;

                    return {

                    	getElements: function () {
                            elements = resource.findAll();
                            return elements;
                        },
                        
                    	findAll: function (params) {
                            elements = resource.findAll(params);
                            return elements;
                        },
                        

                        findById: function (id) {
                            return resource.findById({entityId: id});
                        },

                        
                        findByName: function (name) {
                            return resource.findByName({entityName: name});
                        },                        
                        

                        create: function (entity) {
                            return resource.create(entity);
                        },  
                        

                        update: function (entity) {
                            return resource.update(entity);
                        }, 
                        
                        
                        remove: function (id) {
                            return resource.remove({entityId: id});
                        },
                        
                        exportToXML: function (invoices) {
                            return resource.exportToXML(invoices);
                        },      
                        
                        importFromXML: function (data) {
                            return resource.importFromXML(data);
                        },             
                        
                        importFromExternal: function (data) {
                            return resource.importFromExternal(data);
                        },                          
                    }

                }
            }
        })
    

        .factory("arInvoiceDataService", function (arDataServiceFactory, arResourceFactory) {
            return arDataServiceFactory.newInstance(arResourceFactory.newInstance('ar_invoice'));
        })
      
        
        .factory("arConsignee", function (arDataServiceFactory, arResourceFactory) {
            return arDataServiceFactory.newInstance(arResourceFactory.newInstance('consignee'));
        })
        
        
        
            
})();
