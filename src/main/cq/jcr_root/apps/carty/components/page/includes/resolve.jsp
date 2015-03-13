<div ng-controller="ResolveCtrl" class="resolve">
  <form novalidate>
    <div class="form-row">
      <h4>Path</h4>
      <input type="text" ng-model="form.path" />
    </div>
    <div class="form-row">
      <div class="form-left-cell">&nbsp;</div>
      <button type="button" class="primary" ng-click="resolve()">Resolve</button>
      <button type="button" class="warning" ng-click="map()">Map</button>
    </div>
  </form>

  <div class="resolveResult" ng-show="resolveResult">
    <p>Resource info</p>

    <table class="data">
      <tr>
        <td>Path</td>
        <td>{{resolveResult.resourcePath}}</td>
      </tr>

      <tr>
        <td>Resource type</td>
        <td>{{resolveResult.resourceType}}</td>
      </tr>

      <tr ng-show="resolveResult.resourceSuperType">
        <td>Resource super type</td>
        <td>{{resolveResult.resourceSuperType}}</td>
      </tr>

      <tr>
        <td>Class</td>
        <td>{{resolveResult.class}}</td>
      </tr>
    </table>

    <div class="alert notice" ng-show="resolveResult.mappings.length == 0">
      There are no matching mappings
    </div>

    <div ng-show="resolveResult.mappings.length > 0">
      <p>Matching mappings</p>

      <table class="data">
        <tr>
          <th>name</th>
          <th>match</th>
          <th>path</th>
        </tr>

        <tr ng-repeat="m in resolveResult.mappings" ng-mouseenter="highlightMapping(m.mapping.path)" ng-mouseleave="clearMappingHighlight()">
          <td>{{m.mapping.name}}</td>
          <td>
          <span ng-show="m.mapping.match">{{m.mapping.match}}</span>
          <span ng-show="!m.mapping.match"><i>{{m.mapping.name}}</i></span>
          </td>
          <td>
            <span class="path-breakdown">
              {{m.path[0]}}<span class="selected">{{m.path[1]}}</span>{{m.path[2]}}
            </span>
          </td>
        </tr>
      </table>
    </div>
  </div>

  <div class="resolveResult" ng-show="mapResult">
    <p>Results</p>

    <table class="data">
      <tr>
        <td>Mapped URL</td>
        <td>{{mapResult.url}}</td>
      </tr>
    </table>

    <div class="alert notice" ng-show="mapResult.mappings.length == 0">
      There are no matching mappings
    </div>

    <div ng-show="mapResult.mappings.length > 0">
      <p>Mapping breakdown by their accuracy (the most accurate is the top one)</p>

      <table class="data">
        <tr>
          <th>mapping name</th>
          <th>matching path part</th>
          <th>mapped url</th>
        </tr>

        <tr ng-repeat="m in mapResult.mappings" ng-mouseenter="highlightMapping(m.mapping.path)" ng-mouseleave="clearMappingHighlight()">
          <td>{{m.mapping.name}}</td>
          <td>
            <span class="path-breakdown">
              <span class="selected">{{m.matchingPath[0]}}</span>{{m.matchingPath[1]}}
            </span>
          </td>
          <td>{{m.url}}</td>
        </tr>
      </table>
    </div>
  </div>
</div>