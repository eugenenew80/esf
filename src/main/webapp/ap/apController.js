angular.module("apApp")
    .controller("apController", function ($scope, $location) {
    	
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
						desc: "Невыверенные",
						notes: "Список невыверенных счетов-фактур",
						face : imagePath,
						params: [ ],
						hasNodes: false,
						nodes: []	                	  
	                  },
		              
	                  {
						name: "checked",
						desc: "Выверенные",
						notes: "Список выверенных счетов-фактур",
						face : imagePath,
						params: [ ],
						hasNodes: false,
						nodes: []	                	  
			          }      
	             ]
	    };
        
        $scope.onSelectedMenu = function (name) {
            $location.path("/apInvoices/" + name + "/list/");
        };
        
        
        $location.path("/apInvoices/all/list/");
    });
	