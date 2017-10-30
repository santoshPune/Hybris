describe('createPageService - ', function() {
    var createPageService, restServiceFactory, restPageService;
    var SERVICE_LOCATION = "Some Location";

    beforeEach(function() {
        angular.module('restServiceFactoryModule', []);
        angular.module('resourceLocationsModule', []);

        module('createPageServiceModule', function($provide) {
            $provide.constant('PAGES_LIST_RESOURCE_URI', SERVICE_LOCATION);

            restServiceFactory = jasmine.createSpyObj('restServiceFactory', ['get']);
            $provide.value('restServiceFactory', restServiceFactory);

            restPageService = jasmine.createSpyObj('restPageService', ['save']);
            restServiceFactory.get.andReturn(restPageService);
        });

        inject(function(_createPageService_) {
            createPageService = _createPageService_;
        });
    });


    it('WHEN the createPageService is created THEN it will create a rest service with the right location', function() {
        // Assert
        expect(restServiceFactory.get).toHaveBeenCalledWith(SERVICE_LOCATION);
    });

    it('WHEN createPage is called THEN the  rest service is called with the expected payload', function() {
        // Arrange
        var siteID = "some site";
        var catalogID = "some catalog";
        var catalogVersion = "some version";
        var page = {
            value: 'some value'
        };

        var expectedPayload = {
            value: 'some value',
            siteUID: siteID,
            catalogId: catalogID,
            catalogVersion: catalogVersion
        };

        // Act
        createPageService.createPage(siteID, catalogID, catalogVersion, page);

        // Assert
        expect(restPageService.save).toHaveBeenCalledWith(expectedPayload);
    });
});
