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
/**
 * @ngdoc service
 * @name functionsModule
 *
 * @description
 * provides a list of useful functions that can be used as part of the SmartEdit framework.
 */
angular.module('functionsModule', [])

.factory('ParseError', function() {
        var ParseError = function(value) {
            this.value = value;
        };
        return ParseError;
    })
    /**
     * @ngdoc service
     * @name functionsModule.getOrigin
     *
     * @description
     * returns document location origin
     * Some browsers still do not support W3C document.location.origin, this function caters for gap.
     */
    .factory('getOrigin', function() {
        return function() {
            return window.location.protocol + "//" + window.location.hostname + (window.location.port ? ':' + window.location.port : '');
        };
    })
    /**
     * @ngdoc service
     * @name functionsModule.isBlank
     *
     * @description
     * <b>isBlank</b> will check if a given string is undefined or null or empty.
     * - returns TRUE for undefined / null/ empty string
     * - returns FALSE otherwise
     *
     * @param {String} inputString any input string.
     * 
     * @returns {boolean} true if the string is null else false
     */
    .factory('isBlank', function() {
        return function(value) {
            return (typeof value == 'undefined' || value === null || value === "null" || value.toString().trim().length === 0);
        };
    })

/**
 * @ngdoc service
 * @name functionsModule.extend
 *
 * @description
 * <b>extend</b> provides a convenience to either default a new child or "extend" an existing child with the prototype of the parent
 *
 * @param {Class} ParentClass which has a prototype you wish to extend.
 * @param {Class} ChildClass will have its prototype set.
 * 
 * @returns {Class} ChildClass which has been extended
 */
.factory('extend', function() {
    return function(ParentClass, ChildClass) {
        if (!ChildClass) {
            ChildClass = function() {};
        }
        ChildClass.prototype = Object.create(ParentClass.prototype);
        return ChildClass;
    };
})

/**
 * @ngdoc service
 * @name functionsModule.hitch
 *
 * @description
 * <b>hitch</b> will create a new function that will pass our desired context (scope) to the given function.
 * This method will also pre-bind the given parameters.
 *
 * @param {Object} scope scope that is to be assigned.
 * @param {Function} method the method that needs binding. 
 * 
 * @returns {Function} a new function thats binded to the given scope
 */
.factory('hitch', function() {
    return function(scope, method) {

        var argumentArray = Array.prototype.slice.call(arguments); // arguments is not an array
        // (from  http://www.sitepoint.com/arguments-a-javascript-oddity/)

        var preboundArguments = argumentArray.slice(2);

        return function lockedMethod() {

            // from here, "arguments" are the arguments passed to lockedMethod

            var postBoundArguments = Array.prototype.slice.call(arguments);

            return method.apply(scope, preboundArguments.concat(postBoundArguments));

        };

    };
})

/**
 * @ngdoc service
 * @name functionsModule.customTimeout
 *
 * @description
 * <b>customTimeout</b> will call the javascrit's native setTimeout method to execute a given function after a specified period of time.
 * This method is better than using $timeout since it is difficult to assert on $timeout during end-to-end testing.
 *
 * @param {Function} func function that needs to be executed after the specified duration.
 * @param {Number} duration time in milliseconds. 
 */
.factory('customTimeout', ['$rootScope', function($rootScope) {
    return function(func, duration) {
        setTimeout(function() {
            func();
            $rootScope.$digest();
        }, duration);
    };
}])

/**
 * @ngdoc service
 * @name functionsModule.copy
 *
 * @description
 * <b>copy</b> will do a deep copy of the given input object.
 *
 * @param {Object} candidate the javaScript value that needs to be deep copied.
 * 
 * @returns {Object} A deep copy of the input
 */
.factory('copy', function() {
    return function(candidate) {
        return JSON.parse(JSON.stringify(candidate));
    };
})

/**
 * @ngdoc service
 * @name functionsModule.merge
 *
 * @description
 * <b>merge</b> will merge the contents of two objects together into the first object.
 *
 * @param {Object} target any JavaScript object.
 * @param {Object} source any JavaScript object.
 * 
 * @returns {Object} a new object as a result of merge
 */
.factory('merge', function() {
    return function(source, target) {

        jQuery.extend(source, target);

        return source;
    };
})

