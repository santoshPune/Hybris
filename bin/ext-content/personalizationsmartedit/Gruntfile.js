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
 *
 *
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
            },
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
                    indentSize: 4,
                    fileTypes: [".less"]
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
                src: ['tmp', 'jsTarget']
            },
            cssfiles: {
                src: ['web/webroot/css/*.css']
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
                title: "personalizationsmartedit API",
                startPage: '/#/personalizationsmartedit',
            },
            personalizationsmartedit: {
                api: true,
                src: ['web/features/common/**/*.js', 'web/features/personalizationsmartedit/**/*.js'],
                title: 'personalizationsmartedit'
            },
            personalizationsmarteditcontainer: {
                api: true,
                src: ['web/features/common/**/*.js', 'web/features/personalizationsmarteditcontainer/**/*.js'],
                title: 'personalizationsmarteditcontainer'
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
        uglify: {
            dist: {
                files: [{
                    expand: true, // Enable dynamic expansion.
                    cwd: 'jsTarget/', // Src matches are relative to this path.
                    src: ['*.js'], // Actual pattern(s) to match.
                    dest: 'web/webroot/personalizationsmartedit/js/', // Destination path prefix.
                    ext: '.js', // Dest filepaths will have this extension.
                    extDot: 'first' // Extensions in filenames begin after the first dot
                }],
                options: {
                    mangle: true //ok since one has ng-annotate beforehand
                }
            }
        },
        watch: {
            e2e: {
                files: ['Gruntfile.js', 'web/features/**/*', '*jsTests/e2e/**/*'],
                tasks: ['protractorRun'],
                options: {
                    atBegin: true
                }
            },
            test: {
                files: ['Gruntfile.js', 'web/features/**/*', 'jsTests/**/*'],
                tasks: ['test'],
                options: {
                    atBegin: true
                }
            },
            dev: {
                files: ['Gruntfile.js', 'web/features/**/*', 'jsTests/**/*'],
                tasks: ['dev'],
                options: {
                    atBegin: true
                }
            },
            pack: {
                files: ['Gruntfile.js', 'web/features/**/*', 'jsTests/**/*'],
                tasks: ['package'],
                options: {
                    atBegin: true
                }
            }
        },
        karma: {
            options: {
                configFile: 'jsTests/unit/commons/karma.conf.js',
            },
            unitCommons: {
                configFile: 'jsTests/unit/commons/karma.conf.js',
                singleRun: true
            },
            unitPersonalizationsmartedit: {
                configFile: 'jsTests/unit/personalizationsmartedit/karma.conf.js',
                singleRun: true
            },
            unitPersonalizationsmarteditcontainer: {
                configFile: 'jsTests/unit/personalizationsmarteditcontainer/karma.conf.js',
                singleRun: true
            },
        },
        protractor: {
            options: {
                keepAlive: false,
                configFile: "jsTests/protractor-conf.js"
            },
            run: {},
            maxrun: {
                options: {
                    args: {
                        capabilities: {
                            shardTestFiles: true,
                            maxInstances: 4
                        }
                    }
                }
            }
        }
    });

    grunt.file.setBase('./');

    grunt.registerTask('copyDev', function() {
        var copyDevTask = {
            dev: {
                expand: true,
                flatten: true,
                src: ['jsTarget/*.js'],
                dest: 'web/webroot/personalizationsmartedit/js'
            }
        };

        grunt.config.set('copy', copyDevTask);
        grunt.task.run('copy');
    });

    grunt.registerTask('concatStyling', function() {
        var concatStylingTask = {
            styling: {
                separator: ';',
                src: ["web/webroot/css/*.css"],
                dest: "web/webroot/css/style.css",
            }
        };

        grunt.config.set('concat', concatStylingTask);
        grunt.task.run('concat');
    });

    grunt.registerTask('multiCopySource', function() {
        var multiCopySourceTask = {};

        // read all subdirectories from your modules folder
        grunt.file.expand({
            filter: 'isDirectory'
        }, 'web/features/*').forEach(function(dir) {
            multiCopySourceTask[dir] = {
                expand: true,
                flatten: false,
                src: [dir + '/**/*.js'],
                dest: 'jsTarget/'
            };
        });
        grunt.config.set('copy', multiCopySourceTask);
        grunt.task.run('copy');
    });

    /*
     * generates angular.module('run').run(['$templateCache', function($templateCache) {}]) module
     * that contains template caches so that they become minifyable !!!
     */
    grunt.registerTask('multiNGTemplates', function() {
        var multiNGTemplatesTask = {};

        grunt.file.expand({
            filter: 'isDirectory'
        }, "web/features/*").forEach(function(dir) {
            var folderName = dir.replace("web/features/", "");

            multiNGTemplatesTask[folderName] = {
                src: [dir + '/**/*Template.html'],
                dest: 'jsTarget/' + dir + '/templates.js',
                options: {
                    standalone: true, //to declare a module as opposed to binding to an existing one
                    module: folderName + 'Templates'
                }
            };
        });
        grunt.config.set('ngtemplates', multiNGTemplatesTask);
        grunt.task.run('ngtemplates');
    });

    grunt.registerTask('multiConcat', function() {
        var multiConcatTask = {};

        grunt.file.expand({
            filter: 'isDirectory'
        }, "jsTarget/web/features/*").forEach(function(dir) {
            if (!endsWith(dir, "/commons")) {
                var folderName = dir.replace("jsTarget/web/features/", "");

                multiConcatTask[folderName] = {
                    src: ["jsTarget/web/features/commons/**/*.js", dir + '/**/*.js'],
                    dest: 'jsTarget/' + folderName + '.js'
                };

                grunt.config.set('concat', multiConcatTask);
            }
        });

        grunt.task.run('concat');
    });

    grunt.registerTask("unitPersonalizationsmartedit", 'Executes unit tests for personalizationsmartedit', function() {
        //if npmtestancillary is not present, phantomjs drivers won't be present
        var found = false;
        grunt.file.expand({
            filter: 'isFile'
        }, phantomJSPattern).forEach(function(dir) {
            found = true;
            grunt.task.run('karma:unitCommons');
            grunt.task.run('karma:unitPersonalizationsmartedit');
            grunt.task.run('karma:unitPersonalizationsmarteditcontainer');
            return;
        });
        if (!found) {
            grunt.log.warn('karma:unitPersonalizationsmartedit grunt phase was not run since no phantomjs driver found under ' + phantomJSPattern);
        }
    });

    grunt.registerTask("protractorRun", 'Executes e2e tests for personalizationsmartedit', function() {
        //if npmtestancillary is not present, chrome drivers won't be present
        if (grunt.file.expand({
                filter: 'isFile'
            }, chromeDriverPattern).length > 0) {
            grunt.task.run('protractor:run');
        } else {
            grunt.log.warn('protractor:run grunt phase was not run since no chrome driver found under ' + chromeDriverPattern);
        }
    });

    grunt.registerTask("protractorMaxrun", 'Executes e2e tests for personalization', function() {
        //if npmtestancillary is not present, chrome drivers won't be present
        if (grunt.file.expand({
                filter: 'isFile'
            }, chromeDriverPattern).length > 0) {
            grunt.task.run('protractor:maxrun');
        } else {
            grunt.log.warn('protractor:maxrun grunt phase was not run since no chrome driver found under ' + chromeDriverPattern);
        }
    });

    // Helper Functions
    function endsWith(inputStr, suffix) {
        return inputStr.match(suffix + "$") == suffix;
    }

    grunt.registerTask('styling_only', ['less', 'concatStyling']);
    grunt.registerTask('styling', ['clean', 'styling_only']);

    grunt.registerTask('sanitize', ['jssemicoloned', 'jsbeautifier']);

    grunt.registerTask('compile_only', ['jshint', 'jsbeautifier', 'multiCopySource', 'styling_only', 'multiNGTemplates', 'ngAnnotate:run']);
    grunt.registerTask('compile', ['clean', 'compile_only']);

    grunt.registerTask('test_only', ['unitPersonalizationsmartedit']);
    grunt.registerTask('test', ['compile', 'test_only']);

    grunt.registerTask('dev_only', ['multiConcat', 'copyDev']);
    grunt.registerTask('dev', ['test', 'dev_only']);

    grunt.registerTask('package_only', ['multiConcat', 'uglify:dist', 'ngdocs']);
    grunt.registerTask('package', ['test', 'package_only']);

    grunt.registerTask('verify_only', ['e2e']);
    grunt.registerTask('verify', ['package', 'verify_only']);

    grunt.registerTask('full_dev', ['compile', 'package', 'copyDev']);

    grunt.registerTask('e2e_continuous', ['connect:server', 'watch:e2e']);
    grunt.registerTask('e2e', ['connect:server', 'protractorRun']);
    grunt.registerTask('e2e_max', ['connect:server', 'protractorMaxrun']);
};
