module.exports = {
    addComponentMenuToolbarItem: function() {
        return element(by.css('component-menu'));
    },
    addComponentMenuButton: function() {
        return this.addComponentMenuToolbarItem().element(by.css('button'));
    },
    openMenu: function() {
        return element(by.xpath("//div[@class='ySEComponentMenuW']/button")).click();
    },
    switchTab: function(tabId) {
        return element(by.xpath("//div[@class='ySEComponentsResult']/div/ul[@class='nav nav-tabs']/li[" + tabId + "]")).click();
    },
    _getContent: function(types) {
        return protractor.promise.all(types.map(function(type) {
            return type.getText();
        }));
    },
    getComponentTypes: function() {
        var deferred = protractor.promise.defer();
        var items = by.xpath("//div[@class='tab-content']/div[1]//div[contains(@class,'ySECustCompType')]");

        element.all(items).then(function(types) {
            this._getContent(types).then(function(array) {
                deferred.fulfill(array);
            });

        }.bind(this));
        return deferred.promise;
    },
    getCustomComponentNames: function() {
        var itemsSelector = by.xpath("//div[@class='tab-content']/div[2]//div[contains(@class,'ySECustCompName')]");

        return element.all(itemsSelector).then(function(items) {
            return this._getContent(items);
        }.bind(this));
    },
    getCustomComponentTypes: function() {
        var deferred = protractor.promise.defer();
        var items = by.xpath("//div[@class='tab-content']/div[2]//div[contains(@class,'ySECustCompType')]");

        element.all(items).then(function(types) {
            this._getContent(types).then(function(array) {
                deferred.fulfill(array);
            });

        }.bind(this));
        return deferred.promise;
    },
    menuIsVisible: function() {
        return this.listElements() !== null;
    },
    listElements: function() {
        return this.addComponentMenuToolbarItem().element(by.css('li'));
    },
    searchComponents: function(searchKey) {
        return element(by.xpath("//div[contains(@class,'ySEComponentSearch')]/input")).sendKeys(searchKey);
    },
    getCustomComponentsScrollElement: function() {
        return element(by.xpath("//div[@class='ySEComponentsResult']//div[@class='ySEInfiniteScrolling-container ']"));
    },
};