/**
 * @ngdoc service
 * @name functionsModule.getQueryString
 *
 * @description
 * <b>getQueryString</b> will convert a given object into a query string.
 * 
 * Below is the code snippet for sample input and sample output:
 * 
 * <pre>
 * var params = {
 *  key1 : 'value1',
 *  key2 : 'value2',
 *  key3 : 'value3'
 *  }
 *  
 *  var output = getQueryString(params);
 *  
 *  // The output is '?&key1=value1&key2=value2&key3=value3' 
 *
 *</pre>
 *
 * @param {Object} params Object containing a list of params.
 * 
 * @returns {String} a query string
 */
.factory('getQueryString', function() {
    return function(params) {

        var queryString = "";
        if (params) {
            for (var param in params) {
                queryString += '&' + param + "=" + params[param];
            }
        }
        return "?" + queryString;
    };
})

/**
 * @ngdoc service
 * @name functionsModule.getURI
 *
 * @description
 * Will return the URI part of a URL
 * @param {String} url the URL the URI of which is to be returned
 */
.factory('getURI', function() {
    return function(url) {
        return url && url.indexOf("?") > -1 ? url.split("?")[0] : url;
    };
})

/**
 * @ngdoc service
 * @name functionsModule.parseQuery
 *
 * @description
 * <b>parseQuery</b> will convert a given query string to an object.
 *
 * Below is the code snippet for sample input and sample output:
 *
 * <pre>
 * var query = '?key1=value1&key2=value2&key3=value3';
 *  
 * var output = parseQuery(query);
 * 
 * // The output is { key1 : 'value1', key2 : 'value2', key3 : 'value3' }
 *
 *</pre>
 *
 * @param {String} query String that needs to be parsed.
 * 
 * @returns {Object} an object containing all params of the given query
 */
.factory('parseQuery', function() {
    return function(str) {

        var objURL = {};

        str.replace(new RegExp("([^?=&]+)(=([^&]*))?", "g"), function($0, $1, $2, $3) {
            objURL[$1] = $3;
        });
        return objURL;
    };
})

/**
 * @ngdoc service
 * @name functionsModule.trim
 *
 * @description
 * <b>trim</b> will remove spaces at the beginning and end of a given string.
 *
 * @param {String} inputString any input string.
 * 
 * @returns {String} the newly modified string without spaces at the beginning and the end
 */
.factory('trim', function() {

    return function(aString) {
        var regExpBeginning = /^\s+/;
        var regExpEnd = /\s+$/;
        return aString.replace(regExpBeginning, "").replace(regExpEnd, "");
    };
})

/**
 * @ngdoc service
 * @name functionsModule.convertToArray
 *
 * @description
 * <b>convertToArray</b> will convert the given object to array.
 * The output array elements are an object that has a key and value,
 * where key is the original key and value is the original object.
 * 
 * @param {Object} inputObject any input object.
 * 
 * @returns {Array} the array created from the input object
 */
.factory('convertToArray', function() {

    return function(object) {
        var configuration = [];
        for (var key in object) {
            if (key.indexOf('$') !== 0 && key.indexOf('toJSON') !== 0) {
                configuration.push({
                    key: key,
                    value: object[key]
                });
            }
        }
        return configuration;
    };

})

/**
 * @ngdoc service
 * @name functionsModule.injectJS
 *
 * @description
 * <b>injectJS</b> will inject script tags into html for a given set of sources.
 *
 */
.factory('injectJS', ['customTimeout', '$log', 'hitch', function(customTimeout, $log, hitch) {

    function getInjector() {
        return $script;
    }

    return {
        getInjector: getInjector,

        /** 
         * @ngdoc method
         * @name functionsModule.injectJS#execute
         * @methodOf functionsModule.injectJS
         * 
         * @description
         * <b>execute</b> will extract a given set of sources from the provided configuration object
         * and then inject each source as a JavaScript source tag and potential callbacks once all the
         * sources are wired.
         * 
         * @param {Object} configuration - a given set of configurations.
         * @param {Array} configuration.sources - an array of sources that needs to be added.
         * @param {Function} configuration.callback - Callback to be triggered once all the sources are wired.
         */
        execute: function(conf) {
            var srcs = conf.srcs;
            var index = conf.index;
            var callback = conf.callback;
            if (index === undefined) {
                index = 0;
            }
            if (srcs[index] !== undefined) {
                this.getInjector()(srcs[index], hitch(this, function() {
                    if (index + 1 < srcs.length) {
                        this.execute({
                            srcs: srcs,
                            index: index + 1,
                            callback: callback
                        });
                    } else if (typeof callback == 'function') {
                        callback();
                    }
                }));

            }
        }
    };
}])

