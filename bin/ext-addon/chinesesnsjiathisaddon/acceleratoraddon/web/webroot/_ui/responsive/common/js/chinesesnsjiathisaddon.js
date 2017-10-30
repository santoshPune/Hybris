$("div.jiathis_style").bind("DOMNodeInserted", function() {
	var divObj = $(this).find("div:first");
	if(typeof $(divObj).attr("class") === "undefined") {
		$(divObj).css("width", "320px");
	}
});