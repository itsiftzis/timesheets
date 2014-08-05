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
  'xeditable'
]

app = angular.module('myApp', dependencies)

app.run (editableOptions) ->
  editableOptions.theme = "bs3"

angular.module('workl', dependencies2)

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

angular.module('workl.routeConfig', ['ngRoute'])
    .config ($routeProvider) ->
        $routeProvider
            .when('/', {
                  templateUrl: '/assets/partial/worklogview.html'
                })
            .when('/worklogs/create', {
                  templateUrl: '/assets/partial/worklogcreate.html'
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