/*global angular: false */

angular.module('cartyApp')
  .controller('TemplateCtrl', function($scope, $http, $rootScope, settings) {
    $scope.createFromTemplate = function() {
      $http.post(settings.apiPath + ".createMappings.json", {
        data: $scope.form,
        mappingsRoot: settings.mappingsRoot,
        template: 'default'
      }).success(function(data) {
        $rootScope.$emit('reloadTree');
      });
    };
});