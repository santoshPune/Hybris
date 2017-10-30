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
(function() {
    /**
     * @ngdoc overview
     * @name dragAndDropScrollingModule
     * @description
     * # The dragAndDropScrollingModule
     * This module provides a service that allows controlling how the page scrolls while dragging a SmartEdit component.
     */
    angular.module('dragAndDropScrollingModule', [])
        /**
         * @ngdoc service
         * @name dragAndDropScrollingModule.service:dragAndDropScrollingService
         *
         * @description
         * The Drag and Drop Scrolling service manages the scrolling of a page while dragging a SmartEdit component. It creates,
         * appends, shows and hides UI hints (regions in the page that scroll the page up or down when hovered).
         *
         * This service is only intended to be used by the base
         * {@link dragAndDropServiceInterfaceModule.DragAndDropServiceInterface SmartEdit Drag and Drop service }.
         */
        .factory('dragAndDropScrollingService', ['$filter', 'hitch', function($filter, hitch) {

            var SM_UI = $.ui.smartedit;
            var UI_HINTS = {};
            var SCROLL_DIRECTION = {
                UP: 1,
                DOWN: 2
            };
            var CSS_CLASSES = {
                VISIBLE: 'visible',
                FF_FIX_APPLIED: 'firefox-fix-applied',
                UI_HINT: 'UIhintDragAndDrop'
            };

            // Scrolling Constants - Modify these to change the scrolling behaviour
            var SCROLL_DISTANCE = (SM_UI._getBrowser() === SM_UI._BROWSERS.IE) ? 60 : 5; // Distance (in pixels) used for the scroll animation on html, body of the iFrame
            var SCROLL_SPEED = (SM_UI._getBrowser() === SM_UI._BROWSERS.IE) ? 100 : 10; // Speed (in ms) used for the scroll animation on html, body of the iFrame

            var IFRAME_TOP_STOP_SCROLL = 10;
            var IFRAME_BOTTOM_STOP_SCROLL = 450;

            var IFRAME_TOP_HINT_HEIGHT = 20;
            var IFRAME_BOTTOM_HINT_HEIGHT = 65;


            var dragAndDropConfig;
            var areHintsVisible = false;
            var dragStartedInExternalFrame = false;
            var $translate = $filter('translate');

            // Page element references
            var slots, iFrame, iFrameBody;

            // -- Initialization  ------------------------------------------------------------------------------------

            var _addUIHints = function() {
                var pageBody = this._getSelector('body');
                UI_HINTS.TOP = this._createAndAppendUIHint(pageBody, 'draganddrop.uihint.top', 'top');
                UI_HINTS.BOTTOM = this._createAndAppendUIHint(pageBody, 'draganddrop.uihint.bottom', 'bottom');

                // TODO: Verify this works on other browsers too. Before they were applied to Firefox only.
                UI_HINTS.IFRAME_TOP = this._createAndAppendUIHint(iFrameBody, null, 'top-firefox-fix');
                UI_HINTS.IFRAME_BOTTOM = this._createAndAppendUIHint(iFrameBody, 'draganddrop.uihint.bottom', 'bottom-firefox-fix');

            };

            var _createAndAppendUIHint = function(target, displayMsgKey, cssClass) {
                var uiHint = this._getSelector("<div class='UIhintDragAndDrop' style='display: none;'></div>");

                if (displayMsgKey) {
                    uiHint.append("<span>" + $translate(displayMsgKey) + "</span>");
                }

                if (cssClass) {
                    uiHint.addClass(cssClass);
                }

                target.append(uiHint);

                return uiHint;
            };

            var _areUIHintsInjected = function() {
                return ($('.' + CSS_CLASSES.UI_HINT).length > 0);
            };

            // -- Scrolling Control ------------------------------------------------------------------------------
            var _scrollingCtlFunctions = function() {
                var keepScrolling = false;
                var mousePosition = {};

                var mouseEnteredTopHint = false;
                var mouseEnteredBottomHint = false;

                return {
                    setMousePosition: function(xPos, yPos) {
                        mousePosition = {
                            x: xPos,
                            y: yPos
                        };
                    },
                    getMousePosition: function() {
                        return mousePosition;
                    },
                    checkScrollingStatus: function(scrollRequested, direction) {
                        if (direction === SCROLL_DIRECTION.UP) {
                            var hasReachedTop = SM_UI._getIframeScrollTop() < IFRAME_TOP_STOP_SCROLL;
                            keepScrolling = scrollRequested && !hasReachedTop;
                        } else if (direction === SCROLL_DIRECTION.DOWN) {
                            var hasReachedBottom = (iFrameBody.height() - SM_UI._getIframeScrollTop()) < IFRAME_BOTTOM_STOP_SCROLL;
                            keepScrolling = scrollRequested && !hasReachedBottom;
                        }

                        return keepScrolling;
                    },
                    scrollIFrame: function(direction) {
                        var scroll;
                        if (direction === SCROLL_DIRECTION.UP) {
                            scroll = "-=" + SCROLL_DISTANCE;
                        } else if (direction === SCROLL_DIRECTION.DOWN) {
                            scroll = "+=" + SCROLL_DISTANCE;
                        } else {
                            return;
                        }

                        // Prevent multiple animations to be called
                        var body = iFrame.contents().find('html, body');
                        if (body.is(':animated')) {
                            return;
                        }

                        // Fix the helper position. If this is not done the helper won't follow the scroll movement.
                        var helper = iFrame.contents().find('._sm_helper');
                        if (helper.length > 0) {
                            if (dragStartedInExternalFrame) {
                                if ($.ui.smartedit._getBrowser() !== $.ui.smartedit._BROWSERS.FIREFOX) {
                                    helper.detach().appendTo("body .ySmartEditToolbars");
                                    $('._sm_helper').detach().appendTo('body .ySmartEditToolbars');
                                }
                                helper[0].style.top = mousePosition.y - SM_UI._getIframeScrollTop() + 'px';
                            } else {
                                helper[0].style.top = mousePosition.y + SM_UI._getIframeScrollTop() + 'px';
                            }
                        }

                        // Perform the actual animation
                        body.animate({
                                scrollTop: scroll
                            }, SCROLL_SPEED, null,
                            hitch(this, function() {
                                if (this.checkScrollingStatus(keepScrolling, direction)) {
                                    this.scrollIFrame(direction);
                                }
                            }));
                    },
                    calculateIFrameHintMouseStatus: function(xPos, yPos) {
                        if (yPos < IFRAME_TOP_HINT_HEIGHT) {
                            UI_HINTS.TOP.trigger('mouseenter');
                            mouseEnteredTopHint = true;
                        } else if (yPos > IFRAME_TOP_HINT_HEIGHT && mouseEnteredTopHint) {
                            UI_HINTS.TOP.trigger("mouseleave");
                            mouseEnteredTopHint = false;
                        }

                        if (yPos + IFRAME_BOTTOM_HINT_HEIGHT > iFrame.height()) {
                            UI_HINTS.BOTTOM.trigger('mouseenter');
                            mouseEnteredBottomHint = true;
                        } else if ((yPos + IFRAME_BOTTOM_HINT_HEIGHT < iFrame.height()) && mouseEnteredBottomHint) {
                            UI_HINTS.BOTTOM.trigger('mouseleave');
                            mouseEnteredBottomHint = false;
                        }
                    }
                };
            }();

            var _registerScrollingInPage = function() {

                UI_HINTS.TOP.bind('mouseenter', function() {
                    if (_scrollingCtlFunctions.checkScrollingStatus(true, SCROLL_DIRECTION.UP)) {
                        _scrollingCtlFunctions.scrollIFrame(SCROLL_DIRECTION.UP);
                    }
                });

                UI_HINTS.TOP.bind('mouseleave', function() {
                    _scrollingCtlFunctions.checkScrollingStatus(false, SCROLL_DIRECTION.UP);
                    slots.sortable("refreshPositions");
                });

                UI_HINTS.BOTTOM.bind('mouseenter', function() {
                    if (_scrollingCtlFunctions.checkScrollingStatus(true, SCROLL_DIRECTION.DOWN)) {
                        _scrollingCtlFunctions.scrollIFrame(SCROLL_DIRECTION.DOWN);
                    }
                });

                UI_HINTS.BOTTOM.bind('mouseleave', function() {
                    _scrollingCtlFunctions.checkScrollingStatus(false, SCROLL_DIRECTION.DOWN);
                    slots.sortable("refreshPositions");
                });

                iFrameBody.bind('mousemove', function(event) {
                    if (event.preventDefault) event.preventDefault();
                    //if (event.stopPropagation) event.stopPropagation();

                    _scrollingCtlFunctions.setMousePosition(event.clientX, event.clientY);
                    if (areHintsVisible && !dragStartedInExternalFrame) {
                        _scrollingCtlFunctions.calculateIFrameHintMouseStatus(event.clientX, event.clientY);
                    }
                });
            };


            // -- UI-Hint Display Control ------------------------------------------------------------------------

            /**
             * UI-hints are hidden until drag-and-drop is started. This method will make those UI-hints visible.
             * @param isExternalFrame Whether this method was called from the outer frame.
             * @private
             */
            var _showUIHints = function(isExternalFrame) {
                this._refreshElementReferences();

                UI_HINTS.TOP.toggleClass(CSS_CLASSES.VISIBLE, true);
                UI_HINTS.BOTTOM.toggleClass(CSS_CLASSES.VISIBLE, true);

                if (!isExternalFrame) {
                    // Hide the regular '.UIhintDragAndDrop.bottom' by adding the '.firefox-fix-applied' css class
                    // This class will visibility: hidden
                    UI_HINTS.BOTTOM.toggleClass(CSS_CLASSES.FF_FIX_APPLIED, true);

                    // Then display the Firefox-fix hints instead
                    UI_HINTS.IFRAME_TOP.toggleClass(CSS_CLASSES.VISIBLE, true);
                    UI_HINTS.IFRAME_BOTTOM.toggleClass(CSS_CLASSES.VISIBLE, true);
                }

                areHintsVisible = true;
            };

            /**
             * This method will be called after drag-and-drop is stopped; it will hide UI-hints.
             * @param isExternalFrame Whether this method was called from the outer frame.
             * @private
             */
            var _hideUIHints = function(isExternalFrame) {
                UI_HINTS.TOP.removeClass(CSS_CLASSES.VISIBLE);
                UI_HINTS.BOTTOM.removeClass(CSS_CLASSES.VISIBLE);

                if (!isExternalFrame) {
                    UI_HINTS.BOTTOM.removeClass(CSS_CLASSES.FF_FIX_APPLIED);
                    UI_HINTS.IFRAME_TOP.removeClass(CSS_CLASSES.VISIBLE);
                    UI_HINTS.IFRAME_BOTTOM.removeClass(CSS_CLASSES.VISIBLE);
                }

                areHintsVisible = false;
            };

            // Helper Methods
            function _getSelector(selector) {
                return $(selector);
            }

            function _refreshElementReferences() {
                iFrame = this._getSelector('iframe');
                iFrameBody = iFrame.contents().find('body');
                slots = iFrame.contents().find(dragAndDropConfig.targetSelector);
            }

            return {
                /**
                 * @ngdoc method
                 * @name dragAndDropScrollingModule.service:dragAndDropScrollingService#initialize
                 * @methodOf dragAndDropScrollingModule.service:dragAndDropScrollingService
                 *
                 * @description
                 * This method prepares the page to allow scrolling while a SmartEdit component is dragged. It creates
                 * and appends UI hints and attaches the necessary event handlers.
                 */
                initialize: function() {
                    if (!dragAndDropConfig) {
                        // No drag and drop configuration. No need to do scrolling.
                        return;
                    }

                    this._refreshElementReferences();
                    if (!this._areUIHintsInjected()) {
                        this._addUIHints();
                        this._registerScrollingInPage();
                    }
                },
                /**
                 * @ngdoc method
                 * @name dragAndDropScrollingModule.service:dragAndDropScrollingService#enableScrolling
                 * @methodOf dragAndDropScrollingModule.service:dragAndDropScrollingService
                 *
                 * @description
                 * This method is executed when the dragging of SmartEdit component is started. It shows the UI hints and enables scrolling
                 * when the cursor hovers over them.
                 *
                 * @param {Boolean} startedInExternalFrame Flag that indicates if the drag event was started in the outer frame.
                 * @param {Boolean} isExternalFrame Flag that indicates if the current method is called in the outer frame.
                 */
                enableScrolling: function(startedInExternalFrame, isExternalFrame) {
                    if (!dragAndDropConfig) {
                        // No drag and drop configuration. No need to do scrolling.
                        return;
                    }

                    dragStartedInExternalFrame = startedInExternalFrame;

                    if ((startedInExternalFrame && isExternalFrame) || !startedInExternalFrame) {
                        this._showUIHints(isExternalFrame);
                    }
                },
                /**
                 * @ngdoc method
                 * @name dragAndDropScrollingModule.service:dragAndDropScrollingService#disableScrolling
                 * @methodOf dragAndDropScrollingModule.service:dragAndDropScrollingService
                 *
                 * @description
                 * This method is executed when the dragging of a SmartEdit component stops. It hides UI hints and disables
                 * scrolling.
                 *
                 * @param {Boolean} startedInExternalFrame Flag that indicates if the drag event was started in the outer frame.
                 * @param {Boolean} isExternalFrame Flag that indicates if the current method is called in the outer frame.
                 */
                disableScrolling: function(startedInExternalFrame, isExternalFrame) {
                    if (!dragAndDropConfig) {
                        // No drag and drop configuration. No need to do scrolling.
                        return;
                    }

                    if ((startedInExternalFrame && isExternalFrame) || !startedInExternalFrame) {
                        this._hideUIHints(isExternalFrame);
                    }

                    dragStartedInExternalFrame = false;
                },
                /**
                 * @ngdoc method
                 * @name dragAndDropScrollingModule.service:dragAndDropScrollingService#connectWithDragAndDrop
                 * @methodOf dragAndDropScrollingModule.service:dragAndDropScrollingService
                 *
                 * @description
                 * This method enables the communication between this service and the base
                 * {@link dragAndDropServiceInterfaceModule.DragAndDropServiceInterface SmartEdit Drag and Drop service }.
                 *
                 * @param {Object} dragAndDropConfiguration The object used to configure SmartEdit Drag and Drop service.
                 */
                connectWithDragAndDrop: function(dragAndDropConfiguration) {
                    dragAndDropConfig = dragAndDropConfiguration;
                },

                // Internal methods exposed for unit testing.
                _addUIHints: _addUIHints,
                _areUIHintsInjected: _areUIHintsInjected,
                _showUIHints: _showUIHints,
                _hideUIHints: _hideUIHints,
                _getSelector: _getSelector,
                _createAndAppendUIHint: _createAndAppendUIHint,
                _refreshElementReferences: _refreshElementReferences,
                _registerScrollingInPage: _registerScrollingInPage
            };
        }]);
})();
