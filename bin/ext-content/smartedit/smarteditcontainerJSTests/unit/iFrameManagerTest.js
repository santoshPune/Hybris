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
describe('test iFrameManager class', function() {

    var $httpBackend, $rootScope, $q, $location, iFrameManager, deviceSupports, deviceOrientations, iframeMock, previewRESTService, htmlFetchService, sharedDataService, modalService, heartBeatService;
    var previewTicket = "previewTicket1";

    angular.module('translationServiceModule', []);

    beforeEach(module('sharedDataServiceModule', function($provide) {
        sharedDataService = jasmine.createSpyObj('sharedDataService', ["set", "get"]);
        $provide.value("sharedDataService", sharedDataService);

    }));

    beforeEach(module('iFrameManagerModule', function($provide) {

        var i18nMap = {
            'deviceorientation.vertical.label': 'Vertical',
            'deviceorientation.horizontal.label': 'Horizontal'
        };

        translateMock = function(key) {
            return {
                then: function(callback) {
                    return callback(i18nMap[key]);
                }

            };
        };
        $provide.value("$translate", translateMock);

        loadConfigManagerService = jasmine.createSpyObj('loadConfigManagerService', ["loadAsObject"]);
        $provide.value("loadConfigManagerService", loadConfigManagerService);

        restServiceFactory = jasmine.createSpyObj('restServiceFactory', ["get"]);

        previewRESTService = jasmine.createSpyObj('previewRESTService', ["save"]);
        htmlFetchService = jasmine.createSpyObj('htmlFetchService', ["get"]);

        $provide.value("LANDING_PAGE_PATH", "/");

        restServiceFactory.get.andCallFake(function(uri) {
            if (uri === 'thepreviewTicketUri') {
                return previewRESTService;
            } else {
                return htmlFetchService;
            }
        }); //configurations.previewTicketURI || PREVIEW_RESOURCE_URI
        $provide.value("restServiceFactory", restServiceFactory);

        modalService = jasmine.createSpyObj('modalService', ["open"]);
        $provide.value("modalService", modalService);

        heartBeatService = jasmine.createSpyObj('heartBeatService', ["resetTimer"]);
        $provide.value("heartBeatService", heartBeatService);
    }));

    beforeEach(customMatchers);

    beforeEach(inject(function(_$httpBackend_, _$http_, _$rootScope_, _$q_, _$location_, _iFrameManager_, _deviceSupports_, _deviceOrientations_) {
        $httpBackend = _$httpBackend_;
        $rootScope = _$rootScope_;
        $q = _$q_;
        htmlFetchService.get.andReturn($q.when());
        $location = _$location_;
        iFrameManager = _iFrameManager_;
        deviceSupports = _deviceSupports_;
        deviceOrientations = _deviceOrientations_;
        iframeMock = jasmine.createSpyObj('iframeMock', ["removeClass", "addClass", "css", "attr", "showWaitModal"]);
        iframeMock.removeClass.andReturn(iframeMock);
        iframeMock.addClass.andReturn(iframeMock);
        spyOn(iFrameManager, "_getIframe").andReturn(iframeMock);
        spyOn($location, "url").andReturn("");


        $httpBackend.whenGET("/failinghtml").respond(function() {
            return [404, null, {
                'Content-type': 'text/html'
            }];
        });

        $httpBackend.whenGET("/notfailinghtml").respond(function() {
            return [200];
        });

    }));


    it('iFrameManager WILL show a wait modal dialog', function() {

        iFrameManager.showWaitModal();
        expect(modalService.open).toHaveBeenCalledWith({
            templateUrl: 'web/common/core/services/waitDialog.html',
            cssClasses: 'ySEWaitDialog',
            controller: jasmine.any(Array)
        });

    });

    describe('_mustLoadAsSuch', function() {

        it('will return true if not currentLocation is set', function() {
            iFrameManager.setCurrentLocation(undefined);
            expect(iFrameManager._mustLoadAsSuch('/myurl')).toBe(true);
        });

        it('will return true if the currentLocation is the homePageOrPageFromPageList', function() {
            iFrameManager.setCurrentLocation('/profilepage');
            expect(iFrameManager._mustLoadAsSuch('/profilepage')).toBe(true);
        });

        it('will return true if the currentLocation has a cmsTicketId', function() {
            iFrameManager.setCurrentLocation('/profilepage?cmsTicketId=myticketID');
            expect(iFrameManager._mustLoadAsSuch('/otherpage')).toBe(true);
        });

        it('will return false if we have a currentLocation that is not the home page or a page from the page list, and doesn\'t have a cmsTicketID', function() {
            iFrameManager.setCurrentLocation('/randomURL');
            expect(iFrameManager._mustLoadAsSuch('/homePageOrPageFromPageList')).toBe(false);
        });

    });


    it('GIVEN that _mustLoadHasSuch has returned true WHEN I request to load a preview THEN the page will be loaded in preview mode', function() {
        // Arrange
        iFrameManager._mustLoadAsSuch = jasmine.createSpy().andReturn(true);
        spyOn(iFrameManager, 'load').andReturn();

        // Act
        iFrameManager.loadPreview("myurl", previewTicket);
        $rootScope.$digest();

        // Assert
        expect(iFrameManager.load).toHaveBeenCalledWith('myurl/previewServlet?cmsTicketId=previewTicket1');
    });


    it('GIVEN that _mustLoadHasSuch has returned false WHEN I request to load a preview THEN the page will be first loaded in preview mode, then we will load the currentLocation', function() {
        // Arrange
        iFrameManager._mustLoadAsSuch = jasmine.createSpy().andReturn(false);
        spyOn(iFrameManager, 'load').andReturn();

        // Act
        iFrameManager.setCurrentLocation('aLocation');
        iFrameManager.loadPreview("myurl", previewTicket);
        $rootScope.$digest();

        // Assert
        expect(restServiceFactory.get).toHaveBeenCalledWith('myurl/previewServlet?cmsTicketId=previewTicket1');
        expect(htmlFetchService.get).toHaveBeenCalled();
        expect(iFrameManager.load).toHaveBeenCalledWith('aLocation', true, 'myurl/previewServlet?cmsTicketId=previewTicket1');
    });


    it('GIVEN that loads is called with checkIfFailingHTML set to true WHEN the HTML is not failing THEN iframe will display the requested URL', function() {
        // Arrange/Act
        iFrameManager.load('/notfailinghtml', true, '/myhomepage');
        $httpBackend.flush();

        // Assert
        expect(iframeMock.attr).toHaveBeenCalledWith('src', '/notfailinghtml');
        expect(heartBeatService.resetTimer).toHaveBeenCalledWith(true);
    });

    it('GIVEN that loads is called with checkIfFailingHTML set to true WHEN the HTML is failing THEN iframe will display the homepage', function() {
        // Arrange/Act
        iFrameManager.load('/failinghtml', true, '/myhomepage');
        $httpBackend.flush();

        // Assert
        expect(iframeMock.attr).toHaveBeenCalledWith('src', '/myhomepage');
        expect(heartBeatService.resetTimer).toHaveBeenCalledWith(true);
    });


    it('iFrameManager load the expected url into the iframe and does not open wait dialog (open by storefront loading)', function() {
        // Arrange/Act
        iFrameManager.load("myurl");

        // Assert
        expect(iframeMock.attr).toHaveBeenCalledWith('src', 'myurl');
        expect(heartBeatService.resetTimer).toHaveBeenCalledWith(true);
        expect(modalService.open).not.toHaveBeenCalled();
        expect(htmlFetchService.get).not.toHaveBeenCalled();
    });

    it('iFrameManager loadPreview appends previewServlet suffix to the url and the preview ticket to the query string case 1', function() {
        // Arrange
        spyOn(iFrameManager, 'load').andReturn();

        // Act
        iFrameManager.loadPreview("myurl", previewTicket);

        // Assert
        expect(iFrameManager.load).toHaveBeenCalledWith('myurl/previewServlet?cmsTicketId=previewTicket1');
    });
    it('iFrameManager loadPreview appends previewServlet suffix to the url and the preview ticket to the query string case 2', function() {
        // Arrange
        spyOn(iFrameManager, 'load').andReturn();

        // Act
        iFrameManager.loadPreview("myurl/", previewTicket);

        // Assert
        expect(iFrameManager.load).toHaveBeenCalledWith('myurl/previewServlet?cmsTicketId=previewTicket1');
    });
    it('iFrameManager loadPreview appends previewServlet suffix to the url and the preview ticket to the query string case 3', function() {
        // Arrange
        spyOn(iFrameManager, 'load').andReturn();

        // Act
        iFrameManager.loadPreview("myurl?param1=value1", previewTicket);

        // Assert
        expect(iFrameManager.load).toHaveBeenCalledWith('myurl/previewServlet?param1=value1&cmsTicketId=previewTicket1');
    });
    it('iFrameManager loadPreview appends previewServlet suffix to the url and the preview ticket to the query string case 4', function() {
        // Arrange
        spyOn(iFrameManager, 'load').andReturn();

        // Act
        iFrameManager.loadPreview("myurl/?param1=value1", previewTicket);

        // Assert
        expect(iFrameManager.load).toHaveBeenCalledWith('myurl/previewServlet?param1=value1&cmsTicketId=previewTicket1');
    });

    it('iFrameManager getDeviceSupports returns the expected deviceSupports from factory', function() {

        expect(iFrameManager.getDeviceSupports()).toBe(deviceSupports);
    });

    it('iFrameManager getDeviceOrientations returns the expected deviceOrientations with label being translation of keys', function() {
        $rootScope.$digest();
        var deviceOrientations = iFrameManager.getDeviceOrientations();
        expect(deviceOrientations).toBe(deviceOrientations);

        expect(deviceOrientations).toEqualData([{
            orientation: 'vertical',
            key: 'deviceorientation.vertical.label',
            label: 'Vertical'
        }, {
            orientation: 'horizontal',
            key: 'deviceorientation.horizontal.label',
            label: 'Horizontal'
        }]);
    });

    it('apply on no arguments gives a full frame', function() {

        iFrameManager.apply(undefined, undefined);
        expect(iframeMock.removeClass).toHaveBeenCalled();
        expect(iframeMock.addClass).not.toHaveBeenCalled();
        expect(iframeMock.css).toHaveBeenCalledWith({
            width: '100%',
            height: '100%',
            display: 'block',
            margin: 'auto'
        });
    });

    it('apply device support with no orientation sets it to vertical', function() {

        iFrameManager.apply({
            width: 600,
            height: '100%'

        }, undefined);
        expect(iframeMock.removeClass).toHaveBeenCalled();
        expect(iframeMock.addClass).toHaveBeenCalledWith("device-vertical device-default");
        expect(iframeMock.css).toHaveBeenCalledWith({
            width: 600,
            height: '100%',
            display: 'block',
            margin: 'auto'
        });
    });

    it('apply device support with orientation applies this orientation', function() {

        iFrameManager.apply({
            height: 600,
            width: '100%'
        }, {
            orientation: 'horizontal',
            key: 'deviceorientation.horizontal.label',
        });
        expect(iframeMock.removeClass).toHaveBeenCalled();
        expect(iframeMock.addClass).toHaveBeenCalledWith("device-horizontal device-default");
        expect(iframeMock.css).toHaveBeenCalledWith({
            width: 600,
            height: '100%',
            display: 'block',
            margin: 'auto'
        });
    });


    it('GIVEN that an experience has been set WHEN I request to load a storefront THEN initializeCatalogPreview will call loadPreview with the right parameters', function() {
        // Arrange
        spyOn(iFrameManager, 'loadPreview');

        var experience = {
            siteDescriptor: {
                name: "some name",
                previewUrl: "/someURI/?someSite=site",
                uid: "some uid"
            },
            catalogDescriptor: {
                name: "some cat name",
                catalogId: "some cat uid",
                catalogVersion: "some cat version"
            },
            languageDescriptor: {
                isocode: "some language isocode",
            },
            time: null
        };

        var configurations = {
            previewTicketURI: 'thepreviewTicketUri'
        };

        var preview = {
            ticketId: 'fgwerwertwertwer',
            resourcePath: 'returnedResourcePath/?someSite=site'
        };

        var previewURL = configurations.domain + experience.siteDescriptor.previewUrl;
        var expectedPreviewTicketURI = "thepreviewTicketUri";

        sharedDataService.get.andReturn($q.when(experience));
        loadConfigManagerService.loadAsObject.andReturn($q.when(configurations));
        previewRESTService.save.andReturn($q.when(preview));

        // Act
        iFrameManager.initializeCatalogPreview();
        $rootScope.$digest();

        // Assert
        expect(iFrameManager.loadPreview).toHaveBeenCalledWith('returnedResourcePath/?someSite=site', preview.ticketId);
        expect(restServiceFactory.get).toHaveBeenCalledWith(expectedPreviewTicketURI);
        expect(previewRESTService.save).toHaveBeenCalledWith({
            catalog: experience.catalogDescriptor.catalogId,
            catalogVersion: experience.catalogDescriptor.catalogVersion,
            language: experience.languageDescriptor.isocode,
            resourcePath: previewURL
        });
        expect(window.smartEditBootstrapped).toEqualData({});

        expect(sharedDataService.set).toHaveBeenCalledWith('preview', {
            previewTicketId: preview.ticketId,
            resourcePath: preview.resourcePath
        });
    });

    it('GIVEN that no experience has been set WHEN I request to load a storefront THEN initializeCatalogPreview will redirect to landing page', function() {
        // Arrange
        spyOn(iFrameManager, 'loadPreview');
        sharedDataService.get.andReturn($q.when(null));

        // Act
        iFrameManager.initializeCatalogPreview();
        $rootScope.$digest();

        // Assert
        expect($location.url).toHaveBeenCalledWith('/');
        expect(iFrameManager.loadPreview).not.toHaveBeenCalled();
    });

});
