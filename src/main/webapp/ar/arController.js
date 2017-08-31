angular.module("arApp")
    .controller("arController", function ($scope, $location) {
        
    	var imagePath = 'img/list/menu_318-142617.jpg';
    	
    	//create left menu
        $scope.menu = {
	             name: "root",
	             nodes: [
	                  {
						name: "all",
						desc: "Все",
						notes: "Список всех счетов-фактур",
						face : imagePath,
						params: [ ],
						hasNodes: false,
						nodes: []	                	  
		              },
		                  
	                  {
						name: "new",
						desc: "Непереданные",
						notes: "Список непереданных счетов-фактур",
						face : imagePath,
						params: [ ],
						hasNodes: false,
						nodes: []	                	  
	                  },
		              
	                  {
						name: "exported",
						desc: "Переданные",
						notes: "Список переданных счетов-фактур",
						face : imagePath,
						params: [ ],
						hasNodes: false,
						nodes: []	                	  
			          }      
	             ]
	    };
                
        
        $scope.onSelectedMenu = function (name) {
            $location.path("/arInvoices/" + name + "/list/");
        };
        
        
        $location.path("/arInvoices/all/list/");
    });
	