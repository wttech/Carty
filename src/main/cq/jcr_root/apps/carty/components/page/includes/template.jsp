<div ng-controller="TemplateCtrl">
  <form ng-submit="createFromTemplate()" class="settings">
    <div class="form-row">
      <h4>Content root</h4>
      <span>
        <input type="text" name="contentRoot" ng-model="form.contentRoot" placeholder="/content/geometrixx/en" />
      </span>
    </div>
    <div class="form-row">
      <h4>Domain name</h4>
      <span>
        <input type="text" name="domainName" ng-model="form.domainName" placeholder="geometrixx.com" />
      </span>
    </div>
    <div class="form-row">
      <div class="form-left-cell">&nbsp;</div>
      <button type="submit" class="primary">Create mappings</button>
    </div>
  </form>
</div>
