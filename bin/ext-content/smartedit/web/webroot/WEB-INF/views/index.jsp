<!DOCTYPE html>
<html>

<head>
	<title data-translate="application.name"></title>
    <!--3rd prty libs-->
    <script src="static-resources/thirdparties/jquery/dist/jquery.min.js"></script>
    <script src="static-resources/thirdparties/jquery-ui-smartedit/jquery-ui.min.js"></script>
    <script src="static-resources/thirdparties/jquery-ui-smartedit/jquery-ui-smartedit-extension.min.js"></script>
    <script src="static-resources/thirdparties/bootstrap/dist/js/bootstrap.min.js"></script>
    <script src="static-resources/thirdparties/angular/angular.js"></script>
    <script src="static-resources/thirdparties/angular-cookies/angular-cookies.js"></script>
    <script src="static-resources/thirdparties/angular-route/angular-route.js"></script>
    <script src="static-resources/thirdparties/angular-resource/angular-resource.js"></script>
    <script src="static-resources/thirdparties/angular-bootstrap/ui-bootstrap-tpls.min.js"></script>
    <script src="static-resources/thirdparties/angular-translate/angular-translate.min.js"></script>
    <script src="static-resources/thirdparties/angular-translate-loader-static-files/angular-translate-loader-static-files.min.js"></script>
    <script src="static-resources/thirdparties/angular-sanitize/angular-sanitize.min.js"></script>
    <script src="static-resources/thirdparties/scriptjs/script.js"></script>
    <script src="static-resources/thirdparties/ui-select/dist/select.js"></script>
    <script src="static-resources/thirdparties/ckeditor/ckeditor.js"></script>
    <script src="static-resources/thirdparties/angular-mocks/angular-mocks.js"></script>
    <script src="static-resources/thirdparties/moment/min/moment-with-locales.min.js"></script>
    <script src="static-resources/thirdparties/eonasdan-bootstrap-datetimepicker/build/js/bootstrap-datetimepicker.min.js"></script>
    <script src="static-resources/thirdparties/polyfills/bindPolyfill.js"></script>
    <script src="static-resources/thirdparties/polyfills/endsWithPolyfill.js"></script>
    <script src="static-resources/thirdparties/polyfills/findPolyfill.js"></script>
    <script src="static-resources/thirdparties/polyfills/uint8ArrayReducePolyfill.js"></script>
		<script src="static-resources/thirdparties/ngInfiniteScroll/build/ng-infinite-scroll.min.js"></script>

    <!-- 3rd party css -->
    <link rel="stylesheet" href="static-resources/dist/smartedit/css/outer-styling.css">
    <link rel="stylesheet" href="static-resources/thirdparties/ui-select/dist/select.min.css">
    <link rel="stylesheet" href="static-resources/thirdparties/eonasdan-bootstrap-datetimepicker/build/css/bootstrap-datetimepicker.min.css">

    <!--libs-->
    <script src="static-resources/smarteditcontainer/js/smarteditcontainer.js"></script>
	<script src="static-resources/smarteditloader/js/smarteditloader.js"></script>

</head>

<body data-ng-app="smarteditloader">
	<div ng-class="{'alert-overlay': true, 'ySEEmptyMessage': (!alerts || alerts.length == 0 ) }">
	    <alerts-box alerts="alerts" />
	</div>
    <div data-ng-view></div>
</body>

</html>