/**
 * @ngdoc service
 * @name functionsModule.uniqueArray
 *
 * @description
 * <b>uniqueArray</b> will return the first Array argument supplemented with new entries from the second Array argument.
 * 
 * @param {Array} array1 any JavaScript array.
 * @param {Array} array2 any JavaScript array.
 */
.factory('uniqueArray', function() {

    return function(array1, array2) {

        array2.forEach(function(instance) {
            if (array1.indexOf(instance) == -1) {
                array1.push(instance);
            }
        });

        return array1;
    };
})

/**
 * @ngdoc service
 * @name functionsModule.regExpFactory
 *
 * @description
 * <b>regExpFactory</b> will convert a given pattern into a regular expression.
 * This method will prepend and append a string with ^ and $ respectively replaces
 * and wildcards (*) by proper regex wildcards.
 * 
 * @param {String} pattern any string that needs to be converted to a regular expression.
 * 
 * @returns {RegExp} a regular expression generated from the given string.
 *
 */
.factory('regExpFactory', function() {

    return function(pattern) {

        var onlyAlphanumericsRegex = new RegExp(/^[a-zA-Z\d]+$/i);
        var antRegex = new RegExp(/^[a-zA-Z\d\*]+$/i);

        if (onlyAlphanumericsRegex.test(pattern)) {
            regexpKey = ['^', '$'].join(pattern);
        } else if (antRegex.test(pattern)) {
            regexpKey = ['^', '$'].join(pattern.replace(/\*/, '.*'));
        } else {
            regexpKey = pattern;
        }

        return new RegExp(regexpKey, 'g');
    };
})

/**
 * @ngdoc service
 * @name functionsModule.generateIdentifier
 *
 * @description
 * <b>generateIdentifier</b> will generate a unique string based on system time and a random generator.
 * 
 * @returns {String} a unique identifier.
 *
 */
.factory('generateIdentifier', function() {
    return function() {
        var d = new Date().getTime();
        if (window.performance && typeof window.performance.now === "function") {
            d += performance.now(); //use high-precision timer if available
        }
        var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
            var r = (d + Math.random() * 16) % 16 | 0;
            d = Math.floor(d / 16);
            return (c == 'x' ? r : (r & 0x3 | 0x8)).toString(16);
        });
        return uuid;
    };
})




/**
 * @ngdoc service
 * @name functionsModule.escapeHtml
 *
 * @description
 * <b>escapeHtml</b> will escape &, <, >, " and ' characters .
 *
 * @param {String} a string that needs to be escaped.
 *
 * @returns {String} the escaped string.
 *
 */
