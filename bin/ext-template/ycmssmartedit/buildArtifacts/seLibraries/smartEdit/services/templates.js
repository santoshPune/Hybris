angular.module('coretemplates', []).run(['$templateCache', function($templateCache) {
  'use strict';

  $templateCache.put('web/common/core/services/loginDialog.html',
    "<div data-ng-cloak data-ng-class=\"{'smartEditLogin':modalController.initialized!==true, 'smartEditLoginModal':modalController.initialized!==false, 'vertical-center':true}\">\n" +
    "    <div class=\"ySELoginContent\">\n" +
    "        <div class=\"row text-center ySELogo\">\n" +
    "            <img src=\"static-resources/images/SmartEditLogo.png\">\n" +
    "        </div>\n" +
    "        <form data-ng-submit=\"modalController.submit(loginDialogForm)\" class=\"signinForm\" name='loginDialogForm' novalidate>\n" +
    "            <div class=\"row ySELoginAuthMessage\" data-ng-show=\"modalController.initialized!==false\">\n" +
    "                <div data-ng-class=\"{'col-xs-8 col-xs-push-2 text-center':true}\" data-translate=\"logindialogform.reauth.message1\"></div>\n" +
    "                <div data-ng-class=\"{'col-xs-8 col-xs-push-2 text-center':true}\" data-translate=\"logindialogform.reauth.message2\"></div>\n" +
    "            </div>\n" +
    "\n" +
    "            <div class=\"row form-group hyLoginError\" data-ng-if=\"loginDialogForm.errorMessage && loginDialogForm.posted && (loginDialogForm.$invalid || loginDialogForm.failed)\">\n" +
    "                <div data-ng-class=\"{'col-xs-12 col-sm-8 col-md-6 col-sm-push-2 col-md-push-3 ':modalController.initialized!==true, 'col-xs-8 col-xs-push-2':modalController.initialized!==false, 'alert alert-error-bluebg errorDiv':true}\" id=\"invalidError\"> {{loginDialogForm.errorMessage | translate}}</div>\n" +
    "            </div>\n" +
    "\n" +
    "            <div class=\"row form-group \">\n" +
    "                <input data-ng-class=\"{'col-xs-12 col-sm-8 col-md-6 col-sm-push-2 col-md-push-3 ':modalController.initialized!==true, 'col-xs-8 col-xs-push-2':modalController.initialized!==false}\" type=\"text\" id=\"username_{{modalController.authURIKey}}\" name=\"username\" placeholder=\"{{ 'authentication.form.input.username' | translate}}\" autofocus data-ng-model=\"modalController.auth.username\" required/>\n" +
    "            </div>\n" +
    "            <div class=\"row form-group\">\n" +
    "                <input data-ng-class=\"{'col-xs-12 col-sm-8 col-md-6 col-sm-push-2 col-md-push-3 ':modalController.initialized!==true, 'col-xs-8 col-xs-push-2':modalController.initialized!==false}\" type=\"password\" id='password_{{modalController.authURIKey}}' name='password' placeholder=\"{{ 'authentication.form.input.password' | translate}}\" data-ng-model=\"modalController.auth.password\" required/>\n" +
    "            </div>\n" +
    "\n" +
    "            <div class=\"row form-group\">\n" +
    "                <language-selector data-ng-class=\"{'col-xs-12 col-sm-8 col-md-6 col-sm-push-2 col-md-push-3 ':modalController.initialized!==true, 'col-xs-8 col-xs-push-2':modalController.initialized!==false}\"></language-selector>\n" +
    "            </div>\n" +
    "\n" +
    "            <div class=\"row form-group top-buffer2\">\n" +
    "                <button data-ng-class=\"{'btn btn-lg btn-primary':true, 'col-xs-12 col-sm-8 col-md-6 col-sm-push-2 col-md-push-3 ':modalController.initialized!==true, 'col-xs-8 col-xs-push-2':modalController.initialized!==false, 'text-uppercase':true}\" id=\"submit_{{modalController.authURIKey}}\" name=\"submit\" type=\"submit\" data-translate=\"authentication.form.button.submit\"></button>\n" +
    "            </div>\n" +
    "\n" +
    "        </form>\n" +
    "    </div>\n" +
    "</div>"
  );


  $templateCache.put('web/common/core/services/waitDialog.html',
    "<div class=\"row\">\n" +
    "    <div class=\"col-xs-8\">\n" +
    "        <span data-translate=\"wait.dialog.message\"></span>\n" +
    "    </div>\n" +
    "    <div class=\"col-xs-4\">\n" +
    "        <div class=\"spinner pull-right\">\n" +
    "            <div class=\"spinner-container spinner-container1\">\n" +
    "                <div class=\"spinner-circle1\"></div>\n" +
    "                <div class=\"spinner-circle2\"></div>\n" +
    "                <div class=\"spinner-circle3\"></div>\n" +
    "                <div class=\"circle4\"></div>\n" +
    "            </div>\n" +
    "            <div class=\"spinner-container spinner-container2\">\n" +
    "                <div class=\"spinner-circle1\"></div>\n" +
    "                <div class=\"spinner-circle2\"></div>\n" +
    "                <div class=\"spinner-circle3\"></div>\n" +
    "                <div class=\"circle4\"></div>\n" +
    "            </div>\n" +
    "            <div class=\"spinner-container spinner-container3\">\n" +
    "                <div class=\"spinner-circle1\"></div>\n" +
    "                <div class=\"spinner-circle2\"></div>\n" +
    "                <div class=\"spinner-circle3\"></div>\n" +
    "                <div class=\"circle4\"></div>\n" +
    "            </div>\n" +
    "        </div>\n" +
    "    </div>\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/alerts/alertsTemplate.html',
    "<div class=\"row-fluid\" data-ng-if=\"alerts!=null &amp;&amp; alerts.length>0\">\n" +
    "    <div data-ng-repeat=\"alert in alerts\" class=\"ng-class: {'col-xs-12':true, 'alert':true, 'alert-danger':(alert.successful==false),'alert-success':(alert.successful==true)};\">\n" +
    "        <button type=\"button\" class=\"close\" data-ng-hide=\"alert.closeable==false\" data-ng-click=\"dismissAlert($index);\">&times;</button>\n" +
    "        <span id=\"alertMsg\">{{alert.message | translate}}</span>\n" +
    "    </div>\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/authorization/hasOperationPermissionTemplate.html',
    "<div>\n" +
    "    <div ng-if=\"ctrl.isPermissionGranted\">\n" +
    "        <div ng-transclude></div>\n" +
    "    </div>\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/genericEditor/dateTimePicker/dateTimePickerTemplate.html',
    "<div class=\"input-group date ySEDateField\" data-ng-class=\"{'ySEDataDisabled':!isEditable}\" id=\"date-time-picker-{{name}}\">\n" +
    "    <input type='text' class=\"form-control\" placeholder=\"{{ placeholderText | translate}}\" ng-disabled=\"!isEditable\" name=\"{{name}}\" data-ng-model=\"model\" />\n" +
    "    <span class=\"input-group-addon\" data-ng-show=\"isEditable\">\n" +
    "        <span class=\"glyphicon glyphicon-calendar\"></span>\n" +
    "    </span>\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/genericEditor/errors/seGenericEditorFieldErrorsTemplate.html',
    "<div data-ng-if=\"ctrl.getFilteredErrors().length > 0\">\n" +
    "    <span data-ng-repeat=\"error in ctrl.getFilteredErrors()\" class=\"error-input help-block\">\n" +
    "        {{error | translate}}\n" +
    "    </span>\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/genericEditor/genericEditorFieldTemplate.html',
    "<div data-ng-switch=\"field.cmsStructureType\" validation-id=\"{{field.qualifier}}\" data-ng-cloack class=\"ySEField\">\n" +
    "\n" +
    "    <div data-ng-if=\"field.prefixText\" class=\"ySEText ySEFieldPrefix\">{{field.prefixText | translate}}</div>\n" +
    "    <div data-ng-switch-when=\"Boolean\">\n" +
    "        <div data-ng-include=\"'web/common/services/genericEditor/templates/booleanTemplate.html'\"></div>\n" +
    "    </div>\n" +
    "    <div data-ng-switch-when=\"ShortString\">\n" +
    "        <div data-ng-include=\"'web/common/services/genericEditor/templates/shortStringTemplate.html'\"></div>\n" +
    "    </div>\n" +
    "    <div data-ng-switch-when=\"LongString\">\n" +
    "        <div data-ng-include=\"'web/common/services/genericEditor/templates/longStringTemplate.html'\"></div>\n" +
    "    </div>\n" +
    "    <div data-ng-switch-when=\"RichText\">\n" +
    "        <div data-ng-include=\"'web/common/services/genericEditor/templates/richTextTemplate.html'\"></div>\n" +
    "    </div>\n" +
    "    <div data-ng-switch-when=\"LinkToggle\">\n" +
    "        <div data-ng-include=\"'web/common/services/genericEditor/templates/linkToggleTemplate.html'\"></div>\n" +
    "    </div>\n" +
    "    <div data-ng-switch-when=\"Media\">\n" +
    "        <div data-ng-include=\"'web/common/services/genericEditor/templates/mediaTemplate.html'\"></div>\n" +
    "    </div>\n" +
    "    <div data-ng-switch-when=\"MediaContainer\">\n" +
    "        <div data-ng-include=\"'web/common/services/genericEditor/templates/mediaContainerTemplate.html'\"></div>\n" +
    "    </div>\n" +
    "    <div data-ng-switch-when=\"Dropdown\">\n" +
    "        <div data-ng-include=\"'web/common/services/genericEditor/templates/dropdownTemplate.html'\"></div>\n" +
    "    </div>\n" +
    "    <div data-ng-switch-when=\"Date\">\n" +
    "        <div data-ng-include=\"'web/common/services/genericEditor/templates/dateTimePickerWrapperTemplate.html'\"></div>\n" +
    "    </div>\n" +
    "    <div data-ng-switch-when=\"Enum\">\n" +
    "        <div data-ng-include=\"'web/common/services/genericEditor/templates/enumTemplate.html'\"></div>\n" +
    "    </div>\n" +
    "    <se-generic-editor-field-errors data-field=\"field\" data-qualifier=\"qualifier\"></se-generic-editor-field-errors>\n" +
    "    <div data-ng-if=\"field.postfixText\" class=\"ySEText ySEFieldPostfix\">{{field.postfixText | translate}}</div>\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/genericEditor/genericEditorFieldWrapperTemplate.html',
    "<generic-editor-field data-editor=\"model.editor\" data-field=\"model.field\" data-model=\"model.editor.component[model.field.qualifier]\" data-qualifier=\"tabId\" />"
  );


  $templateCache.put('web/common/services/genericEditor/genericEditorTemplate.html',
    "<div data-ng-cloak class=\"ySEGenericEditor\">\n" +
    "    <div>\n" +
    "        <alerts-box alerts=\"editor.alerts\" />\n" +
    "    </div>\n" +
    "    <form name=\"componentForm\" novalidate data-ng-submit=\"editor.submit(componentForm)\" class=\"col-xs-12 no-enter-submit\">\n" +
    "        <div class=\"modal-header\" data-ng-show=\"modalHeaderTitle\">\n" +
    "            <h4 class=\"modal-title\">{{modalHeaderTitle| translate}}</h4>\n" +
    "        </div>\n" +
    "        <div class=\"ySErow\" data-ng-repeat=\"holder in editor.holders;\" data-ng-switch=\"field.cmsStructureType\">\n" +
    "            <div class=\"\">\n" +
    "                <label id=\"{{holder.field.qualifier}}-label\">{{holder.field.i18nKey | lowercase | translate}}\n" +
    "                    <span data-ng-if=\"holder.field.required\">*</span>\n" +
    "                </label>\n" +
    "\n" +
    "            </div>\n" +
    "            <div class=\"\" id=\"{{holder.field.qualifier}}\">\n" +
    "                <localized-element data-ng-if=\"holder.field.localized\" data-model=\"holder\" data-languages=\"editor.languages\" data-input-template=\"web/common/services/genericEditor/genericEditorFieldWrapperTemplate.html\"></localized-element>\n" +
    "                <generic-editor-field data-ng-if=\"!holder.field.localized\" data-editor=\"holder.editor\" data-field=\"holder.field\" data-model=\"editor.component\" data-qualifier=\"holder.field.qualifier\" />\n" +
    "            </div>\n" +
    "        </div>\n" +
    "        <div class=\"ySEBtnRow modal-footer\" data-ng-show=\"(editor.alwaysShowReset || (editor.showReset===true && editor.isDirty())) || (editor.alwaysShowSubmit || (editor.showSubmit===true && editor.isDirty() && componentForm.$valid))\">\n" +
    "            <button type=\"button\" id=\"cancel\" class=\"btn btn-subordinate\" data-ng-if=\"editor.alwaysShowReset || (editor.showReset===true && editor.isDirty())\" data-ng-click=\"reset()\">{{cancelButtonText| translate}}</button>\n" +
    "            <button type=\"submit\" id=\"submit\" class=\"btn btn-primary\" data-ng-if=\"editor.alwaysShowSubmit || (editor.showSubmit===true && editor.isDirty() && componentForm.$valid)\" data-ng-disabled=\"!(editor.isDirty() && componentForm.$valid)\">{{submitButtonText| translate}}</button>\n" +
    "        </div>\n" +
    "    </form>\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/genericEditor/media/components/errorsList/seErrorsListTemplate.html',
    "<div class=\"field-errors\">\n" +
    "    <div data-ng-repeat=\"error in ctrl.getSubjectErrors()\">{{error.message}}</div>\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/genericEditor/media/components/fileSelector/seFileSelectorTemplate.html',
    "<div class=\"seFileSelector {{::ctrl.customClass}}\">\n" +
    "    <label class=\" btn__fileSelector\" data-ng-show=\"!ctrl.disabled\">\n" +
    "        <img class=\"img-upload\" data-ng-src=\"{{::ctrl.uploadIcon}}\" />\n" +
    "        <p class=\"label label__fileUpload label__fileUpload-link\">{{::ctrl.labelI18nKey | translate}}</p>\n" +
    "        <input type=\"file\" class=\"hide\" accept=\"{{ctrl.buildAcceptedFileTypesList()}}\">\n" +
    "    </label>\n" +
    "    <label class=\" btn__fileSelector\" data-ng-show=\"ctrl.disabled\">\n" +
    "        <img class=\"img-upload\" data-ng-src=\"{{::ctrl.uploadIcon}}\" />\n" +
    "        <p class=\"label label__fileUpload\">{{::ctrl.labelI18nKey | translate}}</p>\n" +
    "    </label>\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/genericEditor/media/components/mediaAdvancedProperties/seMediaAdvancedPropertiesCondensedTemplate.html',
    "<a popover-template=\"ctrl.contentUrl\" popover-trigger=\"focus\" tabindex=\"0\" class=\"media-container-advanced-information\">\n" +
    "    <img class=\"media-advanced-information--image\" data-ng-src=\"{{ctrl.advInfoIcon}}\" alt=\"{{ctrl.i18nKeys.INFORMATION | translate}}\" title=\"{{ctrl.i18nKeys.INFORMATION | translate}}\">\n" +
    "    <p class=\"media-advanced-information--p\">{{ctrl.i18nKeys.INFORMATION | translate}}</p>\n" +
    "</a>"
  );


  $templateCache.put('web/common/services/genericEditor/media/components/mediaAdvancedProperties/seMediaAdvancedPropertiesContentTemplate.html',
    "<div class=\"se-adv-media-info\">\n" +
    "    <span class=\"se-adv-media-info--data advanced-information-description\">\n" +
    "        {{ctrl.i18nKeys.DESCRIPTION | translate}}: {{ctrl.description}}\n" +
    "    </span>\n" +
    "    <span class=\"se-adv-media-info--data advanced-information-code\">\n" +
    "        {{ctrl.i18nKeys.CODE | translate}}: {{ctrl.code}}\n" +
    "    </span>\n" +
    "    <span class=\"se-adv-media-info--data advanced-information-alt-text\">\n" +
    "        {{ctrl.i18nKeys.ALT_TEXT | translate}}: {{ctrl.altText}}\n" +
    "    </span>\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/genericEditor/media/components/mediaAdvancedProperties/seMediaAdvancedPropertiesTemplate.html',
    "<a popover-template=\"ctrl.contentUrl\" data-ng-click=\"$event.stopPropagation()\" popover-append-to-body=\"true\" popover-placement=\"left\" popover-trigger=\"click\" tabindex=\"0\" class=\"media-advanced-information btn btn-subordinate\">\n" +
    "    {{ctrl.i18nKeys.INFORMATION | translate}}\n" +
    "    <img class=\"media-selector--preview--icon\" data-ng-src=\"{{ctrl.advInfoIcon}}\">\n" +
    "</a>"
  );


  $templateCache.put('web/common/services/genericEditor/media/components/mediaContainerField/seMediaContainerFieldTemplate.html',
    "<div class=\"se-media-container-field\">\n" +
    "    <div class=\"row\">\n" +
    "        <se-media-format class=\"col-xs-3 se-media-container-cell\" ng-repeat=\"option in ctrl.field.options\" data-media-code=\"ctrl.model[ctrl.qualifier][option.value]\" data-is-under-edit=\"ctrl.isFormatUnderEdit(option.value)\" data-media-format=\"option.value\" data-errors=\"ctrl.field.errors\" data-on-file-select=\"ctrl.fileSelected(files, option.value)\" data-on-delete=\"ctrl.imageDeleted(option.value)\">\n" +
    "        </se-media-format>\n" +
    "\n" +
    "    </div>\n" +
    "    <se-media-upload-form data-ng-if=\"ctrl.image.file\" data-image=\"ctrl.image\" data-field=\"ctrl.field\" data-on-upload-callback=\"ctrl.imageUploaded(code)\" data-on-cancel-callback=\"ctrl.resetImage()\" data-on-select-callback=\"ctrl.fileSelected(files)\">\n" +
    "    </se-media-upload-form>\n" +
    "    <se-errors-list data-errors=\"ctrl.fileErrors\"></se-errors-list>\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/genericEditor/media/components/mediaField/seMediaFieldTemplate.html',
    "<div class=\"media-field\">\n" +
    "    <se-media-selector ng-if=\"!ctrl.image.file\" data-field=\"ctrl.field\" data-model=\"ctrl.model\" data-editor=\"ctrl.editor\" data-qualifier=\"ctrl.qualifier\" data-replace-icon=\"ctrl.replaceIconUrl\" data-delete-icon=\"ctrl.deleteIconUrl\" data-adv-info-icon=\"ctrl.advInfoIconUrl\" data-image-root=\"ctrl.imageRoot\">\n" +
    "    </se-media-selector>\n" +
    "    <se-file-selector ng-if=\"!ctrl.image.file\" data-label-i18n-key=\"ctrl.i18nKeys.UPLOAD_IMAGE_TO_LIBRARY\" data-accepted-file-types=\"ctrl.acceptedFileTypes\" data-on-file-select=\"ctrl.fileSelected(files)\" data-upload-icon=\"ctrl.uploadIconUrl\" data-image-root=\"ctrl.imageRoot\"></se-file-selector>\n" +
    "    <se-media-upload-form ng-if=\"ctrl.image.file\" data-image=\"ctrl.image\" data-field=\"ctrl.field\" data-on-upload-callback=\"ctrl.imageUploaded(code)\" data-on-cancel-callback=\"ctrl.resetImage()\" data-on-select-callback=\"ctrl.fileSelected(files)\"></se-media-upload-form>\n" +
    "    <se-errors-list ng-if=\"ctrl.fileErrors.length > 0\" data-errors=\"ctrl.fileErrors\"></se-errors-list>\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/genericEditor/media/components/mediaFormat/seMediaFormatTemplate.html',
    "<div class=\"{{ctrl.mediaFormat}} se-media-format\">\n" +
    "    <div class=\"se-media-format-screentype\">\n" +
    "        {{ctrl.mediaFormat}}\n" +
    "    </div>\n" +
    "    <!-- when the image is already uploaded -->\n" +
    "    <div class=\"media-present\" data-ng-if=\"ctrl.isMediaCodeValid() && !ctrl.isUnderEdit\">\n" +
    "        <div class=\"media-present-preview\">\n" +
    "            <se-media-preview data-image-url=\"ctrl.media.url\"></se-media-preview>\n" +
    "            <div class=\"se-media-preview-image-wrapper  se-media-format-image-wrapper\">\n" +
    "                <img class=\"thumbnail thumbnail--image-preview\" data-ng-src=\"{{ctrl.media.url}}\">\n" +
    "            </div>\n" +
    "            <span class=\"media-preview-code se-media-format--code\">{{ctrl.media.code}}</span>\n" +
    "\n" +
    "        </div>\n" +
    "\n" +
    "        <se-media-advanced-properties-condensed data-adv-info-icon=\"ctrl.advInfoIconUrl\" data-code=\"ctrl.media.code\" data-description=\"ctrl.media.description\" data-alt-text=\"ctrl.media.altText\"></se-media-advanced-properties-condensed>\n" +
    "\n" +
    "        <se-file-selector data-custom-class=\" 'media-format-present-replace' \" data-label-i18n-key=\"ctrl.i18nKeys.REPLACE \" data-accepted-file-types=\"ctrl.acceptedFileTypes \" data-on-file-select=\"ctrl.onFileSelect({files: files, format: ctrl.mediaFormat}) \" data-upload-icon=\"ctrl.replaceIconUrl \" data-image-root=\"ctrl.imageRoot \"></se-file-selector>\n" +
    "\n" +
    "        <button class=\"media-selector--preview__left remove-image btn btn-subordinate \" data-ng-click=\"ctrl.onDelete({format: ctrl.mediaFormat}) \">\n" +
    "            <img class=\"media-selector--preview--icon \" data-ng-src=\"{{ctrl.deleteIconUrl}} \" alt=\"{{ctrl.i18nKeys.REMOVE | translate}} \" />\n" +
    "            <p class=\"media-selector--preview__left--p media-selector--preview__left--p__error \">{{ctrl.i18nKeys.REMOVE | translate}}</p>\n" +
    "        </button>\n" +
    "    </div>\n" +
    "\n" +
    "    <!-- when the image is not yet uploaded -->\n" +
    "    <div class=\"media-absent \" data-ng-if=\"!ctrl.isMediaCodeValid() && !ctrl.isUnderEdit \">\n" +
    "        <se-file-selector data-label-i18n-key=\"ctrl.i18nKeys.UPLOAD \" data-accepted-file-types=\"ctrl.acceptedFileTypes \" data-on-file-select=\"ctrl.onFileSelect({files: files, format: ctrl.mediaFormat}) \" data-upload-icon=\"ctrl.uploadIconUrl \" data-image-root=\"ctrl.imageRoot \"></se-file-selector>\n" +
    "    </div>\n" +
    "\n" +
    "    <!-- when the image is under edit -->\n" +
    "    <div data-ng-if=\"ctrl.isUnderEdit \" class=\"media-under-edit-parent\">\n" +
    "        <div class=\"media-is-under-edit \">\n" +
    "            <se-file-selector data-label-i18n-key=\"ctrl.i18nKeys.UPLOAD \" data-disabled=\"true\" data-custom-class=\" 'file-selector-disabled' \" data-accepted-file-types=\"ctrl.acceptedFileTypes \" data-on-file-select=\"ctrl.onFileSelect({files: files, format: ctrl.mediaFormat}) \" data-upload-icon=\"ctrl.uploadIconDisabledUrl \" data-image-root=\"ctrl.imageRoot \"></se-file-selector>\n" +
    "        </div>\n" +
    "        <span class=\"media-preview-under-edit \">{{ctrl.i18nKeys.UNDER_EDIT | translate}}</span>\n" +
    "    </div>\n" +
    "\n" +
    "    <div class=\"error-input help-block \" data-ng-repeat=\"error in ctrl.getErrors() \">\n" +
    "        {{error}}\n" +
    "    </div>\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/genericEditor/media/components/mediaPreview/seMediaPreviewContentTemplate.html',
    "<img class=\"preview-image\" data-ng-src=\"{{ctrl.imageUrl}}\" />"
  );


  $templateCache.put('web/common/services/genericEditor/media/components/mediaPreview/seMediaPreviewTemplate.html',
    "<a popover-template=\"ctrl.contentUrl\" data-ng-click=\"$event.stopPropagation()\" popover-append-to-body=\"true\" popover-placement=\"right\" popover-trigger=\"click\" tabindex=\"1\" class=\"media-preview\">\n" +
    "    <span class=\"hyicon hyicon-search media-preview-icon\"></span>\n" +
    "</a>"
  );


  $templateCache.put('web/common/services/genericEditor/media/components/mediaSelector/seMediaSelectorTemplate.html',
    "<div class=\"media-selector\">\n" +
    "    <ui-select id=\"{{ctrl.field.qualifier}}-selector\" ng-model=\"ctrl.model[ctrl.qualifier]\" theme=\"select2\" data-ng-disabled=\"!ctrl.field.editable\" reset-search-input=\"false\" title=\"\" style=\"width:100%\">\n" +
    "        <ui-select-match placeholder=\"{{'genericeditor.dropdown.placeholder' | translate}}\" allow-clear=\"true\">\n" +
    "            <div class=\"media-selector--preview\">\n" +
    "                <div class=\"row\">\n" +
    "                    <span class=\"col-xs-6\">\n" +
    "                        <se-media-preview data-image-url=\"ctrl.field.options[ctrl.qualifier][0].url\"></se-media-preview>{{ctrl.imagePreviewUrl}}\n" +
    "                        <div class=\"se-media-preview-image-wrapper\">\n" +
    "                            <img class=\"thumbnail thumbnail--image-preview\" data-ng-src={{$select.selected.url}}>\n" +
    "                        </div>\n" +
    "                        <span class=\"media-preview-code\">{{$select.selected.code}}</span>\n" +
    "                    </span>\n" +
    "                    <span class=\"col-xs-6 media-selector--preview--2\">\n" +
    "                        <se-media-advanced-properties class=\"media-selector--preview__right\" data-code=\"ctrl.field.options[ctrl.qualifier][0].code\" data-description=\"ctrl.field.options[ctrl.qualifier][0].description\" data-alt-text=\"ctrl.field.options[ctrl.qualifier][0].altText\" data-adv-info-icon=\"ctrl.advInfoIcon\"></se-media-advanced-properties>\n" +
    "\n" +
    "                        <button class=\"media-selector--preview__right replace-image btn btn-subordinate\" data-ng-click=\"ctrl.onDelete($select, $event)\">\n" +
    "                            <p class=\"media-selector--preview__right--p\">{{'upload.image.replace' | translate}}</p>\n" +
    "                            <img class=\"media-selector--preview--icon\" data-ng-src=\"{{ctrl.replaceIcon}}\" />\n" +
    "                        </button>\n" +
    "                        <button class=\"media-selector--preview__right remove-image btn btn-subordinate\" data-ng-click=\"ctrl.onDelete($select, $event)\">\n" +
    "                            <p class=\"media-selector--preview__right--p\">{{'media.format.remove' | translate}}</p>\n" +
    "                            <img class=\"media-selector--preview--icon\" data-ng-src=\"{{ctrl.deleteIcon}}\" />\n" +
    "                        </button>\n" +
    "                    </span>\n" +
    "                </div>\n" +
    "            </div>\n" +
    "        </ui-select-match>\n" +
    "        <ui-select-choices id=\"{{ctrl.field.qualifier}}-list\" repeat=\"option.code as option in ctrl.field.options[ctrl.qualifier]\" refresh=\"ctrl.editor.refreshOptions(ctrl.field, ctrl.qualifier, $select.search)\" value=\"{{$select.selected.code}}\" refresh-delay=\"0\" position=\"down\">\n" +
    "            <small>\n" +
    "                <img class=\"thumbnail\" data-ng-src={{option.url}}>\n" +
    "                <span>{{option.code}}</span>\n" +
    "            </small>\n" +
    "        </ui-select-choices>\n" +
    "    </ui-select>\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/genericEditor/media/components/mediaUploadForm/seMediaUploadFieldTemplate.html',
    "<div class=\"form-control se-mu--fileinfo--wrapper\">\n" +
    "    <div class=\"row\">\n" +
    "        <div class=\"se-mu--fileinfo--wrapper-col1 col-xs-10\">\n" +
    "            <input type=\"text\" data-ng-class=\"{'se-mu--fileinfo--field--input': true, 'se-mu--fileinfo--field__error': ctrl.error}\" class=\"se-mu--fileinfo--field--input\" name=\"{{ctrl.field}}\" data-ng-model=\"ctrl.model[ctrl.field]\">\n" +
    "        </div>\n" +
    "\n" +
    "        <div class=\"se-mu--fileinfo--wrapper-col2 col-xs-2\">\n" +
    "            <img src=\"static-resources/images/edit_icon.png\" class=\"se-mu--fileinfo--field--icon \" data-ng-if=\"ctrl.displayImage && !ctrl.error\" />\n" +
    "            <img src=\"static-resources/images/edit_icon_error.png\" class=\"se-mu--fileinfo--field--icon__error \" data-ng-if=\"ctrl.error\" />\n" +
    "        </div>\n" +
    "    </div>\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/genericEditor/media/components/mediaUploadForm/seMediaUploadFormTemplate.html',
    "<div class=\"se-media-upload-form\">\n" +
    "\n" +
    "    <div class=\"container-fluid se-media-upload-form--toolbar\">\n" +
    "        <div class=\"navbar-left se-media-upload-form--toolbar--title\">\n" +
    "            <h4 class=\"se-media-upload-form--toolbar--title--h4\">{{ctrl.i18nKeys.UPLOAD_IMAGE_TO_LIBRARY | translate}}</h4>\n" +
    "        </div>\n" +
    "        <div class=\"navbar-right se-media-upload-form--toolbar--buttons\">\n" +
    "            <div class=\"y-toolbar\">\n" +
    "                <div class=\"y-toolbar-cell\">\n" +
    "                    <button class=\"btn btn-subordinate btn-lg navbar-btn se-media-upload-btn__cancel\" type=\"button\" data-ng-click=\"ctrl.onCancel()\">{{ctrl.i18nKeys.UPLOAD_IMAGE_CANCEL | translate}}</button>\n" +
    "                </div>\n" +
    "                <div class=\"y-toolbar-cell\">\n" +
    "                    <button class=\"btn btn-default btn-lg navbar-btn se-media-upload-btn__submit\" type=\"button\" data-ng-click=\"ctrl.onMediaUploadSubmit()\">{{ctrl.i18nKeys.UPLOAD_IMAGE_SUBMIT | translate}}</button>\n" +
    "                </div>\n" +
    "            </div>\n" +
    "        </div>\n" +
    "    </div>\n" +
    "    <div class=\"container-fluid se-media-upload--filename\">\n" +
    "        <div class=\"row\">\n" +
    "            <div class=\"se-media-upload--filename-col1 col-xs-10\">\n" +
    "                <img src=\"static-resources/images/image_placeholder.png\">\n" +
    "                <div class=\"se-media-upload--fn--name\">{{ctrl.getTruncatedName()}}</div>\n" +
    "            </div>\n" +
    "            <div class=\"se-media-upload--filename-col2 col-xs-2\">\n" +
    "                <se-file-selector data-label-i18n-key=\"ctrl.i18nKeys.UPLOAD_IMAGE_REPLACE\" data-accepted-file-types=\"ctrl.acceptedFileTypes\" data-on-file-select=\"ctrl.onSelectCallback({files: files})\"></se-file-selector>\n" +
    "\n" +
    "            </div>\n" +
    "        </div>\n" +
    "    </div>\n" +
    "    <form>\n" +
    "        <div class=\"se-media-upload--fileinfo\">\n" +
    "            <div class=\"se-media-upload--fileinfo--field form-group\">\n" +
    "                <label name=\"label-description\" data-ng-class=\"{ 'se-media-upload-has-error': ctrl.hasError('description'), 'se-media-upload--fileinfo--label': true }\">{{ctrl.i18nKeys.DESCRIPTION | translate}}</label>\n" +
    "                <se-media-upload-field data-field=\"'description'\" data-model=\"ctrl.imageParameters\"></se-media-upload-field>\n" +
    "                <span class=\"upload-field-error upload-field-error-description\" data-ng-repeat=\"error in ctrl.getErrorsForField('description')\">{{error}}</span>\n" +
    "            </div>\n" +
    "\n" +
    "            <div class=\"se-media-upload--fileinfo--field form-group\">\n" +
    "                <label name=\"label-code\" data-ng-class=\"{ 'se-media-upload-has-error': ctrl.hasError('code')}\">{{ctrl.i18nKeys.CODE | translate}}*</label>\n" +
    "                <se-media-upload-field data-error=\"ctrl.hasError('code')\" data-field=\"'code'\" data-model=\"ctrl.imageParameters\"></se-media-upload-field>\n" +
    "                <span class=\"upload-field-error upload-field-error-code\" data-ng-repeat=\"error in ctrl.getErrorsForField('code')\">{{error}}</span>\n" +
    "            </div>\n" +
    "\n" +
    "            <div class=\"se-media-upload--fileinfo--field form-group\">\n" +
    "                <label name=\"label-alt-text\" data-ng-class=\"{ 'se-media-upload-has-error': ctrl.hasError('altText') }\">{{ctrl.i18nKeys.ALT_TEXT | translate}}</label>\n" +
    "                <se-media-upload-field data-field=\"'altText'\" data-model=\"ctrl.imageParameters\"></se-media-upload-field>\n" +
    "                <span class=\"upload-field-error upload-field-error-alt-text \" data-ng-repeat=\"error in ctrl.getErrorsForField('altText')\">{{error}}</span>\n" +
    "            </div>\n" +
    "        </div>\n" +
    "\n" +
    "    </form>\n" +
    "    <span data-ng-if=\"ctrl.isUploading\" class=\"upload-image-in-progress\">\n" +
    "        <!--<img src=\"static-resources/images/spinner.png\"> {{ctrl.i18nKeys.UPLOADING | translate}}-->\n" +
    "    </span>\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/genericEditor/richText/seRichTextFieldTemplate.html',
    "<textarea class=\"ng-class:{'col-xs-12':true, 'has-error':ctrl.field.errors.length>0}\" name=\"{{ctrl.field.qualifier}}-{{ctrl.qualifier}}\" data-ng-disabled=\"!ctrl.field.editable\" data-ng-model=\"ctrl.model[ctrl.qualifier]\" data-ng-change=\"ctrl.reassignUserCheck()\"></textarea>\n" +
    "<div data-ng-if=\"ctrl.requiresUserCheck()\">\n" +
    "    <input type=\"checkbox\" data-ng-model=\"ctrl.field.isUserChecked\" />\n" +
    "    <span class=\"ng-class:{'warning-check-msg':true, 'not-checked':ctrl.editor.hasFrontEndValidationErrors && !ctrl.field.isCheckedByUser}\" data-translate=\"editor.richtext.check\">{{'editor.richtext.check' | translate}}</span>\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/genericEditor/templates/booleanTemplate.html',
    "<div class=\"ySEBooleanField\">\n" +
    "    <span class=\"y-toggle y-toggle-lg\">\n" +
    "        <input type=\"checkbox\" id=\"{{field.qualifier}}-checkbox\" class=\"ySEBooleanField__input\" placeholder=\"{{field.tooltip| translate}}\" name=\"{{field.qualifier}}\" data-ng-disabled=\"!field.editable\" data-ng-model=\"model[qualifier]\" />\n" +
    "        <label class=\"ySEBooleanField__label\" for=\"{{field.qualifier}}-checkbox\"></label>\n" +
    "        <p data-ng-if=\"field.labelText && !model[qualifier]\" class=\"ySEBooleanField__text\">{{field.labelText| translate}}</p>\n" +
    "    </span>\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/genericEditor/templates/dateTimePickerWrapperTemplate.html',
    "<date-time-picker data-name=\"field.qualifier\" data-model=\"model[qualifier]\" data-is-editable=\"field.editable\"></date-time-picker>"
  );


  $templateCache.put('web/common/services/genericEditor/templates/dropdownTemplate.html',
    "<ui-select id=\"{{field.qualifier}}-selector\" ng-model=\"model[qualifier]\" theme=\"select2\" title=\"\" style=\"width:100%\" search-enabled=\"false\" data-dropdown-auto-width=\"false\">\n" +
    "    <ui-select-match placeholder=\"Select an option\">\n" +
    "        <span ng-bind=\"$select.selected.label\"></span>\n" +
    "    </ui-select-match>\n" +
    "    <ui-select-choices id=\"{{field.qualifier}}-list\" repeat=\"option in field.options\" position=\"down\" value=\"{{$selected.selected}}\">\n" +
    "        <span ng-bind=\"option.label\"></span>\n" +
    "    </ui-select-choices>\n" +
    "</ui-select>"
  );


  $templateCache.put('web/common/services/genericEditor/templates/enumTemplate.html',
    "<ui-select id=\"{{field.qualifier}}-selector\" ng-model=\"model[qualifier]\" theme=\"select2\" data-ng-disabled=\"!field.editable\" reset-search-input=\"false\" title=\"\" style=\"width:100%\">\n" +
    "    <ui-select-match placeholder=\"{{'genericeditor.dropdown.placeholder' | translate}}\" allow-clear=\"true\">\n" +
    "        <span id=\"enum-{{field.qualifier}}\">{{$select.selected.label}}</span>\n" +
    "        <br/>\n" +
    "    </ui-select-match>\n" +
    "    <ui-select-choices id=\"{{field.qualifier}}-list\" repeat=\"option.code as option in field.options[qualifier]\" refresh=\"editor.refreshOptions(field, qualifier, $select.search)\" value=\"{{$select.selected.code}}\" refresh-delay=\"0\" position=\"down\">\n" +
    "        <small>\n" +
    "            <span>{{option.label}}</span>\n" +
    "        </small>\n" +
    "    </ui-select-choices>\n" +
    "</ui-select>"
  );


  $templateCache.put('web/common/services/genericEditor/templates/errorMessageTemplate.html',
    "<span data-ng-if=\"field.errors\" data-ng-repeat=\"error in field.errors | filter:{language:qualifier}\" id='validation-error-{{field.qualifier}}-{{qualifier}}-{{$index}}' class=\"error-input help-block\">\n" +
    "    {{error.message|translate}}\n" +
    "</span>"
  );


  $templateCache.put('web/common/services/genericEditor/templates/linkToggleTemplate.html',
    "<div>\n" +
    "    <input type=\"radio\" name=\"external\" id=\"external\" data-ng-model=\"model.external\" data-ng-value=\"true\" data-ng-change=\"editor.emptyUrlLink()\" />\n" +
    "    <label>{{field.externalI18nKey| translate}}</label>\n" +
    "    <input type=\"radio\" name=\"external\" id=\"internal\" data-ng-model=\"model.external\" data-ng-value=\"false\" data-ng-change=\"editor.emptyUrlLink()\" />\n" +
    "    <label>{{field.internalI18nKey| translate}}</label>\n" +
    "    <br/>\n" +
    "    <input type=\"text\" id=\"urlLink\" name=\"urlLink\" data-ng-model=\"model.urlLink\" class=\"ng-class:{'col-xs-12':true, 'form-control':true, 'has-error':field.errors.length>0} \" />\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/genericEditor/templates/longStringTemplate.html',
    "<textarea class=\"ng-class:{'col-xs-12':true, 'has-error':field.errors.length>0}\" placeholder=\"{{field.tooltip| translate}}\" name=\"{{field.qualifier}}\" data-ng-disabled=\"!field.editable\" data-ng-model=\"model[qualifier]\" data-ng-maxlength=\"1000\"></textarea>"
  );


  $templateCache.put('web/common/services/genericEditor/templates/mediaContainerTemplate.html',
    "<se-media-container-field data-field=\"field\" data-model=\"model\" data-editor=\"editor\" data-qualifier=\"qualifier\"></se-media-container-field>"
  );


  $templateCache.put('web/common/services/genericEditor/templates/mediaTemplate.html',
    "<se-media-field data-field=\"field\" data-model=\"model\" data-editor=\"editor\" data-qualifier=\"qualifier\"></se-media-field>"
  );


  $templateCache.put('web/common/services/genericEditor/templates/richTextTemplate.html',
    "<se-rich-text-field data-field=\"field\" data-qualifier=\"qualifier\" data-model=\"model\" data-editor=\"editor\"></se-rich-text-field>"
  );


  $templateCache.put('web/common/services/genericEditor/templates/shortStringTemplate.html',
    "<input type=\"text\" id=\"{{field.qualifier}}-shortstring\" class=\"ng-class:{'col-xs-12':true, 'form-control':true, 'has-error':field.errors.length>0} \" placeholder=\"{{field.tooltip| translate}}\" name=\"{{field.qualifier}}\" data-ng-disabled=\"!field.editable\" data-ng-model=\"model[qualifier]\" />"
  );


  $templateCache.put('web/common/services/languageSelector/languageSelectorTemplate.html',
    "<ui-select id=\"uiSelectToolingLanguage\" ng-model=\"ctrl.selectedLanguage\" on-select=\"ctrl.setSelectedLanguage($item)\" theme=\"select2\" title=\"\" style=\"width:100%\" search-enabled=\"false\" data-dropdown-auto-width=\"false\">\n" +
    "    <ui-select-match placeholder=\"{{'languageselector.dropdown.placeholder' | translate}}\">\n" +
    "        <span ng-bind=\"ctrl.selectedLanguage.name \"></span>\n" +
    "    </ui-select-match>\n" +
    "\n" +
    "    <ui-select-choices repeat=\"language in ctrl.languages track by language.isoCode\" position=\"down \">\n" +
    "        <span ng-bind=\"language.name\"></span>\n" +
    "    </ui-select-choices>\n" +
    "</ui-select>"
  );


  $templateCache.put('web/common/services/localizedElement/localizedElementTemplate.html',
    "<y-tabset data-model=\"model\" tabs-list=\"tabs\" data-num-tabs-displayed=\"6\">\n" +
    "</y-tabset>"
  );


  $templateCache.put('web/common/services/modalTemplate.html',
    "<div class=\"modal-header\" data-ng-if=\"modalController._modalManager.title\">\n" +
    "    <button type=\"button\" class=\"close\" data-ng-if=\"modalController._modalManager._showDismissButton()\" data-ng-click=\"modalController._modalManager._handleDismissButton()\">\n" +
    "        <span class=\"hyicon hyicon-close\"></span>\n" +
    "    </button>\n" +
    "    <h4 id=\"smartedit-modal-title-{{ modalController._modalManager.title }}\">{{ modalController._modalManager.title | translate }}&nbsp;{{ modalController._modalManager.titleSuffix | translate }}</h4>\n" +
    "</div>\n" +
    "<div class=\"modal-body\" id=\"modalBody\">\n" +
    "    <div data-ng-include=\"modalController.templateUrl\" />\n" +
    "</div>\n" +
    "<div class=\"modal-footer\" data-ng-if=\"modalController._modalManager._hasButtons()\">\n" +
    "    <span data-ng-repeat=\"button in modalController._modalManager.getButtons()\">\n" +
    "        <button id='{{ button.id }}' type=\"button\" data-ng-disabled=\"button.disabled\" data-ng-class=\"{ 'btn':true, 'btn-subordinate':button.style=='default', 'btn-primary':button.style=='primary' }\" data-ng-click=\"modalController._modalManager._buttonPressed(button)\">{{ button.label | translate }}</button>\n" +
    "    </span>\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/tabset/yTabsetTemplate.html',
    "<div>\n" +
    "    <ul class=\"nav nav-tabs\">\n" +
    "        <li ng-if=\"tabsList.length!=numTabsDisplayed\" data-ng-repeat=\"tab in tabsList.slice( 0, numTabsDisplayed-1 ) track by $index\" data-ng-class=\"{'active': tab.active }\" data-tab-id=\"{{tab.id}}\">\n" +
    "            <a data-ng-class=\"{'sm-tab-error': tab.hasErrors}\" data-ng-click=\"selectTab(tab)\" data-ng-if=\"!tab.message\">{{tab.title | translate}}</a>\n" +
    "            <a data-ng-class=\"{'sm-tab-error': tab.hasErrors}\" data-ng-click=\"selectTab(tab)\" data-ng-if=\"tab.message\" data-popover=\"{{tab.message}}\" data-popover-trigger=\"mouseenter\">{{tab.title | translate}}</a>\n" +
    "        </li>\n" +
    "        <li ng-if=\"tabsList.length==numTabsDisplayed\" data-ng-repeat=\"tab in tabsList.slice( 0, numTabsDisplayed ) track by $index\" data-ng-class=\"{'active': tab.active }\" data-tab-id=\"{{tab.id}}\">\n" +
    "            <a data-ng-class=\"{'sm-tab-error': tab.hasErrors}\" data-ng-click=\"selectTab(tab)\" data-ng-if=\"!tab.message\">{{tab.title | translate}}</a>\n" +
    "            <a data-ng-class=\"{'sm-tab-error': tab.hasErrors}\" data-ng-click=\"selectTab(tab)\" data-ng-if=\"tab.message\" data-popover=\"{{tab.message}}\" data-popover-trigger=\"mouseenter\">{{tab.title | translate}}</a>\n" +
    "        </li>\n" +
    "        <li data-ng-if=\"tabsList.length > numTabsDisplayed\" class=\"dropdown\" data-ng-class=\"{'active': tabsList.indexOf(selectedTab) >= numTabsDisplayed}\">\n" +
    "            <a data-ng-class=\"{'sm-tab-error': dropDownHasErrors()}\" class=\"dropdown-toggle\" data-toggle=\"dropdown\" data-translate=\"ytabset.tabs.more\">\n" +
    "                <span class=\"caret\"></span>\n" +
    "            </a>\n" +
    "            <ul class=\"dropdown-menu\">\n" +
    "                <li ng-if=\"tabsList.length!=numTabsDisplayed\" data-ng-repeat=\"tab in tabsList.slice( numTabsDisplayed-1)\" data-tab-id=\"{{tab.id}}\">\n" +
    "                    <a data-ng-class=\"{'sm-tab-error': tab.hasErrors}\" data-ng-click=\"selectTab(tab)\">{{tab.title | translate}}</a>\n" +
    "                </li>\n" +
    "            </ul>\n" +
    "        </li>\n" +
    "    </ul>\n" +
    "\n" +
    "    <div>\n" +
    "        <y-tab data-ng-repeat=\"tab in tabsList\" ng-show=\"tab.active\" data-tab-id=\"{{tab.id}}\" content=\"tab.templateUrl\" data-model=\"model\" tab-control=\"tabControl\">\n" +
    "        </y-tab>\n" +
    "    </div>\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/wizard/modalWizardNavBarTemplate.html',
    "<div class=\"modal-wizard-template\">\n" +
    "    <div class=\"modal-wizard-template__steps\">\n" +
    "        <div data-ng-repeat=\"action in wizardController._wizardContext.navActions\" class=\"modal-wizard-template-step\">\n" +
    "            <button id=\"action.id\" data-ng-class=\"{ 'btn modal-wizard-template-step__action': true, 'modal-wizard-template-step__action__enabled': action.enableIfCondition(), 'modal-wizard-template-step__action__disabled': !action.enableIfCondition(), 'modal-wizard-template-step__action__current': action.isCurrentStep() }\" data-ng-click=\"wizardController.executeAction(action)\" data-ng-disabled=\"!action.enableIfCondition()\">{{ action.i18n | translate }}\n" +
    "            </button>\n" +
    "            <span data-ng-if=\"!$last\" class=\"\" data-ng-class=\"{ 'modal-wizard-template-step__glyph-enabled':  action.enableIfCondition(), 'modal-wizard-template-step__glyph-disabled': !action.enableIfCondition()}\"></span>\n" +
    "\n" +
    "        </div>\n" +
    "    </div>\n" +
    "\n" +
    "    <div class=\"modal-wizard-template-content\" data-ng-include=\"wizardController._wizardContext.templateUrl\"></div>\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/wizard/modalWizardTemplate.html',
    "<div>\n" +
    "    <div data-ng-include=\"wizardController._wizardContext.templateOverride || wizardController._wizardContext.templateUrl\"></div>\n" +
    "</div>"
  );


  $templateCache.put('web/smarteditcontainer/core/services/experienceSelectorWidget/experienceSelectorWidgetTemplate.html',
    "<div dropdown is-open=\"status.isopen\" class=\"ySEpreview\" auto-close=\"disabled\">\n" +
    "    <button type=\"button\" dropdown-toggle id=\"experience-selector-btn\" class=\"btn yWebsiteSelectBtn\" aria-pressed=\"false\" title=\"{{action.name}}\" alt=\"{{action.name}}\" data-ng-click=\"resetExperienceSelector()\">\n" +
    "        <span class=\"yWebsiteSelectBtn--label\" data-translate=\"experience.selector.previewing\"></span>\n" +
    "        <span class=\"yWebsiteSelectBtn--text\">{{buildExperienceText()}}</span>\n" +
    "        <span class=\"hyicon hyicon-arrow \"></span>\n" +
    "    </button>\n" +
    "    <div class=\"dropdown-menu bottom btn-block yDdResolution \" role=\"menu\">\n" +
    "        <experience-selector data-experience=\"experience\" data-dropdown-status=\"status\" data-reset-experience-selector=\"resetExperienceSelector\"></experience-selector>\n" +
    "    </div>\n" +
    "</div>"
  );


  $templateCache.put('web/smarteditcontainer/core/services/inflectionPointSelectorWidget/inflectionPointSelectorWidgetTemplate.html',
    "<div dropdown is-open=\"status.isopen\" auto-close=\"disabled\" id=\"inflectionPtDropdown\">\n" +
    "    <button type=\"button\" class=\"btn btn-default yHybridAction yDddlbResolution\" dropdown-toggle ng-disabled=\"disabled \" aria-pressed=\"false\">\n" +
    "        <img data-ng-src=\"{{imageRoot}}{{currentPointSelected.icon}}\" data-ng-typeSelected=\"{{currentPointSelected.type}}\" />\n" +
    "    </button>\n" +
    "    <ul class=\"dropdown-menu bottom btn-block yDdResolution \" role=\"menu\">\n" +
    "        <li ng-repeat=\"choice in points\" class=\"item text-center\">\n" +
    "            <a href data-ng-click=\"selectPoint(choice);\">\n" +
    "                <img data-ng-show=\"currentPointSelected.type !== choice.type\" data-ng-src=\"{{imageRoot}}{{choice.icon}}\" class=\"file\" />\n" +
    "                <img data-ng-show=\"currentPointSelected.type === choice.type\" data-ng-src=\"{{imageRoot}}{{choice.selectedIcon}}\" class=\"file\" />\n" +
    "            </a>\n" +
    "        </li>\n" +
    "    </ul>\n" +
    "</div>"
  );


  $templateCache.put('web/smarteditcontainer/core/services/pageToolMenu/clonePage/clonePageTemplate.html',
    "<div id=\"clone-page-template\">\n" +
    "    {{ \"template.clone.page.title\" | translate }}\n" +
    "</div>"
  );


  $templateCache.put('web/smarteditcontainer/core/services/pageToolMenu/clonePage/clonePageWrapperTemplate.html',
    "<clone-page selected-item-callbacks=\"selectedItemCallbacks\"></clone-page>"
  );


  $templateCache.put('web/smarteditcontainer/core/services/pageToolMenu/pageInformation/pageInformationTemplate.html',
    "<div id=\"page-info-template\">\n" +
    "    {{ \"template.page.information.title\" | translate }}\n" +
    "</div>"
  );


  $templateCache.put('web/smarteditcontainer/core/services/pageToolMenu/pageInformation/pageInformationWrapperTemplate.html',
    "<page-information selected-item-callbacks=\"selectedItemCallbacks\"></page-information>"
  );


  $templateCache.put('web/smarteditcontainer/core/services/pageToolMenu/pageToolMenuTemplate.html',
    "<div>\n" +
    "    <nav class=\"pageToolMenu\">\n" +
    "        <ul class=\"list-unstyled\">\n" +
    "            <li class=\"text-right yCloseToolbar\">\n" +
    "                <a id=\"page-tool-menu-close\" href=\"#\" class=\"nav-close\" data-ng-click=\"closePageToolMenu($event)\">\n" +
    "                    <span class=\"hyicon hyicon-close\"></span>\n" +
    "                </a>\n" +
    "            </li>\n" +
    "        </ul>\n" +
    "        <ul id=\"page-tool-menu-home\" class=\"list-unstyled page-tool-menu\">\n" +
    "            <li class=\"text-left\" data-ng-repeat=\"item in actions\" data-ng-switch=\"item.type\">\n" +
    "                <div data-ng-switch-when=\"TEMPLATE\" class=\"yTemplateToolbar\">\n" +
    "                    <div data-ng-include=\"item.include\" data-ng-if=\"item.include\"></div>\n" +
    "                </div>\n" +
    "\n" +
    "                <div data-ng-switch-when=\"ACTION\">\n" +
    "                    <a href data-ng-click=\"triggerAction(item, $event)\">\n" +
    "                        <span id=\"{{toolbarName}}_option_{{$index}}\" class=\"{{ item.className || 'yStatusDefault' }}\">\n" +
    "                            <img data-ng-src=\"{{imageRoot}}{{item.icons.default}} \" title=\"{{item.name | translate}}\" alt=\"{{item.name | translate}}\" />{{item.name | translate}}\n" +
    "                        </span>\n" +
    "                    </a>\n" +
    "                </div>\n" +
    "\n" +
    "                <div data-ng-switch-when=\"HYBRID_ACTION\" dropdown is-open=\"status.isopen \">\n" +
    "                    <a href data-ng-click=\"triggerAction(item, $event)\">\n" +
    "                        <span id=\"{{toolbarName}}_option_{{$index}}\" class=\"{{ item.className || 'yStatusDefault' }}\" dropdown-toggle>\n" +
    "                            <img data-ng-src=\"{{imageRoot}}{{item.icons.default}} \" title=\"{{item.name | translate}}\" alt=\"{{item.name | translate}}\" />{{item.name | translate}}\n" +
    "                        </span>\n" +
    "                    </a>\n" +
    "                </div>\n" +
    "\n" +
    "                <div data-ng-switch-default></div>\n" +
    "            </li>\n" +
    "        </ul>\n" +
    "        <ul class=\"list-unstyled page-tool-menu hideLevel2\" id=\"page-tool-menu-selected-item\">\n" +
    "            <li class=\"text-left\">\n" +
    "                <a id=\"page-tool-menu-back-anchor\" data-ng-click=\"showPageToolMenuHome()\">\n" +
    "                    <span id=\"page-tool-menu-back\" class=\" icon-back \" />{{backButtonI18nKey | translate}}</a>\n" +
    "            </li>\n" +
    "            <li>\n" +
    "                <div data-ng-include=\"selected.include\" data-ng-if=\"selected.include\"></div>\n" +
    "            </li>\n" +
    "        </ul>\n" +
    "    </nav>\n" +
    "</div>"
  );


  $templateCache.put('web/smarteditcontainer/core/services/pageToolMenu/pageToolMenuWrapperTemplate.html',
    "<page-tool-menu data-image-root=\"imageRoot\" data-toolbar-name=\"pageToolMenu\"></page-tool-menu>"
  );


  $templateCache.put('web/smarteditcontainer/core/services/pageToolMenu/syncStatus/syncStatusTemplate.html',
    "<a href>\n" +
    "    <span class=\"yStatusInfo\">\n" +
    "        <img data-ng-src=\"{{imageRoot}}static-resources/images/icon_small_sync.png\" />{{syncStatus | translate}}\n" +
    "    </span>\n" +
    "    <div class=\"yStatusSubtitle\">{{syncDate | date : 'dd/MM/yy - h:mm:ss a'}}</div>\n" +
    "</a>"
  );


  $templateCache.put('web/smarteditcontainer/core/services/pageToolMenu/syncStatus/syncStatusWrapperTemplate.html',
    "<sync-status></sync-status>"
  );


  $templateCache.put('web/smarteditcontainer/core/services/perspectiveSelectorWidget/perspectiveSelectorWidgetTemplate.html',
    "<div dropdown is-open=\"status.isopen\" auto-close=\"disabled\" class=\"ySEPerspectiveSelector\">\n" +
    "    <button type=\"button\" class=\"btn btn-default yHybridAction\" dropdown-toggle ng-disabled=\"disabled \" aria-pressed=\"false\">\n" +
    "        <span class=\"btn-text\">{{perspectiveData.activePerspective.name | translate}}</span>\n" +
    "        <span class=\"hyicon hyicon-arrow \"></span>\n" +
    "    </button>\n" +
    "    <ul class=\"dropdown-menu bottom btn-block ySEPerspectiveList\" role=\"menu\">\n" +
    "        <li ng-repeat=\"choice in getPerspectives()\" class=\"item ySEPerspectiveList--item\" data-ng-click=\"selectPerspective(choice.key)\">\n" +
    "            <a href data-ng-click=\"selectPerspective(choice.key);\">{{choice.name | translate}}</a>\n" +
    "        </li>\n" +
    "    </ul>\n" +
    "</div>"
  );


  $templateCache.put('web/smarteditcontainer/core/services/topToolbars/deviceSupportTemplate.html',
    "<inflection-point-selector data-ng-if=\"isOnStorefront()\"></inflection-point-selector>"
  );


  $templateCache.put('web/smarteditcontainer/core/services/topToolbars/experienceSelectorWrapperTemplate.html',
    "<experience-selector-button data-ng-if=\"isOnStorefront()\"></experience-selector-button>"
  );


  $templateCache.put('web/smarteditcontainer/core/services/topToolbars/leftToolbarTemplate.html',
    "<div>\n" +
    "    <nav class=\"leftNav\">\n" +
    "\n" +
    "        <ul class=\"list-unstyled leftNav--user\">\n" +
    "            <li class=\"leftNav--user__close\">\n" +
    "                <a href=\"#\" class=\"nav-close\" data-ng-click=\"closeLeftToolbar($event)\">\n" +
    "                    <span class=\"hyicon hyicon-close\"></span>\n" +
    "                </a>\n" +
    "            </li>\n" +
    "            <li class=\"leftNav--user__info\">\n" +
    "                <p>{{username | uppercase}}</p>\n" +
    "            </li>\n" +
    "            <li class=\"leftNav--user__info\">\n" +
    "                <a href=\"#\" id=\"\" data-ng-click=\"signOut($event)\">{{'left.toolbar.sign.out' | translate}}</a>\n" +
    "            </li>\n" +
    "\n" +
    "            <li class=\"leftNav--user__info\">\n" +
    "                <language-selector></language-selector>\n" +
    "            </li>\n" +
    "            <li class=\"divider leftNav--user__divider\"></li>\n" +
    "        </ul>\n" +
    "\n" +
    "        <ul class=\"list-unstyled leftNav--level1\" id=\"hamburger-menu-level1\">\n" +
    "            <li class=\"leftNav--listItem\">\n" +
    "                <a data-ng-click=\"showSites()\">{{'left.toolbar.sites' | translate}}</a>\n" +
    "            </li>\n" +
    "            <li class=\"leftNav--listItem\" has-operation-permission=\"configurationCenterReadPermissionKey\">\n" +
    "                <a id=\"configurationCenter\" data-ng-click=\"showCfgCenter($event)\">{{'left.toolbar.configuration.center' | translate}}\n" +
    "                    <span class=\"icon \"></span>\n" +
    "                </a>\n" +
    "            </li>\n" +
    "        </ul>\n" +
    "        <ul class=\"list-unstyled leftNav--level2 ySELeftHideLevel2\" id=\"hamburger-menu-level2\">\n" +
    "            <li class=\"leftNav--listItem\">\n" +
    "                <a data-ng-click=\"goBack()\">\n" +
    "                    <span class=\" icon-back \">{{'left.toolbar.back' | translate}}</span>\n" +
    "                </a>\n" +
    "            </li>\n" +
    "            <li class=\"leftNav--listItem\">\n" +
    "                <general-configuration/>\n" +
    "            </li>\n" +
    "        </ul>\n" +
    "    </nav>\n" +
    "\n" +
    "    <div class=\"navbar-header pull-left leftNavBtn\">\n" +
    "        <button type=\"button\" class=\"navbar-toggle collapsed \" data-toggle=\"collapse \" data-target=\"#bs-example-navbar-collapse-1 \" id=\"nav-expander\" data-ng-click=\"showToolbar($event) \">\n" +
    "            <span class=\"sr-only \">{{'left.toolbar.toggle.navigation' | translate}}</span>\n" +
    "            <span class=\"icon-bar \"></span>\n" +
    "            <span class=\"icon-bar \"></span>\n" +
    "            <span class=\"icon-bar \"></span>\n" +
    "        </button>\n" +
    "    </div>\n" +
    "\n" +
    "</div>"
  );


  $templateCache.put('web/smarteditcontainer/core/services/topToolbars/leftToolbarWrapperTemplate.html',
    "<left-toolbar data-image-root=\"imageRoot\"></left-toolbar>"
  );


  $templateCache.put('web/smarteditcontainer/core/services/topToolbars/logoTemplate.html',
    "<img data-ng-src=\"{{imageRoot}}static-resources/images/logo_smartEdit.png \" class=\"ySmartEditAppLogo \" alt=\"Smart Edit \" />"
  );


  $templateCache.put('web/smarteditcontainer/core/services/topToolbars/perspectiveSelectorWrapperTemplate.html',
    "<perspective-selector></perspective-selector>"
  );


  $templateCache.put('web/smarteditcontainer/core/services/topToolbars/toolbarActionTemplate.html',
    "<div>\n" +
    "    <div data-ng-if=\"item.type == 'ACTION'\" class=\"btn-group \">\n" +
    "        <button type=\"button\" dropdown-toggle=\"true\" data-toggle=\"button\" class=\"btn\" aria-pressed=\"false\" data-ng-click=\"triggerAction(item, $event)\">\n" +
    "            <span>{{item.name | translate}}</span>\n" +
    "            <img id=\"{{toolbarName}}_option_{{item.key}}\" data-ng-src=\"{{::imageRoot}}{{item.icons.default}} \" class=\"file\" title=\"{{item.name | translate}}\" alt=\"{{item.name | translate}}\" />\n" +
    "        </button>\n" +
    "    </div>\n" +
    "    <div data-ng-if=\"item.type == 'HYBRID_ACTION'\" class=\"btn-group ySEHybridAction\" dropdown is-open=\"status.isopen \">\n" +
    "        <button data-ng-show=\"item.icons.default\" type=\"button\" class=\"btn btn-default yHybridAction yDddlbResolution\" dropdown-toggle ng-disabled=\"disabled \" aria-pressed=\"false \" data-ng-click=\"triggerAction(item, $event)\">\n" +
    "            <span>{{item.name | translate}}</span>\n" +
    "            <img id=\"{{toolbarName}}_option_{{item.key}}\" data-ng-src=\"{{::imageRoot}}{{item.icons.default}}\" class=\"file\" title=\"{{item.name | translate}}\" alt=\"{{item.name | translate}}\" />\n" +
    "        </button>\n" +
    "\n" +
    "        <div data-ng-include=\"item.include\" data-ng-if=\"item.include\"></div>\n" +
    "\n" +
    "    </div>\n" +
    "</div>"
  );


  $templateCache.put('web/smarteditcontainer/core/toolbar/toolbarTemplate.html',
    "<div class='{{cssClass}} yToolbar'>\n" +
    "\n" +
    "    <div class=\"yToolbar__Left\">\n" +
    "        <div data-ng-repeat=\"item in actions | filter:{section:'left'}\" class=\"yTemplateToolbar {{::item.className}}\" data-ng-include=\"item.include && item.type =='TEMPLATE' ? item.include : 'web/smarteditcontainer/core/services/topToolbars/toolbarActionTemplate.html'\"></div>\n" +
    "    </div>\n" +
    "    <div class=\"yToolbar__Middle\">\n" +
    "        <div data-ng-repeat=\"item in actions | filter:{section:'middle'}\" class=\"yTemplateToolbar {{::item.className}}\" data-ng-include=\"item.include && item.type =='TEMPLATE' ? item.include : 'web/smarteditcontainer/core/services/topToolbars/toolbarActionTemplate.html'\"></div>\n" +
    "    </div>\n" +
    "    <div class=\"yToolbar__Right\">\n" +
    "        <div data-ng-repeat=\"item in actions | filter:{section:'right'}\" class=\"yTemplateToolbar {{::item.className}}\" data-ng-include=\"item.include && item.type =='TEMPLATE' ? item.include : 'web/smarteditcontainer/core/services/topToolbars/toolbarActionTemplate.html'\"></div>\n" +
    "    </div>\n" +
    "</div>"
  );


  $templateCache.put('web/smarteditcontainer/fragments/landingPage.html',
    "<div class=\"ySmartEditToolbars\" style=\"position:absolute\">\n" +
    "    <div>\n" +
    "        <toolbar data-css-class=\"ySmartEditTitleToolbar\" data-image-root=\"imageRoot\" data-toolbar-name=\"smartEditTitleToolbar\"></toolbar>\n" +
    "    </div>\n" +
    "</div>\n" +
    "<div class=\"landingPageWrapper\">\n" +
    "    <div class=\"ySELandingTitle\">\n" +
    "        <h1 class=\"landing-page-title\" data-translate='landingpage.title'></h1>\n" +
    "        <h4 class=\"landing-page-label\" data-translate='landingpage.label'></h4>\n" +
    "    </div>\n" +
    "    <div class=\"container-fluid ySELandingPage\">\n" +
    "        <div class=\"row\">\n" +
    "            <div class=\"ySECatalogEntry\" data-ng-repeat=\"catalog in landingCtl.catalogs | startFrom:(landingCtl.currentPage-1)*landingCtl.CATALOGS_PER_PAGE | limitTo:landingCtl.CATALOGS_PER_PAGE\">\n" +
    "                <catalog-details catalog=\"catalog\"></catalog-details>\n" +
    "            </div>\n" +
    "        </div>\n" +
    "        <div class=\"pagination-container\">\n" +
    "            <pagination boundary-links=\"true\" total-items=\"landingCtl.totalItems\" items-per-page=\"landingCtl.CATALOGS_PER_PAGE\" ng-model=\"landingCtl.currentPage\" class=\"pagination-sm\" previous-text=\"&lsaquo;\" next-text=\"&rsaquo;\" first-text=\"&laquo;\" last-text=\"&raquo;\"></pagination>\n" +
    "        </div>\n" +
    "    </div>\n" +
    "</div>"
  );


  $templateCache.put('web/smarteditcontainer/fragments/mainview.html',
    "<div class=\"ySmartEditToolbars\" style=\"position:absolute\">\n" +
    "    <div>\n" +
    "        <toolbar data-css-class=\"ySmartEditTitleToolbar\" data-image-root=\"imageRoot\" data-toolbar-name=\"smartEditTitleToolbar\"></toolbar>\n" +
    "    </div>\n" +
    "    <div>\n" +
    "        <toolbar data-css-class=\"ySmartEditExperienceSelectorToolbar\" data-image-root=\"imageRoot\" data-toolbar-name=\"experienceSelectorToolbar\"></toolbar>\n" +
    "    </div>\n" +
    "</div>\n" +
    "<div ng-class=\"{'alert-overlay': true, 'ySEEmptyMessage': (!alerts || alerts.length == 0 ) }\">\n" +
    "    <alerts-box alerts=\"alerts\" />\n" +
    "</div>\n" +
    "<div id=\"js_iFrameWrapper\" class=\"iframeWrapper\">\n" +
    "    <iframe src=\"\" hy-dropabpe-iframe></iframe>\n" +
    "</div>"
  );


  $templateCache.put('web/smarteditcontainer/modules/administrationModule/editConfigurationsTemplate.html',
    "<div id=\"editConfigurationsBody\" class=\"ySEConfigBody\">\n" +
    "    <form name=\"form.configurationForm\" novalidate data-ng-submit=\"editor.submit(form.configurationForm)\">\n" +
    "        <div class=\"row ySECfgTableHeader\">\n" +
    "            <div class=\"col-xs-3\">\n" +
    "                <label data-translate=\"configurationform.header.key.name\"></label>\n" +
    "            </div>\n" +
    "            <div class=\"col-xs-9\">\n" +
    "                <label data-translate=\"configurationform.header.value.name\"></label>\n" +
    "            </div>\n" +
    "        </div>\n" +
    "        <div class=\"row ySECfgAddEntity\">\n" +
    "            <button class=\"y-add-btn\" type=\"button\" data-ng-click=\"editor.addEntry(); \">\n" +
    "                <span class=\"hyicon hyicon-add \"></span>\n" +
    "                {{'general.configuration.add.button' | translate}}\n" +
    "            </button>\n" +
    "        </div>\n" +
    "        <div class=\"row ySECfgEntity \" data-ng-repeat=\"entry in editor.filterConfiguration() \" data-ng-mouseenter=\"mouseenter() \" data-ng-mouseout=\"mouseout() \">\n" +
    "            <div class=\"col-xs-3 \">\n" +
    "                <input type=\"text \" class=\"ng-class:{ 'col-xs-12':true, 'has-error':entry.errors.keys.length>0}\" name=\"{{entry.key}}_key\" data-ng-model=\"entry.key\" data-ng-required=\"true\" data-ng-disabled=\"!entry.isNew\" />\n" +
    "                <span id=\"{{entry.key}}_error_{{$index}}\" data-ng-if=\"entry.errors.keys\" data-ng-repeat=\"error in entry.errors.keys\" class=\"error-input help-block\">\n" +
    "                    {{error.message|translate}}\n" +
    "                </span>\n" +
    "            </div>\n" +
    "            <div class=\"col-xs-8\">\n" +
    "                <textarea class=\"ng-class:{'col-xs-12':true, 'has-error':entry.errors.values.length>0}\" name=\"{{entry.key}}_value\" data-ng-model=\"entry.value\" data-ng-required=\"true\" data-ng-change=\"editor._validateUserInput(entry)\"></textarea>\n" +
    "                <div data-ng-if=\"entry.requiresUserCheck\">\n" +
    "                    <input id=\"{{entry.key}}_absoluteUrl_check_{{$index}}\" type=\"checkbox\" data-ng-model=\"entry.isCheckedByUser\" />\n" +
    "                    <span id=\"{{entry.key}}_absoluteUrl_msg_{{$index}}\" class=\"ng-class:{'warning-check-msg':true, 'not-checked':entry.hasErrors && !entry.isCheckedByUser}\">{{'configurationform.absoluteurl.check' | translate}}</span>\n" +
    "                </div>\n" +
    "\n" +
    "                <span id=\"{{entry.key}}_error_{{$index}}\" data-ng-if=\"entry.errors.values\" data-ng-repeat=\"error in entry.errors.values\" class=\"error-input help-block\">\n" +
    "                    {{error.message|translate}}\n" +
    "                </span>\n" +
    "            </div>\n" +
    "            <div class=\"col-xs-1\">\n" +
    "                <button type=\"button\" id=\"{{entry.key}}_removeButton_{{$index}}\" class=\"btn btn-subordinate\" data-ng-click=\"editor.removeEntry(entry, form.configurationForm);\">\n" +
    "                    <span class=\"hyicon hyicon-remove\" aria-hidden=\"true\"></span>\n" +
    "                </button>\n" +
    "            </div>\n" +
    "        </div>\n" +
    "    </form>\n" +
    "</div>"
  );


  $templateCache.put('web/smarteditcontainer/modules/administrationModule/generalConfigurationTemplate.html',
    "<a id=\"generalConfiguration\" data-ng-click=\"editConfiguration()\">{{'general.configuration.title' | translate}}</a>"
  );


  $templateCache.put('web/smarteditcontainer/services/widgets/catalogDetails/catalogDetailsTemplate.html',
    "<div class=\"col-xs-12 col-sm-6 ySECatalog\">\n" +
    "    <div class=\"catalog-container\">\n" +
    "        <div class=\"catalog-body\">\n" +
    "            <div class=\"catalog-header row\">\n" +
    "                <h4>{{catalog.name | l10n}}</h4>\n" +
    "            </div>\n" +
    "\n" +
    "            <div class=\"row\">\n" +
    "                <div data-ng-repeat=\"catalogVersion in catalog.catalogVersions\" class=\"col-xs-6 catalog-version-item\">\n" +
    "                    <div class=\"catalog-version-container\" data-ng-click=\"onCatalogSelect(catalogVersion)\">\n" +
    "                        <div class=\"catalog-thumbnail-default-img\">\n" +
    "                            <div class=\"catalog-thumbnail\" style=\"background-image: url('{{catalogVersion.thumbnailUrl}}');\"></div>\n" +
    "                        </div>\n" +
    "                        <div class=\"catalog-version-info\">{{catalogVersion.catalogVersion}}</div>\n" +
    "                    </div>\n" +
    "                </div>\n" +
    "            </div>\n" +
    "\n" +
    "            <div class=\"catalog-footer row\">\n" +
    "                <div data-ng-repeat=\"template in templates\" ng-include=\"template.include\" />\n" +
    "            </div>\n" +
    "        </div>\n" +
    "    </div>\n" +
    "</div>"
  );


  $templateCache.put('web/smarteditcontainer/services/widgets/clientPagedList/clientPagedList.html',
    "<div class=\"fluid-container ySEPageListResult\">\n" +
    "\n" +
    "    <p class=\"paged-list-count\" ng-if=\"displayCount\">\n" +
    "        <span>({{ filteredItems.length }} {{'pagelist.countsearchresult' | translate}})</span>\n" +
    "    </p>\n" +
    "\n" +
    "    <table class=\"paged-list-table table table-striped table-hover techne-table\">\n" +
    "        <thead>\n" +
    "            <tr>\n" +
    "                <th data-ng-repeat=\"key in keys\" data-ng-click=\"orderByColumn(key.property)\" data-ng-style=\"{ 'width': columnWidth + '%' }\" class=\"paged-list-header\" data-ng-class=\"'paged-list-header-'+key.property\">\n" +
    "                    {{ key.i18n | translate }}\n" +
    "                    <span class=\"header-icon\" ng-show=\"visibleSortingHeader === key.property\" ng-class=\"{ 'down': headersSortingState[key.property] == true, 'up': headersSortingState[key.property] == false }\"></span>\n" +
    "                </th>\n" +
    "            </tr>\n" +
    "        </thead>\n" +
    "        <tbody>\n" +
    "            <tr data-ng-repeat=\" item in filteredItems | startFrom:(currentPage-1)*itemsPerPage | limitTo:itemsPerPage \" class=\"techne-table-xs-right techne-table-xs-left paged-list-item \">\n" +
    "                <td ng-repeat=\"key in keys\" ng-class=\"'paged-list-item-'+key.property\">\n" +
    "                    <div data-ng-if=\"renderers[key.property]\" compile-html=\"renderers[key.property](item, key)\"></div>\n" +
    "                    <span data-ng-if=\"!renderers[key.property]\">{{ item[key.property] }}</span>\n" +
    "                </td>\n" +
    "            </tr>\n" +
    "        </tbody>\n" +
    "    </table>\n" +
    "\n" +
    "    <div class=\"pagination-container \">\n" +
    "        <pagination boundary-links=\"true\" total-items=\"totalItems\" items-per-page=\"itemsPerPage\" ng-model=\"currentPage\" class=\"pagination-sm\" previous-text=\"&lsaquo;\" next-text=\"&rsaquo;\" first-text=\"&laquo;\" last-text=\"&raquo;\"></pagination>\n" +
    "    </div>\n" +
    "\n" +
    "</div>"
  );


  $templateCache.put('web/smartedit/core/decoratorFilter/managePerspectivesTemplate.html',
    "<div class=\"modal-header\">\n" +
    "    <h3 class=\"modal-title\">{{ 'modal.perpectives.title' | translate }}</h3>\n" +
    "</div>\n" +
    "<div class=\"modal-body\">\n" +
    "    <table>\n" +
    "        <tr>\n" +
    "            <td rowspan=\"2\">\n" +
    "                {{'modal.perpectives.header.perspectives.name' | translate}}\n" +
    "                <div>\n" +
    "                    <select size=\"12\" data-ng-change=\"perspectiveChanged()\" class=\"form-control\" data-ng-options=\"p.name | translate for p in perspectives\" data-ng-model=\"perspective\"></select>\n" +
    "                </div>\n" +
    "                <button type=\"button\" class=\"btn btn-default btn-xs\" data-ng-click=\"createPerspective()\">\n" +
    "                    <span class=\"glyphicon glyphicon-plus\" aria-hidden=\"true\"></span>\n" +
    "                </button>\n" +
    "                <button type=\"button\" class=\"btn btn-danger btn-xs\" data-ng-if=\"perspective.system !== true\" data-ng-click=\"deletePerspective()\">\n" +
    "                    <span class=\"glyphicon glyphicon-remove\" aria-hidden=\"true\"></span>\n" +
    "                </button>\n" +
    "            </td>\n" +
    "            <td width=\"15px\">\n" +
    "            </td>\n" +
    "            <td valign=\"top\">\n" +
    "                {{'modal.perpectives.header.name.name' | translate}}:\n" +
    "                <span data-ng-if=\"perspective.system===true\">\n" +
    "                    <span style=\"padding-left: 5px\">{{ perspective.name | translate }}</span>\n" +
    "                    <span class='pull-right' style=\"color: #feffc1; padding-left: 10px\">{{ warningForSystem | translate }}</span>\n" +
    "                </span>\n" +
    "                <input style='padding-left: 5px; color: black' data-ng-if=\"perspective.system!==true\" type=\"text\" data-ng-required=\"true\" data-ng-trim=\"true\" data-ng-change=\"nameChanged()\" data-ng-disabled=\"perspective.system === true\" data-ng-model=\"perspective.name\">\n" +
    "                <span style=\"color: #ff0e18\">{{ renameError | translate }}</span>\n" +
    "            </td>\n" +
    "        </tr>\n" +
    "        <tr>\n" +
    "            <td width=\"25px\">\n" +
    "            </td>\n" +
    "            <td>\n" +
    "                {{'modal.perpectives.header.decorators.name' | translate}}\n" +
    "                <div data-ng-repeat=\"ps in decoratorSet\">\n" +
    "                    <input type=\"checkbox\" data-ng-model=\"ps.checked\" data-ng-change=\"save()\" data-ng-disabled=\"perspective.system === true\"> {{ ps.name }}\n" +
    "                </div>\n" +
    "            </td>\n" +
    "        </tr>\n" +
    "    </table>\n" +
    "</div>\n" +
    "<div class=\"modal-footer\">\n" +
    "    <button class=\"btn btn-default\" data-ng-click=\"close()\" data-translate=\"perspectives.actions.close\"></button>\n" +
    "</div>"
  );


  $templateCache.put('web/smartedit/core/decoratorFilter/perspectiveTemplate.html',
    "<div>\n" +
    "    <span class=\"floating-perspective\" data-ng-mouseleave=\"mouseOff()\" data-ng-mouseenter=\"mouseOn()\">\n" +
    "        <div>\n" +
    "            <span class=\"xposed-perspectives\">\n" +
    "                <span class=\"glyphicon glyphicon-th\" aria-hidden=\"true\"></span>\n" +
    "            </span>\n" +
    "        </div>\n" +
    "        <div class=\"floating-bg\" data-ng-show=\"!showButtonOnly()\">\n" +
    "            <div>\n" +
    "                <span data-translate=\"modal.perpectives.header.perspectives.name\"></span>\n" +
    "            </div>\n" +
    "            <div class=\"form-group form-inline\">\n" +
    "                <select data-ng-change=\"perspectiveSelected(perspective)\" class=\"form-control\" data-ng-options=\"p.name | translate for p in perspectives\" ng-model=\"perspective\" />\n" +
    "                <button type=\"button\" data-ng-click=\"manage()\" class=\"btn btn-default\" aria-hidden=\"true\">\n" +
    "                    <span class=\"glyphicon glyphicon-edit\" aria-hidden=\"true\"></span>\n" +
    "                </button>\n" +
    "            </div>\n" +
    "        </div>\n" +
    "    </span>\n" +
    "</div>\n" +
    "<div data-ng-transclude></div>"
  );


  $templateCache.put('web/smartedit/modules/systemModule/features/contextualMenu/contextualMenuDecoratorTemplate.html',
    "<div class=\"cmsx-ctx-wrapper1\">\n" +
    "    <div class=\"cmsx-ctx-wrapper2\">\n" +
    "        <div class=\"decorative-top-border decorative-border\" data-ng-if=\"ctrl.active\"></div>\n" +
    "        <div class=\"decorative-right-border decorative-border\" data-ng-if=\"ctrl.active\"></div>\n" +
    "        <div class=\"decorative-bottom-border decorative-border\" data-ng-if=\"ctrl.active\"></div>\n" +
    "        <div class=\"decorative-left-border decorative-border\" data-ng-if=\"ctrl.active\"></div>\n" +
    "        <div class=\"contextualMenuOverlay\" data-ng-show=\"ctrl.showOverlay() || ctrl.status.isopen\">\n" +
    "            <div data-ng-repeat=\"item in ctrl.getItems().leftMenuItems\" class=\"btn btn-primary cmsx-ctx-btns\" data-ng-init=\"itemsrc = item.iconIdle\" data-ng-mouseout=\"itemsrc = item.iconIdle\" data-ng-mouseover=\"itemsrc = item.iconNonIdle\">\n" +
    "                <span id=\"{{item.i18nKey | translate}}-{{ctrl.smarteditComponentId}}-{{ctrl.smarteditComponentType}}-{{$index}}\" data-ng-if=\"item.iconIdle && ctrl.isHybrisIcon(item.displayClass)\" data-ng-click=\"ctrl.triggerItemCallback(item, $event)\" data-contextual-menu-item=\"\" data-item=\"item\" data-component-id=\"{{ctrl.smarteditComponentId}}\" data-component-type=\"{{ctrl.smarteditComponentType}}\" data-container-id=\"{{ctrl.smarteditContainerId}}\" data-container-type=\"{{ctrl.smarteditContainerType}}\" class=\"ng-class:{clickable:true}\">\n" +
    "                    <img data-ng-src=\"{{itemsrc}}\" id=\"{{item.i18nKey | translate}}-{{ctrl.smarteditComponentId}}-{{ctrl.smarteditComponentType}}-{{$index}}\" title=\"{{item.i18nKey | translate}}\" />\n" +
    "                </span>\n" +
    "                <img id=\"{{item.i18nKey | translate}}-{{ctrl.smarteditComponentId}}-{{ctrl.smarteditComponentType}}-{{$index}}\" title=\"{{item.i18nKey | translate}}\" data-ng-if=\"item.iconIdle && !ctrl.isHybrisIcon(item.displayClass)\" class=\"{{item.displayClass}}\" data-ng-class=\"{clickable:true}\" data-ng-click=\"ctrl.triggerItemCallback(item, $event)\" data-ng-src=\"{{itemsrc}}\" data-contextual-menu-item=\"\" data-item=\"item\" data-component-id=\"{{ctrl.smarteditComponentId}}\" data-component-type=\"{{ctrl.smarteditComponentType}}\" data-container-id=\"{{ctrl.smarteditContainerId}}\" data-container-type=\"{{ctrl.smarteditContainerType}}\" />\n" +
    "            </div>\n" +
    "            <div data-ng-if=\"ctrl.getItems().moreMenuItems.length > 0\" class=\"cmsx-ctx-more\">\n" +
    "\n" +
    "                <div class=\"btn-group yCmsCtxMenu\" dropdown is-open=\"ctrl.status.isopen\" dropdown-append-to-body data-ng-init=\"itemsrc = ctrl.moreButton.iconIdle\" data-ng-mouseout=\"itemsrc = ctrl.moreButton.iconIdle\" data-ng-mouseover=\"itemsrc = ctrl.moreButton.iconNonIdle\">\n" +
    "\n" +
    "                    <button type=\"button\" class=\"cmsx-ctx-more-btn pull-right\" dropdown-toggle data-ng-click=\"ctrl.toggleDropdown($event)\">\n" +
    "                        <img title=\"{{ctrl.moreButton.i18nKey | translate}}\" data-ng-src=\"{{ctrl.status.isopen? ctrl.moreButton.iconNonIdle : itemsrc}}\" data-ng-class=\"{noboxshadow:ctrl.status.isopen}\" />\n" +
    "                    </button>\n" +
    "                    <ul class=\"dropdown-menu\" id=\"{{::ctrl.smarteditComponentId}}-{{::ctrl.smarteditComponentType}}-more-menu\" role=\"menu\">\n" +
    "                        <li data-ng-repeat=\"item in ctrl.getItems().moreMenuItems\">\n" +
    "                            <a data-ng-click=\"ctrl.triggerItemCallback(item, $event)\" data-item=\"item\" data-smartedit-id=\"{{ctrl.smarteditComponentId}}\" data-smartedit-type=\"{{ctrl.smarteditComponentType}}\">\n" +
    "                                <span data-ng-if=\"item.iconIdle && ctrl.isHybrisIcon(item.displayClass)\" data-ng-click=\"ctrl.triggerItemCallback(item, $event)\" data-contextual-menu-item=\"\" data-item=\"item\" data-component-id=\"{{ctrl.smarteditComponentId}}\" data-component-type=\"{{ctrl.smarteditComponentType}}\" data-container-id=\"{{ctrl.smarteditContainerId}}\" data-container-type=\"{{ctrl.smarteditContainerType}}\" class=\"ng-class:{clickable:true}\">\n" +
    "                                    <img class=\"cmsx-ctx-menu-more-icon\" data-ng-src=\"{{itemsrc}}\" id=\"{{item.i18nKey | translate}}-{{ctrl.smarteditComponentId}}-{{ctrl.smarteditComponentType}}-{{$index}}\" title=\"{{item.i18nKey | translate}}\" />\n" +
    "\n" +
    "                                </span>\n" +
    "                                <!--<span class=\"icontext {{item.displayClass}}\" data-ng-if=\"item.iconIdle && !ctrl.isHybrisIcon(item.displayClass)\" />-->\n" +
    "                                <img id=\"{{item.i18nKey | translate}}-{{ctrl.smarteditComponentId}}-{{ctrl.smarteditComponentType}}-{{$index}}\" title=\"{{item.i18nKey | translate}}\" class=\"cmsx-ctx-menu-more-icon\" data-ng-if=\"item.smallIcon && !ctrl.isHybrisIcon(item.smallIcon)\" data-ng-src=\"{{item.smallIcon}}\" />\n" +
    "                                <span class=\"labeltext\">{{item.i18nKey | translate}}</span>\n" +
    "                            </a>\n" +
    "                        </li>\n" +
    "                    </ul>\n" +
    "                </div>\n" +
    "            </div>\n" +
    "            <div class=\"contextualmenuBackground\"></div>\n" +
    "        </div>\n" +
    "        <div class=\"yWrapperData\">\n" +
    "            <div data-ng-transclude></div>\n" +
    "        </div>\n" +
    "    </div>\n" +
    "</div>"
  );


  $templateCache.put('web/smartedit/modules/systemModule/features/slotContextualMenu/basicSlotContextualMenuDecoratorTemplate.html',
    "<div class=\"cmsx-ctx-wrapper1 se-slot-contextual-menu-level1\" data-ng-if=\"ctrl.isSlotNotEmpty\">\n" +
    "    <div class=\"cmsx-ctx-wrapper2 se-slot-contextual-menu-level2\">\n" +
    "        <div class=\"decorative-panel-basic-slot-contextual-menu\" data-ng-if=\"ctrl.showOverlay() || ctrl.status.isopen\">\n" +
    "            <div data-ng-repeat=\"item in ctrl.getItems().leftMenuItems\" class=\"btn btn-primary cmsx-ctx-btns pull-right\" data-ng-init=\"itemsrc = item.iconIdle\" data-ng-mouseout=\"itemsrc = item.iconIdle\" data-ng-mouseover=\"itemsrc = item.iconNonIdle\">\n" +
    "                <div data-ng-if=\"!item.templateUrl\">\n" +
    "                    <span id=\"{{::item.i18nKey | translate}}-{{::ctrl.smarteditComponentId}}-{{::ctrl.smarteditComponentType}}-hyicon\" data-ng-if=\"item.iconIdle && ctrl.isHybrisIcon(item.displayClass)\" data-ng-click=\"ctrl.triggerItemCallback(item, $event)\" class=\"ng-class:{clickable:true}\">\n" +
    "                        <img data-ng-src=\"{{itemsrc}}\" id=\"{{::item.i18nKey | translate}}-{{::ctrl.smarteditComponentId}}-{{::ctrl.smarteditComponentType}}-hyicon-img\" title=\"{{::item.i18nKey | translate}}\" />\n" +
    "                    </span>\n" +
    "                    <img id=\"{{::item.i18nKey | translate}}-{{::ctrl.smarteditComponentId}}-{{::ctrl.smarteditComponentType}}\" title=\"{{::item.i18nKey | translate}}\" data-ng-if=\"item.iconIdle && !ctrl.isHybrisIcon(item.displayClass)\" class=\"{{item.displayClass}}\" data-ng-class=\"{clickable:true}\" data-ng-click=\"ctrl.triggerItemCallback(item, $event)\" data-ng-src=\"{{itemsrc}}\" alt=\"{{item.i18nKey}}\" />\n" +
    "                </div>\n" +
    "                <div data-ng-if=\"item.templateUrl\">\n" +
    "                    <div data-ng-include=\"item.templateUrl\"></div>\n" +
    "                </div>\n" +
    "            </div>\n" +
    "\n" +
    "            <div data-ng-if=\"ctrl.getItems().moreMenuItems.length > 0\" dropdown dropdown-append-to-body is-open=\"ctrl.status.isopen\" class=\"btn btn-primary pull-right cmsx-ctx-btns dropdown\" data-ng-init=\"itemsrc = ctrl.moreButton.iconIdle\" data-ng-mouseout=\"itemsrc = ctrl.moreButton.iconIdle\" data-ng-mouseover=\"itemsrc = ctrl.moreButton.iconNonIdle\">\n" +
    "                <img id=\"{{ctrl.moreButton.i18nKey | translate}}-{{::ctrl.smarteditComponentId}}-{{::ctrl.smarteditComponentType}}\" title=\"{{ctrl.moreButton.i18nKey | translate}}\" data-ng-if=\"ctrl.moreButton.iconIdle && !ctrl.isHybrisIcon(ctrl.moreButton.displayClass)\" class=\"{{ctrl.moreButton.displayClass}}\" data-ng-class=\"{clickable:true}\" data-ng-click=\"ctrl.triggerItemCallback(ctrl.moreButton, $event)\" data-ng-src=\"{{itemsrc}}\" />\n" +
    "                <ul class=\"dropdown-menu\" role=\"menu\">\n" +
    "                    <li role=\"menu\" data-ng-repeat=\"item in ctrl.getItems().moreMenuItems\" id=\"{{::item.i18nKey | translate}}-{{::ctrl.smarteditComponentId}}-{{::ctrl.smarteditComponentType}}-more\">\n" +
    "                        <div data-ng-if=\"!item.templateUrl\">\n" +
    "                            <a data-ng-click=\"ctrl.triggerItemCallback(item, $event)\">\n" +
    "                                <span data-ng-if=\"item.displayClass\" class=\"icontext {{item.displayClass}}\"></span>\n" +
    "                                <img title=\"{{::item.i18nKey | translate}}\" class=\"cmsx-ctx-menu-more-icon\" data-ng-if=\"item.smallIcon && !ctrl.isHybrisIcon(item.smallIcon)\" data-ng-src=\"{{item.smallIcon}}\" alt=\"{{item.i18nKey}}\" />\n" +
    "                                <span class=\"labeltext\">{{::item.i18nKey | translate}}</span>\n" +
    "                            </a>\n" +
    "                        </div>\n" +
    "                        <div data-ng-if=\"item.templateUrl\">\n" +
    "                            <div data-ng-include=\"item.templateUrl\"></div>\n" +
    "                        </div>\n" +
    "                    </li>\n" +
    "                </ul>\n" +
    "            </div>\n" +
    "        </div>\n" +
    "        <div class=\"decorator-padding-container\">\n" +
    "            <div class=\"decorator-basic-slot-border\" data-ng-if=\"ctrl.active\"></div>\n" +
    "            <div data-ng-transclude class=\"yWrapperData\"></div>\n" +
    "        </div>\n" +
    "    </div>\n" +
    "</div>"
  );


  $templateCache.put('web/smartedit/modules/systemModule/features/slotContextualMenu/slotContextualMenuDecoratorTemplate.html',
    "<div>\n" +
    "    <div class=\"cmsx-ctx-wrapper1 se-slot-contextual-menu-level1\">\n" +
    "        <div class=\"cmsx-ctx-wrapper2 se-slot-contextual-menu-level2\">\n" +
    "            <div class=\"decorative-panel-area\" data-ng-if=\"ctrl.showOverlay() || ctrl.status.isopen\">\n" +
    "                <p class=\"decorative-panel-text\">{{::ctrl.smarteditComponentId}}</p>\n" +
    "                <div class=\"decorator-panel-padding-center\"></div>\n" +
    "                <div class=\"decorative-panel-slot-contextual-menu\" data-ng-if=\"ctrl.showOverlay() || ctrl.status.isopen\">\n" +
    "                    <div data-ng-repeat=\"item in ctrl.getItems().leftMenuItems\" class=\"btn btn-primary cmsx-ctx-btns pull-right\" data-ng-init=\"itemsrc = item.iconIdle\" data-ng-mouseout=\"itemsrc = item.iconIdle\" data-ng-mouseover=\"itemsrc = item.iconNonIdle\">\n" +
    "                        <div data-ng-if=\"!item.templateUrl\">\n" +
    "                            <span id=\"{{::item.i18nKey | translate}}-{{::ctrl.smarteditComponentId}}-{{::ctrl.smarteditComponentType}}-hyicon\" data-ng-if=\"item.iconIdle && ctrl.isHybrisIcon(item.displayClass)\" data-ng-click=\"ctrl.triggerItemCallback(item, $event)\" class=\"ng-class:{clickable:true}\">\n" +
    "                                <img data-ng-src=\"{{itemsrc}}\" id=\"{{::item.i18nKey | translate}}-{{::ctrl.smarteditComponentId}}-{{::ctrl.smarteditComponentType}}-hyicon-img\" title=\"{{::item.i18nKey | translate}}\" />\n" +
    "                            </span>\n" +
    "                            <img id=\"{{::item.i18nKey | translate}}-{{::ctrl.smarteditComponentId}}-{{::ctrl.smarteditComponentType}}\" title=\"{{::item.i18nKey | translate}}\" data-ng-if=\"item.iconIdle && !ctrl.isHybrisIcon(item.displayClass)\" class=\"{{item.displayClass}}\" data-ng-class=\"{clickable:true}\" data-ng-click=\"ctrl.triggerItemCallback(item, $event)\" data-ng-src=\"{{itemsrc}}\" alt=\"{{item.i18nKey}}\" />\n" +
    "                        </div>\n" +
    "                        <div data-ng-if=\"item.templateUrl\">\n" +
    "                            <div data-ng-include=\"item.templateUrl\"></div>\n" +
    "                        </div>\n" +
    "                        <div class=\"slot-context-menu-divider\"></div>\n" +
    "                    </div>\n" +
    "                </div>\n" +
    "            </div>\n" +
    "\n" +
    "            <div class=\"decorator-padding-container\">\n" +
    "                <div class=\"decorator-padding-left\" data-ng-class=\"{active: ctrl.active}\"></div>\n" +
    "                <div class=\"decorator-slot-border\" data-ng-class=\"{active: ctrl.active}\"></div>\n" +
    "                <div class=\"yWrapperData\" data-ng-transclude data-ng-class=\"{active: ctrl.active}\"></div>\n" +
    "                <div class=\"decorator-padding-right\" data-ng-class=\"{active: ctrl.active}\"></div>\n" +
    "            </div>\n" +
    "        </div>\n" +
    "    </div>\n" +
    "</div>"
  );

}]);
