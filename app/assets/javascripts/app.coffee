dependencies = [
    'ngRoute',
    'ui.bootstrap',
    'myApp.filters',
    'myApp.services',
    'myApp.controllers',
    'myApp.directives',
    'myApp.common',
    'myApp.routeConfig',
    'xeditable'
]

dependencies2 = [
  'ngRoute',
  'ui.bootstrap',
  'workl.filters',
  'workl.services',
  'workl.controllers',
  'workl.directives',
  'workl.common',
  'workl.routeConfig',
  'xeditable',
  'angularUtils.directives.dirPagination'
]

dependencies3 = [
  'ngRoute',
  'ui.bootstrap',
  'totallogs.filters',
  'totallogs.services',
  'totallogs.controllers',
  'totallogs.directives',
  'totallogs.common',
  'totallogs.routeConfig',
  'angularUtils.directives.dirPagination'
]

dependencies4 = [
  'ngRoute',
  'ui.bootstrap',
  'projectapp.filters',
  'projectapp.services',
  'projectapp.controllers',
  'projectapp.directives',
  'projectapp.common',
  'projectapp.routeConfig',
  'angularUtils.directives.dirPagination'
]

dependencies5 = [
  'ngRoute',
  'ui.bootstrap',
  'mhours.filters',
  'mhours.services',
  'mhours.controllers',
  'mhours.directives',
  'mhours.common',
  'mhours.routeConfig',
  'angularUtils.directives.dirPagination'
]

app = angular.module('myApp', dependencies)

app.run (editableOptions) ->
  editableOptions.theme = "bs3"

angular.module('workl', dependencies2)

angular.module('totallogs', dependencies3)

angular.module('projectapp', dependencies4)

angular.module('mhours', dependencies5)

angular.module('myApp.routeConfig', ['ngRoute'])
    .config ($routeProvider) ->
        $routeProvider
            .when('/', {
                templateUrl: '/assets/partial/userview.html'
            })
            .when('/users/create', {
                templateUrl: '/assets/partial/usercreate.html'
            })
            .otherwise({redirectTo: '/'})

angular.module('totallogs.routeConfig', ['ngRoute'])
    .config ($routeProvider) ->
        $routeProvider
            .when('/', {
                templateUrl: '/assets/partial/totallogsview.html'
            })
            .otherwise({redirectTo: '/'})

angular.module('workl.routeConfig', ['ngRoute'])
    .config ($routeProvider) ->
        $routeProvider
            .when('/', {
                  templateUrl: '/assets/partial/worklogview.html'
                })
            .when('/worklogs/create', {
                  templateUrl: '/assets/partial/worklogcreate.html'
                })
            .when('/worklogs/edit/:worklog', {
                templateUrl: '/assets/partial/worklogedit.html'
              })
            .when('/worklogs/batch', {
                templateUrl: '/assets/partial/worklogbatch.html'
              })
            .otherwise({redirectTo: '/'})

angular.module('projectapp.routeConfig', ['ngRoute'])
    .config ($routeProvider) ->
        $routeProvider
            .when('/', {
                templateUrl: '/assets/partial/projectview.html'
              })
            .when('/client/create', {
                templateUrl: '/assets/partial/projectclientcreate.html'
              })
            .when('/name/create', {
                templateUrl: '/assets/partial/projectnamecreate.html'
              })
            .when('/component/create', {
                templateUrl: '/assets/partial/projectcomponentcreate.html'
              })
            .otherwise({redirectTo: '/'})

angular.module('mhours.routeConfig', ['ngRoute'])
    .config ($routeProvider) ->
      $routeProvider
      .when('/', {
          templateUrl: '/assets/partial/missinghoursview.html'
        })
      .otherwise({redirectTo: '/'})


@commonModule = angular.module('myApp.common', [])
@controllersModule = angular.module('myApp.controllers', [])
@servicesModule = angular.module('myApp.services', [])
@modelsModule = angular.module('myApp.models', [])
@directivesModule = angular.module('myApp.directives', [])
@filtersModule = angular.module('myApp.filters', [])

@filtersModule2 = angular.module('workl.filters', [])
@servicesModule2 = angular.module('workl.services', [])
@controllersModule2 = angular.module('workl.controllers', [])
@directivesModule2 = angular.module('workl.directives', [])
@commonModule2 = angular.module('workl.common', [])
@modelsModule2 = angular.module('workl.models', [])

@filtersModule3 = angular.module('totallogs.filters', [])
@servicesModule3 = angular.module('totallogs.services', [])
@controllersModule3 = angular.module('totallogs.controllers', [])
@directivesModule3 = angular.module('totallogs.directives', [])
@commonModule3 = angular.module('totallogs.common', [])
@modelsModule3 = angular.module('totallogs.models', [])

@filtersModule4 = angular.module('projectapp.filters', [])
@servicesModule4 = angular.module('projectapp.services', [])
@controllersModule4 = angular.module('projectapp.controllers', [])
@directivesModule4 = angular.module('projectapp.directives', [])
@commonModule4 = angular.module('projectapp.common', [])
@modelsModule4 = angular.module('projectapp.models', [])

@filtersModule5 = angular.module('mhours.filters', [])
@servicesModule5 = angular.module('mhours.services', [])
@controllersModule5 = angular.module('mhours.controllers', [])
@directivesModule5 = angular.module('mhours.directives', [])
@commonModule5 = angular.module('mhours.common', [])
@modelsModule5 = angular.module('mhours.models', [])