<%@include file="/libs/foundation/global.jsp" %>
<!doctype html>
<html ng-app="cartyApp">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">

    <title>Carty</title>

    <cq:includeClientLib css="carty"/>
    <cq:includeClientLib js="carty"/>

    <script>
        window.globalSettings = {
            apiPath: '${currentPage.path}/api',
        };
    </script>
</head>

<body class="coral--light">
<div id="carty-app">

    <header class="top coral-Shell-header">

        <div class="logo">
            <a class="coral-Heading coral-Heading--2" href="/">
                <coral-icon icon="adobeMarketingCloud"></coral-icon>
            </a>
        </div>

        <nav class="crumbs coral-Shell-homeAnchor-label">
            <a class="coral-Heading coral-Heading--2" href="/miscadmin">Tools</a>
            <coral-icon icon="chevronRight"></coral-icon>
            <a class="coral-Heading coral-Heading--2" href="${pagePath}.html">Carty</a>
        </nav>
    </header>

    <div role="main">
        <div class="content">
            <div class="content-container">
                <div class="content-container-inner">
                    <coral-alert size="L" variant="{{flash.class}}" ng-show="flash" id="top" size="L">
                        <coral-alert-header>
                            <div class="flexible-parent">
                                <div class="flexible">{{flash.title}}</div>
                                <button class="alert-close-button" is="coral-button" variant="minimal"
                                        coral-close icon="close" ng-click="flash = null"></button>
                            </div>
                        </coral-alert-header>
                        <coral-alert-content>{{flash.msg}}</coral-alert-content>
                    </coral-alert>

                    <img id="logo" src="/apps/carty/clientlibs/carty/img/logo-carty.png"/>

                    <section class="resolve-map">
                        <h2 class="coral-Heading coral-Heading--2">Resolve & map</h2>
                        <%@include file="includes/resolve.jsp" %>
                    </section>

                    <section>
                        <h2 class="coral-Heading coral-Heading--2">Mappings</h2>
                        <p>Review and edit mappings</p>
                        <%@include file="includes/tree.jsp" %>
                    </section>

                    <section>
                        <h2 class="coral-Heading coral-Heading--2">Add new domain</h2>
                        <p>Create a set of mappings necessary to handle a domain.</p>
                        <%@include file="includes/template.jsp" %>
                    </section>

                    <section>
                        <h2 class="coral-Heading coral-Heading--2">Settings</h2>
                        <p>Change mappings settings</p>
                        <%@include file="includes/settings.jsp" %>
                    </section>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>