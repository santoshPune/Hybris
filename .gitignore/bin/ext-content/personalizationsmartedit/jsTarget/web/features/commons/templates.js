angular.module('commonsTemplates', []).run(['$templateCache', function($templateCache) {
  'use strict';

  $templateCache.put('web/features/commons/personalizationsmarteditPagination/personalizationsmarteditPaginationLefttoolbarTemplate.html',
    "<div class=\"row\">\n" +
    "\n" +
    "    <div class=\"col-xs-12\">\n" +
    "        <ul class=\"pagination\">\n" +
    "            <li ng-click=\"prevClick()\" ng-class=\"hasPrevious()?'enabled':'disabled'\"><a>&laquo;</a></li>\n" +
    "            <li ng-repeat=\"i in pagesToDisplay()\"><a ng-click=\"pageClick(i)\" ng-class=\"isActive({{i}})?'active':''\">{{i+1}}</a></li>\n" +
    "            <li ng-click=\"nextClick()\" ng-class=\"hasNext()?'enabled':'disabled'\"><a>&raquo;</a></li>\n" +
    "        </ul>\n" +
    "    </div>\n" +
    "</div>"
  );


  $templateCache.put('web/features/commons/personalizationsmarteditPagination/personalizationsmarteditPaginationTemplate.html',
    "<div class=\"row\">\n" +
    "    <div class=\"col-xs-4\"></div>\n" +
    "    <div class=\"col-xs-4\">\n" +
    "        <ul class=\"pagination pagination-lg\">\n" +
    "            <li ng-click=\"prevClick()\" ng-class=\"hasPrevious()?'enabled':'disabled'\"><a>&laquo;</a></li>\n" +
    "            <li ng-repeat=\"i in pagesToDisplay()\"><a ng-click=\"pageClick(i)\" ng-class=\"isActive({{i}})?'active':''\">{{i+1}}</a></li>\n" +
    "            <li ng-click=\"nextClick()\" ng-class=\"hasNext()?'enabled':'disabled'\"><a>&raquo;</a></li>\n" +
    "        </ul>\n" +
    "    </div>\n" +
    "    <div class=\"col-xs-2\"></div>\n" +
    "    <div class=\"col-xs-2\" ng-if=\"!isFixedPageSize()\">\n" +
    "        <button type=\"button\" class=\"btn btn-link dropdown-toggle pull-right\" data-toggle=\"dropdown\">\n" +
    "            <span ng-bind=\"getCurrentPageSize()\"></span>\n" +
    "            <span data-translate=\"personalization.pagination.rowsperpage\"></span>\n" +
    "            <span class=\"hyicon hyicon-arrow\"></span>\n" +
    "        </button>\n" +
    "        <ul class=\"dropdown-menu pull-right text-left\" role=\"menu\">\n" +
    "            <li ng-repeat=\"i in availablePageSizes() track by $index\"><a ng-click=\"pageSizeClick(i)\">{{i}}</a></li>\n" +
    "        </ul>\n" +
    "    </div>\n" +
    "</div>"
  );

}]);
