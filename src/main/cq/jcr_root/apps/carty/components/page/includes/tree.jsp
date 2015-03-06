<div ng-controller="TreeCtrl">
  <script type="text/ng-template" id="items_renderer.html">
<div ui-tree-handle class="mapping-tree">
  <button class="left button" data-nodrag ng-click="toggle(this)" ng-class="{'icon-treeexpand': collapsed, 'icon-treecollapse': !collapsed}" />
  <div class="right">
    <button class="button icon-add" data-nodrag ng-click="newSubItem(item)" />
    <button class="button icon-delete" data-nodrag ng-click="removeMapping(item)" />
  </div>

  <div class="mapping-entry">
    <p>
      <span editable-text="item.name" e-form="nameForm" buttons="no" onbeforesave="checkNewName(item, $data)" onaftersave="rename(item)" onshow="disableDrag()"
        onhide="enableDrag()" blur="submit"> {{item.name}} </span>
      <button class="button icon-edit" data-nodrag ng-click="nameForm.$show()" ng-show="!nameForm.$visible" />
      <button class="button icon-check" data-nodrag ng-click="nameForm.$submit()" ng-show="nameForm.$visible" />
      <button class="button icon-close" data-nodrag ng-click="nameForm.$cancel()" ng-show="nameForm.$visible" />
    </p>

    <div class="mapping-definition">
      <dl ng-if="item.isMapping">
        <dt>match</dt>
        <dd>
          <span editable-text="item.match" e-form="matchForm" buttons="no" onaftersave="saveMapping(item)" onshow="disableDrag()" onhide="enableDrag()"
            blur="submit"> {{item.match}} </span>
          <button class="button icon-edit" data-nodrag ng-click="matchForm.$show()" ng-show="!matchForm.$visible" />
          <button class="button icon-check" data-nodrag ng-click="matchForm.$submit()" ng-show="matchForm.$visible" />
          <button class="button icon-close" data-nodrag ng-click="matchForm.$cancel()" ng-show="matchForm.$visible" />
        </dd>
        <dt>internalRedirect</dt>
        <dd ng-repeat="(i, r) in item.internalRedirect track by i">
          <span editable-text="item.internalRedirect[i]" e-form="internalRedirectForm" buttons="no" onaftersave="saveMapping(item)" onshow="disableDrag()"
            onhide="enableDrag()" blur="submit"> {{r}} </span>
          <button class="button icon-edit" data-nodrag ng-click="internalRedirectForm.$show()" ng-show="!internalRedirectForm.$visible" />
          <button class="button icon-check" data-nodrag ng-click="internalRedirectForm.$submit()" ng-show="internalRedirectForm.$visible" />
          <button class="button icon-close" data-nodrag ng-click="internalRedirectForm.$cancel()" ng-show="internalRedirectForm.$visible" />

          <button class="button icon-arrowup" data-nodrag ng-click="redirectUp(item, i)" ng-show="!internalRedirectForm.$visible && !$first" />
          <button class="button icon-arrowdown" data-nodrag ng-click="redirectDown(item, i)" ng-show="!internalRedirectForm.$visible && !$last" />
          <button class="button icon-delete" data-nodrag ng-click="deleteRedirect(item, i)" ng-show="!internalRedirectForm.$visible" />
        </dd>
        <dd>
          <button type="button" class="button icon-add" data-nodrag ng-click="addRedirect(item)" />
        </dd>
      </dl>
      <dl ng-if="item.isMapping">
        <dt>redirect</dt>
        <dd>
          <span editable-text="item.redirect" e-form="redirectForm" buttons="no" onaftersave="saveMapping(item)" onshow="disableDrag()" onhide="enableDrag()"
            blur="submit"> {{item.redirect}} </span>
          <button class="button icon-edit" data-nodrag ng-click="redirectForm.$show()" ng-show="!redirectForm.$visible" />
          <button class="button icon-check" data-nodrag ng-click="redirectForm.$submit()" ng-show="redirectForm.$visible" />
          <button class="button icon-close" data-nodrag ng-click="redirectForm.$cancel()" ng-show="redirectForm.$visible" />
        </dd>
        <dt>status</dt>
        <dd>
          <span editable-text="item.status" e-form="statusForm" buttons="no" onaftersave="saveMapping(item)" onshow="disableDrag()" onhide="enableDrag()"
            blur="submit"> {{item.status}} </span>
          <button class="button icon-edit" data-nodrag ng-click="statusForm.$show()" ng-show="!statusForm.$visible" />
          <button class="button icon-check" data-nodrag ng-click="statusForm.$submit()" ng-show="statusForm.$visible" />
          <button class="button icon-close" data-nodrag ng-click="statusForm.$cancel()" ng-show="statusForm.$visible" />
        </dd>
      </dl>
    </div>
  </div>
</div>
<ul ui-tree-nodes="options" ng-model="item.items" ng-class="{hidden: collapsed}">
  <li ng-repeat="item in item.items" ui-tree-node ng-include="'items_renderer.html'"></li>
</ul>
  </script>
  <div ui-tree="options" data-drag-enabled="dragEnabled" callbacks="treeCallbacks">
    <ul ui-tree-nodes ng-model="mappings">
      <li ng-repeat="item in mappings" ui-tree-node ng-include="'items_renderer.html'"></li>
    </ul>
  </div>
  <div class="alert error" ng-show="errorMessage">
    {{errorMessage}}
  </div>
</div>