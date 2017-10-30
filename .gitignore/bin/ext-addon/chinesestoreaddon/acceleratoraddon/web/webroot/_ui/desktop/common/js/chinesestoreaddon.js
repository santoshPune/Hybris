ACC.storefinder = {
	bindAll: function() {},	
	init: function() {
		this.initMap();
		this.bindFindStoresNearMe();
	},	
	initMap: function() {
		if ($("#map_canvas").length > 0) {			
			var $e = $('#map_canvas');
			this.map = new BMap.Map("map_canvas");					

			if ($e.data("southLatitude")) { // Multi-points
				var centerPoint = new BMap.Point($e.data("longitude"), $e.data("latitude"));
				this.map.centerAndZoom(centerPoint, 16);
				this.addAllStores();
			}
			else { // Single-point
				var store = $e.data('stores');
				var centerPoint = new BMap.Point(store.longitude, store.latitude);
				this.map.centerAndZoom(centerPoint, 16);
				this.addStore(store.latitude, store.longitude, store.name);
			}
			
			this.map.addControl(new BMap.MapTypeControl());
			this.map.enableScrollWheelZoom(true);
					
			var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});
			var top_left_navigation = new BMap.NavigationControl();
			this.map.addControl(top_left_control);        
			this.map.addControl(top_left_navigation);     
		}
	},
	addAllStores: function() {		
		var $e = $('#map_canvas'), stores = $e.data('stores');	
		var ps = [], me = this;			
		$.each(stores, function(k, v) {			
			ps.push(new BMap.Point(v.longitude,v.latitude));	
			me.addStore(v.latitude, v.longitude, v.name);
		});
		this.map.setViewport(ps);
	},
	addStore: function(latitude, longitude, name) {
		var me = this;
		var marker = new BMap.Marker(new BMap.Point(longitude, latitude)); 
		var openInfo = function(content, e){
			var p = e.target;
			var point = new BMap.Point(p.getPosition().lng, p.getPosition().lat);
			var infoWindow = new BMap.InfoWindow(content);  
			me.map.openInfoWindow(infoWindow, point);
		};

		this.map.addOverlay(marker);
		marker.addEventListener("click", function(e) { openInfo(name, e); });
	},
	bindFindStoresNearMe: function() {
		var me = this;
		$(document).on("click", "#findStoresNearMe", function(e){
			e.preventDefault();
			var gps = navigator.geolocation;
			
			if (gps) {
				gps.getCurrentPosition(me.positionSuccessStoresNearMe, function (error) {
					console.log("An error occurred... The error code and message are: " + error.code + "/" + error.message);
				});
			}
		});
	},
	positionSuccessStoresNearMe: function(position) {
		$("#latitude").val(position.coords.latitude);
		$("#longitude").val(position.coords.longitude);
		$("#nearMeStorefinderForm").submit();
		return false;
	}	
};


$(function() {
	//remove call google map for long time return in china.
	ACC.global.addGoogleMapsApi = function(callback){
		    if(callback != undefined){
				eval(callback+"()");
			}
	};
	
    $.get(ACC.config.encodedContextPath + "/baidu/key", function(key) {
        var script = document.createElement("script");
        script.src = "https://api.map.baidu.com/api?v=2.0&ak="+key+"&s=1&callback=ACC.storefinder.init";
        document.body.appendChild(script);        
    });
});

