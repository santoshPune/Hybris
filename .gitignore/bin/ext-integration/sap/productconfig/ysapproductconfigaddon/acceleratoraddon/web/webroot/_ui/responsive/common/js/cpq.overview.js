CPQ.overview = {
    bindAll: function()
    {
    	CPQ.overview.bindStaticOnClickHandlers();
    	CPQ.overview.bindDynamicOnClickHandlers();
    	CPQ.overview.bindFacets();
	},

	bindStaticOnClickHandlers: function()
	{
		$(".product-details .name").on("click keypress", function(e) {
			CPQ.uihandler.clickHideShowImageGallery(CPQ.overview.doPost, e);
		});
		$(".cpq-btn-goToCart").on("click", function(e) {
			CPQ.overview.clickGoToCartButton(e);
		});
		$(".cpq-btn-skip").on("click", function(e) {
			CPQ.core.firePost(CPQ.core.actionAndRedirect, [ e, CPQ.core.getResetUrl(), "/cart" ]);
		});
		$(".facet__list > li > a").on("click", function(e) {
			CPQ.overview.clickRemoveFilter(e);
		});
		$(".cpq-back-button, .cpq-btn-backToConfig").on("click", function(e) {
			CPQ.overview.clickBackToConfigButton(e);
		});
		$(".cpq-copyvalues-facet .facet__checkbox").on("change", function(e) {
			CPQ.overview.toggleFilterCheckbox(e);
		});
	},

	bindDynamicOnClickHandlers: function()
	{
		$(".facet__list > li > a").on("click", function(e) {
			CPQ.overview.clickRemoveFilter(e);
		});
		$(".cpq-overview-filter-item .facet__checkbox").on("change", function(e) {
			CPQ.overview.clickApplyFilter(e);
		});
		
	},
	
	

    bindFacets: function () {
        $(document).on("click", ".js-show-facets", function (e) {
            e.preventDefault();
            var selectRefinementsTitle = $(this).data("selectRefinementsTitle");
            ACC.colorbox.open(selectRefinementsTitle, {
                href: "#cpq-overview-facet",
                inline: true,
                width: "480px",
                onComplete: function () {
                    $(document).on("click", ".cpq-js-overview-facet .js-facet__name", function (e) {
                        e.preventDefault();
                        $(".cpq-js-overview-facet  .js-facet").removeClass("active");
                        $(this).parents(".js-facet").addClass("active");
                        $.colorbox.resize()
                    })
                },
                onClosed: function () {
                    $(document).off("click", ".cpq-js-overview-facet .js-facet__name");
                }
            });
        });
        enquire.register("screen and (max-width:" + screenSmMax + ")", function () {
            $("#cboxClose").click();
        });
    },

	
	doPost: function(e, data) {
		$.post(CPQ.core.getPageUrl(), data, function() {
			CPQ.core.ajaxRunning = false;
			CPQ.core.ajaxRunCounter--;
		});

		e.preventDefault();
		e.stopPropagation();
	},

	doFilterPost: function(e) {
		$("#filterCPQAction").val("APPLY_FILTER");
		var data = $("#filterform").serialize();

		$.post(CPQ.core.getPageUrl(), data, function(response) {
			if (CPQ.core.ajaxRunCounter === 1) {
				CPQ.overview.updateContent(response);
			}
			
			CPQ.overview.bindDynamicOnClickHandlers();
			CPQ.overview.bindFacets();
			CPQ.core.ajaxRunning = false;
			CPQ.core.ajaxRunCounter--;
		});

		e.preventDefault();
		e.stopPropagation();

	},
	
	clickGoToCartButton: function(e) {
		if ($("#copyAndRedirectCheckBox").is(":checked")){
			CPQ.core.firePost(CPQ.core.actionAndRedirect, [ e, CPQ.core.getCopyUrl(), CPQ.core.getConfigureUrl() ]);
		} else {
			CPQ.core.firePost(CPQ.core.actionAndRedirect, [ e, CPQ.core.getResetUrl(), "/cart" ]);
		}
	},
	
	clickBackToConfigButton: function(e) {
		var sUrl = $(e.currentTarget).data("backToConfig");
		window.location = sUrl;
	},
	
	clickApplyFilter: function(e) {
		CPQ.overview.toggleFilterCheckbox(e);
		
		CPQ.core.firePost(CPQ.overview.doFilterPost, [e]);
	},
	
	toggleFilterCheckbox: function(e) {
		var facet = $(e.currentTarget).siblings().find(".facet__list__mark");
		
		var className = "filter-selected";
		
		if (facet.hasClass(className)) {
			facet.removeClass(className);
		} else {
			facet.addClass(className);
		}
	},
	
	clickRemoveFilter: function(e) {
		var facet = $(e.currentTarget);
		var facetCheckBox = $("#"+facet.data("filter-id"));
		facetCheckBox.prop("value", false);

		CPQ.core.firePost(CPQ.overview.doFilterPost, [e]);
	},
	
	updateContent: function(response) {
		CPQ.uihandler.updateSlotContent(response, "overviewContentSlot");
		CPQ.uihandler.updateSlotContent(response, "overviewSidebarSlot");
	}
};

$(document).ready(function ()
{
	if ($("#overviewContent").length > 0) {
		CPQ.core.pageType = "configOverview";
		CPQ.core.formNameId = "#overviewform";
		CPQ.overview.bindAll();
	}
});
