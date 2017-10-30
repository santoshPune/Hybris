/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
var JQUERY_UI_PATTERNS = {
    'DRAGGABLE_CONNECT_TO_SORTABLE_DRAG': /drag\: function\( event\, ui\, draggable \) {[\s\S]*this\.refreshPositions\(\)\;[\s]*\}\)\;[\s]*\}[\s]*\}[\s]*\}\)\;[\s]*\}[\s]*\}\)\;/g,
    'DRAGGABLE_CONNECT_TO_SORTABLE_START': /start\: function\( event, ui, draggable \) \{[\s\S]*sortable\._trigger\(\"activate\"\, event\, uiSortable\)\;[\s]*\}[\s]*\}\)\;[\s]*\}\,/g,
    'UI_DRAGGABLE_CLEAR': /\_clear\: function\(\) \{[\s\S]*this\.destroy\(\)\;[\s]*\}[\s]*\}\,/g,
    'UI_DRAGGABLE_CREATE_HELPER': /\_createHelper\: function\(event\)[\s\S]*helper\.css\(\"position\"\, \"absolute\"\)\;[\s]*\}[\s]*return helper\;[\s]*\}\,/g,
    'UI_DRAGGABLE_GENERATE_POSITION': /\_generatePosition\: function\( event, constrainPosition \) \{[\s\S]*this\.offset\.scroll\.left \) \)[\s]*\)[\s]*\}\;[\s]*\}\,/g,
    'UI_SORTABLE_CLEAR': /\_clear\: function\(event, noPropagation\) \{[\s\S]*return \!this\.cancelHelperRemoval\;[\s]*\}\,/g,
    'UI_SORTABLE_GENERATE_POSITION': /\_generatePosition\: function\(event\) \{[\s\S]*scroll\.scrollLeft\(\) \)\)[\s]*\)[\s]*\}\;[\s]*\}\,/g,
    'UI_SORTABLE_MOUSE_START': /\_mouseStart: function\(event\, overrideHandle\, noActivation\) \{[\s\S]*this\.\_mouseDrag\(event\)\; \/\/Execute the drag once - this causes the helper not to be visible before getting its correct position[\s]*return true\;[\s]*\}\,/g
};

var JQUERY_UI_DIFFS_PATH = "web/utils/jquery-ui-smartedit/diffs/";

var JQUERY_UI_DIFFS = {
    'DRAGGABLE_CONNECT_TO_SORTABLE_DRAG': 'draggable-connectToSortable-drag.txt',
    'DRAGGABLE_CONNECT_TO_SORTABLE_START': 'draggable-connectToSortable-start.txt',
    'UI_DRAGGABLE_CLEAR': 'ui-draggable-clear.txt',
    'UI_DRAGGABLE_CREATE_HELPER': 'ui-draggable-createHelper.txt',
    'UI_DRAGGABLE_GENERATE_POSITION': 'ui-draggable-generatePosition.txt',
    'UI_SORTABLE_CLEAR': 'ui-sortable-clear.txt',
    'UI_SORTABLE_GENERATE_POSITION': 'ui-sortable-generatePosition.txt',
    'UI_SORTABLE_MOUSE_START': 'ui-sortable-mouseStart.txt'
};

module.exports.JQUERY_UI_PATTERNS = JQUERY_UI_PATTERNS;
module.exports.JQUERY_UI_DIFFS_PATH = JQUERY_UI_DIFFS_PATH;
module.exports.JQUERY_UI_DIFFS = JQUERY_UI_DIFFS;
