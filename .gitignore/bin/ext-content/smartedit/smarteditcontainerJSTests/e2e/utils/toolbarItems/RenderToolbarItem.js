angular.module('RenderToolbarItemModule', ['toolbarModule', 'renderServiceModule'])
    .run(function(toolbarServiceFactory, renderService) {
        var toolbarService = toolbarServiceFactory.getToolbarService('experienceSelectorToolbar');
        toolbarService.addItems([{
            key: 'toolbar.action.render.component',
            type: 'ACTION',
            i18nKey: 'toolbar.action.render.component',
            priority: 1,
            callback: function() {
                renderService.renderComponent("component1", "componentType1");
            },
            icons: {
                'default': 'render.png'
            }
        }, {
            key: 'toolbar.action.render.slot',
            type: 'ACTION',
            i18nKey: 'toolbar.action.render.slot',
            priority: 2,
            callback: function() {
                renderService.renderSlots(["topHeaderSlot"]);
            },
            icons: {
                'default': 'render.png'
            }
        }]);
    });

angular.module('smarteditcontainer').requires.push('RenderToolbarItemModule');
