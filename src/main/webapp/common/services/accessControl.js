(function() {

	angular.module("common")
		.factory("accessControlService", function(stateService) {
			
			var role=stateService.getUser().role;
			
			return {
				allowAccess: function(featureName) {
				
					if (role=="admin")
						return true;
					
					if (role=="user") {
						switch (featureName) {

							case "nsi":
								return true;
								break;

							case "nsi.ebk":
								return true;
								break;

							case "nsi.ebk.ekr":
								return true;
								break;
							
							case "nsi.ebk.ekr.request.create":
								return true;
								break;

							case "nsi.ebk.ekr.export":
								return true;
								break;

							case "nsi.emf":
								return true;
								break;

							case "nsi.internal":
								return true;
								break;

							default:
								return false;
						}
					}

					if (role=="VIEWER_NSI") {
						switch (featureName) {

							case "nsi":
								return true;
								break;

							case "nsi.ebk":
								return true;
								break;

							case "nsi.ebk.ekr":
								return true;
								break;

							case "nsi.ebk.ekr.search":
								return true;
								break;

							case "nsi.ebk.ekr.request.create":
								return true;
								break;

							case "nsi.ebk.ekr.export":
								return true;
								break;

							case "nsi.ebk.ekr.history":
								return true;
								break;

							case "nsi.ebk.fkr":
								return true;
								break;

							case "nsi.ebk.fkr.search":
								return true;
								break;

							case "nsi.ebk.fkr.request.create":
								return true;
								break;

							case "nsi.ebk.fkr.export":
								return true;
								break;

							case "nsi.ebk.fkr.history":
								return true;
								break;

							case "nsi.ebk.kpb":
								return true;
								break;

							case "nsi.ebk.kpb.search":
								return true;
								break;

							case "nsi.ebk.kpb.request.create":
								return true;
								break;

							case "nsi.ebk.kpb.export":
								return true;
								break;

							case "nsi.ebk.kpb.history":
								return true;
								break;

							case "nsi.ebk.request":
								return true;
								break;

							case "nsi.ebk.request.search":
								return true;
								break;

							case "nsi.ebk.request.edit":
								return true;
								break;

							case "nsi.ebk.request.delete":
								return true;
								break;

							case "nsi.ebk.request.send":
								return true;
								break;

							case "nsi.ebk.request.export":
								return true;
								break;

							case "nsi.ebk.request.view":
								return false;
								break;

							case "nsi.emf":
								return true;
								break;

							case "nsi.internal":
								return true;
								break;

							default:
								return false;
						}
					}

					if (role=="CHIEF_EXPERT_EBK_SPBZ") {
						switch (featureName) {

							case "nsi":
								return true;
								break;

							case "nsi.ebk":
								return true;
								break;

							case "nsi.ebk.ekr":
								return true;
								break;

							case "nsi.ebk.ekr.create":
								return true;
								break;

							case "nsi.ebk.ekr.edit":
								return true;
								break;

							case "nsi.ebk.ekr.transfer":
								return true;
								break;

							case "nsi.ebk.ekr.export":
								return true;
								break;

							case "nsi.ebk.ekr.search":
								return true;
								break;

							case "nsi.ebk.ekr.sendAgree":
								return true;
								break;

							case "nsi.ebk.ekr.history":
								return true;
								break;

							case "nsi.ebk.fkr":
								return true;
								break;

							case "nsi.ebk.fkr.create":
								return true;
								break;

							case "nsi.ebk.fkr.edit":
								return true;
								break;

							case "nsi.ebk.fkr.transfer":
								return true;
								break;

							case "nsi.ebk.fkr.export":
								return true;
								break;

							case "nsi.ebk.fkr.search":
								return true;
								break;

							case "nsi.ebk.fkr.sendAgree":
								return true;
								break;

							case "nsi.ebk.fkr.history":
								return true;
								break;

							case "nsi.ebk.kpb":
								return true;
								break;

							case "nsi.ebk.kpb.create":
								return true;
								break;

							case "nsi.ebk.kpb.edit":
								return true;
								break;

							case "nsi.ebk.kpb.transfer":
								return true;
								break;

							case "nsi.ebk.kpb.export":
								return true;
								break;

							case "nsi.ebk.kpb.search":
								return true;
								break;

							case "nsi.ebk.kpb.sendAgree":
								return true;
								break;

							case "nsi.ebk.kpb.history":
								return true;
								break;

							case "nsi.ebk.request":
								return true;
								break;

							case "nsi.ebk.request.search":
								return true;
								break;

							case "nsi.ebk.request.export":
								return true;
								break;

							case "nsi.ebk.request.cancel":
								return true;
								break;

							case "nsi.ebk.request.accept":
								return true;
								break;

							case "nsi.ebk.request.view":
								return true;
								break;

							case "nsi.ebk.request.edit":
								return false;
								break;

							case "nsi.emf":
								return true;
								break;

							case "nsi.internal":
								return true;
								break;

							default:
								return false;
						}
					}

					if (role=="MANAGER_EBK_SPBZ") {
						switch (featureName) {

							case "nsi":
								return true;
								break;

							case "nsi.ebk":
								return true;
								break;

							case "nsi.ebk.ekr":
								return true;
								break;

							case "nsi.ebk.ekr.export":
								return true;
								break;

							case "nsi.ebk.ekr.search":
								return true;
								break;

							case "nsi.ebk.ekr.reject":
								return true;
								break;

							case "nsi.ebk.ekr.forImprove":
								return true;
								break;

							case "nsi.ebk.ekr.sendUp":
								return true;
								break;

							case "nsi.ebk.ekr.history":
								return true;
								break;

							case "nsi.ebk.fkr":
								return true;
								break;

							case "nsi.ebk.fkr.export":
								return true;
								break;

							case "nsi.ebk.fkr.search":
								return true;
								break;

							case "nsi.ebk.fkr.reject":
								return true;
								break;

							case "nsi.ebk.fkr.forImprove":
								return true;
								break;

							case "nsi.ebk.fkr.sendUp":
								return true;
								break;

							case "nsi.ebk.fkr.history":
								return true;
								break;

							case "nsi.ebk.kpb":
								return true;
								break;

							case "nsi.ebk.kpb.export":
								return true;
								break;

							case "nsi.ebk.kpb.search":
								return true;
								break;

							case "nsi.ebk.kpb.reject":
								return true;
								break;

							case "nsi.ebk.kpb.forImprove":
								return true;
								break;

							case "nsi.ebk.kpb.sendUp":
								return true;
								break;

							case "nsi.ebk.kpb.history":
								return true;
								break;

							case "nsi.emf":
								return true;
								break;

							case "nsi.internal":
								return true;
								break;

							default:
								return false;
						}
					}

					if (role=="CHIEF_EXPERT_EBK_SPBP") {
						switch (featureName) {

							case "nsi":
								return true;
								break;

							case "nsi.ebk":
								return true;
								break;

							case "nsi.ebk.ekr":
								return true;
								break;

							case "nsi.ebk.ekr.export":
								return true;
								break;

							case "nsi.ebk.ekr.search":
								return true;
								break;

							case "nsi.ebk.ekr.reject":
								return true;
								break;

							case "nsi.ebk.ekr.forImprove":
								return true;
								break;

							case "nsi.ebk.ekr.sendApprove":
								return true;
								break;

							case "nsi.ebk.ekr.history":
								return true;
								break;

							case "nsi.emf":
								return true;
								break;

							case "nsi.internal":
								return true;
								break;

							default:
								return false;
						}
					}

					if (role=="MANAGER_EBK_SPBP") {
						switch (featureName) {

							case "nsi":
								return true;
								break;

							case "nsi.ebk":
								return true;
								break;

							case "nsi.ebk.ekr":
								return true;
								break;

							case "nsi.ebk.ekr.export":
								return true;
								break;

							case "nsi.ebk.ekr.search":
								return true;
								break;

							case "nsi.ebk.ekr.reject":
								return true;
								break;

							case "nsi.ebk.ekr.forImprove":
								return true;
								break;

							case "nsi.ebk.ekr.approve":
								return true;
								break;

							case "nsi.ebk.ekr.history":
								return true;
								break;

							case "nsi.emf":
								return true;
								break;

							case "nsi.internal":
								return true;
								break;

							default:
								return false;
						}
					}
					
				}
			}			
		})			
})();
