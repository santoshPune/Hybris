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
angular.module('dragAndDropServiceModule', ['dragAndDropServiceInterfaceModule', 'gatewayProxyModule', 'dragAndDropScrollingModule'])
    .factory('dragAndDropService', function($log, $rootScope, $timeout, DragAndDropServiceInterface, gatewayProxy, extend, hitch, dragAndDropScrollingService) {

        var SM_UI = $.ui.smartedit;
        var HELPER_ID = "_sm_helper";

        var DragAndDropService = function(gatewayId) {
            this.gatewayId = gatewayId;

            gatewayProxy.initForService(this, ["register"]);
            this.configurations = {};

            this._clearVariables = function() {
                this._sharedDragHelper = null;
                this._originalDraggedItem = null;
                this._isDragStartedInDraggable = false;
                this._isHelperRepositioned = false;
                this._isSlotsPositionCachingUpdated = false;
            };
            this._clearVariables();

            //for testing purposes we need the following wrappers
            this.getSelector = function(selector) {
                return $(selector);
            };

            this.getSelectorInIframe = function(selector) {
                return this.getSelector('iframe').contents().find(selector);
            };

            this.cancel = function(sourceSelector, parentSlot, event, ui) {
                if (this.fromFrame) {
                    // if dragging between two sortables
                    if (ui.sender) {
                        ui.sender.sortable("cancel");
                        // if dragging within same sortable
                    } else {
                        parentSlot.sortable("cancel");
                    }
                } else {
                    // this works while dragging from draggable that uses a connectToSortable
                    $(event.target).find(sourceSelector + ':nth-child(' + (ui.item.index() + 1) + ')').remove();
                }
            };

            this.cloneElement = function(selectorName) {
                return $(selectorName).clone();
            };

            // This is only to help test; 'this' is not possible to test, so this method is introduced to be able to
            // get a mock.
            this._getReference = function(reference) {
                return reference;
            };

            this._removeDynamicStyles = function(configuration) {
                this._removeHelpers();
                this.getSelectorInIframe(configuration.targetSelector).removeClass("eligible-slot");
            };

            this._removeHelpers = function() {
                // Needed because of bug arising after a drop from parent frame : some helpers from sortable stick after a stop
                var helperSelector = "." + HELPER_ID;
                this.getSelectorInIframe(helperSelector).remove();
                this.getSelector(helperSelector).remove();
            };

            this._bindWatcher = function(configuration) {
                // This service relies on both, AngularJS and jQuery UI. Although it makes the drag and drop operation very
                // powerful, it also makes interaction very hard, as it combines the lifecycle of both of these frameworks.
                // For instance, when new items are added to the DOM, jQuery doesn't know that something changed,
                // and thus doesn't make them draggable, if necessary. To do so, it's necessary to re-register this at the
                // end of every digest cycle.
                configuration._unbindWatcher = $rootScope.$watch(
                    hitch(this, function() {
                        var elements = $(configuration.sourceSelector);
                        return (elements.filter($(":not(.ui-draggable.ui-draggable-handle)")).length > 0);
                    }),
                    hitch(this, function() {
                        $timeout(hitch(this, function() {
                            $log.debug("Updating draggable components.");
                            this.apply();
                        }, 0, false));
                    }));
            };
        };

        DragAndDropService = extend(DragAndDropServiceInterface, DragAndDropService);

        // Draggable Callbacks
        DragAndDropService.prototype.draggableStart = function(configuration, targetSelector, event, ui) {
            this.getSelectorInIframe('body').addClass('is-dragging');
            this._isDragStartedInDraggable = true;
            this._isHelperRepositioned = false;
            this.updateSlotsPositionCaching(configuration);
            $('._sm_helper').detach().appendTo('body .ySmartEditToolbars');

            // (true, true) drag started in outer frame and this event is triggered in the outer frame.
            dragAndDropScrollingService.enableScrolling(true, true);

            if (configuration.startCallback) {
                configuration.startCallback(event, ui);
            }
        };

        DragAndDropService.prototype.draggableDrag = function(configuration, targetSelector, event, ui) {
            this.fromFrame = false;
            this._sharedDragHelper = ui.helper[0];
            if (!this._isHelperRepositioned) {
                this._isHelperRepositioned = true;
                var helper = ui.helper;
                helper.detach().appendTo('body .ySmartEditToolbars');
                ui.helper[0].style.top = event.clientX;
                ui.helper[0].style.left = event.clientY;
            }
        };

        DragAndDropService.prototype.draggableStop = function(configuration, targetSelector, event, ui) {
            this.getSelectorInIframe('body').removeClass('is-dragging is-sorting');
            this._removeDynamicStyles(configuration);
            this._isDragStartedInDraggable = false;

            if (configuration.stopCallback) {
                configuration.stopCallback(event, ui);
            }

            this._sharedDragHelper = null;
            this._isHelperRepositioned = false;
        };

        // Sortable Callbacks
        DragAndDropService.prototype.sortableStart = function(configuration, targetSelector, event, ui) {
            this.getSelectorInIframe('body').addClass('is-sorting');

            // This will update the slots dimensions, and update jQuery UI internal representation of them. This will ensure
            // correct intersections while dragging components on sortable areas.
            this.updateSlotsPositionCaching(configuration);

            // This is important because UI hints are controlled differently depending on where the dragging started, the inner or outer frame.
            if (!this._isDragStartedInDraggable) {
                // This is not external frame.
                dragAndDropScrollingService.enableScrolling(this._isDragStartedInDraggable, false);
            }

            this.fromFrame = true;
            this.getSelectorInIframe(configuration.targetSelector).addClass("eligible-slot");

            if (this._isDragStartedInDraggable) {
                ui.item.replaceWith(this._sharedDragHelper);
            }

            var sortableElement = this.getSelector(targetSelector);
            sortableElement.sortable("refresh");

            if (configuration.startCallback) {
                configuration.startCallback(event, ui);
            }

        };

        DragAndDropService.prototype.sortableStop = function(configuration, targetSelector, event, ui) {
            this.getSelectorInIframe('body').removeClass('is-sorting');
            this._removeDynamicStyles(configuration);
            this._isHelperRepositioned = false;

            if (configuration.stopCallback) {
                configuration.stopCallback(event, ui);
            }
        };

        DragAndDropService.prototype.sortableOver = function(configuration, targetSelector, event, ui) {
            if (configuration.overCallback) {
                configuration.overCallback(event, ui);
            }
        };

        DragAndDropService.prototype.sortableOut = function(configuration, targetSelector, event, ui) {
            if (configuration.outCallback) {
                configuration.outCallback(event, ui);
            }
        };

        DragAndDropService.prototype.sortableUpdate = function(configuration, targetSelector, event, ui) {
            var parentSlot = ui.item.closest(configuration.targetSelector);
            var targetElement = event.target;

            if (targetElement === parentSlot[0]) {
                var updatedElement = ui.item;

                if (this._isDragStartedInDraggable) {
                    this._isDragStartedInDraggable = false;
                    updatedElement = this._originalDraggedItem;
                    ui.item.replaceWith(updatedElement); // This is to update the DOM
                    ui.item = updatedElement; // This is to update the reference in the ui that's returned to the API caller.
                }

                // This callback happens after the DOM has been updated.
                if (configuration.dropCallback) {
                    configuration.dropCallback(event, ui, hitch(this, this.cancel, configuration.sourceSelector, parentSlot, event, ui));
                }
            }
        };

        DragAndDropService.prototype.draggableHelper = function(configuration, targetSelectorName, event, ui) {

            var content = null;

            // This method creates a clone of the original element. Other parts of the drag-and-drop service need to
            // identify and work with this clone (e.g., we need to remove the dropped clone and update it with whatever
            // is provided by the generic editor). However, it's impossible to know where the clone is going to be. Thus,
            // we need to keep track of it.
            this._originalDraggedItem = this.cloneElement(targetSelectorName).removeAttr("id");
            this._originalDraggedItem.removeClass('ui-draggable ui-draggable-handle');
            this._originalDraggedItem.addClass('ui-sortable-handle');

            if (configuration.helper) {
                var helperElement = this.getSelector(configuration.helper(event, targetSelectorName));
                this._copyElementProperties(this._originalDraggedItem, helperElement);
                content = helperElement.prop("outerHTML");
            } else {
                content = this._originalDraggedItem.html();
            }

            var result = $("<div>" + content + "</div>");
            result.attr('id', HELPER_ID);
            result.addClass(HELPER_ID);

            return result;
        };

        DragAndDropService.prototype.sortableHelper = function(configuration, targetSelector, event, element) {

            var content = element.html();
            if (configuration.helper) {
                var helperElement = this.getSelector(configuration.helper(event, element));
                this._copyElementProperties(element, helperElement);
                content = helperElement.prop("outerHTML");
            }

            var result = $("<div>" + content + "</div>");
            result.attr('id', HELPER_ID);
            result.addClass(HELPER_ID);

            return result;
        };

        DragAndDropService.prototype._copyElementProperties = function(originalElement, destinationElement) {
            var attributes = originalElement[0].attributes;
            for (var i = 0; i < attributes.length; i++) {
                var attr = attributes[i];
                if (attr.nodeName !== 'class' && attr.nodeName !== 'id') {
                    destinationElement.attr(attr.nodeName, attr.nodeValue);
                }
            }
        };

        DragAndDropService.prototype.registerSortable = function(configuration) {

            var thisService = this;

            var iframe = $('#js_iFrameWrapper iframe');
            var IFRAME_BOTTOM_STOP_SCROLL = 450; // Minimum distance (in pixels) allowed between the bottom of the iframe and the toolbar. Helps preventing infinite scroll

            // With this padding bottom, sortable elements will never be outside of the iFrame body and it will prevent infinite scrolling
            iframe.contents().find('body').css('padding-bottom', IFRAME_BOTTOM_STOP_SCROLL / 2.5);

            this.getSelectorInIframe(configuration.targetSelector).sortable({
                connectWith: configuration.targetSelector, //to be able to sort across slots
                items: configuration.sourceSelector, //restrictions on what can be sorted from targetSelector : not direct child since decorator wrapper will add noise between container and components
                iframeFix: true,
                cursorAt: {
                    top: 0,
                    left: 0
                },
                scroll: false,
                intersect: "pointer",
                placeholder: 'ySEDnDPlaceHolder',
                start: function(event, ui) {
                    thisService.sortableStart(configuration, thisService._getReference(this), event, ui);
                },
                stop: function(event, ui) {
                    if (!thisService._isDragStartedInDraggable) {
                        // This is not external frame.
                        dragAndDropScrollingService.disableScrolling(thisService._isDragStartedInDraggable, false);
                    }

                    thisService.sortableStop(configuration, thisService._getReference(this), event, ui);
                },
                over: function(event, ui) {
                    thisService.sortableOver(configuration, thisService._getReference(this), event, ui);
                },
                out: function(event, ui) {
                    if ($.ui.smartedit._getBrowser() !== $.ui.smartedit._BROWSERS.FIREFOX) {
                        if (thisService._isDragStartedInDraggable) {
                            $('._sm_helper').detach().appendTo('body .ySmartEditToolbars');
                            $('iframe').contents().find('._sm_helper').detach().appendTo('body .ySmartEditToolbars');
                        } else {
                            $('._sm_helper').detach().appendTo('body .ySmartEditToolbars');
                        }
                    }

                    thisService.sortableOut(configuration, thisService._getReference(this), event, ui);
                },
                update: function(event, ui) {
                    thisService.sortableUpdate(configuration, thisService._getReference(this), event, ui);
                },
                helper: function(event, element) {
                    return thisService.sortableHelper(configuration, thisService._getReference(this), event, element);
                },
                sort: function(event, ui) {

                }
            });
            this.getSelectorInIframe(configuration.targetSelector).disableSelection();
        };

        DragAndDropService.prototype.registerDrag = function(configuration) {

            var thisService = this;

            this.getSelector(configuration.sourceSelector).draggable({
                revert: false,
                iframeFix: true,
                scroll: false,
                cursorAt: {
                    left: 0,
                    top: 0
                },
                connectToSortable: this.getSelectorInIframe(configuration.targetSelector),
                helper: function(event) {
                    return thisService.draggableHelper(configuration, thisService._getReference(this), event, null);
                },
                start: function(event, ui) {
                    thisService.draggableStart(configuration, thisService._getReference(this), event, ui);
                },
                drag: function(event, ui) {
                    thisService.draggableDrag(configuration, thisService._getReference(this), event, ui);
                },
                stop: function(event, ui) {
                    // (true, true) drag started in outer frame and this event is triggered in the outer frame.
                    dragAndDropScrollingService.disableScrolling(true, true);

                    thisService.draggableStop(configuration, thisService._getReference(this), event, ui);
                }
            });
        };

        DragAndDropService.prototype.register = function(configuration) {
            // Validate
            if (!configuration.id) {
                throw new Error("register() - Drag and drop configuration needs an id.");
            }

            this.configurations[configuration.id] = configuration;
            this._bindWatcher(configuration);
        };

        DragAndDropService.prototype.unregister = function(configurationsIDList) {
            // Remove jQuery UI registration.
            var newConfigurations = {};
            Object.keys(this.configurations).forEach(hitch(this, function(key) {
                var configuration = this.configurations[key];

                if (configurationsIDList.indexOf(configuration.id) >= 0) {
                    // Remove watch
                    configuration._unbindWatcher();

                    this.getSelector(configuration.sourceSelector).draggable("destroy");
                    this.getSelectorInIframe(configuration.targetSelector).sortable("destroy");
                } else {
                    newConfigurations[configuration.id] = configuration;
                }
            }));
            this.configurations = newConfigurations;

            // Reset jQuery UI SmartEdit fixes
            SM_UI._numConnectedDraggables = 0;
            SM_UI._sections = {};
            SM_UI._currentBrowser = null;

            this._clearVariables();
        };

        DragAndDropService.prototype.updateSlotsPositionCaching = function(configuration) {
            var containerSlots = this.getSelectorInIframe(configuration.targetSelector);
            containerSlots.sortable("refreshPositions");
        };

        DragAndDropService.prototype.apply = function() {
            // First make sure that the jQuery UI fix is clean.
            SM_UI._clearCache();

            Object.keys(this.configurations).forEach(hitch(this, function(key) {
                var configuration = this.configurations[key];

                $log.debug("applying dragAndDrop with configuration", configuration);
                //handles drag and drop from and within iframe
                this.registerSortable(configuration);
                //handles drag from parent and connecting to the drop/sort of the iframe
                this.registerDrag(configuration);

                dragAndDropScrollingService.connectWithDragAndDrop(configuration);
            }));
        };

        return new DragAndDropService("dragAndDrop");
    });
