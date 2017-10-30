var WizardStep = function() {};

WizardStep.prototype = {
    // Elements
    listItem: function(itemTitle) {
        return element(by.cssContainingText('.page-type-step-template__item', itemTitle));
    },

    listItemByIndex: function(index) {
        return element.all(by.css('.page-type-step-template__item')).get(index);
    },
    field: function(fieldId) {
        return element.all(by.id(fieldId));
    },

    // Actions
    selectItem: function(itemTitle) {
        this.listItem(itemTitle).click();
    },
    selectItemByIndex: function(itemIndex) {
        return this.listItemByIndex(itemIndex).click();
    },
    enterFieldData: function(fieldId, value) {
        return this.field(fieldId).sendKeys(value);
    }

};

module.exports = WizardStep;
