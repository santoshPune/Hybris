'use strict';

module.exports = function (grunt) {

    // Internal lib.
    var jssemicoloned = require('./lib/jssemicoloned').init(grunt);

    grunt.registerMultiTask('jssemicoloned', 'Insert semicolons into JavaScript source.', function () {
        var options = {}, globals = {};


        grunt.file.expand(this.filesSrc).forEach(function (filepath) {
            grunt.verbose.write('jssemicoloned ' + filepath);
            var result = jssemicoloned.fix(grunt.file.read(filepath), options, globals, filepath);
            if (result !== null && typeof result === 'string') {
                grunt.file.write(filepath, result);
            }
        });

        if (this.errorCount === 0) {
            grunt.log.writeln('Done.');
        }

        return (this.errorCount === 0);
    });

    
};
