<div ng-controller="TemplateCtrl">
    <form ng-submit="createFromTemplate()" class="settings">
        <div class="form-row">
            <h4>Content root</h4>
      <span>
        <input type="text" name="contentRoot" class="coral-Textfield" ng-model="form.contentRoot"
               placeholder="/content/geometrixx/en"/>
      </span>
        </div>
        <div class="form-row">
            <h4>Domain name</h4>
      <span>
        <input type="text" name="domainName" class="coral-Textfield" ng-model="form.domainName"
               placeholder="geometrixx.com"/>
      </span>
        </div>
        <div class="form-row">
            <div class="form-left-cell">&nbsp;</div>
            <button type="submit" class="coral-Button coral-Button--primary">Create mappings</button>
        </div>
    </form>
</div>
