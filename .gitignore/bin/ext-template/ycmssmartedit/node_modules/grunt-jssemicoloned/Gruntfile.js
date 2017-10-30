module.exports = function (grunt) {
    'use strict';

    // Project configuration.
    grunt.initConfig({
        nodeunit: {
            files: ['test/**/*.js']
        },
        watch: {
            files: ['Gruntfile.js', 'tasks/**/*.js', 'test/**/*.js'],
            tasks: 'default'
        },
        jshint: {
            options: {
                jshintrc: '.jshintrc'
            },
            files: ['Gruntfile.js', 'tasks/**/*.js', 'test/**/*.js']
        }
    });
    
    grunt.loadNpmTasks('grunt-contrib-nodeunit');
    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-watch');

    // Load local tasks.
    grunt.loadTasks('tasks');

    // Default task.
    grunt.registerTask('default', ['jshint', 'nodeunit']);

};
