/*global angular: false */

angular.module('cartyApp')
  .controller('ResolveCtrl', ['$scope', '$http', '$rootScope', 'settings', 'localStorageService',
                              function($scope, $http, $rootScope, settings, localStorageService) {

    $scope.form = localStorageService.get('carty-form') || {};

    function setMatchingPaths(data) {
      $rootScope.matchingPaths = _(data.mappings).map(function(v) {
        return v.mapping.path;
      });
    }

    $scope.resolve = function() {
      $scope.resolveResult = null;
      $scope.mapResult = null;
      localStorageService.set('carty-form', $scope.form);

      $http.get(settings.apiPath + '.resolve.json', {
        params: {
          'url': $scope.form.path,
          'mappingsRoot': settings.mappingsRoot
        }
      }).success(function(data) {
        $scope.resolveResult = data;
        setMatchingPaths(data);
      }).error($rootScope.httpError);
    };

    $scope.map = function() {
      $scope.resolveResult = null;
      $scope.mapResult = null;
      localStorageService.set('carty-form', $scope.form);

      $http.get(settings.apiPath + '.map.json', {
        params: {
          'path': $scope.form.path,
          'host': $scope.form.host,
          'mappingsRoot': settings.mappingsRoot
        }
      }).success(function(data) {
        $scope.mapResult = data;
        setMatchingPaths(data);
      }).error($rootScope.httpError);
    };

    $scope.highlightMapping = function(path) {
      $rootScope.$emit('highlightMapping', path);
    };

    $scope.clearMappingHighlight = function() {
      $rootScope.$emit('clearMappingHighlight');
    };
}]);