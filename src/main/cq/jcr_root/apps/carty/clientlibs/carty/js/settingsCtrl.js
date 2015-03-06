/*global angular: false */

angular.module('cartyApp')
  .controller('SettingsCtrl', function($scope, $rootScope, localStorageService) {

    $scope.settingsForm = $rootScope.settings;

    $scope.changeSettings = function() {
      var settings = _.clone($scope.settingsForm);
      $rootScope.settings = settings;
      localStorageService.set('carty-settings', settings);
      $rootScope.$emit('reloadTree');
    };
});