/*global angular: false */

angular.module('cartyApp').controller('TreeCtrl', function($scope, $http, $timeout, $rootScope, settings) {

    var properties = ['match', 'internalRedirect', 'redirect', 'status'];

    function transform(data, parent, name) {
        var m = {};
        _.each(data, function(v, k) {
            if (k.substr(0, 6) === 'sling:') {
                m[k.substr(6)] = v;
            }
        });
        m.name = name;
        m.parent = parent;
        m.path = parent.path + '/' + name;
        m.isMapping = data['jcr:primaryType'] === 'sling:Mapping';
        m.items = [];

        _.each(data, function(v, k) {
            if (_.isObject(v) && !_.isArray(v)) {
                m.items.push(transform(v, m, k));
            }
        });

        return m;
    }

    function loadMappings() {
      var root = settings.mappingsRoot;

      $http.get(root + ".-1.json").success(function(data) {
        var i = root.lastIndexOf('/'),
            parent = root.substring(0, i),
            name = root.substring(i + 1);
        $scope.mappings = transform(data, {'path' : parent}, name).items;
        $scope.errorMessage = null;
      }).error(function() {
        $scope.mappings = [];
        $scope.errorMessage = "Can't load mappings from " + root;
      });
    }

    function slingPost(path, data) {
      return $http({
        method: 'POST',
        url: path,
        data: data,
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        transformRequest: function(data) {
          var str = [], p, q;
          _(data).each(function(v, k) {
            if (typeof v === 'object') {
              str.push(k + '@TypeHint=' + 'String[]');
              _(v).each(function(v1, k1) {
                str.push(k + "=" + encodeURIComponent(v1));
              });
            } else {
              str.push(k + "=" + encodeURIComponent(v));
            }
          });
          return str.join('&');
        }
      });
    }

    function reorder(path, newIndex) {
        var data = {};
        data[':order'] = newIndex;
        return slingPost(path, data);
      }

    function move(from, newParent) {
      var data = {};
      data[from.name + '@MoveFrom'] = from.path;
      return slingPost(newParent.path, data);
    }

    function swap(array, i, j) {
        var tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    $scope.saveMapping = function(mapping) {
      var data = {};
      _(properties).each(function(p) {
        if (mapping[p]) {
          data['sling:' + p] = mapping[p];
        } else {
          data['sling:' + p + '@Delete'] = 'true';
        }
      });
      slingPost(mapping.path, data).success(loadMappings);
    };

    $scope.newSubItem = function(mapping) {
        slingPost(mapping.path + '/*', {
            ':nameHint' : 'new mapping',
            'jcr:primaryType' : 'sling:Mapping'
        }).success(loadMappings);
    };

    $scope.removeMapping = function(mapping) {
      slingPost(mapping.path, {':operation' : 'delete'}).success(loadMappings);
    };

    $scope.expanded = {};

    $scope.toggleMapping = function(path) {
      $scope.expanded[path] = !$scope.expanded[path];
    };

    $scope.checkNewName = function(mapping, newName) {
        if (_(mapping.parent.items).find(function(v) {
            if (v.name === newName && mapping !== v) {
                return true;
            }
        })) {
            return 'Node with this name already exists';
        }
    };

    $scope.rename = function(mapping) {
      var index = mapping.parent.items.indexOf(mapping);
      move(mapping, mapping.parent).success(function() {
        var newPath = mapping.parent.path + '/' + mapping.name;
        $scope.expanded[newPath] = $scope.expanded[mapping.path];
        reorder(newPath, index)['finally'](loadMappings);
      });
    };

    $scope.redirectUp = function(mapping, i, form) {
        swap(mapping.internalRedirect, i, i-1);
        $scope.saveMapping(mapping);
    };

    $scope.redirectDown = function(mapping, i, form) {
        swap(mapping.internalRedirect, i, i+1);
        $scope.saveMapping(mapping);
    };

    $scope.deleteRedirect = function(mapping, i) {
        mapping.internalRedirect.splice(i, 1);
        if (_.isEmpty(mapping.internalRedirect)) {
          mapping.internalRedirect.push("");
        }
        $scope.saveMapping(mapping);
    };

    $scope.addRedirect = function(mapping, redirect) {
        mapping.internalRedirect = mapping.internalRedirect || [];
        mapping.internalRedirect.push("/");
        $scope.saveMapping(mapping);
    };

    $scope.dragEnabled = true;

    $scope.disableDrag = function() {
      $scope.dragEnabled = false;
    };

    $scope.enableDrag = function() {
      $scope.dragEnabled = true;
    };

    $scope.treeCallbacks = {
      dropped : function(event) {
          var source = event.source,
              dest = event.dest,
              mapping = source.nodeScope.$modelValue,
              parent = dest.nodesScope.$parent.$modelValue,
              newIndex = dest.index;
          if (parent === mapping.parent) {
            reorder(parent.path + '/' + mapping.name, newIndex)['finally'](loadMappings);
          } else {
            move(mapping, parent).success(function() {
              var newPath = parent.path + '/' + mapping.name;
              $scope.expanded[newPath] = $scope.expanded[mapping.path];
              reorder(newPath, newIndex)['finally'](loadMappings);
            });
          }
      }
    };

    $rootScope.$on('reloadTree', function() {
      loadMappings();
    });

    $rootScope.$on('highlightMapping', function(event, path) {
      $scope.highlighted = path;
    });

    $rootScope.$on('clearMappingHighlight', function() {
      $scope.highlighted = null;
    });

    $scope.isMatchingPath = function(path) {
      return $rootScope.matchingPaths.indexOf(path) > -1;
    };

    loadMappings();
});