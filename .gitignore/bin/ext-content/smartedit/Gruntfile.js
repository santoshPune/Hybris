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
module.exports = function(grunt) {

    grunt.loadNpmTasks('grunt-protractor-runner');
    grunt.loadNpmTasks('grunt-jssemicoloned');
    grunt.loadNpmTasks('grunt-ng-annotate');
    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-contrib-connect');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks("grunt-jsbeautifier");
    grunt.loadNpmTasks('grunt-angular-templates');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-less');
    grunt.loadNpmTasks('grunt-ngdocs');
    grunt.loadNpmTasks('grunt-replace');

    //grunt.loadNpmTasks('grunt-bower-task');
    grunt.loadNpmTasks('grunt-karma');

    var phantomJSPattern = 'node_modules/karma-phantomjs-launcher/node_modules/phantomjs/lib/phantom/bin/phantomjs*';
    var chromeDriverPattern = 'node_modules/protractor/selenium/chromedriver/chromedriver*';

    var jQueryUIConfig = require('./web/utils/jquery-ui-smartedit/config');

    var JQUERY_UI_PATTERNS = jQueryUIConfig.JQUERY_UI_PATTERNS;
    var JQUERY_UI_DIFFS_PATH = jQueryUIConfig.JQUERY_UI_DIFFS_PATH;
    var JQUERY_UI_DIFFS = jQueryUIConfig.JQUERY_UI_DIFFS;

    var hyTechnePattern = 'node_modules/hyTechne/';

    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),

        connect: {
            dev: {
                options: {
                    hostname: '127.0.0.1',
                    port: 8080,
                    keepalive: true,
                    open: true
                }
            },
            test: {
                options: {
                    //protocol: 'https',
                    hostname: '0.0.0.0',
                    port: 7000
                }
            },
            debug: {
                options: {
                    hostname: '127.0.0.1',
                    port: 7000,
                    keepalive: true
                }
            }
        },
        less: {
            dev: {
                files: [{
                    expand: true,
                    cwd: 'web/smartedit/styling',
                    src: 'inner-styling.less',
                    dest: 'web/webroot/static-resources/dist/smartedit/css/',
                    ext: '.css'
                }, {
                    expand: true,
                    cwd: 'web/smartedit/styling',
                    src: 'outer-styling.less',
                    dest: 'web/webroot/static-resources/dist/smartedit/css/',
                    ext: '.css'
                }]
            },
        },
        //    bower: {
        //        install: {
        //          options: {
        //            install: true,
        //            copy: false,
        //            targetDir: './bower_components',
        //            cleanTargetDir: true
        //          }
        //        }
        //      },
        jsbeautifier: {
            files: ['Gruntfile.js', '*JSTests/**/*.js', '*JSTests/**/*.html', 'web/**/*.js', 'web/**/*.html', '!web/webroot/**/*'],
            options: {
                //config: "path/to/configFile",
                html: {
                    braceStyle: "collapse",
                    indentChar: " ",
                    indentScripts: "keep",
                    indentSize: 4,
                    maxPreserveNewlines: 10,
                    preserveNewlines: true,
                    unformatted: ["a", "sub", "sup", "b", "i", "u"],
                    wrapLineLength: 0
                },
                css: {
                    indentChar: " ",
                    indentSize: 4
                },
                js: {
                    braceStyle: "collapse",
                    breakChainedMethods: false,
                    e4x: false,
                    evalCode: false,
                    indentChar: " ",
                    indentLevel: 0,
                    indentSize: 4,
                    indentWithTabs: false,
                    jslintHappy: false,
                    keepArrayIndentation: false,
                    keepFunctionIndentation: false,
                    maxPreserveNewlines: 10,
                    preserveNewlines: true,
                    spaceBeforeConditional: true,
                    spaceInParen: false,
                    unescapeStrings: false,
                    wrapLineLength: 0,
                    endWithNewline: true
                }
            }
        },
        jssemicoloned: {
            files: ['Gruntfile.js', 'web/common/**/*.js', 'web/smarteditcontainer/**/*.js', 'web/smartedit/**/*.js', '*JSTests/**/*.js', '!web/webroot/**/*']
        },
        /*
         *in preparation for ng-annotate so as not to alter original source files
         */
        clean: {
            run: {
                src: ['tmp', 'jsTarget', 'web/webroot/static-resources/dist/smartedit/css/*.css']
            }
        },
        copy: {
            sources: {
                files: [
                    // includes files within path
                    {
                        expand: true,
                        flatten: false,
                        src: ['web/webApplicationInjector.js', 'web/common/**/*.js', 'web/smarteditloader/**/*.js', 'web/smarteditcontainer/**/*.js', 'web/smartedit/**/*.js'],
                        dest: 'jsTarget/'
                    }, {
                        expand: true,
                        flatten: true,
                        src: [hyTechnePattern + 'dist/fonts/*'],
                        dest: 'web/webroot/static-resources/dist/smartedit/fonts'
                    }
                ]
            },
            dev: {
                files: [
                    // includes files within path
                    {
                        expand: true,
                        flatten: true,
                        src: ['jsTarget/smarteditloader.js'],
                        dest: 'web/webroot/static-resources/smarteditloader/js'
                    }, {
                        expand: true,
                        flatten: true,
                        src: ['jsTarget/smarteditcontainer.js'],
                        dest: 'web/webroot/static-resources/smarteditcontainer/js'
                    }, {
                        expand: true,
                        flatten: true,
                        src: ['jsTarget/presmartedit.js'],
                        dest: 'web/webroot/static-resources/dist/smartedit/js'
                    }, {
                        expand: true,
                        flatten: true,
                        src: ['jsTarget/postsmartedit.js'],
                        dest: 'web/webroot/static-resources/dist/smartedit/js'
                    }
                ]
            },
            se_libraries: {
                files: [{
                    expand: true,
                    flatten: false,
                    cwd: 'web/common/services/',
                    src: ['**/*.js'],
                    dest: 'seLibraries/smartEditContainer/services'
                }, {
                    expand: true,
                    flatten: false,
                    cwd: 'web/smarteditcontainer/services/',
                    src: ['**/*.js'],
                    dest: 'seLibraries/smartEditContainer/services'
                }, {
                    expand: true,
                    flatten: false,
                    cwd: 'web/smartedit/services/',
                    src: ['**/*.js'],
                    dest: 'seLibraries/smartEdit/services'
                }, {
                    expand: true,
                    flatten: false,
                    cwd: 'web/common/services/',
                    src: ['**/*.js'],
                    dest: 'seLibraries/smartEdit/services'
                }, {
                    expand: true,
                    flatten: true,
                    src: ['jsTarget/templates.js'],
                    dest: 'seLibraries/smartEditContainer/services'
                }, {
                    expand: true,
                    flatten: true,
                    src: ['jsTarget/templates.js'],
                    dest: 'seLibraries/smartEdit/services'
                }]
            }
        },
        /**
         * Generate the custom jQueryUI
         */
        replace: {
            jquery_ui: {
                options: {
                    patterns: [{
                        match: JQUERY_UI_PATTERNS.DRAGGABLE_CONNECT_TO_SORTABLE_DRAG,
                        replacement: '<%= grunt.file.read("' + JQUERY_UI_DIFFS_PATH + JQUERY_UI_DIFFS.DRAGGABLE_CONNECT_TO_SORTABLE_DRAG + '") %>'
                    }, {
                        match: JQUERY_UI_PATTERNS.DRAGGABLE_CONNECT_TO_SORTABLE_START,
                        replacement: '<%= grunt.file.read("' + JQUERY_UI_DIFFS_PATH + JQUERY_UI_DIFFS.DRAGGABLE_CONNECT_TO_SORTABLE_START + '") %>'
                    }, {
                        match: JQUERY_UI_PATTERNS.UI_DRAGGABLE_CLEAR,
                        replacement: '<%= grunt.file.read("' + JQUERY_UI_DIFFS_PATH + JQUERY_UI_DIFFS.UI_DRAGGABLE_CLEAR + '") %>'
                    }, {
                        match: JQUERY_UI_PATTERNS.UI_DRAGGABLE_CREATE_HELPER,
                        replacement: '<%= grunt.file.read("' + JQUERY_UI_DIFFS_PATH + JQUERY_UI_DIFFS.UI_DRAGGABLE_CREATE_HELPER + '") %>'
                    }, {
                        match: JQUERY_UI_PATTERNS.UI_DRAGGABLE_GENERATE_POSITION,
                        replacement: '<%= grunt.file.read("' + JQUERY_UI_DIFFS_PATH + JQUERY_UI_DIFFS.UI_DRAGGABLE_GENERATE_POSITION + '") %>'
                    }, {
                        match: JQUERY_UI_PATTERNS.UI_SORTABLE_CLEAR,
                        replacement: '<%= grunt.file.read("' + JQUERY_UI_DIFFS_PATH + JQUERY_UI_DIFFS.UI_SORTABLE_CLEAR + '") %>'
                    }, {
                        match: JQUERY_UI_PATTERNS.UI_SORTABLE_GENERATE_POSITION,
                        replacement: '<%= grunt.file.read("' + JQUERY_UI_DIFFS_PATH + JQUERY_UI_DIFFS.UI_SORTABLE_GENERATE_POSITION + '") %>'
                    }, {
                        match: JQUERY_UI_PATTERNS.UI_SORTABLE_MOUSE_START,
                        replacement: '<%= grunt.file.read("' + JQUERY_UI_DIFFS_PATH + JQUERY_UI_DIFFS.UI_SORTABLE_MOUSE_START + '") %>'
                    }],
                    usePrefix: false,
                    force: false,

                    // IMPORTANT: When set to true, this property will stop the build if one of the RegEx is not matched.
                    pedantic: true
                },
                files: [{
                    expand: true,
                    flatten: true,
                    src: ['web/webroot/static-resources/thirdparties/jquery-ui/jquery-ui.js'],
                    dest: 'web/webroot/static-resources/thirdparties/jquery-ui-smartedit/'
                }]
            }
        },
        /**
         * code quality
         */
        jshint: {
            options: {
                predef: [
                    "angular",
                    "jasmine",
                    //"customMatchers",
                    "describe",
                    "it",
                    "after",
                    "afterEach",
                    "before",
                    "beforeEach",
                    "inject",
                    "module",
                    "expect"
                ]
            },
            all: ['Gruntfile.js', 'web/common/**/*.js', 'web/smarteditcontainer/**/*.js', 'web/smartedit/**/*.js', '*JSTests/**/*.js']
        },

        ngdocs: {
            options: {
                dest: 'jsTarget/docs',
                title: "SmartEdit API",
                startPage: '/#/smartEdit',
            },
            smartEdit: {
                api: true,
                src: ['web/common/**/*.js', 'web/smartedit/**/*.js'],
                title: 'SmartEdit'
            },
            smartEditContainer: {
                api: true,
                src: ['web/common/**/*.js', 'web/smarteditcontainer/**/*.js'],
                title: 'SmartEdit Container'
            }
        },

        /*
         * 'annotates' angular JS files to be minify-ready
         * 
         * @see https://github.com/mzgol/grunt-ng-annotate
         */
        ngAnnotate: {
            options: {
                singleQuotes: true,
            },
            run: {
                files: [{
                    expand: true,
                    src: ['jsTarget/**/*.js']
                }, ],
            }
        },

        karma: {
            options: {
                configFile: 'smarteditJSTests/karma.conf.js'
            },
            unitSmartedit: {
                configFile: 'smarteditJSTests/karma.conf.js',
                singleRun: true
            },
            unitSmarteditContainer: {
                configFile: 'smarteditcontainerJSTests/karma.conf.js',
                singleRun: true
            },
            continuousStorefront: {
                singleRun: false,
                autoWatch: true
            }
        },
        protractor: {
            options: {
                // Required to prevent grunt from exiting with a non-zero status in CI
                keepAlive: process.env.PROTRACTOR_KEEP_ALIVE === 'true',
                configFile: "smarteditcontainerJSTests/protractor-conf.js"
            },
            run: {},
            maxrun: {
                options: {
                    args: {
                        capabilities: {
                            shardTestFiles: true,
                            maxInstances: process.env.PROTRACTOR_CHROME_INSTANCES || 5,
                            chromeOptions: {
                                args: ['lang=en-US']
                            }

                        }
                    }
                }
            }
        },
        /*
         * generates angular.module('run').run(['$templateCache', function($templateCache) {}]) module
         * that contains template caches so that they become minifyable !!!
         */
        ngtemplates: {
            options: {
                standalone: true, //to declare a moduel as opposed to binding to an existing one
                module: 'coretemplates'
            },
            run: {
                src: ['web/common/**/*.html', 'web/smarteditcontainer/**/*.html', 'web/smartedit/**/*.html'],
                dest: 'jsTarget/templates.js'
            }
        },
        concat: {
            options: {
                separator: '\n'
            },
            webApplicationInjector: {
                src: [
                    'web/webroot/static-resources/thirdparties/scriptjs/script.js',
                    'jsTarget/web/webApplicationInjector.js',
                ],
                dest: 'jsTarget/webApplicationInjector.js'
            },
            common: {
                src: [
                    'jsTarget/web/common/**/*.js',
                ],
                dest: 'jsTarget/common.js'
            },
            smarteditloader: {
                src: [
                    'jsTarget/web/common/**/*.js',
                    'jsTarget/templates.js',
                    'jsTarget/web/smarteditloader/**/*.js'
                ],
                dest: 'jsTarget/smarteditloader.js'
            },
            containerAdministrationModule: {
                src: [
                    'jsTarget/web/smarteditcontainer/modules/administrationModule/**/*.js',
                ],
                dest: 'web/webroot/static-resources/smarteditcontainer/modules/administrationModule.js'
            },
            containerDefaultCMSModule: {
                src: [
                    'jsTarget/web/smarteditcontainer/modules/defaultCMS/**/*.js',
                ],
                dest: 'web/webroot/static-resources/smarteditcontainer/modules/defaultCMS.js'
            },
            smarteditcontainer: {
                src: [
                    'jsTarget/web/common/**/*.js',
                    'jsTarget/templates.js',
                    'jsTarget/web/smarteditcontainer/core/**/*.js',
                    'jsTarget/web/smarteditcontainer/services/**/*.js',
                ],
                dest: 'jsTarget/smarteditcontainer.js'
            },
            smartEditSystemModule: {
                src: [
                    'jsTarget/web/smartedit/modules/systemModule/**/*.js',
                ],
                dest: 'web/webroot/static-resources/smartedit/modules/systemModule.js'
            },
            presmartedit: {
                src: [
                    'jsTarget/templates.js',
                    'jsTarget/web/common/**/*.js',
                    'jsTarget/web/smartedit/core/**/*.js',
                    'jsTarget/web/smartedit/services/**/*.js',
                    '!jsTarget/web/smartedit/core/smartedit.js',
                    '!jsTarget/web/smartedit/core/smarteditbootstrap.js'
                ],
                dest: 'jsTarget/presmartedit.js'
            },
            postsmartedit: {
                src: [
                    'jsTarget/web/smartedit/core/smartedit.js',
                    'jsTarget/web/smartedit/core/smarteditbootstrap.js'
                ],
                dest: 'jsTarget/postsmartedit.js'
            },
            productize: {
                src: [
                    'web/webroot/static-resources/thirdparties/angular/angular.js',
                    'web/webroot/static-resources/thirdparties/angular-resource/angular-resource.min.js',
                    'web/webroot/static-resources/thirdparties/angular-cookies/angular-cookies.min.js',
                    'web/webroot/static-resources/thirdparties/angular-mocks/angular-mocks.js',
                    'web/webroot/static-resources/thirdparties/angular-bootstrap/ui-bootstrap-tpls.min.js',
                    'web/webroot/static-resources/thirdparties/angular-translate/angular-translate.min.js',
                    'web/webroot/static-resources/thirdparties/angular-sanitize/angular-sanitize.min.js',
                    'web/webroot/static-resources/thirdparties/angular-translate-loader-static-files/angular-translate-loader-static-files.min.js',
                    'web/webroot/static-resources/thirdparties/ui-select/dist/select.js',
                    'web/webroot/static-resources/thirdparties/ckeditor/ckeditor.js',
                    'web/webroot/static-resources/thirdparties/bootstrap/dist/js/bootstrap.min.js',
                    'web/webroot/static-resources/thirdparties/polyfills/**/*.js',
                    'web/webroot/static-resources/dist/smartedit/js/presmartedit.js'
                ],

                dest: 'web/webroot/static-resources/dist/smartedit/js/presmartedit.js'
            }
        },
        uglify: {
            dist: {
                files: {
                    'web/webroot/static-resources/webApplicationInjector.js': ['jsTarget/webApplicationInjector.js'],
                    'web/webroot/static-resources/smarteditloader/js/smarteditloader.js': ['jsTarget/smarteditloader.js'],
                    'web/webroot/static-resources/smarteditcontainer/js/smarteditcontainer.js': ['jsTarget/smarteditcontainer.js'],
                    'web/webroot/static-resources/dist/smartedit/js/presmartedit.js': ['jsTarget/presmartedit.js'],
                    'web/webroot/static-resources/dist/smartedit/js/postsmartedit.js': ['jsTarget/postsmartedit.js'],
                    'web/webroot/static-resources/thirdparties/jquery-ui-smartedit/jquery-ui.min.js': ['web/webroot/static-resources/thirdparties/jquery-ui-smartedit/jquery-ui.js'],
                    'web/webroot/static-resources/thirdparties/jquery-ui-smartedit/jquery-ui-smartedit-extension.min.js': ['web/webroot/static-resources/thirdparties/jquery-ui-smartedit/jquery-ui-smartedit-extension.js']
                },
                options: {
                    mangle: true //ok since one has ng-annotate beforehand
                }
            }
        },
        watch: {
            e2e: {
                files: ['Gruntfile.js', 'web/common/**/*', 'web/smarteditloader/**/*', 'web/smartedit/**/*', 'web/smarteditcontainer/**/*', '*JSTests/e2e/**/*'],
                tasks: ['protractorRun'],
                options: {
                    atBegin: true
                }
            },
            test: {
                files: ['Gruntfile.js', 'web/common/**/*', 'web/smarteditloader/**/*', 'web/smartedit/**/*', 'web/smarteditcontainer/**/*', '*JSTests/unit/**/*'],
                tasks: ['test'],
                options: {
                    atBegin: true
                }
            },
            test_only: {
                files: ['Gruntfile.js', 'web/common/**/*', 'web/smarteditloader/**/*', 'web/smartedit/**/*', 'web/smarteditcontainer/**/*', '*JSTests/unit/**/*'],
                tasks: ['test_only'],
                options: {
                    atBegin: true
                }
            },
            dev: {
                files: ['Gruntfile.js', 'web/common/**/*', 'web/smarteditloader/**/*', 'web/smartedit/**/*', 'web/smarteditcontainer/**/*', '*JSTests/unit/**/*'],
                tasks: ['dev'],
                options: {
                    atBegin: true
                }
            },
            dev_no_tests: {
                files: ['Gruntfile.js', 'web/common/**/*', 'web/smarteditloader/**/*', 'web/smartedit/**/*', 'web/smarteditcontainer/**/*', '*JSTests/unit/**/*'],
                tasks: ['dev_compile'],
                options: {
                    atBegin: true
                }
            },
            pack: {
                files: ['Gruntfile.js', 'web/common/**/*', 'web/smarteditloader/**/*', 'web/smartedit/**/*', 'web/smarteditcontainer/**/*', '*JSTests/unit/**/*'],
                tasks: ['package'],
                options: {
                    atBegin: true
                }
            },
            ngdocs: {
                files: ['web/**/*'],
                tasks: ['ngdocs'],
                options: {
                    atBegin: true
                }
            }
        }
    });


    grunt.file.setBase('./');

    grunt.registerTask("unitSmartedit", 'Executes unit tests for smartedit', function() {
        //if npmtestancillary is not present, phantomjs drivers won't be present
        var found = false;
        grunt.file.expand({
            filter: 'isFile'
        }, phantomJSPattern).forEach(function(dir) {
            found = true;
            grunt.task.run('karma:unitSmartedit');
            return;
        });
        if (!found) {
            grunt.log.warn('karma:unitSmartedit grunt phase was not run since no phantomjs driver found under ' + phantomJSPattern);
        }
    });

    grunt.registerTask("unitSmarteditContainer", 'Executes unit tests for smarteditContainer', function() {
        //if npmtestancillary is not present, phantomjs drivers won't be present
        var found = false;
        grunt.file.expand({
            filter: 'isFile'
        }, phantomJSPattern).forEach(function(dir) {
            found = true;
            grunt.task.run('karma:unitSmarteditContainer');
            return;
        });
        if (!found) {
            grunt.log.warn('karma:unitSmarteditContainer grunt phase was not run since no phantomjs driver found under ' + phantomJSPattern);
        }

    });

    grunt.registerTask("protractorRun", 'Executes e2e tests for smarteditcontainer', function() {
        //if npmtestancillary is not present, chrome drivers won't be present
        if (grunt.file.expand({
                filter: 'isFile'
            }, phantomJSPattern).length > 0) {
            grunt.task.run('protractor:run');
        } else {
            grunt.log.warn('protractor:run grunt phase was not run since no chrome driver found under ' + chromeDriverPattern);
        }
    });

    grunt.registerTask("protractorMaxrun", 'Executes e2e tests for smarteditcontainer', function() {
        //if npmtestancillary is not present, chrome drivers won't be present
        if (grunt.file.expand({
                filter: 'isFile'
            }, chromeDriverPattern).length > 0) {
            grunt.task.run('protractor:maxrun');
        } else {
            grunt.log.warn('protractor:maxrun grunt phase was not run since no chrome driver found under ' + chromeDriverPattern);
        }

    });

    grunt.registerTask('styling_only', ['less']);
    grunt.registerTask('styling', ['clean', 'styling_only']);

    grunt.registerTask('sanitize', ['jssemicoloned', 'jsbeautifier']);

    grunt.registerTask('compile_only', ['jshint', 'jsbeautifier', 'copy:sources', 'replace:jquery_ui', 'styling_only', 'ngtemplates:run', 'ngAnnotate:run']);
    grunt.registerTask('compile', ['clean', 'compile_only']);

    grunt.registerTask('test_only', ['unitSmartedit', 'unitSmarteditContainer']);
    grunt.registerTask('test', ['compile', 'test_only']);

    grunt.registerTask('aggregate', ['concat:containerAdministrationModule', 'concat:containerDefaultCMSModule', 'concat:smarteditloader', 'concat:smarteditcontainer', 'concat:smartEditSystemModule', 'concat:presmartedit', 'concat:postsmartedit', 'concat:webApplicationInjector']);

    grunt.registerTask('dev_only', ['aggregate', 'copy:dev', 'concat:productize', 'copy:se_libraries']);
    grunt.registerTask('dev', ['test', 'dev_only']);
    grunt.registerTask('dev_compile', ['compile', 'dev_only']);

    grunt.registerTask('package_only', ['aggregate', 'uglify:dist', 'concat:productize', 'ngdocs', 'copy:se_libraries']);
    grunt.registerTask('package', ['test', 'package_only']);
    grunt.registerTask('packageSkipTests', ['compile_only', 'package_only']);

    grunt.registerTask('e2e_continuous', ['connect:test', 'watch:e2e']);
    grunt.registerTask('e2e', ['connect:test', 'protractorRun']);
    grunt.registerTask('e2e_max', ['connect:test', 'protractorMaxrun']);
    grunt.registerTask('verify_only', ['e2e']);
    grunt.registerTask('verify', ['package', 'verify_only']); //if you change verify please change verfiy_max task aswell, it should in sync
    grunt.registerTask('verify_max', ['package', 'e2e_max']);

};
