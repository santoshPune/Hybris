if(typeof $.ui == 'object'){

    // SmartEdit extension
    $.extend($.ui, {

        smartedit: {
            _BROWSERS: {
                FIREFOX: "Firefox",
                SAFARI: "Safari",
                IE: "IE",
                EDGE: "Edge",
                CHROME: "Chrome"
            },

            _CSSCLASSES: {
                DIRTY_HEIGHT: 'ySEDirtyHeight',
                EMPTY_SLOT: 'ySEemptySlot'
            },

            _CSSSELECTORS: {
                COMPONENT_IN_SLOT: '.smartEditComponent[data-smartedit-component-type!="ContentSlot"]'
            },

            _getBrowser: function () {
                if(!this._currentBrowser){
                    var browser = null;
                    if(typeof InstallTrigger !== 'undefined'){
                        browser = this._BROWSERS.FIREFOX;
                    }
                    else if( Object.prototype.toString.call(window.HTMLElement).indexOf('Constructor') > 0 ){
                        browser = this._BROWSERS.SAFARI;
                    }
                    else if( /*@cc_on!@*/false || !!document.documentMode ){
                        browser = this._BROWSERS.IE;
                    }
                    else if( !!window.StyleMedia ){
                        browser = this._BROWSERS.EDGE;
                    }
                    else if( !!window.chrome && !!window.chrome.webstore ){
                        browser = this._BROWSERS.CHROME;
                    }

                    this._currentBrowser = browser;
                }

                return this._currentBrowser;
            },

            _getIframeScrollTop: function () {

                var browser = this._getBrowser();

                if(browser === this._BROWSERS.FIREFOX ||  browser === this._BROWSERS.IE ){
                    return Math.round(this._getSection('iframeHtml').scrollTop());
                }
                else if (browser === this._BROWSERS.CHROME){
                    return Math.round(this._getSection('iframeBody').scrollTop());
                }
                else {
                    // TO DO: TEST FOR OTHER BROWSERS IF BODY OR HTML IS USED
                    return Math.round(this._getSection('iframeBody').scrollTop());
                }

            },

            _getToolbarHeight: function(){
                return Math.round(this._getSection('toolbar').height());
            },

            _clearCache: function(){
                this._sections = {};
            },

            _getSection: function(sectionId) {
                if( !this._sections[ sectionId ] ){
                    var section;
                    switch( sectionId ){
                        case 'body':
                            section = $('body');
                            break;
                        case 'iframe':
                            section = $('iframe');
                            break;
                        case 'iframeHtml':
                            section = this._getSection('iframe').contents().find('html');
                            break;
                        case 'iframeBody':
                            section = this._getSection('iframe').contents().find('body');
                            break;
                        case 'containerSlots':
                            section = this._getSection('iframe').contents().find('.smartEditComponent[data-smartedit-component-type=ContentSlot]');
                            break;
                        case 'toolbar':
                            section = $('.ySmartEditToolbars');
                            break;
                        default:
                            section = null;
                    }

                    this._sections[ sectionId ] = section;
                }

                return this._sections[ sectionId ];
            },

            _isElementInIframe: function(jQueryElement){
                var unwrappedElement = jQueryElement[0];

                return unwrappedElement && unwrappedElement.ownerDocument === this._getSection( 'iframeBody' )[0].ownerDocument;
            },

            _isDragging: function() {
                var currentHelperSelector = this._getSection('iframe').contents().find('#_sm_helper');
                return currentHelperSelector.length > 0 && currentHelperSelector.hasClass("ui-draggable-dragging");
            },

            _getNumConnectedDraggables: function(){
                return this._numConnectedDraggables;
            },

            _addNumConnectedDraggables: function(){
                this._numConnectedDraggables++;
            },

            _reduceNumConnectedDraggables: function(){
                if( this._numConnectedDraggables > 0 ) {
                    this._numConnectedDraggables--;
                }
            },

            // This counter represents the number of draggables that have been connected to a sortable. It's important since
            // it helps determine whether the helper is in control of draggable or sortable. (Most of the time it should be 1 or 0).
            _numConnectedDraggables: 0,

            _sections: {},

            _currentBrowser: null

        }
    });

}
else {

    throw new Error('$.ui is undefined. Make sure jQuery and jQueryUI have been loaded before the jQueryUI smartedit extension.');

}