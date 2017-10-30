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

//disabled as we need to revisit synchronization and much of this test should be e2e
xdescribe(
    'Unit integration test of synchronize directive',
    function() {
        var $compile, $rootScope, $timeout, parentScope, directiveScope, element, smarteditComponentType, smarteditComponentId, $httpBackend, response, $q;

        beforeEach(function() {
            angular.module('translationServiceModule', []);
            angular.module('restServiceFactoryModule', []);
        });

        beforeEach(module('ui.bootstrap'));
        beforeEach(module('synchronizeDecorator'));
        beforeEach(module('pascalprecht.translate'));
        beforeEach(customMatchers);

        beforeEach(module('restServiceFactoryModule', function($provide) {
            restService = jasmine.createSpyObj('restService', ['get', 'save']);
            restService.get.andCallFake(function() {
                return $q.when({
                    status: true
                });
            });
            restServiceFactory = jasmine.createSpyObj('restServiceFactory', ['get']);
            restServiceFactory.get.andCallFake(function() {
                return restService;
            });

            $provide.value('restServiceFactory', restServiceFactory);
        }));

        function setRestServiceSyncResponse(status) {
            restService.get.andCallFake(function() {
                return $q.when({
                    status: status
                });
            });
        }

        // Store references to $rootScope and $compile so they are available to all tests in this describe block
        beforeEach(inject(function(_$compile_, _$rootScope_, _$q_, _$httpBackend_, _$timeout_) {

            $timeout = _$timeout_;
            $httpBackend = _$httpBackend_;
            $httpBackend.whenGET('somePath/en_US').respond({
                'synchronize.notok.tooltip': 'click here to synchronize',
                'synchronize.ok.tooltip': 'this component is already synchronized'
            });

            smarteditComponentType = "Paragraph";
            smarteditComponentId = "theId";

            $compile = _$compile_;
            $rootScope = _$rootScope_;
            parentScope = $rootScope.$new();
            parentScope.active = false;
            directiveScope = parentScope.$new();

            $q = _$q_;

            element = angular.element("<div class=\"synchronize\" data-active=\"active\" data-smartedit-component-id=\"" + smarteditComponentId + "\" data-smartedit-component-type=\"" + smarteditComponentType + "\">initialContent</div>");
            $compile(element)(directiveScope);

            // fire all the watches, so the scope expressions will be evaluated
            $rootScope.$digest();

            expect(element.scope()).toBe(directiveScope);
        }));

        it('synchronization does not show anything except transcluded content if mouse not entered', function() {

            $rootScope.$digest();
            setRestServiceSyncResponse(false);
            var images = element.find('img');
            expect(images.length).toBe(0);
            var content = element.find('span');
            expect(content.length).toBe(1);
            expect(content.text()).toBe("initialContent");

        });

        it('mouseleave from previously entered element hides the sync icon', function() {
            setRestServiceSyncResponse(false);

            element.find('div').trigger('mouseenter');

            var images = element.find('img');
            expect(images.length).toBe(1);

            element.find('div').trigger('mouseleave');

            $timeout.flush();
            images = element.find('img');
            expect(images.length).toBe(0);

        });

        //Disabled
        it('initialized with sync status KO and content is transcluded and clicking on icon does change sync status to OK', function() {
            setRestServiceSyncResponse(false);

            element.find('div').trigger('mouseenter');

            var images = element.find('img');
            expect(images.length).toBe(1);
            expect(images.attr('title')).toBe('click here to synchronize');
            expect(images.attr('src')).toBe('/cmssmartedit/images/synchronization_notok.gif');
            var content = element.find('span');
            expect(content.length).toBe(1);
            expect(content.text()).toBe("initialContent");

            setRestServiceSyncResponse(true);
            images.click();
            images = element.find('img');
            expect(images.length).toBe(1);
            expect(images.attr('title')).toBe('this component is already synchronized');
            expect(images.attr('src')).toBe('/cmssmartedit/images/synchronization_ok.gif');

        });

        it('initialized with sync status OK and content is transcluded and clicking on icon does not change sync status', function() {
            setRestServiceSyncResponse(true);

            element.find('div').trigger('mouseenter');

            var images = element.find('img');
            expect(images.length).toBe(1);
            expect(images.attr('title')).toBe('this component is already synchronized');
            expect(images.attr('src')).toBe('/cmssmartedit/images/synchronization_ok.gif');
            var content = element.find('span');
            expect(content.length).toBe(1);
            expect(content.text()).toBe("initialContent");

            setRestServiceSyncResponse(false);
            images.click();
            $httpBackend.verifyNoOutstandingRequest();
            images = element.find('img');
            expect(images.length).toBe(1);
            expect(images.attr('title')).toBe('this component is already synchronized');
            expect(images.attr('src')).toBe('/cmssmartedit/images/synchronization_ok.gif');
        });

    });
