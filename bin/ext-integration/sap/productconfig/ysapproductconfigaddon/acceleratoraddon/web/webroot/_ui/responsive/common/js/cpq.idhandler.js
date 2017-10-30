CPQ.idhandler = {
	getCsticIdFromCsticFieldId: function(csticFieldId) {
		// Id has format "conflict.1-WCEM_MULTILEVEL._GEN.EXP_NO_USERS.conflicts",
		// remove suffix and prefix "conflict"
		var csticId = csticFieldId.replace(/conflict.(.*).conflicts/g, "$1");
		return csticId;
	},

	getCsticIdFromViolatedCsticFieldId: function(csticFieldId) {
		// Id has format "1-WCEM_MULTILEVEL._GEN.EXP_NO_USERS.conflicts",
		// remove suffix 'conflict'
		var csticId = csticFieldId.substring(0, csticFieldId.length - 10);
		return csticId;
	},

	getGroupIdFromMenuNodeId: function(menuNodeId) {
		// Id has format menuNode_GROUPID,remove prefix
		// 'menuNode_' or "menuLeaf_"
		var groupTitleId = menuNodeId.substring(9, menuNodeId.length);
		return groupTitleId;
	},

	getCsticIdFromConflictCstic: function(csticId) {
		// Id has format conflict.1-WCEM_MULTILEVEL._GEN.EXP_NO_USERS
		// remove prefix "conflict."
		var cstic = csticId;
		if(csticId.startsWith("conflict.")) {
			cstic = csticId.substring(9, csticId.length);
		}
		return cstic;
	},

	getCsticIdFromLableId: function(csticFieldId) {
		// Id has format "1-WCEM_MULTILEVEL._GEN.EXP_NO_USERS.lable",
		// remove suffix 'lable'
		var csticId = csticFieldId.substring(0, csticFieldId.length - 6);
		return csticId;
	},

	getGroupIdFromGroupTitleId: function(groupTitleId) {
		// Id has format GROUPID_title, remove suffix
		// '_title'
		var groupId = groupTitleId.substring(0, groupTitleId.length - 6);
		return groupId;
	}
};
