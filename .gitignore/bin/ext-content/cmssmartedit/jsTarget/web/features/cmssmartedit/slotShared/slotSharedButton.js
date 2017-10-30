angular.module('slotSharedButtonModule', ['slotSharedServiceModule'])
    .controller('slotSharedButtonController', ['slotSharedService', '$scope', '$timeout', function(slotSharedService, $scope, $timeout) {
        this.sharedOffImageUrl = '/cmssmartedit/images/shared_slot_menu_off.png';
        this.sharedOnImageUrl = '/cmssmartedit/images/shared_slot_menu_on.png';
        this.slotSharedFlag = false;
        this.isPopupOpened = false;
        this.buttonName = 'slotSharedButton';

        slotSharedService.isSlotShared(this.slotId).then(function(result) {
            this.slotSharedFlag = result;
        }.bind(this));

        this.positionHiddenComponentList = function() {
            $timeout(function() {
                var buttonElement = $('#sharedSlotButton-' + this.slotId);
                var dropdownElement = $('#shared-slot-list-' + this.slotId);
                var overflow = dropdownElement.offset().left + dropdownElement.width() - $('body').width();
                if (overflow >= 0) {
                    dropdownElement.offset({
                        left: buttonElement.offset().left + buttonElement.width() - dropdownElement.width() - 45,
                        top: dropdownElement.offset().top
                    });
                }
            }.bind(this));
        };

        this.openPopup = function($event) {
            $event.preventDefault();
            $event.stopPropagation();

            this.positionHiddenComponentList();
            this.isPopupOpened = true;
            this.setRemainOpen({
                button: this.buttonName,
                remainOpen: true
            });
        };
    }])
    .directive('slotSharedButton', function() {
        return {
            templateUrl: 'web/features/cmssmartedit/slotShared/slotSharedButtonTemplate.html',
            restrict: 'E',
            controller: 'slotSharedButtonController',
            controllerAs: 'ctrl',
            scope: {},
            bindToController: {
                setRemainOpen: '&',
                active: '=',
                slotId: '@'
            }
        };
    });
