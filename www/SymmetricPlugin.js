/**
 * @author huy.thai
 * huyttq@gmail.com
 */

var exec = require("cordova/exec");

var SymmetricPlugin = function() {};

SymmetricPlugin.prototype.startEngine = function(params, success, fail) {
  return exec(success, fail, 'SymmetricPlugin', 'startEngine', [params]);
};

SymmetricPlugin.prototype.stopEngine = function(params, success, fail) {
  return exec(success, fail, 'SymmetricPlugin', 'stopEngine', [params]);
};

module.exports = new SymmetricPlugin();