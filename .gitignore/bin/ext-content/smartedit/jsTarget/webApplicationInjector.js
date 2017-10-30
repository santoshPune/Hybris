/*!
  * $script.js JS loader & dependency manager
  * https://github.com/ded/script.js
  * (c) Dustin Diaz 2014 | License MIT
  */

(function (name, definition) {
  if (typeof module != 'undefined' && module.exports) module.exports = definition()
  else if (typeof define == 'function' && define.amd) define(definition)
  else this[name] = definition()
})('$script', function () {
  var doc = document
    , head = doc.getElementsByTagName('head')[0]
    , s = 'string'
    , f = false
    , push = 'push'
    , readyState = 'readyState'
    , onreadystatechange = 'onreadystatechange'
    , list = {}
    , ids = {}
    , delay = {}
    , scripts = {}
    , scriptpath
    , urlArgs

  function every(ar, fn) {
    for (var i = 0, j = ar.length; i < j; ++i) if (!fn(ar[i])) return f
    return 1
  }
  function each(ar, fn) {
    every(ar, function (el) {
      return !fn(el)
    })
  }

  function $script(paths, idOrDone, optDone) {
    paths = paths[push] ? paths : [paths]
    var idOrDoneIsDone = idOrDone && idOrDone.call
      , done = idOrDoneIsDone ? idOrDone : optDone
      , id = idOrDoneIsDone ? paths.join('') : idOrDone
      , queue = paths.length
    function loopFn(item) {
      return item.call ? item() : list[item]
    }
    function callback() {
      if (!--queue) {
        list[id] = 1
        done && done()
        for (var dset in delay) {
          every(dset.split('|'), loopFn) && !each(delay[dset], loopFn) && (delay[dset] = [])
        }
      }
    }
    setTimeout(function () {
      each(paths, function loading(path, force) {
        if (path === null) return callback()
        path = !force && path.indexOf('.js') === -1 && !/^https?:\/\//.test(path) && scriptpath ? scriptpath + path + '.js' : path
        if (scripts[path]) {
          if (id) ids[id] = 1
          return (scripts[path] == 2) ? callback() : setTimeout(function () { loading(path, true) }, 0)
        }

        scripts[path] = 1
        if (id) ids[id] = 1
        create(path, callback)
      })
    }, 0)
    return $script
  }

  function create(path, fn) {
    var el = doc.createElement('script'), loaded
    el.onload = el.onerror = el[onreadystatechange] = function () {
      if ((el[readyState] && !(/^c|loade/.test(el[readyState]))) || loaded) return;
      el.onload = el[onreadystatechange] = null
      loaded = 1
      scripts[path] = 2
      fn()
    }
    el.async = 1
    el.src = urlArgs ? path + (path.indexOf('?') === -1 ? '?' : '&') + urlArgs : path;
    head.insertBefore(el, head.lastChild)
  }

  $script.get = create

  $script.order = function (scripts, id, done) {
    (function callback(s) {
      s = scripts.shift()
      !scripts.length ? $script(s, id, done) : $script(s, callback)
    }())
  }

  $script.path = function (p) {
    scriptpath = p
  }
  $script.urlArgs = function (str) {
    urlArgs = str;
  }
  $script.ready = function (deps, ready, req) {
    deps = deps[push] ? deps : [deps]
    var missing = [];
    !each(deps, function (dep) {
      list[dep] || missing[push](dep);
    }) && every(deps, function (dep) {return list[dep]}) ?
      ready() : !function (key) {
      delay[key] = delay[key] || []
      delay[key][push](ready)
      req && req(missing)
    }(deps.join('|'))
    return $script
  }

  $script.done = function (idOrDone) {
    $script([null], idOrDone)
  }

  return $script
});

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
parent.postMessage({
    pk: Math.random(),
    gatewayId: 'smartEditBootstrap',
    eventId: 'loading',
    data: {
        location: document.location.href
    }
}, '*');

(function heartBeat() {
    var HEART_BEAT_PERIOD = 500; // 0.5 seconds
    var HEART_BEAT_GATEWAY_ID = "heartBeatGateway";
    var HEART_BEAT_MSG_ID = 'heartBeat';

    parent.postMessage({
        pk: Math.random(),
        gatewayId: HEART_BEAT_GATEWAY_ID,
        eventId: HEART_BEAT_MSG_ID,
        data: {
            location: document.location.href
        }
    }, '*');
    setTimeout(heartBeat, HEART_BEAT_PERIOD);
})();


$(document).ready(function() {

    parent.postMessage({
        pk: Math.random(),
        gatewayId: 'smartEditBootstrap',
        eventId: 'bootstrapSmartEdit',
        data: {
            location: document.location.href
        }
    }, '*');
});
var injectJS = function(head, srcs, index) {
    if (index < srcs.length) {
        $script(srcs[index], function() {
            injectJS(head, srcs, index + 1);
        });
    }
};

var injectCSS = function(head, cssPaths, index) {

    var link = document.createElement('link');
    link.rel = 'stylesheet';
    link.href = cssPaths[index];
    head.appendChild(link);

    if (index + 1 < cssPaths.length) {
        injectCSS(head, cssPaths, index + 1);
    }
};

// Create IE + others compatible event handler
var eventMethod = window.addEventListener ? "addEventListener" : "attachEvent";
var eventer = window[eventMethod];
var messageEvent = eventMethod == "attachEvent" ? "onmessage" : "message";

// Listen to message from child window
eventer(messageEvent, function(e) {

    //	var originControl = '127.0.0.1:7000';
    //
    //	if(e.origin.indexOf(originControl)==-1){
    //		throw e.origin+" is not allowed to override this storefront";
    //	}
    var event = e.data;
    if (event.eventName == 'smarteditBootstrap') {

        window.smartedit = window.smartedit || {};
        if (event.resources && event.resources.properties) {
            for (var i in event.resources.properties) {
                window.smartedit[i] = event.resources.properties[i];
            }
        }

        var head = document.getElementsByTagName("head")[0];

        //JS Files
        if (event.resources && event.resources.js && event.resources.js.length > 0) {
            injectJS(head, event.resources.js, 0);
        }

        //CSS Files
        if (event.resources && event.resources.css && event.resources.css.length > 0) {
            injectCSS(head, event.resources.css, 0);
        }
    }

}, false);
