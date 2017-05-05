<div ng-controller="SettingsCtrl">
  <form novalidate ng-submit="changeSettings()" class="settings">
    <div class="form-row">
      <h4>Mappings root</h4>
      <span>
        <input type="text" name="mappingsRoot" class="coral-Textfield" ng-model="settingsForm.mappingsRoot" placeholder="Mappings root [ Default: /etc/map ]" />
      </span>
    </div>
    <div class="form-row">
      <div class="form-left-cell">&nbsp;</div>
      <button type="submit" class="coral-Button coral-Button--primary">Submit</button>
    </div>
  </form>
</div>
