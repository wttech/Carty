<div ng-controller="TreeCtrl">
  <script type="text/ng-template" id="items_renderer.html">
<div ui-tree-handle class="mapping-tree" ng-if="!item.isMapping" ng-class="{highlighted: highlighted == item.path}">
  <div class="u-coral-pullRight">
    <button class="coral-Button coral-Button--square coral-Button--quiet" data-nodrag ng-click="newSubItem(item)">
      <i data-nodrag class="coral-Icon coral-Icon--add"></i>
    </button>
    <button class="coral-Button coral-Button--square coral-Button--quiet" data-nodrag ng-click="test();removeMapping(item)">
      <i data-nodrag class="coral-Icon coral-Icon--delete"></i>
    </button>
  </div>

  <div class="mapping-entry">
    <p>
      <span editable-text="item.name" e-form="nameForm" buttons="no" onbeforesave="checkNewName(item, $data)" onaftersave="rename(item)" onshow="disableDrag()" onhide="enableDrag()" blur="submit">
        {{item.name}}
      </span>
      <button class="coral-Button coral-Button--square coral-Button--quiet" data-nodrag ng-click="nameForm.$show()" ng-show="!nameForm.$visible">
        <i data-nodrag class="coral-Icon coral-Icon--edit"></i>
      </button>
      <button class="coral-Button coral-Button--square coral-Button--quiet" data-nodrag ng-click="nameForm.$submit()" ng-show="nameForm.$visible">
        <i data-nodrag class="coral-Icon coral-Icon--check"></i>
      </button>
      <button class="coral-Button coral-Button--square coral-Button--quiet" data-nodrag ng-click="nameForm.$cancel()" ng-show="nameForm.$visible">
        <i data-nodrag class="coral-Icon coral-Icon--close"></i>
      </button>
    </p>
  </div>
</div>

