CPQ.config = {
	lastTarget : undefined,
	bindAll : function() {
		this.registerStaticOnClickHandlers();
		this.doAfterPost();
		$( window ).resize(CPQ.config.makeLabelsUnderImagesSameHeight);
	},
	
	registerStaticOnClickHandlers : function() {
		$(".product-details .name").on(
				"click keypress",
				function(e) {
					CPQ.uihandler.clickHideShowImageGallery(
							CPQ.config.doUpdatePost, e);
				});

		$("#cpqMenuArea").on("click", function(e) {
			CPQ.config.menuOpen(e);
		});
	},

	registerOnClickHandlers : function() {
		$(".cpq-group-title-close, .cpq-group-title-open").on("click keypress",
				function(e) {
					CPQ.uihandler.clickGroupHeader(CPQ.config.doUpdatePost, e);
				});

		$(".cpq-btn-addToCart").on("click", function() {
			CPQ.config.clickAddToCartButton();
		});

		$(".cpq-csticlabel-longtext-icon").on('click', function(e) {
			CPQ.config.longTextIconClicked(e);
		});
		

		CPQ.config.registerConflictOnClickHandlers();
		CPQ.config.registerMenuOnClickHandlers();
		CPQ.config.registerPreviousNextOnClickHandlers();
		CPQ.config.registerCsticValueImageOnClickHandler();
	},

	registerConflictOnClickHandlers : function() {
		$(".cpq-conflict-link-to-config").on(
				"click",
				function(e) {
					CPQ.config.handleConflictNavigation(
							CPQ.idhandler.getCsticIdFromCsticFieldId,
							"NAV_TO_CSTIC_IN_GROUP", e);
				});

		$(".cpq-conflict-link").on(
				"click",
				function(e) {
					CPQ.config.handleConflictNavigation(
							CPQ.idhandler.getCsticIdFromViolatedCsticFieldId,
							"NAV_TO_CSTIC_IN_CONFLICT", e);
				});

		$(".cpq-conflict-retractValue-button").on("click", function(e) {
			CPQ.config.handleRetractConflict(e);
		});
	},

	registerMenuOnClickHandlers : function() {
		$(".cpq-menu-node, .cpq-menu-conflict-header").on("click", function(e) {
			CPQ.config.menuGroupToggle(e);
		});

		$(".cpq-menu-leaf, .cpq-menu-conflict-node").on("click", function(e) {
			CPQ.config.menuNavigation(e);
		});

		$(".cpq-menu-icon-remove").on("click", function(e) {
			CPQ.config.menuClose(e);
		});
	},

	registerPreviousNextOnClickHandlers : function() {
		$(".cpq-previous-button").on("click", function(e) {
			CPQ.config.previousNextButtonClicked("PREV_BTN", e);
		});

		$(".cpq-next-button").on("click", function(e) {
			CPQ.config.previousNextButtonClicked("NEXT_BTN", e);
		});
	},
	
	registerCsticValueImageOnClickHandler : function() {
		
		var multiSelectValueImages = $(".cpq-cstic-value-container-multi");
		
		multiSelectValueImages.on("click", function(e) {
			CPQ.config.csticValueImageMultiClicked(e);
		});		

		multiSelectValueImages.on("keypress", function(e) {
			if(e.which === '13' || e.which === '32' ){ // enter or space
				CPQ.config.csticValueImageMultiClicked(e);
			}
		});
		
		
		var singleSelectValueImages = $(".cpq-cstic-value-container-single");
		
		singleSelectValueImages.on("click", function(e) {
			CPQ.config.csticValueImageSingleClicked(e);
		});
		
		singleSelectValueImages.on("keypress", function(e) {
			if(e.which === '13' || e.which === '32' ){ // enter or space
				CPQ.config.csticValueImageSingleClicked(e);
			}
		});
		
		
		var valueImageContainer = $(".cpq-cstic-value-image-container");
		
		valueImageContainer.on("mouseenter", function(e){
			  CPQ.config.hoverOrFocusOnValueImage($(this));
		});
		
		valueImageContainer.on("focusin", function(e){
			  CPQ.config.hoverOrFocusOnValueImage($(this));
		});
		
		valueImageContainer.on("mouseleave", function(e){
			 CPQ.config.hoverLostOrBlurOnVlaueImage($(this));
		});
		
		valueImageContainer.on("focusout", function(e){
			 CPQ.config.hoverLostOrBlurOnVlaueImage($(this));
		});
				
	},
	
	checkValueImageClicked: function(elem) {
		if(elem.hasClass("cpq-cstic-value-image") || elem.find(".cpq-cstic-value-image").length > 0){
			CPQ.config.valueChangeViaImage = true;
		}
	},
	
	hoverOrFocusOnValueImage: function(elem) {
	      if(CPQ.config.valueChangeViaImage){
	    	  CPQ.config.valueChangeViaImage = false;
	      }else{
	    	  elem.addClass("cpq-cstic-value-image-container-hover");
	      }
	},
	
	hoverLostOrBlurOnVlaueImage: function(elem) {
	      elem.removeClass("cpq-cstic-value-image-container-hover");
	      CPQ.config.valueChangeViaImage = false;
	},

	
	csticValueImageMultiClicked: function(e) {
		CPQ.config.checkValueImageClicked($(e.target));
		
		var containerId = $(e.currentTarget).attr('id');
		// remove suffix .container
		var csticValueIdInput = containerId.substring(0, containerId.length - 10) + ".checkBoxWithImage";
		
		var input = $(CPQ.core.encodeId(csticValueIdInput));
		if(input.val() === "true"){
			input.val("false")
		}else{
			input.val("true")
		}
		var cpqAction = "VALUE_CHANGED";
		var path=input.attr('name');
		var data = CPQ.config
		.getSerializedConfigForm(cpqAction, path, false);
		CPQ.core.firePost(CPQ.config.doUpdatePost, [ e, data ]);
	},

	csticValueImageSingleClicked: function(e) {
		CPQ.config.checkValueImageClicked($(e.target));
		
		var containerId = $(e.currentTarget).attr('id');
		// remove suffix .container
		var csticValueNameId = containerId.substring(0, containerId.length - 10) + ".valueName";
		var csticValueNameDiv = $(CPQ.core.encodeId(csticValueNameId));
		var csticValueName = csticValueNameDiv.text();
		var imageGroupId =  csticValueNameDiv.parent().attr('id');
		// suffix .radioGroupWithImage
		var inputId = imageGroupId.substring(0, imageGroupId.length - 20) + ".radioButtonWithImage";
		
		var input = $(CPQ.core.encodeId(inputId));
		input.val(csticValueName);
		
		var cpqAction = "VALUE_CHANGED";
		var path=input.attr('name');
		var data = CPQ.config
		.getSerializedConfigForm(cpqAction, path, false);
		CPQ.core.firePost(CPQ.config.doUpdatePost, [ e, data ]);
	},

	longTextIconClicked : function(e) {
		var labelId = $(e.currentTarget).parent().attr('id');
		var csticId = CPQ.idhandler.getCsticIdFromLableId(labelId);
		var targetId = CPQ.core.encodeId(csticId + ".showFullLongText");
		var cpqAction;
		if ($(targetId).val() !== "true") {
			$(targetId).val("true");
			cpqAction = 'SHOW_FULL_LONG_TEXT';
			$(e.currentTarget).next().show();
		} else {
			$(targetId).val("false");
			cpqAction = 'HIDE_FULL_LONG_TEXT';
			$(e.currentTarget).next().hide();
		}
		csticId = CPQ.idhandler.getCsticIdFromConflictCstic(csticId);

		var data = CPQ.config
				.getSerializedConfigForm(cpqAction, csticId, false);
		CPQ.core.firePost(CPQ.config.doUpdatePost, [ e, data ]);
	},

	previousNextButtonClicked : function(cpqAction, e) {
		$("#autoExpand").val(false);
		var data = CPQ.config.getSerializedConfigForm(cpqAction, "", false);
		CPQ.core.firePost(CPQ.config.doUpdatePost, [ e, data, "##first##" ]);
	},

	menuClose : function(e) {
		CPQ.config.menuRemove();
		CPQ.focushandler.focusOnFirstInput();
		e.preventDefault();
		e.stopPropagation();
	},

	menuGroupToggle : function(e) {
		var menuNodeId = $(e.currentTarget).attr("id");
		var nodeId = CPQ.idhandler.getGroupIdFromMenuNodeId(menuNodeId);
		$("#autoExpand").val(false);
		var data = CPQ.config.getSerializedConfigForm("MENU_NAVIGATION", "",
				false, "", nodeId);
		CPQ.core.firePost(CPQ.config.doUpdatePost, [ e, data ]);
	},

	menuNavigation : function(e) {
		var menuNodeId = $(e.currentTarget).attr("id");
		var nodeId = CPQ.idhandler.getGroupIdFromMenuNodeId(menuNodeId);
		$("#groupIdToDisplay").val(nodeId);
		var data = CPQ.config.getSerializedConfigForm("MENU_NAVIGATION", "",
				true, nodeId);
		CPQ.core.firePost(CPQ.config.doUpdatePost, [ e, data, "##first##" ]);
		CPQ.config.menuRemove();
	},

	menuRemove : function() {
		var sideBar = $("#configSidebarSlot").parent();
		sideBar.addClass("hidden-xs hidden-sm");

		var menu = $("#cpqMenuArea");
		menu.removeClass("hidden-xs hidden-sm");

		var config = $("#configContentSlot").parent();
		config.removeClass("hidden-xs hidden-sm");
		config.addClass("col-xs-12 col-sm-12");

		// show footer header
		$(".main-footer").removeClass("hidden-xs hidden-sm");
		$(".main-header").removeClass("hidden-xs hidden-sm");
		$("#product-details-header").removeClass("hidden-xs hidden-sm");
	},

	menuOpen : function(e) {
		var sideBar = $("#configSidebarSlot").parent();
		sideBar.removeClass("hidden-xs hidden-sm");

		var menu = $("#cpqMenuArea");
		menu.addClass("hidden-xs hidden-sm");

		var config = $("#configContentSlot").parent();
		config.addClass("hidden-xs hidden-sm");
		config.removeClass("col-xs-12 col-sm-12");

		// show footer header
		$(".main-footer").removeClass("hidden-xs hidden-sm");
		$(".main-header").removeClass("hidden-xs hidden-sm");
		$(".product-details-header").removeClass("hidden-xs hidden-sm");

		// hide footer header
		$(".main-footer").addClass("hidden-xs hidden-sm");
		$(".main-header").addClass("hidden-xs hidden-sm");
		$("#product-details-header").addClass("hidden-xs hidden-sm");

		CPQ.focushandler.focusRestore($(".cpq-menu-leaf-selected").attr("id"),
				true, $(window).height() / 4);
		e.preventDefault();
		e.stopPropagation();
	},

	handleConflictNavigation : function(getCsticIdFx, cpqAction, e) {
		var csticFieldId = $(e.currentTarget).parent().attr("id");
		var csticId = getCsticIdFx.apply(this, [ csticFieldId ]);
		$("#autoExpand").val(false);
		var data = CPQ.config.getSerializedConfigForm(cpqAction, csticId, true,
				"");
		CPQ.core.firePost(CPQ.config.doUpdatePost, [ e, data ]);
	},

	handleRetractConflict : function(e) {
		var csticFieldId = $(e.currentTarget).parent().attr("id");
		var csticId = CPQ.idhandler.getCsticIdFromCsticFieldId(csticFieldId);
		var targetId = CPQ.core.encodeId("conflict." + csticId
				+ ".retractValue");
		var path = $(e.currentTarget).parents(".cpq-cstic").children(
				"input:hidden").attr("name");
		$(targetId).val(true);
		var data = CPQ.config.getSerializedConfigForm("RETRACT_VALUE", path,
				false, "");
		CPQ.core.firePost(CPQ.config.doUpdatePost, [ e, data ]);
	},

	ensureMessagesAreVisible : function() {
		if ($(".alert-info").length && !conflicts) {
			$(document).scrollTop($(".alert-info").offset().top - 10);
		}
	},

	makeLabelsUnderImagesSameHeight : function() {
		$('.cpq-csticValue').each(function() {
			  var maxHeight = 0;
			  var lastYOffSet = 0;
			  var actualLine = [];
			  var imagesOfCstic =  $(this).find('.cpq-csticValueImageLabel');
			  imagesOfCstic.height('auto');
			  imagesOfCstic.each(function() {
			    var actualHeight = $(this).height();
			    var actualOffSet = $(this).offset().top;
			    if(lastYOffSet === 0){
			    	// first elem
			    	lastYOffSet = actualOffSet;
			    	maxHeight = actualHeight;
			    }else if(lastYOffSet !== actualOffSet){
			    	// new line
			    	$(actualLine).height(maxHeight);
			    	maxHeight = actualHeight;
			    	lastYOffSet = actualOffSet;
			    	actualLine = [];
			    }else if(actualHeight > maxHeight){
			    	// new max height in actual line
			        maxHeight = actualHeight;
			    }
			    actualLine.push(this);
			  });
			  if(actualLine.length > 0){
				  $(actualLine).height(maxHeight);
				  actualLine = [];
			  }
			});
	},
	
	doAfterPost : function() {
		CPQ.config.registerOnClickHandlers();
		CPQ.config.registerAjax();
		if ($("#focusId").attr("value").length > 0) {
			CPQ.focushandler
					.focusOnInputByCsticKey($("#focusId").attr("value"));
			$("#focusId").val("");
		}

		CPQ.config.ensureMessagesAreVisible();
		CPQ.config.makeLabelsUnderImagesSameHeight();
	},

	doUpdatePost : function(e, data, focusId) {
		$.post(CPQ.core.getPageUrl(), data, function(response) {
			if (CPQ.core.ajaxRunCounter === 1) {
				var redirectTag = '<div id="redirectUrl">';
				var redirectIndex = response.indexOf(redirectTag);
				if (redirectIndex !== -1) {
					redirectIndex = redirectIndex + redirectTag.length;

					var endTag = "</div>";
					var endIndex = response.indexOf(endTag);
					if (endIndex !== -1) {
						var redirect = response.substring(redirectIndex,
								endIndex);
						window.location.replace(redirect);
					}
					return;
				}

				var focusElementId;
				var scrollTo;
				var oldOffsetTop = 0;

				if (focusId) {
					focusElementId = focusId;
					scrollTo = true;
				} else {
					focusElementId = CPQ.focushandler.focusSave();

					scrollTo = false;
					if (focusElementId.length > 0) {
						oldOffsetTop = $(CPQ.core.encodeId(focusElementId))
								.offset().top;
					}

				}
				CPQ.config.updateContent(response);
				if (focusElementId === "##first##") {
					CPQ.focushandler.focusOnFirstInput();
				} else {
					var newOffsetTop = oldOffsetTop;
					if (focusElementId.length > 0 && !scrollTo) {
						var elem = $(CPQ.core.encodeId(focusElementId));
						if (elem.length > 0) {
							newOffsetTop = elem.offset().top;
						}
					}
					CPQ.focushandler.focusRestore(focusElementId, scrollTo,
							newOffsetTop - oldOffsetTop);
				}
				CPQ.config.doAfterPost();
			}
			CPQ.core.ajaxRunning = false;
			CPQ.core.ajaxRunCounter--;
		});

		e.preventDefault();
		e.stopPropagation();
	},

	getSerializedConfigForm : function(cpqAction, focusId, forceExpand,
			groupIdToToggle, groupIdToToggleInSpecTree) {
		$("#cpqAction").val(cpqAction);
		$("#focusId").val(focusId);
		$("#forceExpand").val(forceExpand);
		$("#groupIdToToggle").val(groupIdToToggle);
		$("#groupIdToToggleInSpecTree").val(groupIdToToggleInSpecTree);
		var data = $("#configform").serialize();

		$("#cpqAction").val("");
		$("#focusId").val("");
		$("#forceExpand").val(false);
		$("#groupIdToToggle").val("");
		$("#groupIdToToggleInSpecTree").val("");

		return data;
	},

	updateContent : function(response) {
		CPQ.uihandler.updateSlotContent(response, "configContentSlot");
		CPQ.uihandler.updateSlotContent(response, "configSidebarSlot");
		CPQ.uihandler.updateSlotContent(response, "configBottombarSlot");
		CPQ.uihandler.updateSlotContent(response, "cpq-message-area");
	},

	registerAjax : function() {
		$("#configform").submit(function(e) {
			e.preventDefault();
		});

		// FF and Chrome does fire onChange when enter is pressed in input field
		// and
		// additionally the onKeyPress event
		$("#configform :input").change(function(e) {
			CPQ.config.fireValueChangedPost(e);
		});

		// IE does not fire onChange when enter is pressed in input field, only
		// on
		// focus loss
		$("#configform :input").keypress(function(e) {
			if (e.which === 13) {
				CPQ.config.fireValueChangedPost(e);
			}
		});

		$(document).ajaxError(function(event, xhr) {
			document.write(xhr.responseText);
		});

	},

	hasTargetChanged : function(e) {
		if (!CPQ.config.lastTarget || e.target.id !== CPQ.config.lastTarget.id
				|| e.target.value !== CPQ.config.lastTarget.value
				|| e.target.checked !== CPQ.config.lastTarget.checked) {
			CPQ.config.lastTarget = e.target;
			return true;
		}
		return false;
	},

	fireValueChangedPost : function(e) {
		if (CPQ.config.hasTargetChanged(e)) {
			var path = $(e.currentTarget).parents(".cpq-cstic").children(
					"input:hidden").attr("name");
			var data = CPQ.config.getSerializedConfigForm('VALUE_CHANGED',
					path, false, "");
			setTimeout(function() {
				CPQ.core.firePost(CPQ.config.doUpdatePost, [ e, data ]);
			}, 50);
		} else {
			e.preventDefault();
			e.stopImmediatePropagation();
		}
	},

	clickAddToCartButton : function() {
		var form = $("#configform")[0];
		form.setAttribute("action", CPQ.core.getAddToCartUrl());
		form.submit();
	}
	
};

$(document).ready(function() {
	if ($("#dynamicConfigContent").length > 0) {
		CPQ.core.pageType = "config";
		CPQ.core.formNameId = "#configform";
		CPQ.config.bindAll();
	}
});
