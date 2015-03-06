/*global angular: false */

angular.module('cartyApp')
  .controller('ResolveCtrl', function($scope, $http, settings, localStorageService) {

    $scope.form = {};
    $scope.form.path = localStorageService.get('carty-path');

    $scope.resolve = function() {
      $scope.resolveResult = null;
      $scope.mapResult = null;
      localStorageService.set('carty-path', $scope.form.path);

      $http.get(settings.apiPath + '.resolve.json', {
        params: {
          'url': $scope.form.path,
          'mappingsRoot': settings.mappingsRoot
        }
      }).success(function(data) {
        $scope.resolveResult = data;
      });
    };

    $scope.map = function() {
      $scope.resolveResult = null;
      $scope.mapResult = null;
      localStorageService.set('carty-path', $scope.form.path);

      $http.get(settings.apiPath + '.map.json', {
        params: {
          'path': $scope.form.path,
          'mappingsRoot': settings.mappingsRoot
        }
      }).success(function(data) {
        $scope.mapResult = data;
      });
    };
});