module.exports = {
    experienceSelectorToolbar: function() {
        return element(by.css('.ySmartEditExperienceSelectorToolbar'));
    },
    renderButton: function() {
        return this.experienceSelectorToolbar().element(by.cssContainingText('button', 'Render Component'));
    },
    renderSlotButton: function() {
        return this.experienceSelectorToolbar().element(by.cssContainingText('button', 'Render Slot'));
    }
};
