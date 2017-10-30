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

        basePath: '../web/webroot/',


        // frameworks to use
        // available frameworks: https://npmjs.org/browse/keyword/karma-adapter
        frameworks: ['jasmine'],

        decorators: [
            'karma-phantomjs-launcher',
            'karma-jasmine',
            'karma-coverage',
            'karma-ng-html2js-preprocessor'
        ],

        preprocessors: {
            '../../web/smarteditcontainer/**/*.js': ['coverage']
        },

        coverageReporter: {
            // specify a common output directory
            dir: '../../jsTarget/test/smarteditContainer/coverage/',
            reporters: [{
                type: 'html',
                subdir: 'report-html'
            }, {
                type: 'cobertura',
                subdir: '.',
                file: 'cobertura.xml'
            }]
        },

        junitReporter: {
            outputDir: '../../jsTarget/test/smarteditContainer/junit/', // results will be saved as $outputDir/$browserName.xml
            outputFile: 'testReport.xml' // if included, results will be saved as $outputDir/$browserName/$outputFile
                //suite: '' // suite will become the package name attribute in xml testsuite element
        },

        // list of files / patterns to load in the browser
        files: [
            'static-resources/thirdparties/jquery/dist/jquery.min.js', //load jquery so that angular will leverage it and not serve with jqLite that has poor API
            'static-resources/thirdparties/jquery-ui-smartedit/jquery-ui.js',
            'static-resources/thirdparties/jquery-ui-smartedit/jquery-ui-smartedit-extension.js',
            'static-resources/thirdparties/moment/moment.js',
            'static-resources/thirdparties/angular/angular.js',
            'static-resources/thirdparties/angular-route/angular-route.js',
            'static-resources/thirdparties/angular-resource/angular-resource.js',
            'static-resources/thirdparties/angular-cookies/angular-cookies.js',
            'static-resources/thirdparties/angular-animate/angular-animate.js',
            'static-resources/thirdparties/angular-mocks/angular-mocks.js',
            'static-resources/thirdparties/angular-sanitize/angular-sanitize.js',
            'static-resources/thirdparties/angular-bootstrap/ui-bootstrap-tpls.min.js', //needed since contains $modal
            'static-resources/thirdparties/angular-translate/angular-translate.min.js',
            'static-resources/thirdparties/angular-translate-loader-static-files/angular-translate-loader-static-files.min.js',
            'static-resources/thirdparties/ckeditor/ckeditor.js',
            'static-resources/thirdparties/ui-select/dist/select.js',
            'static-resources/thirdparties/scriptjs/script.js',
            'static-resources/thirdparties/polyfills/**/*.js',
            //,
            '../../smarteditcontainerJSTests/utils/**/*.js',
            '../../web/**/*.html',
            '../../jsTarget/templates.js',
            '../../web/common/**/*.js',
            '../../web/smarteditcontainer/**/*.js',
            //'../../smarteditcontainerJSTests/unit/**/wizard*Test.js',
            '../../smarteditcontainerJSTests/unit/**/*Test.js',
            //
            {
                pattern: 'static-resources/images/**/*',
                watched: false,
                included: false,
                served: true
            }

        ],

        // list of files to exclude
        exclude: [],


        // test results reporter to use
        // possible values: 'dots', 'progress'
        // available reporters: https://npmjs.org/browse/keyword/karma-reporter
        // coverage reporter generates the coverage
        reporters: ['progress', 'junit'], // 'coverage' interferes with gatewayProxy and proxies empty methods when it should not

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
        browsers: ['PhantomJS'],


        // Continuous Integration mode
        // if true, Karma captures browsers, runs the tests and exits
        singleRun: false,

        proxies: {
            '/static-resources/images/': '/base/static-resources/images/'
        }
    });
};