.factory('escapeHtml', function() {
    return function(string) {
        if (typeof string === 'string') {
            return string.replace(/&/g, '&amp;')
                .replace(/>/g, '&gt;')
                .replace(/</g, '&lt;')
                .replace(/"/g, '&quot;')
                .replace(/'/g, '&apos;');
        } else {
            return string;
        }
    };
})


/**
 * @ngdoc service
 * @name functionsModule.sanitizeHTML
 *
 * @description
 * <b>sanitizeHTML</b> will remove breaks and space .
 *
 * @param {String} a string that needs to be escaped.
 *
 * @returns {String} the sanitized HTML.
 *
 */
.factory('sanitizeHTML', function() {
    return function(obj, localized) {
        var result = angular.copy(obj);
        if (localized) {
            angular.forEach(result.value, function(val, key) {
                if (val !== null) {
                    result.value[key] = val.replace(/(\r\n|\n|\r)/gm, '').replace(/>\s+</g, '><').replace(/<\/br\>/g, '');
                }
            });
        } else {
            if (result !== null) {
                result = result.replace(/(\r\n|\n|\r)/gm, '').replace(/>\s+</g, '><').replace(/<\/br\>/g, '');
            }
        }
        return result;
    };
})

/**
 * @ngdoc service
 * @name functionsModule.toPromise
 *
 * @description
 * <b>toPromise</> transforms a function into a function that is guaranteed to return a Promise that resolves to the
 * original return value of the function.
 */
.factory('toPromise', ['$q', function($q) {
    return function(method, context) {
        return function() {
            return $q.when(method.apply(context, arguments));
        };
    };
}])

/**
 * Checks if `value` is a function.
 *
 * @static
 * @memberOf _
 * @category Objects
 * @param {*} value The value to check.
 * @returns {boolean} Returns `true` if the `value` is a function, else `false`.
 * @example
 *
 * _.isFunction(_);
 * // => true
 */
.factory('isFunction', function() {
    return function(value) {
        return typeof value == 'function';
    };
})

// check if the value is the ECMAScript language type of Object
.factory('isObject', function() {
    /** Used to determine if values are of the language type Object */
    var objectTypes = {
        'boolean': false,
        'function': true,
        'object': true,
        'number': false,
        'string': false,
        'undefined': false
    };
    return function(value) {
        return !!(value && objectTypes[typeof value]);
    };
})

/**
 * Creates a function that will delay the execution of `func` until after
 * `wait` milliseconds have elapsed since the last time it was invoked.
 * Provide an options object to indicate that `func` should be invoked on
 * the leading and/or trailing edge of the `wait` timeout. Subsequent calls
 * to the debounced function will return the result of the last `func` call.
 *
 * Note: If `leading` and `trailing` options are `true` `func` will be called
 * on the trailing edge of the timeout only if the the debounced function is
 * invoked more than once during the `wait` timeout.
 *
 * @static
 * @category Functions
 * @param {Function} func The function to debounce.
 * @param {number} wait The number of milliseconds to delay.
 * @param {Object} [options] The options object.
 * @param {boolean} [options.leading=false] Specify execution on the leading edge of the timeout.
 * @param {number} [options.maxWait] The maximum time `func` is allowed to be delayed before it's called.
 * @param {boolean} [options.trailing=true] Specify execution on the trailing edge of the timeout.
 * @returns {Function} Returns the new debounced function.
 * @example
 *
 * // avoid costly calculations while the window size is in flux
 * var lazyLayout = _.debounce(calculateLayout, 150);
 * jQuery(window).on('resize', lazyLayout);
 *
 * // execute `sendMail` when the click event is fired, debouncing subsequent calls
 * jQuery('#postbox').on('click', _.debounce(sendMail, 300, {
 *   'leading': true,
 *   'trailing': false
 * });
 *
 * // ensure `batchLog` is executed once after 1 second of debounced calls
 * var source = new EventSource('/stream');
 * source.addEventListener('message', _.debounce(batchLog, 250, {
 *   'maxWait': 1000
 * }, false);
 */
.factory('debounce', ['isFunction', 'isObject', function(isFunction, isObject) {
    function TypeError() {

    }

    return function(func, wait, options) {
        var args,
            maxTimeoutId,
            result,
            stamp,
            thisArg,
            timeoutId,
            trailingCall,
            leading,
            lastCalled = 0,
            maxWait = false,
            trailing = true,
            isCalled;

        if (!isFunction(func)) {
            throw new TypeError();
        }
        wait = Math.max(0, wait) || 0;
        if (options === true) {
            leading = true;
            trailing = false;
        } else if (isObject(options)) {
            leading = options.leading;
            maxWait = 'maxWait' in options && (Math.max(wait, options.maxWait) || 0);
            trailing = 'trailing' in options ? options.trailing : trailing;
        }
        var delayed = function() {
            var remaining = wait - (Date.now() - stamp);
            if (remaining <= 0) {
                if (maxTimeoutId) {
                    clearTimeout(maxTimeoutId);
                }
                isCalled = trailingCall;
                maxTimeoutId = timeoutId = trailingCall = undefined;
                if (isCalled) {
                    lastCalled = Date.now();
                    result = func.apply(thisArg, args);
                    if (!timeoutId && !maxTimeoutId) {
                        args = thisArg = null;
                    }
                }
            } else {
                timeoutId = setTimeout(delayed, remaining);
            }
        };

        var maxDelayed = function() {
            if (timeoutId) {
                clearTimeout(timeoutId);
            }
            maxTimeoutId = timeoutId = trailingCall = undefined;
            if (trailing || (maxWait !== wait)) {
                lastCalled = Date.now();
                result = func.apply(thisArg, args);
                if (!timeoutId && !maxTimeoutId) {
                    args = thisArg = null;
                }
            }
        };

        return function() {
            args = arguments;
            stamp = Date.now();
            thisArg = this;
            trailingCall = trailing && (timeoutId || !leading);
            var leadingCall, isCalled;

            if (maxWait === false) {
                leadingCall = leading && !timeoutId;
            } else {
                if (!maxTimeoutId && !leading) {
                    lastCalled = stamp;
                }
                var remaining = maxWait - (stamp - lastCalled);
                isCalled = remaining <= 0;

                if (isCalled) {
                    if (maxTimeoutId) {
                        maxTimeoutId = clearTimeout(maxTimeoutId);
                    }
                    lastCalled = stamp;
                    result = func.apply(thisArg, args);
                } else if (!maxTimeoutId) {
                    maxTimeoutId = setTimeout(maxDelayed, remaining);
                }
            }
            if (isCalled && timeoutId) {
                timeoutId = clearTimeout(timeoutId);
            } else if (!timeoutId && wait !== maxWait) {
                timeoutId = setTimeout(delayed, wait);
            }
            if (leadingCall) {
                isCalled = true;
                result = func.apply(thisArg, args);
            }
            if (isCalled && !timeoutId && !maxTimeoutId) {
                args = thisArg = null;
            }
            return result;
        };
    };
}])

/**
 * Creates a function that, when executed, will only call the `func` function
 * at most once per every `wait` milliseconds. Provide an options object to
 * indicate that `func` should be invoked on the leading and/or trailing edge
 * of the `wait` timeout. Subsequent calls to the throttled function will
 * return the result of the last `func` call.
 *
 * Note: If `leading` and `trailing` options are `true` `func` will be called
 * on the trailing edge of the timeout only if the the throttled function is
 * invoked more than once during the `wait` timeout.
 *
 * @static
 * @category Functions
 * @param {Function} func The function to throttle.
 * @param {number} wait The number of milliseconds to throttle executions to.
 * @param {Object} [options] The options object.
 * @param {boolean} [options.leading=true] Specify execution on the leading edge of the timeout.
 * @param {boolean} [options.trailing=true] Specify execution on the trailing edge of the timeout.
 * @returns {Function} Returns the new throttled function.
 * @example
 *
 * // avoid excessively updating the position while scrolling
 * var throttled = _.throttle(updatePosition, 100);
 * jQuery(window).on('scroll', throttled);
 *
 * // execute `renewToken` when the click event is fired, but not more than once every 5 minutes
 * jQuery('.interactive').on('click', _.throttle(renewToken, 300000, {
 *   'trailing': false
 * }));
 */
.factory('throttle', ['debounce', 'isFunction', 'isObject', function(debounce, isFunction, isObject) {
    return function(func, wait, options) {
        var leading = true,
            trailing = true;

        if (!isFunction(func)) {
            throw new TypeError();
        }
        if (options === false) {
            leading = false;
        } else if (isObject(options)) {
            leading = 'leading' in options ? options.leading : leading;
            trailing = 'trailing' in options ? options.trailing : trailing;
        }
        options = {};
        options.leading = leading;
        options.maxWait = wait;
        options.trailing = trailing;

        return debounce(func, wait, options);
    };
}])

/**
 * @ngdoc service
 * @name functionsModule.parseHTML
 *
 * @description
 * parses a string HTML into a queriable DOM object, stripping any JavaScript from the HTML.
 *
 * @param {String} stringHTML, the string representation of the HTML to parse
 */
.factory('parseHTML', function() {
    return function(stringHTML) {
        return $.parseHTML(stringHTML);
    };
})

/**
 * @ngdoc service
 * @name functionsModule.unsafeParseHTML
 *
 * @description
 * parses a string HTML into a queriable DOM object, preserving any JavaScript present in the HTML.
 * Note - as this preserves the JavaScript present it must only be used on HTML strings originating
 * from a known safe location. Failure to do so may result in an XSS vulnerability.
 *
 * @param {String} stringHTML, the string representation of the HTML to parse
 */
.factory('unsafeParseHTML', function() {
    return function(stringHTML) {
        return $.parseHTML(stringHTML, null, true);
    };
})

/**
 * @ngdoc service
 * @name functionsModule.extractFromElement
 *
 * @description
 * parses a string HTML into a queriable DOM object
 *
 * @param {Object} parent, the DOM element from which we want to extract matching selectors
 * @param {String} extractionSelector, the jQuery selector identifying the elements to be extracted
 */
.factory('extractFromElement', function() {
    return function(parent, extractionSelector) {
        parent = $(parent);
        return parent.filter(extractionSelector).add(parent.find(extractionSelector));
    };
});
