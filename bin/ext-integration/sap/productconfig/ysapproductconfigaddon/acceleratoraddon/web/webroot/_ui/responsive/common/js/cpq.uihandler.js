CPQ.uihandler = {
	toggleExpandCollapseStyle: function (element, elementToBlind, prefix, noBlind) {
		element.toggleClass(prefix + "-open");
		element.toggleClass(prefix + "-close");
		if (noBlind) {
			elementToBlind.toggle();
		} else {
			elementToBlind.slideToggle(100);
		}
	},

	clickHideShowImageGallery: 	function(postFx, e) {
		var element = $("#productName");
		var imageComponent = $("#configImage");
		CPQ.uihandler.toggleExpandCollapseStyle(element, imageComponent, "product-details-glyphicon-chevron");
		$("#cpqAction").val("TOGGLE_IMAGE_GALLERY");
		var data = $(CPQ.core.formNameId).serialize();
		CPQ.core.firePost(postFx, [ e, data ]);
	},

	clickGroupHeader: function(postFx, e){
		var groupElement = $(e.currentTarget);
		var groupTitleId = groupElement.attr("id");
		var groupId = CPQ.idhandler.getGroupIdFromGroupTitleId(groupTitleId);
		CPQ.uihandler.toggleExpandCollapseStyle(groupElement, groupElement.next(), "cpq-group-title");
		$("#cpqAction").val("TOGGLE_GROUP");
		$("#autoExpand").val(false);
		$("#groupIdToToggle").val(groupId);
		var data = $(CPQ.core.formNameId).serialize();
		CPQ.core.firePost(postFx, [ e, data, groupTitleId ]);
	},
	
	updateSlotContent: function(response, slotName) {
		var newSlotContent = CPQ.uihandler.getNewSlotContent(response, slotName);
		$("#" + slotName).replaceWith(newSlotContent);
	},

	getNewSlotContent: function(response, slotName) {
		var startTag = '<div id="start:' + slotName + '" />';
		var endTag = '<div id="end:' + slotName + '" />';
		var newContent = "";

		var startIndex = response.indexOf(startTag);
		if (startIndex !== -1) {
			startIndex = startIndex + startTag.length;
			var endIndex = response.indexOf(endTag);
			if (endIndex !== -1) {
				newContent = response.substring(startIndex, endIndex);
			}
		}
		return newContent;
	}
};

