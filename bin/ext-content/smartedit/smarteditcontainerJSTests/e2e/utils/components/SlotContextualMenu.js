var storefront = require('./Storefront.js');
var perspectives = require('./Perspectives.js');

module.exports = {
    getDottedSlotBorderForNonEmptySlot: function() {
        return perspectives.getElementInOverlay(storefront.TOP_HEADER_SLOT_ID)
            .element(by.css('.decorator-basic-slot-border'));
    },
    getDottedSlotBorderForEmptySlot: function() {
        return perspectives.getElementInOverlay(storefront.OTHER_SLOT_ID)
            .element(by.css('.decorator-basic-slot-border'));
    }
};
