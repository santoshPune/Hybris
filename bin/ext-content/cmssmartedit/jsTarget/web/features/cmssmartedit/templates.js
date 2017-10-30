angular.module('cmssmarteditTemplates', []).run(['$templateCache', function($templateCache) {
  'use strict';

  $templateCache.put('web/features/cmssmartedit/slotShared/slotSharedButtonTemplate.html',
    "<div class=\"shared-slot-button-template\">\n" +
    "\t<div class=\"btn-group shared-slot-button-template__btn-group\" dropdown dropdown-append-to-body is-open=\"ctrl.isPopupOpened\">\n" +
    "\t\t<button type=\"button\"  id=\"sharedSlotButton-{{::ctrl.slotId}}\" class=\"btn btn-primary dropdown-toggle shared-slot-button-template__btn\" \n" +
    "\t\t\t\tdata-ng-if=\"ctrl.slotSharedFlag\" data-ng-click=\"ctrl.openPopup($event)\">\n" +
    "\t\t\t<img class=\"shared-slot-button-template__btn__img\" data-ng-src=\"{{ ctrl.isPopupOpened && ctrl.sharedOnImageUrl || ctrl.sharedOffImageUrl }}\"/>\n" +
    "\t\t</button>\n" +
    "\t\t<ul class=\"dropdown-menu dropdown-menu-right shared-slot-button-template__menu\" role=\"menu\" id=\"shared-slot-list-{{::ctrl.slotId}}\">\n" +
    "\t\t\t<lh class=\"shared-slot-button-template__menu__title\">\n" +
    "\t\t\t\t<p class=\"shared-slot-button-template__menu__title__text\">{{::'slot.shared.popover.message'| translate}}</p>\n" +
    "\t\t\t</lh>\n" +
    "\t\t</ul>\n" +
    "\t</div>\n" +
    "</div>\n"
  );


  $templateCache.put('web/features/cmssmartedit/slotShared/slotSharedTemplate.html',
    "<slot-shared-button data-slot-id=\"{{::ctrl.smarteditComponentId}}\"></slot-shared-button>\n"
  );


  $templateCache.put('web/features/cmssmartedit/slotVisibility/slotVisibilityButtonTemplate.html',
    "<div class=\"slot-visibility-button-template\">\n" +
    "\t<div class=\"btn-group slot-visibility-button-template__btn-group\" dropdown dropdown-append-to-body is-open=\"ctrl.isComponentListOpened\">\n" +
    "\t\t<button type=\"button\" class=\"btn btn-primary dropdown-toggle slot-visibility-button-template__btn\" \n" +
    "\t\t\t\tdata-ng-click=\"ctrl.openHiddenComponentList($event)\"\n" +
    "\t\t\t\tdata-ng-if=\"ctrl.buttonVisible\" id=\"slot-visibility-button-{{::ctrl.slotId}}\">\n" +
    "\t\t\t<img class=\"slot-visibility-button-template__btn__img \" data-ng-src=\"{{ ctrl.eyeImageUrl }}\"/>{{ ::ctrl.hiddenComponentCount }}\n" +
    "\t\t</button>\n" +
    "\t\t<ul class=\"dropdown-menu dropdown-menu-right slot-visibility-button-template__menu\" role=\"menu\" id=\"slot-visibility-list-{{::ctrl.slotId}}\">\n" +
    "\t\t\t<lh class=\"slot-visibility-button-template__menu__title\">\n" +
    "\t\t\t\t<p class=\"slot-visibility-button-template__menu__title__text\">{{'slotvisibility.list.title' | translate}}</p>\n" +
    "\t\t\t\t<img class=\"slot-visibility-button-template__menu__title__close-btn\" data-ng-src=\"{{ ::ctrl.closeImageUrl }}\" data-ng-click=\"ctrl.closeHiddenComponentList($event)\"/>\n" +
    "\t\t\t</lh>\n" +
    "\t\t\t<li class=\"slot-visibility-component slot-visibility-button-template__menu__component\" data-ng-repeat=\"component in ctrl.hiddenComponents\">\n" +
    "\t\t\t\t<slot-visibility-component data-component=\"component\" data-slot-id=\"{{::ctrl.slotId}}\"></slot-visibility-component>\n" +
    "\t\t\t</li>\n" +
    "\t\t</ul>\n" +
    "\t</div>\n" +
    "</div>\n"
  );


  $templateCache.put('web/features/cmssmartedit/slotVisibility/slotVisibilityComponentTemplate.html',
    "<div class=\"slot-visiblity-component-template\">\n" +
    "\t<div class=\"slot-visiblity-component-template__icon\">\n" +
    "\t\t<img data-ng-src=\"{{::ctrl.imageRoot}}/component_default.png\" class=\"slot-visiblity-component-template__icon__image\" alt=\"{{::ctrl.component.typeCode}}\"/>\n" +
    "\t</div>\n" +
    "\t<div class=\"slot-visiblity-component-template__text\">\n" +
    "\t\t<a class=\"slot-visiblity-component-template__text__name\" href=\"#\" data-ng-click=\"ctrl.openEditorModal()\">{{ ::ctrl.component.name }}</a>\n" +
    "\t\t<p class=\"slot-visiblity-component-template__text__type\">{{ ::ctrl.component.typeCode }}</p>\n" +
    "\t</div>\n" +
    "</div>\n"
  );


  $templateCache.put('web/features/cmssmartedit/slotVisibility/slotVisibilityWidgetTemplate.html',
    "<slot-visibility-button data-slot-id=\"{{::ctrl.smarteditComponentId}}\" \n" +
    "\t\t\t\t\t\tdata-set-remain-open=\"ctrl.setRemainOpen(button, remainOpen)\"/>\n"
  );


  $templateCache.put('web/features/cmssmartedit/synchronize/synchronizeDecoratorTemplate.html',
    "<div>\n" +
    "    <div data-ng-mouseleave=\"mouseleave()\">\n" +
    "        <img title=\"{{'synchronize.notok.tooltip' | translate}}\" data-ng-if=\"!service.synced && visible\" class=\"ng-class:{synchronize:true, clickable:true, 'pull-right':!isContainer}\" data-ng-click=\"service.synchronize()\" data-ng-src=/cmssmartedit/images/synchronization_notok.gif />\n" +
    "        <img title=\"{{'synchronize.ok.tooltip' | translate}}\" data-ng-if=\"service.synced && visible\" class=\"ng-class:{synchronize:true, clickable:true, 'pull-right':!isContainer}\" data-ng-src=/cmssmartedit/images/synchronization_ok.gif />\n" +
    "        <div data-ng-mouseenter=\"mouseenter()\" class=\"ng-class:{minDivHeight:true, contentSlot:isContainer && visible}\">\n" +
    "            <div data-ng-transclude></div>\n" +
    "        </div>\n" +
    "    </div>\n" +
    "</div>\n"
  );

}]);
