/*global angular: false */

angular.module('cartyApp', ['xeditable', 'ui.tree', 'LocalStorageModule'])
.factory('settings', function($rootScope) {
  return $rootScope.settings;
})
.run(function(editableOptions, $rootScope, $window, localStorageService) {
  editableOptions.theme = 'default';

  $rootScope.settings = _.extend({
    mappingsRoot: '/etc/map'
  }, $window.globalSettings || {}, localStorageService.get('carty-settings'));
});