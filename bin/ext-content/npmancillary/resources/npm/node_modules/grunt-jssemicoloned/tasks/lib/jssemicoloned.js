'use strict';
var acorn = require('acorn');

exports.init = function (grunt) {

    function _insertSemicolons(source, semicolonInserts) {
        var injectedSource = [],
            scIndex,
            injectPoint,
            scCounter = 0,
            scPrevChar;
        for (scIndex = 0; scIndex < semicolonInserts.length; scIndex++) {
            injectPoint = semicolonInserts[scIndex];
            if (injectPoint !== scCounter) {
                scPrevChar = source.charAt(injectPoint - 1);
                injectedSource.push(source.substring(scCounter, injectPoint), ';');
                scCounter = injectPoint;
            }
        }
        injectedSource.push(source.substring(scCounter, source.length));
        return injectedSource.join('');
    }

    exports.fix = function (src, options, globals, extraMsg) {
        var syntax, semicoloned;

        grunt.log.write('Adding semicolons' + (extraMsg ? ' ' + extraMsg : '') + '  ');
        
        try {
            // Skip shebang.
            if (src[0] === '#' && src[1] === '!') {
                grunt.log.ok('Skipped');
            } else {
                syntax = acorn.parse(src);
                semicoloned = _insertSemicolons(src, syntax.semicolonInserts);
                if (semicoloned !== src) {
                    grunt.log.ok('Fixed');
                    return semicoloned;
                } else {
                    grunt.log.ok();
                    return null;
                }
            }
        } catch (e) {
            grunt.log.write('\n');
            grunt.log.error(e.message);
            grunt.fail.errorcount++;
        }
        
    };
    
    
    return exports;
};
