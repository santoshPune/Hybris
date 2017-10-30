NG_DOCS={
  "sections": {
    "smartEdit": "SmartEdit",
    "smartEditContainer": "SmartEdit Container"
  },
  "pages": [
    {
      "section": "smartEdit",
      "id": "authenticationModule",
      "shortName": "authenticationModule",
      "type": "overview",
      "moduleName": "authenticationModule",
      "shortDescription": "The authenticationModule",
      "keywords": "allows application authenticate authentication authenticationmodule entry logout management module overview points resources service smartedit"
    },
    {
      "section": "smartEdit",
      "id": "authenticationModule.object:DEFAULT_AUTH_MAP",
      "shortName": "DEFAULT_AUTH_MAP",
      "type": "service",
      "moduleName": "authenticationModule",
      "shortDescription": "The default authentication map contains the entry points to use before an authentication map",
      "keywords": "authentication authenticationmodule configuration default entry loaded map object points service smartedit"
    },
    {
      "section": "smartEdit",
      "id": "authenticationModule.object:DEFAULT_CREDENTIALS_MAP",
      "shortName": "DEFAULT_CREDENTIALS_MAP",
      "type": "service",
      "moduleName": "authenticationModule",
      "shortDescription": "The default credentials map contains the credentials to use before an authentication map",
      "keywords": "authentication authenticationmodule configuration credentials default loaded map object service smartedit"
    },
    {
      "section": "smartEdit",
      "id": "authenticationModule.service:authenticationService",
      "shortName": "authenticationService",
      "type": "service",
      "moduleName": "authenticationModule",
      "shortDescription": "The authenticationService is used to authenticate and logout from SmartEdit.",
      "keywords": "access allows application assigned associated authenticate authenticated authenticates authentication authenticationmap authenticationmodule authenticationservice compare current currently default default_authentication_entry_point entered entry entrypoint expiry expression filterentrypoints flag flow identifying indicate indicates isauthenticated isauthentrypoint key landing logout management map maps matching method object point points promise provided re-authenticated re-authentication redirects registered regular relevant removes requested resolves resource resourcelocationsmodule resources retrieve returns service setreauthinprogress shareddataservice shareddataservicemodule smartedit stored successful suitable token tokens true uri url user"
    },
    {
      "section": "smartEdit",
      "id": "authorizationModule.AuthorizationService",
      "shortName": "authorizationModule.AuthorizationService",
      "type": "service",
      "moduleName": "authorizationModule",
      "shortDescription": "Service that makes calls to the permissions REST API to expose permissions for a given hybris type.",
      "keywords": "api authorizationmodule authorizationservice calls canperformoperation check confirm expose hybris method operation permission permissions rest service smartedit type"
    },
    {
      "section": "smartEdit",
      "id": "authorizationModule.authorizationService",
      "shortName": "authorizationModule.authorizationService",
      "type": "overview",
      "moduleName": "authorizationModule.authorizationService",
      "shortDescription": "The authorization module provides services to fetch operation permissions for hybris types existing in the platform.",
      "keywords": "apis authorization authorizationmodule authorizationservice backend existing fetch hybris module operation order overview permissions platform poll restservicefactorymodule services smartedit types"
    },
    {
      "section": "smartEdit",
      "id": "authorizationModule.directive:hasOperationPermission",
      "shortName": "hasOperationPermission",
      "type": "directive",
      "moduleName": "authorizationModule",
      "shortDescription": "Authorization HTML mark-up that will remove elements from the DOM if the user does not have authorization defined",
      "keywords": "authorization authorizationmodule authorizationservice check current defined directive dom elements has-operation-permission html hybris input key mark-up parameter permission remove service smartedit type user validate"
    },
    {
      "section": "smartEdit",
      "id": "compileHtmlModule",
      "shortName": "compileHtmlModule",
      "type": "overview",
      "moduleName": "compileHtmlModule",
      "shortDescription": "The compileHtmlModule",
      "keywords": "compile compilehtmlmodule directive evaluate html markup overview smartedit"
    },
    {
      "section": "smartEdit",
      "id": "compileHtmlModule.directive:compileHtml",
      "shortName": "compileHtml",
      "type": "directive",
      "moduleName": "compileHtmlModule",
      "shortDescription": "Directive responsible for evaluating and compiling HTML markup.",
      "keywords": "compile-html compiled compilehtmlmodule compiling data-ng-click directive evaluated evaluating html injectedcontext item markup onlink path property responsible smartedit string"
    },
    {
      "section": "smartEdit",
      "id": "componentHandlerServiceModule",
      "shortName": "componentHandlerServiceModule",
      "type": "overview",
      "moduleName": "componentHandlerServiceModule",
      "shortDescription": "this module aims at handling smartEdit components both on the original storefront and the smartEdit overlay",
      "keywords": "aims componenthandlerservicemodule components handling module original overlay overview smartedit storefront"
    },
    {
      "section": "smartEdit",
      "id": "componentHandlerServiceModule.COMPONENT_CLASS",
      "shortName": "componentHandlerServiceModule.COMPONENT_CLASS",
      "type": "object",
      "moduleName": "componentHandlerServiceModule",
      "shortDescription": "the css class of the smartEdit components as per contract with the storefront",
      "keywords": "class component_class componenthandlerservicemodule components contract css object smartedit storefront"
    },
    {
      "section": "smartEdit",
      "id": "componentHandlerServiceModule.componentHandlerService",
      "shortName": "componentHandlerServiceModule.componentHandlerService",
      "type": "service",
      "moduleName": "componentHandlerServiceModule",
      "shortDescription": "The service provides convenient methods to get DOM references of smartEdit components both in the original laye rof the storefornt and in the smartEdit overlay",
      "keywords": "api applicable application child class component componenthandlerservice componenthandlerservicemodule components container container_id_attribute container_type_attribute contentslot contract convenient css cssclass defaults direct div dom element extracts fetched handler id_attribute identified iframe invoked jquery laye layer loaded matching method methods methodsof_getallcomponentsselector methodsof_getallslotsselector methodsof_getcomponent methodsof_getcomponentinoverlay methodsof_getcomponentunderslot methodsof_getfromselector methodsof_getid methodsof_getoriginalcomponent methodsof_getoriginalcomponentwithinslot methodsof_getoverlay methodsof_getpageuid methodsof_getparent methodsof_getparentslotforcomponent methodsof_getslotoperationrelatedid methodsof_getslotoperationrelatedtype methodsof_gettype methodsof_setid methodsof_settype object operations optional original overlay pageuid parameter parent perform references relevant represents resides restrict retrieves rof search searched selector service set sets slot slotid smae-layer smartedit smarteditcomponentid smarteditcomponenttype smarteditslotid storefornt storefront string type type_attribute typically wrapper wrapping"
    },
    {
      "section": "smartEdit",
      "id": "componentHandlerServiceModule.CONTAINER_ID_ATTRIBUTE",
      "shortName": "componentHandlerServiceModule.CONTAINER_ID_ATTRIBUTE",
      "type": "object",
      "moduleName": "componentHandlerServiceModule",
      "shortDescription": "the id attribute of the smartEdit container, when applicable, as per contract with the storefront",
      "keywords": "applicable attribute componenthandlerservicemodule container container_id_attribute contract object smartedit storefront"
    },
    {
      "section": "smartEdit",
      "id": "componentHandlerServiceModule.CONTAINER_TYPE_ATTRIBUTE",
      "shortName": "componentHandlerServiceModule.CONTAINER_TYPE_ATTRIBUTE",
      "type": "object",
      "moduleName": "componentHandlerServiceModule",
      "shortDescription": "the type attribute of the smartEdit container, when applicable, as per contract with the storefront",
      "keywords": "applicable attribute componenthandlerservicemodule container container_type_attribute contract object smartedit storefront type"
    },
    {
      "section": "smartEdit",
      "id": "componentHandlerServiceModule.CONTENT_SLOT_TYPE",
      "shortName": "componentHandlerServiceModule.CONTENT_SLOT_TYPE",
      "type": "object",
      "moduleName": "componentHandlerServiceModule",
      "shortDescription": "the type value of the smartEdit slots as per contract with the storefront",
      "keywords": "componenthandlerservicemodule content_slot_type contract object slots smartedit storefront type"
    },
    {
      "section": "smartEdit",
      "id": "componentHandlerServiceModule.ID_ATTRIBUTE",
      "shortName": "componentHandlerServiceModule.ID_ATTRIBUTE",
      "type": "object",
      "moduleName": "componentHandlerServiceModule",
      "shortDescription": "the id attribute of the smartEdit components as per contract with the storefront",
      "keywords": "attribute componenthandlerservicemodule components contract id_attribute object smartedit storefront"
    },
    {
      "section": "smartEdit",
      "id": "componentHandlerServiceModule.OVERLAY_COMPONENT_CLASS",
      "shortName": "componentHandlerServiceModule.OVERLAY_COMPONENT_CLASS",
      "type": "object",
      "moduleName": "componentHandlerServiceModule",
      "shortDescription": "the css class of the smartEdit component clones copied to the storefront overlay",
      "keywords": "class clones component componenthandlerservicemodule copied css object overlay overlay_component_class smartedit storefront"
    },
    {
      "section": "smartEdit",
      "id": "componentHandlerServiceModule.OVERLAY_ID",
      "shortName": "componentHandlerServiceModule.OVERLAY_ID",
      "type": "object",
      "moduleName": "componentHandlerServiceModule",
      "shortDescription": "the identifier of the overlay placed in front of the storefront to where all smartEdit component decorated clones are copied.",
      "keywords": "clones component componenthandlerservicemodule copied decorated front identifier object overlay overlay_id smartedit storefront"
    },
    {
      "section": "smartEdit",
      "id": "componentHandlerServiceModule.SMARTEDIT_ATTRIBUTE_PREFIX",
      "shortName": "componentHandlerServiceModule.SMARTEDIT_ATTRIBUTE_PREFIX",
      "type": "object",
      "moduleName": "componentHandlerServiceModule",
      "shortDescription": "If the storefront needs to expose more attributes than the minimal contract, these attributes must be prefixed with this constant value",
      "keywords": "attributes componenthandlerservicemodule constant contract expose minimal object prefixed smartedit smartedit_attribute_prefix storefront"
    },
    {
      "section": "smartEdit",
      "id": "componentHandlerServiceModule.TYPE_ATTRIBUTE",
      "shortName": "componentHandlerServiceModule.TYPE_ATTRIBUTE",
      "type": "object",
      "moduleName": "componentHandlerServiceModule",
      "shortDescription": "the type attribute of the smartEdit components as per contract with the storefront",
      "keywords": "attribute componenthandlerservicemodule components contract object smartedit storefront type type_attribute"
    },
    {
      "section": "smartEdit",
      "id": "contextualMenuServiceModule.ContextualMenuService",
      "shortName": "contextualMenuServiceModule.ContextualMenuService",
      "type": "service",
      "moduleName": "contextualMenuServiceModule",
      "shortDescription": "The ContextualMenuService is used to add contextual menu items for each component.",
      "keywords": "action activate activated add additems alert ant-like applicable application applied array arrays assigned banner call callback called classes clicking code compares component component-type componentid components componenttype condition configuration container containerid containertype content contextual contextual_menu contextualmenuitemsmap contextualmenuservice contextualmenuservicemodule converts css decorator democlass determine display displayclass displayed dom edit element entry example excludes full function generated getcontextualmenubytype getcontextualmenuitems holds i18nkey icon iconidle iconnonidle identified identifies idle ileftbtns includes input invoked item itemkey items key key-value leftmenuitems limit list localize location map mapping maps match matched menu menus menuservice message method module moremenuitems names non-idle number object optional options pair pass payload performed png properties provided regex regexpfactory remove removeitembykey removes required responsive retrieved return returned returns sample selected service set simple size slot slotid smaller smallicon smartedit specific style translated type types usage valid version visible vlide wildcard wire wrapping"
    },
    {
      "section": "smartEdit",
      "id": "contextualMenuServiceModule.contextualMenuService",
      "shortName": "contextualMenuServiceModule.contextualMenuService",
      "type": "service",
      "moduleName": "contextualMenuServiceModule",
      "shortDescription": "The contextual menu service factory creates an instance of the  ContextualMenuService",
      "keywords": "component contextual contextualmenuservice contextualmenuservicemodule creates factory instance loaded menu service smartedit time type"
    },
    {
      "section": "smartEdit",
      "id": "dateTimePickerModule",
      "shortName": "dateTimePickerModule",
      "type": "overview",
      "moduleName": "dateTimePickerModule",
      "shortDescription": "The dateTimePickerModule",
      "keywords": "datetimepicker datetimepickerlocalizationservice datetimepickermodule directive displaying localize module open opened overview picker service smartedit time tooling"
    },
    {
      "section": "smartEdit",
      "id": "dateTimePickerModule.directive:dateTimePicker",
      "shortName": "dateTimePicker",
      "type": "directive",
      "moduleName": "dateTimePickerModule",
      "shortDescription": "The dateTimePicker",
      "keywords": "datetimepicker datetimepickermodule directive smartedit"
    },
    {
      "section": "smartEdit",
      "id": "dateTimePickerModule.object: tooltipsMap",
      "shortName": "tooltipsMap",
      "type": "object",
      "moduleName": "dateTimePickerModule",
      "shortDescription": "Contains a map of all tooltips to be localized in the date time picker",
      "keywords": "datetimepickermodule localized map object picker smartedit time tooltips tooltipsmap"
    },
    {
      "section": "smartEdit",
      "id": "dateTimePickerModule.object:resolvedLocaleToMomentLocaleMap",
      "shortName": "resolvedLocaleToMomentLocaleMap",
      "type": "object",
      "moduleName": "dateTimePickerModule",
      "shortDescription": "Contains a map of all inconsistent locales ISOs between SmartEdit and MomentJS",
      "keywords": "datetimepickermodule inconsistent isos locales map momentjs object smartedit"
    },
    {
      "section": "smartEdit",
      "id": "dateTimePickerModule.service:dateTimePickerLocalizationService",
      "shortName": "dateTimePickerLocalizationService",
      "type": "service",
      "moduleName": "dateTimePickerModule",
      "shortDescription": "The dateTimePickerLocalizationService is responsible for both localizing the date time picker as well as the tooltips",
      "keywords": "datetimepickerlocalizationservice datetimepickermodule localizing picker responsible service smartedit time tooltips"
    },
    {
      "section": "smartEdit",
      "id": "decoratorServiceModule.service:decoratorService",
      "shortName": "decoratorService",
      "type": "service",
      "moduleName": "decoratorServiceModule",
      "shortDescription": "This service enables and disables decorators. It also maps decorators to SmartEdit component types–regardless if they are enabled or disabled.",
      "keywords": "activated active addmappings ant-like array bound card component componenttype decorated decorator decorator1 decorator2 decorator3 decorator4 decorator5 decoratorkey decorators decoratorservice decoratorservicemodule depends disable disabled disables eligible enable enabled enables exact expression full getdecoratorsforcomponent group identified identifies key key-map list map maps matching method methods_addmappings methods_enable myexacttype pattern perspective referenced regular retrieved retrieves returns service smartedit type types uniquely wild"
    },
    {
      "section": "smartEdit",
      "id": "dragAndDropServiceInterfaceModule.DragAndDropServiceInterface",
      "shortName": "dragAndDropServiceInterfaceModule.DragAndDropServiceInterface",
      "type": "service",
      "moduleName": "dragAndDropServiceInterfaceModule",
      "shortDescription": "Provides an abstract extensible drag and drop service. Used to manage and perform actions to either the SmartEdit",
      "keywords": "abstract actions application callback class configuration container drag draganddropserviceinterface draganddropserviceinterfacemodule dragged dragging drop dropcallback dropped element enters exits extended extensible handle identify iframe instantiated interface items jquery locations manage method mouse outcallback overcallback perform register scope selector selectot serves service smartedit sortable sourceselector startcallback starts targetselector"
    },
    {
      "section": "smartEdit",
      "id": "eventServiceModule",
      "shortName": "eventServiceModule",
      "type": "overview",
      "moduleName": "eventServiceModule",
      "shortDescription": "eventServiceModule contains an event service which is supported by the SmartEdit gatewayFactory to propagate events between SmartEditContainer and SmartEdit.",
      "keywords": "event events eventservicemodule gatewayfactory gatewayfactorymodule overview propagate service smartedit smarteditcontainer supported"
    },
    {
      "section": "smartEdit",
      "id": "eventServiceModule.EVENT_SERVICE_MODE_ASYNCH",
      "shortName": "eventServiceModule.EVENT_SERVICE_MODE_ASYNCH",
      "type": "object",
      "moduleName": "eventServiceModule",
      "shortDescription": "A constant used in the constructor of the Event Service to specify asynchronous event transmission.",
      "keywords": "asynchronous constant event event_service_mode_asynch eventservicemodule object service smartedit transmission"
    },
    {
      "section": "smartEdit",
      "id": "eventServiceModule.EVENT_SERVICE_MODE_SYNCH",
      "shortName": "eventServiceModule.EVENT_SERVICE_MODE_SYNCH",
      "type": "object",
      "moduleName": "eventServiceModule",
      "shortDescription": "A constant that is used in the constructor of the Event Service to specify synchronous event transmission.",
      "keywords": "constant event event_service_mode_synch eventservicemodule object service smartedit synchronous transmission"
    },
    {
      "section": "smartEdit",
      "id": "eventServiceModule.EVENTS",
      "shortName": "eventServiceModule.EVENTS",
      "type": "object",
      "moduleName": "eventServiceModule",
      "shortDescription": "Events that are fired/handled in the SmartEdit application",
      "keywords": "application events eventservicemodule fired object smartedit"
    },
    {
      "section": "smartEdit",
      "id": "eventServiceModule.EventService",
      "shortName": "eventServiceModule.EventService",
      "type": "service",
      "moduleName": "eventServiceModule",
      "shortDescription": "The event service is used to transmit events synchronously or asynchronously. It also contains options to send",
      "keywords": "asynchronous asynchronously constant constants data defaultmode depending event event_service_mode_asynch event_service_mode_synch eventid events eventservice eventservicemodule handlers identifier method mode options payload register send sendevent service set sets smartedit synchronous synchronously transmission transmit unregister"
    },
    {
      "section": "smartEdit",
      "id": "ExperienceInterceptorModule.experienceInterceptor",
      "shortName": "ExperienceInterceptorModule.experienceInterceptor",
      "type": "service",
      "moduleName": "ExperienceInterceptorModule",
      "shortDescription": "A HTTP request interceptor which intercepts all &#39;cmswebservices/catalogs&#39; requests and adds the current catalog and version",
      "keywords": "$httpprovider adding adds angularjs array called catalog cmswebservices config configuration current current_context_catalog current_context_catalog_version data define dependencies details experience experienceinterceptor experienceinterceptormodule factories factory headers holds http https initialization injected interceptor interceptors intercepts method methods note object org passed preview promise registered request requests retrieve returns service set shared smartedit stored uri url variables version versions"
    },
    {
      "section": "smartEdit",
      "id": "featureInterfaceModule",
      "shortName": "featureInterfaceModule",
      "type": "overview",
      "moduleName": "featureInterfaceModule",
      "keywords": "featureinterfacemodule overview smartedit"
    },
    {
      "section": "smartEdit",
      "id": "featureInterfaceModule.service:FeatureServiceInterface",
      "shortName": "FeatureServiceInterface",
      "type": "service",
      "moduleName": "featureInterfaceModule",
      "shortDescription": "The interface stipulates how to register features in the SmartEdit application and the SmartEdit container.",
      "keywords": "_registeraliases action activate addcontextualmenubutton adddecorator additems addmappings addtoolbaritem ant-like api applicable application applied bound button buttons callback callbacks classes clicked clicking component componentid components componenttype concrete condition configuration container containerid containertype content contextual contextualmenuservice contextualmenuservicemodule cross css deal decorator decorators decoratorservice decoratorservicemodule default defined delegates description descriptioni18nkey disable disabled disablingcallback displayclass displayed dom element eligible enable enabled enablingcallback entry event expression feature featureinterfacemodule features frame full function functions gateway gatewayfactory gatewayfactorymodule handler hold holding html hybrid_action i18n icon iconidle iconnonidle icons identified identifies idle image images implementation include instance instances interface invocation invoke invoked item items key keythe l18n list location mappings match meant menu method methods methods_additems methods_disable methods_enable methods_register namei18nkey needed non-idle object optional options order parameter performed perspective perspectiveinterfacemodule perspectiveserviceinterface point reference regexpkey register registered registers registry regular removed representing represents required respective selected selects separate service simplified slot slotid smaller smallicon smartedit smarteditcontainer stand stipulates stores strict style template templates toolbar toolbarid toolbarinterfacemodule toolbarserviceinterface tooltip translated triggered type types unique uniquely url urls user version web wildcard wrapper wrapping"
    },
    {
      "section": "smartEdit",
      "id": "FetchDataHandlerInterfaceModule.FetchDataHandlerInterface",
      "shortName": "FetchDataHandlerInterfaceModule.FetchDataHandlerInterface",
      "type": "service",
      "moduleName": "FetchDataHandlerInterfaceModule",
      "shortDescription": "Interface describing the contract of a fetchDataHandler fetched through dependency injection by the",
      "keywords": "contract defined dependency describing descriptor dropdowns eligible entities entity fetch fetchdatahandler fetchdatahandlerinterface fetchdatahandlerinterfacemodule fetched field findbymask genericeditor genericeditormodule getbyid identifier identifying injection interface list mask matching method populate promise resolving returns search service smartedit type witch"
    },
    {
      "section": "smartEdit",
      "id": "fetchMediaDataHandlerModule.service:FetchMediaDataHandler",
      "shortName": "FetchMediaDataHandler",
      "type": "service",
      "moduleName": "fetchMediaDataHandlerModule",
      "shortDescription": "implementation of FetchDataHandlerInterface for &quot;Media&quot; cmsStructureType",
      "keywords": "api assets cmsstructuretype descriptor fetchdatahandlerinterface fetchdatahandlerinterfacemodule fetchmediadatahandlermodule field filtered findbymask format fullurltomedia1 fullurltomedia2 genericeditor genericeditormodule identifier implementation list located mask media method query rest returned service smartedit someid1 someid2 url"
    },
    {
      "section": "smartEdit",
      "id": "functionsModule",
      "shortName": "functionsModule",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "provides a list of useful functions that can be used as part of the SmartEdit framework.",
      "keywords": "framework functions functionsmodule list service smartedit"
    },
    {
      "section": "smartEdit",
      "id": "functionsModule.convertToArray",
      "shortName": "functionsModule.convertToArray",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "convertToArray will convert the given object to array.",
      "keywords": "array convert converttoarray created elements functionsmodule input inputobject key object original output service smartedit"
    },
    {
      "section": "smartEdit",
      "id": "functionsModule.copy",
      "shortName": "functionsModule.copy",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "copy will do a deep copy of the given input object.",
      "keywords": "candidate copied copy deep functionsmodule input javascript object service smartedit"
    },
    {
      "section": "smartEdit",
      "id": "functionsModule.customTimeout",
      "shortName": "functionsModule.customTimeout",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "customTimeout will call the javascrit&#39;s native setTimeout method to execute a given function after a specified period of time.",
      "keywords": "$timeout assert better call customtimeout difficult duration end-to-end execute executed func function functionsmodule javascrit method milliseconds native period service settimeout smartedit testing time"
    },
    {
      "section": "smartEdit",
      "id": "functionsModule.escapeHtml",
      "shortName": "functionsModule.escapeHtml",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "escapeHtml will escape &amp;, &lt;, &gt;, &quot; and &#39; characters .",
      "keywords": "characters escape escaped escapehtml functionsmodule service smartedit string"
    },
    {
      "section": "smartEdit",
      "id": "functionsModule.extend",
      "shortName": "functionsModule.extend",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "extend provides a convenience to either default a new child or &quot;extend&quot; an existing child with the prototype of the parent",
      "keywords": "child childclass convenience default existing extend extended functionsmodule parent parentclass prototype service set smartedit"
    },
    {
      "section": "smartEdit",
      "id": "functionsModule.extractFromElement",
      "shortName": "functionsModule.extractFromElement",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "parses a string HTML into a queriable DOM object",
      "keywords": "dom element elements extract extracted extractfromelement extractionselector functionsmodule html identifying jquery matching object parent parses queriable selector selectors service smartedit string"
    },
    {
      "section": "smartEdit",
      "id": "functionsModule.generateIdentifier",
      "shortName": "functionsModule.generateIdentifier",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "generateIdentifier will generate a unique string based on system time and a random generator.",
      "keywords": "based functionsmodule generate generateidentifier generator identifier random service smartedit string system time unique"
    },
    {
      "section": "smartEdit",
      "id": "functionsModule.getOrigin",
      "shortName": "functionsModule.getOrigin",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "returns document location origin",
      "keywords": "browsers caters document function functionsmodule gap getorigin location origin returns service smartedit support w3c"
    },
    {
      "section": "smartEdit",
      "id": "functionsModule.getQueryString",
      "shortName": "functionsModule.getQueryString",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "getQueryString will convert a given object into a query string.",
      "keywords": "code convert functionsmodule getquerystring input key1 key2 key3 list object output params query sample service smartedit snippet string value1 value2 value3 var"
    },
    {
      "section": "smartEdit",
      "id": "functionsModule.getURI",
      "shortName": "functionsModule.getURI",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "Will return the URI part of a URL",
      "keywords": "functionsmodule geturi return returned service smartedit uri url"
    },
    {
      "section": "smartEdit",
      "id": "functionsModule.hitch",
      "shortName": "functionsModule.hitch",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "hitch will create a new function that will pass our desired context (scope) to the given function.",
      "keywords": "assigned binded binding context create desired function functionsmodule hitch method parameters pass pre-bind scope service smartedit"
    },
    {
      "section": "smartEdit",
      "id": "functionsModule.injectJS",
      "shortName": "functionsModule.injectJS",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "injectJS will inject script tags into html for a given set of sources.",
      "keywords": "array callback callbacks configuration configurations execute extract functionsmodule html inject injectjs javascript method object potential provided script service set smartedit source sources tag tags triggered wired"
    },
    {
      "section": "smartEdit",
      "id": "functionsModule.isBlank",
      "shortName": "functionsModule.isBlank",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "isBlank will check if a given string is undefined or null or empty.",
      "keywords": "check empty false functionsmodule input inputstring isblank null returns service smartedit string true undefined"
    },
    {
      "section": "smartEdit",
      "id": "functionsModule.merge",
      "shortName": "functionsModule.merge",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "merge will merge the contents of two objects together into the first object.",
      "keywords": "contents functionsmodule javascript merge object objects result service smartedit source target"
    },
    {
      "section": "smartEdit",
      "id": "functionsModule.parseHTML",
      "shortName": "functionsModule.parseHTML",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "parses a string HTML into a queriable DOM object, stripping any JavaScript from the HTML.",
      "keywords": "dom functionsmodule html javascript object parse parsehtml parses queriable representation service smartedit string stringhtml stripping"
    },
    {
      "section": "smartEdit",
      "id": "functionsModule.parseQuery",
      "shortName": "functionsModule.parseQuery",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "parseQuery will convert a given query string to an object.",
      "keywords": "code convert functionsmodule input key1 key2 key3 object output params parsed parsequery query sample service smartedit snippet string value1 value2 value3 var"
    },
    {
      "section": "smartEdit",
      "id": "functionsModule.regExpFactory",
      "shortName": "functionsModule.regExpFactory",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "regExpFactory will convert a given pattern into a regular expression.",
      "keywords": "append convert converted expression functionsmodule generated method pattern prepend proper regex regexpfactory regular replaces service smartedit string wildcards"
    },
    {
      "section": "smartEdit",
      "id": "functionsModule.sanitizeHTML",
      "shortName": "functionsModule.sanitizeHTML",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "sanitizeHTML will remove breaks and space .",
      "keywords": "breaks escaped functionsmodule html remove sanitized sanitizehtml service smartedit space string"
    },
    {
      "section": "smartEdit",
      "id": "functionsModule.toPromise",
      "shortName": "functionsModule.toPromise",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "toPromise&lt;/&gt; transforms a function into a function that is guaranteed to return a Promise that resolves to the",
      "keywords": "function functionsmodule guaranteed original promise resolves return service smartedit topromise transforms"
    },
    {
      "section": "smartEdit",
      "id": "functionsModule.trim",
      "shortName": "functionsModule.trim",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "trim will remove spaces at the beginning and end of a given string.",
      "keywords": "functionsmodule input inputstring modified newly remove service smartedit spaces string trim"
    },
    {
      "section": "smartEdit",
      "id": "functionsModule.uniqueArray",
      "shortName": "functionsModule.uniqueArray",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "uniqueArray will return the first Array argument supplemented with new entries from the second Array argument.",
      "keywords": "argument array array1 array2 entries functionsmodule javascript return second service smartedit supplemented uniquearray"
    },
    {
      "section": "smartEdit",
      "id": "functionsModule.unsafeParseHTML",
      "shortName": "functionsModule.unsafeParseHTML",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "parses a string HTML into a queriable DOM object, preserving any JavaScript present in the HTML.",
      "keywords": "dom failure functionsmodule html javascript location note object originating parse parses preserves preserving queriable representation result safe service smartedit string stringhtml strings unsafeparsehtml vulnerability xss"
    },
    {
      "section": "smartEdit",
      "id": "gatewayFactoryModule.gatewayFactory",
      "shortName": "gatewayFactoryModule.gatewayFactory",
      "type": "service",
      "moduleName": "gatewayFactoryModule",
      "shortDescription": "The Gateway Factory controls the creation of and access to MessageGateway",
      "keywords": "access application argument caches call calls channel clients construct controls corresponding create created creategateway creates creation dispatches error event events exist factory fail gateway gatewayfactory gatewayfactorymodule gatewayid handle handler handling identifier initializes initlistener instances lifecycle logged message messagegateway method newly null order postmessage prevent provide return returns second service smartedit subsequent"
    },
    {
      "section": "smartEdit",
      "id": "gatewayFactoryModule.MessageGateway",
      "shortName": "gatewayFactoryModule.MessageGateway",
      "type": "service",
      "moduleName": "gatewayFactoryModule",
      "shortDescription": "The Message Gateway is a private channel that is used to publish and subscribe to events across iFrame",
      "keywords": "angularjs attempt attempts based benefits boundaries callback chain channel controlled creation cross-origin current data event eventid events exist failure function gateway gatewayfactory gatewayfactorymodule gatewayid generated identifier iframe implementation implements instance instances interrupted invoked key listener message messagegateway messages method number occurs optional origins parameter payload pk postmessage primary private promise promises publish publishes receiving registers reject rejected resolve retries scenarios send service side smartedit subscribe technology underlying w3c-compliant works"
    },
    {
      "section": "smartEdit",
      "id": "gatewayFactoryModule.object:TIMEOUT_TO_RETRY_PUBLISHING",
      "shortName": "TIMEOUT_TO_RETRY_PUBLISHING",
      "type": "object",
      "moduleName": "gatewayFactoryModule",
      "shortDescription": "Period between two retries of a gatewayFactoryModule.MessageGateway to publish an event",
      "keywords": "browser event explorer frames gatewayfactorymodule greater internet messagegateway needed object period postmessage process publish retries smartedit time"
    },
    {
      "section": "smartEdit",
      "id": "gatewayFactoryModule.object:WHITE_LISTED_STOREFRONTS_CONFIGURATION_KEY",
      "shortName": "WHITE_LISTED_STOREFRONTS_CONFIGURATION_KEY",
      "type": "object",
      "moduleName": "gatewayFactoryModule",
      "shortDescription": "the name of the configuration key containing the list of white listed storefront domain names",
      "keywords": "configuration domain gatewayfactorymodule key list listed names object smartedit storefront white"
    },
    {
      "section": "smartEdit",
      "id": "gatewayProxyModule.gatewayProxy",
      "shortName": "gatewayProxyModule.gatewayProxy",
      "type": "service",
      "moduleName": "gatewayProxyModule",
      "shortDescription": "To seamlessly integrate the gateway factory between two services on different frames, you can use a gateway",
      "keywords": "allowing api assigned attaches automatically avoid body call calls communication declared delegates eligible empty factory fail forward frames function functions gateway gatewayfactory gatewayfactorymodule gatewayid gatewayproxy gatewayproxymodule initforservice inner instance instances integrate listeners method methods methodssubset module mutate mutates optional process promise promises providing proxied proxy publish registers registration requires result seamlessly service services simplifies smartedit stub subset trigger turned unnecessarily wraps"
    },
    {
      "section": "smartEdit",
      "id": "genericEditorModule",
      "shortName": "genericEditorModule",
      "type": "overview",
      "moduleName": "genericEditorModule",
      "keywords": "genericeditormodule overview smartedit"
    },
    {
      "section": "smartEdit",
      "id": "genericEditorModule.directive:genericEditor",
      "shortName": "genericEditor",
      "type": "directive",
      "moduleName": "genericEditorModule",
      "shortDescription": "Directive responsible for generating custom HTML CRUD form for any smarteditComponent type.",
      "keywords": "api binding button cancel component content contentapi contract create created creates crud custom data delete deleted described directive display dom element exposes extracted form fulfills function generating generic-editor genericeditor genericeditormodule html identifier inner instance invoker link local method optional original parameter read reset responsible rest scope service set sets smartedit smartedit-component-id smartedit-component-type smarteditcomponent smarteditcomponentid smarteditcomponenttype storefront structure structureapi submit type update updated"
    },
    {
      "section": "smartEdit",
      "id": "genericEditorModule.service:GenericEditor",
      "shortName": "GenericEditor",
      "type": "service",
      "moduleName": "genericEditorModule",
      "shortDescription": "The Generic Editor is a class that makes it possible for SmartEdit users (CMS managers, editors, etc.) to edit components in the SmartEdit interface.",
      "keywords": "actual advised api array associated attempted attributes boolean build built call checkbox class cmsstructureenumtype cmsstructuretype code comparing component componentform components content convention crud current data datahandler de default details determine determined directive dirty display displayed displays documentation dropdown dropdowns edit editable edited editor editors en english entries entry enum error errors example expect expected expression fail fallback false fetch fetchdatahandlerinterface fetchdatahandlerinterfacemodule fetches fetchmediadatahandler fetchmediadatahandlermodule field filter filterable filtered form format fr french fulfill function generic genericeditor genericeditormodule handles hindi holds html i18nkey i18nkeyforsomequalifier1 i18nkeyforsomequalifier2 i18nkeyforsomequalifier3 i18nkeyforsomequalifier4 i18nkeyforsomequalifier5 i18nkeyforsomequalifier6 i18nkeyforsomequalifier7 identified identifier implementation implies indicates indicator initialization input interface invoked isdirty iso json key label language learn list local localized long longstring manage managers mandatory map mask match media message method methods_refreshoptions modified mypackage naming native non-localized object occurs option orientation original pattern payload payloads performing picker populates predicate pristine property provided qualified qualifier qualifier1 qualifier2 qualifier3 reads reason received refreshoptions regular request requested requests required requirements requires reset response rest return returned returns richtext saved saves saving search serialized service sets shipped shortstring smartedit somequalifier1 somequalifier2 somequalifier3 somequalifier4 somequalifier5 somequalifier6 somequalifier7 sort specific string strings structure subject submit support templates text textarea timestamps translated true type types update updated updates users validation validationerror values widget widgets"
    },
    {
      "section": "smartEdit",
      "id": "hasOperationPermissionModule",
      "shortName": "hasOperationPermissionModule",
      "type": "overview",
      "moduleName": "hasOperationPermissionModule",
      "shortDescription": "This module provides a directive used to determine if the current user has permission to perform the action defined",
      "keywords": "action current defined determine directive dom elements hasoperationpermissionmodule key module overview perform permission remove smartedit user"
    },
    {
      "section": "smartEdit",
      "id": "httpAuthInterceptorModule.httpAuthInterceptor",
      "shortName": "httpAuthInterceptorModule.httpAuthInterceptor",
      "type": "service",
      "moduleName": "httpAuthInterceptorModule",
      "shortDescription": "Provides a way for global authentication by intercepting the requests before handing them to the server",
      "keywords": "$http $httpprovider adding adds application array authentication based call called code config configuration dependencies displaying error errors explain factories factory failed failures forwards global handing handles holds http httpauthinterceptor httpauthinterceptormodule injected intercepted intercepting interceptor interceptors intercepts message method methods object parameters registered request requests resource response responseerror responses rest returns server service smartedit status token"
    },
    {
      "section": "smartEdit",
      "id": "httpErrorInterceptorModule",
      "shortName": "httpErrorInterceptorModule",
      "type": "service",
      "moduleName": "httpErrorInterceptorModule",
      "shortDescription": "Module that provides a service called httpErrorInterceptor",
      "keywords": "access called error errors handling httperrorinterceptor httperrorinterceptormodule module occ response service smartedit validation"
    },
    {
      "section": "smartEdit",
      "id": "httpErrorInterceptorModule.httpErrorInterceptor",
      "shortName": "httpErrorInterceptorModule.httpErrorInterceptor",
      "type": "service",
      "moduleName": "httpErrorInterceptorModule",
      "shortDescription": "Provides a way for global error handling by intercepting the requests before handing them to the server",
      "keywords": "$httpprovider access adding angularjs application array based called code dependencies error errors explain factories factory failed global handing handles handling httperrorinterceptor httperrorinterceptormodule https injected intercepting interceptor interceptors intercepts method methods object occ org parameters promise registered rejected requests resolved response responseerror responses returns server service smartedit status validation"
    },
    {
      "section": "smartEdit",
      "id": "i18nInterceptorModule.object:I18NAPIROOT",
      "shortName": "I18NAPIROOT",
      "type": "object",
      "moduleName": "i18nInterceptorModule",
      "shortDescription": "The I18NAPIroot is a hard-coded URI that is used to initialize the translationServiceModule.",
      "keywords": "hard-coded i18n_resource_uri i18napiroot i18ninterceptor i18ninterceptormodule initialize intercepts methods_request object replaces request resourcelocationsmodule service smartedit translationservicemodule uri"
    },
    {
      "section": "smartEdit",
      "id": "i18nInterceptorModule.object:UNDEFINED_LOCALE",
      "shortName": "UNDEFINED_LOCALE",
      "type": "object",
      "moduleName": "i18nInterceptorModule",
      "shortDescription": "The undefined locale set as the preferred language of the translationServiceModule so that",
      "keywords": "browser i18ninterceptor i18ninterceptormodule intercept language locale methods_request object preferred replace request service set smartedit translationservicemodule undefined"
    },
    {
      "section": "smartEdit",
      "id": "i18nInterceptorModule.service:i18nInterceptor",
      "shortName": "i18nInterceptor",
      "type": "service",
      "moduleName": "i18nInterceptorModule",
      "shortDescription": "A HTTP request interceptor that intercepts all i18n calls and handles them as required in the i18nInterceptor.request method.",
      "keywords": "$httpprovider adding angularjs appends array called calls config configuration defined dependencies factories factory getresolvelocale handles http https i18n i18n_resource_uri i18napiroot i18ninterceptor i18ninterceptormodule injected interceptor interceptors intercepts invoked languageservice languageservicemodule locale method methods methods_getresolvelocale methods_request object org passed promise provided registered replaces request requests required resourcelocationsmodule retrieved returns service smartedit url"
    },
    {
      "section": "smartEdit",
      "id": "iframeClickDetectionServiceModule",
      "shortName": "iframeClickDetectionServiceModule",
      "type": "overview",
      "moduleName": "iframeClickDetectionServiceModule",
      "shortDescription": "The iframeClickDetectionServiceModule",
      "keywords": "application bind click container contents detection document event events function functionality gatewayproxymodule iframe iframeclickdetectionservicemodule jquery module mousedown overview propagate proxy requires smartedit transmit triggering"
    },
    {
      "section": "smartEdit",
      "id": "iframeClickDetectionServiceModule.service:iframeClickDetectionService",
      "shortName": "iframeClickDetectionService",
      "type": "service",
      "moduleName": "iframeClickDetectionServiceModule",
      "shortDescription": "The iframe Click Detection service leverages the gatewayProxy service to",
      "keywords": "callback calls click completes container delegates detection events function gatewayproxy gatewayproxymodule iframe iframeclickdetectionservicemodule leverages method mousedown oniframeclick promise propagate proxy resolved service smartedit"
    },
    {
      "section": "smartEdit",
      "id": "interceptorHelperModule.service:interceptorHelper",
      "shortName": "interceptorHelper",
      "type": "service",
      "moduleName": "interceptorHelperModule",
      "shortDescription": "Helper service used to handle request and response in interceptors",
      "keywords": "body callback config error function handle handled handles helper initial interceptor interceptorhelpermodule interceptors method methodsof_handlerequest methodsof_handleresponse methodsof_handleresponseerror object promise rejecting request resolving response return service smartedit success"
    },
    {
      "section": "smartEdit",
      "id": "languageSelectorModule",
      "shortName": "languageSelectorModule",
      "type": "overview",
      "moduleName": "languageSelectorModule",
      "shortDescription": "The language selector module contains a directive which allow the user to select a language.",
      "keywords": "allow api backend call directive language languages languageselectormodule languageservice languageservicemodule list module order overview select selector service smartedit supported user"
    },
    {
      "section": "smartEdit",
      "id": "languageSelectorModule.directive:languageSelector",
      "shortName": "languageSelector",
      "type": "directive",
      "moduleName": "languageSelectorModule",
      "shortDescription": "Language selector provides a drop-down list which contains a list of supported languages.",
      "keywords": "directive drop-down language languages languageselectormodule list select selector smartedit supported system translate"
    },
    {
      "section": "smartEdit",
      "id": "languageServiceModule",
      "shortName": "languageServiceModule",
      "type": "overview",
      "moduleName": "languageServiceModule",
      "shortDescription": "The languageServiceModule",
      "keywords": "fetches language languages languageservicemodule module overview service site smartedit supported"
    },
    {
      "section": "smartEdit",
      "id": "languageServiceModule.SELECTED_LANGUAGE",
      "shortName": "languageServiceModule.SELECTED_LANGUAGE",
      "type": "object",
      "moduleName": "languageServiceModule",
      "shortDescription": "A constant that is used as key to store the selected language in the storageService",
      "keywords": "constant key language languageservicemodule object selected selected_language smartedit storageservice store"
    },
    {
      "section": "smartEdit",
      "id": "languageServiceModule.service:languageService",
      "shortName": "languageService",
      "type": "service",
      "moduleName": "languageServiceModule",
      "shortDescription": "The Language Service fetches all languages for a specified site using REST service calls to the cmswebservices languages API.",
      "keywords": "active api array browser callback calls check cmswebservices code current currently descriptor descriptors determine determines en en_us english fetched fetches format fr french function gateway getbrowserlanguageisocode getbrowserlocale getlanguagesforsite getresolvelocale gettoolinglanguages i18n identifier iso isocode language languages languageservicemodule list locale method nativename object order preference properties register registerswitchlanguage required resolve rest retrieves saved selected service set setselectedlanguage site sites siteuid smartedit smarteditwebservices storage storefront supported switch system tooling true uid unique user"
    },
    {
      "section": "smartEdit",
      "id": "languageServiceModule.SWITCH_LANGUAGE_EVENT",
      "shortName": "languageServiceModule.SWITCH_LANGUAGE_EVENT",
      "type": "object",
      "moduleName": "languageServiceModule",
      "shortDescription": "A constant that is used as key to publish and receive events when a language is changed.",
      "keywords": "changed constant events key language languageservicemodule object publish receive smartedit switch_language_event"
    },
    {
      "section": "smartEdit",
      "id": "loadConfigModule",
      "shortName": "loadConfigModule",
      "type": "overview",
      "moduleName": "loadConfigModule",
      "shortDescription": "The loadConfigModule supplies configuration information to SmartEdit. Configuration is stored in key/value pairs.",
      "keywords": "array configuration exposes functionsmodule key load loadconfigmodule module ngresource object overview pairs resourcelocationsmodule service shareddataservicemodule smartedit stored supplies"
    },
    {
      "section": "smartEdit",
      "id": "loadConfigModule.service:LoadConfigManager",
      "shortName": "LoadConfigManager",
      "type": "service",
      "moduleName": "loadConfigModule",
      "shortDescription": "The LoadConfigManager is used to retrieve configurations stored in configuration API.",
      "keywords": "$log $resource _prettify api array conf configuration configurations converts converttoarray copy create defaulttoolinglanguage example function hitch key loadasarray loadasobject loadconfigmanager loadconfigmanagerservice loadconfigmodule mapped method object pairs promise resourcelocationsmodule retrieve retrieves returns service set shareddataservice smartedit stored values"
    },
    {
      "section": "smartEdit",
      "id": "loadConfigModule.service:loadConfigManagerService",
      "shortName": "loadConfigManagerService",
      "type": "service",
      "moduleName": "loadConfigModule",
      "shortDescription": "A service that is a singleton of loadConfigModule.service:LoadConfigManager  which is used to ",
      "keywords": "configuration entry loadconfigmanager loadconfigmodule module point retrieve service services singleton smartedit values"
    },
    {
      "section": "smartEdit",
      "id": "modalServiceModule",
      "shortName": "modalServiceModule",
      "type": "overview",
      "moduleName": "modalServiceModule",
      "shortDescription": "The modalServiceModule",
      "keywords": "achieving actions adding additionally affect behave button buttons chaning closing components create devoted easy goal manage modal modalmanager modalservice modalservicemodule module object open opened overview providing service smartedit style styles title window windows"
    },
    {
      "section": "smartEdit",
      "id": "modalServiceModule.modalService",
      "shortName": "modalServiceModule.modalService",
      "type": "service",
      "moduleName": "modalServiceModule",
      "shortDescription": "Convenience service to open and style a promise-based templated modal window.",
      "keywords": "$log $q $scope action actions acts addbutton additional angular angularjs app array button buttonhandlerfn buttons callbacks called caller calling cancel choose classed close closed common complex conf config configuration configurations content controller convenience css cssclasses custom data debug declared defer deferred dependency depending details dismiss display errorcallback example explicit explicitly factory feel fragment function functions html https injection inline key label list log logic method methods_addbutton methods_close modal modal_button_actions modal_button_styles modalcontroller modalmanager modalservice modalservicemodule module multiple object open org path piece promise promise-based reject rejected resolve resolved result return separated service setbuttonhandler share simple smartedit someresult someservice space style submit successcallback template templated templateinline templateurl title translated true validatesomething var ways window windows"
    },
    {
      "section": "smartEdit",
      "id": "modalServiceModule.object:MODAL_BUTTON_ACTIONS",
      "shortName": "MODAL_BUTTON_ACTIONS",
      "type": "object",
      "moduleName": "modalServiceModule",
      "shortDescription": "Injectable angular constant",
      "keywords": "action addbutton adding angular angularjs button close close_modal constant defines dismiss example executing existing getbuttons https indicates injectable label methods_getbuttons methods_open modal modal_button_actions modalmanager modalservice modalservicemodule mymodalmanager object open opening org performed promise property rejected resolved returned service smartedit window"
    },
    {
      "section": "smartEdit",
      "id": "modalServiceModule.object:MODAL_BUTTON_STYLES",
      "shortName": "MODAL_BUTTON_STYLES",
      "type": "object",
      "moduleName": "modalServiceModule",
      "shortDescription": "Injectable angular constant",
      "keywords": "addbutton adding angular button cancel cancel_button constant default defines equivalent example existing feel getbuttons indicates injectable label methods_getbuttons methods_open modal modal_button_styles modalmanager modalservice modalservicemodule mymodalmanager object open opening primary property save secondary service smartedit style styled submit window"
    },
    {
      "section": "smartEdit",
      "id": "modalServiceModule.service:ModalManager",
      "shortName": "ModalManager",
      "type": "service",
      "moduleName": "modalServiceModule",
      "shortDescription": "The ModalManager is a service designed to provide easy runtime modification to various aspects of a modal window,",
      "keywords": "$log $q $scope access action addbutton adding additionally allowing allows angularjs applied array aspects avoid button buttonhandlerfn buttonhandlerfunction buttonid buttonpressedcallback buttons callback callbacks called caller cancel cancelled case caution chain clone close closed closing code complexity conf configuration content continue controller corner create data debug default defer deferred designed details disablebutton disabled dismiss dismisscallback displayed don easy empty enable enablebutton enabled enables errorcallback example execute executed exposed fetched fired flag function getbutton getbuttons handler handlers happen header hello https i18n ignore implicitly instance isolated key kind label long manager matching method methods_addbutton methods_setbuttonhandler modal modal_button_actions modal_button_styles modalmanager modalservice modalservicemodule modaltestcontroller modification modifying multiple newly note null object open org parameter parameters pass passed passing press pressed prevent preventing process promise properties provide provided publicly read receives reference registered reject rejected rejecting remove removeallbuttons removebutton removed representing resolve resolved resolving return returned returns runtime sample scenarios scope service setbuttonhandler setdismisscallback setshowheaderdismiss setting showx single smartedit someresult string style submit successcallback suggested title top translated trigger true undefined unique unnecessary update validatesomething validating validation var window"
    },
    {
      "section": "smartEdit",
      "id": "perspectiveInterfaceModule",
      "shortName": "perspectiveInterfaceModule",
      "type": "overview",
      "moduleName": "perspectiveInterfaceModule",
      "keywords": "overview perspectiveinterfacemodule smartedit"
    },
    {
      "section": "smartEdit",
      "id": "perspectiveInterfaceModule.service:PerspectiveServiceInterface",
      "shortName": "PerspectiveServiceInterface",
      "type": "service",
      "moduleName": "perspectiveInterfaceModule",
      "keywords": "activated activating active actives application bound configuration consists cookie current currently currently-selected deactivates deactivating default description descriptioni18nkey disabled disablingcallback enabled enablingcallback feature features flag functions hasactiveperspective i18n identified identifies indicates invoked isemptyperspectiveactive key list method methods_register mode namei18nkey optional overlay parameter perspective perspectiveinterfacemodule perspectives preview referenced register registered registers registry represents respective returns se selectdefault selected selects service smartedit smartedit-perspectives stored stores switches switchto system tooltip translated true uniquely user web"
    },
    {
      "section": "smartEdit",
      "id": "previewDataGenericEditorResponseHandlerModule.service:previewDataGenericEditorResponseHandler",
      "shortName": "previewDataGenericEditorResponseHandler",
      "type": "service",
      "moduleName": "previewDataGenericEditorResponseHandlerModule",
      "shortDescription": "previewDataGenericEditorResponseHandler is invoked by GenericEditor to handle POST response",
      "keywords": "allow api call editor genericeditor genericeditormodule handle handler invoked invoking model multiple post preview previewdatagenericeditorresponsehandler previewdatagenericeditorresponsehandlermodule response responsible retrieving server service set setting smartedit smarteditcomponentid ticketid updatecallback"
    },
    {
      "section": "smartEdit",
      "id": "previewTicketInterceptorModule.previewTicketInterceptor",
      "shortName": "previewTicketInterceptorModule.previewTicketInterceptor",
      "type": "service",
      "moduleName": "previewTicketInterceptorModule",
      "shortDescription": "A HTTP request interceptor that adds the preview ticket to the HTTP header object before a request is made.",
      "keywords": "$httpprovider adding adds array called config configuration current dependencies extracts factories factory header holds http injected interceptor interceptors method methods modified object preview previewticketinterceptor previewticketinterceptormodule property registered request resource returns service smartedit ticket url x-preview-ticket"
    },
    {
      "section": "smartEdit",
      "id": "renderServiceInterfaceModule",
      "shortName": "renderServiceInterfaceModule",
      "type": "overview",
      "moduleName": "renderServiceInterfaceModule",
      "shortDescription": "The renderServiceInterfaceModule",
      "keywords": "abstract accelerator component components data designed displays extensible interface module operation overview performed re-render render renderservice renderserviceinterfacemodule service smartedit update"
    },
    {
      "section": "smartEdit",
      "id": "renderServiceInterfaceModule.service:RenderServiceInterface",
      "shortName": "RenderServiceInterface",
      "type": "service",
      "moduleName": "renderServiceInterfaceModule",
      "shortDescription": "Designed to re-render components after an update component data operation has been performed, according to",
      "keywords": "$compile accelerator backend class compilation component componentid components componenttype content correctly current custom customcontent data decorators decoratorservicemodule designed displayed displays error event executed extended flag frontend html indicates instantiated interface method methods_storeprecompiledcomponent note object operation optional original overlay performed position positioned promise propagate re-render re-rendered re-renders receiving refreshoverlaydimensions reject rejected remove removed removes render rendercomponent rendered rendergateway renderpage renderremoval renderservice renderserviceinterfacemodule renderslots replace requires rerender resets resolve returns saved serves service showoverlay slot slotids slotsids smartedit stack storefront subscribes success time toggleoverlay toggles triggers type update updated updates values visibility wrapping"
    },
    {
      "section": "smartEdit",
      "id": "renderServiceModule",
      "shortName": "renderServiceModule",
      "type": "overview",
      "moduleName": "renderServiceModule",
      "shortDescription": "This module provides the renderService, which is responsible for rendering the SmartEdit overlays used for providing",
      "keywords": "cms context functionality module overlays overview providing rendering renderservice renderservicemodule responsible smartedit storefront"
    },
    {
      "section": "smartEdit",
      "id": "renderServiceModule.renderGateway",
      "shortName": "renderServiceModule.renderGateway",
      "type": "service",
      "moduleName": "renderServiceModule",
      "shortDescription": "Instance of the MessageGateway dealing with rendering-related events.",
      "keywords": "dealing events gatewayfactorymodule instance messagegateway rendergateway rendering-related renderservicemodule service smartedit"
    },
    {
      "section": "smartEdit",
      "id": "renderServiceModule.renderService",
      "shortName": "renderServiceModule.renderService",
      "type": "service",
      "moduleName": "renderServiceModule",
      "shortDescription": "The renderService is responsible for rendering and resizing component overlays, and re-rendering components and slots",
      "keywords": "_resizeslots _updateoverlaydimensions absolutely account additionally appear based body bottom bound bounding calculated children cms comparing component componentelem componentoverlayelem components contextual css default dimensions discover displayed dom dynamic element elements empty encompassing event fetched flag function height iframe implementation inside isvisible jquery manager match menus method minimum original overlap overlay overlays parentoverlayelem perspective position positioned positions provided re-rendering rectangle refresh refreshes refreshoverlaydimensions remain rendering renderservice renderservicemodule resized resizes resizing responsible responsive root scrolling search service set size sizes slot slots smartedit specific storefront storefronts sub-components synced takes toggleoverlay toggles top traverse updates values visibility width window"
    },
    {
      "section": "smartEdit",
      "id": "resourceLocationsModule.object:CATALOG_VERSION_DETAILS_RESOURCE_URI",
      "shortName": "CATALOG_VERSION_DETAILS_RESOURCE_URI",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "Resource URI of the catalog version details REST service.",
      "keywords": "catalog details object resource resourcelocationsmodule rest service smartedit uri version"
    },
    {
      "section": "smartEdit",
      "id": "resourceLocationsModule.object:CATALOGS_PATH",
      "shortName": "CATALOGS_PATH",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "Path of the catalogs",
      "keywords": "catalogs object path resourcelocationsmodule smartedit"
    },
    {
      "section": "smartEdit",
      "id": "resourceLocationsModule.object:CMSWEBSERVICES_PATH",
      "shortName": "CMSWEBSERVICES_PATH",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "Path of the cmswebservices",
      "keywords": "cmswebservices object path resourcelocationsmodule smartedit"
    },
    {
      "section": "smartEdit",
      "id": "resourceLocationsModule.object:CMSWEBSERVICES_RESOURCE_URI",
      "shortName": "CMSWEBSERVICES_RESOURCE_URI",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "Constant for the cmswebservices API root",
      "keywords": "api cmswebservices constant object resourcelocationsmodule root smartedit"
    },
    {
      "section": "smartEdit",
      "id": "resourceLocationsModule.object:CONFIGURATION_COLLECTION_URI",
      "shortName": "CONFIGURATION_COLLECTION_URI",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "The SmartEdit configuration collection API root",
      "keywords": "api collection configuration object resourcelocationsmodule root smartedit"
    },
    {
      "section": "smartEdit",
      "id": "resourceLocationsModule.object:CONFIGURATION_URI",
      "shortName": "CONFIGURATION_URI",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "the name of the SmartEdit configuration API root",
      "keywords": "api configuration object resourcelocationsmodule root smartedit"
    },
    {
      "section": "smartEdit",
      "id": "resourceLocationsModule.object:DEFAULT_AUTHENTICATION_CLIENT_ID",
      "shortName": "DEFAULT_AUTHENTICATION_CLIENT_ID",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "The default OAuth 2 client id to use during authentication.",
      "keywords": "authentication client default oauth object resourcelocationsmodule smartedit"
    },
    {
      "section": "smartEdit",
      "id": "resourceLocationsModule.object:DEFAULT_AUTHENTICATION_ENTRY_POINT",
      "shortName": "DEFAULT_AUTHENTICATION_ENTRY_POINT",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "When configuration is not available yet to provide authenticationMap, one needs a default authentication entry point to access configuration API itself",
      "keywords": "access api authentication authenticationmap configuration default entry object point provide resourcelocationsmodule smartedit"
    },
    {
      "section": "smartEdit",
      "id": "resourceLocationsModule.object:ENUM_RESOURCE_URI",
      "shortName": "ENUM_RESOURCE_URI",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "Path to fetch list of values of a given enum type",
      "keywords": "enum fetch list object path resourcelocationsmodule smartedit type values"
    },
    {
      "section": "smartEdit",
      "id": "resourceLocationsModule.object:I18N_LANGUAGE_RESOURCE_URI",
      "shortName": "I18N_LANGUAGE_RESOURCE_URI",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "Resource URI to fetch the supported i18n languages.",
      "keywords": "fetch i18n languages object resource resourcelocationsmodule smartedit supported uri"
    },
    {
      "section": "smartEdit",
      "id": "resourceLocationsModule.object:I18N_RESOURCE_URI",
      "shortName": "I18N_RESOURCE_URI",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "Resource URI to fetch the i18n initialization map for a given locale.",
      "keywords": "fetch i18n initialization locale map object resource resourcelocationsmodule smartedit uri"
    },
    {
      "section": "smartEdit",
      "id": "resourceLocationsModule.object:LANDING_PAGE_PATH",
      "shortName": "LANDING_PAGE_PATH",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "Path of the landing page",
      "keywords": "landing object path resourcelocationsmodule smartedit"
    },
    {
      "section": "smartEdit",
      "id": "resourceLocationsModule.object:LANGUAGE_RESOURCE_URI",
      "shortName": "LANGUAGE_RESOURCE_URI",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "Resource URI of the languages REST service.",
      "keywords": "languages object resource resourcelocationsmodule rest service smartedit uri"
    },
    {
      "section": "smartEdit",
      "id": "resourceLocationsModule.object:MEDIA_PATH",
      "shortName": "MEDIA_PATH",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "Path of the media",
      "keywords": "media object path resourcelocationsmodule smartedit"
    },
    {
      "section": "smartEdit",
      "id": "resourceLocationsModule.object:MEDIA_RESOURCE_URI",
      "shortName": "MEDIA_RESOURCE_URI",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "Resource URI of the media REST service.",
      "keywords": "media object resource resourcelocationsmodule rest service smartedit uri"
    },
    {
      "section": "smartEdit",
      "id": "resourceLocationsModule.object:PERMISSIONSWEBSERVICES_RESOURCE_URI",
      "shortName": "PERMISSIONSWEBSERVICES_RESOURCE_URI",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "Path to fetch permissions of a given type",
      "keywords": "fetch object path permissions resourcelocationsmodule smartedit type"
    },
    {
      "section": "smartEdit",
      "id": "resourceLocationsModule.object:PREVIEW_RESOURCE_URI",
      "shortName": "PREVIEW_RESOURCE_URI",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "Path of the preview ticket API",
      "keywords": "api object path preview resourcelocationsmodule smartedit ticket"
    },
    {
      "section": "smartEdit",
      "id": "resourceLocationsModule.object:SITES_RESOURCE_URI",
      "shortName": "SITES_RESOURCE_URI",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "Resource URI of the sites REST service.",
      "keywords": "object resource resourcelocationsmodule rest service sites smartedit uri"
    },
    {
      "section": "smartEdit",
      "id": "resourceLocationsModule.object:SMARTEDIT_RESOURCE_URI_REGEXP",
      "shortName": "SMARTEDIT_RESOURCE_URI_REGEXP",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "to calculate platform domain URI, this regular expression will be used",
      "keywords": "calculate domain expression object platform regular resourcelocationsmodule smartedit uri"
    },
    {
      "section": "smartEdit",
      "id": "resourceLocationsModule.object:SMARTEDIT_ROOT",
      "shortName": "SMARTEDIT_ROOT",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "the name of the webapp root context",
      "keywords": "context object resourcelocationsmodule root smartedit webapp"
    },
    {
      "section": "smartEdit",
      "id": "resourceLocationsModule.object:STORE_FRONT_CONTEXT",
      "shortName": "STORE_FRONT_CONTEXT",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "to fetch the store front context for inflection points.",
      "keywords": "context fetch front inflection object points resourcelocationsmodule smartedit store"
    },
    {
      "section": "smartEdit",
      "id": "resourceLocationsModule.object:STOREFRONT_PATH",
      "shortName": "STOREFRONT_PATH",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "Path of the storefront",
      "keywords": "object path resourcelocationsmodule smartedit storefront"
    },
    {
      "section": "smartEdit",
      "id": "resourceLocationsModule.object:STOREFRONT_PATH_WITH_PAGE_ID",
      "shortName": "STOREFRONT_PATH_WITH_PAGE_ID",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "Path of the storefront with a page ID",
      "keywords": "object path resourcelocationsmodule smartedit storefront"
    },
    {
      "section": "smartEdit",
      "id": "resourceLocationsModule.object:SYNC_PATH",
      "shortName": "SYNC_PATH",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "Path of the synchronization service",
      "keywords": "object path resourcelocationsmodule service smartedit synchronization"
    },
    {
      "section": "smartEdit",
      "id": "resourceLocationsModule.resourceLocationToRegex",
      "shortName": "resourceLocationsModule.resourceLocationToRegex",
      "type": "service",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "Generates a regular expresssion matcher from a given resource location URL, replacing predefined keys by wildcard",
      "keywords": "$httpbackend backend endpoint endpointregex example expresssion generates hits http location match matcher matchers mocked passed predefined regex regular replacing resource resourcelocationsmodule resourcelocationtoregex respond service smartedit someresource somevalue url var whenget wildcard"
    },
    {
      "section": "smartEdit",
      "id": "restServiceFactoryModule",
      "shortName": "restServiceFactoryModule",
      "type": "overview",
      "moduleName": "restServiceFactoryModule",
      "shortDescription": "The restServiceFactoryModule",
      "keywords": "factory generate module overview providing resource rest restservicefactorymodule service smartedit url wrapper"
    },
    {
      "section": "smartEdit",
      "id": "restServiceFactoryModule.service:ResourceWrapper",
      "shortName": "ResourceWrapper",
      "type": "service",
      "moduleName": "restServiceFactoryModule",
      "shortDescription": "The ResourceWrapper is service used to make REST calls to the wrapped target URI. It is created",
      "keywords": "angularjs appends based call called calls component components created deletes evaluate evaluates factory getbyid https identifier list loads mapped match matching method methods_get object objects org parameters params payload placeholder placeholders promise provided query rejected rejecting rejects remove resolves resolving resource resourcewrapper rest restservicefactory restservicefactorymodule returned returns save saved saves search searchparams server service single smartedit string target unique update updated updates uri verify warpper wrapped wrapper"
    },
    {
      "section": "smartEdit",
      "id": "restServiceFactoryModule.service:restServiceFactory",
      "shortName": "restServiceFactory",
      "type": "service",
      "moduleName": "restServiceFactoryModule",
      "shortDescription": "A factory used to generate a REST service wrapper for a given resource URL, providing a means to perform HTTP",
      "keywords": "$resource angularjs appended argument automatically best context-wide create created default delegated domain factory forward generate http https identifier location method multiple object operations opposed optional org parameter paths payload perform placeholder points post prefix prefixed prepended provided providing reference relative remain resource resourceid rest restservicefactory restservicefactorymodule retrieved returned separate service services set setdomain slash smartedit split target uri uris url working wrapped wrapper wraps"
    },
    {
      "section": "smartEdit",
      "id": "seBackendValidationHandlerModule",
      "shortName": "seBackendValidationHandlerModule",
      "type": "overview",
      "moduleName": "seBackendValidationHandlerModule",
      "shortDescription": "This module provides the seBackendValidationHandler service, which handles standard OCC validation errors received",
      "keywords": "backend errors handles module occ overview received sebackendvalidationhandler sebackendvalidationhandlermodule service smartedit standard validation"
    },
    {
      "section": "smartEdit",
      "id": "seBackendValidationHandlerModule.seBackendValidationHandler",
      "shortName": "seBackendValidationHandlerModule.seBackendValidationHandler",
      "type": "service",
      "moduleName": "seBackendValidationHandlerModule",
      "shortDescription": "The seBackendValidationHandler service handles validation errors received from the backend.",
      "keywords": "appended appends array backend consisting context contextual contract data details error errors errorscontext example exception expected extracts format handleresponse handles list matches message method mysubject occurred originally output parameter provided received response sebackendvalidationhandler sebackendvalidationhandlermodule service smartedit someothererror subject type validation validationerror var"
    },
    {
      "section": "smartEdit",
      "id": "seFileValidationServiceModule",
      "shortName": "seFileValidationServiceModule",
      "type": "overview",
      "moduleName": "seFileValidationServiceModule",
      "shortDescription": "This module provides the seFileValidationService service, which validates if a specified file meets the required file",
      "keywords": "commerce constraints file hybris meets module overview required sap sefilevalidationservice sefilevalidationservicemodule service size smartedit type validates"
    },
    {
      "section": "smartEdit",
      "id": "seFileValidationServiceModule.seFileObjectValidators",
      "shortName": "seFileValidationServiceModule.seFileObjectValidators",
      "type": "object",
      "moduleName": "seFileValidationServiceModule",
      "shortDescription": "A list of file validators, that includes a validator for file-size constraints and a validator for file-type",
      "keywords": "constraints file file-size file-type includes list object sefileobjectvalidators sefilevalidationservicemodule smartedit validator validators"
    },
    {
      "section": "smartEdit",
      "id": "seFileValidationServiceModule.seFileValidationService",
      "shortName": "seFileValidationServiceModule.seFileValidationService",
      "type": "service",
      "moduleName": "seFileValidationServiceModule",
      "shortDescription": "The seFileValidationService validates that the file provided is of a specified file type and that the file does not",
      "keywords": "api append appends array buildacceptedfiletypeslist comma- comma-separated context contextual creates error errors exceed extensions file header limit list maxium method object output parameter promise provided rejected resolves sefilemimetypeservice sefilemimetypeservicemodule sefileobjectvalidators sefilevalidationservice sefilevalidationserviceconstants sefilevalidationservicemodule separated service size smartedit supported transforms type types valid validate validated validates validator web"
    },
    {
      "section": "smartEdit",
      "id": "seFileValidationServiceModule.seFileValidationServiceConstants",
      "shortName": "seFileValidationServiceModule.seFileValidationServiceConstants",
      "type": "object",
      "moduleName": "seFileValidationServiceModule",
      "shortDescription": "The constants provided by the file validation service.",
      "keywords": "bytes constants file internationalization list map maximum object platform provided sefilevalidationserviceconstants sefilevalidationservicemodule service size smartedit supported types uploaded validation"
    },
    {
      "section": "smartEdit",
      "id": "seMediaServiceModule",
      "shortName": "seMediaServiceModule",
      "type": "overview",
      "moduleName": "seMediaServiceModule",
      "shortDescription": "The media service module provides a service to create an image file for a catalog through AJAX calls. This module",
      "keywords": "$resource ajax calls catalog create data dedicated file form image media module multipart overview posts request semediaservicemodule service smartedit transformed"
    },
    {
      "section": "smartEdit",
      "id": "seMediaServiceModule.seMediaResource",
      "shortName": "seMediaServiceModule.seMediaResource",
      "type": "service",
      "moduleName": "seMediaServiceModule",
      "shortDescription": "A $resource that makes REST calls to the default",
      "keywords": "$resource angularjs api calls catalog cms collection content-type default file formdata http https media method methods mozilla multipart object org pojo post request required rest retrieve semediaresource semediaservicemodule service smartedit supports transform transformation uploads"
    },
    {
      "section": "smartEdit",
      "id": "seMediaServiceModule.seMediaResourceService",
      "shortName": "seMediaServiceModule.seMediaResourceService",
      "type": "service",
      "moduleName": "seMediaServiceModule",
      "shortDescription": "This service provides an interface to the $resource that makes REST ",
      "keywords": "$resource angularjs api calls catalog cms default http https interface media method org rest retrieve returning semediaresourceservice semediaservicemodule service single smartedit supports"
    },
    {
      "section": "smartEdit",
      "id": "seMediaServiceModule.seMediaService",
      "shortName": "seMediaServiceModule.seMediaService",
      "type": "service",
      "moduleName": "seMediaServiceModule",
      "shortDescription": "This service provides an interface to the $resource provided by the seMediaResource service and ",
      "keywords": "$resource alternate alttext angularjs backend catalog catalog-catalog code combination corresponding corresponds description errors exists fails fetch fetches file functionality getmediabycode https identifier images interface media method mozilla object org pojo promise provided request resolves returns selected semediaresource semediaresourceservice semediaservice semediaservicemodule service smartedit specific successful text unique upload uploaded uploadmedia uploads version"
    },
    {
      "section": "smartEdit",
      "id": "seObjectValidatorFactoryModule",
      "shortName": "seObjectValidatorFactoryModule",
      "type": "overview",
      "moduleName": "seObjectValidatorFactoryModule",
      "shortDescription": "This module provides the seObjectValidatorFactory service, which is used to build a validator for a specified list of",
      "keywords": "build list module objects overview seobjectvalidatorfactory seobjectvalidatorfactorymodule service smartedit validator"
    },
    {
      "section": "smartEdit",
      "id": "seObjectValidatorFactoryModule.seObjectValidatorFactory",
      "shortName": "seObjectValidatorFactoryModule.seObjectValidatorFactory",
      "type": "service",
      "moduleName": "seObjectValidatorFactoryModule",
      "shortDescription": "This service provides a factory method to build a validator for a specified list of validator objects.",
      "keywords": "append associate beause block build builds case code consist consists contextual described error errors errorscontext example factory fail failed false function invalid isvalid list message method object objects objectundervalidation parameter parameters predicate result return seobjectvalidatorfactory seobjectvalidatorfactorymodule service single smartedit subject takes validate validating validator validators var"
    },
    {
      "section": "smartEdit",
      "id": "seValidationErrorParserModule",
      "shortName": "seValidationErrorParserModule",
      "type": "overview",
      "moduleName": "seValidationErrorParserModule",
      "shortDescription": "This module provides the validationErrorsParser service, which is used to parse validation errors for parameters",
      "keywords": "errors format language message module overview parameters parse service sevalidationerrorparsermodule smartedit validation validationerrorsparser"
    },
    {
      "section": "smartEdit",
      "id": "seValidationErrorParserModule.seValidationErrorParser",
      "shortName": "seValidationErrorParserModule.seValidationErrorParser",
      "type": "service",
      "moduleName": "seValidationErrorParserModule",
      "shortDescription": "This service provides the functionality to parse validation errors received from the backend.",
      "keywords": "backend details en error errors expects extra final format function functionality language message method object occurred parse parses received service sevalidationerrorparser sevalidationerrorparsermodule smartedit somekey someval stripped validation var widescreen"
    },
    {
      "section": "smartEdit",
      "id": "sharedDataServiceInterfaceModule.SharedDataServiceInterface",
      "shortName": "sharedDataServiceInterfaceModule.SharedDataServiceInterface",
      "type": "service",
      "moduleName": "sharedDataServiceInterfaceModule",
      "shortDescription": "Provides an abstract extensible shared data service. Used to store any data to be used either the SmartEdit",
      "keywords": "abstract application class container data extended extensible fetch instantiated interface key method serves service set shared shareddataserviceinterface shareddataserviceinterfacemodule smartedit store"
    },
    {
      "section": "smartEdit",
      "id": "sharedDataServiceModule",
      "shortName": "sharedDataServiceModule",
      "type": "overview",
      "moduleName": "sharedDataServiceModule",
      "shortDescription": "The sharedDataServiceModule",
      "keywords": "application container data key module overview retrieve service shared shareddataservicemodule smartedit store"
    },
    {
      "section": "smartEdit",
      "id": "sharedDataServiceModule.sharedDataService",
      "shortName": "sharedDataServiceModule.sharedDataService",
      "type": "service",
      "moduleName": "sharedDataServiceModule",
      "shortDescription": "The Shared Data Service is used to store data that is to be shared between the SmartEdit application and the",
      "keywords": "application container data extends gateway gatewayproxy gatewayproxymodule service share shared shareddata shareddataservice shareddataserviceinterface shareddataserviceinterfacemodule shareddataservicemodule smartedit store"
    },
    {
      "section": "smartEdit",
      "id": "storageServiceModule",
      "shortName": "storageServiceModule",
      "type": "overview",
      "moduleName": "storageServiceModule",
      "shortDescription": "The storageServiceModule",
      "keywords": "allows browser module overview service smartedit storage storageservicemodule storing temporary"
    },
    {
      "section": "smartEdit",
      "id": "storageServiceModule.service:storageService",
      "shortName": "storageService",
      "type": "service",
      "moduleName": "storageServiceModule",
      "shortDescription": "The Storage service is used to store temporary information in the browser. The service keeps track of key/value pairs",
      "keywords": "associated associates authenticate authenticated authentication authtoken authtokens authuri browser cookie creates current determine entry getauthtoken getprincipalidentifier getvaluefromcookie identified identifies indicates initialized isinitialized key login method pairs principal principalnamevalue principaluid properly provided remove removeallauthtokens removeauthtoken removed removeprincipalidentifier removes resource retrieve retrieved retrieves service smartedit smartedit-sessions storage storageservicemodule store storeauthtoken stored storeprincipalidentifier stores temporary token track uid uri uris user"
    },
    {
      "section": "smartEdit",
      "id": "tabsetModule",
      "shortName": "tabsetModule",
      "type": "overview",
      "moduleName": "tabsetModule",
      "shortDescription": "The Tabset module provides the directives required to display a group of tabsets within a tabset. The",
      "keywords": "developers directive directives display displaying group interest module organizing overview required responsible smartedit tabs tabset tabsetmodule tabsets ytabset"
    },
    {
      "section": "smartEdit",
      "id": "tabsetModule.directive:yTab",
      "shortName": "yTab",
      "type": "directive",
      "moduleName": "tabsetModule",
      "shortDescription": "The directive  responsible for wrapping the content of a tab within a",
      "keywords": "allows called caller content contents custom data determine directive displayed executed extra fragment function functionality html match model modify object optional parameter parse path register responsible scope smartedit smartedit-tab smartedit-tabset tab tabcontrol tabid tabs tabset tabsetmodule track wrapping ytabset"
    },
    {
      "section": "smartEdit",
      "id": "tabsetModule.directive:yTabset",
      "shortName": "yTabset",
      "type": "directive",
      "moduleName": "tabsetModule",
      "shortDescription": "The directive responsible for displaying and organizing tabs within a tabset. A specified number of tabs will",
      "keywords": "allows body called caller changes child clicks configuration content contents custom data defined determine directive display displayed displaying drop-down error executed extra flag fragment function functionality grouped haserrors header headers html indicates item list maximum menu model modify note number numtabsdisplayed object optional organizing parameter parse passed path register remaining responsible scope selected smartedit smartedit-tab smartedit-tabset tab tabcontrol tabs tabset tabsetmodule tabslist templateurl title track user visual wrapped ytab"
    },
    {
      "section": "smartEdit",
      "id": "toolbarInterfaceModule.ToolbarServiceInterface",
      "shortName": "toolbarInterfaceModule.ToolbarServiceInterface",
      "type": "service",
      "moduleName": "toolbarInterfaceModule",
      "shortDescription": "Provides an abstract extensible toolbar service. Used to manage and perform actions to either the SmartEdit",
      "keywords": "abstract action actions additems application array callback class clicked container default description determines display extended extensible higher html hybrid_action i18nkey icon icons identifier image images include instantiated interface internal internally item key list lower manage maps method middle number perform position priority ranging sections serves service smartedit takes template toolbar toolbarinterfacemodule toolbarserviceinterface translation trigger triggered type unique url urls"
    },
    {
      "section": "smartEdit",
      "id": "toolbarModule.ToolbarService",
      "shortName": "toolbarModule.ToolbarService",
      "type": "service",
      "moduleName": "toolbarModule",
      "shortDescription": "The inner toolbar service is used to add toolbar actions that affect the inner application, publish aliases to",
      "keywords": "actions add additems affect aliases application callbacks communication cross function gateway gatewayid gatewayproxy gatewayproxymodule gettoolbarservice identifier iframe inner instance key manage managed methods methods_additems outer private provided proxy publish returns service single singleton smartedit store toolbar toolbar-name toolbarinterfacemodule toolbarmodule toolbarservice toolbarservicefactory toolbarserviceinterface"
    },
    {
      "section": "smartEdit",
      "id": "toolbarModule.toolbarServiceFactory",
      "shortName": "toolbarModule.toolbarServiceFactory",
      "type": "service",
      "moduleName": "toolbarModule",
      "shortDescription": "The toolbar service factory generates instances of the ToolbarService based on",
      "keywords": "based cached communication corresponding created cross exist exists factory gateway gatewayid gatewayproxy gatewayproxymodule generates gettoolbarservice identifier iframe instance instances method provided respect returns service single singleton smartedit toolbar toolbarmodule toolbarservice toolbarservicefactory"
    },
    {
      "section": "smartEdit",
      "id": "translationServiceModule",
      "shortName": "translationServiceModule",
      "type": "service",
      "moduleName": "translationServiceModule",
      "shortDescription": "This module is used to configure the translate service, the filter, and the directives from the &#39;pascalprecht.translate&#39; package. The configuration consists of:",
      "keywords": "appropriate browser combined configuration configure consists constant directives filter getbrowserlocale i18napiroot i18ninterceptor i18ninterceptormodule initializing languageservice languageservicemodule locale map methods_getbrowserlocale methods_request module object package pascalprecht preferredlanguage replace request retrieved runtime service setting smartedit time translate translation translationservicemodule unaccessible undefined_locale uri"
    },
    {
      "section": "smartEdit",
      "id": "urlServiceInterfaceModule.UrlServiceInterface",
      "shortName": "urlServiceInterfaceModule.UrlServiceInterface",
      "type": "service",
      "moduleName": "urlServiceInterfaceModule",
      "shortDescription": "Provides an abstract extensible url service, Used to open a given URL",
      "keywords": "abstract authentication browser class extended extensible instantiated interface invocation method open opens openurlinpopup pop serves service smartedit url urlserviceinterface urlserviceinterfacemodule"
    },
    {
      "section": "smartEdit",
      "id": "urlServiceModule",
      "shortName": "urlServiceModule",
      "type": "overview",
      "moduleName": "urlServiceModule",
      "shortDescription": "The urlServiceModule",
      "keywords": "browser container functionality module open overview providing service smartedit url urlservicemodule"
    },
    {
      "section": "smartEdit",
      "id": "urlServiceModule.urlService",
      "shortName": "urlServiceModule.urlService",
      "type": "service",
      "moduleName": "urlServiceModule",
      "shortDescription": "The Url Service is used to open a given URL in a new browser url, directly from SmartEdit. ",
      "keywords": "application browser call container data directly extends gateway gatewayproxy gatewayproxymodule making open service share smartedit url urlservice urlserviceinterface urlserviceinterfacemodule urlservicemodule"
    },
    {
      "section": "smartEdit",
      "id": "yInfiniteScrollingModule",
      "shortName": "yInfiniteScrollingModule",
      "type": "overview",
      "moduleName": "yInfiniteScrollingModule",
      "shortDescription": "This module holds the base web component to perform infinite scrolling from paged backend",
      "keywords": "backend base component holds infinite module overview paged perform scrolling smartedit web yinfinitescrollingmodule"
    },
    {
      "section": "smartEdit",
      "id": "yInfiniteScrollingModule.directive:yInfiniteScrolling",
      "shortName": "yInfiniteScrolling",
      "type": "directive",
      "moduleName": "yInfiniteScrollingModule",
      "shortDescription": "A directive that you can use to implement infinite scrolling for an expanding content (typically with a ng-repeat) nested in it.",
      "keywords": "approaches arguments attached backend bottom call change class close container content context crosses css currentpage data decide default defaults determined directive distance dropdown dropdownclass dropdowncontainerclass element evaluated example expanding expected expression failure fetch fetched fetching fetchpage fill filters free function handle height implement implementers infinite invoked items large left listens mask max-height maximum meant measured multiples mycontext nested nextpage ng-repeat number object optional overflow overflow-y override pagesize paginated pagination pixels push query re-fetch reach representing requested requests reset resolved restrict return scroll scrolling search server set size smartedit space starts string tall times triggered type typically yinfinitescrollingmodule"
    },
    {
      "section": "smartEdit",
      "id": "yInfiniteScrollingModule.object:Page",
      "shortName": "Page",
      "type": "object",
      "moduleName": "yInfiniteScrollingModule",
      "shortDescription": "An object representing the backend response to a paged query",
      "keywords": "array backend elements exceed object paged pagination pertaining property query representing requested response returned size smartedit yinfinitescrollingmodule"
    },
    {
      "section": "smartEdit",
      "id": "yInfiniteScrollingModule.object:Pagination",
      "shortName": "Pagination",
      "type": "object",
      "moduleName": "yInfiniteScrollingModule",
      "shortDescription": "An object representing the returned pagination information from backend",
      "keywords": "backend elements mask matching object pagination property representing returned smartedit total totalcount yinfinitescrollingmodule"
    },
    {
      "section": "smartEdit",
      "id": "yInfiniteScrollingModule.object:THROTTLE_MILLISECONDS",
      "shortName": "THROTTLE_MILLISECONDS",
      "type": "object",
      "moduleName": "yInfiniteScrollingModule",
      "shortDescription": "Configures the yInfiniteScrolling directive to throttle the page fetching with the value provided in milliseconds.",
      "keywords": "configures directive fetching milliseconds object provided smartedit throttle yinfinitescrolling yinfinitescrollingmodule"
    },
    {
      "section": "smartEditContainer",
      "id": "administration",
      "shortName": "administration",
      "type": "overview",
      "moduleName": "administration",
      "shortDescription": "The administration module",
      "keywords": "administration configurations data display manage module overview point property service services smarteditcontainer uri web"
    },
    {
      "section": "smartEditContainer",
      "id": "administration.ConfigurationEditor",
      "shortName": "administration.ConfigurationEditor",
      "type": "service",
      "moduleName": "administration",
      "shortDescription": "The Configuration Editor Service is a convenience service that provides the methods to manage configurations within the Configuration Editor UI, such as filtering configurations, adding entries and removing entries.",
      "keywords": "actual add addentry adding adds administration angularjs array button call callback called clicks configuration configurationeditor configurationform configurations control convenience delete deleted deletes edited editor entries entry executed false filterconfiguration filtered filtering form formcontroller https init initializes instance list loadcallback loading loads making manage method methods monitor object org parameter remove removeentry removes removing rest returns saves service set smarteditcontainer submit todelete ui user web"
    },
    {
      "section": "smartEditContainer",
      "id": "administration.directive:generalConfiguration",
      "shortName": "generalConfiguration",
      "type": "directive",
      "moduleName": "administration",
      "shortDescription": "The Generation Configuration directive is an HTML marker. It attaches functions of the Configuration Editor to the",
      "keywords": "administration attaches configuration directive display dom editor elements functions general generation html marker order smarteditcontainer template"
    },
    {
      "section": "smartEditContainer",
      "id": "alertServiceModule",
      "shortName": "alertServiceModule",
      "type": "overview",
      "moduleName": "alertServiceModule",
      "shortDescription": "The Alert service module",
      "keywords": "$rootscope alert alerts alertservicemodule bind module overview service smarteditcontainer"
    },
    {
      "section": "smartEditContainer",
      "id": "alertServiceModule.alertService",
      "shortName": "alertServiceModule.alertService",
      "type": "service",
      "moduleName": "alertServiceModule",
      "shortDescription": "The alert service provides a simple wrapper to bind alerts to the $rootScope",
      "keywords": "$rootscope alert alerts alertservice alertservicemodule array bind binds closable code example false message method object objects pushalerts sample service simple smarteditcontainer snippet successful true wrapper"
    },
    {
      "section": "smartEditContainer",
      "id": "authenticationModule",
      "shortName": "authenticationModule",
      "type": "overview",
      "moduleName": "authenticationModule",
      "shortDescription": "The authenticationModule",
      "keywords": "allows application authenticate authentication authenticationmodule entry logout management module overview points resources service smartedit smarteditcontainer"
    },
    {
      "section": "smartEditContainer",
      "id": "authenticationModule.object:DEFAULT_AUTH_MAP",
      "shortName": "DEFAULT_AUTH_MAP",
      "type": "service",
      "moduleName": "authenticationModule",
      "shortDescription": "The default authentication map contains the entry points to use before an authentication map",
      "keywords": "authentication authenticationmodule configuration default entry loaded map object points service smarteditcontainer"
    },
    {
      "section": "smartEditContainer",
      "id": "authenticationModule.object:DEFAULT_CREDENTIALS_MAP",
      "shortName": "DEFAULT_CREDENTIALS_MAP",
      "type": "service",
      "moduleName": "authenticationModule",
      "shortDescription": "The default credentials map contains the credentials to use before an authentication map",
      "keywords": "authentication authenticationmodule configuration credentials default loaded map object service smarteditcontainer"
    },
    {
      "section": "smartEditContainer",
      "id": "authenticationModule.service:authenticationService",
      "shortName": "authenticationService",
      "type": "service",
      "moduleName": "authenticationModule",
      "shortDescription": "The authenticationService is used to authenticate and logout from SmartEdit.",
      "keywords": "access allows application assigned associated authenticate authenticated authenticates authentication authenticationmap authenticationmodule authenticationservice compare current currently default default_authentication_entry_point entered entry entrypoint expiry expression filterentrypoints flag flow identifying indicate indicates isauthenticated isauthentrypoint key landing logout management map maps matching method object point points promise provided re-authenticated re-authentication redirects registered regular relevant removes requested resolves resource resourcelocationsmodule resources retrieve returns service setreauthinprogress shareddataservice shareddataservicemodule smartedit smarteditcontainer stored successful suitable token tokens true uri url user"
    },
    {
      "section": "smartEditContainer",
      "id": "authorizationModule.AuthorizationService",
      "shortName": "authorizationModule.AuthorizationService",
      "type": "service",
      "moduleName": "authorizationModule",
      "shortDescription": "Service that makes calls to the permissions REST API to expose permissions for a given hybris type.",
      "keywords": "api authorizationmodule authorizationservice calls canperformoperation check confirm expose hybris method operation permission permissions rest service smarteditcontainer type"
    },
    {
      "section": "smartEditContainer",
      "id": "authorizationModule.authorizationService",
      "shortName": "authorizationModule.authorizationService",
      "type": "overview",
      "moduleName": "authorizationModule.authorizationService",
      "shortDescription": "The authorization module provides services to fetch operation permissions for hybris types existing in the platform.",
      "keywords": "apis authorization authorizationmodule authorizationservice backend existing fetch hybris module operation order overview permissions platform poll restservicefactorymodule services smarteditcontainer types"
    },
    {
      "section": "smartEditContainer",
      "id": "authorizationModule.directive:hasOperationPermission",
      "shortName": "hasOperationPermission",
      "type": "directive",
      "moduleName": "authorizationModule",
      "shortDescription": "Authorization HTML mark-up that will remove elements from the DOM if the user does not have authorization defined",
      "keywords": "authorization authorizationmodule authorizationservice check current defined directive dom elements has-operation-permission html hybris input key mark-up parameter permission remove service smarteditcontainer type user validate"
    },
    {
      "section": "smartEditContainer",
      "id": "catalogDetailsModule.directive:catalogDetails",
      "shortName": "catalogDetails",
      "type": "directive",
      "moduleName": "catalogDetailsModule",
      "shortDescription": "Directive responsible for displaying a catalog details",
      "keywords": "catalog catalog-details catalogdetailsmodule catalogid catalogversionid default details directive displayed displaying experience redirects responsible smarteditcontainer storefront"
    },
    {
      "section": "smartEditContainer",
      "id": "catalogDetailsModule.service:catalogDetailsService",
      "shortName": "catalogDetailsService",
      "type": "service",
      "moduleName": "catalogDetailsModule",
      "shortDescription": "The catalog details Service will make it possible to add templates in form of directibe",
      "keywords": "add additems allows array catalog catalogdetailsmodule details directibe directive form getitems hold item items list method service smarteditcontainer template templates"
    },
    {
      "section": "smartEditContainer",
      "id": "catalogServiceModule",
      "shortName": "catalogServiceModule",
      "type": "overview",
      "moduleName": "catalogServiceModule",
      "shortDescription": "The catalogServiceModule",
      "keywords": "catalog catalogs catalogservicemodule fetches hybris module overview platform registered service site sites smarteditcontainer"
    },
    {
      "section": "smartEditContainer",
      "id": "catalogServiceModule.service:catalogService",
      "shortName": "catalogService",
      "type": "service",
      "moduleName": "catalogServiceModule",
      "shortDescription": "The Catalog Service fetches catalogs for a specified site or for all sites registered on the hybris platform using",
      "keywords": "api array calls catalog catalogid catalogs catalogservicemodule catalogversion cmswebservices corresponds descriptor descriptors details ersion fetched fetches getallcatalogsgroupedbyid getcatalogsforsite groupings hybris list method platform properties registered rest retrieved service site sites siteuid smarteditcontainer sorted uid version versions"
    },
    {
      "section": "smartEditContainer",
      "id": "clientPagedListModule",
      "shortName": "clientPagedListModule",
      "type": "overview",
      "moduleName": "clientPagedListModule",
      "shortDescription": "The clientPagedListModule",
      "keywords": "allows clientpagedlistmodule custom directive display items list overview paginated renderers search smarteditcontainer sort user"
    },
    {
      "section": "smartEditContainer",
      "id": "clientPagedListModule.directive:clientPagedList",
      "shortName": "clientPagedList",
      "type": "directive",
      "moduleName": "clientPagedListModule",
      "shortDescription": "Directive responsible for displaying a client-side paginated list of items with custom renderers. It allows the user to search and sort the list.",
      "keywords": "$location _buildexperiencepath access allows array bind click client-paged-list client-side clientpagedlistmodule collection column columns current custom data-ng-click descending descriptors directive display display-count displaycount displayed displaying event example experiencepath exposes filter filtered function functions headerpageid headerpagetemplate headerpagetitle headerpagetype headers hitch html i18n iframemanager injectedcontext item items items-per-page itemsperpage key list match ngmodel number object onlink pagelist pagelistctl paginated path properties property query renderer renderers responsible return returns reversed search set setcurrentlocation size smarteditcontainer sort sort-by sorted specific string table template title true typecode uid user values var"
    },
    {
      "section": "smartEditContainer",
      "id": "compileHtmlModule",
      "shortName": "compileHtmlModule",
      "type": "overview",
      "moduleName": "compileHtmlModule",
      "shortDescription": "The compileHtmlModule",
      "keywords": "compile compilehtmlmodule directive evaluate html markup overview smarteditcontainer"
    },
    {
      "section": "smartEditContainer",
      "id": "compileHtmlModule.directive:compileHtml",
      "shortName": "compileHtml",
      "type": "directive",
      "moduleName": "compileHtmlModule",
      "shortDescription": "Directive responsible for evaluating and compiling HTML markup.",
      "keywords": "compile-html compiled compilehtmlmodule compiling data-ng-click directive evaluated evaluating html injectedcontext item markup onlink path property responsible smarteditcontainer string"
    },
    {
      "section": "smartEditContainer",
      "id": "componentHandlerServiceModule",
      "shortName": "componentHandlerServiceModule",
      "type": "overview",
      "moduleName": "componentHandlerServiceModule",
      "shortDescription": "this module aims at handling smartEdit components both on the original storefront and the smartEdit overlay",
      "keywords": "aims componenthandlerservicemodule components handling module original overlay overview smartedit smarteditcontainer storefront"
    },
    {
      "section": "smartEditContainer",
      "id": "componentHandlerServiceModule.COMPONENT_CLASS",
      "shortName": "componentHandlerServiceModule.COMPONENT_CLASS",
      "type": "object",
      "moduleName": "componentHandlerServiceModule",
      "shortDescription": "the css class of the smartEdit components as per contract with the storefront",
      "keywords": "class component_class componenthandlerservicemodule components contract css object smartedit smarteditcontainer storefront"
    },
    {
      "section": "smartEditContainer",
      "id": "componentHandlerServiceModule.componentHandlerService",
      "shortName": "componentHandlerServiceModule.componentHandlerService",
      "type": "service",
      "moduleName": "componentHandlerServiceModule",
      "shortDescription": "The service provides convenient methods to get DOM references of smartEdit components both in the original laye rof the storefornt and in the smartEdit overlay",
      "keywords": "api applicable application child class component componenthandlerservice componenthandlerservicemodule components container container_id_attribute container_type_attribute contentslot contract convenient css cssclass defaults direct div dom element extracts fetched handler id_attribute identified iframe invoked jquery laye layer loaded matching method methods methodsof_getallcomponentsselector methodsof_getallslotsselector methodsof_getcomponent methodsof_getcomponentinoverlay methodsof_getcomponentunderslot methodsof_getfromselector methodsof_getid methodsof_getoriginalcomponent methodsof_getoriginalcomponentwithinslot methodsof_getoverlay methodsof_getpageuid methodsof_getparent methodsof_getparentslotforcomponent methodsof_getslotoperationrelatedid methodsof_getslotoperationrelatedtype methodsof_gettype methodsof_setid methodsof_settype object operations optional original overlay pageuid parameter parent perform references relevant represents resides restrict retrieves rof search searched selector service set sets slot slotid smae-layer smartedit smarteditcomponentid smarteditcomponenttype smarteditcontainer smarteditslotid storefornt storefront string type type_attribute typically wrapper wrapping"
    },
    {
      "section": "smartEditContainer",
      "id": "componentHandlerServiceModule.CONTAINER_ID_ATTRIBUTE",
      "shortName": "componentHandlerServiceModule.CONTAINER_ID_ATTRIBUTE",
      "type": "object",
      "moduleName": "componentHandlerServiceModule",
      "shortDescription": "the id attribute of the smartEdit container, when applicable, as per contract with the storefront",
      "keywords": "applicable attribute componenthandlerservicemodule container container_id_attribute contract object smartedit smarteditcontainer storefront"
    },
    {
      "section": "smartEditContainer",
      "id": "componentHandlerServiceModule.CONTAINER_TYPE_ATTRIBUTE",
      "shortName": "componentHandlerServiceModule.CONTAINER_TYPE_ATTRIBUTE",
      "type": "object",
      "moduleName": "componentHandlerServiceModule",
      "shortDescription": "the type attribute of the smartEdit container, when applicable, as per contract with the storefront",
      "keywords": "applicable attribute componenthandlerservicemodule container container_type_attribute contract object smartedit smarteditcontainer storefront type"
    },
    {
      "section": "smartEditContainer",
      "id": "componentHandlerServiceModule.CONTENT_SLOT_TYPE",
      "shortName": "componentHandlerServiceModule.CONTENT_SLOT_TYPE",
      "type": "object",
      "moduleName": "componentHandlerServiceModule",
      "shortDescription": "the type value of the smartEdit slots as per contract with the storefront",
      "keywords": "componenthandlerservicemodule content_slot_type contract object slots smartedit smarteditcontainer storefront type"
    },
    {
      "section": "smartEditContainer",
      "id": "componentHandlerServiceModule.ID_ATTRIBUTE",
      "shortName": "componentHandlerServiceModule.ID_ATTRIBUTE",
      "type": "object",
      "moduleName": "componentHandlerServiceModule",
      "shortDescription": "the id attribute of the smartEdit components as per contract with the storefront",
      "keywords": "attribute componenthandlerservicemodule components contract id_attribute object smartedit smarteditcontainer storefront"
    },
    {
      "section": "smartEditContainer",
      "id": "componentHandlerServiceModule.OVERLAY_COMPONENT_CLASS",
      "shortName": "componentHandlerServiceModule.OVERLAY_COMPONENT_CLASS",
      "type": "object",
      "moduleName": "componentHandlerServiceModule",
      "shortDescription": "the css class of the smartEdit component clones copied to the storefront overlay",
      "keywords": "class clones component componenthandlerservicemodule copied css object overlay overlay_component_class smartedit smarteditcontainer storefront"
    },
    {
      "section": "smartEditContainer",
      "id": "componentHandlerServiceModule.OVERLAY_ID",
      "shortName": "componentHandlerServiceModule.OVERLAY_ID",
      "type": "object",
      "moduleName": "componentHandlerServiceModule",
      "shortDescription": "the identifier of the overlay placed in front of the storefront to where all smartEdit component decorated clones are copied.",
      "keywords": "clones component componenthandlerservicemodule copied decorated front identifier object overlay overlay_id smartedit smarteditcontainer storefront"
    },
    {
      "section": "smartEditContainer",
      "id": "componentHandlerServiceModule.SMARTEDIT_ATTRIBUTE_PREFIX",
      "shortName": "componentHandlerServiceModule.SMARTEDIT_ATTRIBUTE_PREFIX",
      "type": "object",
      "moduleName": "componentHandlerServiceModule",
      "shortDescription": "If the storefront needs to expose more attributes than the minimal contract, these attributes must be prefixed with this constant value",
      "keywords": "attributes componenthandlerservicemodule constant contract expose minimal object prefixed smartedit_attribute_prefix smarteditcontainer storefront"
    },
    {
      "section": "smartEditContainer",
      "id": "componentHandlerServiceModule.TYPE_ATTRIBUTE",
      "shortName": "componentHandlerServiceModule.TYPE_ATTRIBUTE",
      "type": "object",
      "moduleName": "componentHandlerServiceModule",
      "shortDescription": "the type attribute of the smartEdit components as per contract with the storefront",
      "keywords": "attribute componenthandlerservicemodule components contract object smartedit smarteditcontainer storefront type type_attribute"
    },
    {
      "section": "smartEditContainer",
      "id": "confirmationModalServiceModule",
      "shortName": "confirmationModalServiceModule",
      "type": "overview",
      "moduleName": "confirmationModalServiceModule",
      "shortDescription": "The confirmationModalServiceModule",
      "keywords": "allows confirmation confirmationmodalservicemodule content dependent modal modalservicemodule module opening overview prompt service smarteditcontainer title"
    },
    {
      "section": "smartEditContainer",
      "id": "confirmationModalServiceModule.service:confirmationModalService",
      "shortName": "confirmationModalService",
      "type": "service",
      "moduleName": "confirmationModalServiceModule",
      "shortDescription": "Service used to open a confirmation modal in which an end-user can confirm or cancel an action. A confirmation modal",
      "keywords": "action actioned button cancel configuration confirm confirmation confirmationmodalservicemodule consists content context default description displayed end-user i18n initialized input key method modal modalservice modalservicemodule object open optional override overrides passed promise property provided rejected required resolved service smarteditcontainer text title user"
    },
    {
      "section": "smartEditContainer",
      "id": "containerComponentHandlerServiceModule",
      "shortName": "containerComponentHandlerServiceModule",
      "type": "overview",
      "moduleName": "containerComponentHandlerServiceModule",
      "shortDescription": "this module aims at handling smartEdit container components both on the original storefront and the smartEdit overlay",
      "keywords": "aims components container containercomponenthandlerservicemodule handling module original overlay overview smartedit smarteditcontainer storefront"
    },
    {
      "section": "smartEditContainer",
      "id": "dateTimePickerModule",
      "shortName": "dateTimePickerModule",
      "type": "overview",
      "moduleName": "dateTimePickerModule",
      "shortDescription": "The dateTimePickerModule",
      "keywords": "datetimepicker datetimepickerlocalizationservice datetimepickermodule directive displaying localize module open opened overview picker service smarteditcontainer time tooling"
    },
    {
      "section": "smartEditContainer",
      "id": "dateTimePickerModule.directive:dateTimePicker",
      "shortName": "dateTimePicker",
      "type": "directive",
      "moduleName": "dateTimePickerModule",
      "shortDescription": "The dateTimePicker",
      "keywords": "datetimepicker datetimepickermodule directive smarteditcontainer"
    },
    {
      "section": "smartEditContainer",
      "id": "dateTimePickerModule.object: tooltipsMap",
      "shortName": "tooltipsMap",
      "type": "object",
      "moduleName": "dateTimePickerModule",
      "shortDescription": "Contains a map of all tooltips to be localized in the date time picker",
      "keywords": "datetimepickermodule localized map object picker smarteditcontainer time tooltips tooltipsmap"
    },
    {
      "section": "smartEditContainer",
      "id": "dateTimePickerModule.object:resolvedLocaleToMomentLocaleMap",
      "shortName": "resolvedLocaleToMomentLocaleMap",
      "type": "object",
      "moduleName": "dateTimePickerModule",
      "shortDescription": "Contains a map of all inconsistent locales ISOs between SmartEdit and MomentJS",
      "keywords": "datetimepickermodule inconsistent isos locales map momentjs object smartedit smarteditcontainer"
    },
    {
      "section": "smartEditContainer",
      "id": "dateTimePickerModule.service:dateTimePickerLocalizationService",
      "shortName": "dateTimePickerLocalizationService",
      "type": "service",
      "moduleName": "dateTimePickerModule",
      "shortDescription": "The dateTimePickerLocalizationService is responsible for both localizing the date time picker as well as the tooltips",
      "keywords": "datetimepickerlocalizationservice datetimepickermodule localizing picker responsible service smarteditcontainer time tooltips"
    },
    {
      "section": "smartEditContainer",
      "id": "dragAndDropScrollingModule",
      "shortName": "dragAndDropScrollingModule",
      "type": "overview",
      "moduleName": "dragAndDropScrollingModule",
      "shortDescription": "The dragAndDropScrollingModule",
      "keywords": "allows component controlling draganddropscrollingmodule dragging module overview scrolls service smartedit smarteditcontainer"
    },
    {
      "section": "smartEditContainer",
      "id": "dragAndDropScrollingModule.service:dragAndDropScrollingService",
      "shortName": "dragAndDropScrollingService",
      "type": "service",
      "moduleName": "dragAndDropScrollingModule",
      "shortDescription": "The Drag and Drop Scrolling service manages the scrolling of a page while dragging a SmartEdit component. It creates,",
      "keywords": "allow appends attaches base called communication component configure connectwithdraganddrop creates current cursor disables disablescrolling drag draganddropconfiguration draganddropscrollingmodule draganddropserviceinterface draganddropserviceinterfacemodule dragged dragging drop enables enablescrolling event executed flag frame handlers hides hints hovered hovers indicates initialize intended isexternalframe manages method object outer prepares scroll scrolling service smartedit smarteditcontainer started startedinexternalframe stops ui"
    },
    {
      "section": "smartEditContainer",
      "id": "dragAndDropServiceInterfaceModule.DragAndDropServiceInterface",
      "shortName": "dragAndDropServiceInterfaceModule.DragAndDropServiceInterface",
      "type": "service",
      "moduleName": "dragAndDropServiceInterfaceModule",
      "shortDescription": "Provides an abstract extensible drag and drop service. Used to manage and perform actions to either the SmartEdit",
      "keywords": "abstract actions application callback class configuration container drag draganddropserviceinterface draganddropserviceinterfacemodule dragged dragging drop dropcallback dropped element enters exits extended extensible handle identify iframe instantiated interface items jquery locations manage method mouse outcallback overcallback perform register scope selector selectot serves service smartedit smarteditcontainer sortable sourceselector startcallback starts targetselector"
    },
    {
      "section": "smartEditContainer",
      "id": "eventServiceModule",
      "shortName": "eventServiceModule",
      "type": "overview",
      "moduleName": "eventServiceModule",
      "shortDescription": "eventServiceModule contains an event service which is supported by the SmartEdit gatewayFactory to propagate events between SmartEditContainer and SmartEdit.",
      "keywords": "event events eventservicemodule gatewayfactory gatewayfactorymodule overview propagate service smartedit smarteditcontainer supported"
    },
    {
      "section": "smartEditContainer",
      "id": "eventServiceModule.EVENT_SERVICE_MODE_ASYNCH",
      "shortName": "eventServiceModule.EVENT_SERVICE_MODE_ASYNCH",
      "type": "object",
      "moduleName": "eventServiceModule",
      "shortDescription": "A constant used in the constructor of the Event Service to specify asynchronous event transmission.",
      "keywords": "asynchronous constant event event_service_mode_asynch eventservicemodule object service smarteditcontainer transmission"
    },
    {
      "section": "smartEditContainer",
      "id": "eventServiceModule.EVENT_SERVICE_MODE_SYNCH",
      "shortName": "eventServiceModule.EVENT_SERVICE_MODE_SYNCH",
      "type": "object",
      "moduleName": "eventServiceModule",
      "shortDescription": "A constant that is used in the constructor of the Event Service to specify synchronous event transmission.",
      "keywords": "constant event event_service_mode_synch eventservicemodule object service smarteditcontainer synchronous transmission"
    },
    {
      "section": "smartEditContainer",
      "id": "eventServiceModule.EVENTS",
      "shortName": "eventServiceModule.EVENTS",
      "type": "object",
      "moduleName": "eventServiceModule",
      "shortDescription": "Events that are fired/handled in the SmartEdit application",
      "keywords": "application events eventservicemodule fired object smartedit smarteditcontainer"
    },
    {
      "section": "smartEditContainer",
      "id": "eventServiceModule.EventService",
      "shortName": "eventServiceModule.EventService",
      "type": "service",
      "moduleName": "eventServiceModule",
      "shortDescription": "The event service is used to transmit events synchronously or asynchronously. It also contains options to send",
      "keywords": "asynchronous asynchronously constant constants data defaultmode depending event event_service_mode_asynch event_service_mode_synch eventid events eventservice eventservicemodule handlers identifier method mode options payload register send sendevent service set sets smarteditcontainer synchronous synchronously transmission transmit unregister"
    },
    {
      "section": "smartEditContainer",
      "id": "ExperienceInterceptorModule.experienceInterceptor",
      "shortName": "ExperienceInterceptorModule.experienceInterceptor",
      "type": "service",
      "moduleName": "ExperienceInterceptorModule",
      "shortDescription": "A HTTP request interceptor which intercepts all &#39;cmswebservices/catalogs&#39; requests and adds the current catalog and version",
      "keywords": "$httpprovider adding adds angularjs array called catalog cmswebservices config configuration current current_context_catalog current_context_catalog_version data define dependencies details experience experienceinterceptor experienceinterceptormodule factories factory headers holds http https initialization injected interceptor interceptors intercepts method methods note object org passed preview promise registered request requests retrieve returns service set shared smarteditcontainer stored uri url variables version versions"
    },
    {
      "section": "smartEditContainer",
      "id": "experienceServiceModule.service:experienceService",
      "shortName": "experienceService",
      "type": "service",
      "moduleName": "experienceServiceModule",
      "shortDescription": "The experience Service deals with building experience objects given a context.",
      "keywords": "builddefaultexperience building catalogid catalogversion context deals experience experienceservicemodule method object objects reconstructed return service siteid smarteditcontainer"
    },
    {
      "section": "smartEditContainer",
      "id": "featureInterfaceModule",
      "shortName": "featureInterfaceModule",
      "type": "overview",
      "moduleName": "featureInterfaceModule",
      "keywords": "featureinterfacemodule overview smarteditcontainer"
    },
    {
      "section": "smartEditContainer",
      "id": "featureInterfaceModule.service:FeatureServiceInterface",
      "shortName": "FeatureServiceInterface",
      "type": "service",
      "moduleName": "featureInterfaceModule",
      "shortDescription": "The interface stipulates how to register features in the SmartEdit application and the SmartEdit container.",
      "keywords": "_registeraliases action activate addcontextualmenubutton adddecorator additems addmappings addtoolbaritem ant-like api applicable application applied bound button buttons callback callbacks classes clicked clicking component componentid components componenttype concrete condition configuration container containerid containertype content contextual contextualmenuservice contextualmenuservicemodule cross css deal decorator decorators decoratorservice decoratorservicemodule default defined delegates description descriptioni18nkey disable disabled disablingcallback displayclass displayed dom element eligible enable enabled enablingcallback entry event expression feature featureinterfacemodule features frame full function functions gateway gatewayfactory gatewayfactorymodule handler hold holding html hybrid_action i18n icon iconidle iconnonidle icons identified identifies idle image images implementation include instance instances interface invocation invoke invoked item items key keythe l18n list location mappings match meant menu method methods methods_additems methods_disable methods_enable methods_register namei18nkey needed non-idle object optional options order parameter performed perspective perspectiveinterfacemodule perspectiveserviceinterface point reference regexpkey register registered registers registry regular removed representing represents required respective selected selects separate service simplified slot slotid smaller smallicon smartedit smarteditcontainer stand stipulates stores strict style template templates toolbar toolbarid toolbarinterfacemodule toolbarserviceinterface tooltip translated triggered type types unique uniquely url urls user version web wildcard wrapper wrapping"
    },
    {
      "section": "smartEditContainer",
      "id": "FetchDataHandlerInterfaceModule.FetchDataHandlerInterface",
      "shortName": "FetchDataHandlerInterfaceModule.FetchDataHandlerInterface",
      "type": "service",
      "moduleName": "FetchDataHandlerInterfaceModule",
      "shortDescription": "Interface describing the contract of a fetchDataHandler fetched through dependency injection by the",
      "keywords": "contract defined dependency describing descriptor dropdowns eligible entities entity fetch fetchdatahandler fetchdatahandlerinterface fetchdatahandlerinterfacemodule fetched field findbymask genericeditor genericeditormodule getbyid identifier identifying injection interface list mask matching method populate promise resolving returns search service smarteditcontainer type witch"
    },
    {
      "section": "smartEditContainer",
      "id": "fetchMediaDataHandlerModule.service:FetchMediaDataHandler",
      "shortName": "FetchMediaDataHandler",
      "type": "service",
      "moduleName": "fetchMediaDataHandlerModule",
      "shortDescription": "implementation of FetchDataHandlerInterface for &quot;Media&quot; cmsStructureType",
      "keywords": "api assets cmsstructuretype descriptor fetchdatahandlerinterface fetchdatahandlerinterfacemodule fetchmediadatahandlermodule field filtered findbymask format fullurltomedia1 fullurltomedia2 genericeditor genericeditormodule identifier implementation list located mask media method query rest returned service smarteditcontainer someid1 someid2 url"
    },
    {
      "section": "smartEditContainer",
      "id": "functionsModule",
      "shortName": "functionsModule",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "provides a list of useful functions that can be used as part of the SmartEdit framework.",
      "keywords": "framework functions functionsmodule list service smartedit smarteditcontainer"
    },
    {
      "section": "smartEditContainer",
      "id": "functionsModule.convertToArray",
      "shortName": "functionsModule.convertToArray",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "convertToArray will convert the given object to array.",
      "keywords": "array convert converttoarray created elements functionsmodule input inputobject key object original output service smarteditcontainer"
    },
    {
      "section": "smartEditContainer",
      "id": "functionsModule.copy",
      "shortName": "functionsModule.copy",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "copy will do a deep copy of the given input object.",
      "keywords": "candidate copied copy deep functionsmodule input javascript object service smarteditcontainer"
    },
    {
      "section": "smartEditContainer",
      "id": "functionsModule.customTimeout",
      "shortName": "functionsModule.customTimeout",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "customTimeout will call the javascrit&#39;s native setTimeout method to execute a given function after a specified period of time.",
      "keywords": "$timeout assert better call customtimeout difficult duration end-to-end execute executed func function functionsmodule javascrit method milliseconds native period service settimeout smarteditcontainer testing time"
    },
    {
      "section": "smartEditContainer",
      "id": "functionsModule.escapeHtml",
      "shortName": "functionsModule.escapeHtml",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "escapeHtml will escape &amp;, &lt;, &gt;, &quot; and &#39; characters .",
      "keywords": "characters escape escaped escapehtml functionsmodule service smarteditcontainer string"
    },
    {
      "section": "smartEditContainer",
      "id": "functionsModule.extend",
      "shortName": "functionsModule.extend",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "extend provides a convenience to either default a new child or &quot;extend&quot; an existing child with the prototype of the parent",
      "keywords": "child childclass convenience default existing extend extended functionsmodule parent parentclass prototype service set smarteditcontainer"
    },
    {
      "section": "smartEditContainer",
      "id": "functionsModule.extractFromElement",
      "shortName": "functionsModule.extractFromElement",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "parses a string HTML into a queriable DOM object",
      "keywords": "dom element elements extract extracted extractfromelement extractionselector functionsmodule html identifying jquery matching object parent parses queriable selector selectors service smarteditcontainer string"
    },
    {
      "section": "smartEditContainer",
      "id": "functionsModule.generateIdentifier",
      "shortName": "functionsModule.generateIdentifier",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "generateIdentifier will generate a unique string based on system time and a random generator.",
      "keywords": "based functionsmodule generate generateidentifier generator identifier random service smarteditcontainer string system time unique"
    },
    {
      "section": "smartEditContainer",
      "id": "functionsModule.getOrigin",
      "shortName": "functionsModule.getOrigin",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "returns document location origin",
      "keywords": "browsers caters document function functionsmodule gap getorigin location origin returns service smarteditcontainer support w3c"
    },
    {
      "section": "smartEditContainer",
      "id": "functionsModule.getQueryString",
      "shortName": "functionsModule.getQueryString",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "getQueryString will convert a given object into a query string.",
      "keywords": "code convert functionsmodule getquerystring input key1 key2 key3 list object output params query sample service smarteditcontainer snippet string value1 value2 value3 var"
    },
    {
      "section": "smartEditContainer",
      "id": "functionsModule.getURI",
      "shortName": "functionsModule.getURI",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "Will return the URI part of a URL",
      "keywords": "functionsmodule geturi return returned service smarteditcontainer uri url"
    },
    {
      "section": "smartEditContainer",
      "id": "functionsModule.hitch",
      "shortName": "functionsModule.hitch",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "hitch will create a new function that will pass our desired context (scope) to the given function.",
      "keywords": "assigned binded binding context create desired function functionsmodule hitch method parameters pass pre-bind scope service smarteditcontainer"
    },
    {
      "section": "smartEditContainer",
      "id": "functionsModule.injectJS",
      "shortName": "functionsModule.injectJS",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "injectJS will inject script tags into html for a given set of sources.",
      "keywords": "array callback callbacks configuration configurations execute extract functionsmodule html inject injectjs javascript method object potential provided script service set smarteditcontainer source sources tag tags triggered wired"
    },
    {
      "section": "smartEditContainer",
      "id": "functionsModule.isBlank",
      "shortName": "functionsModule.isBlank",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "isBlank will check if a given string is undefined or null or empty.",
      "keywords": "check empty false functionsmodule input inputstring isblank null returns service smarteditcontainer string true undefined"
    },
    {
      "section": "smartEditContainer",
      "id": "functionsModule.merge",
      "shortName": "functionsModule.merge",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "merge will merge the contents of two objects together into the first object.",
      "keywords": "contents functionsmodule javascript merge object objects result service smarteditcontainer source target"
    },
    {
      "section": "smartEditContainer",
      "id": "functionsModule.parseHTML",
      "shortName": "functionsModule.parseHTML",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "parses a string HTML into a queriable DOM object, stripping any JavaScript from the HTML.",
      "keywords": "dom functionsmodule html javascript object parse parsehtml parses queriable representation service smarteditcontainer string stringhtml stripping"
    },
    {
      "section": "smartEditContainer",
      "id": "functionsModule.parseQuery",
      "shortName": "functionsModule.parseQuery",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "parseQuery will convert a given query string to an object.",
      "keywords": "code convert functionsmodule input key1 key2 key3 object output params parsed parsequery query sample service smarteditcontainer snippet string value1 value2 value3 var"
    },
    {
      "section": "smartEditContainer",
      "id": "functionsModule.regExpFactory",
      "shortName": "functionsModule.regExpFactory",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "regExpFactory will convert a given pattern into a regular expression.",
      "keywords": "append convert converted expression functionsmodule generated method pattern prepend proper regex regexpfactory regular replaces service smarteditcontainer string wildcards"
    },
    {
      "section": "smartEditContainer",
      "id": "functionsModule.sanitizeHTML",
      "shortName": "functionsModule.sanitizeHTML",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "sanitizeHTML will remove breaks and space .",
      "keywords": "breaks escaped functionsmodule html remove sanitized sanitizehtml service smarteditcontainer space string"
    },
    {
      "section": "smartEditContainer",
      "id": "functionsModule.toPromise",
      "shortName": "functionsModule.toPromise",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "toPromise&lt;/&gt; transforms a function into a function that is guaranteed to return a Promise that resolves to the",
      "keywords": "function functionsmodule guaranteed original promise resolves return service smarteditcontainer topromise transforms"
    },
    {
      "section": "smartEditContainer",
      "id": "functionsModule.trim",
      "shortName": "functionsModule.trim",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "trim will remove spaces at the beginning and end of a given string.",
      "keywords": "functionsmodule input inputstring modified newly remove service smarteditcontainer spaces string trim"
    },
    {
      "section": "smartEditContainer",
      "id": "functionsModule.uniqueArray",
      "shortName": "functionsModule.uniqueArray",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "uniqueArray will return the first Array argument supplemented with new entries from the second Array argument.",
      "keywords": "argument array array1 array2 entries functionsmodule javascript return second service smarteditcontainer supplemented uniquearray"
    },
    {
      "section": "smartEditContainer",
      "id": "functionsModule.unsafeParseHTML",
      "shortName": "functionsModule.unsafeParseHTML",
      "type": "service",
      "moduleName": "functionsModule",
      "shortDescription": "parses a string HTML into a queriable DOM object, preserving any JavaScript present in the HTML.",
      "keywords": "dom failure functionsmodule html javascript location note object originating parse parses preserves preserving queriable representation result safe service smarteditcontainer string stringhtml strings unsafeparsehtml vulnerability xss"
    },
    {
      "section": "smartEditContainer",
      "id": "gatewayFactoryModule.gatewayFactory",
      "shortName": "gatewayFactoryModule.gatewayFactory",
      "type": "service",
      "moduleName": "gatewayFactoryModule",
      "shortDescription": "The Gateway Factory controls the creation of and access to MessageGateway",
      "keywords": "access application argument caches call calls channel clients construct controls corresponding create created creategateway creates creation dispatches error event events exist factory fail gateway gatewayfactory gatewayfactorymodule gatewayid handle handler handling identifier initializes initlistener instances lifecycle logged message messagegateway method newly null order postmessage prevent provide return returns second service smarteditcontainer subsequent"
    },
    {
      "section": "smartEditContainer",
      "id": "gatewayFactoryModule.MessageGateway",
      "shortName": "gatewayFactoryModule.MessageGateway",
      "type": "service",
      "moduleName": "gatewayFactoryModule",
      "shortDescription": "The Message Gateway is a private channel that is used to publish and subscribe to events across iFrame",
      "keywords": "angularjs attempt attempts based benefits boundaries callback chain channel controlled creation cross-origin current data event eventid events exist failure function gateway gatewayfactory gatewayfactorymodule gatewayid generated identifier iframe implementation implements instance instances interrupted invoked key listener message messagegateway messages method number occurs optional origins parameter payload pk postmessage primary private promise promises publish publishes receiving registers reject rejected resolve retries scenarios send service side smarteditcontainer subscribe technology underlying w3c-compliant works"
    },
    {
      "section": "smartEditContainer",
      "id": "gatewayFactoryModule.object:TIMEOUT_TO_RETRY_PUBLISHING",
      "shortName": "TIMEOUT_TO_RETRY_PUBLISHING",
      "type": "object",
      "moduleName": "gatewayFactoryModule",
      "shortDescription": "Period between two retries of a gatewayFactoryModule.MessageGateway to publish an event",
      "keywords": "browser event explorer frames gatewayfactorymodule greater internet messagegateway needed object period postmessage process publish retries smarteditcontainer time"
    },
    {
      "section": "smartEditContainer",
      "id": "gatewayFactoryModule.object:WHITE_LISTED_STOREFRONTS_CONFIGURATION_KEY",
      "shortName": "WHITE_LISTED_STOREFRONTS_CONFIGURATION_KEY",
      "type": "object",
      "moduleName": "gatewayFactoryModule",
      "shortDescription": "the name of the configuration key containing the list of white listed storefront domain names",
      "keywords": "configuration domain gatewayfactorymodule key list listed names object smarteditcontainer storefront white"
    },
    {
      "section": "smartEditContainer",
      "id": "gatewayProxyModule.gatewayProxy",
      "shortName": "gatewayProxyModule.gatewayProxy",
      "type": "service",
      "moduleName": "gatewayProxyModule",
      "shortDescription": "To seamlessly integrate the gateway factory between two services on different frames, you can use a gateway",
      "keywords": "allowing api assigned attaches automatically avoid body call calls communication declared delegates eligible empty factory fail forward frames function functions gateway gatewayfactory gatewayfactorymodule gatewayid gatewayproxy gatewayproxymodule initforservice inner instance instances integrate listeners method methods methodssubset module mutate mutates optional process promise promises providing proxied proxy publish registers registration requires result seamlessly service services simplifies smarteditcontainer stub subset trigger turned unnecessarily wraps"
    },
    {
      "section": "smartEditContainer",
      "id": "genericEditorModule",
      "shortName": "genericEditorModule",
      "type": "overview",
      "moduleName": "genericEditorModule",
      "keywords": "genericeditormodule overview smarteditcontainer"
    },
    {
      "section": "smartEditContainer",
      "id": "genericEditorModule.directive:genericEditor",
      "shortName": "genericEditor",
      "type": "directive",
      "moduleName": "genericEditorModule",
      "shortDescription": "Directive responsible for generating custom HTML CRUD form for any smarteditComponent type.",
      "keywords": "api binding button cancel component content contentapi contract create created creates crud custom data delete deleted described directive display dom element exposes extracted form fulfills function generating generic-editor genericeditor genericeditormodule html identifier inner instance invoker link local method optional original parameter read reset responsible rest scope service set sets smartedit smartedit-component-id smartedit-component-type smarteditcomponent smarteditcomponentid smarteditcomponenttype smarteditcontainer storefront structure structureapi submit type update updated"
    },
    {
      "section": "smartEditContainer",
      "id": "genericEditorModule.service:GenericEditor",
      "shortName": "GenericEditor",
      "type": "service",
      "moduleName": "genericEditorModule",
      "shortDescription": "The Generic Editor is a class that makes it possible for SmartEdit users (CMS managers, editors, etc.) to edit components in the SmartEdit interface.",
      "keywords": "actual advised api array associated attempted attributes boolean build built call checkbox class cmsstructureenumtype cmsstructuretype code comparing component componentform components content convention crud current data datahandler de default details determine determined directive dirty display displayed displays documentation dropdown dropdowns edit editable edited editor editors en english entries entry enum error errors example expect expected expression fail fallback false fetch fetchdatahandlerinterface fetchdatahandlerinterfacemodule fetches fetchmediadatahandler fetchmediadatahandlermodule field filter filterable filtered form format fr french fulfill function generic genericeditor genericeditormodule handles hindi holds html i18nkey i18nkeyforsomequalifier1 i18nkeyforsomequalifier2 i18nkeyforsomequalifier3 i18nkeyforsomequalifier4 i18nkeyforsomequalifier5 i18nkeyforsomequalifier6 i18nkeyforsomequalifier7 identified identifier implementation implies indicates indicator initialization input interface invoked isdirty iso json key label language learn list local localized long longstring manage managers mandatory map mask match media message method methods_refreshoptions modified mypackage naming native non-localized object occurs option orientation original pattern payload payloads performing picker populates predicate pristine property provided qualified qualifier qualifier1 qualifier2 qualifier3 reads reason received refreshoptions regular request requested requests required requirements requires reset response rest return returned returns richtext saved saves saving search serialized service sets shipped shortstring smartedit smarteditcontainer somequalifier1 somequalifier2 somequalifier3 somequalifier4 somequalifier5 somequalifier6 somequalifier7 sort specific string strings structure subject submit support templates text textarea timestamps translated true type types update updated updates users validation validationerror values widget widgets"
    },
    {
      "section": "smartEditContainer",
      "id": "hasOperationPermissionModule",
      "shortName": "hasOperationPermissionModule",
      "type": "overview",
      "moduleName": "hasOperationPermissionModule",
      "shortDescription": "This module provides a directive used to determine if the current user has permission to perform the action defined",
      "keywords": "action current defined determine directive dom elements hasoperationpermissionmodule key module overview perform permission remove smarteditcontainer user"
    },
    {
      "section": "smartEditContainer",
      "id": "httpAuthInterceptorModule.httpAuthInterceptor",
      "shortName": "httpAuthInterceptorModule.httpAuthInterceptor",
      "type": "service",
      "moduleName": "httpAuthInterceptorModule",
      "shortDescription": "Provides a way for global authentication by intercepting the requests before handing them to the server",
      "keywords": "$http $httpprovider adding adds application array authentication based call called code config configuration dependencies displaying error errors explain factories factory failed failures forwards global handing handles holds http httpauthinterceptor httpauthinterceptormodule injected intercepted intercepting interceptor interceptors intercepts message method methods object parameters registered request requests resource response responseerror responses rest returns server service smarteditcontainer status token"
    },
    {
      "section": "smartEditContainer",
      "id": "httpErrorInterceptorModule",
      "shortName": "httpErrorInterceptorModule",
      "type": "service",
      "moduleName": "httpErrorInterceptorModule",
      "shortDescription": "Module that provides a service called httpErrorInterceptor",
      "keywords": "access called error errors handling httperrorinterceptor httperrorinterceptormodule module occ response service smarteditcontainer validation"
    },
    {
      "section": "smartEditContainer",
      "id": "httpErrorInterceptorModule.httpErrorInterceptor",
      "shortName": "httpErrorInterceptorModule.httpErrorInterceptor",
      "type": "service",
      "moduleName": "httpErrorInterceptorModule",
      "shortDescription": "Provides a way for global error handling by intercepting the requests before handing them to the server",
      "keywords": "$httpprovider access adding angularjs application array based called code dependencies error errors explain factories factory failed global handing handles handling httperrorinterceptor httperrorinterceptormodule https injected intercepting interceptor interceptors intercepts method methods object occ org parameters promise registered rejected requests resolved response responseerror responses returns server service smarteditcontainer status validation"
    },
    {
      "section": "smartEditContainer",
      "id": "i18nInterceptorModule.object:I18NAPIROOT",
      "shortName": "I18NAPIROOT",
      "type": "object",
      "moduleName": "i18nInterceptorModule",
      "shortDescription": "The I18NAPIroot is a hard-coded URI that is used to initialize the translationServiceModule.",
      "keywords": "hard-coded i18n_resource_uri i18napiroot i18ninterceptor i18ninterceptormodule initialize intercepts methods_request object replaces request resourcelocationsmodule service smarteditcontainer translationservicemodule uri"
    },
    {
      "section": "smartEditContainer",
      "id": "i18nInterceptorModule.object:UNDEFINED_LOCALE",
      "shortName": "UNDEFINED_LOCALE",
      "type": "object",
      "moduleName": "i18nInterceptorModule",
      "shortDescription": "The undefined locale set as the preferred language of the translationServiceModule so that",
      "keywords": "browser i18ninterceptor i18ninterceptormodule intercept language locale methods_request object preferred replace request service set smarteditcontainer translationservicemodule undefined"
    },
    {
      "section": "smartEditContainer",
      "id": "i18nInterceptorModule.service:i18nInterceptor",
      "shortName": "i18nInterceptor",
      "type": "service",
      "moduleName": "i18nInterceptorModule",
      "shortDescription": "A HTTP request interceptor that intercepts all i18n calls and handles them as required in the i18nInterceptor.request method.",
      "keywords": "$httpprovider adding angularjs appends array called calls config configuration defined dependencies factories factory getresolvelocale handles http https i18n i18n_resource_uri i18napiroot i18ninterceptor i18ninterceptormodule injected interceptor interceptors intercepts invoked languageservice languageservicemodule locale method methods methods_getresolvelocale methods_request object org passed promise provided registered replaces request requests required resourcelocationsmodule retrieved returns service smarteditcontainer url"
    },
    {
      "section": "smartEditContainer",
      "id": "iframeClickDetectionServiceModule",
      "shortName": "iframeClickDetectionServiceModule",
      "type": "overview",
      "moduleName": "iframeClickDetectionServiceModule",
      "shortDescription": "The iframeClickDetectionServiceModule",
      "keywords": "application click container detection events functionality gateway iframe iframeclickdetectionservicemodule listen module mousedown overview proxy requires smartedit smarteditcontainer"
    },
    {
      "section": "smartEditContainer",
      "id": "iframeClickDetectionServiceModule.service:iframeClickDetectionService",
      "shortName": "iframeClickDetectionService",
      "type": "service",
      "moduleName": "iframeClickDetectionServiceModule",
      "shortDescription": "The iframe Click Detection service uses the  gatewayProxy service to listen",
      "keywords": "application callback callbacks click currently detection events function functionality gatewayproxy gatewayproxymodule iframe iframeclickdetectionservicemodule listen listener method mousedown occurs oniframeclick register registercallback registered registers remove removecallback removes service smartedit smarteditcontainer triggered triggers"
    },
    {
      "section": "smartEditContainer",
      "id": "iFrameManagerModule",
      "shortName": "iFrameManagerModule",
      "type": "service",
      "moduleName": "iFrameManagerModule",
      "shortDescription": "Module that provides a service called iFrameManager which has a set of methods",
      "keywords": "called iframe iframemanager iframemanagermodule load methods module service set smarteditcontainer storefront"
    },
    {
      "section": "smartEditContainer",
      "id": "iFrameManagerModule.iFrameManager",
      "shortName": "iFrameManagerModule.iFrameManager",
      "type": "service",
      "moduleName": "iFrameManagerModule",
      "shortDescription": "The iFrame Manager service provides methods to load the storefront into an iframe. The preview of the storefront can be loaded for a specified input homepage and a specified preview ticket. The iframe src attribute is updated with that information in order to display the storefront in SmartEdit.",
      "keywords": "add ajax api append attribute boolean call called catalog check constraint content-type context current data display exist exists experience homepage homepageorpagefrompagelist iframe iframemanager iframemanagermodule indicating initial initialize initializecatalogpreview input landing language list load loaded loading loadpreview loads location manager method methods mode modified order preview previewticket prior query redirected requested returns select server service set setcurrentlocation sets setting shared smartedit smarteditcontainer src stored storefront stores string subsequent ticket time updated uri url user"
    },
    {
      "section": "smartEditContainer",
      "id": "interceptorHelperModule.service:interceptorHelper",
      "shortName": "interceptorHelper",
      "type": "service",
      "moduleName": "interceptorHelperModule",
      "shortDescription": "Helper service used to handle request and response in interceptors",
      "keywords": "body callback config error function handle handled handles helper initial interceptor interceptorhelpermodule interceptors method methodsof_handlerequest methodsof_handleresponse methodsof_handleresponseerror object promise rejecting request resolving response return service smarteditcontainer success"
    },
    {
      "section": "smartEditContainer",
      "id": "l10nModule.filter:l10n",
      "shortName": "l10n",
      "type": "filter",
      "moduleName": "l10nModule",
      "shortDescription": "Filter that accepts a localized map as input and returns the value corresponding to the resolvedLocale of languageServiceModule and defaults to the first entry.",
      "keywords": "accepts class corresponding defaults entry extended filter input instantiated interface isocodes l10nmodule language languageservicemodule localized localizedmap map resolvedlocale returns serves smarteditcontainer values"
    },
    {
      "section": "smartEditContainer",
      "id": "languageSelectorModule",
      "shortName": "languageSelectorModule",
      "type": "overview",
      "moduleName": "languageSelectorModule",
      "shortDescription": "The language selector module contains a directive which allow the user to select a language.",
      "keywords": "allow api backend call directive language languages languageselectormodule languageservice languageservicemodule list module order overview select selector service smarteditcontainer supported user"
    },
    {
      "section": "smartEditContainer",
      "id": "languageSelectorModule.directive:languageSelector",
      "shortName": "languageSelector",
      "type": "directive",
      "moduleName": "languageSelectorModule",
      "shortDescription": "Language selector provides a drop-down list which contains a list of supported languages.",
      "keywords": "directive drop-down language languages languageselectormodule list select selector smarteditcontainer supported system translate"
    },
    {
      "section": "smartEditContainer",
      "id": "languageServiceModule",
      "shortName": "languageServiceModule",
      "type": "overview",
      "moduleName": "languageServiceModule",
      "shortDescription": "The languageServiceModule",
      "keywords": "fetches language languages languageservicemodule module overview service site smarteditcontainer supported"
    },
    {
      "section": "smartEditContainer",
      "id": "languageServiceModule.SELECTED_LANGUAGE",
      "shortName": "languageServiceModule.SELECTED_LANGUAGE",
      "type": "object",
      "moduleName": "languageServiceModule",
      "shortDescription": "A constant that is used as key to store the selected language in the storageService",
      "keywords": "constant key language languageservicemodule object selected selected_language smarteditcontainer storageservice store"
    },
    {
      "section": "smartEditContainer",
      "id": "languageServiceModule.service:languageService",
      "shortName": "languageService",
      "type": "service",
      "moduleName": "languageServiceModule",
      "shortDescription": "The Language Service fetches all languages for a specified site using REST service calls to the cmswebservices languages API.",
      "keywords": "active api array browser callback calls check cmswebservices code current currently descriptor descriptors determine determines en en_us english fetched fetches format fr french function gateway getbrowserlanguageisocode getbrowserlocale getlanguagesforsite getresolvelocale gettoolinglanguages i18n identifier iso isocode language languages languageservicemodule list locale method nativename object order preference properties register registerswitchlanguage required resolve rest retrieves saved selected service set setselectedlanguage site sites siteuid smarteditcontainer smarteditwebservices storage storefront supported switch system tooling true uid unique user"
    },
    {
      "section": "smartEditContainer",
      "id": "languageServiceModule.SWITCH_LANGUAGE_EVENT",
      "shortName": "languageServiceModule.SWITCH_LANGUAGE_EVENT",
      "type": "object",
      "moduleName": "languageServiceModule",
      "shortDescription": "A constant that is used as key to publish and receive events when a language is changed.",
      "keywords": "changed constant events key language languageservicemodule object publish receive smarteditcontainer switch_language_event"
    },
    {
      "section": "smartEditContainer",
      "id": "loadConfigModule",
      "shortName": "loadConfigModule",
      "type": "overview",
      "moduleName": "loadConfigModule",
      "shortDescription": "The loadConfigModule supplies configuration information to SmartEdit. Configuration is stored in key/value pairs.",
      "keywords": "array configuration exposes functionsmodule key load loadconfigmodule module ngresource object overview pairs resourcelocationsmodule service shareddataservicemodule smartedit smarteditcontainer stored supplies"
    },
    {
      "section": "smartEditContainer",
      "id": "loadConfigModule.service:LoadConfigManager",
      "shortName": "LoadConfigManager",
      "type": "service",
      "moduleName": "loadConfigModule",
      "shortDescription": "The LoadConfigManager is used to retrieve configurations stored in configuration API.",
      "keywords": "$log $resource _prettify api array conf configuration configurations converts converttoarray copy create defaulttoolinglanguage example function hitch key loadasarray loadasobject loadconfigmanager loadconfigmanagerservice loadconfigmodule mapped method object pairs promise resourcelocationsmodule retrieve retrieves returns service set shareddataservice smarteditcontainer stored values"
    },
    {
      "section": "smartEditContainer",
      "id": "loadConfigModule.service:loadConfigManagerService",
      "shortName": "loadConfigManagerService",
      "type": "service",
      "moduleName": "loadConfigModule",
      "shortDescription": "A service that is a singleton of loadConfigModule.service:LoadConfigManager  which is used to ",
      "keywords": "configuration entry loadconfigmanager loadconfigmodule module point retrieve service services singleton smartedit smarteditcontainer values"
    },
    {
      "section": "smartEditContainer",
      "id": "modalServiceModule",
      "shortName": "modalServiceModule",
      "type": "overview",
      "moduleName": "modalServiceModule",
      "shortDescription": "The modalServiceModule",
      "keywords": "achieving actions adding additionally affect behave button buttons chaning closing components create devoted easy goal manage modal modalmanager modalservice modalservicemodule module object open opened overview providing service smarteditcontainer style styles title window windows"
    },
    {
      "section": "smartEditContainer",
      "id": "modalServiceModule.modalService",
      "shortName": "modalServiceModule.modalService",
      "type": "service",
      "moduleName": "modalServiceModule",
      "shortDescription": "Convenience service to open and style a promise-based templated modal window.",
      "keywords": "$log $q $scope action actions acts addbutton additional angular angularjs app array button buttonhandlerfn buttons callbacks called caller calling cancel choose classed close closed common complex conf config configuration configurations content controller convenience css cssclasses custom data debug declared defer deferred dependency depending details dismiss display errorcallback example explicit explicitly factory feel fragment function functions html https injection inline key label list log logic method methods_addbutton methods_close modal modal_button_actions modal_button_styles modalcontroller modalmanager modalservice modalservicemodule module multiple object open org path piece promise promise-based reject rejected resolve resolved result return separated service setbuttonhandler share simple smarteditcontainer someresult someservice space style submit successcallback template templated templateinline templateurl title translated true validatesomething var ways window windows"
    },
    {
      "section": "smartEditContainer",
      "id": "modalServiceModule.object:MODAL_BUTTON_ACTIONS",
      "shortName": "MODAL_BUTTON_ACTIONS",
      "type": "object",
      "moduleName": "modalServiceModule",
      "shortDescription": "Injectable angular constant",
      "keywords": "action addbutton adding angular angularjs button close close_modal constant defines dismiss example executing existing getbuttons https indicates injectable label methods_getbuttons methods_open modal modal_button_actions modalmanager modalservice modalservicemodule mymodalmanager object open opening org performed promise property rejected resolved returned service smarteditcontainer window"
    },
    {
      "section": "smartEditContainer",
      "id": "modalServiceModule.object:MODAL_BUTTON_STYLES",
      "shortName": "MODAL_BUTTON_STYLES",
      "type": "object",
      "moduleName": "modalServiceModule",
      "shortDescription": "Injectable angular constant",
      "keywords": "addbutton adding angular button cancel cancel_button constant default defines equivalent example existing feel getbuttons indicates injectable label methods_getbuttons methods_open modal modal_button_styles modalmanager modalservice modalservicemodule mymodalmanager object open opening primary property save secondary service smarteditcontainer style styled submit window"
    },
    {
      "section": "smartEditContainer",
      "id": "modalServiceModule.service:ModalManager",
      "shortName": "ModalManager",
      "type": "service",
      "moduleName": "modalServiceModule",
      "shortDescription": "The ModalManager is a service designed to provide easy runtime modification to various aspects of a modal window,",
      "keywords": "$log $q $scope access action addbutton adding additionally allowing allows angularjs applied array aspects avoid button buttonhandlerfn buttonhandlerfunction buttonid buttonpressedcallback buttons callback callbacks called caller cancel cancelled case caution chain clone close closed closing code complexity conf configuration content continue controller corner create data debug default defer deferred designed details disablebutton disabled dismiss dismisscallback displayed don easy empty enable enablebutton enabled enables errorcallback example execute executed exposed fetched fired flag function getbutton getbuttons handler handlers happen header hello https i18n ignore implicitly instance isolated key kind label long manager matching method methods_addbutton methods_setbuttonhandler modal modal_button_actions modal_button_styles modalmanager modalservice modalservicemodule modaltestcontroller modification modifying multiple newly note null object open org parameter parameters pass passed passing press pressed prevent preventing process promise properties provide provided publicly read receives reference registered reject rejected rejecting remove removeallbuttons removebutton removed representing resolve resolved resolving return returned returns runtime sample scenarios scope service setbuttonhandler setdismisscallback setshowheaderdismiss setting showx single smarteditcontainer someresult string style submit successcallback suggested title top translated trigger true undefined unique unnecessary update validatesomething validating validation var window"
    },
    {
      "section": "smartEditContainer",
      "id": "pageToolMenuModule",
      "shortName": "pageToolMenuModule",
      "type": "overview",
      "moduleName": "pageToolMenuModule",
      "shortDescription": "The pageToolMenuModule",
      "keywords": "compiled configurable directive displays menu module overview pagetoolmenumodule smarteditcontainer tool"
    },
    {
      "section": "smartEditContainer",
      "id": "pageToolMenuModule",
      "shortName": "pageToolMenuModule",
      "type": "overview",
      "moduleName": "pageToolMenuModule",
      "shortDescription": "The sharedDataServiceModule",
      "keywords": "application container data key module overview pagetoolmenumodule retrieve service shared shareddataservicemodule smartedit smarteditcontainer store"
    },
    {
      "section": "smartEditContainer",
      "id": "pageToolMenuModule.directive:pageToolMenu",
      "shortName": "pageToolMenu",
      "type": "directive",
      "moduleName": "pageToolMenuModule",
      "shortDescription": "Once compiled, the page tool menu directive displays the configurable page tool menu. By default, the page tool menu",
      "keywords": "actions add additems angular app called compiled configurable configured content current customizable default directive displays example extended extendedpageinformation file folder gateway gettoolbarservice html hybrid_action i18nkey icons imageroot images include item menu module opens pagetoolmenu pagetoolmenumodule pagetoolmenuservice path png proxy root service smarteditcontainer static-resources status synchronization title tool toolbar toolbarmodule toolbarname toolbarservice toolbarservicefactory type var web"
    },
    {
      "section": "smartEditContainer",
      "id": "perspectiveInterfaceModule",
      "shortName": "perspectiveInterfaceModule",
      "type": "overview",
      "moduleName": "perspectiveInterfaceModule",
      "keywords": "overview perspectiveinterfacemodule smarteditcontainer"
    },
    {
      "section": "smartEditContainer",
      "id": "perspectiveInterfaceModule.service:PerspectiveServiceInterface",
      "shortName": "PerspectiveServiceInterface",
      "type": "service",
      "moduleName": "perspectiveInterfaceModule",
      "keywords": "activated activating active actives application bound configuration consists cookie current currently currently-selected deactivates deactivating default description descriptioni18nkey disabled disablingcallback enabled enablingcallback feature features flag functions hasactiveperspective i18n identified identifies indicates invoked isemptyperspectiveactive key list method methods_register mode namei18nkey optional overlay parameter perspective perspectiveinterfacemodule perspectives preview referenced register registered registers registry represents respective returns se selectdefault selected selects service smartedit smartedit-perspectives smarteditcontainer stored stores switches switchto system tooltip translated true uniquely user web"
    },
    {
      "section": "smartEditContainer",
      "id": "perspectiveInterfaceModule.service:renderGateway",
      "shortName": "renderGateway",
      "type": "service",
      "moduleName": "perspectiveInterfaceModule",
      "shortDescription": "Instance of the MessageGateway dealing with rendering related events",
      "keywords": "dealing events gatewayfactorymodule instance messagegateway perspectiveinterfacemodule rendering service smarteditcontainer"
    },
    {
      "section": "smartEditContainer",
      "id": "previewDataGenericEditorResponseHandlerModule.service:previewDataGenericEditorResponseHandler",
      "shortName": "previewDataGenericEditorResponseHandler",
      "type": "service",
      "moduleName": "previewDataGenericEditorResponseHandlerModule",
      "shortDescription": "previewDataGenericEditorResponseHandler is invoked by GenericEditor to handle POST response",
      "keywords": "allow api call editor genericeditor genericeditormodule handle handler invoked invoking model multiple post preview previewdatagenericeditorresponsehandler previewdatagenericeditorresponsehandlermodule response responsible retrieving server service set setting smarteditcomponentid smarteditcontainer ticketid updatecallback"
    },
    {
      "section": "smartEditContainer",
      "id": "previewTicketInterceptorModule.previewTicketInterceptor",
      "shortName": "previewTicketInterceptorModule.previewTicketInterceptor",
      "type": "service",
      "moduleName": "previewTicketInterceptorModule",
      "shortDescription": "A HTTP request interceptor that adds the preview ticket to the HTTP header object before a request is made.",
      "keywords": "$httpprovider adding adds array called config configuration current dependencies extracts factories factory header holds http injected interceptor interceptors method methods modified object preview previewticketinterceptor previewticketinterceptormodule property registered request resource returns service smarteditcontainer ticket url x-preview-ticket"
    },
    {
      "section": "smartEditContainer",
      "id": "renderServiceInterfaceModule",
      "shortName": "renderServiceInterfaceModule",
      "type": "overview",
      "moduleName": "renderServiceInterfaceModule",
      "shortDescription": "The renderServiceInterfaceModule",
      "keywords": "abstract accelerator component components data designed displays extensible interface module operation overview performed re-render render renderservice renderserviceinterfacemodule service smarteditcontainer update"
    },
    {
      "section": "smartEditContainer",
      "id": "renderServiceInterfaceModule.service:RenderServiceInterface",
      "shortName": "RenderServiceInterface",
      "type": "service",
      "moduleName": "renderServiceInterfaceModule",
      "shortDescription": "Designed to re-render components after an update component data operation has been performed, according to",
      "keywords": "$compile accelerator backend class compilation component componentid components componenttype content correctly current custom customcontent data decorators decoratorservicemodule designed displayed displays error event executed extended flag frontend html indicates instantiated interface method methods_storeprecompiledcomponent note object operation optional original overlay performed position positioned promise propagate re-render re-rendered re-renders receiving refreshoverlaydimensions reject rejected remove removed removes render rendercomponent rendered rendergateway renderpage renderremoval renderservice renderserviceinterfacemodule renderslots replace requires rerender resets resolve returns saved serves service showoverlay slot slotids slotsids smarteditcontainer stack storefront subscribes success time toggleoverlay toggles triggers type update updated updates values visibility wrapping"
    },
    {
      "section": "smartEditContainer",
      "id": "resourceLocationsModule.object:CATALOG_VERSION_DETAILS_RESOURCE_URI",
      "shortName": "CATALOG_VERSION_DETAILS_RESOURCE_URI",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "Resource URI of the catalog version details REST service.",
      "keywords": "catalog details object resource resourcelocationsmodule rest service smarteditcontainer uri version"
    },
    {
      "section": "smartEditContainer",
      "id": "resourceLocationsModule.object:CATALOGS_PATH",
      "shortName": "CATALOGS_PATH",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "Path of the catalogs",
      "keywords": "catalogs object path resourcelocationsmodule smarteditcontainer"
    },
    {
      "section": "smartEditContainer",
      "id": "resourceLocationsModule.object:CMSWEBSERVICES_PATH",
      "shortName": "CMSWEBSERVICES_PATH",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "Path of the cmswebservices",
      "keywords": "cmswebservices object path resourcelocationsmodule smarteditcontainer"
    },
    {
      "section": "smartEditContainer",
      "id": "resourceLocationsModule.object:CMSWEBSERVICES_RESOURCE_URI",
      "shortName": "CMSWEBSERVICES_RESOURCE_URI",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "Constant for the cmswebservices API root",
      "keywords": "api cmswebservices constant object resourcelocationsmodule root smarteditcontainer"
    },
    {
      "section": "smartEditContainer",
      "id": "resourceLocationsModule.object:CONFIGURATION_COLLECTION_URI",
      "shortName": "CONFIGURATION_COLLECTION_URI",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "The SmartEdit configuration collection API root",
      "keywords": "api collection configuration object resourcelocationsmodule root smartedit smarteditcontainer"
    },
    {
      "section": "smartEditContainer",
      "id": "resourceLocationsModule.object:CONFIGURATION_URI",
      "shortName": "CONFIGURATION_URI",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "the name of the SmartEdit configuration API root",
      "keywords": "api configuration object resourcelocationsmodule root smartedit smarteditcontainer"
    },
    {
      "section": "smartEditContainer",
      "id": "resourceLocationsModule.object:DEFAULT_AUTHENTICATION_CLIENT_ID",
      "shortName": "DEFAULT_AUTHENTICATION_CLIENT_ID",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "The default OAuth 2 client id to use during authentication.",
      "keywords": "authentication client default oauth object resourcelocationsmodule smarteditcontainer"
    },
    {
      "section": "smartEditContainer",
      "id": "resourceLocationsModule.object:DEFAULT_AUTHENTICATION_ENTRY_POINT",
      "shortName": "DEFAULT_AUTHENTICATION_ENTRY_POINT",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "When configuration is not available yet to provide authenticationMap, one needs a default authentication entry point to access configuration API itself",
      "keywords": "access api authentication authenticationmap configuration default entry object point provide resourcelocationsmodule smarteditcontainer"
    },
    {
      "section": "smartEditContainer",
      "id": "resourceLocationsModule.object:ENUM_RESOURCE_URI",
      "shortName": "ENUM_RESOURCE_URI",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "Path to fetch list of values of a given enum type",
      "keywords": "enum fetch list object path resourcelocationsmodule smarteditcontainer type values"
    },
    {
      "section": "smartEditContainer",
      "id": "resourceLocationsModule.object:I18N_LANGUAGE_RESOURCE_URI",
      "shortName": "I18N_LANGUAGE_RESOURCE_URI",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "Resource URI to fetch the supported i18n languages.",
      "keywords": "fetch i18n languages object resource resourcelocationsmodule smarteditcontainer supported uri"
    },
    {
      "section": "smartEditContainer",
      "id": "resourceLocationsModule.object:I18N_RESOURCE_URI",
      "shortName": "I18N_RESOURCE_URI",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "Resource URI to fetch the i18n initialization map for a given locale.",
      "keywords": "fetch i18n initialization locale map object resource resourcelocationsmodule smarteditcontainer uri"
    },
    {
      "section": "smartEditContainer",
      "id": "resourceLocationsModule.object:LANDING_PAGE_PATH",
      "shortName": "LANDING_PAGE_PATH",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "Path of the landing page",
      "keywords": "landing object path resourcelocationsmodule smarteditcontainer"
    },
    {
      "section": "smartEditContainer",
      "id": "resourceLocationsModule.object:LANGUAGE_RESOURCE_URI",
      "shortName": "LANGUAGE_RESOURCE_URI",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "Resource URI of the languages REST service.",
      "keywords": "languages object resource resourcelocationsmodule rest service smarteditcontainer uri"
    },
    {
      "section": "smartEditContainer",
      "id": "resourceLocationsModule.object:MEDIA_PATH",
      "shortName": "MEDIA_PATH",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "Path of the media",
      "keywords": "media object path resourcelocationsmodule smarteditcontainer"
    },
    {
      "section": "smartEditContainer",
      "id": "resourceLocationsModule.object:MEDIA_RESOURCE_URI",
      "shortName": "MEDIA_RESOURCE_URI",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "Resource URI of the media REST service.",
      "keywords": "media object resource resourcelocationsmodule rest service smarteditcontainer uri"
    },
    {
      "section": "smartEditContainer",
      "id": "resourceLocationsModule.object:PERMISSIONSWEBSERVICES_RESOURCE_URI",
      "shortName": "PERMISSIONSWEBSERVICES_RESOURCE_URI",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "Path to fetch permissions of a given type",
      "keywords": "fetch object path permissions resourcelocationsmodule smarteditcontainer type"
    },
    {
      "section": "smartEditContainer",
      "id": "resourceLocationsModule.object:PREVIEW_RESOURCE_URI",
      "shortName": "PREVIEW_RESOURCE_URI",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "Path of the preview ticket API",
      "keywords": "api object path preview resourcelocationsmodule smarteditcontainer ticket"
    },
    {
      "section": "smartEditContainer",
      "id": "resourceLocationsModule.object:SITES_RESOURCE_URI",
      "shortName": "SITES_RESOURCE_URI",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "Resource URI of the sites REST service.",
      "keywords": "object resource resourcelocationsmodule rest service sites smarteditcontainer uri"
    },
    {
      "section": "smartEditContainer",
      "id": "resourceLocationsModule.object:SMARTEDIT_RESOURCE_URI_REGEXP",
      "shortName": "SMARTEDIT_RESOURCE_URI_REGEXP",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "to calculate platform domain URI, this regular expression will be used",
      "keywords": "calculate domain expression object platform regular resourcelocationsmodule smarteditcontainer uri"
    },
    {
      "section": "smartEditContainer",
      "id": "resourceLocationsModule.object:SMARTEDIT_ROOT",
      "shortName": "SMARTEDIT_ROOT",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "the name of the webapp root context",
      "keywords": "context object resourcelocationsmodule root smarteditcontainer webapp"
    },
    {
      "section": "smartEditContainer",
      "id": "resourceLocationsModule.object:STORE_FRONT_CONTEXT",
      "shortName": "STORE_FRONT_CONTEXT",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "to fetch the store front context for inflection points.",
      "keywords": "context fetch front inflection object points resourcelocationsmodule smarteditcontainer store"
    },
    {
      "section": "smartEditContainer",
      "id": "resourceLocationsModule.object:STOREFRONT_PATH",
      "shortName": "STOREFRONT_PATH",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "Path of the storefront",
      "keywords": "object path resourcelocationsmodule smarteditcontainer storefront"
    },
    {
      "section": "smartEditContainer",
      "id": "resourceLocationsModule.object:STOREFRONT_PATH_WITH_PAGE_ID",
      "shortName": "STOREFRONT_PATH_WITH_PAGE_ID",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "Path of the storefront with a page ID",
      "keywords": "object path resourcelocationsmodule smarteditcontainer storefront"
    },
    {
      "section": "smartEditContainer",
      "id": "resourceLocationsModule.object:SYNC_PATH",
      "shortName": "SYNC_PATH",
      "type": "object",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "Path of the synchronization service",
      "keywords": "object path resourcelocationsmodule service smarteditcontainer synchronization"
    },
    {
      "section": "smartEditContainer",
      "id": "resourceLocationsModule.resourceLocationToRegex",
      "shortName": "resourceLocationsModule.resourceLocationToRegex",
      "type": "service",
      "moduleName": "resourceLocationsModule",
      "shortDescription": "Generates a regular expresssion matcher from a given resource location URL, replacing predefined keys by wildcard",
      "keywords": "$httpbackend backend endpoint endpointregex example expresssion generates hits http location match matcher matchers mocked passed predefined regex regular replacing resource resourcelocationsmodule resourcelocationtoregex respond service smarteditcontainer someresource somevalue url var whenget wildcard"
    },
    {
      "section": "smartEditContainer",
      "id": "restServiceFactoryModule",
      "shortName": "restServiceFactoryModule",
      "type": "overview",
      "moduleName": "restServiceFactoryModule",
      "shortDescription": "The restServiceFactoryModule",
      "keywords": "factory generate module overview providing resource rest restservicefactorymodule service smarteditcontainer url wrapper"
    },
    {
      "section": "smartEditContainer",
      "id": "restServiceFactoryModule.service:ResourceWrapper",
      "shortName": "ResourceWrapper",
      "type": "service",
      "moduleName": "restServiceFactoryModule",
      "shortDescription": "The ResourceWrapper is service used to make REST calls to the wrapped target URI. It is created",
      "keywords": "angularjs appends based call called calls component components created deletes evaluate evaluates factory getbyid https identifier list loads mapped match matching method methods_get object objects org parameters params payload placeholder placeholders promise provided query rejected rejecting rejects remove resolves resolving resource resourcewrapper rest restservicefactory restservicefactorymodule returned returns save saved saves search searchparams server service single smarteditcontainer string target unique update updated updates uri verify warpper wrapped wrapper"
    },
    {
      "section": "smartEditContainer",
      "id": "restServiceFactoryModule.service:restServiceFactory",
      "shortName": "restServiceFactory",
      "type": "service",
      "moduleName": "restServiceFactoryModule",
      "shortDescription": "A factory used to generate a REST service wrapper for a given resource URL, providing a means to perform HTTP",
      "keywords": "$resource angularjs appended argument automatically best context-wide create created default delegated domain factory forward generate http https identifier location method multiple object operations opposed optional org parameter paths payload perform placeholder points post prefix prefixed prepended provided providing reference relative remain resource resourceid rest restservicefactory restservicefactorymodule retrieved returned separate service services set setdomain slash smarteditcontainer split target uri uris url working wrapped wrapper wraps"
    },
    {
      "section": "smartEditContainer",
      "id": "seBackendValidationHandlerModule",
      "shortName": "seBackendValidationHandlerModule",
      "type": "overview",
      "moduleName": "seBackendValidationHandlerModule",
      "shortDescription": "This module provides the seBackendValidationHandler service, which handles standard OCC validation errors received",
      "keywords": "backend errors handles module occ overview received sebackendvalidationhandler sebackendvalidationhandlermodule service smarteditcontainer standard validation"
    },
    {
      "section": "smartEditContainer",
      "id": "seBackendValidationHandlerModule.seBackendValidationHandler",
      "shortName": "seBackendValidationHandlerModule.seBackendValidationHandler",
      "type": "service",
      "moduleName": "seBackendValidationHandlerModule",
      "shortDescription": "The seBackendValidationHandler service handles validation errors received from the backend.",
      "keywords": "appended appends array backend consisting context contextual contract data details error errors errorscontext example exception expected extracts format handleresponse handles list matches message method mysubject occurred originally output parameter provided received response sebackendvalidationhandler sebackendvalidationhandlermodule service smarteditcontainer someothererror subject type validation validationerror var"
    },
    {
      "section": "smartEditContainer",
      "id": "seFileValidationServiceModule",
      "shortName": "seFileValidationServiceModule",
      "type": "overview",
      "moduleName": "seFileValidationServiceModule",
      "shortDescription": "This module provides the seFileValidationService service, which validates if a specified file meets the required file",
      "keywords": "commerce constraints file hybris meets module overview required sap sefilevalidationservice sefilevalidationservicemodule service size smarteditcontainer type validates"
    },
    {
      "section": "smartEditContainer",
      "id": "seFileValidationServiceModule.seFileObjectValidators",
      "shortName": "seFileValidationServiceModule.seFileObjectValidators",
      "type": "object",
      "moduleName": "seFileValidationServiceModule",
      "shortDescription": "A list of file validators, that includes a validator for file-size constraints and a validator for file-type",
      "keywords": "constraints file file-size file-type includes list object sefileobjectvalidators sefilevalidationservicemodule smarteditcontainer validator validators"
    },
    {
      "section": "smartEditContainer",
      "id": "seFileValidationServiceModule.seFileValidationService",
      "shortName": "seFileValidationServiceModule.seFileValidationService",
      "type": "service",
      "moduleName": "seFileValidationServiceModule",
      "shortDescription": "The seFileValidationService validates that the file provided is of a specified file type and that the file does not",
      "keywords": "api append appends array buildacceptedfiletypeslist comma- comma-separated context contextual creates error errors exceed extensions file header limit list maxium method object output parameter promise provided rejected resolves sefilemimetypeservice sefilemimetypeservicemodule sefileobjectvalidators sefilevalidationservice sefilevalidationserviceconstants sefilevalidationservicemodule separated service size smarteditcontainer supported transforms type types valid validate validated validates validator web"
    },
    {
      "section": "smartEditContainer",
      "id": "seFileValidationServiceModule.seFileValidationServiceConstants",
      "shortName": "seFileValidationServiceModule.seFileValidationServiceConstants",
      "type": "object",
      "moduleName": "seFileValidationServiceModule",
      "shortDescription": "The constants provided by the file validation service.",
      "keywords": "bytes constants file internationalization list map maximum object platform provided sefilevalidationserviceconstants sefilevalidationservicemodule service size smarteditcontainer supported types uploaded validation"
    },
    {
      "section": "smartEditContainer",
      "id": "seMediaServiceModule",
      "shortName": "seMediaServiceModule",
      "type": "overview",
      "moduleName": "seMediaServiceModule",
      "shortDescription": "The media service module provides a service to create an image file for a catalog through AJAX calls. This module",
      "keywords": "$resource ajax calls catalog create data dedicated file form image media module multipart overview posts request semediaservicemodule service smarteditcontainer transformed"
    },
    {
      "section": "smartEditContainer",
      "id": "seMediaServiceModule.seMediaResource",
      "shortName": "seMediaServiceModule.seMediaResource",
      "type": "service",
      "moduleName": "seMediaServiceModule",
      "shortDescription": "A $resource that makes REST calls to the default",
      "keywords": "$resource angularjs api calls catalog cms collection content-type default file formdata http https media method methods mozilla multipart object org pojo post request required rest retrieve semediaresource semediaservicemodule service smarteditcontainer supports transform transformation uploads"
    },
    {
      "section": "smartEditContainer",
      "id": "seMediaServiceModule.seMediaResourceService",
      "shortName": "seMediaServiceModule.seMediaResourceService",
      "type": "service",
      "moduleName": "seMediaServiceModule",
      "shortDescription": "This service provides an interface to the $resource that makes REST ",
      "keywords": "$resource angularjs api calls catalog cms default http https interface media method org rest retrieve returning semediaresourceservice semediaservicemodule service single smarteditcontainer supports"
    },
    {
      "section": "smartEditContainer",
      "id": "seMediaServiceModule.seMediaService",
      "shortName": "seMediaServiceModule.seMediaService",
      "type": "service",
      "moduleName": "seMediaServiceModule",
      "shortDescription": "This service provides an interface to the $resource provided by the seMediaResource service and ",
      "keywords": "$resource alternate alttext angularjs backend catalog catalog-catalog code combination corresponding corresponds description errors exists fails fetch fetches file functionality getmediabycode https identifier images interface media method mozilla object org pojo promise provided request resolves returns selected semediaresource semediaresourceservice semediaservice semediaservicemodule service smarteditcontainer specific successful text unique upload uploaded uploadmedia uploads version"
    },
    {
      "section": "smartEditContainer",
      "id": "seObjectValidatorFactoryModule",
      "shortName": "seObjectValidatorFactoryModule",
      "type": "overview",
      "moduleName": "seObjectValidatorFactoryModule",
      "shortDescription": "This module provides the seObjectValidatorFactory service, which is used to build a validator for a specified list of",
      "keywords": "build list module objects overview seobjectvalidatorfactory seobjectvalidatorfactorymodule service smarteditcontainer validator"
    },
    {
      "section": "smartEditContainer",
      "id": "seObjectValidatorFactoryModule.seObjectValidatorFactory",
      "shortName": "seObjectValidatorFactoryModule.seObjectValidatorFactory",
      "type": "service",
      "moduleName": "seObjectValidatorFactoryModule",
      "shortDescription": "This service provides a factory method to build a validator for a specified list of validator objects.",
      "keywords": "append associate beause block build builds case code consist consists contextual described error errors errorscontext example factory fail failed false function invalid isvalid list message method object objects objectundervalidation parameter parameters predicate result return seobjectvalidatorfactory seobjectvalidatorfactorymodule service single smarteditcontainer subject takes validate validating validator validators var"
    },
    {
      "section": "smartEditContainer",
      "id": "seValidationErrorParserModule",
      "shortName": "seValidationErrorParserModule",
      "type": "overview",
      "moduleName": "seValidationErrorParserModule",
      "shortDescription": "This module provides the validationErrorsParser service, which is used to parse validation errors for parameters",
      "keywords": "errors format language message module overview parameters parse service sevalidationerrorparsermodule smarteditcontainer validation validationerrorsparser"
    },
    {
      "section": "smartEditContainer",
      "id": "seValidationErrorParserModule.seValidationErrorParser",
      "shortName": "seValidationErrorParserModule.seValidationErrorParser",
      "type": "service",
      "moduleName": "seValidationErrorParserModule",
      "shortDescription": "This service provides the functionality to parse validation errors received from the backend.",
      "keywords": "backend details en error errors expects extra final format function functionality language message method object occurred parse parses received service sevalidationerrorparser sevalidationerrorparsermodule smarteditcontainer somekey someval stripped validation var widescreen"
    },
    {
      "section": "smartEditContainer",
      "id": "sharedDataServiceInterfaceModule.SharedDataServiceInterface",
      "shortName": "sharedDataServiceInterfaceModule.SharedDataServiceInterface",
      "type": "service",
      "moduleName": "sharedDataServiceInterfaceModule",
      "shortDescription": "Provides an abstract extensible shared data service. Used to store any data to be used either the SmartEdit",
      "keywords": "abstract application class container data extended extensible fetch instantiated interface key method retrieve serves service set shared shareddataserviceinterface shareddataserviceinterfacemodule smartedit smarteditcontainer store"
    },
    {
      "section": "smartEditContainer",
      "id": "sharedDataServiceModule.sharedDataService",
      "shortName": "sharedDataServiceModule.sharedDataService",
      "type": "service",
      "moduleName": "sharedDataServiceModule",
      "shortDescription": "The Shared Data Service is used to store data that is to be shared between the SmartEdit application and the",
      "keywords": "application container data extends gateway gatewayproxy gatewayproxymodule service share shared shareddata shareddataservice shareddataserviceinterface shareddataserviceinterfacemodule shareddataservicemodule smartedit smarteditcontainer store"
    },
    {
      "section": "smartEditContainer",
      "id": "siteServiceModule",
      "shortName": "siteServiceModule",
      "type": "overview",
      "moduleName": "siteServiceModule",
      "shortDescription": "The siteServiceModule",
      "keywords": "configured fetches hybris module overview platform service site sites siteservicemodule smarteditcontainer"
    },
    {
      "section": "smartEditContainer",
      "id": "siteServiceModule.service:siteService",
      "shortName": "siteService",
      "type": "service",
      "moduleName": "siteServiceModule",
      "shortDescription": "The Site Service fetches all sites configured on the hybris platform using REST calls to the cmswebservices sites API.",
      "keywords": "api array calls cmswebservices configured descriptor descriptors descriptos fetched fetches getsitebyid getsites hybris list method platform previewurl properties redirecturl rest service site sites siteservicemodule smarteditcontainer uid"
    },
    {
      "section": "smartEditContainer",
      "id": "smarteditcontainer.controller:landingPageController",
      "shortName": "landingPageController",
      "type": "controller",
      "moduleName": "smarteditcontainer",
      "shortDescription": "When called, it will retrieve the list of catalogs to display on the landing page.",
      "keywords": "called catalog catalogs catalogservice configuration controller display factory landing list load loadconfigmanagerservice manager rest restservicefactory retrieve service smarteditcontainer"
    },
    {
      "section": "smartEditContainer",
      "id": "storageServiceModule",
      "shortName": "storageServiceModule",
      "type": "overview",
      "moduleName": "storageServiceModule",
      "shortDescription": "The storageServiceModule",
      "keywords": "allows browser module overview service smarteditcontainer storage storageservicemodule storing temporary"
    },
    {
      "section": "smartEditContainer",
      "id": "storageServiceModule.service:storageService",
      "shortName": "storageService",
      "type": "service",
      "moduleName": "storageServiceModule",
      "shortDescription": "The Storage service is used to store temporary information in the browser. The service keeps track of key/value pairs",
      "keywords": "access associated associates authenticate authenticated authentication authtoken authtokens authuri browser content cookie cookiename creates current desired determine doesn encode encoded entry exist flag getauthtoken getauthtokens getprincipalidentifier getvaluefromcookie identified identifies indicates initialized isencoded isinitialized key login map method null obfuscate pairs point principal principalnamekey principalnamevalue principaluid properly provided putvalueincookie remove removeallauthtokens removeauthtoken removed removeprincipalidentifier removes resource retrieve retrieved retrieves service smartedit-sessions smarteditcontainer specifies storage storageservicemodule store storeauthtoken stored storeprincipalidentifier stores temporary token track uid uri uris user"
    },
    {
      "section": "smartEditContainer",
      "id": "tabsetModule",
      "shortName": "tabsetModule",
      "type": "overview",
      "moduleName": "tabsetModule",
      "shortDescription": "The Tabset module provides the directives required to display a group of tabsets within a tabset. The",
      "keywords": "developers directive directives display displaying group interest module organizing overview required responsible smartedit smarteditcontainer tabs tabset tabsetmodule tabsets ytabset"
    },
    {
      "section": "smartEditContainer",
      "id": "tabsetModule.directive:yTab",
      "shortName": "yTab",
      "type": "directive",
      "moduleName": "tabsetModule",
      "shortDescription": "The directive  responsible for wrapping the content of a tab within a",
      "keywords": "allows called caller content contents custom data determine directive displayed executed extra fragment function functionality html match model modify object optional parameter parse path register responsible scope smartedit-tab smartedit-tabset smarteditcontainer tab tabcontrol tabid tabs tabset tabsetmodule track wrapping ytabset"
    },
    {
      "section": "smartEditContainer",
      "id": "tabsetModule.directive:yTabset",
      "shortName": "yTabset",
      "type": "directive",
      "moduleName": "tabsetModule",
      "shortDescription": "The directive responsible for displaying and organizing tabs within a tabset. A specified number of tabs will",
      "keywords": "allows body called caller changes child clicks configuration content contents custom data defined determine directive display displayed displaying drop-down error executed extra flag fragment function functionality grouped haserrors header headers html indicates item list maximum menu model modify note number numtabsdisplayed object optional organizing parameter parse passed path register remaining responsible scope selected smartedit-tab smartedit-tabset smarteditcontainer tab tabcontrol tabs tabset tabsetmodule tabslist templateurl title track user visual wrapped ytab"
    },
    {
      "section": "smartEditContainer",
      "id": "toolbarInterfaceModule.ToolbarServiceInterface",
      "shortName": "toolbarInterfaceModule.ToolbarServiceInterface",
      "type": "service",
      "moduleName": "toolbarInterfaceModule",
      "shortDescription": "Provides an abstract extensible toolbar service. Used to manage and perform actions to either the SmartEdit",
      "keywords": "abstract action actions additems aliases application array callback class clicked container default description determines display extended extensible higher html hybrid_action i18nkey icon icons identified identifier image images include instantiated interface internal internally item itemkey key list lower manage maps method middle number perform position priority provided ranging remove removeitembykey removes sections serves service smartedit smarteditcontainer takes template toolbar toolbarinterfacemodule toolbarserviceinterface translation trigger triggered type unique url urls"
    },
    {
      "section": "smartEditContainer",
      "id": "toolbarModule.directive:toolbar",
      "shortName": "toolbar",
      "type": "directive",
      "moduleName": "toolbarModule",
      "shortDescription": "Toolbar HTML mark-up that compiles into a configurable toolbar with an assigned ToolbarService for functionality.",
      "keywords": "assigned classes compiles configurable css cssclass directive folder functionality gateway html imageroot images item mark-up path proxy root service smarteditcontainer space-separated string styling toolbar toolbarmodule toolbarname toolbarservice"
    },
    {
      "section": "smartEditContainer",
      "id": "toolbarModule.ToolbarService",
      "shortName": "toolbarModule.ToolbarService",
      "type": "service",
      "moduleName": "toolbarModule",
      "shortDescription": "The SmartEdit container toolbar service is used to add toolbar items that can perform actions to either",
      "keywords": "action actions add additems additemsstyling adds alias aliases application callback classes clients communication container cross css directive displayed forwarded function gateway gatewayid gatewayproxy gatewayproxymodule iframe items key-name list mapping maps meant method methods methods_additems models pass perform proxy responsible service smartedit smarteditcontainer space-separated specific toolbar toolbarinterfacemodule toolbarmodule toolbarservice toolbarserviceinterface user view"
    },
    {
      "section": "smartEditContainer",
      "id": "toolbarModule.toolbarServiceFactory",
      "shortName": "toolbarModule.toolbarServiceFactory",
      "type": "service",
      "moduleName": "toolbarModule",
      "shortDescription": "The toolbar service factory generates instances of the ToolbarService based on",
      "keywords": "based cached communication corresponding created cross exist exists factory gateway gatewayid gatewayproxy gatewayproxymodule generates gettoolbarservice identifier iframe instance instances method provided respect returns service single singleton smarteditcontainer toolbar toolbarmodule toolbarservice toolbarservicefactory"
    },
    {
      "section": "smartEditContainer",
      "id": "translationServiceModule",
      "shortName": "translationServiceModule",
      "type": "service",
      "moduleName": "translationServiceModule",
      "shortDescription": "This module is used to configure the translate service, the filter, and the directives from the &#39;pascalprecht.translate&#39; package. The configuration consists of:",
      "keywords": "appropriate browser combined configuration configure consists constant directives filter getbrowserlocale i18napiroot i18ninterceptor i18ninterceptormodule initializing languageservice languageservicemodule locale map methods_getbrowserlocale methods_request module object package pascalprecht preferredlanguage replace request retrieved runtime service setting smarteditcontainer time translate translation translationservicemodule unaccessible undefined_locale uri"
    },
    {
      "section": "smartEditContainer",
      "id": "urlServiceInterfaceModule.UrlServiceInterface",
      "shortName": "urlServiceInterfaceModule.UrlServiceInterface",
      "type": "service",
      "moduleName": "urlServiceInterfaceModule",
      "shortDescription": "Provides an abstract extensible url service, Used to open a given URL",
      "keywords": "abstract authentication browser class extended extensible instantiated interface invocation method navigate navigates open opens openurlinpopup path pop serves service smarteditcontainer tab url urlserviceinterface urlserviceinterfacemodule"
    },
    {
      "section": "smartEditContainer",
      "id": "urlServiceModule",
      "shortName": "urlServiceModule",
      "type": "overview",
      "moduleName": "urlServiceModule",
      "shortDescription": "The urlServiceModule",
      "keywords": "browser container functionality module open overview providing service smartedit smarteditcontainer url urlservicemodule"
    },
    {
      "section": "smartEditContainer",
      "id": "urlServiceModule.urlService",
      "shortName": "urlServiceModule.urlService",
      "type": "service",
      "moduleName": "urlServiceModule",
      "shortDescription": "The Url Service is used to open a given URL in a pop up or new browser url, directly from SmartEdit. ",
      "keywords": "application browser call container data directly extends gateway gatewayproxy gatewayproxymodule making open pop service share smartedit smarteditcontainer url urlservice urlserviceinterface urlserviceinterfacemodule urlservicemodule"
    },
    {
      "section": "smartEditContainer",
      "id": "yInfiniteScrollingModule",
      "shortName": "yInfiniteScrollingModule",
      "type": "overview",
      "moduleName": "yInfiniteScrollingModule",
      "shortDescription": "This module holds the base web component to perform infinite scrolling from paged backend",
      "keywords": "backend base component holds infinite module overview paged perform scrolling smarteditcontainer web yinfinitescrollingmodule"
    },
    {
      "section": "smartEditContainer",
      "id": "yInfiniteScrollingModule.directive:yInfiniteScrolling",
      "shortName": "yInfiniteScrolling",
      "type": "directive",
      "moduleName": "yInfiniteScrollingModule",
      "shortDescription": "A directive that you can use to implement infinite scrolling for an expanding content (typically with a ng-repeat) nested in it.",
      "keywords": "approaches arguments attached backend bottom call change class close container content context crosses css currentpage data decide default defaults determined directive distance dropdown dropdownclass dropdowncontainerclass element evaluated example expanding expected expression failure fetch fetched fetching fetchpage fill filters free function handle height implement implementers infinite invoked items large left listens mask max-height maximum meant measured multiples mycontext nested nextpage ng-repeat number object optional overflow overflow-y override pagesize paginated pagination pixels push query re-fetch reach representing requested requests reset resolved restrict return scroll scrolling search server set size smarteditcontainer space starts string tall times triggered type typically yinfinitescrollingmodule"
    },
    {
      "section": "smartEditContainer",
      "id": "yInfiniteScrollingModule.object:Page",
      "shortName": "Page",
      "type": "object",
      "moduleName": "yInfiniteScrollingModule",
      "shortDescription": "An object representing the backend response to a paged query",
      "keywords": "array backend elements exceed object paged pagination pertaining property query representing requested response returned size smarteditcontainer yinfinitescrollingmodule"
    },
    {
      "section": "smartEditContainer",
      "id": "yInfiniteScrollingModule.object:Pagination",
      "shortName": "Pagination",
      "type": "object",
      "moduleName": "yInfiniteScrollingModule",
      "shortDescription": "An object representing the returned pagination information from backend",
      "keywords": "backend elements mask matching object pagination property representing returned smarteditcontainer total totalcount yinfinitescrollingmodule"
    },
    {
      "section": "smartEditContainer",
      "id": "yInfiniteScrollingModule.object:THROTTLE_MILLISECONDS",
      "shortName": "THROTTLE_MILLISECONDS",
      "type": "object",
      "moduleName": "yInfiniteScrollingModule",
      "shortDescription": "Configures the yInfiniteScrolling directive to throttle the page fetching with the value provided in milliseconds.",
      "keywords": "configures directive fetching milliseconds object provided smarteditcontainer throttle yinfinitescrolling yinfinitescrollingmodule"
    }
  ],
  "apis": {
    "smartEdit": true,
    "smartEditContainer": true
  },
  "html5Mode": false,
  "editExample": true,
  "startPage": "/#/smartEdit",
  "scripts": [
    "angular.min.js"
  ]
};