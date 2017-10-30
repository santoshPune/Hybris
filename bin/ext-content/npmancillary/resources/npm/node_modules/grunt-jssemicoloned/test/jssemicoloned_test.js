'use strict';

var grunt = require('grunt');
var jssemicoloned = require('../tasks/lib/jssemicoloned').init(grunt);

/*
  ======== A Handy Little Nodeunit Reference ========
  https://github.com/caolan/nodeunit

  Test methods:
    test.expect(numAssertions)
    test.done()
  Test assertions:
    test.ok(value, [message])
    test.equal(actual, expected, [message])
    test.notEqual(actual, expected, [message])
    test.deepEqual(actual, expected, [message])
    test.notDeepEqual(actual, expected, [message])
    test.strictEqual(actual, expected, [message])
    test.notStrictEqual(actual, expected, [message])
    test.throws(block, [error], [message])
    test.doesNotThrow(block, [error], [message])
    test.ifError(value)
*/

exports.jssemicoloned = {
    setUp: function (done) {
        // setup here
        done();
    },
    'jssemicoloned': function (test) {
        var input, unexpected, expected, actual;
        test.expect(14);

        input = 'void(0);';
        expected = null;
        actual = jssemicoloned.fix(input);
        test.equal(actual, expected, 'correct code shouldn\'t change');

        input = 'void(0)\n';
        expected = 'void(0);\n';
        actual = jssemicoloned.fix(input);
        test.equal(actual, expected, 'semicolon expected before a newline');

        input = 'if (true) { a = false || {} }\n';
        expected = 'if (true) { a = false || {}; }\n';
        actual = jssemicoloned.fix(input);
        test.equal(actual, expected, 'semicolon expected at end of block');

        input = 'function add (a, b) {\nreturn\na + b}';
        unexpected = null;
        expected = 'function add (a, b) {\nreturn;\na + b;}';
        actual = jssemicoloned.fix(input);
        test.notEqual(actual, unexpected, 'semicolon expected at end of line even after return, so should return non-null');
        test.equal(actual, expected, 'semicolon expected at end of line even after return');

        input = 'var a = function(){};\n';
        unexpected = 'var a = function(){;};\n';
        expected = null;
        actual = jssemicoloned.fix(input);
        test.notEqual(actual, unexpected, 'semicolon inserted in noop');
        test.equal(actual, expected, 'don\'t insert semicolon in noop');

        input = 'var a = function() {\nb();\n};\n';
        unexpected = 'var a = function() {\nb();;\n};\n';
        expected = null;
        actual = jssemicoloned.fix(input);
        test.notEqual(actual, unexpected, 'double semicolon');
        test.equal(actual, expected, 'no double semicolon');
        
        input = 'var a = function() {\nb();\n};\n';
        unexpected = 'var a = function() {\nb();;\n};\n';
        expected = null;
        actual = jssemicoloned.fix(input);
        test.notEqual(actual, unexpected, 'double semicolon');
        test.equal(actual, expected, 'no double semicolon');
        
        input = ';(function(){\nvar a={}\na.b=function(c,d,e){\nvar f=g.h(i)\nj.k(l,m)\n}\n})()\n';
        expected = ';(function(){\nvar a={};\na.b=function(c,d,e){\nvar f=g.h(i);\nj.k(l,m);\n};\n})();\n';
        actual = jssemicoloned.fix(input);
        test.equal(actual, expected, 'more complex thingy');

        input = 'x = function() {try{return a}\ncatch(e){return b}\n}\n';
        expected = 'x = function() {try{return a;}\ncatch(e){return b;}\n};\n';
        actual = jssemicoloned.fix(input);
        test.equal(actual, expected, 'should\'t add a semicolon after catch');

        input = 'void(0)';
        expected = 'void(0);';
        actual = jssemicoloned.fix(input, 'semicolon expected before EOF');
        test.equal(actual, expected);

        test.done();
    }
};