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
    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-contrib-connect');
    grunt.loadNpmTasks("grunt-jsbeautifier");
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-less');
    grunt.loadNpmTasks('grunt-karma');
    grunt.loadNpmTasks('grunt-ngdocs');

    var phantomJSPattern = 'node_modules/karma-phantomjs-launcher/node_modules/phantomjs/lib/phantom/bin/phantomjs*';
    var chromeDriverPattern = 'node_modules/protractor/selenium/chromedriver/chromedriver*';

    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),

        connect: {
            server: {
                options: {
                    hostname: 'localhost',
                    port: 7000
                }
            }
        },
        less: {
            dev: {
                files: [{
                    expand: true,
                    cwd: 'web/features/styling',
                    src: '*.less',
                    dest: 'web/webroot/css/',
                    ext: '.css'
                }]
            },
        },
        /*
         *in preparation for ng-annotate so as not to alter original source files
         */
        jsbeautifier: {
            files: ['Gruntfile.js', 'web/features/**/*', 'jsTests/**/*'],
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
            files: ['Gruntfile.js', 'web/features/**/*.js', 'jsTests/**/*.js']
        },
        clean: {
            temp: {
                src: ['jsTarget']
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
            all: ['Gruntfile.js', 'web/features/**/*.js', 'jsTests/**/*.js']
        },

        ngdocs: {
            options: {
                dest: 'jsTarget/docs',
                title: "ycmssmartedit API",
                startPage: '/#/ycmssmartedit',
            },
            ycmssmartedit: {
                api: true,
                src: ['web/features/common/**/*.js', 'web/features/ycmssmartedit/**/*.js'],
                title: 'ycmssmartedit'
            }
        },
        watch: {
            test: {
                files: ['Gruntfile.js', 'web/features/**/*', 'jsTests/**/*'],
                tasks: ['test'],
                options: {
                    atBegin: true
                }
            },
            e2e: {
                files: ['Gruntfile.js', 'web/features/**/*'],
                tasks: ['e2e'],
                options: {
                    atBegin: true
                }
            }
        }
    });


    grunt.file.setBase('./');

    grunt.registerTask('multiKarma', function() {
        //if npmtestancillary is not present, phantomjs drivers won't be present
        if (grunt.file.expand({
                filter: 'isFile'
            }, phantomJSPattern).length > 0) {

            var multiKarmaTask = {};

            grunt.file.expand({
                filter: 'isDirectory'
            }, "jsTests/*").forEach(function(dir) {
                if (!endsWith(dir, "/utils")) {
                    var folderName = dir.replace("jsTests/", "");

                    multiKarmaTask[folderName] = {
                        options: {
                            configFile: dir + "/karma.conf.js",
                            singleRun: true
                        }
                    };
                }
            });

            grunt.config.set('karma', multiKarmaTask);
            grunt.task.run('karma');
        } else {
            grunt.log.warn('multiKarma grunt phase was not run since no phantomjs driver found under ' + phantomJSPattern);
        }
    });

    grunt.registerTask('multiProtractor', function() {
        //if npmtestancillary is not present, chrome drivers won't be present
        if (grunt.file.expand({
                filter: 'isFile'
            }, chromeDriverPattern).length > 0) {

            var multiProtractor = {};

            grunt.file.expand({
                filter: 'isDirectory'
            }, "jsTests/*").forEach(function(dir) {
                if (!endsWith(dir, "/utils")) {
                    var folderName = dir.replace("jsTests/", "");

                    multiProtractor[folderName] = {
                        options: {
                            keepAlive: process.env.PROTRACTOR_KEEP_ALIVE === 'true',
                            configFile: 'jsTests/protractor-conf.js',
                            args: {
                                specs: ['jsTests/' + folderName + '/e2e/*Test.js']
                            }
                        }
                    };
                }
            });

            grunt.config.set('protractor', multiProtractor);
            grunt.task.run('protractor');

        } else {
            grunt.log.warn('protractor grunt phase was not run since no chrome driver found under ' + chromeDriverPattern);
        }
    });

    grunt.registerTask("multiProtractorMax", 'Executes end to end tests for each project via protractor separately', function() {
        //if npmtestancillary is not present, chrome drivers won't be present
        if (grunt.file.expand({
                filter: 'isFile'
            }, chromeDriverPattern).length > 0) {

            var multiProtractor = {};

            grunt.file.expand({
                filter: 'isDirectory'
            }, "jsTests/*").forEach(function(dir) {
                if (!endsWith(dir, "/utils")) {
                    var folderName = dir.replace("jsTests/", "");

                    multiProtractor[folderName] = {
                        options: {
                            keepAlive: process.env.PROTRACTOR_KEEP_ALIVE === 'true',
                            configFile: "jsTests/protractor-conf.js",
                            args: {
                                specs: ['jsTests/' + folderName + '/e2e/**/*Test.js'],
                                capabilities: {
                                    shardTestFiles: true,
                                    maxInstances: process.env.PROTRACTOR_CHROME_INSTANCES || 5
                                }
                            }
                        }
                    };

                }
            });

            grunt.config.set('protractor', multiProtractor);
            grunt.task.run('protractor');

        } else {
            grunt.log.warn('protractor grunt phase was not run since no chrome driver found under ' + chromeDriverPattern);
        }

    });

    // Helper Functions
    function endsWith(inputStr, suffix) {
        return inputStr.match(suffix + "$") == suffix;
    }

    grunt.registerTask('docs', ['clean', 'ngdocs']);

    grunt.registerTask('styling', ['clean', 'less']);

    grunt.registerTask('sanitize', ['jssemicoloned', 'jsbeautifier']);

    grunt.registerTask('compile_only', ['jshint']);
    grunt.registerTask('compile', ['clean', 'compile_only']);

    grunt.registerTask('test_only', ['multiKarma']);
    grunt.registerTask('test', ['compile', 'multiKarma']);

    grunt.registerTask('e2e', ['connect:server', 'multiProtractor']);
    grunt.registerTask('e2e_max', ['connect:server', 'multiProtractorMax']);

};