<div ui-tree-handle class="mapping-tree" ng-if="item.isMapping" ng-class="{highlighted: highlighted == item.path}">
  <button class="coral-Button coral-Button--square coral-Button--quiet tree-expand-collapse" data-nodrag ng-click="toggleMapping(item.path)">
    <i data-nodrag class="coral-Icon" ng-class="{'coral-Icon--treeExpand': !expanded[item.path], 'coral-Icon--treeCollapse': expanded[item.path]}"></i>
  </button>

  <div data-nodrag class="u-coral-pullRight">
    <button class="coral-Button coral-Button--square coral-Button--quiet" data-nodrag ng-click="newSubItem(item)">
      <i data-nodrag class="coral-Icon coral-Icon--add"></i>
    </button>
    <button class="coral-Button coral-Button--square coral-Button--quiet" data-nodrag ng-click="test();removeMapping(item)">
      <i data-nodrag class="coral-Icon coral-Icon--delete"></i>
    </button>
  </div>

  <div class="mapping-entry">
    <p>
      <span class="mapping-name" ng-class="{short: !expanded[item.path]}">
        <span editable-text="item.name" e-form="nameForm" buttons="no" onbeforesave="checkNewName(item, $data)" onaftersave="rename(item)" onshow="disableDrag()" onhide="enableDrag()" blur="submit">
          {{item.name}}
        </span>
        <button class="coral-Button coral-Button--square coral-Button--quiet" data-nodrag ng-click="nameForm.$show()" ng-show="!nameForm.$visible && expanded[item.path]">
          <i data-nodrag class="coral-Icon coral-Icon--edit"></i>
        </button>
      </span>

      <span class="short-mapping-definition" ng-show="!expanded[item.path]">
          <span ng-if="item.match">{{item.match}}</span>
          <i ng-if="!item.match">{{item.name}}</i>
          &rarr; [{{item.internalRedirect.join(', ')}}]
      </span>

      <button class="coral-Button coral-Button--square coral-Button--quiet" data-nodrag ng-click="nameForm.$submit()" ng-show="nameForm.$visible">
        <i data-nodrag class="coral-Icon coral-Icon--check"></i>
      </button>
      <button class="coral-Button coral-Button--square coral-Button--quiet" data-nodrag ng-click="nameForm.$cancel()" ng-show="nameForm.$visible">
        <i data-nodrag class="coral-Icon coral-Icon--close"></i>
      </button>
    </p>

    <div class="mapping-definition" ng-show="expanded[item.path]">
      <dl>
        <dt>match</dt>
        <dd>
          <span editable-text="item.match" e-form="matchForm" buttons="no" onaftersave="saveMapping(item)" onshow="disableDrag()" onhide="enableDrag()"
            blur="submit"> {{item.match}} </span>
          <button class="coral-Button coral-Button--square coral-Button--quiet" data-nodrag ng-click="matchForm.$show()" ng-show="!matchForm.$visible">
            <i data-nodrag class="coral-Icon coral-Icon--edit"></i>
          </button>
          <button class="coral-Button coral-Button--square coral-Button--quiet" data-nodrag ng-click="matchForm.$submit()" ng-show="matchForm.$visible">
            <i data-nodrag class="coral-Icon coral-Icon--check"></i>
          </button>
          <button class="coral-Button coral-Button--square coral-Button--quiet" data-nodrag ng-click="matchForm.$cancel()" ng-show="matchForm.$visible">
            <i data-nodrag class="coral-Icon coral-Icon--close"></i>
          </button>
        </dd>
        <dt>internalRedirect</dt>
        <dd ng-repeat="(i, r) in item.internalRedirect track by i">
          <span editable-text="item.internalRedirect[i]" e-form="internalRedirectForm" buttons="no" onaftersave="saveMapping(item)" onshow="disableDrag()"
            onhide="enableDrag()" blur="submit"> {{r}} </span>
          <button class="coral-Button coral-Button--square coral-Button--quiet" data-nodrag ng-click="internalRedirectForm.$show()" ng-show="!internalRedirectForm.$visible">
            <i data-nodrag class="coral-Icon coral-Icon--edit"></i>
          </button>
          <button class="coral-Button coral-Button--square coral-Button--quiet" data-nodrag ng-click="internalRedirectForm.$submit()" ng-show="internalRedirectForm.$visible">
            <i data-nodrag class="coral-Icon coral-Icon--check"></i>
          </button>
          <button class="coral-Button coral-Button--square coral-Button--quiet" data-nodrag ng-click="internalRedirectForm.$cancel()" ng-show="internalRedirectForm.$visible">
            <i data-nodrag class="coral-Icon coral-Icon--close"></i>
          </button>

          <button class="coral-Button coral-Button--square coral-Button--quiet" data-nodrag ng-click="redirectUp(item, i)" ng-show="!internalRedirectForm.$visible && !$first">
            <i data-nodrag class="coral-Icon coral-Icon--arrowup"></i>
          </button>
          <button class="coral-Button coral-Button--square coral-Button--quiet" data-nodrag ng-click="redirectDown(item, i)" ng-show="!internalRedirectForm.$visible && !$last">
            <i data-nodrag class="coral-Icon coral-Icon--arrowdown"></i>
          </button>
          <button class="coral-Button coral-Button--square coral-Button--quiet" data-nodrag ng-click="test();deleteRedirect(item, i)" ng-show="!internalRedirectForm.$visible">
            <i data-nodrag class="coral-Icon coral-Icon--delete"></i>
          </button>
        </dd>
        <dd>
          <button class="coral-Button coral-Button--square coral-Button--quiet" class="button" data-nodrag ng-click="addRedirect(item)">
            <i data-nodrag class="coral-Icon coral-Icon--add"></i>
          </button>
        </dd>
      </dl>
      <dl ng-if="item.isMapping">
        <dt>redirect</dt>
        <dd>
          <span editable-text="item.redirect" e-form="redirectForm" buttons="no" onaftersave="saveMapping(item)" onshow="disableDrag()" onhide="enableDrag()"
            blur="submit"> {{item.redirect}} </span>
          <button class="coral-Button coral-Button--square coral-Button--quiet" data-nodrag ng-click="redirectForm.$show()" ng-show="!redirectForm.$visible">
            <i data-nodrag class="coral-Icon coral-Icon--edit"></i>
          </button>
          <button class="coral-Button coral-Button--square coral-Button--quiet" data-nodrag ng-click="redirectForm.$submit()" ng-show="redirectForm.$visible">
            <i data-nodrag class="coral-Icon coral-Icon--check"></i>
          </button>
          <button class="coral-Button coral-Button--square coral-Button--quiet" data-nodrag ng-click="redirectForm.$cancel()" ng-show="redirectForm.$visible">
            <i data-nodrag class="coral-Icon coral-Icon--close"></i>
          </button>
        </dd>
        <dt>status</dt>
        <dd>
          <span editable-text="item.status" e-form="statusForm" buttons="no" onaftersave="saveMapping(item)" onshow="disableDrag()" onhide="enableDrag()"
            blur="submit"> {{item.status}} </span>
          <button class="coral-Button coral-Button--square coral-Button--quiet" data-nodrag ng-click="statusForm.$show()" ng-show="!statusForm.$visible">
            <i data-nodrag class="coral-Icon coral-Icon--edit"></i>
          </button>
          <button class="coral-Button coral-Button--square coral-Button--quiet" data-nodrag ng-click="statusForm.$submit()" ng-show="statusForm.$visible">
            <i data-nodrag class="coral-Icon coral-Icon--check"></i>
          </button>
          <button class="coral-Button coral-Button--square coral-Button--quiet" data-nodrag ng-click="statusForm.$cancel()" ng-show="statusForm.$visible">
            <i data-nodrag class="coral-Icon coral-Icon--close"></i>
          </button>
        </dd>
      </dl>
    </div>
  </div>
</div>
<ul ui-tree-nodes="options" ng-model="item.items">
  <li ng-repeat="item in item.items" ui-tree-node ng-include="'items_renderer.html'" ng-show="!item.isMapping || !showOnlyMatching || isMatchingPath(item.path)"></li>
</ul>
  </script>

  <div ui-tree="options" callbacks="treeCallbacks">
    <ul ui-tree-nodes ng-model="mappings">
      <li ng-repeat="item in mappings" ui-tree-node ng-include="'items_renderer.html'" ng-show="!item.isMapping || !showOnlyMatching || isMatchingPath(item.path)"></li>
    </ul>
  </div>


  <coral-checkbox ng-show="matchingPaths">
    <input type="checkbox" class=" coral-Checkbox-input" ng-model="showOnlyMatching" id="showOnlyMatching"/>
    <span class="coral-Checkbox-checkmark" handle="checkbox"></span>
    <label class=" coral-Checkbox-description" handle="labelWrapper" for="showOnlyMatching">
      <coral-checkbox-label>Show only matching</coral-checkbox-label></label>
  </coral-checkbox>
</div>