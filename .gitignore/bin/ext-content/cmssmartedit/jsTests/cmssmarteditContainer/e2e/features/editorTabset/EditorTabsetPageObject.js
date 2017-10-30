module.exports = {
    EDITOR_TABSET_SELECTOR: 'editor-tabset',
    EDITOR_TABSET_TABS: 'ul.nav-tabs',
    EDITOR_DROPDOWN_MENU: 'ul.dropdown-menu',
    LINK_SELECTOR: 'a',
    BASIC_TAB_SELECTOR: 'li[data-tab-id="basicTab"]',
    ADMIN_TAB_SELECTOR: 'li[data-tab-id="adminTab"]',
    VISIBILITY_TAB_SELECTOR: 'li[data-tab-id="visibilityTab"]',
    GENERIC_TAB_SELECTOR: 'li[data-tab-id="genericTab"]',
    TAB1_TAB_SELECTOR: 'li[data-tab-id="tab1"]',
    TAB2_TAB_SELECTOR: 'li[data-tab-id="tab2"]',
    TAB3_TAB_SELECTOR: 'li[data-tab-id="tab3"]',
    BASIC_TAB_CONTENT_SELECTOR: 'y-tab[data-tab-id="basicTab"]',
    ADMIN_TAB_CONTENT_SELECTOR: 'y-tab[data-tab-id="adminTab"]',
    VISIBILITY_TAB_CONTENT_SELECTOR: 'y-tab[data-tab-id="visibilityTab"]',
    GENERIC_TAB_CONTENT_SELECTOR: 'y-tab[data-tab-id="genericTab"]',
    TAB1_TAB_CONTENT_SELECTOR: 'y-tab[data-tab-id="tab1"]',
    TAB2_TAB_CONTENT_SELECTOR: 'y-tab[data-tab-id="tab2"]',
    TAB3_TAB_CONTENT_SELECTOR: 'y-tab[data-tab-id="tab3"]',
    editorTabset: function() {
        return element(by.css(this.EDITOR_TABSET_SELECTOR));
    },
    editorTabsetTabs: function() {
        return this.editorTabset().element(by.css(this.EDITOR_TABSET_TABS));
    },
    editorDropdownMenu: function() {
        return this.editorTabset().element(by.css(this.EDITOR_DROPDOWN_MENU));
    },
    basicTab: function() {
        return this.editorTabsetTabs().element(by.css(this.BASIC_TAB_SELECTOR));
    },
    adminTab: function() {
        return this.editorTabsetTabs().element(by.css(this.ADMIN_TAB_SELECTOR));
    },
    visibilityTab: function() {
        return this.editorTabsetTabs().element(by.css(this.VISIBILITY_TAB_SELECTOR));
    },
    genericTab: function() {
        return this.editorTabsetTabs().element(by.css(this.GENERIC_TAB_SELECTOR));
    },
    genericTabLink: function() {
        return this.genericTab().element(by.css(this.LINK_SELECTOR));
    },
    tab1Tab: function() {
        return this.editorTabsetTabs().element(by.css(this.TAB1_TAB_SELECTOR));
    },
    tab1DropdownMenu: function() {
        return this.editorTabsetTabs().element(by.css(this.TAB1_TAB_SELECTOR));
    },
    tab1DropdownMenuLink: function() {
        return this.tab1DropdownMenu().element(by.css(this.LINK_SELECTOR));
    },
    tab2Tab: function() {
        return this.editorTabsetTabs().element(by.css(this.TAB2_TAB_SELECTOR));
    },
    tab3Tab: function() {
        return this.editorTabsetTabs().element(by.css(this.TAB3_TAB_SELECTOR));
    },
    tab2DropdownMenu: function() {
        return this.editorDropdownMenu().element(by.css(this.TAB2_TAB_SELECTOR));
    },
    tab2DropdownMenuLink: function() {
        return this.tab2DropdownMenu().element(by.css(this.LINK_SELECTOR));
    },
    tab3DropdownMenu: function() {
        return this.editorDropdownMenu().element(by.css(this.TAB3_TAB_SELECTOR));
    },
    tab3DropdownMenuLink: function() {
        return this.tab3DropdownMenu().element(by.css(this.LINK_SELECTOR));
    },
    basicTabContent: function() {
        return this.editorTabset().element(by.css(this.BASIC_TAB_CONTENT_SELECTOR));
    },
    adminTabContent: function() {
        return this.editorTabset().element(by.css(this.ADMIN_TAB_CONTENT_SELECTOR));
    },
    visibilityTabContent: function() {
        return this.editorTabset().element(by.css(this.VISIBILITY_TAB_CONTENT_SELECTOR));
    },
    genericTabContent: function() {
        return this.editorTabset().element(by.css(this.GENERIC_TAB_CONTENT_SELECTOR));
    },
    tab1TabContent: function() {
        return this.editorTabset().element(by.css(this.TAB1_TAB_CONTENT_SELECTOR));
    },
    tab2TabContent: function() {
        return this.editorTabset().element(by.css(this.TAB2_TAB_CONTENT_SELECTOR));
    },
    tab3TabContent: function() {
        return this.editorTabset().element(by.css(this.TAB3_TAB_CONTENT_SELECTOR));
    }
};
