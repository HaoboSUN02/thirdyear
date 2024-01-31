// CodeMirror, copyright (c) by Marijn Haverbeke and others
// Distributed under an MIT license: http://codemirror.net/LICENSE

(function(mod) {
  if (typeof exports == "object" && typeof module == "object") // CommonJS
    mod(require("../../lib/codemirror"));
  else if (typeof define == "function" && define.amd) // AMD
    define(["../../lib/codemirror"], mod);
  else // Plain browser env
    mod(CodeMirror);
})(function(CodeMirror) {
"use strict";

CodeMirror.defineMode("apl", function() {
  var builtInOps = {
    ".": "innerProduct",
    "\\": "scan",
    "/": "reduce",
    "‚å?": "reduce1Axis",
    "‚ç?": "scan1Axis",
    "¬®": "each",
    "‚ç?": "power"
  };
  var builtInFuncs = {
    "+": ["conjugate", "add"],
    "‚à?": ["negate", "subtract"],
    "√ó": ["signOf", "multiply"],
    "√∑": ["reciprocal", "divide"],
    "‚å?": ["ceiling", "greaterOf"],
    "‚å?": ["floor", "lesserOf"],
    "‚à?": ["absolute", "residue"],
    "‚ç?": ["indexGenerate", "indexOf"],
    "?": ["roll", "deal"],
    "‚ã?": ["exponentiate", "toThePowerOf"],
    "‚ç?": ["naturalLog", "logToTheBase"],
    "‚ó?": ["piTimes", "circularFuncs"],
    "!": ["factorial", "binomial"],
    "‚å?": ["matrixInverse", "matrixDivide"],
    "<": [null, "lessThan"],
    "‚â?": [null, "lessThanOrEqual"],
    "=": [null, "equals"],
    ">": [null, "greaterThan"],
    "‚â?": [null, "greaterThanOrEqual"],
    "‚â?": [null, "notEqual"],
    "‚â?": ["depth", "match"],
    "‚â?": [null, "notMatch"],
    "‚à?": ["enlist", "membership"],
    "‚ç?": [null, "find"],
    "‚à?": ["unique", "union"],
    "‚à?": [null, "intersection"],
    "‚à?": ["not", "without"],
    "‚à?": [null, "or"],
    "‚à?": [null, "and"],
    "‚ç?": [null, "nor"],
    "‚ç?": [null, "nand"],
    "‚ç?": ["shapeOf", "reshape"],
    ",": ["ravel", "catenate"],
    "‚ç?": [null, "firstAxisCatenate"],
    "‚å?": ["reverse", "rotate"],
    "‚ä?": ["axis1Reverse", "axis1Rotate"],
    "‚ç?": ["transpose", null],
    "‚Ü?": ["first", "take"],
    "‚Ü?": [null, "drop"],
    "‚ä?": ["enclose", "partitionWithAxis"],
    "‚ä?": ["diclose", "pick"],
    "‚å?": [null, "index"],
    "‚ç?": ["gradeUp", null],
    "‚ç?": ["gradeDown", null],
    "‚ä?": ["encode", null],
    "‚ä?": ["decode", null],
    "‚ç?": ["format", "formatByExample"],
    "‚ç?": ["execute", null],
    "‚ä?": ["stop", "left"],
    "‚ä?": ["pass", "right"]
  };

  var isOperator = /[\.\/‚åø‚çÄ¬®‚ç£]/;
  var isNiladic = /‚ç?/;
  var isFunction = /[\+‚àí√ó√∑‚åà‚åä‚à£‚ç≥\?‚ãÜ‚çü‚ó?!‚å?<‚â?=>‚â•‚â†‚â°‚â¢‚àà‚ç∑‚à™‚à©‚àº‚à®‚àß‚ç±‚ç≤‚ç¥,‚ç™‚åΩ‚äñ‚çâ‚Üë‚Üì‚äÇ‚äÉ‚å∑‚çã‚çí‚ä§‚ä•‚çï‚çé‚ä£‚ä¢]/;
  var isArrow = /‚Ü?/;
  var isComment = /[‚ç?#].*$/;

  var stringEater = function(type) {
    var prev;
    prev = false;
    return function(c) {
      prev = c;
      if (c === type) {
        return prev === "\\";
      }
      return true;
    };
  };
  return {
    startState: function() {
      return {
        prev: false,
        func: false,
        op: false,
        string: false,
        escape: false
      };
    },
    token: function(stream, state) {
      var ch, funcName, word;
      if (stream.eatSpace()) {
        return null;
      }
      ch = stream.next();
      if (ch === '"' || ch === "'") {
        stream.eatWhile(stringEater(ch));
        stream.next();
        state.prev = true;
        return "string";
      }
      if (/[\[{\(]/.test(ch)) {
        state.prev = false;
        return null;
      }
      if (/[\]}\)]/.test(ch)) {
        state.prev = true;
        return null;
      }
      if (isNiladic.test(ch)) {
        state.prev = false;
        return "niladic";
      }
      if (/[¬Ø\d]/.test(ch)) {
        if (state.func) {
          state.func = false;
          state.prev = false;
        } else {
          state.prev = true;
        }
        stream.eatWhile(/[\w\.]/);
        return "number";
      }
      if (isOperator.test(ch)) {
        return "operator apl-" + builtInOps[ch];
      }
      if (isArrow.test(ch)) {
        return "apl-arrow";
      }
      if (isFunction.test(ch)) {
        funcName = "apl-";
        if (builtInFuncs[ch] != null) {
          if (state.prev) {
            funcName += builtInFuncs[ch][1];
          } else {
            funcName += builtInFuncs[ch][0];
          }
        }
        state.func = true;
        state.prev = false;
        return "function " + funcName;
      }
      if (isComment.test(ch)) {
        stream.skipToEnd();
        return "comment";
      }
      if (ch === "‚à?" && stream.peek() === ".") {
        stream.next();
        return "function jot-dot";
      }
      stream.eatWhile(/[\w\$_]/);
      word = stream.current();
      state.prev = true;
      return "keyword";
    }
  };
});

CodeMirror.defineMIME("text/apl", "apl");

});
