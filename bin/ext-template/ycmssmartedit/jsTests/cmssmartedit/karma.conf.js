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
// Karma configuration
// Generated on Sat Jul 05 2014 07:57:17 GMT-0400 (EDT)

module.exports = function(config) {
    config.set({

        // base path that will be used to resolve all patterns (eg. files, exclude)
        //basePath: '../../../main/webapp/',
        basePath: '../../',


        // frameworks to use
        // available frameworks: https://npmjs.org/browse/keyword/karma-adapter
        frameworks: ['jasmine'],

        decorators: [
            'karma-phantomjs-launcher',
            //'karma-chrome-launcher',
            //'karma-firefox-launcher',
            'karma-jasmine',
            'karma-coverage',
            'karma-ng-html2js-preprocessor'
        ],

        preprocessors: {
            //'web/**/*.html': 'ng-html2js',
            //'web/decorators/**/*.js': ['coverage']
        },

        coverageReporter: {
            // specify a common output directory
            dir: 'jsTests/coverage/',
            reporters: [{
                type: 'html',
                subdir: 'report-html'
            }, {
                type: 'cobertura',
                subdir: '.',
                file: 'cobertura.xml'
            }, ]
        },

        junitReporter: {
            outputDir: 'jsTarget/test/cmssmartedit/junit/', // results will be saved as $outputDir/$browserName.xml
            outputFile: 'testReport.xml' // if included, results will be saved as $outputDir/$browserName/$outputFile
                //suite: '' // suite will become the package name attribute in xml testsuite element
        },

        ngHtml2JsPreprocessor: {
            /* since this bower components will be served by another webapp,
             * the directives must call a path that is consistent with it being a bower_component.
             * even when tested in standalone.
             * To cater for this one must preprocess the html templates to reflect their final path in the webapp build
             * UNLESS the web root is paramterized per app setup
             */
            //stripPrefix: 'templates/',
            //prependPrefix: '/bower_components/angularClientUtils/templates/'

            // or define a custom transform function
            //      cacheIdFromPath: function(filepath) {
            //        return cacheId;
            //      },

            // setting this option will create only a single module that contains templates
            // from all the files, so you can load them all with module('foo')
            //moduleName: 'directiveTemplateFilesModule'
        },

        // list of files / patterns to load in the browser
        files: [
            'bower_components/jquery/dist/jquery.js', //load jquery so that angular will leverage it and not serve with jqLite that has poor API
            'bower_components/angular/angular.js',
            'bower_components/angular-resource/angular-resource.js',
            'bower_components/angular-animate/angular-animate.js',
            'bower_components/angular-mocks/angular-mocks.js',
            'bower_components/angular-bootstrap/ui-bootstrap-tpls.min.js', //needed since contains $modal
            'bower_components/angular-translate/angular-translate.min.js',
            'bower_components/angular-translate-loader-static-files/angular-translate-loader-static-files.min.js',
            'bower_components/ui-select/dist/select.js',

            'tempLibraries/smartEdit/**/*.js',
            'jsTests/utils/**/*.js',
            'web/**/*.html',
            'web/features/commons/**/*.js',
            'jsTarget/web/features/cmssmartedit/templates.js',
            'web/features/cmssmartedit/**/*.js',
            'jsTests/cmssmartedit/unit/features/**/*.js'
        ],

        // list of files to exclude
        exclude: [],


        // test results reporter to use
        // possible values: 'dots', 'progress'
        // available reporters: https://npmjs.org/browse/keyword/karma-reporter
        // coverage reporter generates the coverage
        reporters: ['progress', 'junit'], // 'coverage'


        // web server port
        port: 9876,


        // enable / disable colors in the output (reporters and logs)
        colors: true,


        // level of logging
        // possible values: config.LOG_DISABLE || config.LOG_ERROR || config.LOG_WARN || config.LOG_INFO || config.LOG_DEBUG
        logLevel: config.LOG_INFO,


        // enable / disable watching file and executing tests whenever any file changes
        autoWatch: true,


        // start these browsers
        // available browser launchers: https://npmjs.org/browse/keyword/karma-launcher
        browsers: ['PhantomJS'], //Chrome


        // Continuous Integration mode
        // if true, Karma captures browsers, runs the tests and exits
        singleRun: false
    });
};
