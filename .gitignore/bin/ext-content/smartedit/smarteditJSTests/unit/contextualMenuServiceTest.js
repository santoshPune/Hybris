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
describe('test contextualMenuServiceModule', function() {

    var contextualMenuService, renderService;

    beforeEach(customMatchers);

    beforeEach(module('renderServiceModule', function($provide) {
        renderService = jasmine.createSpyObj('renderService', ['_resizeEmptySlots']);
        $provide.value('renderService', renderService);
    }));

    beforeEach(module('contextualMenuServiceModule'));
    beforeEach(inject(function(_contextualMenuService_) {
        contextualMenuService = _contextualMenuService_;
    }));

    it('addItems WILL throw an error when item doesnt contain a valid key', function() {
        expect(function() {
            contextualMenuService.addItems({
                'type1': [{
                    notKey: 'contextualMenuItem1'
                }, {
                    key: 'contextualMenuItem2'
                }]
            });
        }).toThrow(new Error("addItems() - Cannot add items. Error: Item doesn't have key."));
    });

    it('getContextualMenuByType will return an unique array of contextual menu items when componenttype is given', function() {

        contextualMenuService.addItems({
            'type1': [{
                key: 'contextualMenuItem1'
            }, {
                key: 'contextualMenuItem2'
            }]
        });

        contextualMenuService.addItems({
            'type1': [{
                key: 'contextualMenuItem3'
            }],
            'type2': [{
                key: 'contextualMenuItem3'
            }, {
                key: 'contextualMenuItem4'
            }]
        });

        expect(contextualMenuService.getContextualMenuByType('type1')).toEqualData([{
            key: 'contextualMenuItem1'
        }, {
            key: 'contextualMenuItem2'
        }, {
            key: 'contextualMenuItem3'
        }]);
        expect(contextualMenuService.getContextualMenuByType('type2')).toEqualData([{
            key: 'contextualMenuItem3'
        }, {
            key: 'contextualMenuItem4'
        }]);

    });

    it('getContextualMenuByType will return an unique array of contextual menu items when it matches the regexps', function() {

        contextualMenuService.addItems({
            '*Suffix': [{
                key: 'element1'
            }, {
                key: 'element2'
            }],
            '.*Suffix': [{
                key: 'element2'
            }, {
                key: 'element3'
            }],
            'TypeSuffix': [{
                key: 'element3'
            }, {
                key: 'element4'
            }],
            '^((?!Middle).)*$': [{
                key: 'element4'
            }, {
                key: 'element5'
            }],
            'PrefixType': [{
                key: 'element5'
            }, {
                key: 'element6'
            }]
        });

        expect(contextualMenuService.getContextualMenuByType('TypeSuffix')).toEqualData([{
            key: 'element1'
        }, {
            key: 'element2'
        }, {
            key: 'element3'
        }, {
            key: 'element4'
        }, {
            key: 'element5'
        }]);

        expect(contextualMenuService.getContextualMenuByType('TypeSuffixes')).toEqualData([{
            key: 'element2'
        }, {
            key: 'element3'
        }, {
            key: 'element4'
        }, {
            key: 'element5'
        }]);

        expect(contextualMenuService.getContextualMenuByType('MiddleTypeSuffix')).toEqualData([{
            key: 'element1'
        }, {
            key: 'element2'
        }, {
            key: 'element3'
        }]);
    });

    describe('getContextualMenuItems will return an array-of-array of contextual menu items based on condition', function() {

        it('will return those menu items which satisfy the condition or those that have no condition set (default condition to be true)', function() {

            contextualMenuService.addItems({
                'ComponentType1': [{
                    key: 'key1',
                    i18nKey: 'ICON1',
                    icon: 'icon1.png'
                }, {
                    key: 'key2',
                    i18nKey: 'ICON2',
                    condition: function(configuration) {
                        return configuration.componentId === 'ComponentId2';
                    },
                    icon: 'icon2.png'
                }, {
                    key: 'key3',
                    i18nKey: 'ICON3',
                    condition: function(configuration) {
                        return true;
                    },
                    icon: 'icon3.png'
                }, {
                    key: 'key4',
                    i18nKey: 'ICON4',
                    condition: function(configuration) {
                        return false;
                    },
                    icon: 'icon4.png'
                }, {
                    key: 'key5',
                    i18nKey: 'ICON5',
                    condition: function(configuration) {
                        return configuration.componentId === 'ComponentId1';
                    },
                    icon: 'icon5.png'
                }, {
                    key: 'key6',
                    i18nKey: 'ICON6',
                    condition: function(configuration) {
                        return configuration.componentType === 'ComponentType1';
                    },
                    icon: 'icon6.png'
                }]
            });

            expect(contextualMenuService.getContextualMenuItems({
                componentId: 'ComponentId1',
                componentType: 'ComponentType1',
                iLeftBtns: 3
            })).toEqualData({
                leftMenuItems: [{
                    key: 'key1',
                    i18nKey: 'ICON1',
                    icon: 'icon1.png'
                }, {
                    key: 'key3',
                    i18nKey: 'ICON3',
                    condition: Function,
                    icon: 'icon3.png'
                }, {
                    key: 'key5',
                    i18nKey: 'ICON5',
                    condition: Function,
                    icon: 'icon5.png'
                }],
                moreMenuItems: [{
                    key: 'key6',
                    i18nKey: 'ICON6',
                    condition: Function,
                    icon: 'icon6.png'
                }]
            });

        });

        it('for iLeftBtns= 3, will set a maximum of 3 menu items to the left (1st element in the array) and the rest to the right (2nd element in the array)', function() {

            contextualMenuService.addItems({
                'ComponentType1': [{
                    key: 'key1',
                    i18nKey: 'ICON1',
                    icon: 'icon1.png'
                }, {
                    key: 'key2',
                    i18nKey: 'ICON2',
                    condition: function(configuration) {
                        return configuration.componentId === 'ComponentId2';
                    },
                    icon: 'icon2.png'
                }, {
                    key: 'key3',
                    i18nKey: 'ICON3',
                    condition: function(configuration) {
                        return true;
                    },
                    icon: 'icon3.png'
                }, {
                    key: 'key4',
                    i18nKey: 'ICON4',
                    condition: function(configuration) {
                        return false;
                    },
                    icon: 'icon4.png'
                }, {
                    key: 'key5',
                    i18nKey: 'ICON5',
                    condition: function(configuration) {
                        return configuration.componentId === 'ComponentId1';
                    },
                    icon: 'icon5.png'
                }, {
                    key: 'key6',
                    i18nKey: 'ICON6',
                    condition: function(configuration) {
                        return configuration.componentType === 'ComponentType1';
                    },
                    icon: 'icon6.png'
                }]
            });

            expect(contextualMenuService.getContextualMenuItems({
                componentId: 'ComponentId1',
                componentType: 'ComponentType1',
                iLeftBtns: 3
            }).leftMenuItems).toEqualData([{
                key: 'key1',
                i18nKey: 'ICON1',
                icon: 'icon1.png'
            }, {
                key: 'key3',
                i18nKey: 'ICON3',
                condition: function(configuration) {
                    return true;
                },
                icon: 'icon3.png'
            }, {
                key: 'key5',
                i18nKey: 'ICON5',
                condition: function(configuration) {
                    return configuration.componentId === 'ComponentId1';
                },
                icon: 'icon5.png'
            }]);

            expect(contextualMenuService.getContextualMenuItems({
                componentId: 'ComponentId1',
                componentType: 'ComponentType1',
                iLeftBtns: 3
            }).moreMenuItems).toEqualData([{
                key: 'key6',
                i18nKey: 'ICON6',
                condition: function(configuration) {
                    return configuration.componentType === 'ComponentType1';
                },
                icon: 'icon6.png'
            }]);
        });

    });

    it('getContextualMenuItems WILL provide the dom element WHEN the condition is called ', function() {

        var element = angular.element('<div></div>');
        var contextualItemMock = jasmine.createSpyObj('contextualItemMock', ['condition']);
        contextualItemMock.key = 'key1';
        contextualItemMock.i18nKey = 'ICON1';
        contextualItemMock.condition.andReturn(true);
        contextualItemMock.icon = 'icon1.png';

        var obj = {
            'ComponentType1': [contextualItemMock]
        };

        contextualMenuService.addItems(obj);
        expect(contextualMenuService.getContextualMenuItems({
            componentId: 'ComponentId1',
            componentType: 'ComponentType1',
            iLeftBtns: 1,
            element: element
        }).leftMenuItems).toEqualData(obj.ComponentType1);
        expect(contextualItemMock.condition).toHaveBeenCalledWith({
            componentId: 'ComponentId1',
            componentType: 'ComponentType1',
            element: element
        });

    });

    it('removeItemByKey WILL remove all the items with the provided key WHEN the condition is called', function() {
        contextualMenuService.addItems({
            'ComponentType1': [{
                key: 'key1',
                i18nKey: 'ICON1',
                icon: 'icon1.png'
            }, {
                key: 'key2',
                i18nKey: 'ICON2',
                icon: 'icon2.png'
            }, {
                key: 'key3',
                i18nKey: 'ICON3',
                icon: 'icon3.png'
            }],
            'ComponentType2': [{
                key: 'key1',
                i18nKey: 'ICON1',
                icon: 'icon1.png'
            }, {
                key: 'key2',
                i18nKey: 'ICON2',
                icon: 'icon2.png'
            }, {
                key: 'key5',
                i18nKey: 'ICON5',
                icon: 'icon5.png'
            }]
        });

        contextualMenuService.removeItemByKey('key2');

        expect(contextualMenuService.getContextualMenuItems({
            componentId: 'ComponentId1',
            componentType: 'ComponentType1',
            iLeftBtns: 3
        }).leftMenuItems).toEqualData([{
            key: 'key1',
            i18nKey: 'ICON1',
            icon: 'icon1.png'
        }, {
            key: 'key3',
            i18nKey: 'ICON3',
            icon: 'icon3.png'
        }]);

        expect(contextualMenuService.getContextualMenuItems({
            componentId: 'ComponentId5',
            componentType: 'ComponentType2',
            iLeftBtns: 3
        }).leftMenuItems).toEqualData([{
            key: 'key1',
            i18nKey: 'ICON1',
            icon: 'icon1.png'
        }, {
            key: 'key5',
            i18nKey: 'ICON5',
            icon: 'icon5.png'
        }]);
    });

    it('removeItemByKey WILL not do anything WHEN the provided key does not match an item', function() {
        contextualMenuService.addItems({
            'ComponentType1': [{
                key: 'key1',
                i18nKey: 'ICON1',
                icon: 'icon1.png'
            }, {
                key: 'key2',
                i18nKey: 'ICON2',
                icon: 'icon2.png'
            }, {
                key: 'key3',
                i18nKey: 'ICON3',
                icon: 'icon3.png'
            }],
            'ComponentType2': [{
                key: 'key1',
                i18nKey: 'ICON1',
                icon: 'icon1.png'
            }, {
                key: 'key2',
                i18nKey: 'ICON2',
                icon: 'icon2.png'
            }, {
                key: 'key5',
                i18nKey: 'ICON5',
                icon: 'icon5.png'
            }]
        });

        contextualMenuService.removeItemByKey('key10');

        expect(contextualMenuService.getContextualMenuItems({
            componentId: 'ComponentId1',
            componentType: 'ComponentType1',
            iLeftBtns: 3
        }).leftMenuItems).toEqualData([{
            key: 'key1',
            i18nKey: 'ICON1',
            icon: 'icon1.png'
        }, {
            key: 'key2',
            i18nKey: 'ICON2',
            icon: 'icon2.png'
        }, {
            key: 'key3',
            i18nKey: 'ICON3',
            icon: 'icon3.png'
        }]);

        expect(contextualMenuService.getContextualMenuItems({
            componentId: 'ComponentId5',
            componentType: 'ComponentType2',
            iLeftBtns: 3
        }).leftMenuItems).toEqualData([{
            key: 'key1',
            i18nKey: 'ICON1',
            icon: 'icon1.png'
        }, {
            key: 'key2',
            i18nKey: 'ICON2',
            icon: 'icon2.png'
        }, {
            key: 'key5',
            i18nKey: 'ICON5',
            icon: 'icon5.png'
        }]);
    });
});
