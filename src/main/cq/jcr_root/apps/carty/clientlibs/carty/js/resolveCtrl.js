/*global angular: false */

angular.module('cartyApp')
  .controller('ResolveCtrl', function($scope, $http, $rootScope, settings, localStorageService) {

    $scope.form = {};
    $scope.form.path = localStorageService.get('carty-path');

    function setMatchingPaths(data) {
      $rootScope.matchingPaths = _(data.mappings).map(function(v) {
        return v.mapping.path;
      });
    }

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
        setMatchingPaths(data);
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
        setMatchingPaths(data);
      });
    };

    $scope.highlightMapping = function(path) {
      $rootScope.$emit('highlightMapping', path);
    };

    $scope.clearMappingHighlight = function() {
      $rootScope.$emit('clearMappingHighlight');
    };
});