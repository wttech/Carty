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

  $rootScope.showFlash = function(title, message, type) {
    $rootScope.flash = { title: title, msg: message, class: type || 'notice' };
    $('.content-container').animate({scrollTop: 0});
  };
  
  $rootScope.httpError = function(err, status) {
    if (err.title) {
      $rootScope.showFlash(err.title, err['status.message']);
    } else {
      var $err = $('<div>').append(err);
      $rootScope.showFlash($err.find('title').text(), $err.find('p').text());
    }
  }
});