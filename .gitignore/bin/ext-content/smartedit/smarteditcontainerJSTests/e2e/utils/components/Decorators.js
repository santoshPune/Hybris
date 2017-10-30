var perspectives = require('./Perspectives.js');

module.exports = {
    renderDecorator: function(componentId) {
        var renderButtonId = componentId + '-render-button-inner';
        return perspectives.getElementInOverlay(componentId).element(by.id(renderButtonId));
    },
    renderSlotDecorator: function(componentId) {
        var renderButtonId = componentId + '-render-slot-button-inner';
        return perspectives.getElementInOverlay(componentId).element(by.id(renderButtonId));
    },
    dirtyContentDecorator: function(componentId) {
        var renderButtonId = componentId + '-dirty-content-button';
        return perspectives.getElementInOverlay(componentId).element(by.id(renderButtonId));
    }
};
